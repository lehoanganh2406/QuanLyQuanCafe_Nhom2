package GUI;

import javax.swing.*;
import javax.swing.table.*;
import connectDB.ConnectDB;
import dao.Order_DAO;
import entity.SanPham;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.List;

public class Order_GUI extends JFrame implements ActionListener {

	private static final String CARD_ALL     = "ALL";
    private static final String CARD_COFFEE  = "COFFEE";
    private static final String CARD_TRA     = "TRA";
    private static final String CARD_TRASUA  = "TRASUA";
    private static final String CARD_NUOCEP  = "NUOCEP";
    private static final String CARD_BANH    = "BANH";
    private static final String CARD_KHAC    = "KHAC";
    
    private final Order_DAO orderDAO = Order_DAO.getInstance();
    
	private ImageIcon iconTimKiem = new ImageIcon(getClass().getResource("/img/iconTimKiem.png"));
    private ImageIcon iconChuyenBan = new ImageIcon(getClass().getResource("/img/chuyenban.png"));
    private ImageIcon iconQuayLai = new ImageIcon(getClass().getResource("/img/back_16.png"));
    private ImageIcon iconThanhToan = new ImageIcon(getClass().getResource("/img/bill_16.png"));
    private ImageIcon iconBoMon = new ImageIcon(getClass().getResource("/img/trash_16.png"));
	private int soBan;
	
	private JPanel jWes, jCen, pNor, pCen, jEst;
	private JButton btnTatCa,btnCoffee, btnTra, btnTraSua, btnNuocEp, btnBanh, btnKhac;
	private JLabel lblBan;
	private CardLayout cenCards = new CardLayout();
	private final Map<String,String> maLoaiByTen = new HashMap<>();
	private JLabel lblTongSL, lblTongTien;
	private JTable tblCart;
	private DefaultTableModel cartModel;
	private JButton  btnTim, btnChuyenBan, btnQuayLai, btnRemove, btnThanhToan ;
	private JTextField txtTimKiem;
    public Order_GUI(int soBan) {
        this.soBan = soBan;
        setTitle("Menu - Bàn " + soBan);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        napMaLoaiFromDB();
        thanhTieuDe();
        thanhBenTrai();
        menuCenter(); 
        thanhBenPhai();
        btnTatCa.addActionListener(this);
        btnCoffee.addActionListener(this);
        btnTra.addActionListener(this);
        btnTraSua.addActionListener(this);
        btnNuocEp.addActionListener(this);
        btnBanh.addActionListener(this);
        btnKhac.addActionListener(this);

        btnTim.addActionListener(this);
        txtTimKiem.addActionListener(this);
        btnChuyenBan.addActionListener(this);
        btnQuayLai.addActionListener(this);
        btnRemove.addActionListener(this);
        btnThanhToan.addActionListener(this);
    }

