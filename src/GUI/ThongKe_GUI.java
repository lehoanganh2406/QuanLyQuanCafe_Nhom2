package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.toedter.calendar.JDateChooser;

import connectDB.ConnectDB;
import dao.ThongKe_DAO;

public class ThongKe_GUI extends JFrame implements ActionListener {

    private String tenHienThi;
    private int loaiTaiKhoan;
    private String maNV;

    private static final Color MAU_NAU_DAM  = new Color(134, 90, 82);
    private static final Color MAU_NAU_NHAT = new Color(227, 207, 193);
    private static final Color MAU_NEN      = new Color(245, 238, 230);

    // ===== FONT CHUNG =====
    private static final Font FONT_LABEL       = new Font("Times New Roman", Font.PLAIN, 20);
    private static final Font FONT_RADIO       = new Font("Times New Roman", Font.PLAIN, 19);
    private static final Font FONT_COMBO       = new Font("Times New Roman", Font.PLAIN, 19);
    private static final Font FONT_DATE        = new Font("Times New Roman", Font.PLAIN, 19);
    private static final Font FONT_BUTTON      = new Font("Times New Roman", Font.BOLD, 18);
    private static final Font FONT_CARD_TITLE  = new Font("Times New Roman", Font.BOLD, 18);
    private static final Font FONT_CARD_VALUE  = new Font("Times New Roman", Font.BOLD, 27);

    private pnThanhMenu menu;
    private boolean isChartDoanhThu = true;

    // Filter thời gian
    private JRadioButton radTheoNgay, radTheoThang, radTheoNam;
    private ButtonGroup groupTime;
    private JComboBox<Integer> cboThang;
    private JComboBox<Integer> cboNam;
    private JDateChooser dcTuNgay;
    private JDateChooser dcDenNgay;

    // Nút chọn loại biểu đồ
    private JButton btnBieuDoDoanhThu, btnBieuDoSanPham, btnXuatExcel;

    // Khu vực biểu đồ
    private JPanel pnChartContainer;

    // Các ô thống kê tổng quan
    private JLabel lblTongDoanhThuValue;
    private JLabel lblSoHoaDonValue;
    private JLabel lblSoKhachValue;

    // DAO thống kê
    private ThongKe_DAO thongKeDAO = new ThongKe_DAO();

