package dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.NhanVien;

public class NhanVien_DAO {

    private Connection con;

    public NhanVien_DAO() {
        con = ConnectDB.getInstance().getConnection();
    }

    // Chuyển util.Date -> sql.Date
    private Date toSqlDate(java.util.Date d) {
        return d != null ? new Date(d.getTime()) : null;
    }

    // ================== LẤY TẤT CẢ NHÂN VIÊN ==================
    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> ds = new ArrayList<>();
        String sql = "SELECT maNV, hoTen, diaChi, CCCD, dienThoai, gioiTinh, ngaySinh, ngayVaoLam, chucVu FROM NhanVien";

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                NhanVien nv = new NhanVien(
                        rs.getString("maNV"),
                        rs.getString("hoTen"),
                        rs.getString("diaChi"),
                        rs.getString("CCCD"),
                        rs.getString("dienThoai"),
                        rs.getBoolean("gioiTinh"),            // BIT -> Boolean
                        rs.getDate("ngaySinh"),
                        rs.getDate("ngayVaoLam"),
                        rs.getString("chucVu")
                );
                ds.add(nv);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ds;
    }

    // ================== LẤY THEO MÃ ==================
    public NhanVien getNhanVienTheoMa(String maNV) {
        String sql = "SELECT maNV, hoTen, diaChi, CCCD, dienThoai, gioiTinh, ngaySinh, ngayVaoLam, chucVu "
                   + "FROM NhanVien WHERE maNV = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNV);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new NhanVien(
                            rs.getString("maNV"),
                            rs.getString("hoTen"),
                            rs.getString("diaChi"),
                            rs.getString("CCCD"),
                            rs.getString("dienThoai"),
                            rs.getBoolean("gioiTinh"),
                            rs.getDate("ngaySinh"),
                            rs.getDate("ngayVaoLam"),
                            rs.getString("chucVu")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ================== THÊM NHÂN VIÊN ==================
    public NhanVien themNhanVien(NhanVien nv) {
        String sql = "INSERT INTO NhanVien " +
                "(hoTen, diaChi, CCCD, dienThoai, gioiTinh, ngaySinh, ngayVaoLam, chucVu) " +
                "OUTPUT inserted.maNV " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getHoTen());
            ps.setString(2, nv.getDiaChi());
            ps.setString(3, nv.getCccd());
            ps.setString(4, nv.getDienThoai());
            ps.setBoolean(5, nv.getGioiTinh() != null && nv.getGioiTinh());

            java.sql.Date sqlNgaySinh = nv.getNgaySinh() != null ? new java.sql.Date(nv.getNgaySinh().getTime()) : null;
            java.sql.Date sqlNgayVao  = nv.getNgayVaoLam() != null ? new java.sql.Date(nv.getNgayVaoLam().getTime()) : new java.sql.Date(System.currentTimeMillis());

            if (sqlNgaySinh != null) ps.setDate(6, sqlNgaySinh); else ps.setNull(6, java.sql.Types.DATE);
            ps.setDate(7, sqlNgayVao);

            ps.setString(8, nv.getChucVu() != null ? nv.getChucVu() : "Nhân viên");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String maMoi = rs.getString(1);
                    nv.setMaNV(maMoi);
                    return nv; // trả về đối tượng đã có mã
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // insert fail
    }


    // ================== CẬP NHẬT NHÂN VIÊN ==================
    public boolean capNhatNhanVien(NhanVien nv) {
        String sql = "UPDATE NhanVien SET hoTen = ?, diaChi = ?, CCCD = ?, dienThoai = ?, "
                   + "gioiTinh = ?, ngaySinh = ?, ngayVaoLam = ?, chucVu = ? "
                   + "WHERE maNV = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nv.getHoTen());
            ps.setString(2, nv.getDiaChi());
            ps.setString(3, nv.getCccd());
            ps.setString(4, nv.getDienThoai());

            if (nv.getGioiTinh() != null)
                ps.setBoolean(5, nv.getGioiTinh());
            else
                ps.setNull(5, Types.BIT);

            Date sqlNgaySinh = toSqlDate(nv.getNgaySinh());
            if (sqlNgaySinh != null) ps.setDate(6, sqlNgaySinh);
            else ps.setNull(6, Types.DATE);

            Date sqlNgayVao = toSqlDate(nv.getNgayVaoLam());
            if (sqlNgayVao != null) ps.setDate(7, sqlNgayVao);
            else ps.setNull(7, Types.DATE);

            ps.setString(8, nv.getChucVu());
            ps.setString(9, nv.getMaNV());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            // e.printStackTrace();
            return false;
        }
    }

    // ================== XÓA NHÂN VIÊN ==================
    public boolean xoaNhanVien(String maNV) {
        String sql = "DELETE FROM NhanVien WHERE maNV = ?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maNV);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // e.printStackTrace();
            return false;
        }
    }

    // ================== TÌM THEO TÊN ==================
 // 🔹 Tìm theo nhiều tiêu chí (tên, sdt, chức vụ)
    public List<NhanVien> timKiemNhanVien(String ten, String sdt, String chucVu) {
        List<NhanVien> ds = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM NhanVien WHERE 1=1");

        if (ten != null && !ten.isEmpty())
            sql.append(" AND hoTen LIKE ?");
        if (sdt != null && !sdt.isEmpty())
            sql.append(" AND dienThoai LIKE ?");
        if (chucVu != null && !chucVu.isEmpty() && !"Tất cả".equalsIgnoreCase(chucVu))
            sql.append(" AND chucVu = ?");

        try (
             PreparedStatement ps = con.prepareStatement(sql.toString())) {

            int index = 1;
            if (ten != null && !ten.isEmpty())
                ps.setString(index++, "%" + ten + "%");
            if (sdt != null && !sdt.isEmpty())
                ps.setString(index++, "%" + sdt + "%");
            if (chucVu != null && !chucVu.isEmpty() && !"Tất cả".equalsIgnoreCase(chucVu))
                ps.setString(index++, chucVu);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ds.add(new NhanVien(
                    rs.getString("maNV"),
                    rs.getString("hoTen"),
                    rs.getString("diaChi"),
                    rs.getString("CCCD"),
                    rs.getString("dienThoai"),
                    rs.getBoolean("gioiTinh"),
                    rs.getDate("ngaySinh"),
                    rs.getDate("ngayVaoLam"),
                    rs.getString("chucVu")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

}
