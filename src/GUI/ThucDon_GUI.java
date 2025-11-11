package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.util.List;

import connectDB.ConnectDB;
import dao.SanPham_DAO;
import entity.SanPham;

public class ThucDon_GUI extends JFrame implements ActionListener {
    private static final String CARD_ALL = "ALL";
    private static final Color MAU_NAU_DAM = new Color(134, 90, 82);
    private static final Color MAU_NAU_NHAT = new Color(227, 207, 193);

    private final SanPham_DAO spDAO = SanPham_DAO.getInstance();

    private JPanel pWest, pCenter, pNorth, pCards;
    private JButton btnTatCa, btnThem, btnXoa, btnSua, btnChiTiet, btnTim;
    private JTextField txtTim;
    private CardLayout cardLayout = new CardLayout();
    private JLabel lblTitle;
    private SanPham selectedSP;
    private JPanel selectedCard;
    private String tenHienThi;
    private int loaiTaiKhoan;

    // NEW: menu + header
    private pnThanhMenu menuPanel;
    private PanelTieuDe tieude;
	private String maNV;

    public ThucDon_GUI(String tenHienThi, int loaiTaiKhoan, String maNV) {
        try {
            ConnectDB.getInstance().connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.tenHienThi = tenHienThi;
        this.loaiTaiKhoan = loaiTaiKhoan;
        this.maNV = maNV;

        setTitle("Quản lý thực đơn");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLayout(new BorderLayout()); 
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                // Mở lại màn hình chính, truyền đúng tên + loại tài khoản
                new ManHinhChinh_GUI(tenHienThi, loaiTaiKhoan, maNV).setVisible(true);
                dispose(); // đóng cửa sổ Thực đơn
            }
        });


        // Font mặc định
        Font fontlbl = new Font("Times New Roman", Font.BOLD, 18);
        Font fonttxt = new Font("Times New Roman", Font.PLAIN, 18);
        UIManager.put("Label.font", fontlbl);
        UIManager.put("Button.font", fonttxt);
        UIManager.put("TextField.font", fonttxt);
        UIManager.put("ComboBox.font", fonttxt);

        buildNorth();   // header + thanh tìm kiếm + nút chức năng
        buildCenter();  // pCenter + pCards
        buildWest();    // menu loại món nằm trong pCenter (bên trái)

        // ===== Menu trái ẩn/hiện khi bấm icon =====
        menuPanel = new pnThanhMenu(tenHienThi, loaiTaiKhoan, maNV);
        menuPanel.setVisible(false);
        add(menuPanel, BorderLayout.WEST);

        tieude.getBtnMenu().addActionListener(e -> {
            menuPanel.setVisible(!menuPanel.isVisible());
            revalidate();
            repaint();
        });

        loadCard(CARD_ALL);
        applyPermission();
    }

    private void buildNorth() {
        String chucVu = (loaiTaiKhoan == 1) ? "Quản lý" : "Nhân viên";
        tieude = new PanelTieuDe("Thực đơn", "/img/thucdon.png", chucVu, tenHienThi);

        // Panel chứa thanh tìm kiếm & nút chức năng
        JPanel pChucNang = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        pChucNang.setBackground(MAU_NAU_DAM);

        lblTitle = new JLabel("THỰC ĐƠN QUÁN", JLabel.LEFT);
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 28));

        txtTim = new JTextField("Nhập tên, mã hoặc loại món...");
        txtTim.setPreferredSize(new Dimension(400, 30));
        txtTim.setForeground(Color.GRAY);
        txtTim.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (txtTim.getText().equals("Nhập tên, mã hoặc loại món...")) {
                    txtTim.setText("");
                    txtTim.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (txtTim.getText().isEmpty()) {
                    txtTim.setText("Nhập tên, mã hoặc loại món...");
                    txtTim.setForeground(Color.GRAY);
                }
            }
        });

        btnTim = new JButton("Tìm kiếm");
        btnThem = new JButton("Thêm");
        btnXoa = new JButton("Xóa");
        btnSua = new JButton("Sửa");
        btnChiTiet = new JButton("Chi tiết");

        for (JButton b : new JButton[]{btnTim, btnThem, btnXoa, btnSua, btnChiTiet}) {
            b.setFont(new Font("Times New Roman", Font.BOLD, 18));
            b.setBackground(Color.WHITE);
            b.setFocusPainted(false);
            b.setBorderPainted(false);
            b.setOpaque(true);
        }

        pChucNang.add(lblTitle);
        pChucNang.add(Box.createHorizontalStrut(40));
        pChucNang.add(txtTim);
        pChucNang.add(btnTim);
        pChucNang.add(Box.createHorizontalStrut(40));
        pChucNang.add(btnThem);
        pChucNang.add(btnXoa);
        pChucNang.add(btnSua);
        pChucNang.add(btnChiTiet);

        pNorth = new JPanel(new BorderLayout());
        pNorth.add(tieude, BorderLayout.NORTH);
        pNorth.add(pChucNang, BorderLayout.CENTER);

        add(pNorth, BorderLayout.NORTH);

        btnTim.addActionListener(this);
        btnThem.addActionListener(this);
        btnXoa.addActionListener(this);
        btnSua.addActionListener(this);
        btnChiTiet.addActionListener(this);
    }

    private void buildCenter() {
        pCenter = new JPanel(new BorderLayout());
        pCards = new JPanel(cardLayout);
        pCenter.add(pCards, BorderLayout.CENTER);
        add(pCenter, BorderLayout.CENTER);
    }

    private void buildWest() {
        pWest = new JPanel(new GridLayout(10, 1, 10, 10));
        pWest.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pWest.setBackground(MAU_NAU_DAM);
        pWest.setPreferredSize(new Dimension(170, 0));

        btnTatCa = taoNut("Tất cả");
        pWest.add(btnTatCa);

        for (String loai : List.of("Coffee", "Trà", "Trà sữa", "Nước ép", "Bánh", "Khác")) {
            JButton b = taoNut(loai);
            pWest.add(b);
            b.addActionListener(e -> showCard(loai));
        }

        //  THAY ĐỔI: add vào pCenter (trái), KHÔNG add vào JFrame.WEST
        pCenter.add(pWest, BorderLayout.WEST);

        btnTatCa.addActionListener(e -> showCard(CARD_ALL));
    }

    private JButton taoNut(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    private void loadCard(String cardKey) {
        List<SanPham> ds = (cardKey.equals(CARD_ALL))
                ? spDAO.getAllSanPham()
                : spDAO.getSanPhamByLoai(cardKey);

        // Panel chứa các card — dùng FlowLayout để cố định kích thước card
        JPanel grid = new JPanel(new FlowLayout(FlowLayout.LEFT, 25, 25));
        grid.setBackground(MAU_NAU_NHAT);

        for (SanPham sp : ds) {
            grid.add(cardSanPham(sp));
        }

        grid.setPreferredSize(new Dimension(950, (int) Math.ceil(ds.size() / 3.0) * 340));

        JScrollPane scroll = new JScrollPane(grid,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(null);

        pCards.add(scroll, cardKey);
    }


    private JPanel cardSanPham(SanPham sp) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(310, 320));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        JLabel lblImg = taiAnh(sp.getImg());
        lblImg.setPreferredSize(new Dimension(270, 200));
        card.add(lblImg, BorderLayout.CENTER);

        JLabel lblTen = new JLabel(sp.getTenSP(), SwingConstants.CENTER);
        lblTen.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblTen.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        JLabel lblGia = new JLabel(String.format("%,.0f đ", sp.getDonGia()), SwingConstants.CENTER);
        lblGia.setFont(new Font("Times New Roman", Font.BOLD, 18));
        lblGia.setForeground(Color.RED);

        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setBackground(Color.WHITE);
        info.add(lblTen);
        info.add(lblGia);
        card.add(info, BorderLayout.SOUTH);

        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (card != selectedCard) {
                    card.setBorder(BorderFactory.createLineBorder(MAU_NAU_DAM, 3));
                }
            }
            @Override
            public void mouseExited(MouseEvent e) {
                if (card != selectedCard) {
                    card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
                }
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                if (selectedCard != null && selectedCard != card) {
                    selectedCard.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
                }
                selectedCard = card;
                selectedSP = sp;
                card.setBorder(BorderFactory.createLineBorder(MAU_NAU_DAM, 5));

                if (e.getClickCount() == 2) {
                    showChiTiet(sp);
                }
            }
        });

        return card;
    }

    private JLabel taiAnh(String imgPath) {
        JLabel lbl = new JLabel("", SwingConstants.CENTER);
        lbl.setPreferredSize(new Dimension(280, 200));
        try {
            if (imgPath == null || imgPath.trim().isEmpty()) {
                lbl.setText("Không có ảnh");
                lbl.setOpaque(true);
                lbl.setBackground(Color.LIGHT_GRAY);
                return lbl;
            }
            ImageIcon icon;
            if (new File(imgPath).exists()) {
                icon = new ImageIcon(imgPath);
            } else {
                java.net.URL url = getClass().getResource("/img/" + imgPath);
                if (url != null) {
                    icon = new ImageIcon(url);
                } else {
                    lbl.setText("Không tìm thấy ảnh");
                    lbl.setOpaque(true);
                    lbl.setBackground(Color.LIGHT_GRAY);
                    return lbl;
                }
            }
            Image scaled = icon.getImage().getScaledInstance(280, 200, Image.SCALE_SMOOTH);
            lbl.setIcon(new ImageIcon(scaled));
        } catch (Exception ex) {
            lbl.setText("Lỗi ảnh");
            lbl.setOpaque(true);
            lbl.setBackground(Color.PINK);
            ex.printStackTrace();
        }
        return lbl;
    }

    private void showCard(String key) {
        if (Arrays.stream(pCards.getComponents()).noneMatch(c -> key.equals(c.getName()))) {
            loadCard(key);
        }
        cardLayout.show(pCards, key);
    }

    private void showChiTiet(SanPham sp) {
        JFrame frmChiTiet = new JFrame("Chi tiết sản phẩm");
        frmChiTiet.setSize(700, 750);
        frmChiTiet.setLocationRelativeTo(this);
        frmChiTiet.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frmChiTiet.setLayout(new BorderLayout());

        JPanel pNorth = new JPanel();
        pNorth.setBackground(MAU_NAU_DAM);
        JLabel lblTitle = new JLabel("CHI TIẾT SẢN PHẨM");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 26));
        pNorth.add(lblTitle);
        frmChiTiet.add(pNorth, BorderLayout.NORTH);

        JPanel pMain = new JPanel();
        pMain.setBackground(MAU_NAU_NHAT);
        pMain.setLayout(new BoxLayout(pMain, BoxLayout.Y_AXIS));
        pMain.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        pMain.add(createRowView("Mã sản phẩm:", sp.getMaSP()));
        pMain.add(Box.createVerticalStrut(10));
        pMain.add(createRowView("Tên sản phẩm:", sp.getTenSP()));
        pMain.add(Box.createVerticalStrut(10));
        pMain.add(createRowView("Số lượng:", String.valueOf(sp.getSoLuong())));
        pMain.add(Box.createVerticalStrut(10));
        pMain.add(createRowView("Đơn giá:", String.format("%,.0f đ", sp.getDonGia())));
        pMain.add(Box.createVerticalStrut(10));
        pMain.add(createRowView("Loại sản phẩm:", 
            sp.getLoaiSP() != null ? sp.getLoaiSP().getTenLoai() : ""));
        pMain.add(Box.createVerticalStrut(10));
        pMain.add(createRowView("Mô tả:", sp.getMoTa() == null ? "(Không có mô tả)" : sp.getMoTa()));

        // ===== Ảnh sản phẩm =====
        JPanel pAnh = new JPanel();
        pAnh.setBackground(MAU_NAU_NHAT);
        JLabel lblAnh = new JLabel("", JLabel.CENTER);
        lblAnh.setPreferredSize(new Dimension(420, 270));
        lblAnh.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblAnh.setBackground(Color.WHITE);

        try {
            if (sp.getImg() != null && !sp.getImg().isEmpty()) {
                File file = new File(sp.getImg());
                ImageIcon icon;
                if (file.exists()) {
                    icon = new ImageIcon(sp.getImg());
                } else {
                    java.net.URL url = getClass().getResource("/img/" + sp.getImg());
                    icon = url != null ? new ImageIcon(url) : null;
                }
                if (icon != null) {
                    Image img = icon.getImage().getScaledInstance(420, 270, Image.SCALE_SMOOTH);
                    lblAnh.setIcon(new ImageIcon(img));
                } else {
                    lblAnh.setText("Không tìm thấy ảnh");
                }
            } else {
                lblAnh.setText("Chưa có ảnh");
            }
        } catch (Exception e) {
            lblAnh.setText("Lỗi ảnh");
        }

        pAnh.add(lblAnh);
        pMain.add(Box.createVerticalStrut(20));
        pMain.add(pAnh);

        // ===== Nút đóng =====
        JPanel pBtn = new JPanel();
        pBtn.setBackground(MAU_NAU_NHAT);
        JButton btnDong = new JButton("Đóng");
        btnDong.setBackground(MAU_NAU_DAM);
        btnDong.setForeground(Color.WHITE);
        btnDong.setPreferredSize(new Dimension(100, 30));
        pBtn.add(btnDong);
        pMain.add(Box.createVerticalStrut(20));
        pMain.add(pBtn);

        frmChiTiet.add(pMain, BorderLayout.CENTER);
        
        btnDong.addActionListener(ev -> frmChiTiet.dispose());
        frmChiTiet.setVisible(true);
    }
 // Hàm tạo dòng hiển thị thông tin (chỉ đọc)
    private JPanel createRowView(String label, String value) {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(new Color(227, 207, 193));
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(130, 25));
        JTextField txt = new JTextField(value);
        txt.setEditable(false);
        txt.setBackground(Color.WHITE);
        p.add(lbl, BorderLayout.WEST);
        p.add(txt, BorderLayout.CENTER);
        return p;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o.equals(btnTim)) {
            String kw = txtTim.getText().trim();
            List<SanPham> kq = spDAO.search(kw);

            if (kq.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy!");
            } else {
                JPanel grid = new JPanel(new GridLayout(0, 4, 30, 30));
                grid.setBackground(MAU_NAU_NHAT);
                for (SanPham sp : kq)
                    grid.add(cardSanPham(sp));
                JScrollPane scroll = new JScrollPane(grid);
                pCards.add(scroll, "SEARCH");
                cardLayout.show(pCards, "SEARCH");
            }
        } else if (o.equals(btnThem)) {
            if (loaiTaiKhoan == 0) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền thêm món!");
                return;
            }
            new ThemSanPham_GUI().setVisible(true);

        } else if (o.equals(btnXoa)) {
            if (loaiTaiKhoan == 0) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền xóa món!");
                return;
            }
            if (selectedSP == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần xóa!");
                return;
            }
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn xóa sản phẩm: " + selectedSP.getTenSP() + " ?",
                    "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean ok = spDAO.xoaSanPham(selectedSP.getMaSP());
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công!");
                    pCards.removeAll();
                    loadCard(CARD_ALL);
                    pCards.revalidate();
                    pCards.repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Xóa thất bại!");
                }
            }

        } else if (o.equals(btnSua)) {
            if (loaiTaiKhoan == 0) {
                JOptionPane.showMessageDialog(this, "Bạn không có quyền sửa món!");
                return;
            }
            if (selectedSP == null) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn món cần sửa!");
                return;
            }
            ThemSanPham_GUI suaSP = new ThemSanPham_GUI(selectedSP);
            suaSP.setVisible(true);

            pCards.removeAll();
            loadCard(CARD_ALL);
            pCards.revalidate();
            pCards.repaint();

        } else if (o.equals(btnChiTiet)) {
            if (selectedSP != null)
                showChiTiet(selectedSP);
            else
                JOptionPane.showMessageDialog(this, "Chọn món cần xem chi tiết!");
        }
    }

    private void applyPermission() {
        if (loaiTaiKhoan == 0) {
            if (btnThem != null) btnThem.setEnabled(false);
            if (btnSua  != null) btnSua.setEnabled(false);
            if (btnXoa  != null) btnXoa.setEnabled(false);
        }
    }

}