    public ThongKe_GUI(String tenHienThi, int loaiTaiKhoan, String maNV) {
        // Kết nối DB
        try {
            ConnectDB.getInstance().connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.tenHienThi = tenHienThi;
        this.loaiTaiKhoan = loaiTaiKhoan;
        this.maNV = maNV;

        setTitle("Thống kê");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(MAU_NEN);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                new ManHinhChinh_GUI(tenHienThi, loaiTaiKhoan, maNV).setVisible(true);
                dispose();
            }
        });

        taoThanhTieuDe();
        taoNoiDungChinh();

        // Lần đầu load: thống kê theo tháng hiện tại
        hienThiBieuDoDoanhThu();
    }

    /* ================= TIÊU ĐỀ + MENU ================= */
    private void taoThanhTieuDe() {
        String chucVu = (loaiTaiKhoan == 1) ? "Quản lý" : "Nhân viên";
        PanelTieuDe tieude = new PanelTieuDe("Thống kê", "/img/thongke.png", chucVu, tenHienThi);
        add(tieude, BorderLayout.NORTH);

        menu = new pnThanhMenu(tenHienThi, loaiTaiKhoan, maNV);
        menu.setVisible(false);
        add(menu, BorderLayout.WEST);

        tieude.getBtnMenu().addActionListener(e -> {
            menu.setVisible(!menu.isVisible());
            revalidate();
            repaint();
        });
    }

    /* ================= NỘI DUNG CHÍNH ================= */
    private void taoNoiDungChinh() {
        JPanel pnCenter = new JPanel(new BorderLayout());
        pnCenter.setBackground(MAU_NEN);
        add(pnCenter, BorderLayout.CENTER);

        // ====== Panel phía trên: filter + nút + ô thống kê ======
        JPanel pnNorth = new JPanel(new BorderLayout());
        pnNorth.setBackground(MAU_NEN);
        pnNorth.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        pnCenter.add(pnNorth, BorderLayout.NORTH);

        // 1.1 Panel filter + nút
        JPanel pnTop = new JPanel(new BorderLayout());
        pnTop.setBackground(MAU_NEN);

        // --- Panel filter thời gian (trái) ---
        JPanel pnFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnFilter.setBackground(MAU_NEN);

        JLabel lblThoiGian = new JLabel("Thống kê theo: ");

        // Radio
        radTheoNgay  = new JRadioButton("Ngày");
        radTheoThang = new JRadioButton("Tháng");
        radTheoNam   = new JRadioButton("Năm");

        groupTime = new ButtonGroup();
        groupTime.add(radTheoNgay);
        groupTime.add(radTheoThang);
        groupTime.add(radTheoNam);

        radTheoThang.setSelected(true);

        // Combobox tháng
        cboThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cboThang.addItem(i);

        // Combobox năm
        cboNam = new JComboBox<>();
        for (int y = 2022; y <= 2026; y++) cboNam.addItem(y);
        LocalDate today = LocalDate.now();
        cboThang.setSelectedItem(today.getMonthValue());
        cboNam.setSelectedItem(today.getYear());

        // DateChooser
        dcTuNgay = new JDateChooser();
        dcDenNgay = new JDateChooser();

        // ====== LABEL PHỤ ======
        JLabel lblThang   = new JLabel("Tháng:");
        JLabel lblNam     = new JLabel("Năm:");
        JLabel lblTuNgay  = new JLabel("Từ ngày:");
        JLabel lblDenNgay = new JLabel("Đến ngày:");

        // ====== ÁP DỤNG STYLE BẰNG VÒNG FOR ======
        styleLabels(lblThoiGian, lblThang, lblNam, lblTuNgay, lblDenNgay);
        styleRadios(radTheoNgay, radTheoThang, radTheoNam);
        styleCombos(cboThang, cboNam);
        styleDateChoosers(dcTuNgay, dcDenNgay);

        // ====== LẮNG NGHE THAY ĐỔI FILTER ======
        cboThang.addActionListener(e -> {
            if (radTheoThang.isSelected()) {
                capNhatBieuDoTheoLoai();
            }
        });

        cboNam.addActionListener(e -> {
            // Áp dụng cho cả theo ngày / tháng / năm
            capNhatBieuDoTheoLoai();
        });

        dcTuNgay.addPropertyChangeListener("date", evt -> {
            if (radTheoNgay.isSelected()) {
                capNhatBieuDoTheoLoai();
            }
        });

        dcDenNgay.addPropertyChangeListener("date", evt -> {
            if (radTheoNgay.isSelected()) {
                capNhatBieuDoTheoLoai();
            }
        });

        // ===== ADD VÀO FILTER PANEL =====
        pnFilter.add(lblThoiGian);
        pnFilter.add(radTheoNgay);
        pnFilter.add(radTheoThang);
        pnFilter.add(radTheoNam);

        pnFilter.add(lblThang);
        pnFilter.add(cboThang);

        pnFilter.add(lblNam);
        pnFilter.add(cboNam);

        pnFilter.add(lblTuNgay);
        pnFilter.add(dcTuNgay);

        pnFilter.add(lblDenNgay);
        pnFilter.add(dcDenNgay);

        // --- Panel nút chọn biểu đồ (phải) ---
        JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        pnButtons.setBackground(MAU_NEN);

        btnBieuDoDoanhThu = new JButton("Biểu đồ doanh thu");
        btnBieuDoSanPham  = new JButton("Biểu đồ sản phẩm");
        btnXuatExcel      = new JButton("Xuất Excel");

        styleSecondaryButton(btnBieuDoDoanhThu);
        styleSecondaryButton(btnBieuDoSanPham);
        styleSecondaryButton(btnXuatExcel);   

        btnBieuDoDoanhThu.addActionListener(this);
        btnBieuDoSanPham.addActionListener(this);
        btnXuatExcel.addActionListener(this);

        radTheoNgay.addActionListener(this);
        radTheoThang.addActionListener(this);
        radTheoNam.addActionListener(this);

        pnButtons.add(btnBieuDoDoanhThu);
        pnButtons.add(btnBieuDoSanPham);
        pnButtons.add(btnXuatExcel);       // 👉 thêm vào panel


        pnTop.add(pnFilter, BorderLayout.WEST);
        pnTop.add(pnButtons, BorderLayout.EAST);

        // 1.2 Panel ô thống kê tổng quan
        JPanel pnTongQuan = taoPanelTongQuan();

        pnNorth.add(pnTop, BorderLayout.NORTH);
        pnNorth.add(pnTongQuan, BorderLayout.SOUTH);

        // 2. Panel trung tâm: khu vực biểu đồ
        pnChartContainer = new JPanel(new BorderLayout());
        pnChartContainer.setBackground(MAU_NEN);
        pnChartContainer.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        pnCenter.add(pnChartContainer, BorderLayout.CENTER);
        
     // Nút đang được chọn
        final JButton[] activeBtn = { null };

        // Màu nền
        Color normal  = Color.WHITE;
        Color hover   = new Color(245, 230, 220);
        Color pressed = new Color(230, 200, 190);

        for (JButton btn : new JButton[] { btnBieuDoDoanhThu, btnBieuDoSanPham, btnXuatExcel }) {

            btn.setContentAreaFilled(false);
            btn.setOpaque(true);
            btn.setBackground(normal);

            btn.addMouseListener(new java.awt.event.MouseAdapter() {

                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    if (activeBtn[0] != btn) {
                        btn.setBackground(hover);
                    }
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    if (activeBtn[0] != btn) {
                        btn.setBackground(normal);
                    }
                }

                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {

                    // Nút cũ trả về màu trắng
                    if (activeBtn[0] != null && activeBtn[0] != btn) {
                        activeBtn[0].setBackground(normal);
                    }

                    // Nút hiện tại được chọn
                    activeBtn[0] = btn;

                    // Giữ màu pressed
                    btn.setBackground(pressed);
                }

                @Override
                public void mouseReleased(java.awt.event.MouseEvent e) {
                    // KHÔNG làm gì để NÚT GIỮ NGUYÊN màu pressed
                }
            });
        }


    }

    /* ================= PANEL Ô THỐNG KÊ ================= */
    private JPanel taoPanelTongQuan() {
        JPanel pn = new JPanel();
        pn.setBackground(MAU_NEN);
        pn.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));
        pn.setLayout(new java.awt.GridLayout(1, 3, 15, 0));

        lblTongDoanhThuValue = new JLabel("0 đ", JLabel.CENTER);
        lblSoHoaDonValue     = new JLabel("0",   JLabel.CENTER);
        lblSoKhachValue      = new JLabel("0",   JLabel.CENTER);

        pn.add(taoCardThongKe("Tổng doanh thu", lblTongDoanhThuValue));
        pn.add(taoCardThongKe("Số hóa đơn",     lblSoHoaDonValue));
        pn.add(taoCardThongKe("Số khách hàng",  lblSoKhachValue));

        return pn;
    }

    private JPanel taoCardThongKe(String title, JLabel lblValue) {
        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAU_NAU_DAM, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setLayout(new BorderLayout());

        JLabel lblTitle = new JLabel(title, JLabel.LEFT);
        lblTitle.setFont(FONT_CARD_TITLE);
        lblTitle.setForeground(MAU_NAU_DAM);

        lblValue.setFont(FONT_CARD_VALUE);
        lblValue.setForeground(Color.DARK_GRAY);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lblValue, BorderLayout.CENTER);

        return card;
    }

    private void styleSecondaryButton(JButton btn) {
        btn.setBackground(MAU_NAU_NHAT);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setFont(FONT_BUTTON);
        btn.setPreferredSize(new Dimension(180, 36));
    }

    /* ================== HÀM STYLE DÙNG VÒNG FOR ================== */

    private void styleLabels(JLabel... labels) {
        for (JLabel lb : labels) {
            lb.setFont(FONT_LABEL);
            lb.setForeground(Color.DARK_GRAY);
        }
    }

    private void styleRadios(JRadioButton... radios) {
        for (JRadioButton r : radios) {
            r.setFont(FONT_RADIO);
            r.setBackground(MAU_NEN);
        }
    }

    private void styleCombos(JComboBox<?>... combos) {
        for (JComboBox<?> cbo : combos) {
            cbo.setFont(FONT_COMBO);
            cbo.setPreferredSize(new Dimension(90, 30));
        }
    }

    private void styleDateChoosers(JDateChooser... choosers) {
        for (JDateChooser dc : choosers) {
            dc.setFont(FONT_DATE);
            dc.getDateEditor().getUiComponent().setFont(FONT_DATE);
            dc.setPreferredSize(new Dimension(150, 30));
        }
    }

    /* ================== HÀM TIỆN ÍCH ================== */

    private LocalDate toLocalDate(Date date) {
        if (date == null) return null;
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Trả về khoảng thời gian [from, to] theo radio + combobox
     */
    private LocalDate[] getKhoangThoiGian() {
        int year = (int) cboNam.getSelectedItem();

        LocalDate from, to;

        if (radTheoNgay.isSelected()) {
            from = toLocalDate(dcTuNgay.getDate());
            to   = toLocalDate(dcDenNgay.getDate());
            if (from == null || to == null) {
                return null;
            }
        } else if (radTheoThang.isSelected()) {
            int month = (int) cboThang.getSelectedItem();
            from = LocalDate.of(year, month, 1);
            to   = from.withDayOfMonth(from.lengthOfMonth());
        } else { // theo năm
            from = LocalDate.of(year, 1, 1);
            to   = LocalDate.of(year, 12, 31);
        }
        return new LocalDate[] { from, to };
    }

    /* ================= TẠO BIỂU ĐỒ ================= */

    private void hienThiBieuDoDoanhThu() {
        pnChartContainer.removeAll();

        LocalDate[] range = getKhoangThoiGian();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String title;
        String categoryAxisLabel;

        if (range == null) {
            // chưa chọn ngày (trường hợp theo ngày) -> không vẽ được
            title = "Chưa chọn khoảng ngày";
            categoryAxisLabel = "Thời gian";
        } else {
            LocalDate from = range[0];
            LocalDate to   = range[1];

            dataset = taoDatasetDoanhThu(from, to);

            if (radTheoNgay.isSelected()) {
                title = "Doanh thu theo ngày";
                categoryAxisLabel = "Ngày";
            } else if (radTheoThang.isSelected()) {
                title = "Doanh thu theo ngày trong tháng";
                categoryAxisLabel = "Ngày";
            } else {
                title = "Doanh thu theo tháng trong năm";
                categoryAxisLabel = "Tháng";
            }

            // Cập nhật 3 ô thống kê tổng quan
            capNhatTongQuan(from, to);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                title,
                categoryAxisLabel,
                "Doanh thu (đ)",
                dataset,
                PlotOrientation.VERTICAL,
                true,   // có legend
                true,
                false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 500));

        pnChartContainer.add(chartPanel, BorderLayout.CENTER);
        pnChartContainer.revalidate();
        pnChartContainer.repaint();
    }

    private void hienThiBieuDoSanPham() {
        pnChartContainer.removeAll();

        LocalDate[] range = getKhoangThoiGian();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String title = "Top sản phẩm bán chạy";

        if (range != null) {
            LocalDate from = range[0];
            LocalDate to   = range[1];
            dataset = taoDatasetSanPham(from, to);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                title,
                "Sản phẩm",
                "Số lượng bán",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(800, 500));

        pnChartContainer.add(chartPanel, BorderLayout.CENTER);
        pnChartContainer.revalidate();
        pnChartContainer.repaint();
    }

    /* ===== Dataset từ SQL ===== */

    private DefaultCategoryDataset taoDatasetDoanhThu(LocalDate from, LocalDate to) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String series = "Doanh thu";

        // Lấy doanh thu theo ngày (key = LocalDate, value = tổng doanh thu ngày đó)
        Map<LocalDate, Double> mapNgay = thongKeDAO.getDoanhThuTheoNgay(from, to);

        if (radTheoNam.isSelected()) {
            // ===== TRƯỜNG HỢP THEO NĂM: LUÔN ĐỦ 12 THÁNG =====
            double[] tongTheoThang = new double[13]; // index 1..12

            // Gộp doanh thu từng ngày vào tháng tương ứng
            for (Map.Entry<LocalDate, Double> e : mapNgay.entrySet()) {
                int month = e.getKey().getMonthValue();
                tongTheoThang[month] += e.getValue();
            }

            // Add đủ 12 tháng lên biểu đồ (kể cả = 0)
            for (int m = 1; m <= 12; m++) {
                double value = tongTheoThang[m];   // nếu không có thì mặc định = 0
                dataset.addValue(value, series, "Th " + m);
            }
        } else {
            // ===== TRƯỜNG HỢP THEO NGÀY / THÁNG: LUÔN ĐỦ CÁC NGÀY TRONG KHOẢNG =====
            LocalDate d = from;
            while (!d.isAfter(to)) {
                double value = 0.0;
                Double val = mapNgay.get(d);
                if (val != null) value = val;

                // Label trục X: dd/MM
                String label = d.getDayOfMonth() + "/" + d.getMonthValue();
                dataset.addValue(value, series, label);

                d = d.plusDays(1);
            }
        }

        return dataset;
    }

    private DefaultCategoryDataset taoDatasetSanPham(LocalDate from, LocalDate to) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String series = "Số lượng";

        Map<String, Integer> topSP = thongKeDAO.getTopSanPham(from, to, 10);
        for (Map.Entry<String, Integer> e : topSP.entrySet()) {
            String tenSP = e.getKey();
            int soLuong  = e.getValue();
            dataset.addValue(soLuong, series, tenSP);
        }

        return dataset;
    }

    /* ===== Cập nhật các ô tổng quan từ SQL ===== */

    private void capNhatTongQuan(LocalDate from, LocalDate to) {
        double tongDT = thongKeDAO.getTongDoanhThu(from, to);
        int soHD      = thongKeDAO.getSoHoaDon(from, to);
        int soKH      = thongKeDAO.getSoKhachHang(from, to);

        lblTongDoanhThuValue.setText(String.format("%,.0f đ", tongDT));
        lblSoHoaDonValue.setText(String.valueOf(soHD));
        lblSoKhachValue.setText(String.valueOf(soKH));
    }

    private void capNhatBieuDoTheoLoai() {
        if (isChartDoanhThu) {
            hienThiBieuDoDoanhThu();
        } else {
            hienThiBieuDoSanPham();
        }
    }

    /* ================= EVENT ================= */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o == btnBieuDoDoanhThu) {
            isChartDoanhThu = true;
            hienThiBieuDoDoanhThu();

        } else if (o == btnBieuDoSanPham) {
            isChartDoanhThu = false;
            hienThiBieuDoSanPham();

        } else if (o == radTheoNgay || o == radTheoThang || o == radTheoNam) {

            if (radTheoNgay.isSelected()) {
                cboThang.setEnabled(false);
                cboNam.setEnabled(true);
                dcTuNgay.setEnabled(true);
                dcDenNgay.setEnabled(true);
            }

            if (radTheoThang.isSelected()) {
                cboThang.setEnabled(true);
                cboNam.setEnabled(true);
                dcTuNgay.setEnabled(false);
                dcDenNgay.setEnabled(false);
            }

            if (radTheoNam.isSelected()) {
                cboThang.setEnabled(false);
                cboNam.setEnabled(true);
                dcTuNgay.setEnabled(false);
                dcDenNgay.setEnabled(false);
            }

            capNhatBieuDoTheoLoai();
        }
    }

    /* ================= MAIN TEST ================= */
    public static void main(String[] args) {
        ThongKe_GUI gui = new ThongKe_GUI("Admin Demo", 1, "NV001");
        gui.setVisible(true);
    }
}
