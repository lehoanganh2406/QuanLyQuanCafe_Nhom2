package entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SanPham {
	private String maSP;   
    private String tenSP;
    private int soLuong;
    private double donGia; 
    private String img;
    private LoaiSanPham loaiSP; 
    private String moTa;
	
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

	public LoaiSanPham getLoaiSP() {
		return loaiSP;
	}

	public void setLoaiSP(LoaiSanPham loaiSP) {
		this.loaiSP = loaiSP;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public SanPham(String maSP) {
		this.maSP = maSP;
	}
	
    
	 public String getMoTa() {
		return moTa;
	}

	public void setMoTa(String moTa) {
		this.moTa = moTa;
	}

	public SanPham(String maSP, String tenSP, int soLuong, double donGia, LoaiSanPham loaiSP, String img) {
		this.maSP = maSP;
		this.tenSP = tenSP;
		this.soLuong = soLuong;
		this.donGia = donGia;
		this.loaiSP = loaiSP;
		this.img = img;
	}
	
	

	 public SanPham(String maSP, String tenSP, int soLuong, double donGia, String img, LoaiSanPham loaiSP, String moTa) {
		this.maSP = maSP;
		this.tenSP = tenSP;
		this.soLuong = soLuong;
		this.donGia = donGia;
		this.img = img;
		this.loaiSP = loaiSP;
		this.moTa = moTa;
	}

	public SanPham(ResultSet rs) throws SQLException {
		    this.maSP = rs.getString("maSP");
		    this.tenSP = rs.getString("tenSP");
		    this.soLuong = rs.getInt("soLuong");
		    this.donGia = rs.getDouble("donGia");
		    this.img = rs.getString("img");
		    this.loaiSP = new LoaiSanPham(
		    		rs.getString("maLoai"),
		    		rs.getString("loaiSP"));
		    this.moTa = rs.getString("moTa");
		}
}
