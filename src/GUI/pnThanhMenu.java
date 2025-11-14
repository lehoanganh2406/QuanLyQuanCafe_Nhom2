package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class pnThanhMenu extends JPanel implements ActionListener {

    private JButton btnHome,btnThongKe, btnOrder, btnMenu, btnDSKhachHang, btnDSNhanVien, btnHoaDon, btnLogout;
    private String tenHienThi;
    private int loaiTaiKhoan; // 1 = Quản lý, 0 = Nhân viên
    private Color themeColor = new Color(120, 74, 57);
	private String maNV;

    public pnThanhMenu(String tenHienThi, int loaiTaiKhoan, String maNV) {
        this.tenHienThi = tenHienThi;
        this.loaiTaiKhoan = loaiTaiKhoan;
        this.maNV = maNV;
        initComponents();
    }

    private void initComponents() {
        setBackground(new Color(240, 240, 240));
        setPreferredSize(new Dimension(300, 0));
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        // ===== Nút TRANG CHỦ (logo + chữ COFFEE) =====
        btnHome = createSideButton("COFFEE", "src/img/logocafe.png");

        // ===== Các nút menu =====
        btnOrder        = createSideButton("ORDER", "src/img/order.png");
        btnMenu         = createSideButton("THỰC ĐƠN", "src/img/menu.png");
        btnDSKhachHang  = createSideButton("DANH SÁCH KHÁCH HÀNG", "src/img/user.png");
        btnDSNhanVien   = createSideButton("DANH SÁCH NHÂN VIÊN", "src/img/staff.png");
        btnHoaDon       = createSideButton("LỊCH SỬ HÓA ĐƠN", "src/img/bill.png");
        btnThongKe      = createSideButton("THỐNG KÊ", "src/img/thongke.png");
        btnLogout       = createSideButton("ĐĂNG XUẤT", "src/img/logout.png");

        // ===== Sắp xếp menu =====
        add(btnHome);
        add(btnOrder);
        add(btnMenu);
        add(btnDSKhachHang);   
        add(btnDSNhanVien);    
        add(btnThongKe);
        add(btnHoaDon);

        add(Box.createVerticalGlue());
        add(btnLogout);

        // Tooltip cho nhân viên ở mục không có quyền
        if (loaiTaiKhoan == 0) {
            btnDSKhachHang.setToolTipText("Chỉ quản lý mới được truy cập");
            btnDSNhanVien.setToolTipText("Chỉ quản lý mới được truy cập");
            btnThongKe.setToolTipText("Chỉ quản lý mới được truy cập");
        }

        // Gắn sự kiện
        for (JButton btn : new JButton[]{
                btnHome, btnOrder, btnMenu,
                btnDSKhachHang, btnDSNhanVien,
                btnHoaDon, btnLogout, btnThongKe}) {
            btn.addActionListener(this);
        }
    }

    private JButton createSideButton(String text, String iconPath) {
        JButton btn = new JButton(text);

        ImageIcon icon = new ImageIcon(iconPath);
        Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btn.setIcon(new ImageIcon(scaledImage));

        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(10);
        btn.setFont(new Font("Arial", Font.BOLD, 18));
        btn.setForeground(themeColor);
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, new Color(230, 230, 230)));
        btn.setPreferredSize(new Dimension(300, 100));
        btn.setMaximumSize(new Dimension(300, 100));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(245, 230, 220));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                btn.setBackground(Color.WHITE);
            }
        });

        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();

        if (src.equals(btnHome)) {
            openTrangChu();

        } else if (src.equals(btnOrder)) {
            // Cả QL + NV đều được
            openOrder();

        } else if (src.equals(btnMenu)) {
            // Cả 2 vào, phân quyền thêm/sửa/xóa xử lý trong ThucDon_GUI
            openMenu();

        } else if (src.equals(btnDSKhachHang)) {
            if (loaiTaiKhoan == 1) {
                openDSKhachHang();
            } else {
                showNoPermission();
            }

        } else if (src.equals(btnDSNhanVien)) {
            if (loaiTaiKhoan == 1) {
                openDSNhanVien();
            } else {
                showNoPermission();
            }

        } else if (src.equals(btnThongKe)) {
			openThongKe();
		}
        else if (src.equals(btnHoaDon)) {
            // Cả 2 vào, xóa chỉ cho QL xử lý trong LichsuThanhToan
            openHoaDon();

        } else if (src.equals(btnLogout)) {
            logout();
        }
    }

    private void showNoPermission() {
        JOptionPane.showMessageDialog(this,
                "Bạn không có quyền truy cập chức năng này!",
                "Thông báo",
                JOptionPane.WARNING_MESSAGE);
    }

    // ==== Mở form (truyền role qua) ====
    private void openTrangChu() {
        new ManHinhChinh_GUI(tenHienThi, loaiTaiKhoan, maNV).setVisible(true);
        closeCurrent();
    }

    private void openOrder() {
        new Ban_GUI(tenHienThi, loaiTaiKhoan, maNV).setVisible(true);
        closeCurrent();
    }

    private void openMenu() {
        new ThucDon_GUI(tenHienThi, loaiTaiKhoan, maNV).setVisible(true);
        closeCurrent();
    }

    private void openDSKhachHang() {
        new DsKhachHang_GUI(tenHienThi, loaiTaiKhoan, maNV).setVisible(true);
        closeCurrent();
    }

    private void openDSNhanVien() {
        new DSNhanVien_GUI(tenHienThi, loaiTaiKhoan, maNV).setVisible(true);
        closeCurrent();
    }

    private void openHoaDon() {
        new DsHoaDon_GUI(tenHienThi, loaiTaiKhoan, maNV).setVisible(true);
        closeCurrent();
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new Login_GUI().setVisible(true);
            closeCurrent();
        }
    }
    private void openThongKe() {
    	new ThongKe_GUI(tenHienThi, loaiTaiKhoan, maNV).setVisible(true);
        closeCurrent();

	}

    private void closeCurrent() {
        Window w = SwingUtilities.getWindowAncestor(this);
        if (w != null) w.dispose();
    }
}
