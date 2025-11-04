package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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

    public ThucDon_GUI() {
        setTitle("Quản lý thực đơn");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        buildNorth();
        buildWest();
        buildCenter();

        loadCard(CARD_ALL);
    }

    private void buildNorth() {
        // Tạo panel tiêu đề riêng
        PanelTieuDe tieude = new PanelTieuDe("Thực đơn", "/img/thucdon.png");
        
        // Panel chứa thanh tìm kiếm và nút chức năng
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

        // Gán style chung cho các nút
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

        // Gộp 2 phần lại: PanelTieuDe ở trên, pChucNang ở dưới
        pNorth = new JPanel(new BorderLayout());
        pNorth.add(tieude, BorderLayout.NORTH);
        pNorth.add(pChucNang, BorderLayout.CENTER);

        // Thêm vào frame
        add(pNorth, BorderLayout.NORTH);

        // Thêm sự kiện
        btnTim.addActionListener(this);
        btnThem.addActionListener(this);
        btnXoa.addActionListener(this);
        btnSua.addActionListener(this);
        btnChiTiet.addActionListener(this);
    }

    
    private void buildWest() {
        pWest = new JPanel(new GridLayout(10, 1, 10, 10));
        pWest.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        pWest.setBackground(MAU_NAU_DAM);
        pWest.setPreferredSize(new Dimension(170, 0)); // 200px là độ rộng mong muốn

        btnTatCa = taoNut("Tất cả");
        pWest.add(btnTatCa);

        for (String loai : List.of("Coffee", "Trà", "Trà sữa", "Nước ép", "Bánh", "Khác")) {
            JButton b = taoNut(loai);
            pWest.add(b);
            b.addActionListener(e -> showCard(loai));
        }
        add(pWest, BorderLayout.WEST);

        btnTatCa.addActionListener(e -> showCard(CARD_ALL));
    }

    private JButton taoNut(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    private void buildCenter() {
        pCenter = new JPanel(new BorderLayout());
        pCards = new JPanel(cardLayout);
        pCenter.add(pCards, BorderLayout.CENTER);
        add(pCenter, BorderLayout.CENTER);
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
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(310, 320));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Ảnh
        JLabel lblImg = taiAnh(sp.getImg());
        lblImg.setPreferredSize(new Dimension(270, 200));
        card.add(lblImg, BorderLayout.CENTER);

        // Tên món (ở giữa)
        JLabel lblTen = new JLabel(sp.getTenSP(), SwingConstants.CENTER);
        lblTen.setFont(new Font("Times New Roman", Font.BOLD, 20));
        lblTen.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        // Giá tiền (màu đỏ, giữa)
        JLabel lblGia = new JLabel(String.format("%,.0f đ", sp.getDonGia()), SwingConstants.CENTER);
        lblGia.setFont(new Font("Times New Roman", Font.BOLD, 18));
        lblGia.setForeground(Color.RED);

        // Panel thông tin
        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setBackground(Color.WHITE);
        info.add(lblTen);
        info.add(lblGia);

        card.add(info, BorderLayout.SOUTH);

        // Hiệu ứng hover
        card.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(MAU_NAU_DAM, 5));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                selectedSP = sp;
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

            // chỉ giữ lại tên file (nếu người dùng lưu cả đường dẫn)
            if (imgPath.contains("\\"))
                imgPath = imgPath.substring(imgPath.lastIndexOf("\\") + 1);

            java.net.URL url = getClass().getResource("/img/" + imgPath);
            if (url != null) {
                ImageIcon icon = new ImageIcon(url);
                Image scaled = icon.getImage().getScaledInstance(280, 200, Image.SCALE_SMOOTH);
                lbl.setIcon(new ImageIcon(scaled));
            } else {
                lbl.setText("Không tìm thấy ảnh");
                lbl.setOpaque(true);
                lbl.setBackground(Color.LIGHT_GRAY);
                System.out.println("❌ Không tìm thấy ảnh: " + imgPath);
            }
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
        JOptionPane.showMessageDialog(this,
                "Mã: " + sp.getMaSP() +
                "\nTên: " + sp.getTenSP() +
                "\nGiá: " + sp.getDonGia() +
                "\nLoại: " + (sp.getLoaiSP() != null ? sp.getLoaiSP().getTenLoai() : ""));
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
                JPanel grid = new JPanel(new GridLayout(0, 3, 30, 30));
                for (SanPham sp : kq)
                    grid.add(cardSanPham(sp));
                JScrollPane scroll = new JScrollPane(grid);
                pCards.add(scroll, "SEARCH");
                cardLayout.show(pCards, "SEARCH");
            }
        } else if (o.equals(btnThem)) {
            JOptionPane.showMessageDialog(this, "Thêm món mới (chưa code)");
        } else if (o.equals(btnXoa)) {
            if (selectedSP == null) {
                JOptionPane.showMessageDialog(this, "Chọn món cần xóa!");
                return;
            }
            JOptionPane.showMessageDialog(this, "Xóa: " + selectedSP.getTenSP());
        } else if (o.equals(btnSua)) {
            if (selectedSP == null) {
                JOptionPane.showMessageDialog(this, "Chọn món cần sửa!");
                return;
            }
            JOptionPane.showMessageDialog(this, "Sửa: " + selectedSP.getTenSP());
        } else if (o.equals(btnChiTiet)) {
            if (selectedSP != null)
                showChiTiet(selectedSP);
            else
                JOptionPane.showMessageDialog(this, "Chọn món cần xem chi tiết!");
        }
    }

    public static void main(String[] args) {
        ConnectDB.getInstance().connect();
        SwingUtilities.invokeLater(() -> new ThucDon_GUI().setVisible(true));
    }
}
