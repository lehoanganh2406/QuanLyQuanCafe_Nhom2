package GUI;

import GUI.ManHinhChinh_GUI;
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

        JPanel panelLeft = new JPanel(new BorderLayout());
        ImageIcon icon = new ImageIcon("src/img/caphelogo.jpg"); 
        Image img = icon.getImage().getScaledInstance(450, 500, Image.SCALE_SMOOTH);
        JLabel lblImage = new JLabel(new ImageIcon(img));
        panelLeft.add(lblImage, BorderLayout.CENTER);

        JPanel panelLogin = new JPanel();
        panelLogin.setBackground(Color.decode("#996C63"));
        panelLogin.setLayout(null);

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 50, 450, 30);
        panelLogin.add(lblTitle);

        JLabel lblMaNV = new JLabel("TÊN ĐĂNG NHẬP");
        lblMaNV.setForeground(Color.WHITE);
        lblMaNV.setFont(new Font("Arial", Font.BOLD, 12));
        lblMaNV.setBounds(50, 120, 120, 20);
        panelLogin.add(lblMaNV);

        txtMaNV = new JTextField();
        txtMaNV.setBounds(50, 140, 270, 25);
        txtMaNV.setBackground(new Color(220, 220, 220));
        txtMaNV.setBorder(null);
        panelLogin.add(txtMaNV);

        JLabel lblPassword = new JLabel("PASSWORD");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(new Font("Arial", Font.BOLD, 12));
        lblPassword.setBounds(50, 180, 120, 20);
        panelLogin.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(50, 200, 270, 25);
        txtPassword.setBackground(new Color(220, 220, 220));
        txtPassword.setBorder(null);
        panelLogin.add(txtPassword);

        ImageIcon eyeClose = new ImageIcon(
                new ImageIcon(getClass().getResource("/img/anMatKhau.png"))
                        .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
        ImageIcon eyeOpen = new ImageIcon(
                new ImageIcon(getClass().getResource("/img/nhinMatKhau.png"))
                        .getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));

        lblTogglePassword = new JLabel(eyeClose);
        lblTogglePassword.setBounds(325, 200, 25, 25);
        lblTogglePassword.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        lblTogglePassword.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                hienMatKhau = !hienMatKhau;
                txtPassword.setEchoChar(hienMatKhau ? (char) 0 : '•');
                lblTogglePassword.setIcon(hienMatKhau ? eyeOpen : eyeClose);
            }
        });
        panelLogin.add(lblTogglePassword);

        btnDangNhap = new JButton("ĐĂNG NHẬP");
        btnDangNhap.setBounds(150, 250, 110, 30);
        btnDangNhap.setBackground(new Color(220, 220, 220));
        btnDangNhap.setFont(new Font("Arial", Font.BOLD, 12));
        btnDangNhap.setFocusPainted(false);
        panelLogin.add(btnDangNhap);

        btnDangNhap.addActionListener(this);
        txtPassword.addActionListener(this);
        txtMaNV.addActionListener(this);

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
