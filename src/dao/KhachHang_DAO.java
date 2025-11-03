package dao;

import connectDB.ConnectDB;
import entity.KhachHang;
import entity.Order;

import java.sql.*;
import java.util.ArrayList;

public class KhachHang_DAO {
    private static KhachHang_DAO instance;
    public static KhachHang_DAO getInstance() {
        if (instance == null) instance = new KhachHang_DAO();
        return instance;
    }
    
    public ArrayList<KhachHang> getAllKhachHang() {
        ArrayList<KhachHang> ds = new ArrayList<>();
        String sql ="SELECT maKH, tenKH, sdt, diemTL FROM dbo.KhachHang ORDER BY tenKH";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(new KhachHang(
                		rs.getString("maKH"),
                        rs.getString("tenKH"),
                        rs.getString("sdt"),
                        rs.getInt("diemTL")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ds;
    }
 
    /** Tìm KH theo mã */
    public KhachHang getByMa(String maKH) {
        String sql = "SELECT maKH, tenKH, sdt, diemTL FROM dbo.KhachHang WHERE maKH = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKH);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KhachHang(
                            rs.getString("maKH"),
                            rs.getString("tenKH"),
                            rs.getString("sdt"),
                            rs.getInt("diemTL")
                    );
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    /** Tìm KH theo SĐT */
    public KhachHang getBySDT(String sdt) {
        String sql = "SELECT maKH, tenKH, sdt, diemTL FROM dbo.KhachHang WHERE sdt = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sdt);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KhachHang(
                            rs.getString("maKH"),
                            rs.getString("tenKH"),
                            rs.getString("sdt"),
                            rs.getInt("diemTL")
                    );
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    /** Thêm KH (maKH auto từ DEFAULT của DB) */
    public boolean insert(KhachHang kh) {
        String sql = "INSERT INTO dbo.KhachHang (tenKH, sdt, diemTL) VALUES (?, ?, ?)";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getSdt());
            ps.setInt(3, kh.getDiemTL());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    /** Cập nhật thông tin cơ bản theo mã */
    public boolean updateInfo(KhachHang kh) {
        String sql = "UPDATE dbo.KhachHang SET tenKH = ?, sdt = ? WHERE maKH = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getSdt());
            ps.setString(3, kh.getMaKH());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    /** Cập nhật điểm tích luỹ */
    public boolean updateDiem(String maKH, int diemMoi) {
        String sql = "UPDATE dbo.KhachHang SET diemTL = ? WHERE maKH = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, diemMoi);
            ps.setString(2, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    /** Xoá KH theo mã */
    public boolean delete(String maKH) {
        String sql = "DELETE FROM dbo.KhachHang WHERE maKH = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maKH);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    /** Tìm kiếm theo tên hoặc SĐT (keyword LIKE) */
    public ArrayList<KhachHang> searchByNameOrPhone(String keyword) {
        ArrayList<KhachHang> ds = new ArrayList<>();
        String sql = """
            SELECT maKH, tenKH, sdt, diemTL
            FROM dbo.KhachHang
            WHERE tenKH LIKE ? OR sdt LIKE ?
            ORDER BY tenKH
        """;
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(new KhachHang(
                            rs.getString("maKH"),
                            rs.getString("tenKH"),
                            rs.getString("sdt"),
                            rs.getInt("diemTL")
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ds;
    }
}