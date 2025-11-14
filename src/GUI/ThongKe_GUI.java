package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.toedter.calendar.JDateChooser;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import connectDB.ConnectDB;
import dao.ThongKe_DAO;

public class ThongKe_GUI extends JFrame implements ActionListener {

    // ================== UI COLOR ==================
    private static final Color MAU_NAU_DAM  = new Color(134, 90, 82);
    private static final Color MAU_NAU_NHAT = new Color(227, 207, 193);
    private static final Color MAU_NEN      = new Color(245, 238, 230);

    // Fonts
    private static final Font FONT_LABEL       = new Font("Times New Roman", Font.PLAIN, 20);
    private static final Font FONT_RADIO       = new Font("Times New Roman", Font.PLAIN, 19);
    private static final Font FONT_COMBO       = new Font("Times New Roman", Font.PLAIN, 19);
    private static final Font FONT_DATE        = new Font("Times New Roman", Font.PLAIN, 19);
    private static final Font FONT_BUTTON      = new Font("Times New Roman", Font.BOLD, 18);
    private static final Font FONT_CARD_TITLE  = new Font("Times New Roman", Font.BOLD, 18);
    private static final Font FONT_CARD_VALUE  = new Font("Times New Roman", Font.BOLD, 27);

    // MENU + user
    private String tenHienThi;
    private int loaiTaiKhoan;
    private String maNV;
    private pnThanhMenu menu;

    // Filter
    private JRadioButton radTheoNgay, radTheoThang, radTheoNam;
    private ButtonGroup groupTime;
    private JComboBox<Integer> cboThang;
    private JComboBox<Integer> cboNam;
    private JDateChooser dcTuNgay, dcDenNgay;

    // Buttons
    private JButton btnBieuDoDoanhThu, btnBieuDoSanPham, btnXuatExcel;
    private boolean isChartDoanhThu = true;

    // Chart container
    private JPanel pnChartContainer;

    // Summary labels
    private JLabel lblTongDoanhThuValue, lblSoHoaDonValue, lblSoKhachValue;

    // DAO
    private ThongKe_DAO thongKeDAO = new ThongKe_DAO();

    // ===================== CONSTRUCTOR ====================

    public ThongKe_GUI(String tenHienThi, int loaiTaiKhoan, String maNV) {
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

        hienThiBieuDoDoanhThu();
    }

    // ====================== TITLE BAR =====================
    private void taoThanhTieuDe() {
        String chucVu = (loaiTaiKhoan == 1) ? "Quản lý" : "Nhân viên";
        PanelTieuDe td = new PanelTieuDe("Thống kê", "/img/thongke.png", chucVu, tenHienThi);
        add(td, BorderLayout.NORTH);

        menu = new pnThanhMenu(tenHienThi, loaiTaiKhoan, maNV);
        menu.setVisible(false);
        add(menu, BorderLayout.WEST);

        td.getBtnMenu().addActionListener(e -> {
            menu.setVisible(!menu.isVisible());
            revalidate();
            repaint();
        });
    }

