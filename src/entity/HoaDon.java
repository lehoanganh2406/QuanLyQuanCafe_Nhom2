package entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class HoaDon {
	private String maHD;
	private Ban maBan;
	private KhachHang maKH;
	private NhanVien maNV;
	private Timestamp thoiGianVao;
	private Timestamp thoiGianRa;
	private boolean trangThai;
	private int diemTL;
	private double giamGia;
	private double tongTien;
	
	private double tienKhachTra;

	public double getTienKhachTra() {
	    return tienKhachTra;
	}

	public void setTienKhachTra(double tienKhachTra) {
	    this.tienKhachTra = tienKhachTra;
	}
	
	
	public int getDiemTL() {
		return diemTL;
	}

	public void setDiemTL(int diemTL) {
		this.diemTL = diemTL;
	}

	public String getMaHD() {
		return maHD;
	}
	public void setMaHD(String maHD) {
		this.maHD = maHD;
	}
	public Ban getMaBan() {
		return maBan;
	}
	public void setMaBan(Ban maBan) {
		this.maBan = maBan;
	}
	public KhachHang getMaKH() {
		return maKH;
	}
	public void setMaKH(KhachHang maKH) {
		this.maKH = maKH;
	}
	public Timestamp getThoiGianVao() {
		return thoiGianVao;
	}
	public void setThoiGianVao(Timestamp thoiGianVao) {
		this.thoiGianVao = thoiGianVao;
	}
	public Timestamp getThoiGianRa() {
		return thoiGianRa;
	}
	public void setThoiGianRa(Timestamp thoiGianRa) {
		this.thoiGianRa = thoiGianRa;
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
	public double getTongTien() {
		return tongTien;
	}
	public void setTongTien(double tongTien) {
		this.tongTien = tongTien;
	}
	public NhanVien getMaNV() {
		return maNV;
	}
	public void setMaNV(NhanVien maNV) {
		this.maNV = maNV;
	}
	public HoaDon() {
		// TODO Auto-generated constructor stub
	}
	
	public HoaDon(String maHD) {
		super();
		this.maHD = maHD;
	}
	public HoaDon(ResultSet rs) throws SQLException {
        this.maHD = rs.getString("maHD");
        this.maBan = new Ban(rs.getString("maBan")); 
        this.maKH = new KhachHang(rs.getString("maKH"));
        this.maNV = new NhanVien(rs.getString("maNV"));
        this.thoiGianVao = rs.getTimestamp("thoiGianVao");
        this.thoiGianRa = rs.getTimestamp("thoiGianRa");
        this.trangThai = rs.getInt("trangThai") == 1;
        this.diemTL = rs.getInt("diemTL");
        this.giamGia = rs.getInt("giamGia");
        this.tongTien = rs.getDouble("tongTien");
        this.tienKhachTra = rs.getDouble("tienKhachTra");
    }

	public HoaDon(String maHD, Ban maBan, KhachHang maKH, NhanVien maNV, Timestamp thoiGianVao, Timestamp thoiGianRa,
			boolean trangThai, int diemTL, double giamGia, double tongTien, double tienKhachTra) {
		this.maHD = maHD;
		this.maBan = maBan;
		this.maKH = maKH;
		this.maNV = maNV;
		this.thoiGianVao = thoiGianVao;
		this.thoiGianRa = thoiGianRa;
		this.trangThai = trangThai;
		this.diemTL = diemTL;
		this.giamGia = giamGia;
		this.tongTien = tongTien;
		this.tienKhachTra = tienKhachTra;
	}

	


	
	
	
	
	
	
	

}