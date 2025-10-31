package GUI;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import entity.KhachHang;

public class TrangKhachHang_GUI extends JFrame implements ActionListener,MouseListener{
	private JTextField txtma;
	private JTextField txtten;
	private JTextField txtsdt;
	private JTextField txtdtl;
	private DefaultTableModel mdKH;
	private JTable tableKH;
	private JButton btnBack;
	private JButton btnThem;
	private JButton btnClose;
	private JTextField txtTim;
	private JButton btnTim;
	
	public TrangKhachHang_GUI() {
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		setTitle("thêm khách hàng");
		
		Color nen= new Color(230, 190, 145);
		Color brownColor = new Color(125, 95, 87);
		JPanel pNhap = new JPanel(new BorderLayout());
		pNhap.setPreferredSize(new Dimension(0,60));
		pNhap.setBackground(brownColor);
		JLabel til;
		pNhap.add(til=new JLabel("Khách Hàng"));
		til.setHorizontalAlignment(JLabel.CENTER);
		til.setForeground(Color.white);
		til.setFont(new Font("Arial", Font.BOLD, 40));
		
//		nut dong
		
		btnClose= new JButton("X");
		btnClose.setBackground(brownColor);
		btnClose.setForeground(Color.white);
		pNhap.add(btnClose,BorderLayout.EAST);
		
		
		add(pNhap,BorderLayout.NORTH);
		
		
		JPanel pcenter = new JPanel(new BorderLayout());
		JPanel pform = new JPanel();
		pcenter.setBackground(nen);
		pform.setBackground(nen);
		Box b,b1,b2,b3,b4;
		b=Box.createVerticalBox();
		
		b1=Box.createHorizontalBox();
		b1.add(new JLabel("Mã khách hang:"));
		b.add(Box.createVerticalStrut(5));
		b1.add(Box.createHorizontalStrut(30));
		b1.add(txtma= new JTextField(20));
		b.add(b1);
		
		
		b2=Box.createHorizontalBox();
		b2.add(new JLabel("Tên khách hàng :"));
		b2.add(Box.createHorizontalStrut(20));
		b2.add(txtten=new JTextField(20));
		b.add(Box.createVerticalStrut(5));
		b.add(b2);
		
		b3=Box.createHorizontalBox();
		b3.add(new JLabel("SDT liên hệ:"));
		b3.add(Box.createHorizontalStrut(49));
		b3.add(txtsdt=new JTextField(20));
		b.add(Box.createVerticalStrut(5));
		b.add(b3);
		
		b4=Box.createHorizontalBox();
		b4.add(new JLabel("Điểm tích lũy:"));
		b4.add(Box.createHorizontalStrut(40));
		b4.add(txtdtl=new JTextField(20));
		b.add(Box.createVerticalStrut(5));
		b.add(b4);
		b.setPreferredSize(new Dimension(400, 150));
		
		pform.add(b,BorderLayout.NORTH);
		pcenter.add(pform,BorderLayout.NORTH);
		
		
		add(pcenter);
		
		String [] header= {"Mã KH","Tên KH","SDT","Điểm tích lũy"};
		mdKH= new DefaultTableModel(header,0);
		tableKH= new JTable(mdKH);
		JScrollPane scroll = new JScrollPane(tableKH);
		scroll.getViewport().setBackground(Color.gray);
		
		pcenter.add(scroll);
		
		Box c;
		c=Box.createVerticalBox();
		c.add(Box.createVerticalStrut(15));
		Box a;
		a=Box.createHorizontalBox();
		a.add(Box.createHorizontalStrut(20));
		a.add(btnBack=new JButton("Quay lại"));
		a.add(Box.createHorizontalStrut(5));
		a.add(btnTim=new JButton("Tìm theo SDT"));
		a.add(Box.createHorizontalStrut(10));
		a.add(txtTim = new JTextField(10));		
		
//		đổi màu
		btnBack.setBackground(brownColor);
		btnBack.setForeground(Color.white);
		
		btnTim.setBackground(brownColor);
		btnTim.setForeground(Color.white);
		
		
		a.add(Box.createHorizontalStrut(200));
		a.add(btnThem= new JButton("Thêm"));
		btnThem.setBackground(brownColor);
		btnThem.setForeground(Color.white);
		a.add(Box.createHorizontalStrut(10));
		
		c.add(a);
		c.add(Box.createVerticalStrut(15));
		pcenter.add(c,BorderLayout.SOUTH);
		setFontSizeForAllComponents(pcenter, 15);
		
		btnBack.addActionListener(this);
		btnClose.addActionListener(this);
		
		btnThem.addActionListener(this);
		tableKH.addMouseListener(this);
		
		taidulieuKH();
		
		
		
	}
	private static void setFontSizeForAllComponents(Container container, int newSize) {
	    for (Component component : container.getComponents()) {
	        Font currentFont = component.getFont();
	        if (currentFont != null) {
	            Font newFont = new Font(currentFont.getName(), currentFont.getStyle(), newSize);
	            component.setFont(newFont);
	        }
	        
	        if (component instanceof Container) {
	            setFontSizeForAllComponents((Container) component, newSize); // Đệ quy cho các thành phần con
	        }
	    }
	}

