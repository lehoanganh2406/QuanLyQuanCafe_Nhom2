package connectDB;

import java.sql.*;

import javax.swing.JOptionPane;

public class connectDB {
	private static Connection connection = null;

	public static Connection getConnection() {
		if (connection == null) {
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
				String url = "jdbc:sqlserver://CHATLGPT:1433;databaseName=QuanLyQuanCF;encrypt=false";
				String userName = "sa";
				String password = "123456";
				
				connection = DriverManager.getConnection(url, userName, password);
		//		JOptionPane.showMessageDialog(null, "Kết nối CSDL thành công");
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Kết nối CSDL tất bại");
			}
		}
		return connection;
	}
	
	public static void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
				connection = null;
				System.out.println("Đóng kết nối với CSDL");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
