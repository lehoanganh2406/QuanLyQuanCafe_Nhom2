package entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Order {
	private String maSP;   
    private String tenSP;
    private int soLuong;
    private double donGia; 
    private String loaiSP; 
    private String img;
	
    public double getTotal() {
        return donGia * soLuong;
    }

	public String getMaSP() {
		return maSP;
	}

	public void setMaSP(String maSP) {
		this.maSP = maSP;
	}

	public String getTenSP() {
		return tenSP;
	}

	public void setTenSP(String tenSP) {
		this.tenSP = tenSP;
	}

	public int getSoLuong() {
		return soLuong;
	}

	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}

	public double getDonGia() {
		return donGia;
	}

	public void setDonGia(double donGia) {
		this.donGia = donGia;
	}

	public String getLoaiSP() {
		return loaiSP;
	}

	public void setLoaiSP(String loaiSP) {
		this.loaiSP = loaiSP;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Order(String maSP, String tenSP, int soLuong, double donGia, String loaiSP, String img) {
		this.maSP = maSP;
		this.tenSP = tenSP;
		this.soLuong = soLuong;
		this.donGia = donGia;
		this.loaiSP = loaiSP;
		this.img = img;
	}

	public Order(String maSP) {
		this.maSP = maSP;
	}
    
	 public Order(ResultSet rs) throws SQLException {
	        this( rs.getString("maSP"),     
	            rs.getString("tenSP"),
	            rs.getInt("soLuong"),
	            rs.getDouble("donGia"),
	            rs.getString("loaiSP"),    
	            rs.getString("img")
	        );
	    }
}
