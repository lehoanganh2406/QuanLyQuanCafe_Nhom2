package entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Ban {
    private String maBan;     
    private String tenBan;
    private String trangThai;

    public String getMaBan() {
        return maBan;
    }

    public void setMaBan(String maBan) {
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

    public Ban(String maBan, String tenBan, String trangThai) {
        this.maBan = maBan;
        this.tenBan = tenBan;
        this.trangThai = trangThai;
    }

    public Ban(ResultSet rs) throws SQLException {
        this(
            rs.getString("maBan"),       
            rs.getString("tenBan"),
            rs.getString("trangThai")
        );
    }
    

    public Ban(String tenBan, String trangThai) {
		super();
		this.tenBan = tenBan;
		this.trangThai = trangThai;
	}

	@Override
    public String toString() {
        return "Ban [maBan=" + maBan + ", tenBan=" + tenBan + ", trangThai=" + trangThai + "]";
    }
}