    public static void main(String[] args) {
        ConnectDB.getInstance().connect();
        SwingUtilities.invokeLater(() -> new Order_GUI(1).setVisible(true));
    }
//    Thanh tiêu đề
    private void thanhTieuDe() {
		PanelTieuDe tieude = new PanelTieuDe("Order", "/img/iconOrder.png");
		add(tieude, BorderLayout.NORTH);
	}
//    Thanh Bên Trái
    private void thanhBenTrai() {
		jWes = new JPanel(new GridLayout(12,1,10,10));
		jWes.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		jWes.add(btnTatCa= taoNutMenu("Tất cả"));
        jWes.add(btnCoffee = taoNutMenu("Coffee"));
        jWes.add(btnTra    = taoNutMenu("Trà"));
        jWes.add(btnTraSua = taoNutMenu("Trà Sữa"));
        jWes.add(btnNuocEp = taoNutMenu("Nước Ép"));
        jWes.add(btnBanh   = taoNutMenu("Bánh"));
        jWes.add(btnKhac   = taoNutMenu("Khác"));
        jWes.setPreferredSize(new Dimension(170, getHeight()));
        add(jWes, BorderLayout.WEST);
		
	}
    private JButton taoNutMenu(String text) {
		JButton btn = new JButton(text);
		btn.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btn.setBackground(Color.white);
        btn.setFocusPainted(false); // tắt viền
        btn.setContentAreaFilled(true); // không màu nền
        btn.setOpaque(true);
        btn.setBorderPainted(false);
		return btn;
	}
//    Center
    private void menuCenter() {
    	 jCen = new JPanel(new BorderLayout());
    	 pNor = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 8));
         pNor.setBorder(BorderFactory.createEmptyBorder(10,16,6,16));
         txtTimKiem = new JTextField();
         btnTim = taoNut("Tìm", iconTimKiem);
         btnTim.setPreferredSize(new Dimension(140, 40));
         pNor.add(txtTimKiem);
         pNor.add(btnTim);
         pNor.setBackground(new Color(255,228,196));
         txtTimKiem.setPreferredSize(new Dimension(400, 40));
         txtTimKiem.setFont(new Font("Montserrat", Font.PLAIN, 16));
         txtTimKiem.setText("Nhập sản phẩm cần tìm...");
         txtTimKiem.setBackground(Color.WHITE);
         txtTimKiem.setForeground(Color.GRAY);
         txtTimKiem.setBorder(BorderFactory.createCompoundBorder(
                 BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
                 BorderFactory.createEmptyBorder(6, 12, 6, 12)
         ));
         txtTimKiem.addFocusListener(new FocusAdapter() {
        	 // Click vào ô nhập
             @Override public void focusGained(FocusEvent e) {
                 if (txtTimKiem.getText().equals("Nhập sản phẩm cần tìm...")) {
                     txtTimKiem.setText(""); 
                     txtTimKiem.setForeground(Color.BLACK);
                 }
             }
             // Rời khỏi ô nhập
             @Override public void focusLost(FocusEvent e) {
                 if (txtTimKiem.getText().isEmpty()) {
                     txtTimKiem.setText("Nhập sản phẩm cần tìm..."); txtTimKiem.setForeground(Color.GRAY);
                 }
             }
         });
         pNor.add(Box.createHorizontalStrut(60));
         lblBan = new JLabel("Bàn " + soBan);
         lblBan.setFont(new Font("Times New Roman", Font.BOLD, 30));
         pNor.add(lblBan);

         pNor.add(Box.createHorizontalStrut(40));
         btnChuyenBan = taoNut("Chuyển Bàn", iconChuyenBan);
         btnChuyenBan.setPreferredSize(new Dimension(200, 40));
         pNor.add(btnChuyenBan);
         
         pCen = new JPanel(cenCards );
         pCen.setBackground(Color.decode("#E3CFC1"));

         jCen.add(pNor, BorderLayout.NORTH);
         jCen.add(pCen,  BorderLayout.CENTER);
         add(jCen, BorderLayout.CENTER);
         // nạp dữ liệu
         napDuLieuVaoCard(CARD_ALL);
         napDuLieuVaoCard(CARD_COFFEE);
         napDuLieuVaoCard(CARD_TRA);
         napDuLieuVaoCard(CARD_TRASUA);
         napDuLieuVaoCard(CARD_NUOCEP);
         napDuLieuVaoCard(CARD_BANH);
         napDuLieuVaoCard(CARD_KHAC);

         mauNut();
	}
