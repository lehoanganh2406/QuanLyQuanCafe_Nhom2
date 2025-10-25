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

	 public ArrayList<Order> getSanPhamByMASP(int maSP) {
		    String sql = "SELECT maSP, tenSP, soLuong, donGia, total, img FROM dbo.SanPham WHERE maSP = ?";
		    ArrayList<Order> dsList = new ArrayList<>();

		    try (Connection con = ConnectDB.getConnection();
		         PreparedStatement ps = con.prepareStatement(sql)) {

		        ps.setInt(1, maSP);

		        try (ResultSet rs = ps.executeQuery()) {
		            while (rs.next()) {
		                Order o = new Order(
		                    rs.getInt("maSP"),
		                    rs.getString("tenSP"),
		                    rs.getInt("soLuong"),
		                    rs.getDouble("donGia"),
		                    rs.getDouble("total"),
		                    rs.getString("img")
		                );
		                dsList.add(o);
		            }
		        }
		    } catch (SQLException e) {
		        e.printStackTrace();
		    }

		    return dsList;
		}
}