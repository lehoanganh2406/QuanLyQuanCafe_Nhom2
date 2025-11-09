package GUI;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.*;

import connectDB.ConnectDB;
import dao.ChiTietHoaDon_DAO;
import dao.HoaDon_DAO;
import dao.KhachHang_DAO;
import entity.Ban;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;
import entity.NhanVien;
import entity.SanPham;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.Timestamp;
import java.util.List;
import java.util.ArrayList;

public class ThanhToan_GUI extends JFrame implements ActionListener, KeyListener {
    private int soBan;
    private JButton btnMenu;
    private JLabel lblOrder;
    private JLabel lblThongTinKhachHang;
    private JTextField txtTruDiem, txtGiamGia;
    private JLabel lblTongThanhToan;
    private final ImageIcon iconQuayLai = new ImageIcon(getClass().getResource("/img/back_16.png"));
    private final ImageIcon iconThanhToan = new ImageIcon(getClass().getResource("/img/bill_16.png"));
    private Order_GUI orderGui;
    private Timestamp thoiGianVao;

    // Bảng chính + footer
    private JTable bangMon, tblFooter;
    private DefaultTableModel modelBang, footerModel;
    private JScrollPane spMain, spFooter;
    private JPanel tableStack;

    // Cột phải
    private JTextField txtTienKhachTra;
    private JLabel lblTienThua;
    private JComboBox<String> cboPhuongThuc;
    private JButton thanhToan, nutThemKhach, nutQuayLai;
	private JLabel lblMaHoaDon;
	private HoaDon_DAO hoaDonDAO;
	private KhachHang khachHangHienTai;
	private NhanVien nhanVienDangNhap;
	private ChiTietHoaDon_DAO chiTietHD;
	private KhachHang_DAO khachHang_DAO;
	private Timestamp tGianRa;
	
    
    public static void main(String[] args) {
        ConnectDB.getInstance().connect();
        SwingUtilities.invokeLater(() -> new ThanhToan_GUI(null, 1, new ArrayList<>(), 0L).setVisible(true));
    }
    public ThanhToan_GUI(Order_GUI orderGui, int soBan, ArrayList<Object[]> cartRows, Long tongTien) {
        this.soBan = soBan;
        this.orderGui = orderGui;
        this.thoiGianVao = Ban_GUI.thoiGianVao.getOrDefault(
                soBan, new Timestamp(System.currentTimeMillis()));
        if (cartRows == null) cartRows = new ArrayList<>();
        if (tongTien == null) tongTien = 0L;

        setTitle("Menu - Bàn " + soBan);
        setSize(1650, 1024);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        thanhTieuDe();
        taoCenter();

        // Nạp dữ liệu giỏ hàng (mặc định)
        napDuLieuGioHang(cartRows);
        capNhatTongThanhToan();
        updateFooterSum();
        
     // Action
        nutThemKhach.addActionListener(this);
        nutQuayLai.addActionListener(this);
        thanhToan.addActionListener(this);
        cboPhuongThuc.addActionListener(this);

        // Key – recalculation cho 3 ô
        txtTruDiem.addKeyListener(this);
        txtGiamGia.addKeyListener(this);
        txtTienKhachTra.addKeyListener(this);

    }
    

    // Thanh tiêu đề
    private void thanhTieuDe() {
    	PanelTieuDe panel = new PanelTieuDe("Thanh Toán", "/img/thanhtoan.png");
        add(panel, BorderLayout.NORTH);
    }

