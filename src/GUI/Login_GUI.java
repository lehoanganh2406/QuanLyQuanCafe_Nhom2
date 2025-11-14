package GUI;

import javax.swing.*;

import connectDB.ConnectDB;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login_GUI extends JFrame implements ActionListener {

    private JTextField txtMaNV;
    private JPasswordField txtPassword;
    private JButton btnDangNhap;
    private JLabel lblTogglePassword;
    private boolean hienMatKhau = false;

    public Login_GUI() {
        setTitle("Đăng nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        // ===== Panel trái: hình cà phê =====
        JPanel panelLeft = new JPanel(new BorderLayout());
        ImageIcon icon = new ImageIcon("src/img/caphelogo.jpg");   // ảnh nền bên trái
        Image img = icon.getImage().getScaledInstance(450, 500, Image.SCALE_SMOOTH);
        JLabel lblImage = new JLabel(new ImageIcon(img));
        panelLeft.add(lblImage, BorderLayout.CENTER);

        // ===== Panel phải: dùng GridBagLayout để căn giữa form =====
        JPanel panelLogin = new JPanel();
        panelLogin.setBackground(Color.decode("#996C63"));
        panelLogin.setLayout(new GridBagLayout()); // căn giữa mọi thứ trong panel này

        // Panel con chứa form, vẫn dùng setBounds cho dễ canh
        JPanel loginForm = new JPanel(null);
        loginForm.setPreferredSize(new Dimension(350, 260));
        loginForm.setBackground(Color.decode("#996C63"));

        // ======= Tiêu đề =======
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 10, 350, 30);
        loginForm.add(lblTitle);

        // ======= Tên đăng nhập =======
        JLabel lblMaNV = new JLabel("TÊN ĐĂNG NHẬP");
        lblMaNV.setForeground(Color.WHITE);
        lblMaNV.setFont(new Font("Arial", Font.BOLD, 12));
        lblMaNV.setBounds(40, 60, 150, 20);
        loginForm.add(lblMaNV);

        txtMaNV = new JTextField();
        txtMaNV.setBounds(40, 80, 260, 28);
        txtMaNV.setBackground(new Color(220, 220, 220));
        txtMaNV.setBorder(null);
        loginForm.add(txtMaNV);

        // ======= Mật khẩu =======
        JLabel lblPassword = new JLabel("PASSWORD");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(new Font("Arial", Font.BOLD, 12));
        lblPassword.setBounds(40, 120, 120, 20);
        loginForm.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(40, 140, 260, 28);
        txtPassword.setBackground(new Color(220, 220, 220));
        txtPassword.setBorder(null);
        loginForm.add(txtPassword);

        // Icon ẩn/hiện mật khẩu
        ImageIcon eyeClose = new ImageIcon(
                new ImageIcon(getClass().getResource("/img/anMatKhau.png"))
                        .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        ImageIcon eyeOpen = new ImageIcon(
                new ImageIcon(getClass().getResource("/img/nhinMatKhau.png"))
                        .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));

        lblTogglePassword = new JLabel(eyeClose);
        lblTogglePassword.setBounds(305, 140, 25, 25);
        lblTogglePassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblTogglePassword.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                hienMatKhau = !hienMatKhau;
                txtPassword.setEchoChar(hienMatKhau ? (char) 0 : '•');
                lblTogglePassword.setIcon(hienMatKhau ? eyeOpen : eyeClose);
            }
        });
        loginForm.add(lblTogglePassword);

        // ======= Nút đăng nhập =======
        btnDangNhap = new JButton("ĐĂNG NHẬP");
        btnDangNhap.setBounds(115, 190, 120, 32);
        btnDangNhap.setBackground(new Color(220, 220, 220));
        btnDangNhap.setFont(new Font("Arial", Font.BOLD, 13));
        btnDangNhap.setFocusPainted(false);
        loginForm.add(btnDangNhap);

        // Đưa loginForm vào giữa panelLogin bằng GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        panelLogin.add(loginForm, gbc);

        // ===== Gắn listener =====
        btnDangNhap.addActionListener(this);
        txtPassword.addActionListener(this);
        txtMaNV.addActionListener(this);

        // ===== Tách trái/phải =====
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(panelLeft);
        splitPane.setRightComponent(panelLogin);
        splitPane.setDividerLocation(450);
        splitPane.setDividerSize(0);
        splitPane.setEnabled(false);

        add(splitPane, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o.equals(btnDangNhap) || o.equals(txtPassword))
            kiemTraTaiKhoang();
        if (o.equals(txtMaNV)) {
            txtPassword.requestFocus();
        }
    }

    private void kiemTraTaiKhoang() {
        String dangNhap = txtMaNV.getText().trim();
        String matKhau = new String(txtPassword.getPassword()).trim();

        Connection con = ConnectDB.getInstance().getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            String sql = """
                    SELECT tk.tenDangNhap, tk.matKhau, tk.tenHienThi, tk.loaiTaiKhoan, tk.maNV,
                           nv.hoTen, nv.chucVu
                    FROM TaiKhoan tk
                    JOIN NhanVien nv ON tk.maNV = nv.maNV
                    WHERE tk.tenDangNhap = ? AND tk.matKhau = ?
                    """;

            stmt = con.prepareStatement(sql);
            stmt.setString(1, dangNhap);
            stmt.setString(2, matKhau);

            rs = stmt.executeQuery();

            if (rs.next()) {
                String tenHienThi = rs.getString("hoTen");
                String chucVu = rs.getString("chucVu");
                int loaiTaiKhoan = rs.getInt("loaiTaiKhoan");
                String maNV = rs.getString("maNV");

                ManHinhChinh_GUI mhc = new ManHinhChinh_GUI(tenHienThi, loaiTaiKhoan, maNV);
                mhc.setVisible(true);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối với CSDL!");
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (con != null) con.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login_GUI().setVisible(true));
    }
}
