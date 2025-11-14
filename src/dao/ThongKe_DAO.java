package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import connectDB.ConnectDB;

public class ThongKe_DAO {

    private Connection con;

    public ThongKe_DAO() {
        con = ConnectDB.getInstance().getConnection();
    }

    /**
     * Doanh thu theo ngày trong khoảng [fromDate, toDate]
     * CHỈ lấy hóa đơn đã thanh toán (trangThai = 1)
     */
    public Map<LocalDate, Double> getDoanhThuTheoNgay(LocalDate fromDate, LocalDate toDate) {
        Map<LocalDate, Double> result = new LinkedHashMap<>();

        String sql =
            "SELECT CONVERT(date, thoiGianVao) AS ngay, SUM(tongTien) AS doanhThu " +
            "FROM HoaDon " +
            "WHERE CONVERT(date, thoiGianVao) BETWEEN ? AND ? " +
            "  AND trangThai = 1 " +           // chỉ hóa đơn đã thanh toán
            "GROUP BY CONVERT(date, thoiGianVao) " +
            "ORDER BY ngay";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fromDate));
            ps.setDate(2, Date.valueOf(toDate));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LocalDate day = rs.getDate("ngay").toLocalDate();
                    double doanhThu = rs.getDouble("doanhThu");
                    result.put(day, doanhThu);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Tổng doanh thu trong khoảng [fromDate, toDate]
     * CHỈ hóa đơn đã thanh toán
     */
    public double getTongDoanhThu(LocalDate fromDate, LocalDate toDate) {
        double tong = 0;

        String sql =
            "SELECT SUM(tongTien) AS tong " +
            "FROM HoaDon " +
            "WHERE CONVERT(date, thoiGianVao) BETWEEN ? AND ? " +
            "  AND trangThai = 1";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fromDate));
            ps.setDate(2, Date.valueOf(toDate));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tong = rs.getDouble("tong");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tong;
    }

    /**
     * Số hóa đơn trong khoảng [fromDate, toDate]
     * CHỈ hóa đơn đã thanh toán
     */
    public int getSoHoaDon(LocalDate fromDate, LocalDate toDate) {
        int count = 0;

        String sql =
            "SELECT COUNT(*) AS soHD " +
            "FROM HoaDon " +
            "WHERE CONVERT(date, thoiGianVao) BETWEEN ? AND ? " +
            "  AND trangThai = 1";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fromDate));
            ps.setDate(2, Date.valueOf(toDate));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("soHD");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Số khách hàng (distinct maKH != null) trong khoảng [fromDate, toDate]
     * CHỈ hóa đơn đã thanh toán
     */
    public int getSoKhachHang(LocalDate fromDate, LocalDate toDate) {
        int count = 0;

        String sql =
            "SELECT COUNT(DISTINCT maKH) AS soKH " +
            "FROM HoaDon " +
            "WHERE CONVERT(date, thoiGianVao) BETWEEN ? AND ? " +
            "  AND trangThai = 1 " +
            "  AND maKH IS NOT NULL";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fromDate));
            ps.setDate(2, Date.valueOf(toDate));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("soKH");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

    /**
     * Top sản phẩm bán chạy trong khoảng [fromDate, toDate]
     * CHỈ hóa đơn đã thanh toán
     */
    public Map<String, Integer> getTopSanPham(LocalDate fromDate, LocalDate toDate, int limit) {
        Map<String, Integer> result = new LinkedHashMap<>();

        String sql =
            "SELECT TOP (?) sp.tenSP, SUM(ct.soLuong) AS soLuongBan " +
            "FROM ChiTietHoaDon ct " +
            "JOIN HoaDon hd ON ct.maHD = hd.maHD " +
            "JOIN SanPham sp ON ct.maSP = sp.maSP " +
            "WHERE CONVERT(date, hd.thoiGianVao) BETWEEN ? AND ? " +
            "  AND hd.trangThai = 1 " +          // chỉ bill đã thanh toán
            "GROUP BY sp.tenSP " +
            "ORDER BY soLuongBan DESC";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ps.setDate(2, Date.valueOf(fromDate));
            ps.setDate(3, Date.valueOf(toDate));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String tenSP = rs.getString("tenSP");
                    int soLuong = rs.getInt("soLuongBan");
                    result.put(tenSP, soLuong);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
