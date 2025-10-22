package connectDB;

import java.sql.*;

public class ConnectDB {
	private static Connection con = null;
	
	public void connect()  {
        String severName = "localhost";
        String databaseName = "QuanLyQuanCafe";
        String username = "sa";
        String password = "Qazwsxedc@12345";
        String url = "jdbc:sqlserver://localhost:1433;databaseName=QuanLyQuanCF;encrypt=true;trustServerCertificate=true;loginTimeout=5";
        try {
			con = DriverManager.getConnection(url, username, password);
			System.out.println("Kết nối database thành công.");
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Kết nối thất bại!");
		}
        
    }
    
	public static void closeConnection(Connection c) {
		try {
			if (c != null && !c.isClosed()) {
				c.close();
				System.out.println("Đã đóng kết nối database.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


    public static Connection getConnection() {
        return con;
    }

	
}
