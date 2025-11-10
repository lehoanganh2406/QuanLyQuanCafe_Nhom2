package GUI;

import java.awt.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import connectDB.ConnectDB;
import dao.Ban_DAO;
import entity.Ban;

public class Ban_GUI extends JFrame {
	
	private JPanel jCen, pnBan;
	private String tenHienThi;
	private int loaiTaiKhoan;
	private String maNV;
	public static final Map<Integer, Timestamp> thoiGianVao = new HashMap<>();
	public Ban_GUI(String tenHienThi, int loaiTaiKhoan, String maNV) {
		try {
			ConnectDB.getInstance().connect();
			System.out.println("ket nnoi thanh cong");
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.tenHienThi = tenHienThi;
        this.loaiTaiKhoan = loaiTaiKhoan;
        this.maNV = maNV;
		setTitle("MH nhân viên phục vụ - Order");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                // Quay lại màn hình chính, giữ đúng tài khoản + tên hiển thị
                new ManHinhChinh_GUI(tenHienThi, loaiTaiKhoan, maNV).setVisible(true);
                dispose();
            }
        });

        
        thanhTieuDe();
        banOrder();
        
	}

	private void thanhTieuDe() {
		 String chucVu = (loaiTaiKhoan == 1) ? "Quản lý" : "Nhân viên";
		PanelTieuDe tieude = new PanelTieuDe("Order", "/img/iconOrder.png",chucVu,tenHienThi);
		add(tieude, BorderLayout.NORTH);
		pnThanhMenu menu = new pnThanhMenu(tenHienThi, loaiTaiKhoan, maNV);
		menu.setVisible(false);
		add(menu, BorderLayout.WEST);

		tieude.getBtnMenu().addActionListener(e -> {
		    menu.setVisible(!menu.isVisible());
		    revalidate();
		    repaint();
		});
	}
	private void banOrder() {
		jCen = new JPanel(new BorderLayout());
		jCen.setBackground(Color.WHITE);
		pnBan = new JPanel(new GridLayout(0, 5, 30, 30));
		pnBan.setBackground(Color.WHITE);
		pnBan.setBorder(BorderFactory.createEmptyBorder(30,50,30,50));
		for (int i = 1; i < 41; i++) {
			int soBan = i;
			JButton btnBan = new JButton("Bàn "+ soBan);
			btnBan.setPreferredSize(new Dimension(180, 140)); // kích thước mỗi bàn
            btnBan.setFont(new Font("Times New Roman", Font.BOLD, 25));
            btnBan.setFocusPainted(false);   // tắt viền focus
            btnBan.setForeground(Color.BLACK); // màu chữ
            btnBan.setBackground(Color.decode("#E3CFC1")); // màu nâu nhạt
            btnBan.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
            ImageIcon iconTable = new ImageIcon(
                    new ImageIcon(getClass().getResource("/img/iconlyCF.png"))
                    .getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)
                );
            btnBan.setIcon(iconTable);
            pnBan.add(btnBan);
            btnBan.addActionListener(e -> {
            	thoiGianVao.put(soBan, new Timestamp(System.currentTimeMillis()));
            	this.setVisible(false);
                new Order_GUI(soBan, tenHienThi, loaiTaiKhoan, maNV).setVisible(true);
                dispose();
            });
            JScrollPane spBan = new JScrollPane(pnBan, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            spBan.setBackground(Color.WHITE);
            spBan.getVerticalScrollBar().setUnitIncrement(10);
            jCen.add(spBan, BorderLayout.CENTER);
            add(jCen, BorderLayout.CENTER);
		}
		
	}
	

}