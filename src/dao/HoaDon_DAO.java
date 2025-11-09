package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import connectDB.ConnectDB;
import entity.Ban;
import entity.HoaDon;
import entity.KhachHang;
import entity.LoaiSanPham;
import entity.NhanVien;
import entity.SanPham;

public class HoaDon_DAO {
	
	private static HoaDon_DAO instance;

    public static HoaDon_DAO getInstance() {
        if (instance == null) instance = new HoaDon_DAO();
        return instance;
    }
    
    public List<HoaDon> getAllHoaDon() {
        String sql = "SELECT maHD, maBan, maKH, maNV, thoiGianVao, thoiGianRa, trangThai, diemTL ,giamGia, tongTien,tienKhachTra FROM HoaDon";
        List<HoaDon> dsHD = new ArrayList<>();
        Connection con = ConnectDB.getConnection();

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                dsHD.add(new HoaDon(rs)); 
            }
            System.out.println("Lấy danh sách hóa đơn thành công.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dsHD;
    }
    
    public HoaDon getHoaDonTheoMa(String maHD) {
        HoaDon hd = null;
        Connection con = ConnectDB.getInstance().getConnection();
        String sql = "SELECT * FROM HoaDon WHERE maHD = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                hd = new HoaDon();
                hd.setMaHD(rs.getString("maHD"));
                hd.setMaBan(new Ban(rs.getString("maBan")));
                hd.setMaNV(new NhanVien(rs.getString("maNV")));
                hd.setThoiGianVao(rs.getTimestamp("thoiGianVao"));
                hd.setThoiGianRa(rs.getTimestamp("thoiGianRa"));
                hd.setTrangThai(rs.getInt("trangThai") == 1);
                hd.setDiemTL(rs.getInt("diemTL"));
                hd.setGiamGia(rs.getDouble("giamGia"));
                hd.setTongTien(rs.getDouble("tongTien"));

                // 🧠 Lấy khách hàng chi tiết bằng DAO phụ
                String maKH = rs.getString("maKH");
                if (maKH != null) {
                    KhachHang_DAO khDao = new KhachHang_DAO();
                    KhachHang kh = khDao.getByMa(maKH);
                    hd.setMaKH(kh);
                }

                // Nếu có thêm tiền khách trả
                try {
                    hd.setTienKhachTra(rs.getDouble("tienKhachTra"));
                } catch (Exception ignore) {}
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hd;
    }

    
   

    
    public String layMaHoaDon() {
        String maHD = "HD???";
        try {
            Connection con = ConnectDB.getConnection();
            PreparedStatement ps = con.prepareStatement(
                "SELECT CONCAT('HD', RIGHT('000' + CAST(NEXT VALUE FOR seq_HoaDon AS VARCHAR(3)), 3))"
            );
            ResultSet rs = ps.executeQuery();
            if (rs.next()) maHD = rs.getString(1);
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return maHD;
    }
    
    public boolean themHoaDon(HoaDon hd) {
        int n = 0;
        try {
            Connection con = ConnectDB.getConnection();
            String sql = "INSERT INTO HoaDon(" +
                         " maHD, maBan, maKH, maNV, thoiGianVao, thoiGianRa, trangThai, diemTL ,giamGia, tongTien, tienKhachTra) " +
                         "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, hd.getMaHD());
            ps.setString(2, hd.getMaBan().getMaBan());

            if (hd.getMaKH() == null) {
                ps.setNull(3, Types.NVARCHAR);
            } else {
                ps.setString(3, hd.getMaKH().getMaKH());
            }

            ps.setString(4, hd.getMaNV().getMaNV());
            ps.setTimestamp(5, hd.getThoiGianVao());
            ps.setTimestamp(6, hd.getThoiGianRa());
            ps.setInt(7, hd.isTrangThai() ? 1 : 0);
            ps.setInt(8, hd.getDiemTL());
            ps.setDouble(9, hd.getGiamGia());
            ps.setDouble(10, hd.getTongTien());
            ps.setDouble(11, hd.getTienKhachTra());

            n = ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n > 0;
    }
    public boolean capNhatTienKhachTra(String maHD, double tienKhachTra) {
        String sql = "UPDATE HoaDon SET tienKhachTra = ? WHERE maHD = ?";
        Connection con = ConnectDB.getInstance().getConnection();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, tienKhachTra);
            ps.setString(2, maHD);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    
    public boolean xoaHoaDon(String maHD) {
		String sql= "delete FROM HoaDon WHERE maHD = ?";
		Connection con= ConnectDB.getInstance().getConnection();
		try (PreparedStatement st= con.prepareStatement(sql)) {
			st.setString(1, maHD);
			int aff= st.executeUpdate();
			return aff>0;      //xoa thanh cong
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;     //xoa that bai
		
	}
    
    public List<SanPham> getSPtheomaHD(String maHD) {
		List<SanPham> list= new ArrayList<SanPham>();
		String sql = """
		        SELECT sp.maSP, sp.tenSP, cthd.soLuong, sp.donGia, (cthd.soLuong * sp.donGia) AS thanhTien
		        FROM ChiTietHoaDon cthd
		        JOIN SanPham sp ON cthd.maSP = sp.maSP
		        WHERE cthd.maHD = ?
		    """;
		Connection con= ConnectDB.getInstance().getConnection();
		try (PreparedStatement st= con.prepareStatement(sql)){
			st.setString(1, maHD);
			try (ResultSet rs= st.executeQuery()){
				while(rs.next()) {
					SanPham sp = new SanPham(
		                    rs.getString("maSP"),
		                    rs.getString("tenSP"),
		                    rs.getInt("soLuong"),
		                    rs.getDouble("donGia"),
		                    rs.getString("img"),
		                    new LoaiSanPham(rs.getString("loaiSP")),
		                    rs.getString("moTa")
		                );
					list.add(sp);
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
    
    public List<HoaDon> getHDtheoNgay(java.util.Date tuNgay, java.util.Date denNgay) {
		List<HoaDon> dsHD= new ArrayList<HoaDon>();
		String sql= "select * FROM HoaDon WHERE CAST (thoiGianVao AS DATE) between ? AND ? ORDER BY thoiGianVao ASC";
		
		Connection con= ConnectDB.getInstance().getConnection();
		try (PreparedStatement st= con.prepareStatement(sql)){
			st.setDate(1, new java.sql.Date(tuNgay.getTime()));
	        st.setDate(2, new java.sql.Date(denNgay.getTime()));
			
			try(ResultSet rs= st.executeQuery()) {
				while(rs.next()) {
					HoaDon h= new HoaDon();
					h.setMaHD(rs.getString("maHD"));
					h.setThoiGianVao(rs.getTimestamp("thoiGianVao"));
					h.setThoiGianRa(rs.getTimestamp("thoiGianRa"));
					h.setGiamGia(rs.getDouble("giamGia"));
					h.setTongTien(rs.getDouble("tongTien"));
					h.setTrangThai(rs.getBoolean("trangThai"));
					h.setMaNV(new NhanVien(rs.getString("maNV")));
					
					if (rs.getString("maKH")!=null) {
						h.setMaKH(new KhachHang(rs.getString("maKH")));
					}
					
					dsHD.add(h);
					
				}
			} 
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dsHD;
		
		
		
	}

}