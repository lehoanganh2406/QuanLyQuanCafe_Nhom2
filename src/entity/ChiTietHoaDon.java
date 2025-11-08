package entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChiTietHoaDon {
	private HoaDon hoaDon;    
    private SanPham sanPham;   
    private NhanVien nhanVien; 
    private int soLuong;
	public HoaDon getHoaDon() {
		return hoaDon;
	}
	public void setHoaDon(HoaDon hoaDon) {
		this.hoaDon = hoaDon;
	}
	public SanPham getSanPham() {
		return sanPham;
	}
	public void setSanPham(SanPham sanPham) {
		this.sanPham = sanPham;
	}
	public NhanVien getNhanVien() {
		return nhanVien;
	}
	public void setNhanVien(NhanVien nhanVien) {
		this.nhanVien = nhanVien;
	}
	public int getSoLuong() {
		return soLuong;
	}
	public void setSoLuong(int soLuong) {
		this.soLuong = soLuong;
	}
	public ChiTietHoaDon(HoaDon hoaDon, SanPham sanPham, NhanVien nhanVien, int soLuong) {
		this.hoaDon = hoaDon;
		this.sanPham = sanPham;
		this.nhanVien = nhanVien;
		this.soLuong = soLuong;
	}
	public ChiTietHoaDon(ResultSet rs) throws SQLException {
        this(
            new HoaDon(rs.getString("maHD")),
            new SanPham(rs.getString("maSP")),
            new NhanVien(rs.getString("maNV")),
            rs.getInt("soLuong")
        );
    }
	
	public ChiTietHoaDon(HoaDon hoaDon, SanPham sanPham, int soLuong) {
        this.hoaDon = hoaDon;
        this.sanPham = sanPham;
        this.soLuong = soLuong;
    }
    
	
}
