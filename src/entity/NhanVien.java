package entity;

import java.util.Date;

public class NhanVien {
	private String id;
    private String hoTen;
    private String sdt;
    private String gioiTinh;
    private int namSinh;
    private Date ngayVaoLam;
    
    
    
	public NhanVien() {
		super();
	}

	public NhanVien(String id) {
		super();
		this.id = id;
	}

	public NhanVien(String id, String hoTen, String sdt, String gioiTinh, int namSinh, Date ngayVaoLam) {
		super();
		this.id = id;
		this.hoTen = hoTen;
		this.sdt = sdt;
		this.gioiTinh = gioiTinh;
		this.namSinh = namSinh;
		this.ngayVaoLam = ngayVaoLam;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	@Override
	public String toString() {
		return "NhanVien [hoTen=" + hoTen + "]";
	}

	
    
}