    // ====================== MAIN PANEL ====================
    private void taoNoiDungChinh() {
        JPanel pnCenter = new JPanel(new BorderLayout());
        pnCenter.setBackground(MAU_NEN);
        add(pnCenter, BorderLayout.CENTER);

        JPanel pnNorth = new JPanel(new BorderLayout());
        pnNorth.setBackground(MAU_NEN);
        pnNorth.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        pnCenter.add(pnNorth, BorderLayout.NORTH);

        // ---------------- FILTER PANEL ----------------
        JPanel pnTop = new JPanel(new BorderLayout());
        pnTop.setBackground(MAU_NEN);

        JPanel pnFilter = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        pnFilter.setBackground(MAU_NEN);

        JLabel lblThoiGian = new JLabel("Thống kê theo:");
        styleLabels(lblThoiGian);

        radTheoNgay  = new JRadioButton("Ngày");
        radTheoThang = new JRadioButton("Tháng");
        radTheoNam   = new JRadioButton("Năm");
        styleRadios(radTheoNgay, radTheoThang, radTheoNam);

        groupTime = new ButtonGroup();
        groupTime.add(radTheoNgay);
        groupTime.add(radTheoThang);
        groupTime.add(radTheoNam);
        radTheoThang.setSelected(true);

        cboThang = new JComboBox<>();
        for (int i = 1; i <= 12; i++) cboThang.addItem(i);

        cboNam = new JComboBox<>();
        for (int y = 2020; y <= 2030; y++) cboNam.addItem(y);

        styleCombos(cboThang, cboNam);

        LocalDate today = LocalDate.now();
        cboThang.setSelectedItem(today.getMonthValue());
        cboNam.setSelectedItem(today.getYear());

        dcTuNgay = new JDateChooser();
        dcDenNgay = new JDateChooser();
        styleDateChoosers(dcTuNgay, dcDenNgay);

        JLabel lblThang   = new JLabel("Tháng:");
        JLabel lblNam     = new JLabel("Năm:");
        JLabel lblTuNgay  = new JLabel("Từ ngày:");
        JLabel lblDenNgay = new JLabel("Đến ngày:");
        styleLabels(lblThang, lblNam, lblTuNgay, lblDenNgay);

        pnFilter.add(lblThoiGian);
        pnFilter.add(radTheoNgay);
        pnFilter.add(radTheoThang);
        pnFilter.add(radTheoNam);

        pnFilter.add(lblThang); pnFilter.add(cboThang);
        pnFilter.add(lblNam);   pnFilter.add(cboNam);
        pnFilter.add(lblTuNgay); pnFilter.add(dcTuNgay);
        pnFilter.add(lblDenNgay); pnFilter.add(dcDenNgay);

        // ---------------- BUTTONS PANEL ----------------
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
        pnButtons.add(btnXuatExcel);

        pnTop.add(pnFilter, BorderLayout.WEST);
        pnTop.add(pnButtons, BorderLayout.EAST);

        pnNorth.add(pnTop, BorderLayout.NORTH);

        // ================= SUMMARY PANEL =================
        JPanel pnTongQuan = taoPanelTongQuan();
        pnNorth.add(pnTongQuan, BorderLayout.SOUTH);

        // ================= CHART PANEL =================
        pnChartContainer = new JPanel(new BorderLayout());
        pnChartContainer.setBackground(MAU_NEN);
        pnChartContainer.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        pnCenter.add(pnChartContainer, BorderLayout.CENTER);

        // ================= BUTTON HOVER + CLICK =================
        setupButtonHover();
     // ====== LẮNG NGHE THAY ĐỔI FILTER ======
        cboThang.addActionListener(e -> {
            if (radTheoThang.isSelected()) {
                capNhatBieuDoTheoLoai();
            }
        });

        cboNam.addActionListener(e -> {
            // dùng cho cả theo ngày / tháng / năm
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

    }

    // ===================== SUMMARY CARDS ==================

    private JPanel taoPanelTongQuan() {
        JPanel pn = new JPanel();
        pn.setBackground(MAU_NEN);
        pn.setLayout(new java.awt.GridLayout(1, 3, 15, 0));

        lblTongDoanhThuValue = new JLabel("0 đ", JLabel.CENTER);
        lblSoHoaDonValue     = new JLabel("0", JLabel.CENTER);
        lblSoKhachValue      = new JLabel("0", JLabel.CENTER);

        pn.add(taoCard("Tổng doanh thu", lblTongDoanhThuValue));
        pn.add(taoCard("Số hóa đơn", lblSoHoaDonValue));
        pn.add(taoCard("Số khách hàng", lblSoKhachValue));

        return pn;
    }

    private JPanel taoCard(String title, JLabel lbl) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MAU_NAU_DAM, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(FONT_CARD_TITLE);
        lblTitle.setForeground(MAU_NAU_DAM);

        lbl.setFont(FONT_CARD_VALUE);

        card.add(lblTitle, BorderLayout.NORTH);
        card.add(lbl, BorderLayout.CENTER);

        return card;
    }

    // ====================== STYLE FUNCTIONS ===============
    private void styleLabels(JLabel... ls) {
        for (JLabel l : ls) {
            l.setFont(FONT_LABEL);
            l.setForeground(Color.DARK_GRAY);
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

    private void styleDateChoosers(JDateChooser... ds) {
        for (JDateChooser dc : ds) {
            dc.setPreferredSize(new Dimension(150, 30));
            dc.getDateEditor().getUiComponent().setFont(FONT_DATE);
        }
    }

    private void styleSecondaryButton(JButton btn) {
        btn.setBackground(MAU_NAU_NHAT);
        btn.setFont(FONT_BUTTON);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(180, 36));
    }

    private void setupButtonHover() {
        final JButton[] activeBtn = {null};

        Color normal = Color.WHITE;
        Color hover = new Color(245, 230, 220);
        Color press = new Color(230, 200, 190);

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
                    btn.setBackground(press);
                }

                @Override
                public void mouseReleased(java.awt.event.MouseEvent e) {
                    // KHÔNG làm gì để NÚT GIỮ NGUYÊN màu pressed
                }
            });
        }


    }
  

    // ===================== DATE RANGE =====================

    private LocalDate toLocalDate(Date d) {
        if (d == null) return null;
        return d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    private LocalDate[] getKhoangThoiGian() {
        int year = (int) cboNam.getSelectedItem();

        LocalDate from, to;

        if (radTheoNgay.isSelected()) {
            from = toLocalDate(dcTuNgay.getDate());
            to   = toLocalDate(dcDenNgay.getDate());
            if (from == null || to == null) return null;

        } else if (radTheoThang.isSelected()) {
            int month = (int) cboThang.getSelectedItem();
            from = LocalDate.of(year, month, 1);
            to   = from.withDayOfMonth(from.lengthOfMonth());

        } else {
            from = LocalDate.of(year, 1, 1);
            to   = LocalDate.of(year, 12, 31);
        }

        return new LocalDate[]{ from, to };
    }

    // ======================= BIỂU ĐỒ ======================
    private void hienThiBieuDoDoanhThu() {
        pnChartContainer.removeAll();

        LocalDate[] r = getKhoangThoiGian();
        DefaultCategoryDataset ds;
        String title;
        String xlabel;

        if (r == null) {
            ds = new DefaultCategoryDataset();
            title = "Chưa chọn ngày";
            xlabel = "Ngày";

        } else {
            LocalDate from = r[0];
            LocalDate to   = r[1];

            ds = taoDatasetDoanhThu(from, to);
            xlabel = radTheoNam.isSelected() ? "Tháng" : "Ngày";
            title = "Doanh thu";

            capNhatTongQuan(from, to);
        }

        JFreeChart chart = ChartFactory.createBarChart(
                title, xlabel, "Doanh thu (đ)", ds,
                PlotOrientation.VERTICAL, true, true, false);

        pnChartContainer.add(new ChartPanel(chart), BorderLayout.CENTER);
        pnChartContainer.revalidate();
        pnChartContainer.repaint();
    }

    private void hienThiBieuDoSanPham() {
        pnChartContainer.removeAll();

        LocalDate[] r = getKhoangThoiGian();

        DefaultCategoryDataset ds =
                (r == null) ? new DefaultCategoryDataset()
                            : taoDatasetSanPham(r[0], r[1]);

        JFreeChart chart = ChartFactory.createBarChart(
                "Top sản phẩm bán chạy", "Sản phẩm", "Số lượng",
                ds, PlotOrientation.VERTICAL, true, true, false);

        org.jfree.chart.plot.CategoryPlot p = chart.getCategoryPlot();
        org.jfree.chart.axis.CategoryAxis x = p.getDomainAxis();
        x.setCategoryLabelPositions(
                org.jfree.chart.axis.CategoryLabelPositions.createUpRotationLabelPositions(Math.PI / 4));

        pnChartContainer.add(new ChartPanel(chart), BorderLayout.CENTER);
        pnChartContainer.revalidate();
        pnChartContainer.repaint();
    }

    private DefaultCategoryDataset taoDatasetDoanhThu(LocalDate from, LocalDate to) {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();

        Map<LocalDate, Double> map = thongKeDAO.getDoanhThuTheoNgay(from, to);

        if (radTheoNam.isSelected()) {
            double[] t = new double[13];

            for (var e : map.entrySet())
                t[e.getKey().getMonthValue()] += e.getValue();

            for (int m = 1; m <= 12; m++)
                ds.addValue(t[m], "Doanh thu", "Th " + m);

        } else {
            LocalDate d = from;
            while (!d.isAfter(to)) {
                double v = map.getOrDefault(d, 0.0);
                ds.addValue(v, "Doanh thu",
                        d.getDayOfMonth() + "/" + d.getMonthValue());
                d = d.plusDays(1);
            }
        }
        return ds;
    }

    private DefaultCategoryDataset taoDatasetSanPham(LocalDate from, LocalDate to) {
        DefaultCategoryDataset ds = new DefaultCategoryDataset();
        Map<String, Integer> map = thongKeDAO.getTopSanPham(from, to, 20);

        for (var e : map.entrySet())
            ds.addValue(e.getValue(), "Số lượng", e.getKey());

        return ds;
    }

    private void capNhatTongQuan(LocalDate from, LocalDate to) {
        lblTongDoanhThuValue.setText(
                String.format("%,.0f đ", thongKeDAO.getTongDoanhThu(from, to)));

        lblSoHoaDonValue.setText(
                String.valueOf(thongKeDAO.getSoHoaDon(from, to)));

        lblSoKhachValue.setText(
                String.valueOf(thongKeDAO.getSoKhachHang(from, to)));
    }


    // ======================= ACTIONPERFORMED ==================
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
            } else if (radTheoThang.isSelected()) {
                cboThang.setEnabled(true);
                cboNam.setEnabled(true);
                dcTuNgay.setEnabled(false);
                dcDenNgay.setEnabled(false);
            } else if (radTheoNam.isSelected()) {
                cboThang.setEnabled(false);
                cboNam.setEnabled(true);
                dcTuNgay.setEnabled(false);
                dcDenNgay.setEnabled(false);
            }

            capNhatBieuDoTheoLoai();

        } else if (o == btnXuatExcel) {

            LocalDate[] range = getKhoangThoiGian();
            if (range == null) {
                JOptionPane.showMessageDialog(this,
                        "Vui lòng chọn ngày hợp lệ",
                        "Thiếu dữ liệu",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            exportBaoCaoExcel(range[0], range[1]);
        }
    }

    private void capNhatBieuDoTheoLoai() {
        if (isChartDoanhThu)
            hienThiBieuDoDoanhThu();
        else
            hienThiBieuDoSanPham();
    }

    private void exportBaoCaoExcel(LocalDate from, LocalDate to) {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("BaoCao_" + from + "_den_" + to + ".xlsx"));

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION)
            return;

        File file = chooser.getSelectedFile();

        try (Workbook wb = new XSSFWorkbook()) {

            createSummarySheet(wb, from, to);
            createRevenueSheet(wb, from, to);
            createTopProductSheet(wb, from, to);
            createHoaDonSheet(wb, from, to);

            try (FileOutputStream f = new FileOutputStream(file)) {
                wb.write(f);
            }

            JOptionPane.showMessageDialog(this, "Xuất Excel thành công!");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi xuất Excel: " + ex.getMessage());
        }
    }

    // ======== SHEET 1 – TỔNG QUAN ========
    private void createSummarySheet(Workbook wb, LocalDate from, LocalDate to) {
        Sheet sheet = wb.createSheet("Tong quan");

        CellStyle title = wb.createCellStyle();
        org.apache.poi.ss.usermodel.Font f = wb.createFont();
        f.setBold(true);
        f.setFontHeightInPoints((short) 16);
        title.setFont(f);

        CellStyle bold = wb.createCellStyle();
        org.apache.poi.ss.usermodel.Font f2 = wb.createFont();
        f2.setBold(true);
        bold.setFont(f2);

        CellStyle money = wb.createCellStyle();
        money.setDataFormat(wb.createDataFormat().getFormat("#,##0"));

        int r = 0;

        Row t = sheet.createRow(r++);
        Cell c = t.createCell(0);
        c.setCellValue("BÁO CÁO THỐNG KÊ QUÁN CAFE");
        c.setCellStyle(title);

        r++;

        Row rt = sheet.createRow(r++);
        rt.createCell(0).setCellValue("Từ ngày:");
        rt.createCell(1).setCellValue(from.toString());
        rt.createCell(2).setCellValue("Đến ngày:");
        rt.createCell(3).setCellValue(to.toString());
        rt.getCell(0).setCellStyle(bold);
        rt.getCell(2).setCellStyle(bold);

        Row rn = sheet.createRow(r++);
        rn.createCell(0).setCellValue("Người lập:");
        rn.createCell(1).setCellValue(tenHienThi);
        rn.getCell(0).setCellStyle(bold);

        r++;

        Row rdt = sheet.createRow(r++);
        rdt.createCell(0).setCellValue("Tổng doanh thu:");
        Cell cd = rdt.createCell(1);
        cd.setCellValue(thongKeDAO.getTongDoanhThu(from, to));
        cd.setCellStyle(money);
        rdt.getCell(0).setCellStyle(bold);

        Row rhd = sheet.createRow(r++);
        rhd.createCell(0).setCellValue("Số hóa đơn:");
        rhd.createCell(1).setCellValue(thongKeDAO.getSoHoaDon(from, to));
        rhd.getCell(0).setCellStyle(bold);

        Row rkh = sheet.createRow(r++);
        rkh.createCell(0).setCellValue("Số khách:");
        rkh.createCell(1).setCellValue(thongKeDAO.getSoKhachHang(from, to));
        rkh.getCell(0).setCellStyle(bold);

        for (int i = 0; i < 4; i++)
            sheet.autoSizeColumn(i);
    }

    // ======== SHEET 2 – DOANH THU TỪNG NGÀY ========
    private void createRevenueSheet(Workbook wb, LocalDate from, LocalDate to) {
        Sheet sheet = wb.createSheet("Doanh thu ngay");

        CellStyle header = wb.createCellStyle();
        org.apache.poi.ss.usermodel.Font f = wb.createFont();
        f.setBold(true);
        header.setFont(f);

        CellStyle money = wb.createCellStyle();
        money.setDataFormat(wb.createDataFormat().getFormat("#,##0"));

        int r = 0;

        Row h = sheet.createRow(r++);
        h.createCell(0).setCellValue("Ngày");
        h.createCell(1).setCellValue("Doanh thu");
        h.getCell(0).setCellStyle(header);
        h.getCell(1).setCellStyle(header);

        Map<LocalDate, Double> map = thongKeDAO.getDoanhThuTheoNgay(from, to);

        LocalDate d = from;
        while (!d.isAfter(to)) {
            Row row = sheet.createRow(r++);
            row.createCell(0).setCellValue(d.toString());

            Cell c = row.createCell(1);
            c.setCellValue(map.getOrDefault(d, 0.0));
            c.setCellStyle(money);

            d = d.plusDays(1);
        }

        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    // ======== SHEET 3 – TOP SẢN PHẨM ========
    private void createTopProductSheet(Workbook wb, LocalDate from, LocalDate to) {
        Sheet sheet = wb.createSheet("Top san pham");

        CellStyle header = wb.createCellStyle();
        org.apache.poi.ss.usermodel.Font f = wb.createFont();
        f.setBold(true);
        header.setFont(f);

        CellStyle num = wb.createCellStyle();
        num.setDataFormat(wb.createDataFormat().getFormat("#,##0"));

        CellStyle money = wb.createCellStyle();
        money.setDataFormat(wb.createDataFormat().getFormat("#,##0"));

        int r = 0;

        Row h = sheet.createRow(r++);
        String[] cols = { "Tên sản phẩm", "Số lượng", "Doanh thu", "Giá TB" };

        for (int i = 0; i < cols.length; i++) {
            h.createCell(i).setCellValue(cols[i]);
            h.getCell(i).setCellStyle(header);
        }

        String sql =
            "SELECT TOP (10) sp.tenSP, " +
            "SUM(ct.soLuong) AS soLuongBan, " +
            "SUM(ct.soLuong * sp.donGia) AS doanhThuSP, " +
            "AVG(CAST(sp.donGia AS DECIMAL(18,2))) AS giaTB " +
            "FROM ChiTietHoaDon ct " +
            "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
            "JOIN SanPham sp ON ct.maSP = sp.maSP " +
            "WHERE CONVERT(date, hd.thoiGianVao) BETWEEN ? AND ? AND hd.trangThai = 1 " +
            "GROUP BY sp.tenSP " +
            "ORDER BY soLuongBan DESC";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(from));
            ps.setDate(2, java.sql.Date.valueOf(to));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Row row = sheet.createRow(r++);

                    row.createCell(0).setCellValue(rs.getString("tenSP"));

                    Cell cSL = row.createCell(1);
                    cSL.setCellValue(rs.getInt("soLuongBan"));
                    cSL.setCellStyle(num);

                    Cell cDT = row.createCell(2);
                    cDT.setCellValue(rs.getDouble("doanhThuSP"));
                    cDT.setCellStyle(money);

                    Cell cGia = row.createCell(3);
                    cGia.setCellValue(rs.getDouble("giaTB"));
                    cGia.setCellStyle(money);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (int i = 0; i < cols.length; i++)
            sheet.autoSizeColumn(i);
    }

    // ======== SHEET 4 – DANH SÁCH HÓA ĐƠN ========
    private void createHoaDonSheet(Workbook wb, LocalDate from, LocalDate to) {
        Sheet sheet = wb.createSheet("Danh sach hoa don");

        CellStyle header = wb.createCellStyle();
        org.apache.poi.ss.usermodel.Font f = wb.createFont();
        f.setBold(true);
        header.setFont(f);

        CellStyle money = wb.createCellStyle();
        money.setDataFormat(wb.createDataFormat().getFormat("#,##0"));

        int r = 0;

        Row h = sheet.createRow(r++);
        String[] cols = {
                "Mã HĐ", "Bàn", "Mã KH", "Mã NV",
                "Thời gian vào", "Thời gian ra",
                "Giảm giá", "Tổng tiền", "Khách trả"
        };

        for (int i = 0; i < cols.length; i++) {
            h.createCell(i).setCellValue(cols[i]);
            h.getCell(i).setCellStyle(header);
        }

        String sql =
            "SELECT maHD, maBan, maKH, maNV, thoiGianVao, thoiGianRa, giamGia, tongTien, tienKhachTra " +
            "FROM HoaDon " +
            "WHERE CONVERT(date, thoiGianVao) BETWEEN ? AND ? AND trangThai = 1 " +
            "ORDER BY thoiGianVao";

        try (Connection con = ConnectDB.getInstance().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(from));
            ps.setDate(2, java.sql.Date.valueOf(to));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Row row = sheet.createRow(r++);

                    row.createCell(0).setCellValue(rs.getString("maHD"));
                    row.createCell(1).setCellValue(rs.getString("maBan"));
                    row.createCell(2).setCellValue(rs.getString("maKH") == null ? "" : rs.getString("maKH"));
                    row.createCell(3).setCellValue(rs.getString("maNV"));
                    row.createCell(4).setCellValue(rs.getTimestamp("thoiGianVao").toString());

                    row.createCell(5).setCellValue(
                            rs.getTimestamp("thoiGianRa") == null ?
                                    "" : rs.getTimestamp("thoiGianRa").toString()
                    );

                    row.createCell(6).setCellValue(rs.getInt("giamGia"));

                    Cell cTong = row.createCell(7);
                    cTong.setCellValue(rs.getDouble("tongTien"));
                    cTong.setCellStyle(money);

                    Cell cTra = row.createCell(8);
                    cTra.setCellValue(rs.getDouble("tienKhachTra"));
                    cTra.setCellStyle(money);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (int i = 0; i < cols.length; i++)
            sheet.autoSizeColumn(i);
    }

}
