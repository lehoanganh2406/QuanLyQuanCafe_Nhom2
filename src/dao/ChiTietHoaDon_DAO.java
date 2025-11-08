package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.ChiTietHoaDon;

public class ChiTietHoaDon_DAO {
	private static ChiTietHoaDon_DAO instance;
	public static ChiTietHoaDon_DAO getInstance() {
		if (instance == null) instance = new ChiTietHoaDon_DAO();
        return instance;
    }
	private Connection getConnection() {
        return ConnectDB.getInstance().getConnection();
    }
	public boolean themChiTiet(ChiTietHoaDon ct) {
        String sql = "INSERT INTO ChiTietHoaDon (maHD, maSP, maNV, soLuong) VALUES (?, ?, ?, ?)";
        Connection con = ConnectDB.getInstance().getConnection();
        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, ct.getHoaDon().getMaHD());
            ps.setString(2, ct.getSanPham().getMaSP());
            ps.setString(3, ct.getNhanVien().getMaNV());
            ps.setInt(4, ct.getSoLuong());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Nếu lỗi vi phạm khóa chính (trùng sản phẩm trong cùng hóa đơn)
            if (e.getErrorCode() == 2627 || e.getErrorCode() == 2601) {
                System.err.println("Sản phẩm đã tồn tại trong chi tiết hóa đơn này!");
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }
	public List<ChiTietHoaDon> getChiTietTheoMaHD(String maHD) {
        List<ChiTietHoaDon> list = new ArrayList<>();
        String sql = "SELECT maHD, maSP, maNV, soLuong FROM ChiTietHoaDon WHERE maHD = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, maHD);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new ChiTietHoaDon(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
