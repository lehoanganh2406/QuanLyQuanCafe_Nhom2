package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.LoaiSanPham;
import entity.SanPham;

public class SanPham_DAO {
    private static SanPham_DAO instance;

    public static SanPham_DAO getInstance() {
        if (instance == null) instance = new SanPham_DAO();
        return instance;
    }

    private Connection getCon() {
        return ConnectDB.getInstance().getConnection();
    }

    // ========== SELECT ==========

    public List<SanPham> getAllSanPham() {
        List<SanPham> ds = new ArrayList<>();
        String sql = "SELECT sp.maSP, sp.tenSP, sp.soLuong, sp.donGia, sp.img, sp.moTa, " +
                     "       ls.maLoai, ls.loaiSP " +
                     "FROM SanPham sp " +
                     "JOIN LoaiSanPham ls ON sp.maLoai = ls.maLoai " +
                     "ORDER BY sp.tenSP";
        try (PreparedStatement ps = getCon().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(new SanPham(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public List<SanPham> getSanPhamByMaSP(String maSP) {
        List<SanPham> ds = new ArrayList<>();
        String sql = "SELECT sp.maSP, sp.tenSP, sp.soLuong, sp.donGia, sp.img, sp.moTa, " +
                     "       ls.maLoai, ls.loaiSP " +
                     "FROM SanPham sp " +
                     "JOIN LoaiSanPham ls ON sp.maLoai = ls.maLoai " +
                     "WHERE sp.maSP = ?";
        try (PreparedStatement ps = getCon().prepareStatement(sql)) {
            ps.setString(1, maSP);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(new SanPham(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public List<SanPham> getSanPhamByMaLoai(String maLoai) {
        List<SanPham> ds = new ArrayList<>();
        String sql = "SELECT sp.maSP, sp.tenSP, sp.soLuong, sp.donGia, sp.img, sp.moTa, " +
                     "       ls.maLoai, ls.loaiSP " +
                     "FROM SanPham sp " +
                     "JOIN LoaiSanPham ls ON sp.maLoai = ls.maLoai " +
                     "WHERE sp.maLoai = ? " +
                     "ORDER BY sp.tenSP";
        try (PreparedStatement ps = getCon().prepareStatement(sql)) {
            ps.setString(1, maLoai);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(new SanPham(rs));
                }
            }
        } catch (SQLException e) {
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

    public List<SanPham> search(String keyword) {
        List<SanPham> ds = new ArrayList<>();
        String sql = "SELECT sp.maSP, sp.tenSP, sp.soLuong, sp.donGia, sp.img, sp.moTa, " +
                     "       ls.maLoai, ls.loaiSP " +
                     "FROM SanPham sp " +
                     "JOIN LoaiSanPham ls ON sp.maLoai = ls.maLoai " +
                     "WHERE sp.maSP LIKE ? OR sp.tenSP LIKE ? OR ls.loaiSP LIKE ? " +
                     "ORDER BY sp.tenSP";
        try (PreparedStatement ps = getCon().prepareStatement(sql)) {
            String p = "%" + keyword + "%";
            ps.setString(1, p);
            ps.setString(2, p);
            ps.setString(3, p);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(new SanPham(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    public List<SanPham> searchByNameAndLoai(String keyword, String maLoai) {
        List<SanPham> ds = new ArrayList<>();
        String sql = "SELECT sp.maSP, sp.tenSP, sp.soLuong, sp.donGia, sp.img, sp.moTa, " +
                     "       ls.maLoai, ls.loaiSP " +
                     "FROM SanPham sp " +
                     "JOIN LoaiSanPham ls ON sp.maLoai = ls.maLoai " +
                     "WHERE sp.maLoai = ? AND sp.tenSP LIKE ? " +
                     "ORDER BY sp.tenSP";
        try (PreparedStatement ps = getCon().prepareStatement(sql)) {
            ps.setString(1, maLoai);
            ps.setString(2, "%" + keyword + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(new SanPham(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // ========== CRUD ==========

    public boolean themSanPham(SanPham sp) {
        String sql = "INSERT INTO SanPham (tenSP, soLuong, donGia, img, maLoai, moTa) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getCon().prepareStatement(sql)) {
            ps.setString(1, sp.getTenSP());
            ps.setInt(2, sp.getSoLuong());
            ps.setDouble(3, sp.getDonGia());
            ps.setString(4, sp.getImg());
            ps.setString(5, sp.getLoaiSP().getMaLoai());
            ps.setString(6, sp.getMoTa());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean suaSanPham(SanPham sp) {
        String sql = "UPDATE SanPham " +
                     "SET tenSP = ?, soLuong = ?, donGia = ?, img = ?, maLoai = ?, moTa = ? " +
                     "WHERE maSP = ?";
        try (PreparedStatement ps = getCon().prepareStatement(sql)) {
            ps.setString(1, sp.getTenSP());
            ps.setInt(2, sp.getSoLuong());
            ps.setDouble(3, sp.getDonGia());
            ps.setString(4, sp.getImg());
            ps.setString(5, sp.getLoaiSP().getMaLoai());
            ps.setString(6, sp.getMoTa());
            ps.setString(7, sp.getMaSP());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean xoaSanPham(String maSP) {
        String sql = "DELETE FROM SanPham WHERE maSP = ?";
        try (PreparedStatement ps = getCon().prepareStatement(sql)) {
            ps.setString(1, maSP);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ========== Loại sản phẩm ==========

    public List<LoaiSanPham> getAllLoaiSanPham() {
        List<LoaiSanPham> ds = new ArrayList<>();
        String sql = "SELECT maLoai, loaiSP FROM LoaiSanPham";
        try (PreparedStatement ps = getCon().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(new LoaiSanPham(
                        rs.getString("maLoai"),
                        rs.getString("loaiSP")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }
}
