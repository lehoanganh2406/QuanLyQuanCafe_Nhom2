package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManHinhChinh_GUI extends JFrame {

    private JButton btnLogout, btnOrder, btnMenu, btnDSKhachHang, btnDSNhanVien, btnHoaDon, btnThongKe;
    private String tenHienThi; // tên người đăng nhập
	private int loaiTaiKhoan;
	private String maNV;

    // ===== Constructor chính (nhận tên hiển thị từ form Login) =====
    public ManHinhChinh_GUI(String tenHienThi, int loaiTaiKhoan, String maNV) {
        this.tenHienThi = tenHienThi;
        this.loaiTaiKhoan = loaiTaiKhoan;
        this.maNV = maNV;
        initComponents();
    }

    private void initComponents() {
        setTitle("Coffee & Food & Tea");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setLayout(new BorderLayout());

        Color brown = new Color(120, 74, 57);
        Color beige = new Color(227, 202, 183);
        Color lightGray = new Color(240, 240, 240);

        // ======= THANH TRÊN =======
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(brown);
        topPanel.setPreferredSize(new Dimension(0, 60));

        JLabel lblTitle = new JLabel("  COFFEE & FOOD & TEA", JLabel.LEFT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        
        String chucVu = (loaiTaiKhoan == 1) ? "Quản lý" : "Nhân viên";
        JLabel lblUser = new JLabel("Xin chào , "+ chucVu + " " + tenHienThi + "  ");
        lblUser.setFont(new Font("Arial", Font.PLAIN, 16));
        lblUser.setForeground(Color.WHITE);

        topPanel.add(lblTitle, BorderLayout.WEST);
        topPanel.add(lblUser, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // ======= THANH BÊN TRÁI =======
        pnThanhMenu leftPanel = new pnThanhMenu(tenHienThi, loaiTaiKhoan,maNV);
        add(leftPanel, BorderLayout.WEST);

        // ======= KHU VỰC CHÍNH =======
        Image bgImage = new ImageIcon(getClass().getResource("/img/coffee.jpg")).getImage();

        JPanel mainPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                int w = getWidth();
                int h = getHeight();
                // Vẽ ảnh phủ kín toàn bộ panel center
                g.drawImage(bgImage, 0, 0, w, h, this);
            }
        };
        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createSideButton(String text, Color color, String imagePath) {
        JButton btn = new JButton(text);

        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btn.setIcon(new ImageIcon(scaledImage));

        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(10);
        btn.setFont(new Font("Arial", Font.BOLD, 20));
        btn.setForeground(color);
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        btn.setPreferredSize(new Dimension(350, 100));
        btn.setMaximumSize(new Dimension(350, 100));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

}
