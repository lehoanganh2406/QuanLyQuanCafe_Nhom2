package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;

import connectDB.ConnectDB;
import dao.NhanVien_DAO;
import entity.NhanVien;

public class DSNhanVien_GUI extends JFrame implements ActionListener, MouseListener {
    private JLabel lblMaNV,lblTenNV, lblSDT, lblGioiTinh, lblNgaySinh, lblNgayVao, lblDiaChi, lblcccd, lblChucVu;
    private JTextField txtMaNV, txtTenNV, txtSDT, txtDiaChi, txtCCCD;
    private JCheckBox checkGioiTinh;
    private JDateChooser ngaySinh, ngayVao;
    private JButton btnTim, btnThem, btnXoa, btnSua, btnLamMoi;
	private DefaultTableModel modelNhanVien;
	private JTable tableNhanVien;
	private JComboBox<String> comboChucVu;
	private NhanVien_DAO nv_DAO;
	private JLabel lbMess;
	private String tenHienThi;
	private int loaiTaiKhoan;
	private String maNV;

    public DSNhanVien_GUI(String tenHienThi, int loaiTaiKhoan, String maNV) {
    	try {
            ConnectDB.getInstance().connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    	this.tenHienThi = tenHienThi;
        this.loaiTaiKhoan = loaiTaiKhoan;
        this.maNV = maNV;
    	nv_DAO = new NhanVien_DAO();
        setTitle("Danh sách nhân viên ");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout()); 
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                // Quay lại màn hình chính, giữ đúng tài khoản + tên hiển thị
                new ManHinhChinh_GUI(tenHienThi, loaiTaiKhoan, maNV).setVisible(true);
                dispose();
            }
        });


        thanhTieuDe();

        JPanel pMain = new JPanel(new BorderLayout());
        JPanel pNor = new JPanel(new BorderLayout());

        // ====== THÔNG TIN NV (TRÁI) ======
        Box b, b1, b2, b3, b4,b5;
        b = Box.createVerticalBox();
        b.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(165,42,42),2),
                "Thông tin nhân viên", 0, 0,
                new Font("Times New Roman", Font.BOLD, 24)));

        // Hàng 1
        b.add(Box.createVerticalStrut(20));
        b1 = Box.createHorizontalBox();
        b1.add(Box.createHorizontalStrut(20));
        b1.add(lblMaNV = new JLabel("Mã nhân viên:"));
        b1.add(Box.createHorizontalStrut(20));
        b1.add(txtMaNV = new JTextField(20));
        txtMaNV.setEditable(false);
        b1.add(Box.createHorizontalStrut(95));
        b1.add(lblTenNV = new JLabel("Tên nhân viên:"));
        b1.add(Box.createHorizontalStrut(20));
        b1.add(txtTenNV = new JTextField(20));
        b1.add(Box.createHorizontalStrut(150));
        b.add(b1);

        // Hàng 2
        b.add(Box.createVerticalStrut(20));
        b2 = Box.createHorizontalBox();
        b2.add(Box.createHorizontalStrut(20));
        b2.add(lblSDT = new JLabel("Số điện thoại:"));
        b2.add(Box.createHorizontalStrut(27));
        b2.add(txtSDT = new JTextField(20));
        b2.add(Box.createHorizontalStrut(90));
        b2.add(lblGioiTinh = new JLabel("Giới tính:"));
        b2.add(Box.createHorizontalStrut(70));
        b2.add(checkGioiTinh = new JCheckBox("Nam"));
        b2.add(Box.createHorizontalStrut(675));
        b.add(b2);
        
     // Hàng 3
        b.add(Box.createVerticalStrut(20));
        b3 = Box.createHorizontalBox();
        b3.add(Box.createHorizontalStrut(20));
        b3.add(lblDiaChi = new JLabel("Địa chỉ:"));
        b3.add(Box.createHorizontalStrut(80));
        b3.add(txtDiaChi = new JTextField(20));
        b3.add(Box.createHorizontalStrut(100));
        b3.add(lblcccd = new JLabel("CCCD:"));
        b3.add(Box.createHorizontalStrut(85));
        b3.add(txtCCCD = new JTextField(20));
        b3.add(Box.createHorizontalStrut(150));
        b.add(b3);
        
        // Hàng 4
        b.add(Box.createVerticalStrut(20));
        b4 = Box.createHorizontalBox();
        b4.add(Box.createHorizontalStrut(20));
        b4.add(lblNgaySinh = new JLabel("Ngày sinh:"));
        b4.add(Box.createHorizontalStrut(53));
        b4.add(ngaySinh = new JDateChooser());
        ngaySinh.setDateFormatString("dd/MM/yyyy");
        b4.add(Box.createHorizontalStrut(100));
        b4.add(lblNgayVao = new JLabel("Ngày vào làm:"));
        b4.add(Box.createHorizontalStrut(25));
        b4.add(ngayVao = new JDateChooser());
        ngayVao.setDateFormatString("dd/MM/yyyy");
        b4.add(Box.createHorizontalStrut(150));
        b.add(b4);
        
        b.add(Box.createVerticalStrut(20));
        b5 = Box.createHorizontalBox();
        b5.add(Box.createHorizontalStrut(20));
        b5.add(lblChucVu = new JLabel("Chức vụ:"));
        b5.add(Box.createHorizontalStrut(65));
        b5.add(comboChucVu = new JComboBox<>(new String[]{"Tất cả","Nhân viên", "Quản lý"}));
        b5.add(Box.createHorizontalStrut(40));
        b5.add(lbMess = new JLabel(""));
        lbMess.setForeground(Color.RED);
        b5.add(Box.createHorizontalStrut(500));
        b.add(b5);
        b.add(Box.createVerticalStrut(20));

        // font
        Font lbl = new Font("Times New Roman", Font.BOLD, 22);
        Font txt = new Font("Times New Roman", Font.PLAIN, 22);
        lblMaNV.setFont(lbl);
        lblTenNV.setFont(lbl);
        lblSDT.setFont(lbl);
        lblGioiTinh.setFont(lbl);
        lblNgaySinh.setFont(lbl);
        lblNgayVao.setFont(lbl);
        lblDiaChi.setFont(lbl);
        lblcccd.setFont(lbl);
        lbMess.setFont(lbl);
        lblChucVu.setFont(lbl);
        txtMaNV.setFont(txt);
        txtSDT.setFont(txt);
        txtTenNV.setFont(txt);
        checkGioiTinh.setFont(txt);
        ngaySinh.setFont(txt);
        ngayVao.setFont(txt);
        txtDiaChi.setFont(txt);
        txtCCCD.setFont(txt);
        comboChucVu.setFont(txt);
        

        // ====== CÁC NÚT (PHẢI) ======
        Box a = Box.createVerticalBox();
        a.add(Box.createVerticalStrut(17));
        a.add(Box.createVerticalGlue()); // đẩy giữa theo chiều dọc

        btnTim = taoNut("Tìm kiếm");
        btnThem = taoNut("Thêm");
        btnSua = taoNut("Sửa");
        btnXoa = taoNut("Xóa");
        btnLamMoi = taoNut("Làm mới");

        // mỗi nút là một hàng riêng
        for (JButton btn : new JButton[]{btnTim, btnThem, btnSua, btnXoa, btnLamMoi}) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            a.add(btn);
            a.add(Box.createVerticalStrut(15)); // khoảng cách giữa các nút
            btn.addActionListener(this);
        }

        a.add(Box.createVerticalGlue()); // căn giữa tổng thể


        pNor.add(b, BorderLayout.CENTER);
        pNor.add(a, BorderLayout.EAST);
        
        
     // ==== Table ====
        String[] cols = {"Mã nhân viên","Họ tên nhân viên","Số điện thoại","Giới tính","Địa chỉ","CCCD","Ngày sinh","Ngày vào làm","Chức vụ"};
        modelNhanVien = new DefaultTableModel(cols,0);
        tableNhanVien = new JTable(modelNhanVien);
        tableNhanVien.setRowHeight(28);
        tableNhanVien.setFont(new Font("Times New Roman", Font.PLAIN, 20));
        tableNhanVien.setDefaultEditor(Object.class, null); 
        tableNhanVien.addMouseListener(this);
        
     // ---- SET ĐỘ RỘNG TỪNG CỘT ----
        tableNhanVien.getColumnModel().getColumn(0).setPreferredWidth(120); // Mã NV
        tableNhanVien.getColumnModel().getColumn(1).setPreferredWidth(200); // Họ tên
        tableNhanVien.getColumnModel().getColumn(2).setPreferredWidth(150); // SĐT
        tableNhanVien.getColumnModel().getColumn(3).setPreferredWidth(60); // Giới tính
        tableNhanVien.getColumnModel().getColumn(4).setPreferredWidth(300); // Địa chỉ
        tableNhanVien.getColumnModel().getColumn(5).setPreferredWidth(150); // CCCD
        tableNhanVien.getColumnModel().getColumn(6).setPreferredWidth(150); // Ngày sinh
        tableNhanVien.getColumnModel().getColumn(7).setPreferredWidth(150); // Ngày vào
        tableNhanVien.getColumnModel().getColumn(8).setPreferredWidth(130); // Chức vụ
        
        JTableHeader header = tableNhanVien.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 48));
        header.setFont(new Font("Times New Roman", Font.BOLD, 20));
        header.setOpaque(true);
        header.setBackground(Color.decode("#EDE7E3"));
        header.setBorder(BorderFactory.createEmptyBorder());

        JScrollPane scroll = new JScrollPane(tableNhanVien);
	    scroll.getViewport().setBackground(Color.WHITE); // nền bên trong bảng
        scroll.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(165,42,42),2),
        		"Danh Sách Nhân Viên",0,0,new Font("Times New Roman", Font.BOLD, 24)));
        
        pMain.add(pNor, BorderLayout.NORTH);
        pMain.add(scroll, BorderLayout.CENTER);
        add(pMain, BorderLayout.CENTER);
        
        taiDanhSachNhanVienLen();
    }

    /* ===================== TIÊU ĐỀ ===================== */
    private void thanhTieuDe() {
    	String chucVu = (loaiTaiKhoan == 1) ? "Quản lý" : "Nhân viên";
        PanelTieuDe tieude = new PanelTieuDe("Danh sách nhân viên", "/img/nhanvien.png", chucVu, tenHienThi);
        add(tieude, BorderLayout.NORTH);
        pnThanhMenu menu = new pnThanhMenu(tenHienThi, loaiTaiKhoan,maNV);
		menu.setVisible(false);
		add(menu, BorderLayout.WEST);

		tieude.getBtnMenu().addActionListener(e -> {
		    menu.setVisible(!menu.isVisible());
		    revalidate();
		    repaint();
		});
    }


    private JButton taoNut(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Times New Roman", Font.BOLD, 22));
        btn.setPreferredSize(new Dimension(150, 50));
        btn.setMaximumSize(new Dimension(150, 50));  // giữ size trong BoxLayout
        btn.setAlignmentX(CENTER_ALIGNMENT);

        btn.setForeground(Color.WHITE);
        btn.setBackground(Color.decode("#865A52"));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);

        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(Color.decode("#865A52").darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(Color.decode("#865A52"));
            }
        });

        return btn;
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(btnThem)) {
			if (!txtMaNV.getText().trim().isEmpty()) {
		        JOptionPane.showMessageDialog(this,"Trùng mã");
		        return;
		    }
			if (!kiemTraDuLieu()) return;
			NhanVien nv = nutThem();
			NhanVien nvDB = nv_DAO.themNhanVien(nv);
			if (nvDB !=null && nvDB.getMaNV() != null) {
				String gioiTinh = (nvDB.getGioiTinh() != null && nvDB.getGioiTinh()) ? "Nam" : "Nữ";
	            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	            String ngaySinhStr = nvDB.getNgaySinh() != null ? sdf.format(nvDB.getNgaySinh()) : "";
	            String ngayVaoStr  = nvDB.getNgayVaoLam() != null ? sdf.format(nvDB.getNgayVaoLam()) : "";
	            txtMaNV.setText(nvDB.getMaNV());
	            modelNhanVien.addRow(new Object[] {nv.getMaNV(), nv.getHoTen(), nv.getDienThoai(), gioiTinh,
		        		nv.getDiaChi(), nv.getCccd(), ngaySinhStr, ngayVaoStr, nv.getChucVu()});
	            showMess("Thêm thành công", txtMaNV);
			} else {
				JOptionPane.showMessageDialog(this, "Thêm tất bại");
			}
		} else if (o.equals(btnXoa)) {
			nutXoa();
		} else if (o.equals(btnLamMoi)) {
			nutLamMoi();
		} else if (o.equals(btnSua)) {
			nutSua();
		} else if (o.equals(btnTim)) {
			nutTimKiem();
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int row = tableNhanVien.getSelectedRow();
	    if (row == -1) return;

	    txtMaNV.setText(modelNhanVien.getValueAt(row, 0).toString());
	    txtTenNV.setText(modelNhanVien.getValueAt(row, 1).toString());
	    txtSDT.setText(modelNhanVien.getValueAt(row, 2).toString());
	    checkGioiTinh.setSelected("Nam".equalsIgnoreCase(modelNhanVien.getValueAt(row, 3).toString()));
	    txtDiaChi.setText(modelNhanVien.getValueAt(row, 4).toString());
	    txtCCCD.setText(modelNhanVien.getValueAt(row, 5).toString());
	    comboChucVu.setSelectedItem(modelNhanVien.getValueAt(row, 8).toString());

	    try {
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        ngaySinh.setDate(sdf.parse(modelNhanVien.getValueAt(row, 6).toString()));
	        ngayVao.setDate(sdf.parse(modelNhanVien.getValueAt(row, 7).toString()));
	    } catch (Exception ex) {
	        ngaySinh.setDate(null);
	        ngayVao.setDate(null);
	    }
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	private void taiDanhSachNhanVienLen() {
		List<NhanVien> dsNV = nv_DAO.getAllNhanVien();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for (NhanVien nv : dsNV) {
			String gioiTinhStr = (nv.getGioiTinh() != null && nv.getGioiTinh()) ? "Nam" : "Nữ";
			String ngaySinhStr = nv.getNgaySinh() != null ? sdf.format(nv.getNgaySinh()) : "";
	        String ngayVaoStr  = nv.getNgayVaoLam() != null ? sdf.format(nv.getNgayVaoLam()) : "";
	        modelNhanVien.addRow(new Object[] {nv.getMaNV(), nv.getHoTen(), nv.getDienThoai(), gioiTinhStr,
	        		nv.getDiaChi(), nv.getCccd(), ngaySinhStr, ngayVaoStr, nv.getChucVu()});
		}

	}
	private void showMess(String mess, JTextField txt) {
		lbMess.setText(mess);
		txt.requestFocus();

	}
	private boolean kiemTraDuLieu() {
	    String ten   = txtTenNV.getText().trim();
	    String sdt   = txtSDT.getText().trim();
	    String diaChi= txtDiaChi.getText().trim();
	    String cccd  = txtCCCD.getText().trim();
	    Date ns      = ngaySinh.getDate();
	    Date nv      = ngayVao.getDate();
	    String chucVu= comboChucVu.getSelectedItem().toString();

	    // Tên
	    if (ten.isEmpty()) {
	        showMess("Tên nhân viên không được để trống!", txtTenNV);
	        return false;
	    }

	    // SĐT không rỗng + đúng định dạng
	    if (sdt.isEmpty()) {
	        showMess("Số điện thoại không được để trống!", txtSDT);
	        return false;
	    }
	    if (!sdt.matches("0\\d{9}")) {
	        showMess("Số điện thoại phải có 10 chữ số và bắt đầu bằng 0!", txtSDT);
	        return false;
	    }

	    // Địa chỉ
	    if (diaChi.isEmpty()) {
	        showMess("Địa chỉ không được để trống!", txtDiaChi);
	        return false;
	    }

	    // CCCD
	    if (cccd.isEmpty()) {
	        showMess("CCCD không được để trống!", txtCCCD);
	        return false;
	    }
	    if (!cccd.matches("\\d{12}")) {
	        showMess("CCCD phải gồm 12 chữ số!", txtCCCD);
	        return false;
	    }

	    // Ngày sinh
	    if (ns == null) {
	        lbMess.setText("Ngày sinh không được để trống!");
	        return false;
	    }

	    // Tuổi >= 18
	    Calendar cal = Calendar.getInstance();
	    cal.add(Calendar.YEAR, -18);
	    if (ns.after(cal.getTime())) {
	        lbMess.setText("Nhân viên phải đủ 18 tuổi trở lên!");
	        ngaySinh.requestFocus();
	        return false;
	    }

	    Date today = new Date();
	    if (nv == null) {
	        ngayVao.setDate(today);
	    } else if (nv.after(today)) {
	        lbMess.setText("Ngày vào làm không được lớn hơn ngày hiện tại!");
	        ngayVao.requestFocus();
	        return false;
	    }

	    // Chức vụ
	    if (chucVu.equalsIgnoreCase("Tất cả")) {
	        lbMess.setText("Vui lòng chọn chức vụ hợp lệ!");
	        comboChucVu.requestFocus();
	        return false;
	    }

	    lbMess.setText("");
	    return true;
	}

	private NhanVien nutThem() {
		String ten = txtTenNV.getText().trim();
	    String sdt = txtSDT.getText().trim();
	    String diaChi = txtDiaChi.getText().trim();
	    String cccd = txtCCCD.getText().trim();
	    Boolean gt = checkGioiTinh.isSelected(); // true = Nam, false = Nữ
	    Date ns = ngaySinh.getDate();
	    Date nv = ngayVao.getDate();
	    String chucVu = comboChucVu.getSelectedItem().toString();

	    // maNV để null, DB tự sinh
	    return new NhanVien(null, ten, diaChi, cccd, sdt, gt, ns, nv, chucVu);

	}
	private void nutXoa() {
		int row = tableNhanVien.getSelectedRow();
	    if (row == -1) {
	        JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xóa!");
	        return;
	    }

	    String maNV = modelNhanVien.getValueAt(row, 0).toString();
	    int chon = JOptionPane.showConfirmDialog(this, 
	            "Bạn có chắc muốn xóa nhân viên có mã " + maNV + " không?", 
	            "Xác nhận xóa", JOptionPane.YES_NO_OPTION);

	    if (chon == JOptionPane.YES_OPTION) {
	        boolean kq = nv_DAO.xoaNhanVien(maNV);
	        if (kq) {
	            modelNhanVien.removeRow(row);
	            nutLamMoi();
	            showMess("Xóa thành công!", txtMaNV);
	            modelNhanVien.setRowCount(0);
	            taiDanhSachNhanVienLen();
	        } else {
	            JOptionPane.showMessageDialog(this, "Xóa thất bại! Kiểm tra lại mã nhân viên.");
	        }
	    }

	}
	private void nutLamMoi() {
		modelNhanVien.setRowCount(0);
		txtMaNV.setText("");
	    txtTenNV.setText("");
	    txtSDT.setText("");
	    txtDiaChi.setText("");
	    txtCCCD.setText("");
	    checkGioiTinh.setSelected(false);
	    ngaySinh.setDate(null);
	    ngayVao.setDate(null);
	    comboChucVu.setSelectedIndex(0);
	    lbMess.setText("");
	    taiDanhSachNhanVienLen();

	}
	private void nutSua() {
	    int row = tableNhanVien.getSelectedRow();
	    if (row == -1) {
	        JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!");
	        return;
	    }

	    if (!kiemTraDuLieu()) return;

	    String maNV   = txtMaNV.getText().trim(); 
	    String ten    = txtTenNV.getText().trim();
	    String sdt    = txtSDT.getText().trim();
	    String diaChi = txtDiaChi.getText().trim();
	    String cccd   = txtCCCD.getText().trim();
	    Boolean gt    = checkGioiTinh.isSelected();
	    Date ngayS    = ngaySinh.getDate();
	    Date ngayV    = ngayVao.getDate();
	    String chucVu = comboChucVu.getSelectedItem().toString();

	    NhanVien nvUpdate = new NhanVien(maNV, ten, diaChi, cccd, sdt, gt, ngayS, ngayV, chucVu);

	    int confirm = JOptionPane.showConfirmDialog(
	            this,
	            "Bạn có chắc muốn cập nhật thông tin nhân viên " + maNV + " không?",
	            "Xác nhận sửa",
	            JOptionPane.YES_NO_OPTION
	    );
	    if (confirm != JOptionPane.YES_OPTION) return;

	    if (nv_DAO.capNhatNhanVien(nvUpdate)) {
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        String gtStr  = (gt != null && gt) ? "Nam" : "Nữ";
	        String nsStr  = (ngayS != null) ? sdf.format(ngayS) : "";
	        String nvStr  = (ngayV != null) ? sdf.format(ngayV) : "";

	        modelNhanVien.setValueAt(ten,    row, 1);
	        modelNhanVien.setValueAt(sdt,    row, 2);
	        modelNhanVien.setValueAt(gtStr,  row, 3);
	        modelNhanVien.setValueAt(diaChi, row, 4);
	        modelNhanVien.setValueAt(cccd,   row, 5);
	        modelNhanVien.setValueAt(nsStr,  row, 6);
	        modelNhanVien.setValueAt(nvStr,  row, 7);
	        modelNhanVien.setValueAt(chucVu, row, 8);

	        showMess("Cập nhật thành công!", txtMaNV);
	    } else {
	        JOptionPane.showMessageDialog(this, "Sửa thất bại! Vui lòng kiểm tra lại.");
	    }
	}

	private void nutTimKiem() {
	    String ten    = txtTenNV.getText().trim();
	    String sdt    = txtSDT.getText().trim();
	    String chucVu = comboChucVu.getSelectedItem().toString();

	    boolean coDieuKien = !ten.isEmpty() || !sdt.isEmpty() || !"Tất cả".equalsIgnoreCase(chucVu);

	    // 🔹 Nếu người dùng không nhập gì => bật hộp thoại tìm theo mã
	    if (!coDieuKien) {
	        String maNV = JOptionPane.showInputDialog(this, "Nhập mã nhân viên cần tìm:");
	        if (maNV == null || maNV.trim().isEmpty()) {
	            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã nhân viên!");
	            return;
	        }

	        NhanVien nv = nv_DAO.getNhanVienTheoMa(maNV.trim());
	        modelNhanVien.setRowCount(0);

	        if (nv != null) {
	            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	            String gtStr = (nv.getGioiTinh() != null && nv.getGioiTinh()) ? "Nam" : "Nữ";
	            String nsStr = nv.getNgaySinh() != null ? sdf.format(nv.getNgaySinh()) : "";
	            String nvStr = nv.getNgayVaoLam() != null ? sdf.format(nv.getNgayVaoLam()) : "";

	            modelNhanVien.addRow(new Object[]{
	                nv.getMaNV(), nv.getHoTen(), nv.getDienThoai(), gtStr,
	                nv.getDiaChi(), nv.getCccd(), nsStr, nvStr, nv.getChucVu()
	            });
	            showMess("Đã tìm thấy nhân viên: " + maNV, txtMaNV);
	        } else {
	            JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên có mã: " + maNV);
	            taiDanhSachNhanVienLen();
	        }
	        return;
	    }

	    // 🔹 Nếu có nhập tên / sdt / chức vụ → tìm theo điều kiện
	    List<NhanVien> ds = nv_DAO.timKiemNhanVien(ten, sdt, chucVu);
	    modelNhanVien.setRowCount(0);

	    if (ds.isEmpty()) {
	        JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên phù hợp!");
	        taiDanhSachNhanVienLen();
	        return;
	    }

	    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	    for (NhanVien nv : ds) {
	        String gtStr = (nv.getGioiTinh() != null && nv.getGioiTinh()) ? "Nam" : "Nữ";
	        String nsStr = nv.getNgaySinh() != null ? sdf.format(nv.getNgaySinh()) : "";
	        String nvStr = nv.getNgayVaoLam() != null ? sdf.format(nv.getNgayVaoLam()) : "";

	        modelNhanVien.addRow(new Object[]{
	            nv.getMaNV(), nv.getHoTen(), nv.getDienThoai(), gtStr,
	            nv.getDiaChi(), nv.getCccd(), nsStr, nvStr, nv.getChucVu()
	        });
	    }

	    showMess("Đã tìm thấy " + ds.size() + " nhân viên phù hợp!", txtTenNV);
	}

}
