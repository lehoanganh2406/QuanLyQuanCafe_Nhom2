package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;

import com.toedter.calendar.JDateChooser;

import connectDB.ConnectDB;
import dao.ChiTietHoaDon_DAO;
import dao.HoaDon_DAO;
import dao.SanPham_DAO;
import entity.ChiTietHoaDon;
import entity.HoaDon;
import entity.KhachHang;

public class LichsuThanhToan extends JFrame implements ActionListener,MouseListener,PropertyChangeListener{
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
	private HoaDon_DAO hd_dao;
	private ChiTietHoaDon_DAO cthd_dao;
	private SanPham_DAO sp_dao;
	private JDateChooser dateTuNgay;
	private JDateChooser dateDenNgay;

	public LichsuThanhToan() {
		try {
			ConnectDB.getInstance().connect();
			System.out.println("ket nnoi thanh cong");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		hd_dao=new HoaDon_DAO();
		cthd_dao=new ChiTietHoaDon_DAO();
		sp_dao= new SanPham_DAO();
		
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
        lbl.setFont(new Font("Arial", Font.BOLD, 28));
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
        btnTim=new JButton("Tìm");
        btnTim.setFont(new Font("Arial", Font.PLAIN, 20));
        btnTim.setBackground(Color.decode("#865A52"));
        btnTim.setForeground(Color.white);
        b.add(txtTim=new JTextField(20));
        txtTim.setText("Nhập mã hóa đơn cần tìm...");
        txtTim.setFont(new Font("Arial", Font.PLAIN, 17));
        txtTim.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtTim.getText().equals("Nhập mã hóa đơn cần tìm...")) {
                    txtTim.setText("");
                    txtTim.setForeground(Color.BLACK); 
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtTim.getText().isEmpty()) {
                    txtTim.setText("Nhập mã hóa đơn cần tìm...");
                    txtTim.setForeground(Color.lightGray); 
                }
            }
        });
        b.add(Box.createHorizontalStrut(35));
        b.add(btnTim);
        btnTim.setPreferredSize(new Dimension(120, 30));
        
        b.add(Box.createHorizontalStrut(15));
        
        dateTuNgay = new JDateChooser();
        dateTuNgay.setDateFormatString("dd/MM/yyyy");

        dateDenNgay = new JDateChooser();
        dateDenNgay.setDateFormatString("dd/MM/yyyy");
        
        
        JLabel tungay = new JLabel("Từ ngày :");
        tungay.setFont(new Font("Arial", Font.PLAIN, 20));
		b.add(tungay);
        b.add(dateTuNgay);
        b.add(Box.createHorizontalStrut(15));
        
        JLabel denngay=new JLabel("Đến ngày :");
        denngay.setFont(new Font("Arial", Font.PLAIN, 20));
		b.add(denngay);
        b.add(dateDenNgay);
        b.add(Box.createHorizontalStrut(15));
        
        
        
        
        
        b.setPreferredSize(new Dimension(500, 50)); 
        

        B.add(b);
        pNor.setBackground(Color.decode("#E3CFC1"));
        pNor.add(B);
        pNor.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0)); 
        pCen.add(pNor,BorderLayout.NORTH);
        
        JPanel pTable = new JPanel(new BorderLayout());
        String[] header= {"Mã HD","TG Tạo","TG Thanh Toán","MÃ NV","Điểm TL","Giảm Giá","Tổng Thanh Toán","Trạng Thái"};
        
        mdHD=new DefaultTableModel(header,0);
        
        tableHD= new JTable(mdHD);
        tableHD.setFont(new Font("Arial", Font.PLAIN, 15));
//        tableHD.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        pTable.add(new JScrollPane(tableHD));
        
        
        
        
        pCen.add(pTable);
        
        JPanel pCTHD = new JPanel(new BorderLayout());
        
        Box t= Box.createHorizontalBox();
