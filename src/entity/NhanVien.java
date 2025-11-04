package entity;

import java.util.Date;

public class NhanVien {
	private String maNV;
    private String hoTen;
    private String sdt;
    private String gioiTinh;
    private int namSinh;
    private Date ngayVaoLam;
	public String getMaNV() {
		return maNV;
	}
	public void setMaNV(String maNV) {
		this.maNV = maNV;
	}
	public String getHoTen() {
		return hoTen;
	}
	public void setHoTen(String hoTen) {
		this.hoTen = hoTen;
	}
	public String getSdt() {
		return sdt;
	}
	public void setSdt(String sdt) {
		this.sdt = sdt;
	}
	public String getGioiTinh() {
		return gioiTinh;
	}
	public void setGioiTinh(String gioiTinh) {
		this.gioiTinh = gioiTinh;
	}
	public int getNamSinh() {
		return namSinh;
	}
	public void setNamSinh(int namSinh) {
		this.namSinh = namSinh;
	}
	public Date getNgayVaoLam() {
		return ngayVaoLam;
	}
	public void setNgayVaoLam(Date ngayVaoLam) {
		this.ngayVaoLam = ngayVaoLam;
	}
	public NhanVien(String maNV, String hoTen, String sdt, String gioiTinh, int namSinh, Date ngayVaoLam) {
		this.maNV = maNV;
		this.hoTen = hoTen;
		this.sdt = sdt;
		this.gioiTinh = gioiTinh;
		this.namSinh = namSinh;
		this.ngayVaoLam = ngayVaoLam;
	}
	public NhanVien(String maNV) {
		this.maNV = maNV;
	}
	public NhanVien() {
		// TODO Auto-generated constructor stub
	}
   
	

	
    
}
