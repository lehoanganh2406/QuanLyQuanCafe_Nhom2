package GUI;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.*;
import connectDB.ConnectDB;
import dao.SanPham_DAO;
import entity.SanPham;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.List;

public class Order_GUI extends JFrame implements ActionListener {

    // DAO
    private final SanPham_DAO spDAO = SanPham_DAO.getInstance();

    // Icons
    private final ImageIcon iconTimKiem   = new ImageIcon(getClass().getResource("/img/iconTimKiem.png"));
    private final ImageIcon iconChuyenBan = new ImageIcon(getClass().getResource("/img/chuyenban.png"));
    private final ImageIcon iconQuayLai   = new ImageIcon(getClass().getResource("/img/back_16.png"));
    private final ImageIcon iconThanhToan = new ImageIcon(getClass().getResource("/img/bill_16.png"));
    private final ImageIcon iconBoMon     = new ImageIcon(getClass().getResource("/img/trash_16.png"));

    private int soBan;

    // Layout tổng
    private JPanel jWes, jCen, pNor, pCen, jEst;
    private final CardLayout cenCards = new CardLayout();

    // Trái: danh mục
    private JButton btnTatCa;
    private final Map<String,String> maLoaiByTen = new HashMap<>();
    private final Set<String> loadedCards = new HashSet<>(); // tránh nạp trùng card

    // North center
    private JLabel lblBan;
    private JTextField txtTimKiem;
    private JButton btnTim, btnChuyenBan;

    // Phải: giỏ hàng
    private JTable tblCart;
    private DefaultTableModel cartModel;
    private JLabel lblTongSL, lblTongTien;
    private JButton btnQuayLai, btnRemove, btnThanhToan;

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

        // Sự kiện nút chức năng
        btnTim.addActionListener(this);
        txtTimKiem.addActionListener(this);
        btnChuyenBan.addActionListener(this);
        btnQuayLai.addActionListener(this);
        btnRemove.addActionListener(this);
        btnThanhToan.addActionListener(this);

