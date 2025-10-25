package dao;
import java.sql.*;
import java.util.*;
import connectDB.ConnectDB;
import entity.Ban;

public class Ban_DAO {
	 private static Ban_DAO instance;
	 public static Ban_DAO getInstance() {
	        if (instance == null)
	            instance = new Ban_DAO();
	        return instance;
	    }  

	public ArrayList<Ban> getAll() {
		String query = "SELECT * FROM dbo.Ban";
		ArrayList<Ban> dsList = new ArrayList<Ban>();       
		Connection con = ConnectDB.getInstance().getConnection();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
			 while (rs.next()) {
	                int maBan = rs.getInt("maBan");
	                String tenBan = rs.getString("tenBan");
	                String trangThai = rs.getString("trangThai");
	                dsList.add(new Ban(rs));
	            }
			 System.out.println("Lấy danh sách bàn thành công\n");
			 rs.close();
			 stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		return dsList;
		
	}
	
	public Ban getBanByMaBan(int maBan) {
		String query = "SELECT * FROM dbo.Ban WHERE maBan = ?";
		Ban b = null;
		try (Connection c = ConnectDB.getConnection()) {
			PreparedStatement pst = c.prepareStatement(query);
			pst.setInt(1, maBan);
			ResultSet rs = pst.executeQuery();

			if (rs.next()) {
				b = new Ban(rs.getInt("maBan"), rs.getString("tenBan"), rs.getString("trangThai"));
			}
			rs.close();
			pst.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return b;
    }
	
	public boolean themBan(Ban b) {
        String sql = "INSERT INTO dbo.Ban (tenBan, trangThai) VALUES (?, ?)";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, b.getTenBan());
            pst.setString(2, b.getTrangThai());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	
	public boolean capNhatBan(Ban b) {
        final String sql = "UPDATE dbo.Ban SET tenBan = ?, trangThai = ? WHERE maBan = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, b.getTenBan());
            pst.setString(2, b.getTrangThai());
            pst.setInt(3, b.getMaBan());

            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

	public boolean xoaBan(int maBan) {
        final String sql = "DELETE FROM dbo.Ban WHERE maBan = ?";

        try (Connection con = ConnectDB.getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, maBan);
            return pst.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

		
	}
	
	

