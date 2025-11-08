package entity;

import java.sql.ResultSet;
import java.sql.SQLException;

public class KhachHang {
    private String maKH;
    private String tenKH;
    private String sdt;
    private int diemTL;

    public String getMaKH() {
        return maKH;
    }
    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    public String getTenKH() {
        return tenKH;
    }
    public void setTenKH(String tenKH) {
        this.tenKH = tenKH;
    }

    public String getSdt() {
        return sdt;
    }
    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public int getDiemTL() {
        return diemTL;
    }
    public void setDiemTL(int diemTL) {
        this.diemTL = diemTL;
    }

    // ===== CONSTRUCTOR =====
    public KhachHang(String maKH, String tenKH, String sdt, int diemTL) {
        this.maKH = maKH;
        this.tenKH = tenKH;
        this.sdt = sdt;
        this.diemTL = diemTL;
    }

    // Constructor khi thêm KH (auto maKH)
    public KhachHang(String tenKH, String sdt, int diemTL) {
        this.tenKH = tenKH;
        this.sdt = sdt;
        this.diemTL = diemTL;
    }

    
    

    public KhachHang(String maKH, String tenKH) {
		super();
		this.maKH = maKH;
		this.tenKH = tenKH;
	}
	public KhachHang(String maKH) {
		this.maKH = maKH;
	}
	public KhachHang(ResultSet rs) throws SQLException {
        this(
            rs.getString("maKH"),
            rs.getString("tenKH"),
            rs.getString("sdt"),
            rs.getInt("diemTL")
        );
    }
	

}
