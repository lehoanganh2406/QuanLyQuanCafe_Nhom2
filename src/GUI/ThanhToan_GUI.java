package GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import connectDB.ConnectDB;

import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import dao.*;
import entity.*;

public class ThanhToan_GUI extends JFrame {
	private int soBan;
	private JButton btnMenu;
	private JLabel lblOrder;
	private JLabel lblThongTinKhachHang;
	private JTextField txtTruDiem, txtGiamGia;
	private JLabel lblTongThanhToan;
	private ImageIcon iconQuayLai = new ImageIcon(getClass().getResource("/img/back_16.png"));

	public ThanhToan_GUI(int soBan)  {
		this.soBan = soBan;
        setTitle("Menu - Bàn " + soBan);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        thanhTieuDe();
        taoCenter();
	}
	public static void main(String[] args) {
        ConnectDB.getInstance().connect();
        SwingUtilities.invokeLater(() -> new ThanhToan_GUI(1).setVisible(true));
    }
	// ========== Thanh tiêu đề =========
    public void thanhTieuDe() {
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
 // ========== Center =========
    public void taoCenter() {
        // panel center dùng FlowLayout để đặt 2 cột trái | phải
        JPanel trungTam = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 12));
        trungTam.setOpaque(false);

        // ===== cột TRÁI: 
        JPanel cotTrai = new JPanel(new BorderLayout(0, 12));
        cotTrai.setOpaque(false);
        cotTrai.setPreferredSize(new Dimension(1200, 900)); // để FlowLayout canh khối

        // --- NORTH: 
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
            "  • 10.000 VND = 1 điểm"
        );
        ghiChu.setEditable(false);
        ghiChu.setOpaque(false);
        ghiChu.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        ghiChu.setAlignmentX(Component.LEFT_ALIGNMENT);
//        ghiChu.setBorder(BorderFactory.createEmptyBorder(0, 90, 0, 0));
        thongTinTren.setBorder(BorderFactory.createEmptyBorder(0, 200, 0, 0));
        thongTinTren.add(ghiChu);

        DefaultTableModel modelBang = new DefaultTableModel(
                new Object[]{"Mã", "Tên món", "Số lượng", "Giá", "Thành tiền"}, 
                0 // 0 dòng => chưa có dữ liệu
            );

            JTable bangMon = new JTable(modelBang);
//            bangMon.setRowHeight(48);
            bangMon.setFont(new Font("Times New Roman", Font.PLAIN, 18));
            bangMon.setFillsViewportHeight(true);

            // ====== Custom Header đẹp như mẫu ======
            JTableHeader header = bangMon.getTableHeader();
            header.setPreferredSize(new Dimension(header.getWidth(), 48));
            header.setBackground(new Color(245,239,234)); // màu header trong hình
            header.setFont(new Font("Times New Roman", Font.BOLD, 18));
            header.setBorder(BorderFactory.createMatteBorder(1,1,1,1,new Color(185,167,156)));

            // ====== Bọc cuộn ======
            JScrollPane cuonBang = new JScrollPane(bangMon);
            cuonBang.setBorder(BorderFactory.createLineBorder(new Color(185,167,156)));




        // --- SOUTH: nút quay lại
        JButton nutQuayLai = taoNut("Quay lại", iconQuayLai);
        JPanel dayTrai = new JPanel(new FlowLayout(FlowLayout.LEFT));
        dayTrai.setOpaque(false);
        dayTrai.add(nutQuayLai);

        // ghép vào cột trái
        cotTrai.add(thongTinTren, BorderLayout.NORTH);
        cotTrai.add(cuonBang,     BorderLayout.CENTER);
        cotTrai.add(dayTrai,      BorderLayout.SOUTH);

        // ===== cột PHẢI: tạm để trống hoặc bạn thêm sau
        JPanel cotPhai = new JPanel();
        cotPhai.setOpaque(false);
