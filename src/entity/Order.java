package entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Order {
	private int maSP;
	private String tenSP;
	private int soLuong;
	private double donGia;
	private double total;
	private String img;
	public int getMaSP() {
		return maSP;
	}
	public void setMaSP(int maSP) {
		this.maSP = maSP;
	}
	public String getTenSP() {
		return tenSP;
	}
	public void setTenSP(String tenSP) {
		this.tenSP = tenSP;
	}
	public double getDonGia() {
		return donGia;
	}
	public void setDonGia(double donGia) {
		this.donGia = donGia;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public Order(int maSP) {
		this.maSP = maSP;
	}
	public int getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
	
	public Order(int maSP, String tenSP, int soLuong, double donGia, double total, String img) {
		this.maSP = maSP;
		this.tenSP = tenSP;
		this.soLuong = soLuong;
		this.donGia = donGia;
		this.total = total;
		this.img = img;
	}
	public Order(ResultSet rs) throws SQLException {
        this(rs.getInt("maSP"),rs.getString("tenSP"), rs.getInt("soLuong"), rs.getDouble("donGia"), rs.getDouble("total"), rs.getString("img"));
    }
	
	
	
}