	public static void main(String[] args) {
		
		new TrangKhachHang_GUI().setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		
		int row=tableKH.getSelectedRow();
		if (row!=-1) {
			txtma.setText(tableKH.getValueAt(row, 0).toString());
			txtdtl.setText(tableKH.getValueAt(row, 1).toString());
			txtsdt.setText(tableKH.getValueAt(row, 2).toString());
			txtten.setText(tableKH.getValueAt(row, 3).toString());
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o= e.getSource();
		if (o.equals(btnThem)) {
			if (!valiData()) {
				return ;
			}
			String maKH = txtma.getText().trim();
	        String tenKH = txtten.getText().trim();
	        String sdt = txtsdt.getText().trim();
	        int diemTL = Integer.parseInt(txtdtl.getText().trim()); 
	        KhachHang kh = new KhachHang(maKH, tenKH, sdt, diemTL);
	        try {
	            // Gọi hàm thêm khách hàng vào cơ sở dữ liệu
	            boolean success = themKhachHang(kh);
	            
	            if (success) {
	            	taidulieuKH();
	                Object[] rowData = { kh.getMaKH(), kh.getTenKH(), kh.getSdt(), kh.getDiemTL() };
	                mdKH.addRow(rowData);
	                
	                // Thông báo thêm thành công
	                JOptionPane.showMessageDialog(null, "Thêm khách hàng thành công và đã cập nhật bảng.");
	                
	            } else {
	                JOptionPane.showMessageDialog(null, "Không thể thêm khách hàng.");
	            }
	        } catch (Exception e1) {
	            e1.printStackTrace(); 
	            JOptionPane.showMessageDialog(null, "Đã xảy ra lỗi khi thêm khách hàng: " + e1.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
	        }
		}
		
	}

	private boolean themKhachHang(KhachHang kh) {
		// TODO Auto-generated method stub
		return true;
	}
	private boolean valiData() {
		String ma=txtma.getText().trim();
		String ten= txtten.getText().trim();
		String sdt= txtsdt.getText().trim();
		
		if (!(ma.length()>0&& ma.matches("^KH\\d{3}"))) {
			JOptionPane.showMessageDialog(this, "mã không hợp lệ , nhập đúng : KH001");
			return false;
		} 
		if (!(ten.length()>0 && sdt.length()>0)) {
			JOptionPane.showMessageDialog(this, "các ô nhập không được rỗng");
			return false;
		}
		if (!(sdt.matches("0\\d{9}"))) {
			JOptionPane.showMessageDialog(this, "SDT phải đầy đủ 9 số");
		}
		
		
		return true;
		
	}
	
	private void taidulieuKH() {
		List<KhachHang> ds= new ArrayList<KhachHang>();
		hienthiKH(ds);
		
	}
	
	private void hienthiKH(List<KhachHang> list) {
		mdKH.setRowCount(0);
		for (KhachHang k : list) {
			Object[] rowData= {
					k.getMaKH(),k.getTenKH(),k.getSdt(),k.getDiemTL()
			};
			mdKH.addRow(rowData);
		}
	}
}
