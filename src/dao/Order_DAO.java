package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.Order;

public class Order_DAO {
    private static Order_DAO instance;
    public static Order_DAO getInstance() {
        if (instance == null) instance = new Order_DAO();
        return instance;
    }

//    Lấy tất cả sản phẩm + tên loại 
    public ArrayList<Order> getAllSanPham() {
        ArrayList<Order> ds = new ArrayList<>();
        String sql =
            "SELECT sp.maSP, sp.tenSP, sp.soLuong, sp.donGia, sp.img, " +
            "       ls.loaiSP AS loaiSP " +
            "FROM dbo.SanPham sp " +
            "JOIN dbo.LoaiSanPham ls ON ls.maLoai = sp.maLoai " +
            "ORDER BY sp.tenSP";

        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(new Order(
                    rs.getString("maSP"),
                    rs.getString("tenSP"),
                    rs.getInt("soLuong"),
                    rs.getDouble("donGia"),
                    rs.getString("loaiSP"),
                    rs.getString("img")
                ));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ds;
    }

//    Lấy sản phẩm theo mã SP  
    public ArrayList<Order> getSanPhamByMaSP(String maSP) {
        String sql =
            "SELECT sp.maSP, sp.tenSP, sp.soLuong, sp.donGia, sp.img, " +
            "       ls.loaiSP AS loaiSP " +
            "FROM dbo.SanPham sp " +
            "JOIN dbo.LoaiSanPham ls ON ls.maLoai = sp.maLoai " +
            "WHERE sp.maSP = ?";

        ArrayList<Order> dsList = new ArrayList<>();
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    dsList.add(new Order(
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        rs.getInt("soLuong"),
                        rs.getDouble("donGia"),
                        rs.getString("loaiSP"),
                        rs.getString("img")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsList;
    }

//    Lấy sản phẩm theo mã loại
    public ArrayList<Order> getSanPhamByLoai(String maLoai) {
        String sql =
            "SELECT sp.maSP, sp.tenSP, sp.soLuong, sp.donGia, sp.img, " +
            "       ls.loaiSP AS loaiSP " +
            "FROM dbo.SanPham sp " +
            "JOIN dbo.LoaiSanPham ls ON ls.maLoai = sp.maLoai " +
            "WHERE sp.maLoai = ? " +
            "ORDER BY sp.tenSP";

        ArrayList<Order> ds = new ArrayList<>();
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maLoai);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(new Order(
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        rs.getInt("soLuong"),
                        rs.getDouble("donGia"),
                        rs.getString("loaiSP"),
                        rs.getString("img")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

//     Tìm theo tên sản phẩm 
    public ArrayList<Order> searchByName(String keyword) {
        ArrayList<Order> ds = new ArrayList<>();
        String sql =
            "SELECT sp.maSP, sp.tenSP, sp.soLuong, sp.donGia, sp.img, ls.loaiSP AS loaiSP " +
            "FROM dbo.SanPham sp " +
            "JOIN dbo.LoaiSanPham ls ON ls.maLoai = sp.maLoai " +
            "WHERE sp.tenSP LIKE ? " +
            "ORDER BY sp.tenSP";

        Connection c = ConnectDB.getInstance().getConnection();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(new Order(
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        rs.getInt("soLuong"),
                        rs.getDouble("donGia"),
                        rs.getString("loaiSP"),
                        rs.getString("img")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

//     Tìm theo tên + mã loại 
    public ArrayList<Order> searchByNameAndLoai(String keyword, String maLoai) {
        ArrayList<Order> ds = new ArrayList<>();
        String sql =
            "SELECT sp.maSP, sp.tenSP, sp.soLuong, sp.donGia, sp.img, ls.loaiSP AS loaiSP " +
            "FROM dbo.SanPham sp " +
            "JOIN dbo.LoaiSanPham ls ON ls.maLoai = sp.maLoai " +
            "WHERE sp.maLoai = ? AND sp.tenSP LIKE ? " +
            "ORDER BY sp.tenSP";

        Connection c = ConnectDB.getInstance().getConnection();
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, maLoai);
            ps.setString(2, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(new Order(
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        rs.getInt("soLuong"),
                        rs.getDouble("donGia"),
                        rs.getString("loaiSP"),
                        rs.getString("img")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
}