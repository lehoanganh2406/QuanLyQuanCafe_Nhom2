package dao;

import connectDB.ConnectDB; 
import entity.KhachHang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
//import java.util.*;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;

public class KhachHang_DAO {
    
    private Connection con; 
    public boolean themKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (tenKH, soDienThoai, diemTichLuy) VALUES (?, ?, ?)";        
        con = null; 
        try {
            con = ConnectDB.getConnection(); // Lấy kết nối
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setString(1, kh.getTenKH());
            ps.setString(2, kh.getSdt());
            ps.setInt(3, kh.getDiemTL()); 

            int rowsAffected = ps.executeUpdate();
            System.out.println("Thêm khách hàng thành công: " + kh.getTenKH() + "\n");
            ps.close();

            
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            // Đảm bảo kết nối luôn được đóng
            ConnectDB.closeConnection(con);
        }
    }
    
    public List<KhachHang> layTatCaKhachHang() {
        List<KhachHang> ds = new ArrayList<KhachHang>();
        String sql = "SELECT maKH, tenKH, soDienThoai, diemTichLuy FROM KhachHang";
        con = null;
        try {
            con = ConnectDB.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                KhachHang kh = new KhachHang(
                    rs.getString("maKH"),
                    rs.getString("tenKH"),
                    rs.getString("soDienThoai"),
                    rs.getInt("diemTichLuy")
                );
                ds.add(kh);
            }
            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection(con);
        }
        return ds;
    }
    public KhachHang timKhachHangTheoSDT(String sdt) {
        KhachHang kh = null;
        String sql = "SELECT maKH, tenKH, soDienThoai, diemTichLuy FROM KhachHang WHERE soDienThoai = ?";
        con = null;
        try {
            con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            
            ps.setString(1, sdt);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                kh = new KhachHang(
                    rs.getString("maKH"),
                    rs.getString("tenKH"),
                    rs.getString("soDienThoai"),
                    rs.getInt("diemTichLuy")
                );
            }
            rs.close();
            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConnectDB.closeConnection(con);
        }
        return kh;
        
    }
    
    public boolean capNhatDiemTichLuy(int maKH, int diemMoi) {
        String sql = "UPDATE KhachHang SET diemTichLuy = ? WHERE maKH = ?";
        con = null;
        try {
            con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, diemMoi); // Tổng điểm mới
            ps.setInt(2, maKH);    // Mã KH cần cập nhật
            
            int rowsAffected = ps.executeUpdate();
            
            ps.close();
            
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            ConnectDB.closeConnection(con);
        }
    }
    
}