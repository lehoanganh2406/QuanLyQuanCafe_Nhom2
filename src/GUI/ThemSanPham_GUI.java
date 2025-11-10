package GUI;

import javax.swing.*;
import connectDB.ConnectDB;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
import dao.SanPham_DAO;
import entity.SanPham;
import entity.LoaiSanPham;

public class ThemSanPham_GUI extends JFrame implements ActionListener {
    private JTextField txtTenSP, txtSoLuong, txtDonGia, txtMoTa;
    private JComboBox<String> cboLoai;
    private JButton btnLuu, btnHuy, btnChonAnh;
    private JLabel lblAnh;
    private File fileAnh;
    private SanPham_DAO spDAO = new SanPham_DAO();
    private List<LoaiSanPham> dsLoai;
    private SanPham spSua; // nếu != null -> đang sửa

    private static final Color MAU_NAU_DAM = new Color(134, 90, 82);
    private static final Color MAU_NAU_NHAT = new Color(227, 207, 193);

    public ThemSanPham_GUI() {
        this(null); //Gọi lại constructor có tham số, ở chế độ "thêm"
    }

    public ThemSanPham_GUI(SanPham sp) {
    	try {
			ConnectDB.getInstance().connect();
			System.out.println("ket nnoi thanh cong");
		} catch (Exception e) {
			e.printStackTrace();
		}
        this.spSua = sp;
        setTitle(sp == null ? "Thêm sản phẩm mới" : "Sửa sản phẩm");
        setSize(700, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        //Đặt font mặc định cho toàn bộ giao diện
        Font fontlbl = new Font("Times New Roman", Font.BOLD, 18);
        Font fonttxt = new Font("Times New Roman", Font.PLAIN, 18);
        UIManager.put("Label.font", fontlbl);
        UIManager.put("Button.font", fonttxt);
        UIManager.put("TextField.font", fonttxt);
        UIManager.put("ComboBox.font", fonttxt);

        JPanel pNorth = new JPanel();
        pNorth.setBackground(MAU_NAU_DAM);
        JLabel lblTitle = new JLabel(sp == null ? "THÊM SẢN PHẨM" : "SỬA SẢN PHẨM");
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 26));
        pNorth.add(lblTitle);
        add(pNorth, BorderLayout.NORTH);

        // ====== Nội dung chính ======
        JPanel pMain = new JPanel();
        pMain.setBackground(MAU_NAU_NHAT);
        pMain.setLayout(new BoxLayout(pMain, BoxLayout.Y_AXIS));
        pMain.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        pMain.add(createRow("Tên sản phẩm:", txtTenSP = new JTextField(20)));
        pMain.add(Box.createVerticalStrut(10));

        pMain.add(createRow("Số lượng:", txtSoLuong = new JTextField(20)));
        pMain.add(Box.createVerticalStrut(10));

        pMain.add(createRow("Đơn giá:", txtDonGia = new JTextField(20)));
        pMain.add(Box.createVerticalStrut(10));

        cboLoai = new JComboBox<>();
        pMain.add(createRow("Loại sản phẩm:", cboLoai));
        pMain.add(Box.createVerticalStrut(10));

        pMain.add(createRow("Mô tả:", txtMoTa = new JTextField(20)));
        pMain.add(Box.createVerticalStrut(10));

        //Ảnh
        JPanel pAnhBtn = createRow("Ảnh sản phẩm:", btnChonAnh = new JButton("Chọn ảnh"));
        btnChonAnh.setBackground(MAU_NAU_DAM);
        btnChonAnh.setForeground(Color.WHITE);
        pMain.add(pAnhBtn);
        pMain.add(Box.createVerticalStrut(5));


        JLabel lblLe = new JLabel("                     ");
        lblAnh = new JLabel();
        lblAnh.setPreferredSize(new Dimension(390, 250));
        lblAnh.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        lblAnh.setOpaque(true);
        lblAnh.setBackground(Color.WHITE);
        lblAnh.setHorizontalAlignment(JLabel.CENTER);
        JPanel pAnh = new JPanel();
        pAnh.setBackground(MAU_NAU_NHAT);
        pAnh.add(lblLe);
        pAnh.add(lblAnh);
        pMain.add(pAnh);

        //Nút
        JPanel pBtn = new JPanel();
        pBtn.setBackground(MAU_NAU_NHAT);
        btnLuu = new JButton(sp == null ? "Lưu" : "Cập nhật");
        btnHuy = new JButton("Hủy");
        btnLuu.setBackground(MAU_NAU_DAM);
        btnLuu.setForeground(Color.WHITE);
        btnLuu.setPreferredSize(new Dimension(100, 30));
        
        btnHuy.setBackground(MAU_NAU_DAM);
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setPreferredSize(new Dimension(100, 30));

        pBtn.add(btnLuu);
        pBtn.add(Box.createHorizontalStrut(40));
        pBtn.add(btnHuy);
        pMain.add(Box.createVerticalStrut(20));
        pMain.add(pBtn);

        add(pMain, BorderLayout.CENTER);