//        t.add(Box.createHorizontalStrut(20));
        
        

        // Tạo Box cho phần bên trái
        Box a, a1,a2,a3,a4,a5,a6;
        a=Box.createVerticalBox();
        a1=Box.createHorizontalBox();
        a1.add(new JLabel("Mã HD: "));
        a1.add(txtMaHD = new JTextField(10));
        
        
        a2=Box.createHorizontalBox();
        a2.add(new JLabel("Mã NV: "));
        a2.add(txtMaNV = new JTextField(10));
        
        a3=Box.createHorizontalBox();
        a3.add(new JLabel("Mã KH: "));
        a3.add(txtMaKH = new JTextField(10));
        
        a4=Box.createHorizontalBox();
        a4.add(new JLabel("Tên KH: "));
        a4.add(txtTenKH = new JTextField(10));
        
        a5=Box.createHorizontalBox();
        a5.add(new JLabel("TG Vào: "));
        a5.add(txtTGTao = new JTextField(10));
        
        a6=Box.createHorizontalBox();
        a6.add(new JLabel("Tg Ra: "));
        a6.add(txtTGTT = new JTextField(10));
        a.add(a1);a.add(a2);a.add(a3);a.add(a6);a.add(a5);a.add(a4);
//        a.setPreferredSize(new Dimension(300, 200));
//        a.add(Box.createVerticalStrut(20));
//        a.setFont(new Font("Arial", Font.PLAIN, 20));
        
        t.add(a);
        
        
        
        
        
        Box c,c1,c2,c3,c4,c5;
        c=Box.createVerticalBox();
        c1=Box.createHorizontalBox();
        c1.add(new JLabel("Tổng Tiền: "));
        c1.add(txtTongTien = new JTextField(10));
        
        
        c2=Box.createHorizontalBox();
        c2.add(new JLabel("Giảm: "));
        c2.add(txtGiam = new JTextField(10));
        
        c3=Box.createHorizontalBox();
        c3.add(new JLabel("Trừ Điểm TL:"));
        c3.add(txtDTL = new JTextField(10));
        
        c4=Box.createHorizontalBox();
        c4.add(new JLabel("Khách Trả:"));
        c4.add(txtThanhtoan = new JTextField(10));
        
        c5=Box.createHorizontalBox();
        c5.add(new JLabel("Tiền Thối :"));
        c5.add(txtTienthoi = new JTextField(10));
        
        
        c.add(c1);c.add(c2);c.add(c3);c.add(c4);c.add(c5);
//        c.add(Box.createVerticalStrut(20));
//        c.setFont(new Font("Arial", Font.PLAIN, 20));
        c.setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 0));
        t.add(c);
        
        
        setTextFieldBackground(t, Color.decode("#DAB48C"));
        setTextFieldsEditable(t, false);
        setFontForComponents(t,new Font("Arial", Font.PLAIN, 20) );
        t.setPreferredSize(new Dimension(700, 200));
        t.setBorder(BorderFactory.createEmptyBorder(20, 5, 0, 0));
        t.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.RED, 2), // viền đỏ dày 2px
                "CHI TIẾT HÓA ĐƠN",                          // tiêu đề
                TitledBorder.CENTER,                            // canh trái
                TitledBorder.TOP,                             // vị trí trên
                new Font("Arial", Font.BOLD, 14),             // font tiêu đề
                Color.RED                                     // màu chữ tiêu đề
        ));

        