        // Mặc định mở "Tất cả"
        showCategory("Tất cả");
    }

    public static void main(String[] args) {
        ConnectDB.getInstance().connect();
        SwingUtilities.invokeLater(() -> new Order_GUI(1).setVisible(true));
    }

    /* ===================== TIÊU ĐỀ ===================== */
    private void thanhTieuDe() {
        PanelTieuDe tieude = new PanelTieuDe("Order", "/img/iconOrder.png");
        add(tieude, BorderLayout.NORTH);
    }

    /* ===================== CỘT TRÁI: DANH MỤC ===================== */
    private void thanhBenTrai() {
        jWes = new JPanel(new GridLayout(12, 1, 10, 10));
        jWes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Nút "Tất cả"
        btnTatCa = taoNutMenu("Tất cả");
        jWes.add(btnTatCa);
        btnTatCa.addActionListener(e -> showCategory("Tất cả"));

        // Các loại theo nhãn hiển thị (dùng Arrays.asList để compatible Java 8)
        for (String loai : Arrays.asList("Coffee", "Trà", "Trà sữa", "Nước ép", "Bánh", "Khác")) {
            JButton b = taoNutMenu(loai);
            jWes.add(b);
            b.addActionListener(e -> showCategory(loai));
        }

        jWes.setPreferredSize(new Dimension(170, getHeight()));
        add(jWes, BorderLayout.WEST);
    }

    private JButton taoNutMenu(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Times New Roman", Font.BOLD, 20));
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(true);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        return btn;
    }

    /* ===================== TRUNG TÂM ===================== */
    private void menuCenter() {
        jCen = new JPanel(new BorderLayout());

        // Thanh trên (tìm kiếm + bàn + chuyển bàn)
        pNor = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 8));
        pNor.setBorder(BorderFactory.createEmptyBorder(10, 16, 6, 16));
        pNor.setBackground(new Color(255, 228, 196));

        txtTimKiem = new JTextField();
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
            @Override public void focusGained(FocusEvent e) {
                if ("Nhập sản phẩm cần tìm...".equals(txtTimKiem.getText())) {
                    txtTimKiem.setText("");
                    txtTimKiem.setForeground(Color.BLACK);
                }
            }
            @Override public void focusLost(FocusEvent e) {
                if (txtTimKiem.getText().isEmpty()) {
                    txtTimKiem.setText("Nhập sản phẩm cần tìm...");
                    txtTimKiem.setForeground(Color.GRAY);
                }
            }
        });

        btnTim = taoNut("Tìm", iconTimKiem);
        btnTim.setPreferredSize(new Dimension(140, 40));

        pNor.add(txtTimKiem);
        pNor.add(btnTim);
        pNor.add(Box.createHorizontalStrut(60));

        lblBan = new JLabel("Bàn " + soBan);
        lblBan.setFont(new Font("Times New Roman", Font.BOLD, 30));
        pNor.add(lblBan);

        pNor.add(Box.createHorizontalStrut(40));
        btnChuyenBan = taoNut("Chuyển Bàn", iconChuyenBan);
        btnChuyenBan.setPreferredSize(new Dimension(200, 40));
        pNor.add(btnChuyenBan);

        // Khu vực card
        pCen = new JPanel(cenCards);
        pCen.setBackground(Color.decode("#E3CFC1"));

        jCen.add(pNor, BorderLayout.NORTH);
        jCen.add(pCen, BorderLayout.CENTER);
        add(jCen, BorderLayout.CENTER);
    }

    /* ===================== CỘT PHẢI: GIỎ HÀNG ===================== */
    private void thanhBenPhai() {
        jEst = new JPanel(new BorderLayout());
        jEst.setBackground(Color.decode("#E3CFC1"));

        String[] cols = {"Mã món", "Tên món", "SL", "Đơn giá", "Thành tiền"};
        cartModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) {
                return c == 2; // chỉ cho phép sửa cột SL
            }
            @Override public Class<?> getColumnClass(int c) {
                switch (c) {
                    case 0: return String.class;  // mã
                    case 2: return Integer.class; // SL
                    case 3:
                    case 4: return Long.class;    // giá
                    default: return String.class; // tên
                }
            }
        };

        // Tooltip cột SL (xuất hiện ngay)
        tblCart = new JTable(cartModel) {
            @Override public String getToolTipText(MouseEvent e) {
                Point p = e.getPoint();
                int col = columnAtPoint(p);
                if (col == 2) return "Nhập số lượng > 0";
                return super.getToolTipText(e);
            }
        };
        ToolTipManager ttm = ToolTipManager.sharedInstance();
        ttm.setInitialDelay(0);
        ttm.registerComponent(tblCart);

        tblCart.setRowHeight(36);
        tblCart.setFont(new Font("Times New Roman", Font.PLAIN, 20));
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

        // South: tổng & nút
        JPanel south = new JPanel(new BorderLayout());
        south.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel total = new JPanel(new GridLayout(2, 2, 8, 8));
        JLabel lblSL = new JLabel("Tổng SL:"); lblSL.setFont(new Font("Times New Roman", Font.BOLD, 26));
        total.add(lblSL);
        lblTongSL = new JLabel("0", SwingConstants.RIGHT); lblTongSL.setFont(new Font("Times New Roman", Font.BOLD, 26));
        total.add(lblTongSL);
        JLabel lblTT = new JLabel("Tổng tiền:"); lblTT.setFont(new Font("Times New Roman", Font.BOLD, 26));
        total.add(lblTT);
        lblTongTien = new JLabel("0", SwingConstants.RIGHT); lblTongTien.setFont(new Font("Times New Roman", Font.BOLD, 26));
        total.add(lblTongTien);

        JPanel buttonsTrai = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnQuayLai = taoNut("Quay lại", iconQuayLai);
        btnQuayLai.setPreferredSize(new Dimension(140, 40));
        buttonsTrai.add(btnQuayLai);

        JPanel buttonsPhai = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnRemove = taoNut("Bỏ món", iconBoMon);
        btnRemove.setPreferredSize(new Dimension(140, 40));
        btnThanhToan = taoNut("Thanh toán", iconThanhToan);
        btnThanhToan.setPreferredSize(new Dimension(170, 40));
        buttonsPhai.add(btnRemove);
        buttonsPhai.add(btnThanhToan);

        JPanel bottomBar = new JPanel(new BorderLayout());
        bottomBar.add(buttonsTrai, BorderLayout.WEST);
        bottomBar.add(buttonsPhai, BorderLayout.EAST);

        south.add(total, BorderLayout.NORTH);
        south.add(bottomBar, BorderLayout.SOUTH);

        jEst.add(south, BorderLayout.SOUTH);
        jEst.setPreferredSize(new Dimension(700, getHeight()));
        add(jEst, BorderLayout.EAST);

        // Lắng nghe chỉnh SL để tính lại thành tiền + tổng
        cartModel.addTableModelListener(e -> {
            if (e.getType() != TableModelEvent.UPDATE) return;
            int row = e.getFirstRow();
            int col = e.getColumn();
            if (row >= 0 && col == 2) {
                Object val = cartModel.getValueAt(row, 2);
                int sl;
                try {
                    sl = Integer.parseInt(val.toString());
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập số hợp lệ!");
                    cartModel.setValueAt(1, row, 2);
                    return;
                }
                if (sl <= 0) {
                    JOptionPane.showMessageDialog(null, "Số lượng phải lớn hơn 0!");
                    cartModel.setValueAt(1, row, 2);
                    return;
                }
                long donGia = (Long) cartModel.getValueAt(row, 3);
                cartModel.setValueAt((long) sl * donGia, row, 4);
                tinhTong();
            }
        });
    }

    private JButton taoNut(String ten, ImageIcon icon) {
        JButton btn = new JButton(ten, icon);
        btn.setBackground(Color.WHITE);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Times New Roman", Font.BOLD, 18));
        return btn;
    }

    /* ===================== DATA & HIỂN THỊ CARD ===================== */
    private void napMaLoaiFromDB() {
        // Map tên hiển thị → mã loại trong DB
        String[] tenLoais = {"Coffee", "Trà", "Trà sữa", "Nước ép", "Bánh", "Khác"};
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(
                "SELECT maLoai, loaiSP FROM dbo.LoaiSanPham " +
                "WHERE loaiSP IN (N'Coffee',N'Trà',N'Trà sữa',N'Nước ép',N'Bánh',N'Khác')")) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    maLoaiByTen.put(rs.getString("loaiSP"), rs.getString("maLoai")); // loaiSP → maLoai
                }
            }
            for (String t : tenLoais) maLoaiByTen.putIfAbsent(t, null);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showCategory(String label) {
        // Đổi màu nút
        for (Component c : jWes.getComponents()) {
            if (c instanceof JButton) c.setBackground(Color.WHITE);
        }
        for (Component c : jWes.getComponents()) {
            if (c instanceof JButton b && b.getText().equals(label)) {
                b.setBackground(new Color(255, 228, 196));
                break;
            }
        }

        // Nạp card nếu chưa có
        if (!loadedCards.contains(label)) {
            String maLoai = "Tất cả".equals(label) ? null : maLoaiByTen.get(label);
            List<SanPham> list = (maLoai == null) ? spDAO.getAllSanPham()
                                                  : spDAO.getSanPhamByMaLoai(maLoai);
            JComponent pane = gridForm(new ArrayList<>(list), maLoai);
            pCen.add(pane, label);
            loadedCards.add(label);
        }

        // Hiển thị card theo label
        cenCards.show(pCen, label);
    }

    private JPanel luoiGridPanel(List<SanPham> items) {
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
        scroll.putClientProperty("maLoai", maLoai); // để tìm kiếm biết đang tab nào
        return scroll;
    }

    private JPanel cardOrder(SanPham o) {
        JPanel card = new JPanel(new BorderLayout());
        card.setPreferredSize(new Dimension(280, 270));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(new Color(220, 210, 200), 1));

        JLabel lblImg = taiAnh(o.getImg());

        JPanel info = new JPanel(new BorderLayout());
        info.setBackground(Color.WHITE);
        info.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

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
            if (imgPath.contains("\\")) imgPath = imgPath.substring(imgPath.lastIndexOf("\\") + 1);
            URL url = getClass().getResource("/img/" + imgPath);
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

    /* ===================== GIỎ HÀNG ===================== */
    private void addToCart(SanPham o) {
        String ma = o.getMaSP();
        String ten = o.getTenSP();
        long donGia = Math.round(o.getDonGia());

        // Check trùng mã → tăng SL
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
        cartModel.addRow(new Object[]{ma, ten, 1, donGia, donGia});
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

    /* ===================== TÌM KIẾM ===================== */
    private JScrollPane layScrollDangHienThi() {
        for (Component comp : pCen.getComponents()) {
            if (comp.isVisible() && comp instanceof JScrollPane) return (JScrollPane) comp;
        }
        return null;
    }

    private void noiDungTab() {
        JScrollPane sp = layScrollDangHienThi();
        if (sp == null) return;

        Object val = sp.getClientProperty("maLoai");
        String maLoai = (val instanceof String) ? (String) val : null;

        List<SanPham> ds = (maLoai == null)
                ? spDAO.getAllSanPham()
                : spDAO.getSanPhamByMaLoai(maLoai);

        JPanel newGrid = luoiGridPanel(ds);
        sp.setViewportView(newGrid);
        newGrid.revalidate();
        newGrid.repaint();
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

        List<SanPham> ds = (maLoai == null)
                ? spDAO.search(keyword)
                : spDAO.searchByNameAndLoai(keyword, maLoai);

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

    /* ===================== TIỆN ÍCH ===================== */
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

    private void chuyenBan() {
        String input = JOptionPane.showInputDialog(this, "Nhập số bàn mới:", String.valueOf(soBan));
        if (input != null && input.trim().matches("\\d+")) {
            int banMoi = Integer.parseInt(input.trim());
            if (banMoi <= 0 || banMoi > 40) {
                JOptionPane.showMessageDialog(this, "Số bàn không hợp lệ!");
                return;
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

    /* ===================== ACTIONS ===================== */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();
        if (o.equals(btnTim) || o.equals(txtTimKiem)) {
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