    // Center
    private void taoCenter() {
        JPanel trungTam = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 12));
        trungTam.setOpaque(false);

        // ===== Cột trái
        JPanel cotTrai = new JPanel(new BorderLayout(0,12));
        cotTrai.setOpaque(false);
        cotTrai.setPreferredSize(new Dimension(1200, 900));

        // Top info
        JPanel thongTinTren = new JPanel();
        thongTinTren.setOpaque(false);
        thongTinTren.setLayout(new BoxLayout(thongTinTren, BoxLayout.Y_AXIS));
        thongTinTren.add(tieuDe("Tên bàn:", "Bàn " + soBan));
        thongTinTren.add(Box.createVerticalStrut(6));
        lblThongTinKhachHang = tieuDe("Mã khách hàng:", "— Chưa chọn —");
        thongTinTren.add(lblThongTinKhachHang);
        thongTinTren.add(Box.createVerticalStrut(8));
        nutThemKhach = taoNut("Thêm thông tin khách hàng");
        nutThemKhach.setAlignmentX(Component.LEFT_ALIGNMENT);
        thongTinTren.add(nutThemKhach);
        thongTinTren.add(Box.createVerticalStrut(8));
        JTextArea ghiChu = new JTextArea(
            "Ghi chú:\n" +
            "- Cách quy đổi điểm tích lũy thành tiền thanh toán:\n" +
            "  • 1 điểm = 1.000 VND\n" +
            "- Cách tính điểm:\n" +
            "  • 10.000 VND = 1 điểm");
        ghiChu.setEditable(false);
        ghiChu.setOpaque(false);
        ghiChu.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        ghiChu.setAlignmentX(Component.LEFT_ALIGNMENT);
        thongTinTren.setBorder(BorderFactory.createEmptyBorder(0, 200, 0, 0));
        thongTinTren.add(ghiChu);

        // ===== Bảng chính
        modelBang = new DefaultTableModel(new Object[]{"Mã","Tên món","Số lượng","Đơn Giá","Thành tiền"}, 0);

        bangMon = new JTable(modelBang);
        bangMon.setEnabled(false);
        bangMon.setRowHeight(36);
        bangMon.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        bangMon.setFillsViewportHeight(true);
        JTableHeader header = bangMon.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 48));
        header.setBackground(new Color(245,239,234));
        header.setFont(new Font("Times New Roman", Font.BOLD, 18));
        header.setBorder(BorderFactory.createMatteBorder(1,1,1,1,new Color(185,167,156)));

        VNDRenderer vndR = new VNDRenderer();
        bangMon.getColumnModel().getColumn(3).setCellRenderer(vndR);
        bangMon.getColumnModel().getColumn(4).setCellRenderer(vndR);

        spMain = new JScrollPane(bangMon);
        spMain.setBorder(BorderFactory.createLineBorder(new Color(185,167,156)));
        spMain.setPreferredSize(new Dimension(1010, 900));

        // ===== Footer (1 dòng tổng) – cố định
        footerModel = new DefaultTableModel(new Object[]{"Mã","Tên món","Số lượng","Đơn Giá","Thành tiền"}, 0) ;

        tblFooter = new JTable(footerModel);
        tblFooter.setTableHeader(null);
        tblFooter.setEnabled(false);
        tblFooter.setRowHeight(bangMon.getRowHeight());


        spFooter = new JScrollPane(tblFooter);
        spFooter.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        spFooter.setBorder(BorderFactory.createEmptyBorder());
        spFooter.setPreferredSize(new Dimension(1010, 50));

        // Ghép main + footer
        tableStack = new JPanel(new BorderLayout());
        tableStack.add(spMain,   BorderLayout.CENTER);
        tableStack.add(spFooter, BorderLayout.SOUTH);

        // Nút quay lại
        nutQuayLai = taoNut("Quay lại", iconQuayLai);
        JPanel dayTrai = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dayTrai.setOpaque(false);
        dayTrai.add(nutQuayLai);

        // Ghép cột trái
        cotTrai.add(thongTinTren, BorderLayout.NORTH);
        cotTrai.add(tableStack,   BorderLayout.CENTER);  // chỉ add stack
        cotTrai.add(dayTrai,      BorderLayout.SOUTH);

        // ===== Cột phải (thông tin/giảm giá)
        JPanel cotPhai = new JPanel(new BorderLayout());
        cotPhai.setOpaque(false);
        JPanel phanNor = new JPanel();
        phanNor.setOpaque(false);
        phanNor.setLayout(new BoxLayout(phanNor, BoxLayout.Y_AXIS));
        
        hoaDonDAO = new HoaDon_DAO();
        lblMaHoaDon = new JLabel(hoaDonDAO.layMaHoaDon());
        lblMaHoaDon.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        phanNor.add(hangDong("Mã hóa đơn:", lblMaHoaDon));
        phanNor.add(Box.createVerticalStrut(8));
        phanNor.add(tieuDeDonGian("Mã nhân viên:"));
        phanNor.add(Box.createVerticalStrut(16));

        // Ô text nhập trực tiếp
        txtTruDiem = taoOText("Nhập điểm tích lũy...");
        txtGiamGia = taoOText("Nhập % giảm giá...");
        setNumericFilter(txtTruDiem, 0, Integer.MAX_VALUE);
        setNumericFilter(txtGiamGia, 0, 100);

        phanNor.add(hangNhap("Trừ điểm:", txtTruDiem));
        phanNor.add(Box.createVerticalStrut(10));
        phanNor.add(hangNhap("Giảm giá:", txtGiamGia));
        phanNor.add(Box.createVerticalStrut(16));

        lblTongThanhToan = new JLabel("0 VND");
        lblTongThanhToan.setFont(new Font("Times New Roman", Font.BOLD, 22));
        phanNor.add(hangDong("Tổng thanh toán:", lblTongThanhToan));

        txtTienKhachTra = taoOText("Nhập tiền khách trả...");
        setNumericFilter(txtTienKhachTra, 0, -1);
        phanNor.add(Box.createVerticalStrut(10));
        phanNor.add(hangNhap("Tiền khách trả:", txtTienKhachTra));
        phanNor.add(Box.createVerticalStrut(10));

        // Label tiền thừa
        lblTienThua = new JLabel("0 VND");
        lblTienThua.setFont(new Font("Times New Roman", Font.BOLD, 22));
        phanNor.add(hangDong("Tiền thừa trả khách:", lblTienThua));


        cotPhai.add(phanNor, BorderLayout.NORTH);
        cotPhai.add(taoPhanSouthPhai(), BorderLayout.SOUTH);

        // Đặt 2 cột
        trungTam.add(cotTrai);
        trungTam.add(cotPhai);
        add(trungTam, BorderLayout.CENTER);
        
     // Nền + font footer
        DefaultTableCellRenderer footerRenderer = new DefaultTableCellRenderer() {
            @Override public Component getTableCellRendererComponent(JTable table, Object value,
                                                                     boolean isSelected, boolean hasFocus,
                                                                     int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setFont(new Font("Times New Roman", Font.BOLD, 22));
                c.setBackground(new Color(255,248,220));
                c.setOpaque(true);
                c.setHorizontalAlignment((column >= 2) ? SwingConstants.RIGHT : SwingConstants.LEFT);
                if (value instanceof Number && (column == 3 || column == 4)) {
                    c.setText(dinhDangVND(((Number) value).longValue()));
                }
                return c;
            }
        };
        for (int i = 0; i < tblFooter.getColumnCount(); i++) {
            tblFooter.getColumnModel().getColumn(i).setCellRenderer(footerRenderer);
        }
        
     // Tự cập nhật footer & tổng tiền khi dữ liệu bảng thay đổi
        modelBang.addTableModelListener(e -> {
            updateFooterSum();
            capNhatTongThanhToan();
        });

        txtTienKhachTra.addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) {
                String s = txtTienKhachTra.getText().trim().replace(".", "");
                if (s.isEmpty()) return;
                try {
                    long val = Long.parseLong(s);
                    txtTienKhachTra.setText(String.valueOf(val)); // để capNhat… format VND ra label
                    capNhatTongThanhToan();
                } catch (NumberFormatException ex) {
                    txtTienKhachTra.setText("");
                    capNhatTongThanhToan();
                }
            }
        });
    }

    // JLabel tiêu đề
    private JLabel tieuDe(String nhan, String giaTri) {
        JLabel lbl = new JLabel(nhan + "   " + giaTri);
        lbl.setFont(new Font("Times New Roman", Font.BOLD, 28));
        return lbl;
    }
    private JButton taoNut(String ten, ImageIcon icon) {
        JButton btn = new JButton(ten, icon);
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Times New Roman", Font.BOLD, 20));
        return btn;
    }
    private JButton taoNut(String ten) { return taoNut(ten, null); 
    }
    private JPanel tieuDeDonGian(String nhan) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.setOpaque(false);
        JLabel l1 = new JLabel(nhan);
        l1.setFont(new Font("Times New Roman", Font.BOLD, 22));
        p.add(l1);
        return p;
    }

    // Hàng nhập text (label + textfield)
    private JPanel hangNhap(String nhan, JTextField txt) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p.setOpaque(false);
        JLabel l = new JLabel(nhan);
        l.setFont(new Font("Times New Roman", Font.BOLD, 22));
        p.add(l);
        p.add(txt);
        return p;
    }

    // ô text
    private JTextField taoOText(String text) {
        JTextField tf = new JTextField();

        tf.setPreferredSize(new Dimension(200, 36));
        tf.setFont(new Font("Times New Roman", Font.PLAIN, 16));
        tf.setBackground(Color.WHITE);
        tf.setForeground(Color.GRAY);
        tf.setText(text);
        tf.setHorizontalAlignment(SwingConstants.LEFT);

        Color line = new Color(180,180,180);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(line, 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));

        tf.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (tf.getText().equals(text)) tf.setText("");
                tf.setForeground(Color.BLACK);
                tf.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(140,140,140), 1),
                        BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            }
            @Override public void focusLost(FocusEvent e) {
                if (tf.getText().trim().isEmpty()) {
                    tf.setText(text);
                    tf.setForeground(Color.GRAY);
                } else tf.setForeground(Color.BLACK);
                tf.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(line, 1),
                        BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            }
        });
        return tf;
    }

 // Thiết lập filter để chỉ cho phép nhập số nguyên trong phạm vi min và max
    private void setNumericFilter(JTextField field, int min, int max) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                replace(fb, offset, 0, string, attr);
            }
            @Override public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                String old = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = old.substring(0, offset) + (text == null ? "" : text) + old.substring(offset + length);
                if (newText.isEmpty() || newText.matches("\\d+")) {
                    if (newText.isEmpty()) { super.replace(fb, offset, length, text, attrs); return; }
                    try {
                        long v = Long.parseLong(newText);
                        boolean okMin = v >= min;
                        boolean okMax = (max < 0) || (v <= max);
                        if (okMin && okMax) super.replace(fb, offset, length, text, attrs);
                    } catch (NumberFormatException ignored) {}
                }
            }
        });
    }

 
    private static class VNDRenderer extends DefaultTableCellRenderer {
        @Override protected void setValue(Object value) {
            if (value instanceof Number n) {
                long v = n.longValue();
                setText(String.format("%,d", v).replace(',', '.'));
            } else {
                setText(value == null ? "" : value.toString());
            }
            setHorizontalAlignment(SwingConstants.RIGHT);
        }
    }

    // nập dữ liệu bảng bên order vào thanh toán
    private void napDuLieuGioHang(List<Object[]> cartRows) {
        modelBang.setRowCount(0);
        for (Object[] row : cartRows) modelBang.addRow(row);
    }

    private String dinhDangVND(double vnd) {
        long rounded = Math.round(vnd);
        return String.format("%,d", rounded).replace(',', '.') + " VND";
    }


    private double parseVND(String s) {
        if (s == null) return 0.0;
        s = s.replace(".", "").replace(" VND", "").trim();
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private int safeInt(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }
    private double safeDouble(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return 0.0;
        }
    }

    private void capNhatTongThanhToan() {
        // 1) Tổng gốc
        double tongGoc = 0.0;
        for (int i = 0; i < modelBang.getRowCount(); i++) {
            Object val = modelBang.getValueAt(i, 4);
            if (val instanceof Number n) {
                tongGoc += n.doubleValue();
            }
        }

        // 2) Giảm giá & trừ điểm
        int tru  = safeInt(txtTruDiem.getText());
        int giam = safeInt(txtGiamGia.getText());
        if (giam > 100) giam = 100;
        if (giam < 0)   giam = 0;
        if (tru  < 0)   tru  = 0;

        double sauPhanTram = tongGoc * (1.0 - (giam / 100.0));
        double tongCuoi = Math.max(0.0, sauPhanTram - tru * 1000.0);
        lblTongThanhToan.setText(dinhDangVND(tongCuoi));

        // 3) Theo phương thức thanh toán
        boolean laTienMat = (cboPhuongThuc == null)
                || "Tiền mặt".equals(cboPhuongThuc.getSelectedItem());

        double tra;
        if (laTienMat) {
            txtTienKhachTra.setEditable(true);
            txtTienKhachTra.setForeground(Color.BLACK);
            tra = safeDouble(txtTienKhachTra.getText());
        } else {
            txtTienKhachTra.setEditable(false);
            txtTienKhachTra.setForeground(Color.BLUE);
            txtTienKhachTra.setText(String.valueOf(Math.round(tongCuoi)));
            tra = tongCuoi;
        }

        // 4) Tiền thừa
        double thua = tra - tongCuoi;
        lblTienThua.setText(dinhDangVND(thua));
        lblTienThua.setForeground(thua < 0 ? Color.RED : Color.BLACK);
    }


    /** Tính tổng SL/Thành tiền cho footer (1 dòng) */
    private void updateFooterSum() {
        if (footerModel == null) return;

        footerModel.setRowCount(0);
        int  sumSL = 0;
        long sumTT = 0L;

        for (int r = 0; r < modelBang.getRowCount(); r++) {
            Number sl = (Number) modelBang.getValueAt(r, 2);
            Number tt = (Number) modelBang.getValueAt(r, 4);
            if (sl != null) sumSL += sl.intValue();
            if (tt != null) sumTT += tt.longValue();
        }

        // Cột 0: "", cột 1: "TỔNG", cột 2: sumSL (Integer), cột 3: 0L, cột 4: sumTT (Long)
        footerModel.addRow(new Object[]{"", "TỔNG", sumSL, 0L, sumTT});
    }


    private JPanel hangDong(String nhan, JComponent comp) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p.setOpaque(false);
        JLabel l = new JLabel(nhan);
        l.setFont(new Font("Times New Roman", Font.BOLD, 22));
        if (comp instanceof JLabel lbl) lbl.setHorizontalAlignment(SwingConstants.LEFT);
        p.add(l);
        p.add(comp);
        return p;
    }

    private JPanel taoPhanSouthPhai() {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBorder(BorderFactory.createEmptyBorder(16, 0, 0, 0));

        // === Row 1: Phương thức + ComboBox
        JPanel rowPM = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        rowPM.setOpaque(false);

        JLabel lbl = new JLabel("Phương thức:");
        lbl.setFont(new Font("Times New Roman", Font.BOLD, 22));

        cboPhuongThuc = new JComboBox<>(new String[]{ "Tiền mặt", "Chuyển khoản", "Visa" });
        cboPhuongThuc.setPreferredSize(new Dimension(200, 36));
        cboPhuongThuc.setFont(new Font("Times New Roman", Font.PLAIN, 18));

        rowPM.add(lbl);
        rowPM.add(cboPhuongThuc);

        // === Tạo khoảng cách giữa 2 hàng
        p.add(rowPM);
        p.add(Box.createVerticalStrut(150));

        // === Row 2: Nút Thanh Toán (căn phải)
        JPanel rowBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rowBtn.setOpaque(false);

        thanhToan = taoNut("Thanh Toán", iconThanhToan);
        thanhToan.setPreferredSize(new Dimension(200, 70));
        rowBtn.add(thanhToan);

        p.add(rowBtn);
        return p;
    }
