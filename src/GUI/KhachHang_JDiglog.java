package GUI;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import connectDB.ConnectDB;
import dao.KhachHang_DAO;
import entity.KhachHang;

public class KhachHang_JDiglog extends JDialog implements ActionListener, MouseListener {
	private KhachHang selected;
	private JTextField txtma, txtten, txtsdt, txtdtl, txtTim;
    private DefaultTableModel mdKH;
    private JTable tableKH;
    private JButton btnBack, btnThem, btnChon;
    private final Color nen = Color.decode("#E3CFC1");
    private final Color brownColor = Color.decode("#865A52");

    public KhachHang_JDiglog(Window owner) {
    	super(owner, "Quản lý khách hàng", Dialog.ModalityType.APPLICATION_MODAL);
        setSize(800, 750);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        // ===== HEADER =====
        JPanel pNhap = new JPanel(new BorderLayout());
        pNhap.setPreferredSize(new Dimension(0, 60));
        pNhap.setBackground(brownColor);
        JLabel til = new JLabel("KHÁCH HÀNG", JLabel.CENTER);
        til.setForeground(Color.white);
        til.setFont(new Font("Arial", Font.BOLD, 38));
        pNhap.add(til, BorderLayout.CENTER);
        add(pNhap, BorderLayout.NORTH);

        // ===== FORM NHẬP =====
        JPanel pcenter = new JPanel(new BorderLayout());
		JPanel pform = new JPanel();
		pcenter.setBackground(nen);
		pform.setBackground(nen);
		Box b,b1,b2,b3,b4;
		b=Box.createVerticalBox();
		
		b1=Box.createHorizontalBox();
		b1.add(new JLabel("Mã khách hàng:"));
		b.add(Box.createVerticalStrut(5));
		b1.add(Box.createHorizontalStrut(27));
		b1.add(txtma= new JTextField(20));
		txtma.setEditable(false); 
        txtma.setFocusable(false);
		b.add(b1);
		
		
		b2=Box.createHorizontalBox();
		b2.add(new JLabel("Tên khách hàng :"));
		b2.add(Box.createHorizontalStrut(20));
		b2.add(txtten=new JTextField(20));
		b.add(Box.createVerticalStrut(5));
		b.add(b2);
		
		b3=Box.createHorizontalBox();
		b3.add(new JLabel("SDT liên hệ:"));
		b3.add(Box.createHorizontalStrut(49));
		b3.add(txtsdt=new JTextField(20));
		b.add(Box.createVerticalStrut(5));
		b.add(b3);
		
		b4=Box.createHorizontalBox();
		b4.add(new JLabel("Điểm tích lũy:"));
		b4.add(Box.createHorizontalStrut(40));
		b4.add(txtdtl=new JTextField(20));
		txtdtl.setEditable(false);
        txtdtl.setFocusable(false);
		b.add(Box.createVerticalStrut(5));
		b.add(b4);
		b.setPreferredSize(new Dimension(400, 150));
		
		pform.add(b,BorderLayout.NORTH);
		pcenter.add(pform,BorderLayout.NORTH);
        pcenter.add(pform, BorderLayout.NORTH);

        // ===== BẢNG =====
        String[] header = {"Mã KH", "Tên KH", "SĐT", "Điểm TL"};
        mdKH = new DefaultTableModel(header, 0);
        tableKH = new JTable(mdKH);
        tableKH.setRowHeight(28);
        tableKH.setFont(new Font("Arial", Font.PLAIN, 14));
        tableKH.setGridColor(new Color(180, 150, 120));
        tableKH.setSelectionBackground(new Color(210, 180, 140));
        tableKH.setSelectionForeground(Color.BLACK);

        JTableHeader headerTable = tableKH.getTableHeader();
        headerTable.setPreferredSize(new Dimension(headerTable.getWidth(), 45));
        headerTable.setFont(new Font("Times New Roman", Font.BOLD, 18));
        headerTable.setBackground(Color.decode("#EDE7E3"));
        headerTable.setBorder(BorderFactory.createEmptyBorder());

        JScrollPane scroll = new JScrollPane(tableKH,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pcenter.add(scroll, BorderLayout.CENTER);

        // ===== BUTTONS =====
        Box c = Box.createVerticalBox();
        Box a = Box.createHorizontalBox();

        a.add(Box.createHorizontalStrut(20));
        a.add(btnBack = new JButton("Quay lại"));
        a.add(Box.createHorizontalGlue());
        a.add(btnThem = new JButton("Thêm"));
        a.add(Box.createHorizontalStrut(10));
        a.add(btnChon = new JButton("Chọn")); 
        a.add(Box.createHorizontalStrut(20));

        for (JButton btt : new JButton[]{btnBack, btnThem, btnChon}) {
            btt.setBackground(brownColor);
            btt.setForeground(Color.WHITE);
            btt.setFocusPainted(false);
        }

        c.add(Box.createVerticalStrut(15));
        c.add(a);
        c.add(Box.createVerticalStrut(15));
        pcenter.add(c, BorderLayout.SOUTH);

        add(pcenter, BorderLayout.CENTER);

        // ===== EVENT =====
        btnBack.addActionListener(this);
        btnThem.addActionListener(this);
        btnChon.addActionListener(this);
        tableKH.addMouseListener(this);
        txtsdt.addActionListener(e -> timTheoSoDienThoai());


        // ===== LOAD DATA =====
        taidulieuKH();
    }

    // ===== LOAD DỮ LIỆU TỪ DAO =====
    private void taidulieuKH() {
        List<KhachHang> ds = KhachHang_DAO.getInstance().getAllKhachHang();
        hienthiKH(ds);
    }

    private void hienthiKH(List<KhachHang> list) {
        mdKH.setRowCount(0);
        for (KhachHang k : list) {
            Object[] row = {k.getMaKH(), k.getTenKH(), k.getSdt(), k.getDiemTL()};
            mdKH.addRow(row);
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

    private boolean themKhachHang(KhachHang kh) {
        return KhachHang_DAO.getInstance().insert(kh);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o.equals(btnThem)) {
            if (!valiData()) return;
            String tenKH = txtten.getText().trim();
            String sdt = txtsdt.getText().trim();
            int diemTL = 0;
            KhachHang kh = new KhachHang(null, tenKH, sdt, diemTL);

            boolean success = themKhachHang(kh);
            if (success) {
                taidulieuKH();
                JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
                txtma.setText(""); txtten.setText(""); txtsdt.setText(""); txtdtl.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Không thể thêm khách hàng (trùng SĐT?)");
            }
        }

        if (o.equals(btnChon)) {
            int row = tableKH.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Hãy chọn một khách hàng trong bảng!");
                return;
            }
            // Lấy đủ dữ liệu từ bảng, có thể gọi lại DAO theo mã để nhất quán
            String ma   = tableKH.getValueAt(row, 0).toString();
            String ten  = tableKH.getValueAt(row, 1).toString();
            String sdt  = tableKH.getValueAt(row, 2).toString();
            int diem    = Integer.parseInt(tableKH.getValueAt(row, 3).toString());

            selected = new KhachHang(ma, ten, sdt, diem);
            dispose(); // đóng dialog, ThanhToan_GUI sẽ gọi getSelected()
        }

        if (o.equals(btnBack)) dispose();
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int row = tableKH.getSelectedRow();
        if (row != -1) {
            txtma.setText(tableKH.getValueAt(row, 0).toString());
            txtten.setText(tableKH.getValueAt(row, 1).toString());
            txtsdt.setText(tableKH.getValueAt(row, 2).toString());
            txtdtl.setText(tableKH.getValueAt(row, 3).toString());
        }
    }
    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}