//        cotPhai.setPreferredSize(new Dimension(300, 520)); 
        JPanel phanNor = new JPanel();
        phanNor.setOpaque(false);
        phanNor.setLayout(new BoxLayout(phanNor, BoxLayout.Y_AXIS));
        phanNor.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));

        // 1) Mã hóa đơn
        phanNor.add(tieuDeDonGian("Mã hóa đơn:"));
        phanNor.add(Box.createVerticalStrut(8));

        // 2) Mã nhân viên
        phanNor.add(tieuDeDonGian("Mã nhân viên:"));
        phanNor.add(Box.createVerticalStrut(16));

        // 3) Trừ điểm (%)
        phanNor.add(hangPhanTram("Trừ điểm:", txtTruDiem = taoOPhanTram()));
        phanNor.add(Box.createVerticalStrut(10));

        // 4) Giảm giá (%)
        phanNor.add(hangPhanTram("Giảm giá:", txtGiamGia = taoOPhanTram()));
        phanNor.add(Box.createVerticalStrut(16));

        // 5) Tổng thanh toán
        lblTongThanhToan = new JLabel("Tổng thanh toán: 210.000VND");
        lblTongThanhToan.setFont(new Font("Times New Roman", Font.BOLD, 22));
        phanNor.add(lblTongThanhToan);
        
        cotPhai.add(phanNor, BorderLayout.NORTH);
        // đặt 2 cột vào center (FlowLayout)
        trungTam.add(cotTrai);
        trungTam.add(cotPhai);

        add(trungTam, BorderLayout.CENTER);
    }
    private JLabel tieuDe(String nhan, String giaTri) {
        JLabel lbl = new JLabel(nhan + "   " + giaTri);
        lbl.setFont(new Font("Times New Roman", Font.BOLD, 28));
        return lbl;
    }
    private JButton taoNut(String ten,ImageIcon icon ) {
        JButton btn = new JButton(ten, icon);
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Times New Roman", Font.BOLD, 20));
        return btn;
    }
    private JButton taoNut(String ten) {
        return taoNut(ten, null);
    }
    private JPanel tieuDeDonGian(String nhan) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        p.setOpaque(false);

        JLabel l1 = new JLabel(nhan);
        l1.setFont(new Font("Times New Roman", Font.BOLD, 22));
        

        p.add(l1); ;
        return p;
    }
 // ===== Tạo dòng: [Nhãn] [TextField giả] [%]
    private JPanel hangPhanTram(String nhan, JTextField txt) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        p.setOpaque(false);

        JLabel l = new JLabel(nhan);
        l.setFont(new Font("Times New Roman", Font.BOLD, 22));

        JLabel percent = new JLabel("%");
        percent.setFont(new Font("Times New Roman", Font.BOLD, 22));

        // click vào ô → mở dialog nhập
        txt.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseClicked(java.awt.event.MouseEvent e) {
                Integer val = moNhapPhanTram("Nhập " + nhan.toLowerCase()); // 0..100
                if (val != null) {
                    txt.setText(String.valueOf(val));
                    capNhatTongThanhToan(); // tính lại nếu bạn có logic
                }
            }
        });

        p.add(l);
        p.add(txt);
        p.add(percent);
        return p;
    }

    // ===== TextField kiểu “ô nhập khi click” (không cho gõ trực tiếp)
    private JTextField taoOPhanTram() {
        JTextField tf = new JTextField();
        tf.setEditable(false);                           // không gõ trực tiếp
        tf.setColumns(4);
        tf.setPreferredSize(new Dimension(80, 32));
        tf.setHorizontalAlignment(SwingConstants.CENTER);
        tf.setFont(new Font("Times New Roman", Font.PLAIN, 22));
        tf.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        tf.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.DARK_GRAY)); // giống underline mảnh
        tf.setToolTipText("Nhấn để nhập phần trăm");
        return tf;
    }

    // ===== Dialog nhập % bằng JSpinner (0..100)
    private Integer moNhapPhanTram(String tieuDe) {
        JSpinner sp = new JSpinner(new SpinnerNumberModel(0, 0, 100, 1));
        JComponent editor = sp.getEditor();
        if (editor instanceof JSpinner.DefaultEditor de) {
            de.getTextField().setHorizontalAlignment(SwingConstants.CENTER);
            de.getTextField().setFont(new Font("Times New Roman", Font.PLAIN, 18));
        }
        int opt = JOptionPane.showConfirmDialog(this, sp, tieuDe, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opt == JOptionPane.OK_OPTION) {
            return (Integer) sp.getValue();
        }
        return null;
    }

    // ===== Tính lại tổng (placeholder – bạn gắn với model giỏ hàng)
    private void capNhatTongThanhToan() {
        // ví dụ: đọc % giảm
        int tru = safeInt(txtTruDiem.getText());
        int giam = safeInt(txtGiamGia.getText());
        // TODO: lấy tổng gốc từ giỏ hàng rồi áp dụng
        // ví dụ minh họa giữ nguyên text:
        lblTongThanhToan.setText("Tổng thanh toán: 210.000VND");
    }
    private int safeInt(String s) { try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; } }

}


    

