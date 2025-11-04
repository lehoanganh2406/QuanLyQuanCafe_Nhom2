package entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Ban {
    private String maBan;     
    private String tenBan;

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


    public Ban(String maBan, String tenBan) {
        this.maBan = maBan;
        this.tenBan = tenBan;
        
    }

    public Ban(ResultSet rs) throws SQLException {
        this(
            rs.getString("maBan"),       
            rs.getString("tenBan")
        );
    }
    public Ban() {
		// TODO Auto-generated constructor stub
	}
    

	public Ban(String maBan) {
		super();
		this.maBan = maBan;
	}

	@Override
	public String toString() {
		return "Ban [maBan=" + maBan + ", tenBan=" + tenBan + "]";
	}
    
	
}
