package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import connectDB.connectDB;
import entity.Ban;

public class Ban_DAO {
    private static Ban_DAO instance;

    public static Ban_DAO getInstance() {
        if (instance == null) instance = new Ban_DAO();
        return instance;
    }

//    Lấy toàn bộ bàn 
    public List<Ban> getAllBan() {
        String sql = "SELECT maBan, tenBan, trangThai FROM dbo.Ban";
        List<Ban> dsList = new ArrayList<>();
        Connection con = connectDB.getConnection();
        try (
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dsList.add(new Ban(rs)); // Ban(ResultSet) đã dùng getString()
            }
            System.out.println("Lấy danh sách bàn thành công");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsList;
    }

//    Tìm bàn theo mã 
    public Ban getBanByMaBan(String maBan) {
        String sql = "SELECT maBan, tenBan, trangThai FROM dbo.Ban WHERE maBan = ?";
        Ban b = null;
        Connection con = connectDB.getConnection();
        try (
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, maBan);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    b = new Ban(rs); 
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return b;
    }

//    Thêm bàn 
    public boolean themBan(Ban b) {
        String sql = "INSERT INTO dbo.Ban (tenBan, trangThai) VALUES (?, ?)";
        Connection con = connectDB.getConnection();
        try (
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, b.getTenBan());
            pst.setString(2, b.getTrangThai());
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

//    Cập nhật bàn theo mã  
    public boolean capNhatBan(Ban b) {
        String sql = "UPDATE dbo.Ban SET tenBan = ?, trangThai = ? WHERE maBan = ?";
        Connection con = connectDB.getConnection();
        try (
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, b.getTenBan());
            pst.setString(2, b.getTrangThai());
            pst.setString(3, b.getMaBan()); // đổi sang String
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

//     Xoá bàn theo mã 
    public boolean xoaBan(String maBan) {
        String sql = "DELETE FROM dbo.Ban WHERE maBan = ?";
        Connection con = connectDB.getConnection();
        try (
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, maBan);
            return pst.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
