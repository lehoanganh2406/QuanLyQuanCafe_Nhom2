package entity;

import java.util.Date;

public class HoaDon {
	private String maHD;
	private Ban maban;
	private KhachHang maKH;
//	private nhanvien manv;
	private Date tgThanhtoan;
	private Date tgTao;
	private boolean trangThai;
	private double giamGia;
	private double tongtien;
	public String getMaHD() {
		return maHD;
	}
	public void setMaHD(String maHD) {
		this.maHD = maHD;
	}
	public Ban getMaban() {
		return maban;
	}
	public void setMaban(Ban maban) {
		this.maban = maban;
	}
	public KhachHang getMaKH() {
		return maKH;
	}
	public void setMaKH(KhachHang maKH) {
		this.maKH = maKH;
	}
	public Date getTgThanhtoan() {
		return tgThanhtoan;
	}
	public void setTgThanhtoan(Date tgThanhtoan) {
		this.tgThanhtoan = tgThanhtoan;
	}
	public Date getTgTao() {
		return tgTao;
	}
	public void setTgTao(Date tgTao) {
		this.tgTao = tgTao;
	}
	public boolean isTrangThai() {
		return trangThai;
	}
	public void setTrangThai(boolean trangThai) {
		this.trangThai = trangThai;
	}
	public double getGiamGia() {
		return giamGia;
	}
	public void setGiamGia(double giamGia) {
		this.giamGia = giamGia;
	}
	public double getTongtien() {
		return tongtien;
	}
	public void setTongtien(double tongtien) {
		this.tongtien = tongtien;
	}
	public HoaDon(String maHD, Ban maban, KhachHang maKH, Date tgThanhtoan, Date tgTao, boolean trangThai,
			double giamGia, double tongtien) {
		this.maHD = maHD;
		this.maban = maban;
		this.maKH = maKH;
		this.tgThanhtoan = tgThanhtoan;
		this.tgTao = tgTao;
		this.trangThai = trangThai;
		this.giamGia = giamGia;
		this.tongtien = tongtien;
	}
	public HoaDon() {
		super();
	}
	@Override
	public String toString() {
		return "HoaDon [maHD=" + maHD + ", maban=" + maban + ", maKH=" + maKH + ", tgThanhtoan=" + tgThanhtoan
				+ ", tgTao=" + tgTao + ", trangThai=" + trangThai + ", giamGia=" + giamGia + ", tongtien=" + tongtien
				+ "]";
	}
	
	
	

}
