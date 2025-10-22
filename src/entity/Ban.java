package entity;

import java.sql.ResultSet;
import java.sql.SQLException;


public class Ban {
	private int maBan;
	private String tenBan;
	private String trangThai;
	
	public int getMaBan() {
		return maBan;
	}

	public void setMaBan(int maBan) {
		this.maBan = maBan;
	}

	public String getTenBan() {
		return tenBan;
	}

	public void setTenBan(String tenBan) {
		this.tenBan = tenBan;
	}

	public String getTrangThai() {
		return trangThai;
	}

	public void setTrangThai(String trangThai) {
		this.trangThai = trangThai;
	}

	public Ban(int maBan, String tenBan, String trangThai) {
		this.maBan = maBan;
		this.tenBan = tenBan;
		this.trangThai = trangThai;
	}

	public Ban(ResultSet rs) throws SQLException {
        this(rs.getInt("maBan"), rs.getString("tenBan"), rs.getString("trangThai"));
    }
	
}
