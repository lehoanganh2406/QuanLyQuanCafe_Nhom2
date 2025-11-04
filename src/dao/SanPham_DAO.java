package dao;

import java.util.*;
import entity.*;
import java.sql.*;
import connectDB.ConnectDB;

public class SanPham_DAO {
    private static SanPham_DAO instance;

    public static SanPham_DAO getInstance() {
        if (instance == null) instance = new SanPham_DAO();
        return instance;
    }

    // 🔹 Lấy tất cả sản phẩm
    public List<SanPham> getAllSanPham() {
        List<SanPham> ds = new ArrayList<>();
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "SELECT sp.maSP, sp.tenSP, sp.soLuong, sp.donGia, sp.img, sp.moTa, " +
                         "ls.maLoai, ls.loaiSP " +   // ✅ sửa ở đây
                         "FROM SanPham sp JOIN LoaiSanPham ls ON sp.maLoai = ls.maLoai";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LoaiSanPham loai = new LoaiSanPham(rs.getString("maLoai"), rs.getString("loaiSP"));
                SanPham sp = new SanPham(
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        rs.getInt("soLuong"),
                        rs.getDouble("donGia"),
                        rs.getString("img"),
                        loai,
                        rs.getString("moTa")
                );
                ds.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    // 🔹 Lấy sản phẩm theo loại
    public List<SanPham> getSanPhamByLoai(String tenLoai) {
        List<SanPham> ds = new ArrayList<>();
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "SELECT sp.maSP, sp.tenSP, sp.soLuong, sp.donGia, sp.img, sp.moTa, " +
                         "ls.maLoai, ls.loaiSP " +  // ✅ sửa ở đây
                         "FROM SanPham sp JOIN LoaiSanPham ls ON sp.maLoai = ls.maLoai " +
                         "WHERE ls.loaiSP = ?";     // ✅ sửa ở đây
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, tenLoai);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LoaiSanPham loai = new LoaiSanPham(rs.getString("maLoai"), rs.getString("loaiSP"));
                SanPham sp = new SanPham(
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        rs.getInt("soLuong"),
                        rs.getDouble("donGia"),
                        rs.getString("img"),
                        loai,
                        rs.getString("moTa")
                );
                ds.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    // 🔹 Tìm kiếm theo từ khóa
    public List<SanPham> search(String keyword) {
        List<SanPham> ds = new ArrayList<>();
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "SELECT sp.maSP, sp.tenSP, sp.soLuong, sp.donGia, sp.img, sp.moTa, " +
                         "ls.maLoai, ls.loaiSP " +  // ✅ sửa ở đây
                         "FROM SanPham sp JOIN LoaiSanPham ls ON sp.maLoai = ls.maLoai " +
                         "WHERE sp.maSP LIKE ? OR sp.tenSP LIKE ? OR ls.loaiSP LIKE ?";  // ✅ sửa ở đây
            PreparedStatement stmt = con.prepareStatement(sql);
            String pattern = "%" + keyword + "%";
            stmt.setString(1, pattern);
            stmt.setString(2, pattern);
            stmt.setString(3, pattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                LoaiSanPham loai = new LoaiSanPham(rs.getString("maLoai"), rs.getString("loaiSP"));
                SanPham sp = new SanPham(
                        rs.getString("maSP"),
                        rs.getString("tenSP"),
                        rs.getInt("soLuong"),
                        rs.getDouble("donGia"),
                        rs.getString("img"),
                        loai,
                        rs.getString("moTa")
                );
                ds.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }
}
