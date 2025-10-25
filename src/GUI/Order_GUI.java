package GUI;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;

import connectDB.ConnectDB;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import dao.*;
import entity.*;
public class Order_GUI extends JFrame {
	private JButton btnMenu;
	private JLabel lblOrder;
	private Order_DAO orderDAO = new Order_DAO();
	// Sidebar trái
    private JPanel jWes;
    private JButton btnCoffee, btnTra, btnTraSua, btnNuocEp, btnBanh, btnKhac;
    
	private ImageIcon iconTimKiem = new ImageIcon(getClass().getResource("/img/iconTimKiem.png"));
	private ImageIcon iconChuyenBan = new ImageIcon(getClass().getResource("/img/chuyenban.png"));
	// Center
    private JPanel jCen;         // container: NORTH = search, CENTER = pCen
    private JPanel pNor;         // thanh search
    private JPanel pCen;         // CardLayout cho từng loại
    private final CardLayout cenCards = new CardLayout();
    // Phải
    private JPanel jEst;
    private JTable tblCart;
    private DefaultTableModel cartModel;
    private JLabel lblTongSL, lblTongTien;
	private int soBan;
	private JLabel lblBan;
	// Keys nhóm
    private static final String CARD_COFFEE  = "COFFEE";
    private static final String CARD_TRA     = "TRA";
    private static final String CARD_TRASUA  = "TRASUA";
    private static final String CARD_NUOCEP  = "NUOCEP";
    private static final String CARD_BANH    = "BANH";
    private static final String CARD_KHAC    = "KHAC";
    
