package dao;
import java.sql.*;
import java.util.*;
import connectDB.ConnectDB;
import entity.Ban;

public class Ban_DAO {
	
	private Connection con;

	public ArrayList<Ban> dsBan() {
		String query = "SELECT * FROM dbo.Ban";
		ArrayList<Ban> ds = new ArrayList<Ban>();        
        try {
            con = ConnectDB.getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
			 while (rs.next()) {
	                int maBan = rs.getInt("maBan");
	                String tenBan = rs.getString("tenBan");
	                String trangThai = rs.getString("trangThai");
	                ds.add(new Ban(maBan, tenBan, trangThai));
	            }
			 System.out.println("Lấy danh sách bàn thành công\n");
			 rs.close();
			 stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			ConnectDB.closeConnection(con);
		}
        
		return ds;
		
	}
	
	public Ban xemBanTheoMaBan(int maBan) {
		String query = "select * FROM dbo.Ban WHERE maBan = ?";
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
        final String sql = "INSERT INTO dbo.Ban (tenBan, trangThai) VALUES (?, ?)";

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
	
	

