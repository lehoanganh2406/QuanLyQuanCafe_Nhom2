package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Menu_GUI extends JFrame {
    private JButton btnMenu;
    private JLabel  lblOrder;

    private JTextField txtTimKiem;
    private JPanel jWes;

    private JButton btnCoffee, btnTra, btnTraSua, btnNuocEp, btnBanh, btnKhac;

    // ====== NEW: center dùng CardLayout để chuyển category ======
    private JPanel pCen;                  // CENTER
    private CardLayout cenCards = new CardLayout();
	private JPanel jCen;
	private JPanel pNor;

    // Tên card (key) — gọn, không dấu/space
    private static final String CARD_COFFEE  = "COFFEE";
    private static final String CARD_TRA     = "TRA";
    private static final String CARD_TRASUA  = "TRASUA";
    private static final String CARD_NUOCEP  = "NUOCEP";
    private static final String CARD_BANH    = "BANH";
    private static final String CARD_KHAC    = "KHAC";

    public Menu_GUI(int soBan) {
        setTitle("Menu - Bàn " + soBan);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        thanhTieuDe();
        thanhBenTrai();
        menucafe();

        
    }

    

    // ===== Grid card sản phẩm (3 cột + scroll) =====
    private JComponent buildCategoryGrid(String[] names, String[] prices, String[] imagePaths) {
        JPanel grid = new JPanel(new GridBagLayout());
        grid.setBackground(new Color(0xD8C1AC)); // nền be nhẹ

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0; gbc.gridy = 0;

        for (int i = 0; i < names.length; i++) {
            JPanel card = createProductCard(names[i], prices[i], imagePaths[i]);
            grid.add(card, gbc);

            gbc.gridx++;
            if (gbc.gridx == 3) { // 3 cột
                gbc.gridx = 0;
                gbc.gridy++;
            }
        }

        JScrollPane scroll = new JScrollPane(grid,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(null);
        return scroll;
    }

    // ===== Card item: ảnh (200x150) + tên + giá đỏ =====
    private JPanel createProductCard(String name, String price, String imgPath) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(220, 240));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 210, 200), 1));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Ảnh
        JLabel lblImg;
        try {
            ImageIcon img = new ImageIcon(getClass().getResource(imgPath));
            Image scaled = img.getImage().getScaledInstance(220, 150, Image.SCALE_SMOOTH);
            lblImg = new JLabel(new ImageIcon(scaled));
        } catch (Exception ex) {
            // fallback khi thiếu ảnh
            lblImg = new JLabel("No Image", SwingConstants.CENTER);
            lblImg.setPreferredSize(new Dimension(220, 150));
            lblImg.setOpaque(true);
            lblImg.setBackground(new Color(0xF0F0F0));
        }
        lblImg.setHorizontalAlignment(SwingConstants.CENTER);

        // Tên + giá
        JPanel info = new JPanel(new BorderLayout());
        info.setBackground(Color.WHITE);
        info.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

        JLabel lblName = new JLabel(name);
        lblName.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JLabel lblPrice = new JLabel(price);
        lblPrice.setForeground(Color.RED);
        lblPrice.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPrice.setHorizontalAlignment(SwingConstants.RIGHT);

        info.add(lblName, BorderLayout.WEST);
        info.add(lblPrice, BorderLayout.EAST);

        card.add(lblImg, BorderLayout.CENTER);
        card.add(info, BorderLayout.SOUTH);

        // Hover nhẹ cho card
        card.addMouseListener(new MouseAdapter() {
            Color border = new Color(220,210,200);
            @Override public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(new Color(200, 180, 160), 2));
            }
            @Override public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(border, 1));
            }
        });

        return card;
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu_GUI(1).setVisible(true));
    }
    
 // ===== Thanh tiêu đề =====
    private void thanhTieuDe() {
    	
        JPanel jNor = new JPanel(new BorderLayout());
        jNor.setBackground(Color.decode("#865A52"));
        jNor.setPreferredSize(new Dimension(0,60));

        JPanel jNorLeft = new JPanel(new FlowLayout(FlowLayout.LEFT,10,8));
        jNorLeft.setOpaque(false);
        ImageIcon iconMenu = new ImageIcon(new ImageIcon(getClass().getResource("/img/iconMenu.png"))
                .getImage().getScaledInstance(49, 32, Image.SCALE_SMOOTH));
        jNorLeft.add(btnMenu = new JButton(iconMenu));
        btnMenu.setBackground(Color.decode("#865A52"));
        btnMenu.setBorderPainted(false);
        btnMenu.setFocusPainted(false);

        JPanel jNorCen = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
        jNorCen.setOpaque(false);
        ImageIcon iconOrder = new ImageIcon(new ImageIcon(getClass().getResource("/img/iconOrder.png"))
                .getImage().getScaledInstance(38, 48, Image.SCALE_SMOOTH));
        jNorCen.add(lblOrder = new JLabel("Order", iconOrder, SwingConstants.CENTER));
        lblOrder.setBackground(Color.decode("#FFF1E6"));
        lblOrder.setFont(new Font("Montserrat", Font.PLAIN, 32));
        lblOrder.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        lblOrder.setForeground(Color.BLACK);
        lblOrder.setOpaque(true);
        lblOrder.setIconTextGap(10);
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
                if (c instanceof JButton) {
                    c.setBackground(defaultColor);
                }
            }
            btn.setBackground(selectedColor);

            // === Điều hướng card theo text nút ===
            switch (text) {
                case "Coffee"   -> cenCards.show(pCen, CARD_COFFEE);
                case "Trà"      -> cenCards.show(pCen, CARD_TRA);
                case "Trà Sữa"  -> cenCards.show(pCen, CARD_TRASUA);
                case "Nước Ép"  -> cenCards.show(pCen, CARD_NUOCEP);
                case "Bánh"     -> cenCards.show(pCen, CARD_BANH);
                case "Khác"     -> cenCards.show(pCen, CARD_KHAC);
            }
        });

        return btn;
    }
    
    private void menucafe() {
    	// ===== CENTER: CardLayout =====
    	jCen = new JPanel(new BorderLayout());
    	pNor = new JPanel();
    	pNor.add(txtTimKiem = new JTextField(20));
    	
        pCen = new JPanel(cenCards);
        pCen.setBackground(Color.decode("#E3CFC1")); // be nhẹ

        // Thêm các card 
        pCen.add(buildCategoryGrid(
                new String[]{"Cà phê đen đá","Capuchino","Cà phê sữa đá","Latte","Americano","Cà phê đá say"},
                new String[]{"25.000","45.000","35.000","45.000","35.000","55.000"},
                new String[]{"/img/cf_den.png","/img/capu.png","/img/cf_sua.png","/img/latte.png","/img/americano.png","/img/cf_dasay.png"}
        ), CARD_COFFEE);

        pCen.add(buildCategoryGrid(
                new String[]{"Trà đào","Trà vải","Trà lài","Trà xanh sữa"},
                new String[]{"30.000","32.000","28.000","35.000"},
                new String[]{"/img/tra_dao.png","/img/tra_vai.png","/img/tra_lai.png","/img/tra_xanh_sua.png"}
        ), CARD_TRA);

        pCen.add(buildCategoryGrid(
                new String[]{"Trà sữa trân châu","Trà sữa matcha","Trà sữa socola"},
                new String[]{"35.000","39.000","39.000"},
                new String[]{"/img/ts_tranchau.png","/img/ts_matcha.png","/img/ts_socola.png"}
        ), CARD_TRASUA);

        pCen.add(buildCategoryGrid(
                new String[]{"Cam ép","Dứa ép","Táo ép","Cà rốt ép"},
                new String[]{"35.000","35.000","35.000","35.000"},
                new String[]{"/img/nuocep_cam.png","/img/nuocep_dua.png","/img/nuocep_tao.png","/img/nuocep_carot.png"}
        ), CARD_NUOCEP);

        pCen.add(buildCategoryGrid(
                new String[]{"Croissant","Bánh su kem","Bánh mousse","Tiramisu"},
                new String[]{"22.000","20.000","28.000","32.000"},
                new String[]{"/img/banh_croissant.png","/img/banh_sukem.png","/img/banh_mousse.png","/img/banh_tiramisu.png"}
        ), CARD_BANH);

        pCen.add(buildCategoryGrid(
                new String[]{"Soda chanh","Yogurt dâu","Sinh tố bơ"},
                new String[]{"28.000","30.000","38.000"},
                new String[]{"/img/soda_chanh.png","/img/yogurt_dau.png","/img/sinhto_bo.png"}
        ), CARD_KHAC);

        jCen.add(pNor, BorderLayout.NORTH);
        jCen.add(pCen, BorderLayout.CENTER);
        add(jCen, BorderLayout.CENTER);

        // Mặc định hiện Coffee
        cenCards.show(pCen, CARD_COFFEE);

        // Gán điều hướng: bấm nút → show card tương ứng
        mapButtonToCard();

	}
}
