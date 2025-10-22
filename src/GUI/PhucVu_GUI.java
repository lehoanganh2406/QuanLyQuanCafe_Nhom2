package GUI;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PhucVu_GUI extends JFrame implements ActionListener{
	private JButton btnMenu;
	private JLabel lblOrder;
	private JButton btnDong;
	private JPanel jCen;
	private JButton btn;
	private int banWidth = 310;
	private int banheight = 140;
	private JPanel pnBan;
	public PhucVu_GUI() {
		setTitle("MH nhân viên phục vụ - Order");
		setSize(1280, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
       //====== Thanh tiêu đề =======
        JPanel jNor = new JPanel(new BorderLayout());
        jNor.setBackground(Color.decode("#865A52"));   // màu nâu
        jNor.setPreferredSize(new Dimension(0,60));
        
        JPanel jNorLeft = new JPanel(new FlowLayout(FlowLayout.LEFT,10,8));
        jNorLeft.setOpaque(false); // để hiện nền nâu của jNor
        ImageIcon iconMenu = new ImageIcon(new ImageIcon(getClass().getResource("/img/iconMenu.png"))
        		.getImage().getScaledInstance(49, 32, Image.SCALE_SMOOTH));
        jNorLeft.add(btnMenu = new JButton(iconMenu));
        btnMenu.setBackground(Color.decode("#865A52"));
        btnMenu.setBorderPainted(false); // tắt viền
        btnMenu.setFocusPainted(false);   // tắt viền focus
        
        JPanel jNorCen = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        jNorCen.setOpaque(false); // để hiện nền nâu của jNor
        ImageIcon iconOrder = new ImageIcon(new ImageIcon(getClass().getResource("/img/iconOrder.png"))
        		.getImage().getScaledInstance(38, 48, Image.SCALE_SMOOTH));
        jNorCen.add(lblOrder = new JLabel("Order", iconOrder, SwingConstants.CENTER));
        lblOrder.setBackground(Color.decode("#FFF1E6"));  // be nhạt
        lblOrder.setFont(new Font("Montserrat", Font.PLAIN, 32));
        lblOrder.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12)); // padding
        lblOrder.setForeground(Color.BLACK);
        lblOrder.setOpaque(true);   // hirnt thị nền
        lblOrder.setIconTextGap(10); // khoảng cách chữ và icon
        lblOrder.setHorizontalAlignment(SwingConstants.LEFT);
        
        
        jNor.add(jNorLeft, BorderLayout.WEST);
        jNor.add(jNorCen, BorderLayout.CENTER);
        add(jNor, BorderLayout.NORTH);
        
        jCen = new JPanel(new BorderLayout());
        jCen.setBackground(Color.WHITE);
        pnBan = new JPanel();
        pnBan.setBackground(Color.WHITE);
        FlowLayout flShowTable = new FlowLayout(FlowLayout.LEFT, 20 ,20);
        pnBan.setLayout(flShowTable);
        pnBan.setPreferredSize(new Dimension(banWidth, banheight));
        for (int i = 1; i <= 25; i++) {
            JButton btnBan = new JButton("Bàn " + i);
            btnBan.setPreferredSize(new Dimension(180, 90)); // kích thước mỗi bàn
            pnBan.add(btnBan);
        }

        JScrollPane spBan = new JScrollPane(pnBan, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        spBan.setBackground(Color.WHITE);
        spBan.getVerticalScrollBar().setUnitIncrement(10);
        jCen.add(spBan, BorderLayout.CENTER);
        add(jCen, BorderLayout.CENTER);
        
	}
	public static void main(String[] args) {
		new PhucVu_GUI().setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
	}
	
	

}
