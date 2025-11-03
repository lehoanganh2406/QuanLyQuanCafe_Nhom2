package GUI;

import java.awt.*;
import javax.swing.*;

import connectDB.ConnectDB;
import dao.Ban_DAO;
import entity.Ban;

public class Ban_GUI extends JFrame {
	
	private JPanel jCen, pnBan;
	public Ban_GUI() {
		
		setTitle("MH nhân viên phục vụ - Order");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        thanhTieuDe();
        banOrder();
        
	}
	public static void main(String[] args) {
		ConnectDB.getInstance().connect();
		Ban_DAO dao = Ban_DAO.getInstance();
	    if (dao.getAllBan().isEmpty()) { 
	        dao.themBan(new Ban("Bàn 1", "Trống"));
	        dao.themBan(new Ban("Bàn 2", "Trống"));
	        dao.themBan(new Ban("Bàn 3", "Trống"));
	        dao.themBan(new Ban("Bàn 4", "Trống"));
	        dao.themBan(new Ban("Bàn 5", "Trống"));
	    }
		new  Ban_GUI().setVisible(true);
	}
	private void thanhTieuDe() {
		PanelTieuDe tieude = new PanelTieuDe("Order", "/img/iconOrder.png");
		add(tieude, BorderLayout.NORTH);
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
            	this.setVisible(false);
                new Order_GUI(soBan).setVisible(true);
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