//    Nhận KH từ trang chọn và đổ xuống UI (label + giới hạn ô Trừ điểm) 
    private void capNhatThongTinKhachHang(KhachHang kh) {
        if (kh == null) return;
        this.khachHangHienTai = kh;
        // Cập nhật label “Mã khách hàng: …”
        lblThongTinKhachHang.setText(String.format("Mã khách hàng: %s — %s (Điểm TL: %d)",
                kh.getMaKH(), kh.getTenKH(), kh.getDiemTL()));

        // Giới hạn ô Trừ điểm theo điểm tích lũy hiện có
        setNumericFilter(txtTruDiem, 0, kh.getDiemTL());      // thay max động = điểm KH
        txtTruDiem.setText("0");                               // reset về 0 cho chắc
        txtTruDiem.setToolTipText("Tối đa " + kh.getDiemTL() + " điểm");
        ToolTipManager ttm = ToolTipManager.sharedInstance();
        ttm.setInitialDelay(0);     // hiện ngay lập tức
        ttm.registerComponent(txtTruDiem);
        // Tính lại tổng + tiền thừa
        capNhatTongThanhToan();
    }
    private void capNhatDiemTichLuySauThanhToan() {
        if (khachHangHienTai == null) return; // không chọn KH thì bỏ qua

        int diemDangCo = khachHangHienTai.getDiemTL();
        int diemDaDung = safeInt(txtTruDiem.getText());       // đã giới hạn bởi setNumericFilter
        double soTienThucTra = parseVND(lblTongThanhToan.getText()); // sau giảm %, sau trừ điểm

        // Quy đổi: 10.000 VND = 1 điểm
        int diemCong = (int) (soTienThucTra / 10_000L);

        int diemMoi = Math.max(0, diemDangCo - diemDaDung + diemCong);
        khachHang_DAO = new KhachHang_DAO();

        if (khachHang_DAO.updateDiem(khachHangHienTai.getMaKH(), diemMoi)) {
            khachHangHienTai.setDiemTL(diemMoi);
        } else {
            JOptionPane.showMessageDialog(this,
                "Không thể cập nhật điểm tích lũy cho khách hàng.",
                "Cảnh báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    private void nutThanhToan() {
    	double tienThua = parseVND(lblTienThua.getText());
        if (tienThua < 0) {
            JOptionPane.showMessageDialog(this,
                    "Tiền khách trả chưa đủ, không thể thanh toán!",
                    "Thiếu tiền", JOptionPane.WARNING_MESSAGE);
            txtTienKhachTra.requestFocus();
            return;
        }
        
		String maHD = lblMaHoaDon.getText();
		tGianRa = new Timestamp(System.currentTimeMillis());
		Ban banHienTai = new Ban(String.format("B%03d", soBan));
		if (nhanVienDangNhap == null) {
	        nhanVienDangNhap = new NhanVien("NV001"); // TODO: thay bằng NV đăng nhập thực
	    }
		int giamGia = safeInt(txtGiamGia.getText());
		double tongTien = parseVND(lblTongThanhToan.getText());
	    double tienKhach = parseVND(txtTienKhachTra.getText());
	    HoaDon hd = new HoaDon(maHD, banHienTai, khachHangHienTai, nhanVienDangNhap, thoiGianVao, tGianRa, true, giamGia, tongTien, tienKhach);
	    
	    if (hoaDonDAO.themHoaDon(hd)) {
			chiTietHD = new ChiTietHoaDon_DAO();
			boolean loiChiTiet = false;
			for (int i = 0; i < modelBang.getRowCount(); i++) {
				String maSP = (String) modelBang.getValueAt(i, 0);
				int soLuong = (int) modelBang.getValueAt(i, 2);
				ChiTietHoaDon ct = new ChiTietHoaDon(hd, new SanPham(maSP), nhanVienDangNhap, soLuong);
				if (!chiTietHD.themChiTiet(ct)) {
					loiChiTiet = true;
				}
			}
			if (!loiChiTiet) {
				capNhatDiemTichLuySauThanhToan();
				JOptionPane.showMessageDialog(this, "Thanh toán thành công");
				xuatHoaDonPDF(maHD);
				this.setVisible(false);
				orderGui.dispose();
				Ban_GUI.thoiGianVao.remove(soBan);
				new Ban_GUI().setVisible(true);
			
			}else {
				JOptionPane.showMessageDialog(this, "Thanh toán thất bại");
			}
		}else {
			JOptionPane.showMessageDialog(this, "Thanh toán thất bại");
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		Object o = e.getSource();
		if (o.equals(txtGiamGia) || o.equals(txtTruDiem) || o.equals(txtTienKhachTra)) {
			capNhatTongThanhToan();
		}
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(nutQuayLai)) {
			if (orderGui != null) orderGui.setVisible(true);
            dispose();
		}else if (o.equals(nutThemKhach)) {
			KhachHang_JDiglog trangKH = new KhachHang_JDiglog(SwingUtilities.getWindowAncestor(this));
	        trangKH.setLocationRelativeTo(this);
	        trangKH.setVisible(true);
	        KhachHang chon = trangKH.getSelected();
	        if (chon != null) {
	            capNhatThongTinKhachHang(chon);
	            txtTruDiem.requestFocus();
	        }
		}else if (o.equals(cboPhuongThuc)) {
			boolean laTienMat = "Tiền mặt".equals(cboPhuongThuc.getSelectedItem());
	        double tong = parseVND(lblTongThanhToan.getText());
	        if (!laTienMat) {
	        	txtTienKhachTra.setEditable(false);
	        	txtTienKhachTra.setForeground(Color.BLACK);
	            txtTienKhachTra.setText(String.valueOf(tong));
	        }else {
	        	txtTienKhachTra.setEditable(true);
	            txtTienKhachTra.setForeground(Color.BLUE);
			}
	        capNhatTongThanhToan();
		}else if (o.equals(thanhToan)) {
			nutThanhToan();
		}
		
	}
	
	private void xuatHoaDonPDF(String maHD) {
	    try {
	        // 1️ Lấy đường dẫn thư mục hiện tại đang chạy 
	        String currentDir = System.getProperty("user.dir");

	        // 2️ Tạo file PDF trong cùng thư mục
	        String outPath = currentDir + File.separator + "HoaDon_" + maHD + ".pdf";

	        // 3️ Đường dẫn font tiếng Việt 
	        String fontPath = "C:/Windows/Fonts/times.ttf";

	        // 4️ Xuất PDF dạng bảng
	        XuatPDF.xuatHoaDonPDF(
	        	    outPath,
	        	    fontPath,
	        	    maHD,
	        	    thoiGianVao,
	        	    tGianRa,
	        	    khachHangHienTai != null ? khachHangHienTai.getTenKH() : "Khách lẻ",
	        	    nhanVienDangNhap != null ? nhanVienDangNhap.getMaNV() : "",
	        	    modelBang,
	        	    (int) footerModel.getValueAt(0, 2),
	        	    ((Number) footerModel.getValueAt(0, 4)).doubleValue(),
	        	    safeInt(txtTruDiem.getText()),
	        	    safeInt(txtGiamGia.getText()),
	        	    parseVND(lblTongThanhToan.getText()),
	        	    safeDouble(txtTienKhachTra.getText()),
	        	    safeDouble(txtTienKhachTra.getText()) - parseVND(lblTongThanhToan.getText())
	        	);


	        // 5️⃣ Thông báo thành công
	        JOptionPane.showMessageDialog(this,
	            "Xuất hoá đơn PDF thành công!\nFile lưu tại:\n" + outPath);

	    } catch (Exception ex) {
	        ex.printStackTrace();
	        JOptionPane.showMessageDialog(this,
	            "Lỗi khi xuất PDF: " + ex.getMessage(),
	            "PDF Error", JOptionPane.ERROR_MESSAGE);
	    }
	}


}