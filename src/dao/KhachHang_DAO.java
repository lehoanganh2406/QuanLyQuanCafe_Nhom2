package dao;

import connectDB.ConnectDB; 
import entity.KhachHang;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
//import java.util.*;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;

public class KhachHang_DAO {
    
    private Connection con; 
    public boolean themKhachHang(KhachHang kh) {
        String sql = "INSERT INTO KhachHang (maKH, tenKH, soDienThoai, diemTichLuy) VALUES (?, ?, ?, ?)";        
        con = null; 
        try {
            con = ConnectDB.getConnection(); // Lấy kết nối
            PreparedStatement ps = con.prepareStatement(sql);
            
            // Gán giá trị cho các tham số
            ps.setString(1, kh.getMaKH()); // Giả sử bạn đã có mã khách hàng
            ps.setString(2, kh.getTenKH());
            ps.setString(3, kh.getSdt());
            
//            int diemTL= tinhDiemTL(kh.get)
            ps.setInt(4, kh.getDiemTL()); // Điểm tích lũy được nhập vào

            int rowsAffected = ps.executeUpdate();
            
            // Thông báo thêm khách hàng thành công
            JOptionPane.showMessageDialog(null, "Thêm khách hàng thành công: " + kh.getTenKH());
            ps.close();

            return rowsAffected > 0;

        } catch (SQLException e) {
            // Hiển thị thông báo lỗi
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm khách hàng: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return false;
        } finally {
            ConnectDB.closeConnection(con);
        }
    }
    
    private int tinhDiemTL(double amountSpent) {
        return (int) (amountSpent / 1000); // 1.000 VND = 1 điểm
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