    public static void main(String[] args) {
        ConnectDB.getInstance().connect();
        SwingUtilities.invokeLater(() -> new KhachHang_JDiglog((Window) null).setVisible(true));
    }
    private void timTheoSoDienThoai() {
        String sdt = txtsdt.getText().trim();

        // Validate nhanh cho ô tìm
        if (!sdt.matches("^0\\d{9}$")) {
            JOptionPane.showMessageDialog(this, "Nhập SĐT 10 số, bắt đầu bằng 0 (vd: 0901234567)");
            return;
        }

        KhachHang kh = KhachHang_DAO.getInstance().getBySDT(sdt);
        if (kh != null) {
            // Đổ form
            txtma.setText(kh.getMaKH());
            txtten.setText(kh.getTenKH());
            txtdtl.setText(String.valueOf(kh.getDiemTL()));

            // Lọc bảng chỉ còn khách này
            mdKH.setRowCount(0);
            mdKH.addRow(new Object[]{ kh.getMaKH(), kh.getTenKH(), kh.getSdt(), kh.getDiemTL() });

            // Chọn dòng đầu để đồng bộ UI
            if (tableKH.getRowCount() > 0) {
                tableKH.setRowSelectionInterval(0, 0);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng với SĐT: " + sdt);
            taidulieuKH();
        }
    }
    public KhachHang getSelected() {
        return selected;
    }

}