	public Order_GUI(int soBan) {
		this.soBan = soBan;
		setTitle("Menu - Bàn " + soBan);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        thanhTieuDe();
        thanhBenTrai();
        MenuCenter();
        thanhBenPhai();
        
	}
	public static void main(String[] args) {
		ConnectDB.getInstance().connect();
		SwingUtilities.invokeLater(() -> new Order_GUI(1).setVisible(true));
	}
	// ========== Thanh tiêu đề =========
	public void thanhTieuDe() {
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
        lblOrder.setBackground(Color.decode("#E3CFC1"));  // be nhạt
        lblOrder.setFont(new Font("Times New Roman", Font.PLAIN, 32));
        lblOrder.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12)); // padding
        lblOrder.setForeground(Color.BLACK);
        lblOrder.setOpaque(true);   // hirnt thị nền
        lblOrder.setIconTextGap(10); // khoảng cách chữ và icon
        lblOrder.setHorizontalAlignment(SwingConstants.LEFT);
        
        
        jNor.add(jNorLeft, BorderLayout.WEST);
        jNor.add(jNorCen, BorderLayout.CENTER);
        add(jNor, BorderLayout.NORTH);
        
	}
	 // ===== Sidebar trái =====
    private void thanhBenTrai() {
    	
        jWes = new JPanel(new GridLayout(12,1,10,10));
        jWes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jWes.add(btnCoffee = taoNutMenu("Coffee"));
        jWes.add(btnTra    = taoNutMenu("Trà"));
        jWes.add(btnTraSua = taoNutMenu("Trà Sữa"));
        jWes.add(btnNuocEp = taoNutMenu("Nước Ép"));
        jWes.add(btnBanh   = taoNutMenu("Bánh"));
        jWes.add(btnKhac   = taoNutMenu("Khác"));
        jWes.setPreferredSize(new Dimension(170, getHeight()));
        add(jWes, BorderLayout.WEST);
	}
    
 // ===== Tạo nút sidebar =====
    private JButton taoNutMenu(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btn.setBackground(Color.white);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setIconTextGap(12);
        btn.setMargin(new Insets(0, 12, 0, 12));

        Color defaultColor  = Color.white;
        Color hoverColor    = new Color(253,245,230); // light
        Color selectedColor = new Color(255,228,196); // đậm hơn chút

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent evt) {
                if (btn.getBackground().equals(defaultColor)) {
                    btn.setBackground(hoverColor);
                }
            }
            @Override public void mouseExited(MouseEvent evt) {
                if (!btn.getBackground().equals(selectedColor)) {
                    btn.setBackground(defaultColor);
                }
            }
        });

        btn.addActionListener(e -> {
            for (Component c : jWes.getComponents()) {
                if (c instanceof JButton) c.setBackground(defaultColor);
            }
            btn.setBackground(selectedColor);

            switch (text) {
                case "Coffee"  -> cenCards.show(pCen, CARD_COFFEE);
                case "Trà"     -> cenCards.show(pCen, CARD_TRA);
                case "Trà Sữa" -> cenCards.show(pCen, CARD_TRASUA);
                case "Nước Ép" -> cenCards.show(pCen, CARD_NUOCEP);
                case "Bánh"    -> cenCards.show(pCen, CARD_BANH);
                case "Khác"    -> cenCards.show(pCen, CARD_KHAC);
            }
            pCen.revalidate();
            pCen.repaint();
        });

        return btn;
    }
    
 // ===== Ánh xạ nút → card (set selected mặc định) =====
    private void mapButtonToCard() {
        // giả sử mặc định chọn Coffee
        for (Component c : jWes.getComponents()) {
            if (c instanceof JButton b) b.setBackground(Color.white);
        }
        btnCoffee.setBackground(new Color(255,228,196));
        cenCards.show(pCen, CARD_COFFEE);
    }
    private void MenuCenter() {
    	jCen = new JPanel(new BorderLayout());

        // Thanh tìm kiếm (đơn giản)
        pNor = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 8));
        pNor.setBorder(BorderFactory.createEmptyBorder(10,16,6,16));
        JTextField txtTimKiem = new JTextField();
        JButton btnTim = new JButton("Tìm",iconTimKiem);
        pNor.add(txtTimKiem, BorderLayout.CENTER);
        pNor.add(btnTim,  BorderLayout.EAST);
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
        btnTim.setPreferredSize(new Dimension(140, 40));
        btnTim.setBackground(Color.WHITE);
        btnTim.setForeground(Color.BLACK);
        btnTim.setFocusPainted(false);
        btnTim.setFont(new Font("Times New Roman", Font.BOLD, 18));

        pNor.add(Box.createHorizontalStrut(60)); 

        lblBan = new JLabel("Bàn " + soBan);
        lblBan.setFont(new Font("Times New Roman", Font.BOLD, 30));
        pNor.add(lblBan);
        
        pNor.add(Box.createHorizontalStrut(40)); 
        JButton btnChuyenBan = new JButton("Chuyển bàn", iconChuyenBan);
        btnChuyenBan.setPreferredSize(new Dimension(200, 40));
        btnChuyenBan.setBackground(Color.WHITE);
        btnChuyenBan.setForeground(Color.BLACK);
        btnChuyenBan.setFocusPainted(false);  
        btnChuyenBan.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        pNor.add(btnChuyenBan);
        txtTimKiem.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txtTimKiem.getText().equals("Nhập sản phẩm cần tìm...")) {
                	txtTimKiem.setText("");
                	txtTimKiem.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (txtTimKiem.getText().isEmpty()) {
                	txtTimKiem.setText("Nhập sản phẩm cần tìm...");
                	txtTimKiem.setForeground(Color.GRAY);
                }
            }
        });
        btnChuyenBan.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(
                    this,
                    "Nhập số bàn mới:",
                    String.valueOf(soBan)
            );
            if (input != null && input.trim().matches("\\d+")) {
                int banMoi = Integer.parseInt(input.trim());

                if (banMoi <= 0 || banMoi>25) {
                    JOptionPane.showMessageDialog(this, "Số bàn không hợp lệ!");
                    return;
                }

                soBan = banMoi;
                lblBan.setText("Bàn " + soBan);

            }
        });
     // Khu card
        pCen = new JPanel(cenCards);
        pCen.setBackground(Color.decode("#E3CFC1"));

        jCen.add(pNor, BorderLayout.NORTH);
        jCen.add(pCen,  BorderLayout.CENTER);
        add(jCen, BorderLayout.CENTER);

        // ===== NẠP DỮ LIỆU TỪ DB THEO LOẠI =====
        napDuLieuVaoCard(CARD_COFFEE);
        napDuLieuVaoCard(CARD_TRA);
        napDuLieuVaoCard(CARD_TRASUA);
        napDuLieuVaoCard(CARD_NUOCEP);
        napDuLieuVaoCard(CARD_BANH);
        napDuLieuVaoCard(CARD_KHAC);

        // Hiển thị mặc định
        mapButtonToCard();
        
	}
    private void thanhBenPhai() {
    	jEst = new JPanel(new BorderLayout());
        jEst.setPreferredSize(new Dimension(420, getHeight()));
        jEst.setBackground(Color.decode("#E3CFC1"));

        String[] cols = {"Tên món", "SL", "Thành tiền"};
        cartModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 1; } // cho sửa SL
            @Override public Class<?> getColumnClass(int c) { return (c == 1) ? Integer.class : String.class; }
        };
        tblCart = new JTable(cartModel);
        tblCart.setRowHeight(36);
        tblCart.putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
        tblCart.getColumnModel().getColumn(1).setPreferredWidth(60);
