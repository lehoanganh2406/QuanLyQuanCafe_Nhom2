package GUI;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;

import connectDB.ConnectDB;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class Ban_GUI extends JFrame implements ActionListener{
	private JButton btnMenu;
	private JLabel lblOrder;
	private JButton btnDong;
	private JPanel jCen;
	private JButton btn;
	private int banWidth = 310;
	private int banheight = 140;
	private JPanel pnBan;
	public Ban_GUI() {
		setTitle("MH nhân viên phục vụ - Order");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        thanhTieuDe();
        cacBan();
        
        
	}
	
	public static void main(String[] args) {
		ConnectDB.getInstance().connect();
		new Ban_GUI().setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
	}
	private void thanhTieuDe() {
    	PanelTieuDe panel = new PanelTieuDe("Order", "/img/iconOrder.png");
        add(panel, BorderLayout.NORTH);
    }
	private void cacBan() {
		jCen = new JPanel(new BorderLayout());
        jCen.setBackground(Color.WHITE);
        pnBan = new JPanel();
        pnBan.setBackground(Color.WHITE);
        pnBan.setBackground(Color.WHITE);
        pnBan.setLayout(new GridLayout(0, 5, 30, 30));
        pnBan.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        for (int i = 1; i <= 30; i++) {
        	final int soBan = i;
            JButton btnBan = new JButton("Bàn " + soBan);
            btnBan.setPreferredSize(new Dimension(200, 100)); // kích thước mỗi bàn
            btnBan.setFont(new Font("Times New Roman", Font.BOLD, 25));
            pnBan.add(btnBan);
            btnBan.setFocusPainted(false);   // tắt viền focus
            btnBan.setForeground(Color.BLACK); // màu chữ
            btnBan.setBackground(Color.decode("#E3CFC1")); // màu nâu nhạt
            btnBan.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
            ImageIcon iconTable = new ImageIcon(
                    new ImageIcon(getClass().getResource("/img/iconlyCF.png"))
                    .getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH)
                );
            btnBan.setIcon(iconTable);
            btnBan.addActionListener(e -> {
            	this.setVisible(false);
                // mở màn hình menu và truyền số bàn
                new Order_GUI(soBan).setVisible(true);
                dispose();
            });
        }

        JScrollPane spBan = new JScrollPane(pnBan, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        spBan.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = Color.decode("#E3CFC1"); // màu tay kéo
                trackColor = Color.decode("#F5E9E0"); // màu nền
            }
        });
        spBan.setBackground(Color.WHITE);
        spBan.getVerticalScrollBar().setUnitIncrement(10);
        jCen.add(spBan, BorderLayout.CENTER);
        add(jCen, BorderLayout.CENTER);
        

	}

}