        loadLoaiSanPham();
	    if (sp != null) fillForm(sp);
	     
        btnLuu.addActionListener(this);
        btnHuy.addActionListener(this);
        btnChonAnh.addActionListener(this);
    }

    private JPanel createRow(String label, JComponent field) {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(MAU_NAU_NHAT);
        JLabel lbl = new JLabel(label);
        lbl.setPreferredSize(new Dimension(130, 30));
        p.add(lbl, BorderLayout.WEST);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    private void loadLoaiSanPham() {
        dsLoai = spDAO.getAllLoaiSanPham();
        cboLoai.removeAllItems();
        for (LoaiSanPham lsp : dsLoai) {
            cboLoai.addItem(lsp.getTenLoai());
        }
    }

    private void fillForm(SanPham sp) {
        txtTenSP.setText(sp.getTenSP());
        txtSoLuong.setText(String.valueOf(sp.getSoLuong()));
        txtDonGia.setText(String.valueOf(sp.getDonGia()));
        txtMoTa.setText(sp.getMoTa());
        // chọn loại đúng
        for (int i = 0; i < dsLoai.size(); i++) {
            if (dsLoai.get(i).getMaLoai().equals(sp.getLoaiSP().getMaLoai())) {
                cboLoai.setSelectedIndex(i);
                break;
            }
        }
        //hiển thị ảnh
        if (sp.getImg() != null && !sp.getImg().isEmpty()) {
            try {
                File f = new File(sp.getImg());
                ImageIcon icon;

                if (f.exists()) {
                    //Ảnh nằm ngoài ổ đĩa (đường dẫn tuyệt đối)
                    icon = new ImageIcon(f.getAbsolutePath());
                } else {
                    //Ảnh nằm trong thư mục resources /img/
                    java.net.URL url = getClass().getResource("/img/" + sp.getImg());
                    if (url != null)
                        icon = new ImageIcon(url);
                    else
                        throw new Exception("Không tìm thấy ảnh");
                }

                Image scaled = icon.getImage().getScaledInstance(400, 250, Image.SCALE_SMOOTH);
                lblAnh.setIcon(new ImageIcon(scaled));
                lblAnh.setText("");
                lblAnh.repaint();

            } catch (Exception ex) {
                lblAnh.setText("Không tìm thấy ảnh");
                lblAnh.setOpaque(true);
                lblAnh.setBackground(Color.LIGHT_GRAY);
            }
        } else {
            lblAnh.setText("Không có ảnh");
            lblAnh.setOpaque(true);
            lblAnh.setBackground(Color.LIGHT_GRAY);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o.equals(btnHuy)) {
            dispose();
        } 
        else if (o.equals(btnChonAnh)) {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Chọn ảnh sản phẩm");
            chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Hình ảnh", "jpg", "png", "jpeg"));
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                fileAnh = chooser.getSelectedFile();
                ImageIcon icon = new ImageIcon(fileAnh.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(400, 250, Image.SCALE_SMOOTH);
                lblAnh.setIcon(new ImageIcon(img));
            }
        } 
        
        else if (o.equals(btnLuu)) {
            try {
                String ten = txtTenSP.getText().trim();
                int soLuong = Integer.parseInt(txtSoLuong.getText().trim());
                double donGia = Double.parseDouble(txtDonGia.getText().trim());
                String moTa = txtMoTa.getText().trim();

                int index = cboLoai.getSelectedIndex();
                if (index < 0) {
                    JOptionPane.showMessageDialog(this, "Vui lòng chọn loại sản phẩm!");
                    return;
                }
                LoaiSanPham loai = dsLoai.get(index);
                String pathAnh = null;
                if (fileAnh != null) {
                    // chỉ lưu tên file, ví dụ "trasua.png"
                    pathAnh = fileAnh.getName();
                } else if (spSua != null) {
                    // đang sửa mà không chọn ảnh mới -> giữ ảnh cũ
                    pathAnh = spSua.getImg();
                }

                boolean ok;
                if (spSua == null) {
                    // thêm
                    SanPham spMoi = new SanPham(null, ten, soLuong, donGia, pathAnh, loai, moTa);
                    ok = spDAO.themSanPham(spMoi);
                } else {
                    // sửa
                    spSua.setTenSP(ten);
                    spSua.setSoLuong(soLuong);
                    spSua.setDonGia(donGia);
                    spSua.setMoTa(moTa);
                    spSua.setLoaiSP(loai);
                    spSua.setImg(pathAnh);
                    ok = spDAO.suaSanPham(spSua);
                }

                if (ok) {
                    JOptionPane.showMessageDialog(this,
                            (spSua == null ? "Thêm" : "Cập nhật") + " sản phẩm thành công!");
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this,
                            (spSua == null ? "Thêm" : "Cập nhật") + " sản phẩm thất bại!");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đúng định dạng dữ liệu!");
            }
        }

    }

    public static void main(String[] args) {
        ConnectDB.getInstance().connect();
        new ThemSanPham_GUI(null).setVisible(true);
    }
}