//        tblCart.getModel().addTableModelListener(e -> recalcTotal());

        jEst.add(new JScrollPane(tblCart), BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout());
        south.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JPanel total = new JPanel(new GridLayout(2,2,8,8));
        total.add(new JLabel("Tổng SL:"));
        total.add(lblTongSL = new JLabel("0", SwingConstants.RIGHT));
        total.add(new JLabel("Tổng tiền:"));
        total.add(lblTongTien = new JLabel("0", SwingConstants.RIGHT));
        lblTongSL.setFont(lblTongSL.getFont().deriveFont(Font.BOLD, 18f));
        lblTongTien.setFont(lblTongTien.getFont().deriveFont(Font.BOLD, 22f));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT,10,0));
        JButton btnRemove = new JButton("Bỏ món");
        JButton btnPay = new JButton("Thanh toán");
        buttons.add(btnRemove); buttons.add(btnPay);

//        btnRemove.addActionListener(e -> removeSelectedRow());
        // btnPay.addActionListener(e -> ... lưu hoá đơn ...)

        south.add(total, BorderLayout.NORTH);
        south.add(buttons, BorderLayout.SOUTH);

        jEst.add(south, BorderLayout.SOUTH);
        jEst.setPreferredSize(new Dimension(700, getHeight()));
        add(jEst, BorderLayout.EAST);

	}
    private int chonMaLoai(String key) {
        return switch (key) {
            case CARD_COFFEE -> 1;
            case CARD_TRA    -> 2;
            case CARD_TRASUA -> 3;
            case CARD_NUOCEP -> 4;
            case CARD_BANH   -> 5;
            case CARD_KHAC   -> 6;
            default -> 0;
        };
    }
 // ===== Load 1 category từ DB và add vào CardLayout =====
    private void napDuLieuVaoCard(String cardKey) {
    	int maLoai = chonMaLoai(cardKey);
        ArrayList<Order> list = orderDAO.getSanPhamByLoai(maLoai);
        JComponent pane = gridForm(list);
        pCen.add(pane, cardKey);
    }
    
    private JComponent gridForm(ArrayList<Order> items) {
        JPanel grid = new JPanel(new FlowLayout(FlowLayout.LEFT, 60, 40));
        grid.setBackground(Color.decode("#E3CFC1"));

        for (Order o : items) {
            JPanel card = cardOrder(o);
            grid.add(card);
        }

        JScrollPane scroll = new JScrollPane(grid,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.setBorder(null);

        scroll.getViewport().addChangeListener(e -> {
            int width = scroll.getViewport().getWidth();
            grid.setPreferredSize(new Dimension(width, grid.getPreferredSize().height));
            grid.revalidate();
        });

        return scroll;
    }

    
 // ===== Card từ Order =====
    private JPanel cardOrder(Order o) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(280, 270));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220,210,200), 1));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        

        // Ảnh
        JLabel lblImg = taiAnh(o.getImg());

        // Tên + Giá
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
        
        return card;
    }
    
 // ===== Load ảnh từ DB path  =====
    private JLabel taiAnh(String imgPath) {
        JLabel lbl = new JLabel("", SwingConstants.CENTER);
        lbl.setPreferredSize(new Dimension(220, 150));

        String p = (imgPath == null || imgPath.isBlank()) ? null
                : (imgPath.startsWith("/") ? imgPath : "/img/" + imgPath);

        java.net.URL url = (p != null) ? getClass().getResource(p) : null;
        ImageIcon icon = (url != null) ? new ImageIcon(url) : null;


        if (icon != null) {
            Image scaled = icon.getImage().getScaledInstance(280, 225, Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(scaled));
        } else {
            lbl.setText("No Image");
            lbl.setOpaque(true);
            lbl.setBackground(new Color(0xF0F0F0));
        }
        return lbl;
    }
    
    private String formatVND(double vnd) {
        long v = Math.round(vnd);
        return String.format("%,d", v).replace(',', '.');
    }

}
