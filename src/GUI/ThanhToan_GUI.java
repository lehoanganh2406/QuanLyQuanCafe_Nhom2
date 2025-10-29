package GUI;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import javax.swing.text.*;
import connectDB.ConnectDB;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class ThanhToan_GUI extends JFrame {
    private int soBan;
    private JButton btnMenu;
    private JLabel lblOrder;
    private JLabel lblThongTinKhachHang;
    private JTextField txtTruDiem, txtGiamGia;
    private JLabel lblTongThanhToan;
    private final ImageIcon iconQuayLai = new ImageIcon(getClass().getResource("/img/back_16.png"));
    private ImageIcon iconThanhToan = new ImageIcon(getClass().getResource("/img/bill_16.png"));
    private Order_GUI orderGui;

    // Bảng chính + footer
    private JTable bangMon;
    private JTable tblFooter;
    private DefaultTableModel modelBang;
    private DefaultTableModel footerModel;
    private JScrollPane spMain;
    private JScrollPane spFooter;
    private JPanel tableStack;
	private JTextField txtTienKhachTra;
	private JLabel lblTienThua;
	private JComboBox cboPhuongThuc;
	private JButton thanhToan;

    public ThanhToan_GUI(Order_GUI orderGui, int soBan, ArrayList<Object[]> cartRows, Long tongTien) {
        this.soBan = soBan;
        this.orderGui = orderGui;
        if (cartRows == null) cartRows = new ArrayList<>();
        if (tongTien == null) tongTien = 0L;

        setTitle("Menu - Bàn " + soBan);
        setSize(1650,1024);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        thanhTieuDe();
        taoCenter();

        // Nạp dữ liệu giỏ hàng (mặc định)
        napDuLieuGioHang(cartRows);
        capNhatTongThanhToan();
        updateFooterSum();
        syncFooterColumnWidths();
    }

    public static void main(String[] args) {
        ConnectDB.getInstance().connect();
        SwingUtilities.invokeLater(() -> new ThanhToan_GUI(null, 1, null, null).setVisible(true));
    }

    // =================== Header ===================
    private void thanhTieuDe() {
        JPanel jNor = new JPanel(new BorderLayout());
        jNor.setBackground(Color.decode("#865A52"));
        jNor.setPreferredSize(new Dimension(0,60));

        JPanel jNorLeft = new JPanel(new FlowLayout(FlowLayout.LEFT,10,8));
        jNorLeft.setOpaque(false);
        ImageIcon iconMenu = new ImageIcon(new ImageIcon(getClass().getResource("/img/iconMenu.png"))
                .getImage().getScaledInstance(49, 32, Image.SCALE_SMOOTH));
        jNorLeft.add(btnMenu = new JButton(iconMenu));
        btnMenu.setBackground(Color.decode("#865A52"));
        btnMenu.setBorderPainted(false);
        btnMenu.setFocusPainted(false);

        JPanel jNorCen = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        jNorCen.setOpaque(false);
        ImageIcon iconOrder = new ImageIcon(new ImageIcon(getClass().getResource("/img/thanhtoan.png"))
                .getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH));
        jNorCen.add(lblOrder = new JLabel("Thanh Toán", iconOrder, SwingConstants.CENTER));
        lblOrder.setBackground(Color.decode("#E3CFC1"));
        lblOrder.setFont(new Font("Times New Roman", Font.PLAIN, 32));
        lblOrder.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        lblOrder.setForeground(Color.BLACK);
        lblOrder.setOpaque(true);
        lblOrder.setIconTextGap(10);
        lblOrder.setHorizontalAlignment(SwingConstants.LEFT);

        jNor.add(jNorLeft, BorderLayout.WEST);
        jNor.add(jNorCen, BorderLayout.CENTER);
        add(jNor, BorderLayout.NORTH);
    }

    // =================== Center ===================
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
        JButton nutThemKhach = taoNut("Thêm thông tin khách hàng");
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
        modelBang = new DefaultTableModel(new Object[]{"Mã","Tên món","Số lượng","Đơn Giá","Thành tiền"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int c) {
                return switch (c) {
                    case 0, 2 -> Integer.class;
                    case 3, 4 -> Long.class;
                    default -> String.class;
                };
            }
        };
        bangMon = new JTable(modelBang);
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
        footerModel = new DefaultTableModel(new Object[]{"Mã","Tên món","Số lượng","Đơn Giá","Thành tiền"}, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int c) {
                return switch (c) {
                    case 0, 2 -> Integer.class;
                    case 3, 4 -> Long.class;
                    default -> String.class;
                };
            }
        };
        tblFooter = new JTable(footerModel);
        tblFooter.setTableHeader(null);
        tblFooter.setEnabled(false);
        tblFooter.setRowHeight(bangMon.getRowHeight());

        // Footer renderer: bold + nền + format VND nếu là số
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

        spFooter = new JScrollPane(tblFooter);
        spFooter.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        spFooter.setBorder(BorderFactory.createEmptyBorder());
        spFooter.setPreferredSize(new Dimension(1010, 50));

        // Ghép main + footer
        tableStack = new JPanel(new BorderLayout());
        tableStack.add(spMain,   BorderLayout.CENTER);
        tableStack.add(spFooter, BorderLayout.SOUTH);

        // Đồng bộ width cột footer với bảng chính
        bangMon.getColumnModel().addColumnModelListener(new TableColumnModelListener() {
            @Override public void columnMarginChanged(ChangeEvent e) { syncFooterColumnWidths(); }
            @Override public void columnMoved(TableColumnModelEvent e)   { syncFooterColumnWidths(); }
            @Override public void columnAdded(TableColumnModelEvent e)   { syncFooterColumnWidths(); }
            @Override public void columnRemoved(TableColumnModelEvent e) { syncFooterColumnWidths(); }
            @Override public void columnSelectionChanged(ListSelectionEvent e) {}
        });

        // Tự cập nhật footer & tổng tiền khi dữ liệu bảng thay đổi
        modelBang.addTableModelListener(e -> {
            updateFooterSum();
            capNhatTongThanhToan();
        });

        // Nút quay lại
        JButton nutQuayLai = taoNut("Quay lại", iconQuayLai);
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

        phanNor.add(tieuDeDonGian("Mã hóa đơn:"));
        phanNor.add(Box.createVerticalStrut(8));
        phanNor.add(tieuDeDonGian("Mã nhân viên:"));
        phanNor.add(Box.createVerticalStrut(16));

        // >>> Sửa theo yêu cầu: ô text nhập trực tiếp (không còn spinner, không label % rời)
        txtTruDiem = taoOText("Nhập điểm tích lũy...");     // số điểm, >= 0
        txtGiamGia = taoOText("Nhập % giảm giá...");     // % 0..100
        setNumericFilter(txtTruDiem, 0, Integer.MAX_VALUE);
        setNumericFilter(txtGiamGia, 0, 100);

        phanNor.add(hangNhap("Trừ điểm:", txtTruDiem));
        phanNor.add(Box.createVerticalStrut(10));
        phanNor.add(hangNhap("Giảm giá:",     txtGiamGia));
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

        KeyAdapter recalc = new KeyAdapter() {
            @Override public void keyReleased(KeyEvent e) { capNhatTongThanhToan(); }
        };
        txtTruDiem.addKeyListener(recalc);
        txtGiamGia.addKeyListener(recalc);
        txtTienKhachTra.addKeyListener(recalc);
        cotPhai.add(phanNor, BorderLayout.NORTH);
        cotPhai.add(taoPhanSouthPhai(), BorderLayout.SOUTH);
        txtTienKhachTra.addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) {
                String s = txtTienKhachTra.getText().trim().replace(".", "");
                if (s.isEmpty()) return;
                try {
                    long val = Long.parseLong(s);
                    txtTienKhachTra.setText(String.valueOf(val)); // để capNhat… format VND ra label
                    capNhatTongThanhToan();
                } catch (NumberFormatException ex) {
                    // nếu sai thì để trống
                    txtTienKhachTra.setText("");
                    capNhatTongThanhToan();
                }
            }
        });


        // Đặt 2 cột
        trungTam.add(cotTrai);
        trungTam.add(cotPhai);
        add(trungTam, BorderLayout.CENTER);

        nutQuayLai.addActionListener(e -> {
            if (orderGui != null) orderGui.setVisible(true);
            dispose();
        });
    }

    // =================== Helpers ===================
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
    private JButton taoNut(String ten) { return taoNut(ten, null); }
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


    // DocumentFilter số nguyên, với min/max (max < 0 => không giới hạn)
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

    private int safeInt(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }

    /** Nạp giỏ hàng vào bảng chính */
    private void napDuLieuGioHang(java.util.List<Object[]> cartRows) {
        modelBang.setRowCount(0);
        for (Object[] row : cartRows) modelBang.addRow(row);
    }

    private String dinhDangVND(long vnd) {
        return String.format("%,d", vnd).replace(',', '.') + " VND";
    }

    private class VNDRenderer extends DefaultTableCellRenderer {
        @Override
        protected void setValue(Object value) {
            if (value instanceof Number n) {
                long v = n.longValue();
                setText(String.format("%,d", v).replace(',', '.'));
            } else {
                setText("");
            }
            setHorizontalAlignment(SwingConstants.RIGHT);
        }
    }
    private long safeLong(String s) {
        try { return Long.parseLong(s.trim()); } catch (Exception e) { return 0L; }
    }

    private void capNhatTongThanhToan() {
    // 1) Tính tổng gốc từ bảng
    long tongGoc = 0L;
    for (int i = 0; i < modelBang.getRowCount(); i++) {
        tongGoc += ((Number) modelBang.getValueAt(i, 4)).longValue();
    }

    // 2) Áp giảm giá (%) và trừ điểm (1 điểm = 1.000 VND)
    int tru  = safeInt(txtTruDiem.getText());
    int giam = safeInt(txtGiamGia.getText());
    if (giam > 100) giam = 100;
    if (giam < 0)   giam = 0;
    if (tru  < 0)   tru  = 0;

    double sauPhanTram = tongGoc * (1.0 - (giam / 100.0));
    long tongCuoi = Math.max(0, Math.round(sauPhanTram) - tru * 1000L);
    lblTongThanhToan.setText(dinhDangVND(tongCuoi));

    // 3) Xử lý theo phương thức thanh toán
    boolean laTienMat = (cboPhuongThuc == null) || "Tiền mặt".equals(cboPhuongThuc.getSelectedItem());
    long tra;
    if (laTienMat) {
        // Cho nhập tự do (đã có filter >= 0)
        txtTienKhachTra.setEnabled(true);
        tra = safeLong(txtTienKhachTra.getText());
    } else {
        // CK/Visa: auto = tổng thanh toán, khóa ô nhập
        txtTienKhachTra.setEnabled(false);
        txtTienKhachTra.setText(String.valueOf(tongCuoi)); // để đồng bộ UI
        tra = tongCuoi;
    }

    // 4) Tính tiền thừa (có thể âm nếu khách đưa thiếu)
    long thua = tra - tongCuoi;
    lblTienThua.setText(dinhDangVND(thua));
    lblTienThua.setForeground(thua < 0 ? Color.RED : new Color(0,128,0));
}


    /** Đồng bộ độ rộng cột footer theo bảng chính */
    private void syncFooterColumnWidths() {
        if (bangMon == null || tblFooter == null) return;
        TableColumnModel m1 = bangMon.getColumnModel();
        TableColumnModel m2 = tblFooter.getColumnModel();
        for (int i = 0; i < m1.getColumnCount(); i++) {
            int w = m1.getColumn(i).getWidth();
            m2.getColumn(i).setPreferredWidth(w);
            m2.getColumn(i).setWidth(w);
        }
        tblFooter.revalidate();
        tblFooter.repaint();
    }

    /** Tính tổng SL/Thành tiền cho footer */
    private void updateFooterSum() {
        if (footerModel == null) return;
        footerModel.setRowCount(0);
        int sumSL = 0; long sumTT = 0;
        for (int r = 0; r < modelBang.getRowCount(); r++) {
            sumSL += ((Number) modelBang.getValueAt(r, 2)).intValue();
            sumTT += ((Number) modelBang.getValueAt(r, 4)).longValue();
        }
        // giữ đúng kiểu dữ liệu (Long) để renderer format VND
        footerModel.addRow(new Object[]{null, "TỔNG", sumSL, 0L, sumTT});
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
 // ===== Helpers cho input style giống ô search =====
    private JTextField taoOText(String placeholder) {
        JTextField tf = new JTextField();
        tf.setColumns(10);
        tf.setPreferredSize(new Dimension(220, 36));
        tf.setFont(new Font("Montserrat", Font.PLAIN, 16)); // hoặc SansSerif nếu thiếu
        tf.setBackground(Color.WHITE);
        tf.setForeground(Color.GRAY);            // placeholder màu xám
        tf.setText(placeholder);
        tf.setHorizontalAlignment(SwingConstants.LEFT);

        // padding + viền xám nhạt
        Color line = new Color(180,180,180);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(line, 1),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));

        // focus: đổi màu viền + ẩn/hiện placeholder
        tf.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) {
                    tf.setText("");
                }
                tf.setForeground(Color.BLACK);
                tf.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(140,140,140), 1),
                        BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            }
            @Override public void focusLost(FocusEvent e) {
                if (tf.getText().trim().isEmpty()) {
                    tf.setText(placeholder);
                    tf.setForeground(Color.GRAY);
                }
                else tf.setForeground(Color.BLACK);
                tf.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(line, 1),
                        BorderFactory.createEmptyBorder(6, 12, 6, 12)
                ));
            }
        });
        return tf;
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

    // Handler chọn phương thức
    cboPhuongThuc.addActionListener(e -> {
        boolean laTienMat = "Tiền mặt".equals(cboPhuongThuc.getSelectedItem());
        txtTienKhachTra.setEnabled(laTienMat);

        if (!laTienMat) {
            long tong = parseVND(lblTongThanhToan.getText());
            txtTienKhachTra.setText(String.valueOf(tong)); 
            capNhatTongThanhToan();
        }
    });

    // === Tạo khoảng cách giữa 2 hàng
    p.add(rowPM);
    p.add(Box.createVerticalStrut(150)); 

    // === Row 2: Nút Thanh Toán (căn phải)
    JPanel rowBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
    rowBtn.setOpaque(false);

    thanhToan = taoNut("Thanh Toán", iconThanhToan);
    thanhToan.setPreferredSize(new Dimension(200, 70));

    rowBtn.add(thanhToan);

    // Add vào panel chính
    p.add(rowBtn);

    return p;
}


    private long parseVND(String s) {
        if (s == null) return 0L;
        s = s.replace(".", "").replace(" VND", "").trim();
        try { return Long.parseLong(s); } catch (Exception e) { return 0L; }
    }


}
