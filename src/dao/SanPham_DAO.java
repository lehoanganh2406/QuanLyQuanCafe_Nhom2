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
                         "ls.maLoai, ls.loaiSP " +  
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

    public List<SanPham> getSanPhamByLoai(String tenLoai) {
        List<SanPham> ds = new ArrayList<>();
        try {
            Connection con = ConnectDB.getInstance().getConnection();
            String sql = "SELECT sp.maSP, sp.tenSP, sp.soLuong, sp.donGia, sp.img, sp.moTa, " +
                         "ls.maLoai, ls.loaiSP " +  
                         "FROM SanPham sp JOIN LoaiSanPham ls ON sp.maLoai = ls.maLoai " +
                         "WHERE ls.loaiSP = ?";   
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
                         "ls.maLoai, ls.loaiSP " + 
                         "FROM SanPham sp JOIN LoaiSanPham ls ON sp.maLoai = ls.maLoai " +
                         "WHERE sp.maSP LIKE ? OR sp.tenSP LIKE ? OR ls.loaiSP LIKE ?"; 
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
    
    public boolean themSanPham(SanPham sp) {
    	Connection con = ConnectDB.getInstance().getConnection();
        String sql = "INSERT INTO SanPham (tenSP, soLuong, donGia, img, maLoai, moTa) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSP());
            ps.setInt(2, sp.getSoLuong());
            ps.setDouble(3, sp.getDonGia());
            ps.setString(4, sp.getImg());
            ps.setString(5, sp.getLoaiSP().getMaLoai());
            ps.setString(6, sp.getMoTa());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean xoaSanPham(String maSP) {
        Connection con = ConnectDB.getInstance().getConnection();
        String sql = "DELETE FROM SanPham WHERE maSP = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean suaSanPham(SanPham sp) {
        Connection con = ConnectDB.getInstance().getConnection();
        String sql = "UPDATE SanPham SET tenSP = ?, soLuong = ?, donGia = ?, img = ?, moTa = ?, maLoai = ? WHERE maSP = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sp.getTenSP());
            ps.setInt(2, sp.getSoLuong());
            ps.setDouble(3, sp.getDonGia());
            ps.setString(4, sp.getImg());
            ps.setString(5, sp.getMoTa());
            ps.setString(6, sp.getLoaiSP().getMaLoai());
            ps.setString(7, sp.getMaSP());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<LoaiSanPham> getAllLoaiSanPham() {
        List<LoaiSanPham> ds = new ArrayList<>();
        Connection con = ConnectDB.getInstance().getConnection();
        try {
            String sql = "SELECT maLoai, loaiSP FROM LoaiSanPham";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ds.add(new LoaiSanPham(rs.getString("maLoai"), rs.getString("loaiSP")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }


}