//        pSou.add(c);
        
        pCTHD.add(t,BorderLayout.WEST);
        
        String[] header1= {"Mã","Tên Món","Số Lượng","Giá","Thành Tiền"};
        mdMon=new DefaultTableModel(header1, 0);
        tableMon= new JTable(mdMon);
        scoll= new JScrollPane(tableMon);
        scoll.setPreferredSize(new Dimension(200, 200));
        pCTHD.add(scoll);
        pCTHD.setBorder(BorderFactory.createEmptyBorder(0, 20, 30, 20)); 
        
        
        pCen.add(pCTHD,BorderLayout.SOUTH);
        
        
        docDulieutuDBvaoTable();
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
        
        
        for (JTextField txt : new JTextField[]{txtMaHD, txtMaKH, txtMaNV, txtTGTT,txtTGTao
        		,txtTenKH,txtTongTien,txtDTL,txtGiam,txtTienthoi,txtThanhtoan}) {
            txt.setBorder(null);                    
            txt.setBackground(null);     
            txt.setForeground(Color.BLACK);        
            txt.setCaretColor(Color.BLACK);        
        }
        
        
        pDuoi.setBackground(Color.decode("#E3CFC1"));
        pCTHD.setBackground(Color.decode("#E3CFC1"));
        
        add(pDuoi,BorderLayout.SOUTH);
        add(pTil,BorderLayout.NORTH);
        tableHD.addMouseListener(this);
        tableMon.addMouseListener(this);
        txtThanhtoan.addActionListener(e -> tinhTienThoi());
        btnTim.addActionListener(this);
        btnXoa.addActionListener(this);
        
        dateTuNgay.addPropertyChangeListener("date", evt -> {
            if (dateTuNgay.getDate() != null && dateDenNgay.getDate() != null) {
                locHDtheongay();
            }
        });

        dateDenNgay.addPropertyChangeListener("date", evt -> {
            if (dateTuNgay.getDate() != null && dateDenNgay.getDate() != null) {
                locHDtheongay();
            }
        });
        
        btnBack.addActionListener(e-> {
        	this.setVisible(false);
        	new ThanhToan_GUI(null, 0, null, null);
        	
        	
        });
        
	
	}

	public static void main(String[] args) {
		 SwingUtilities.invokeLater(() -> {
		        new LichsuThanhToan().setVisible(true);
		    });

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
	private void setFontForComponents(Box box, Font font) {
	    for (Component component : box.getComponents()) {
	        if (component instanceof Box) {
	            // Nếu là Box, gọi đệ quy
	            setFontForComponents((Box) component, font);
	        } else if (component instanceof JLabel) {
	            // Nếu là JLabel, thiết lập font
	            ((JLabel) component).setFont(font);
	        } else if (component instanceof JTextField) {
	            // Nếu là JTextField, thiết lập font
	            ((JTextField) component).setFont(font);
	        }
	    }
	}
	
	public void docDulieutuDBvaoTable() {
		List<HoaDon> list= hd_dao.getAllHoaDon();
		for (HoaDon hd : list) {
	        mdHD.addRow(new Object[] {
	            hd.getMaHD(),
	            hd.getThoiGianVao(),
	            hd.getThoiGianRa(),
	            hd.getMaNV() != null ? hd.getMaNV().getMaNV() : "",
	            hd.getDiemTL(),
	            hd.getGiamGia(),
	            String.format("%,.0f", hd.getTongTien()),
	            hd.isTrangThai() ? "Đã thanh toán" : "Chưa thanh toán"
	        });
	    }
		
	}
	@Override
	public void mouseClicked(MouseEvent e) {
	    int row = tableHD.getSelectedRow();
	    if (row >= 0) {
	        String maHD = tableHD.getValueAt(row, 0).toString();
	        HoaDon hd = hd_dao.getHoaDonTheoMa(maHD);

	        if (hd != null) {
	        	
	            txtMaHD.setText(hd.getMaHD());
	            txtTGTao.setText(hd.getThoiGianVao() != null ? hd.getThoiGianVao().toString() : "");
	            txtTGTT.setText(hd.getThoiGianRa() != null ? hd.getThoiGianRa().toString() : "");
	            txtMaNV.setText(hd.getMaNV() != null ? hd.getMaNV().getMaNV() : "");
	            txtMaKH.setText(hd.getMaKH() != null ? hd.getMaKH().getMaKH() : "");
	            txtGiam.setText(String.valueOf(hd.getGiamGia()));
	            txtTongTien.setText(String.format("%,.0f VND", hd.getTongTien()));
	            
	            txtTenKH.setText(hd.getMaKH() != null ? hd.getMaKH().getTenKH() : "");
	            txtDTL.setText(String.valueOf(hd.getDiemTL()));
	            txtThanhtoan.setText(String.format("%,.0f VND", hd.getTienKhachTra()));
	            txtTienthoi.setText("");

	            tinhTienThoi();

	            // load chi tiết món
	            mdMon.setRowCount(0);
                List<ChiTietHoaDon> dsMon = cthd_dao.getChiTietTheomahd2(maHD);
                for (ChiTietHoaDon ct : dsMon) {
                    double thanhTien = ct.getSoLuong() * ct.getSanPham().getDonGia();
                    mdMon.addRow(new Object[] {
                        ct.getSanPham().getMaSP(),
                        ct.getSanPham().getTenSP(),
                        ct.getSoLuong(),
                        String.format("%,.0f", ct.getSanPham().getDonGia()),
                        String.format("%,.0f", thanhTien)
                    });
                }
	        }
	    }
	}



	private void tinhTienThoi() {
		try {
			double tong= Double.parseDouble(txtTongTien.getText().replace(",", "").replace(" VND", "").trim());
			double giam= Double.parseDouble(txtGiam.getText().replace(",", "").trim());
			double thanhtoan= Double.parseDouble(txtThanhtoan.getText().replace(",", "").replace(" VND", "").trim());;
			
			double tienGiamgia= tong*(1-giam/100);
			
			double tienThoi= thanhtoan-tienGiamgia;
			
			if (tienThoi<0) {
				JOptionPane.showMessageDialog(this, "khách chưa trả đủ tiền","Cảnh báo ",JOptionPane.WARNING_MESSAGE);
				txtTienthoi.setText("0");
			} else {
				
				txtTienthoi.setText(String.format("%,.0f VND", tienThoi));

			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Cần nhập đúng dạng số");
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
		Object o=e.getSource();
		if (o.equals(btnXoa)) {
			xoa();
		
		}
		else if (o.equals(btnTim)) {
			timKiem();
			
		}
		
	}
	
	public void xoa() {
		int row= tableHD.getSelectedRow();
		if (row<0) {
			JOptionPane.showMessageDialog(this, "Hãy chọn hóa đơn để xóa");
			return ;
			
		}
		
		String ma= tableHD.getValueAt(row, 0).toString();
		int confirm= JOptionPane.showConfirmDialog(this, "Có chắc chắn muốn xóa hóa đơn [" + ma + "] không?",
				"Xác nhận xóa",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE
		);
		
		if (confirm!=JOptionPane.YES_OPTION) {
			return;
		}
		
		boolean re= hd_dao.xoaHoaDon(ma);
		if (re) {
			DefaultTableModel md= (DefaultTableModel) tableHD.getModel();
			md.removeRow(row);
			JOptionPane.showMessageDialog(this, "Đã xóa hóa đơn thành công");
		}
		else {
			JOptionPane.showMessageDialog(this, "Xóa hóa đơn thất bại");

		}
		
		
	}
	 
	public void timKiem() {
		String ma= txtTim.getText().trim();
		if (ma.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Cần nhập mã để tìm ");
			txtTim.requestFocus();
			return;
		}
		
		dateTuNgay.setDate(null);
	    dateDenNgay.setDate(null);
	    DefaultTableModel md = (DefaultTableModel) tableHD.getModel();
	    md.setRowCount(0);
	    List<HoaDon> list = hd_dao.getAllHoaDon();
	    for (HoaDon hd : list) {
	        md.addRow(new Object[] {
	            hd.getMaHD(),
	            hd.getThoiGianVao(),
	            hd.getThoiGianRa(),
	            hd.getMaNV() != null ? hd.getMaNV().getMaNV() : "",
	            hd.getGiamGia(),
	            String.format("%,.0f", hd.getTongTien()),
	            hd.isTrangThai() ? "Đã thanh toán" : "Chưa thanh toán"
	        });
	    }
	    
	    boolean find = false;
	    for (int i = 0; i < md.getRowCount(); i++) {
	        String maHD = md.getValueAt(i, 0).toString();
	        if (maHD.equalsIgnoreCase(ma)) {
	            tableHD.setRowSelectionInterval(i, i);
	            tableHD.scrollRectToVisible(tableHD.getCellRect(i, 0, true));
	            find = true;
	            break;
	        }
	    }
		
		if (!find) {
			JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn có mã "+ma);
		}
	}
	
	
	public void locHDtheongay() {
		
		Date tuNgay = dateTuNgay.getDate();
	    Date denNgay = dateDenNgay.getDate();
		
		
		if (tuNgay== null || denNgay==null) {
			JOptionPane.showMessageDialog(this, "Phải nhập đầy đủ 2 ô thời gian");
			return;
		}
		
		else if (tuNgay.after(denNgay)) {
			JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải trước ngày cuối");
			return;
		}
		
		List<HoaDon> ds= hd_dao.getHDtheoNgay(tuNgay, denNgay);
		DefaultTableModel md= (DefaultTableModel) tableHD.getModel();
		md.setRowCount(0);
		if (ds.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Không có hóa đơn trong khoảng ngày đó");
			
			return;
		}
		
		
		for (HoaDon h : ds) {
			md.addRow(new Object[] {
					h.getMaHD(),h.getThoiGianVao(),h.getThoiGianRa(),
					h.getMaNV()!=null ? h.getMaNV().getMaNV() : "",
							h.getGiamGia(),
							String.format("%,.0f", h.getTongTien()),
				            h.isTrangThai() ? "Đã thanh toán" : "Chưa thanh toán"
			});
			
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	

}
