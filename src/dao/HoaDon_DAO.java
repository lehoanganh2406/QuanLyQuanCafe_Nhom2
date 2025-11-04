package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import connectDB.ConnectDB;
import entity.HoaDon;

public class HoaDon_DAO {
	
	private static HoaDon_DAO instance;

    public static HoaDon_DAO getInstance() {
        if (instance == null) instance = new HoaDon_DAO();
        return instance;
    }
    
//    CHƯA CÓ LỚP NHÂN VIÊN 
//    CHƯA CÓ LỚP NHÂN VIÊN
    
    
    
    public List<HoaDon> getAllHoaDon() {
        String sql = "SELECT maHD, maBan, maKH, thoiGianVao, thoiGianRa, trangThai, giamGia, tongTien FROM HoaDon";
        List<HoaDon> dsHD = new ArrayList<>();
        Connection con = ConnectDB.getInstance().getConnection();

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dsHD.add(new HoaDon(rs)); 
            }
            System.out.println("Lấy danh sách hóa đơn thành công.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHD;
    }
    
    public String layMaHoaDon() {
        String maHD = "HD???";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT CONCAT('HD', RIGHT('000' + CAST(NEXT VALUE FOR seq_HoaDon AS VARCHAR(3)), 3))"
            );
            ResultSet rs = ps.executeQuery();
            if (rs.next()) maHD = rs.getString(1);
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maHD;
    }
    
    public boolean themHoaDon(HoaDon hd) {
        int n = 0;
        try {
            Connection con = ConnectDB.getConnection();
            String sql = "INSERT INTO HoaDon(" +
                         " maHD, maBan, maKH, maNV, thoiGianVao, thoiGianRa, trangThai, giamGia, tongTien) " +
                         "VALUES (?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, hd.getMaHD());
            ps.setString(2, hd.getMaBan().getMaBan());

            if (hd.getMaKH() == null) {
                ps.setNull(3, Types.NVARCHAR);
            } else {
                ps.setString(3, hd.getMaKH().getMaKH());
            }

            ps.setString(4, hd.getMaNV().getMaNV());
            ps.setTimestamp(5, hd.getThoiGianVao());
            ps.setTimestamp(6, hd.getThoiGianRa());
            ps.setInt(7, hd.isTrangThai() ? 1 : 0);
            ps.setInt(8, (int) hd.getGiamGia());
            ps.setDouble(9, hd.getTongTien());

            n = ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

}
