package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class LichsuThanhToan extends JFrame{
	private JButton btnMenu;
	private JLabel lbl;
	private JButton btnClose;

	public LichsuThanhToan() {
		setTitle("lich su hoa don");
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		
		JPanel pTil = new JPanel(new BorderLayout());
		pTil.setBackground(Color.decode("#865A52"));  
        pTil.setPreferredSize(new Dimension(0,60));
        JPanel pTil_trai = new JPanel(new FlowLayout(FlowLayout.LEFT,10,8));
        pTil_trai.setOpaque(false); 
        ImageIcon iconMenu = new ImageIcon(new ImageIcon(getClass().getResource("/img/iconMenu.png"))
        		.getImage().getScaledInstance(49, 32, Image.SCALE_SMOOTH));
        pTil_trai.add(btnMenu = new JButton(iconMenu));
        btnMenu.setBackground(Color.decode("#865A52"));
        btnMenu.setBorderPainted(false); 
        btnMenu.setFocusPainted(false);   
        
        JPanel pTil_phai = new JPanel(new BorderLayout());
        pTil_phai.setOpaque(false);

        ImageIcon iconCoin = new ImageIcon(new ImageIcon(getClass().getResource("/img/iconcoin.png"))
                .getImage().getScaledInstance(40, 50, Image.SCALE_SMOOTH));

        lbl = new JLabel("Lịch sử hóa đơn", iconCoin, SwingConstants.LEFT);
        lbl.setFont(new Font("Montserrat", Font.BOLD, 28));
        lbl.setBackground(Color.decode("#FFF1E6"));
        lbl.setOpaque(true);
        lbl.setIconTextGap(12);
        lbl.setPreferredSize(new Dimension(300, 60)); 
        pTil_phai.add(lbl,BorderLayout.WEST);
        
        
        btnClose= new JButton("X");
		btnClose.setBackground(Color.decode("#865A52"));
		btnClose.setForeground(Color.white);
		pTil_phai.add(btnClose,BorderLayout.EAST);
        
        pTil.add(pTil_trai, BorderLayout.WEST);
        pTil.add(pTil_phai);
        
        
        
        add(pTil,BorderLayout.NORTH);
        
        
	
	}

	public static void main(String[] args) {
		new LichsuThanhToan().setVisible(true);

	}

}
