package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

import com.toedter.calendar.JDateChooser;

public class LichsuThanhToan extends JFrame{
	private JButton btnMenu;
	private JLabel lbl;
	private JButton btnClose;
	private JTextField txtTim;
	private JButton btnTim;
	private DefaultTableModel mdHD;
	private JTable tableHD;
	private JTextField txtMaHD;
	private JTextField txtMaNV;
	private JTextField txtMaKH;
	private JTextField txtTenKH;
	private JTextField txtTGTao;
	private JTextField txtTGTT;
	private JTextField txtTongTien;
	private JTextField txtGiam;
	private JTextField txtDTL;
	private JTextField txtThanhtoan;
	private JTextField txtTienthoi;
	private DefaultTableModel mdMon;
	private JTable tableMon;
	private JScrollPane scoll;
	private JButton btnBack;
	private JButton btnXoa;

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
        
        
        JPanel pCen = new JPanel(new BorderLayout());
        pCen.setBackground(Color.decode("#DAB48C"));
        
        JPanel pNor = new JPanel(new BorderLayout());
        
        Box b,B;
        B=Box.createVerticalBox();        
        b=Box.createHorizontalBox();
        B.add(Box.createVerticalStrut(20));
        b.add(Box.createHorizontalStrut(35));
        b.add(txtTim=new JTextField("Nhập mã hóa đơn cần tìm",20));
        b.add(Box.createHorizontalStrut(15));
        
        JDateChooser dateTuNgay = new JDateChooser();
        dateTuNgay.setDateFormatString("dd/MM/yyyy");

        JDateChooser dateDenNgay = new JDateChooser();
        dateDenNgay.setDateFormatString("dd/MM/yyyy");
        btnTim=new JButton("Tìm");
        btnTim.setBackground(Color.decode("#865A52"));
        btnTim.setForeground(Color.white);
        
        b.add(new JLabel("Từ ngày :"));
        b.add(dateTuNgay);
        b.add(Box.createHorizontalStrut(15));
        
        b.add(new JLabel("Đến ngày :"));
        b.add(dateDenNgay);
        b.add(Box.createHorizontalStrut(15));
        
        b.add(btnTim);
        b.add(Box.createHorizontalStrut(35));
        b.setPreferredSize(new Dimension(500, 50)); 
        

        B.add(b);
        
        pNor.add(B);
        pNor.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0)); 
        pCen.add(pNor,BorderLayout.NORTH);
        
        JPanel pTable = new JPanel(new BorderLayout());
        String[] header= {"mã hóa đơn","TG tạo","TG thanh toán","mã nhân viên","giảm giá","Tổng thanh toán","trạng thái"};
        mdHD=new DefaultTableModel(header,0);
        tableHD= new JTable(mdHD);
        pTable.add(new JScrollPane(tableHD));
        
        
        
        
        pCen.add(pTable);
        
        JPanel pCTHD = new JPanel(new BorderLayout());
        
        Box t= Box.createHorizontalBox();
//        t.add(Box.createHorizontalStrut(20));
        
        

        // Tạo Box cho phần bên trái
        Box a, a1,a2,a3,a4,a5,a6;
        a=Box.createVerticalBox();
        a1=Box.createHorizontalBox();
        a1.add(new JLabel("Mã hóa đơn"));
        a1.add(txtMaHD = new JTextField(10));
        
        
        a2=Box.createHorizontalBox();
        a2.add(new JLabel("Mã nhân viên"));
        a2.add(txtMaNV = new JTextField(10));
        
        a3=Box.createHorizontalBox();
        a3.add(new JLabel("Mã khách hàng"));
        a3.add(txtMaKH = new JTextField(10));
        
        a4=Box.createHorizontalBox();
        a4.add(new JLabel("Tên khách hàng"));
        a4.add(txtTenKH = new JTextField(10));
        
        a5=Box.createHorizontalBox();
        a5.add(new JLabel("TG tạo"));
        a5.add(txtTGTao = new JTextField(10));
        
        a6=Box.createHorizontalBox();
        a6.add(new JLabel("Tg thanh toán"));
        a6.add(txtTGTT = new JTextField(10));
        a.add(a1);a.add(a2);a.add(a3);a.add(a6);a.add(a5);a.add(a4);
//        a.setPreferredSize(new Dimension(300, 200));
//        a.add(Box.createVerticalStrut(20));
        
        t.add(a);
        
        
        
        
        
        Box c,c1,c2,c3,c4,c5;
        c=Box.createVerticalBox();
        c1=Box.createHorizontalBox();
        c1.add(new JLabel("tổng tiền"));
        c1.add(txtTongTien = new JTextField(10));
        
        
        c2=Box.createHorizontalBox();
        c2.add(new JLabel("giảm: "));
        c2.add(txtGiam = new JTextField(10));
        
        c3=Box.createHorizontalBox();
        c3.add(new JLabel("trừ điểm TL:"));
        c3.add(txtDTL = new JTextField(10));
        
        c4=Box.createHorizontalBox();
        c4.add(new JLabel("Khách Trả:"));
        c4.add(txtThanhtoan = new JTextField(10));
        
        c5=Box.createHorizontalBox();
        c5.add(new JLabel("Tiền thối :"));
        c5.add(txtTienthoi = new JTextField(10));
        
        
        c.add(c1);c.add(c2);c.add(c3);c.add(c4);c.add(c5);
//        c.add(Box.createVerticalStrut(20));
        t.add(c);
        
        setTextFieldBackground(t, Color.decode("#DAB48C"));
        setTextFieldsEditable(t, false);
        t.setPreferredSize(new Dimension(700, 200));
        
        
//        pSou.add(c);
        
        pCTHD.add(t,BorderLayout.WEST);
        
        String[] header1= {"mã","tên món","số lượng","giá","thành tiền"};
        mdMon=new DefaultTableModel(header1, 0);
        tableMon= new JTable(mdMon);
        scoll= new JScrollPane(tableMon);
        scoll.setPreferredSize(new Dimension(200, 200));
        pCTHD.add(scoll);
        pCTHD.setBorder(BorderFactory.createEmptyBorder(0, 20, 30, 20)); 
        
        
        pCen.add(pCTHD,BorderLayout.SOUTH);
        
        
        
        add(pCen);
        
        JPanel pDuoi = new JPanel();
        pDuoi.add(btnBack= new JButton("Quay lại"),BorderLayout.WEST);
        pDuoi.add(btnXoa=new JButton("Xóa"),BorderLayout.EAST);
        btnBack.setBackground(Color.decode("#865A52"));
        btnBack.setForeground(Color.white);
        btnXoa.setBackground(Color.decode("#865A52"));
        btnXoa.setForeground(Color.white);
        btnBack.setPreferredSize(new Dimension(100, 40));
        btnXoa.setPreferredSize(new Dimension(100, 40));
        
        
        
        
        add(pDuoi,BorderLayout.SOUTH);
        add(pTil,BorderLayout.NORTH);
        
        
	
	}

	public static void main(String[] args) {
		new LichsuThanhToan().setVisible(true);

	}
	
	private void setTextFieldBackground(Box box, Color color) {
        for (Component component : box.getComponents()) {
            if (component instanceof Box) {
                setTextFieldBackground((Box) component, color);
            } else if (component instanceof JTextField) {
                component.setBackground(color);
            }
        }
    }
	private void setTextFieldsEditable(Box box, boolean editable) {
        for (Component component : box.getComponents()) {
            if (component instanceof Box) {
                setTextFieldsEditable((Box) component, editable);
            } else if (component instanceof JTextField) {
                ((JTextComponent) component).setEditable(editable);
            }
        }
    }

}
