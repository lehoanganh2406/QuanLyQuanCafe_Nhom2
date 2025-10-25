package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import connectDB.ConnectDB;
import entity.Order;

public class Order_DAO {
	private static Order_DAO instance;
	public static Order_DAO getInstance() {
	        if (instance == null)
	            instance = new Order_DAO();
	        return instance;
	    }  

	public ArrayList<Order> getSanPhamByMaSP(int maSP) {
	    String sql =
	        "SELECT sp.maSP, sp.tenSP, sp.soLuong, sp.donGia, sp.img, " +
	        "       ls.loaiSP AS tenLoai " +
	        "FROM dbo.SanPham sp " +
	        "JOIN dbo.LoaiSanPham ls ON ls.maLoai = sp.maLoai " +
	        "WHERE sp.maSP = ?";

	    ArrayList<Order> dsList = new ArrayList<>();
	    Connection con = ConnectDB.getInstance().getConnection();
	    try (
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, maSP);
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                dsList.add(new Order(
	                    rs.getInt("maSP"),
	                    rs.getString("tenSP"),
	                    rs.getInt("soLuong"),
	                    rs.getDouble("donGia"),
	                    rs.getString("tenLoai"),
	                    rs.getString("img")
	                ));
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return dsList;
	}
	 
	public ArrayList<Order> getSanPhamByLoai(int maLoai) {
	    String sql =
	        "SELECT sp.maSP, sp.tenSP, sp.soLuong, sp.donGia, sp.img, " +
	        "       ls.loaiSP AS tenLoai " +
	        "FROM dbo.SanPham sp " +
	        "JOIN dbo.LoaiSanPham ls ON ls.maLoai = sp.maLoai " +
	        "WHERE sp.maLoai = ? " +
	        "ORDER BY sp.tenSP";

	    ArrayList<Order> ds = new ArrayList<>();
	    Connection con = ConnectDB.getInstance().getConnection();
	    try (
	         PreparedStatement ps = con.prepareStatement(sql)) {

	        ps.setInt(1, maLoai);

	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                ds.add(new Order(
	                    rs.getInt("maSP"),
	                    rs.getString("tenSP"),
	                    rs.getInt("soLuong"),
	                    rs.getDouble("donGia"),
	                    rs.getString("tenLoai"),
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