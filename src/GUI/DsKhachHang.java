package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
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
import javax.swing.table.JTableHeader;

import connectDB.ConnectDB;
import dao.KhachHang_DAO;
import entity.KhachHang;

public class DsKhachHang extends JFrame implements ActionListener,MouseListener {
	
	private DefaultTableModel mdDSKH;
	private JTable tableDSKH;
	private JButton btnBack;
	private JButton btnThem;
	private final Color brownColor = Color.decode("#865A52");
	private final Color nen = Color.decode("#E3CFC1");
	private JTextField txtTim;
	private JButton btnTim;
	private JButton btnXoa;
	private JButton btnSua;
	private JTextField txtten;
	private JTextField txtsdt;
	private JTextField txtdtl;
	private JTextField txtma;
	private KhachHang_DAO kh_dao;
	

	public DsKhachHang() {
		try {
			ConnectDB.getInstance().connect();
			System.out.println("ket nnoi thanh cong");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		kh_dao= new KhachHang_DAO();
		
		setTitle("Danh Sach Khach Hang");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        thanhTieuDe();
        
        JPanel pcenter = new JPanel(new BorderLayout());
        pcenter.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        String[] header = {"Mã KH", "Tên KH", "SĐT", "Điểm TL"};
        mdDSKH = new DefaultTableModel(header, 0);
        tableDSKH = new JTable(mdDSKH);
        tableDSKH.setRowHeight(28);
        tableDSKH.setFont(new Font("Arial", Font.PLAIN, 14));
        tableDSKH.setGridColor(new Color(180, 150, 120));
        tableDSKH.setSelectionBackground(new Color(210, 180, 140));
        tableDSKH.setSelectionForeground(Color.BLACK);

        JTableHeader headerTable = tableDSKH.getTableHeader();
        headerTable.setPreferredSize(new Dimension(headerTable.getWidth(), 45));
        headerTable.setFont(new Font("Times New Roman", Font.BOLD, 18));
        headerTable.setBackground(Color.decode("#EDE7E3"));
        headerTable.setBorder(BorderFactory.createEmptyBorder());

        JScrollPane scroll = new JScrollPane(tableDSKH,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pcenter.add(scroll, BorderLayout.CENTER);
        
        JPanel pSou = new JPanel(new BorderLayout());
        
        Box c = Box.createVerticalBox();
        Box a = Box.createHorizontalBox();

        a.add(Box.createHorizontalStrut(20));
        a.add(btnBack = new JButton("Quay lại"));
        a.add(Box.createHorizontalStrut(10));
        a.add(btnThem = new JButton("Thêm"));
        a.add(Box.createHorizontalStrut(10));
        a.add(btnXoa=new JButton("Xoa"));
        a.add(Box.createHorizontalStrut(10));
        a.add(btnSua=new JButton("Sua"));
        a.add(Box.createHorizontalStrut(10));
        a.add(txtTim=new JTextField("nhập sdt cần tìm",10));
        a.add(Box.createHorizontalStrut(10));
        a.add(btnTim=new JButton("Tìm"));
        a.add(Box.createHorizontalStrut(20));

        for (JButton btt : new JButton[]{btnBack, btnThem,btnSua,btnTim,btnXoa}) {
            btt.setBackground(brownColor);
            btt.setForeground(Color.WHITE);
            btt.setFocusPainted(false);
            btt.setPreferredSize(new Dimension(100, 40)); 
        }

        c.add(Box.createVerticalStrut(15));
        c.add(a);
        c.add(Box.createVerticalStrut(15));
        
//        doc dữ liệu vào bảng 
        docDulieutuDBvaoTable();
        
        
        JPanel pform = new JPanel();
		pcenter.setBackground(nen);
		pform.setBackground(nen);
		Box b,b1,b2,b3,b4;
		b=Box.createVerticalBox();
		
		b1=Box.createHorizontalBox();
		b1.add(new JLabel("Mã khách hàng:"));
		b.add(Box.createVerticalStrut(5));
		b1.add(Box.createHorizontalStrut(27));
		b1.add(txtma= new JTextField(20));
		txtma.setEditable(false); 
//        txtma.setFocusable(false);
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
//		txtdtl.setEditable(false);
//        txtdtl.setFocusable(false);
		b.add(Box.createVerticalStrut(5));
		b.add(b4);
		b.setPreferredSize(new Dimension(400, 150));
		
		pform.add(b,BorderLayout.NORTH);
		pcenter.add(pform,BorderLayout.NORTH);
        pcenter.add(pform, BorderLayout.NORTH);
        
        for (JTextField txt : new JTextField[]{txtma, txtten, txtsdt, txtdtl}) {
            txt.setBorder(null);                    
            txt.setBackground(null);     
            txt.setForeground(Color.BLACK);        
            txt.setCaretColor(Color.BLACK);        
        }
        
        
        pSou.add(c);
        add(pSou,BorderLayout.SOUTH);
        
        add(pcenter);
        
        tableDSKH.addMouseListener(this);
        btnThem.addActionListener(this);
        btnBack.addActionListener(this);
        btnSua.addActionListener(this);
        
        btnTim.addActionListener(this);
        btnXoa.addActionListener(this);
        
        
	}

	public static void main(String[] args) {
		new DsKhachHang().setVisible(true);

	}
	
	private void thanhTieuDe() {
		PanelTieuDe tieude = new PanelTieuDe("Danh Sach Khach Hang", "/img/iconuser.png");
		add(tieude, BorderLayout.NORTH);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row= tableDSKH.getSelectedRow();
		txtma.setText(mdDSKH.getValueAt(row, 0).toString());
		txtten.setText(mdDSKH.getValueAt(row, 1).toString());
		txtsdt.setText(mdDSKH.getValueAt(row, 2).toString());
		txtdtl.setText(mdDSKH.getValueAt(row, 3).toString());
		
		
		
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
			if (!valiData()) return ;
			
			String tenKH = txtten.getText().trim();
            String sdt = txtsdt.getText().trim();
            int diemTL = 0;
            KhachHang khTonTai = kh_dao.getBySDT(sdt);
            if (khTonTai != null) {
                JOptionPane.showMessageDialog(this, 
                    "Số điện thoại này đã tồn tại trong hệ thống!",
                    "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            KhachHang kh = new KhachHang(null, tenKH, sdt, diemTL);
            boolean themOK = kh_dao.insert(kh);

            if (themOK) {
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");

                mdDSKH.setRowCount(0);
                docDulieutuDBvaoTable();

                
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Thêm khách hàng thất bại!", 
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
            
            
            cleartext();
            
			
		}
		else if (o.equals(btnXoa)) {
			int row = tableDSKH.getSelectedRow();

		    if (row == -1) {
		        JOptionPane.showMessageDialog(this, 
		            "Cần chọn 1 khách hàng để xóa",
		            "Thông báo", JOptionPane.WARNING_MESSAGE);
		        return;
		    }
		    String maKH = mdDSKH.getValueAt(row, 0).toString();
		    String tenKH = mdDSKH.getValueAt(row, 1).toString();

		    int confirm = JOptionPane.showConfirmDialog(this,
		        "ban co chan chan xoa khach hang nay: " + tenKH + " (" + maKH + ")?",
		        "Confirm xoa", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		    if (confirm == JOptionPane.YES_OPTION) {
		        boolean xoaOK = kh_dao.delete(maKH);

		        if (xoaOK) {
		            JOptionPane.showMessageDialog(this, 
		                "Xoa thanh cong", 
		                "Thong bao", JOptionPane.INFORMATION_MESSAGE);

		            mdDSKH.setRowCount(0);
		            docDulieutuDBvaoTable();
		            cleartext();
		        } else {
		            JOptionPane.showMessageDialog(this, 
		                "Xóa thất bại!", 
		                "Lỗi xóa", JOptionPane.ERROR_MESSAGE);
		        }
		    }

			
		}
		
		else if (o.equals(btnSua)) {
		    int row = tableDSKH.getSelectedRow();
		    if (row == -1) {
		        JOptionPane.showMessageDialog(this, "cần chọn khách hàng cần sửa");
		        return;
		    }

		    if (!valiData()) return; 

		    String maKH = txtma.getText().trim();
		    String tenKH = txtten.getText().trim();
		    String sdt = txtsdt.getText().trim();
		    int diemTL = 0;

		    try {
		        diemTL = Integer.parseInt(txtdtl.getText().trim());
		        if (diemTL < 0) {
		            JOptionPane.showMessageDialog(this, "ĐTL phải lớn hơn 0.");
		            return;
		        }
		    } catch (NumberFormatException ex) {
		        JOptionPane.showMessageDialog(this, "LỖI nhập số , nhập số nguyên ");
		        return;
		    }

		    int confirm = JOptionPane.showConfirmDialog(this,
		        "Có chắc chắn muốn cập nhật thông tin khách hàng này?",
		        "Xác nhận sửa", JOptionPane.YES_NO_OPTION);

		    if (confirm == JOptionPane.YES_OPTION) {
		        KhachHang kh = new KhachHang(maKH, tenKH, sdt, diemTL);

		        boolean suaTT = kh_dao.updateInfo(kh);
		        boolean suaDTL = kh_dao.updateDiem(maKH, diemTL);

		        if (suaTT || suaDTL) {
		            JOptionPane.showMessageDialog(this, "Cập nhật thông tin khách hàng thành công");

		            mdDSKH.setRowCount(0);
		            docDulieutuDBvaoTable();
		        } else {
		            JOptionPane.showMessageDialog(this,  "ko thể cập nhật ");
		        }
		    }
		}
		
		else if (o.equals(btnTim)) {
		    String sdt = txtTim.getText().trim();

		    if (sdt.isEmpty()) {
		        JOptionPane.showMessageDialog(this, "Cần nhập sdt cần tìm");
		        return;
		    }

		    KhachHang kh = kh_dao.getBySDT(sdt);
		    if (kh != null) {
		        for (int i = 0; i < mdDSKH.getRowCount(); i++) {
		            String sdtTable = mdDSKH.getValueAt(i, 2).toString();
		            if (sdtTable.equals(sdt)) {
		                tableDSKH.setRowSelectionInterval(i, i);
		                tableDSKH.scrollRectToVisible(tableDSKH.getCellRect(i, 0, true));

		                txtma.setText(kh.getMaKH());
		                txtten.setText(kh.getTenKH());
		                txtsdt.setText(kh.getSdt());
		                txtdtl.setText(String.valueOf(kh.getDiemTL()));
		                return;
		            }
		        }
		    } else {
		        JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng có SĐT: " + sdt);
		    }
		}




		
		
	}
	
	private void cleartext() {
		txtma.setText("");
        txtten.setText("");
        txtsdt.setText("");
        txtdtl.setText("");

	}

//	
	public void docDulieutuDBvaoTable() {
		List<KhachHang> list= kh_dao.getAllKhachHang();
		for (KhachHang k : list) {
			mdDSKH.addRow(new Object[] {
					k.getMaKH(),k.getTenKH(),k.getSdt(),k.getDiemTL()
			});
		}
	}
	
	private boolean valiData() {
        String ten = txtten.getText().trim();
        String sdt = txtsdt.getText().trim();
        if (ten.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Các ô nhập không được rỗng");
            return false;
        }
        if (!sdt.matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(this, "SĐT phải gồm 10 số và bắt đầu bằng 0");
            return false;
        }
        return true;
    }
}
