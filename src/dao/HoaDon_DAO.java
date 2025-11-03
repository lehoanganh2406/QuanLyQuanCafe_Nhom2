package dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
                dsHD.add(new HoaDon()); 
            }
            System.out.println("✅ Lấy danh sách hóa đơn thành công.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHD;
    }


}