//    Thanh Bên Phải
    private void thanhBenPhai() {
        jEst = new JPanel(new BorderLayout());
        jEst.setBackground(Color.decode("#E3CFC1"));

        String[] cols = {"Mã món","Tên món", "SL", "Đơn giá", "Thành tiền"};
        cartModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
            @Override public Class<?> getColumnClass(int c) {
                return switch (c) {
                    case 0 -> String.class;  // mã món (SPxxx)
                    case 2 -> Integer.class; // SL
                    case 3,4 -> Long.class;  // giá
                    default -> String.class; // tên món
                };
            }
        };

        tblCart = new JTable(cartModel);
        tblCart.setRowHeight(36);
        tblCart.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        tblCart.setDefaultEditor(Object.class, null);
        TableColumnModel colss = tblCart.getColumnModel();
        colss.getColumn(0).setPreferredWidth(110);
        colss.getColumn(1).setPreferredWidth(220);
        colss.getColumn(2).setPreferredWidth(60);
        colss.getColumn(3).setPreferredWidth(120);
        colss.getColumn(4).setPreferredWidth(140);
        TableCellRenderer vndR = new VNDRenderer();
        tblCart.getColumnModel().getColumn(3).setCellRenderer(vndR);
        tblCart.getColumnModel().getColumn(4).setCellRenderer(vndR);

        JTableHeader header = tblCart.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 48));
        header.setFont(new Font("Times New Roman", Font.BOLD, 20));
        header.setOpaque(true);
        header.setBackground(Color.decode("#EDE7E3"));
        header.setBorder(BorderFactory.createEmptyBorder());

        JScrollPane scroll = new JScrollPane(tblCart);
        jEst.add(scroll, BorderLayout.CENTER);
        tblCart.addMouseListener(new MouseAdapter() {
			@Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 1) return;
                int viewRow = tblCart.rowAtPoint(e.getPoint());
                int viewCol = tblCart.columnAtPoint(e.getPoint());
                if (viewRow < 0 || viewCol != 2) return;

                int modelRow = tblCart.convertRowIndexToModel(viewRow);
                int curSL = (Integer) cartModel.getValueAt(modelRow, 2);
                long donGia = (Long) cartModel.getValueAt(modelRow, 3);

                String input = JOptionPane.showInputDialog(Order_GUI.this, "Nhập số lượng (>= 1):", curSL);
                if (input == null) return;
                input = input.trim();
                if (!input.matches("\\d+")) {
                    JOptionPane.showMessageDialog(Order_GUI.this, "Vui lòng nhập số nguyên hợp lệ."); return;
                }
                int sl = Integer.parseInt(input);
                if (sl < 1) {
                    JOptionPane.showMessageDialog(Order_GUI.this, "Số lượng không hợp lệ."); return;
                }
                cartModel.setValueAt(sl, modelRow, 2);
                cartModel.setValueAt(sl * donGia, modelRow, 4);
                tinhTong();
            }
        });

        JPanel south = new JPanel(new BorderLayout());
        south.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel total = new JPanel(new GridLayout(2,2,8,8));
        JLabel lblSL = new JLabel("Tổng SL:"); lblSL.setFont(new Font("Times New Roman", Font.BOLD, 26));
        total.add(lblSL);
        lblTongSL = new JLabel("0", SwingConstants.RIGHT); lblTongSL.setFont(new Font("Times New Roman", Font.BOLD, 26));
        total.add(lblTongSL);
        JLabel lblTT = new JLabel("Tổng tiền:"); lblTT.setFont(new Font("Times New Roman", Font.BOLD, 26));
        total.add(lblTT);
        lblTongTien = new JLabel("0", SwingConstants.RIGHT); lblTongTien.setFont(new Font("Times New Roman", Font.BOLD, 26));
        total.add(lblTongTien);

        JPanel buttonsTrai = new JPanel(new FlowLayout(FlowLayout.LEFT,10,0));
        btnQuayLai = taoNut("Quay lại", iconQuayLai);
        buttonsTrai.add(btnQuayLai);
        btnQuayLai.setPreferredSize(new Dimension(140, 40));

        JPanel buttonsPhai = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0));
        btnRemove = taoNut("Bỏ món", iconBoMon);
        btnThanhToan = taoNut("Thanh toán", iconThanhToan);
        buttonsPhai.add(btnRemove); buttonsPhai.add(btnThanhToan);
        btnRemove.setPreferredSize(new Dimension(140, 40));
        btnThanhToan.setPreferredSize(new Dimension(170, 40));

        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.add(buttonsTrai, BorderLayout.WEST);
        bottomBar.add(buttonsPhai, BorderLayout.EAST);
        south.add(total, BorderLayout.NORTH);
        south.add(bottomBar, BorderLayout.SOUTH);

        jEst.add(south, BorderLayout.SOUTH);
        jEst.setPreferredSize(new Dimension(400, getHeight()));
        add(jEst, BorderLayout.EAST);

        
    }
    private JButton taoNut(String ten, ImageIcon icon) {
        JButton btn = new JButton(ten, icon);
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Times New Roman", Font.BOLD, 18));
        return btn;
    }
    private void mauNut() {
        for (Component c : jWes.getComponents()) {
            if (c instanceof JButton b) b.setBackground(Color.white);
        }
        btnTatCa.setBackground(new Color(255,228,196));
        cenCards.show(pCen, CARD_ALL);
    }

    private String chonMaLoai(String key) {
        return switch (key) {
            case CARD_COFFEE -> maLoaiByTen.get("Coffee");
            case CARD_TRA    -> maLoaiByTen.get("Trà");
            case CARD_TRASUA -> maLoaiByTen.get("Trà sữa");
            case CARD_NUOCEP -> maLoaiByTen.get("Nước ép");
            case CARD_BANH   -> maLoaiByTen.get("Bánh");
            case CARD_KHAC   -> maLoaiByTen.get("Khác");
            default -> null;
        };
    }
    private void napDuLieuVaoCard(String cardKey) {
		List<SanPham> list;
		String maLoai = chonMaLoai(cardKey);
		if (CARD_ALL.equals(cardKey)) {
			list = orderDAO.getAllSanPham();
		}else {
			list = orderDAO.getSanPhamByLoai(maLoai);
		}
		JComponent pane = gridForm(new ArrayList<>(list), maLoai);
        pCen.add(pane, cardKey);

	}
    private JPanel cardOrder(SanPham o) {
    	JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(280, 270));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220,210,200), 1));
        
        JLabel lblImg = taiAnh(o.getImg());

        JPanel info = new JPanel(new BorderLayout());
        info.setBackground(Color.WHITE);
        info.setBorder(BorderFactory.createEmptyBorder(8,10,8,10));

        JLabel lblName = new JLabel(o.getTenSP());
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 20));

        JLabel lblPrice = new JLabel(formatVND(o.getDonGia()));
        lblPrice.setForeground(Color.RED);
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblPrice.setHorizontalAlignment(SwingConstants.RIGHT);

        info.add(lblName, BorderLayout.WEST);
        info.add(lblPrice, BorderLayout.EAST);

        card.add(lblImg, BorderLayout.CENTER);
        card.add(info, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { addToCart(o); }
        });

        return card;

	}
    private void napMaLoaiFromDB() {
        // tên -> mã
        String[] tenLoais = {"Coffee","Trà","Trà sữa","Nước ép","Bánh","Khác"};
        Connection con = ConnectDB.getInstance().getConnection();
        try (
             PreparedStatement ps = con.prepareStatement(
                     "SELECT maLoai, loaiSP FROM dbo.LoaiSanPham WHERE loaiSP IN (N'Coffee',N'Trà',N'Trà sữa',N'Nước ép',N'Bánh',N'Khác')")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    maLoaiByTen.put(rs.getString("loaiSP"), rs.getString("maLoai")); // loaiSP -> LSPxxx
                }
            }
            // fallback nếu thiếu tên nào
            for (String t : tenLoais) maLoaiByTen.putIfAbsent(t, null);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void addToCart(SanPham o) {
        String ma = o.getMaSP();            // String
        String ten = o.getTenSP();
        long donGia = Math.round(o.getDonGia());

        // Kiểm tra trùng mã
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            String maRow = (String) cartModel.getValueAt(i, 0);
            if (ma.equals(maRow)) {
                int sl = (Integer) cartModel.getValueAt(i, 2) + 1;
                cartModel.setValueAt(sl, i, 2);
                cartModel.setValueAt(sl * donGia, i, 4);
                tinhTong();
                return;
            }
        }
        // Thêm mới
        cartModel.addRow(new Object[]{ ma, ten, 1, donGia, donGia });
        tinhTong();
    }
    private void tinhTong() {
        int tongSL = 0; long tongTien = 0;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            tongSL += (Integer) cartModel.getValueAt(i, 2);
            tongTien += (Long) cartModel.getValueAt(i, 4);
        }
        lblTongSL.setText(String.valueOf(tongSL));
        lblTongTien.setText(String.format("%,d", tongTien).replace(',', '.'));
    }

    private JLabel taiAnh(String imgPath) {
        JLabel lbl = new JLabel("", SwingConstants.CENTER);
        lbl.setPreferredSize(new Dimension(320, 225));
        String p = (imgPath == null || imgPath.isBlank()) ? null : (imgPath.startsWith("/") ? imgPath : "/img/" + imgPath);
        java.net.URL url = (p != null) ? getClass().getResource(p) : null;
        ImageIcon icon = (url != null) ? new ImageIcon(url) : null;
        if (icon != null) {
            Image scaled = icon.getImage().getScaledInstance(320, 225, Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(scaled));
        } else {
            lbl.setText("No Image"); lbl.setOpaque(true); lbl.setBackground(Color.WHITE);
        }
        return lbl;
    }

    private JPanel luoiGridPanel(ArrayList<SanPham> items) {
    	int hgap = 60, vgap = 40, cols = 3;
        JPanel grid = new JPanel(new GridLayout(0, cols, hgap, vgap));
        grid.setBackground(Color.decode("#E3CFC1"));
        for (SanPham o : items) {
        	grid.add(cardOrder(o));
        }
        return grid;
	}
    private JComponent gridForm(ArrayList<SanPham> items, String maLoai) {
        JPanel grid = luoiGridPanel(items);
        JScrollPane scroll = new JScrollPane(grid, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.putClientProperty("maLoai", maLoai); 
        return scroll;
    }
    private JScrollPane layScrollDangHienThi() {
        for (Component comp : pCen.getComponents()) {
            if (comp.isVisible() && comp instanceof JScrollPane sp) return sp;
        }
        return null;
    }
    private void noiDungTab() {
        JScrollPane sp = layScrollDangHienThi();
        if (sp == null) return;

        Object val = sp.getClientProperty("maLoai");
        String maLoai = (val instanceof String) ? (String) val : null;

        ArrayList<SanPham> ds = (maLoai == null)
                ? orderDAO.getAllSanPham()
                : orderDAO.getSanPhamByLoai(maLoai);

        JPanel newGrid = luoiGridPanel(ds);
        sp.setViewportView(newGrid);
        newGrid.revalidate(); newGrid.repaint();
    }

    private String formatVND(double vnd) {
        long v = Math.round(vnd);
        return String.format("%,d", v).replace(',', '.');
    }
    private class VNDRenderer extends DefaultTableCellRenderer {
        @Override protected void setValue(Object value) {
            if (value instanceof Number n) setText(formatVND(n.doubleValue()));
            else setText("");
            setHorizontalAlignment(SwingConstants.RIGHT);
        }
    }
    private void chonTab(JButton btn, String cardKey) {
        for (Component c : jWes.getComponents()) {
            if (c instanceof JButton) c.setBackground(Color.WHITE);
        }
        btn.setBackground(new Color(255,228,196));
        cenCards.show(pCen, cardKey);
    }
    private void chuyenBan() {
        String input = JOptionPane.showInputDialog(this, "Nhập số bàn mới:", String.valueOf(soBan));
        if (input != null && input.trim().matches("\\d+")) {
            int banMoi = Integer.parseInt(input.trim());
            if (banMoi <= 0 || banMoi > 40) {
                JOptionPane.showMessageDialog(this, "Số bàn không hợp lệ!"); return;
            }
            soBan = banMoi;
            lblBan.setText("Bàn " + soBan);
        }
    }
    private void xoaMonTrongGio() {
        int viewRow = tblCart.getSelectedRow();
        if (viewRow < 0) {
            JOptionPane.showMessageDialog(this, "Hãy chọn món để xóa!");
            return;
        }
        int modelRow = tblCart.convertRowIndexToModel(viewRow);
        String ten = (String) cartModel.getValueAt(modelRow, 1);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn xóa \"" + ten + "\"?", "Xác nhận", JOptionPane.OK_CANCEL_OPTION);
        if (confirm != JOptionPane.OK_OPTION) return;
        cartModel.removeRow(modelRow);
        tinhTong();
    }
    private void moManHinhThanhToan() {
        if (cartModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống!");
            return;
        }
        ArrayList<Object[]> items = new ArrayList<>();
        long tongTien = 0L;
        for (int i = 0; i < cartModel.getRowCount(); i++) {
            items.add(new Object[]{
                    cartModel.getValueAt(i, 0),
                    cartModel.getValueAt(i, 1),
                    cartModel.getValueAt(i, 2),
                    cartModel.getValueAt(i, 3),
                    cartModel.getValueAt(i, 4)
            });
            tongTien += (Long) cartModel.getValueAt(i, 4);
        }
        ThanhToan_GUI tt = new ThanhToan_GUI(this, soBan, items, tongTien);
        tt.setVisible(true);
    }
    private void timKiem() {
    	String keyword = txtTimKiem.getText().trim();
        JScrollPane visibleScroll = layScrollDangHienThi();
        if (visibleScroll == null) return;

        if (keyword.isEmpty() || keyword.equalsIgnoreCase("Nhập sản phẩm cần tìm...")) {
            JOptionPane.showMessageDialog(this, "Bạn chưa nhập món cần tìm!");
            noiDungTab();
            return;
        }

        Object val = visibleScroll.getClientProperty("maLoai");
        String maLoai = (val instanceof String) ? (String) val : null;

        ArrayList<SanPham> ds;
        if (maLoai == null)
            ds = orderDAO.searchByName(keyword);
        else
            ds = orderDAO.searchByNameAndLoai(keyword, maLoai);

        if (ds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy \"" + keyword + "\" trong tab hiện tại.");
            noiDungTab();
            return;
        }

        JPanel newGrid = luoiGridPanel(ds);
        visibleScroll.setViewportView(newGrid);
        newGrid.revalidate();
        newGrid.repaint();
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnTatCa)) {
	        chonTab(btnTatCa, CARD_ALL);
	    } else if (o.equals(btnCoffee)) {
	        chonTab(btnCoffee, CARD_COFFEE);
	    } else if (o.equals(btnTra)) {
	        chonTab(btnTra, CARD_TRA);
	    } else if (o.equals(btnTraSua)) {
	        chonTab(btnTraSua, CARD_TRASUA);
	    } else if (o.equals(btnNuocEp)) {
	        chonTab(btnNuocEp, CARD_NUOCEP);
	    } else if (o.equals(btnBanh)) {
	        chonTab(btnBanh, CARD_BANH);
	    } else if (o.equals(btnKhac)) {
	        chonTab(btnKhac, CARD_KHAC);
	    } else if (o.equals(btnTim) || o.equals(txtTimKiem)) {
			timKiem();
		} else if (o.equals(btnChuyenBan)) {
			chuyenBan();
		} else if (o.equals(btnRemove)) {
			xoaMonTrongGio();
		} else if (o.equals(btnThanhToan)) {
			moManHinhThanhToan();
		} else if (o.equals(btnQuayLai)) {
			new Ban_GUI().setVisible(true);
			dispose();
		}
		
	}
	
}