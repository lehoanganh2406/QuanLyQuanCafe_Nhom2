package QuanLyQuanCF_GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PhucVu_GUI extends JFrame implements ActionListener{
	private JButton btnMenu;
	private JLabel lblOrder;
	private JButton btnDong;
	private JPanel jCen;
	private Dimension screenSize;
	private JButton btn;
	public PhucVu_GUI() {
		setTitle("MH nhân viên phục vụ - Order");
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screenSize.width, screenSize.height);
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
        
        //======== Danh sách bàn ========
        final int GAP_X   = 60;   // khoảng cách ngang giữa các nút
        final int GAP_Y   = 40;   // khoảng cách dọc giữa các nút
        final int BTN_W   = 188;  // chiều rộng nút
        final int BTN_H   = 88;   // chiều cao nút
        final int RADIUS  = 14;   // bo góc
        final Font BTN_FONT = new Font("Arial", Font.BOLD, 14);

        jCen = new JPanel(new GridLayout(5, 5, GAP_X, GAP_Y));
        jCen.setBackground(Color.decode("#E3CFC1"));
        jCen.setBorder(BorderFactory.createEmptyBorder(60, 60, 70, 60));

        for (int i = 0; i < 25; i++) {
        	int banSo = i+1;
            btn = new JButton("Bàn " + String.format("%02d", banSo)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // màu nền theo trạng thái (nhẹ tay để giống hình)
                    Color fill = Color.WHITE;
                    if (getModel().isPressed())      fill = new Color(235, 235, 235);
                    else if (getModel().isRollover()) fill = new Color(246, 246, 246);

                    // vẽ nền bo góc (chừa 1px để viền sắc nét)
                    g2.setColor(fill);
                    g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, RADIUS * 2, RADIUS * 2);

                    // viền mờ rất nhẹ
                    g2.setColor(new Color(0, 0, 0, 28));
                    g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, RADIUS * 2, RADIUS * 2);

                    g2.dispose();
                    super.paintComponent(g);
                }
            };

            // style nút nhỏ gọn
            btn.setFont(BTN_FONT);
            btn.setForeground(new Color(30, 30, 30));
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setOpaque(false);
            btn.setPreferredSize(new Dimension(BTN_W, BTN_H));
            btn.setMargin(new Insets(6, 14, 6, 14)); // padding trong nút
            btn.setFocusable(false); // tránh viền xanh khi focus bằng phím
            btn.addActionListener(e -> {
                // Ẩn cửa sổ hiện tại
                this.setVisible(false);

                // Mở cửa sổ mới
//                new Menu_GUI.setVisible(true);
            });

            jCen.add(btn);
        }
        add(jCen, BorderLayout.CENTER);
        
        
	}
	public static void main(String[] args) {
		new PhucVu_GUI().setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
	}
	

}
