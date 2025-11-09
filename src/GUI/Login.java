package GUI;
import GUI.ManHinhChinh;
import javax.swing.*;

import connectDB.ConnectDB;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Login extends JFrame implements ActionListener{

    private JTextField txtMaNV;
    private JPasswordField txtPassword;
    private JButton btnDangNhap;
    private JRadioButton rdoNhanVien, rdoQuanLy;

    public Login() {
        setTitle("Đăng nhập");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        getContentPane().setBackground(new Color(227, 202, 183));
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel panelLogin = new JPanel();
        panelLogin.setBackground(new Color(120, 74, 57));
        panelLogin.setPreferredSize(new Dimension(350, 230));
        panelLogin.setLayout(null);
        panelLogin.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 10, 350, 30);
        panelLogin.add(lblTitle);

        JLabel lblMaNV = new JLabel("MÃ NHÂN VIÊN");
        lblMaNV.setForeground(Color.WHITE);
        lblMaNV.setFont(new Font("Arial", Font.BOLD, 12));
        lblMaNV.setBounds(40, 50, 120, 20);
        panelLogin.add(lblMaNV);

        txtMaNV = new JTextField();
        txtMaNV.setBounds(40, 70, 270, 25);
        txtMaNV.setBackground(new Color(220, 220, 220));
        txtMaNV.setBorder(null);
        panelLogin.add(txtMaNV);

        JLabel lblPassword = new JLabel("PASSWORD");
        lblPassword.setForeground(Color.WHITE);
        lblPassword.setFont(new Font("Arial", Font.BOLD, 12));
        lblPassword.setBounds(40, 100, 120, 20);
        panelLogin.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(40, 120, 270, 25);
        txtPassword.setBackground(new Color(220, 220, 220));
        txtPassword.setBorder(null);
        panelLogin.add(txtPassword);

        btnDangNhap = new JButton("ĐĂNG NHẬP");
        btnDangNhap.setBounds(120, 155, 110, 30);
        btnDangNhap.setBackground(new Color(220, 220, 220));
        btnDangNhap.setFont(new Font("Arial", Font.BOLD, 12));
        btnDangNhap.setFocusPainted(false);
        panelLogin.add(btnDangNhap);
        btnDangNhap.addActionListener(this);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(panelLogin, gbc);
    }

    

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		
		if(o.equals(btnDangNhap))
			kiemTraTaiKhoang();
		
	}

	private void kiemTraTaiKhoang() {
	    String dangNhap = txtMaNV.getText().trim();
	    String matKhau = new String(txtPassword.getPassword()).trim(); 

	    Connection con = ConnectDB.getInstance().getConnection();
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        String sql = "SELECT * FROM TaiKhoan WHERE tenDangNhap = ? AND matKhau = ?";
	        stmt = con.prepareStatement(sql);
	        stmt.setString(1, dangNhap);
	        stmt.setString(2, matKhau);

	        rs = stmt.executeQuery();

	        if (rs.next()) {
	            String tenHienThi = rs.getString("tenHienThi"); // Lấy tên hiển thị
	            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
	            ManHinhChinh mhc = new ManHinhChinh(tenHienThi); // Truyền tên vào màn hình chính
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
	    SwingUtilities.invokeLater(() -> {
	        new Login().setVisible(true);
	    });
	}
}




