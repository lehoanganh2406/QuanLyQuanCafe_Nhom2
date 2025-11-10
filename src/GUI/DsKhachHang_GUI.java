package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
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

import connectDB.ConnectDB;
import dao.KhachHang_DAO;
import entity.KhachHang;

public class DsKhachHang_GUI extends JFrame implements ActionListener, MouseListener {

    private DefaultTableModel mdDSKH;
    private JTable tableDSKH;
    private JButton btnBack;
    private JButton btnThem;
    private final Color brownColor = Color.decode("#865A52");
    private final Color nen = Color.decode("#E3CFC1");
    private JTextField txtTim;
    private JButton btnTim;
    private JButton btnXoa;
    private JButton btnSua;
    private JTextField txtten;
    private JTextField txtsdt;
    private JTextField txtdtl;
    private JTextField txtma;
    private KhachHang_DAO kh_dao;

    private String tenHienThi;
    private int loaiTaiKhoan;
	private String maNV;

    // ===== Constructor chính (dùng khi mở từ ManHinhChinh) =====
    public DsKhachHang_GUI(String tenHienThi, int loaiTaiKhoan, String maNV) {
        this.tenHienThi = tenHienThi;
        this.loaiTaiKhoan = loaiTaiKhoan;
        this.maNV = maNV;
        try {
            ConnectDB.getInstance().connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

        kh_dao = new KhachHang_DAO();

        setTitle("Danh sách khách hàng");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                // Quay lại màn hình chính, giữ đúng tài khoản + tên hiển thị
                new ManHinhChinh_GUI(tenHienThi, loaiTaiKhoan, maNV).setVisible(true);
                dispose();
            }
        });


        thanhTieuDe();

        JPanel pcenter = new JPanel(new BorderLayout());
        pcenter.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        pcenter.setBackground(nen);

        // ===== Bảng khách hàng =====
        String[] header = { "Mã KH", "Tên KH", "SĐT", "Điểm TL" };
        mdDSKH = new DefaultTableModel(header, 0);
        tableDSKH = new JTable(mdDSKH);
        tableDSKH.setRowHeight(28);
        tableDSKH.setFont(new Font("Arial", Font.PLAIN, 14));
        tableDSKH.setGridColor(new Color(180, 150, 120));
        tableDSKH.setSelectionBackground(new Color(210, 180, 140));
        tableDSKH.setSelectionForeground(Color.BLACK);

        JTableHeader headerTable = tableDSKH.getTableHeader();
        headerTable.setPreferredSize(new Dimension(headerTable.getWidth(), 45));
        headerTable.setFont(new Font("Times New Roman", Font.BOLD, 18));
        headerTable.setBackground(Color.decode("#EDE7E3"));
        headerTable.setBorder(BorderFactory.createEmptyBorder());

        JScrollPane scroll = new JScrollPane(
                tableDSKH,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pcenter.add(scroll, BorderLayout.CENTER);

        // ===== Thanh nút phía dưới =====
        JPanel pSou = new JPanel(new BorderLayout());
        Box c = Box.createVerticalBox();
        Box a = Box.createHorizontalBox();

        a.add(Box.createHorizontalStrut(20));
        a.add(btnBack = new JButton("Quay lại"));
        a.add(Box.createHorizontalStrut(10));
        a.add(btnThem = new JButton("Thêm"));
        a.add(Box.createHorizontalStrut(10));
        a.add(btnXoa = new JButton("Xóa"));
        a.add(Box.createHorizontalStrut(10));
        a.add(btnSua = new JButton("Sửa"));
        a.add(Box.createHorizontalStrut(10));
        a.add(txtTim = new JTextField("nhập sdt cần tìm", 10));
        a.add(Box.createHorizontalStrut(10));
        a.add(btnTim = new JButton("Tìm"));
        a.add(Box.createHorizontalStrut(20));

        for (JButton btt : new JButton[] { btnBack, btnThem, btnSua, btnTim, btnXoa }) {
            btt.setBackground(brownColor);
            btt.setForeground(Color.WHITE);
            btt.setFocusPainted(false);
            btt.setPreferredSize(new Dimension(100, 40));
        }

        c.add(Box.createVerticalStrut(15));
        c.add(a);
        c.add(Box.createVerticalStrut(15));
        pSou.add(c);
        add(pSou, BorderLayout.SOUTH);

        // ===== Form thông tin phía trên bảng =====
        JPanel pform = new JPanel();
        pform.setBackground(nen);

        Box b, b1, b2, b3, b4;
        b = Box.createVerticalBox();

        b1 = Box.createHorizontalBox();
        b1.add(new JLabel("Mã khách hàng:"));
        b1.add(Box.createHorizontalStrut(27));
        b1.add(txtma = new JTextField(20));
        txtma.setEditable(false);
        b.add(Box.createVerticalStrut(5));
        b.add(b1);

        b2 = Box.createHorizontalBox();
        b2.add(new JLabel("Tên khách hàng:"));
        b2.add(Box.createHorizontalStrut(20));
        b2.add(txtten = new JTextField(20));
        b.add(Box.createVerticalStrut(5));
        b.add(b2);

        b3 = Box.createHorizontalBox();
        b3.add(new JLabel("SDT liên hệ:"));
        b3.add(Box.createHorizontalStrut(49));
        b3.add(txtsdt = new JTextField(20));
        b.add(Box.createVerticalStrut(5));
        b.add(b3);

        b4 = Box.createHorizontalBox();
        b4.add(new JLabel("Điểm tích lũy:"));
        b4.add(Box.createHorizontalStrut(40));
        b4.add(txtdtl = new JTextField(20));
        b.add(Box.createVerticalStrut(5));
        b.add(b4);

        b.setPreferredSize(new Dimension(400, 150));
        pform.add(b, BorderLayout.NORTH);
        pcenter.add(pform, BorderLayout.NORTH);

        for (JTextField txt : new JTextField[] { txtma, txtten, txtsdt, txtdtl }) {
            txt.setBorder(null);
            txt.setBackground(null);
            txt.setForeground(Color.BLACK);
            txt.setCaretColor(Color.BLACK);
        }

        add(pcenter);

        // ===== Sự kiện =====
        tableDSKH.addMouseListener(this);
        btnThem.addActionListener(this);
        btnBack.addActionListener(this);
        btnSua.addActionListener(this);
        btnTim.addActionListener(this);
        btnXoa.addActionListener(this);

        // Load dữ liệu ban đầu
        docDulieutuDBvaoTable();
    }


    private void thanhTieuDe() {
        String chucVu = (loaiTaiKhoan == 1) ? "Quản lý" : "Nhân viên";
        PanelTieuDe tieude =
                new PanelTieuDe("Danh sách khách hàng", "/img/iconuser.png", chucVu, tenHienThi);
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

    /* ===================== Mouse ===================== */
    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tableDSKH.getSelectedRow();
        if (row < 0) return;
        txtma.setText(mdDSKH.getValueAt(row, 0).toString());
        txtten.setText(mdDSKH.getValueAt(row, 1).toString());
        txtsdt.setText(mdDSKH.getValueAt(row, 2).toString());
        txtdtl.setText(mdDSKH.getValueAt(row, 3).toString());
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    /* ===================== Action ===================== */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o.equals(btnBack)) {
            // Quay lại màn hình chính, giữ thông tin user
            new ManHinhChinh_GUI(tenHienThi, loaiTaiKhoan, maNV).setVisible(true);
            dispose();
        }

        else if (o.equals(btnThem)) {
            if (!valiData()) return;

            String tenKH = txtten.getText().trim();
            String sdt = txtsdt.getText().trim();
            int diemTL = 0;

            KhachHang khTonTai = kh_dao.getBySDT(sdt);
            if (khTonTai != null) {
                JOptionPane.showMessageDialog(this,
                        "Số điện thoại này đã tồn tại trong hệ thống!",
                        "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }

            KhachHang kh = new KhachHang(null, tenKH, sdt, diemTL);
            boolean themOK = kh_dao.insert(kh);

            if (themOK) {
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                mdDSKH.setRowCount(0);
                docDulieutuDBvaoTable();
                cleartext();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Thêm khách hàng thất bại!",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        else if (o.equals(btnXoa)) {
            int row = tableDSKH.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Cần chọn 1 khách hàng để xóa");
                return;
            }
            String maKH = mdDSKH.getValueAt(row, 0).toString();
            String tenKH = mdDSKH.getValueAt(row, 1).toString();

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn xóa khách hàng: " + tenKH + " (" + maKH + ")?",
                    "Xác nhận xóa", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                boolean xoaOK = kh_dao.delete(maKH);
                if (xoaOK) {
                    JOptionPane.showMessageDialog(this, "Xóa thành công");
                    mdDSKH.setRowCount(0);
                    docDulieutuDBvaoTable();
                    cleartext();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Xóa thất bại!",
                            "Lỗi xóa", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        else if (o.equals(btnSua)) {
            int row = tableDSKH.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Cần chọn khách hàng cần sửa");
                return;
            }
            if (!valiData()) return;

            String maKH = txtma.getText().trim();
            String tenKH = txtten.getText().trim();
            String sdt = txtsdt.getText().trim();
            int diemTL;

            try {
                diemTL = Integer.parseInt(txtdtl.getText().trim());
                if (diemTL < 0) {
                    JOptionPane.showMessageDialog(this, "Điểm tích lũy phải >= 0");
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Điểm tích lũy phải là số nguyên");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Có chắc chắn muốn cập nhật thông tin khách hàng này?",
                    "Xác nhận sửa", JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                KhachHang kh = new KhachHang(maKH, tenKH, sdt, diemTL);
                boolean suaTT = kh_dao.updateInfo(kh);
                boolean suaDTL = kh_dao.updateDiem(maKH, diemTL);

                if (suaTT || suaDTL) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thông tin khách hàng thành công");
                    mdDSKH.setRowCount(0);
                    docDulieutuDBvaoTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể cập nhật");
                }
            }
        }

        else if (o.equals(btnTim)) {
            String sdt = txtTim.getText().trim();
            if (sdt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Cần nhập SĐT cần tìm");
                return;
            }

            KhachHang kh = kh_dao.getBySDT(sdt);
            if (kh != null) {
                for (int i = 0; i < mdDSKH.getRowCount(); i++) {
                    String sdtTable = mdDSKH.getValueAt(i, 2).toString();
                    if (sdtTable.equals(sdt)) {
                        tableDSKH.setRowSelectionInterval(i, i);
                        tableDSKH.scrollRectToVisible(tableDSKH.getCellRect(i, 0, true));

                        txtma.setText(kh.getMaKH());
                        txtten.setText(kh.getTenKH());
                        txtsdt.setText(kh.getSdt());
                        txtdtl.setText(String.valueOf(kh.getDiemTL()));
                        return;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "Không tìm thấy khách hàng có SĐT: " + sdt);
            }
        }
    }

    /* ===================== Helpers ===================== */

    private void cleartext() {
        txtma.setText("");
        txtten.setText("");
        txtsdt.setText("");
        txtdtl.setText("");
    }

    public void docDulieutuDBvaoTable() {
        List<KhachHang> list = kh_dao.getAllKhachHang();
        for (KhachHang k : list) {
            mdDSKH.addRow(new Object[]{
                    k.getMaKH(),
                    k.getTenKH(),
                    k.getSdt(),
                    k.getDiemTL()
            });
        }
    }

    private boolean valiData() {
        String ten = txtten.getText().trim();
        String sdt = txtsdt.getText().trim();
        if (ten.isEmpty() || sdt.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Các ô nhập không được rỗng");
            return false;
        }
        if (!sdt.matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(this, "SĐT phải gồm 10 số và bắt đầu bằng 0");
            return false;
        }
        return true;
    }
}
