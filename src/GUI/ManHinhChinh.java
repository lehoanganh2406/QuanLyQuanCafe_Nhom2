package GUI;

import GUI_Login.Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ManHinhChinh extends JFrame implements ActionListener {

    private JButton btnLogout, btnOrder, btnMenu, btnThanhVien;

    public ManHinhChinh() {
        setTitle("Coffee & Food & Tea");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(false);
        setLayout(new BorderLayout());

        Color brown = new Color(120, 74, 57);
        Color beige = new Color(227, 202, 183);
        Color lightGray = new Color(240, 240, 240);

        // ======= THANH TRÊN =======
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(brown);
        topPanel.setPreferredSize(new Dimension(0, 60));

        JLabel lblTitle = new JLabel("  COFFEE & FOOD & TEA", JLabel.LEFT);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitle.setForeground(Color.WHITE);

        topPanel.add(lblTitle, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        // ======= THANH BÊN TRÁI =======
        JPanel leftPanel = new JPanel();
        leftPanel.setBackground(lightGray);
        leftPanel.setPreferredSize(new Dimension(220, 0));
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));

        // === Logo ===
        ImageIcon icon = new ImageIcon("src/img/logocafe.png");
        Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaledImage);

        JLabel lblLogo = new JLabel("COFFEE", icon, JLabel.CENTER);
        lblLogo.setFont(new Font("Arial", Font.BOLD, 18));
        lblLogo.setForeground(brown);
        lblLogo.setIconTextGap(10);
        lblLogo.setHorizontalTextPosition(SwingConstants.RIGHT);
        lblLogo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);

        leftPanel.add(lblLogo);

        // === Các nút menu ===
        btnOrder = createSideButton("ORDER", brown, "src/img/order.png");
        btnMenu = createSideButton("MENU", brown, "src/img/menu.png");
        btnThanhVien = createSideButton("DS THÀNH VIÊN", brown, "src/img/user.png");
        btnLogout = createSideButton("ĐĂNG XUẤT", brown, "src/img/logout.png");

        btnOrder.addActionListener(this);
        btnMenu.addActionListener(this);
        btnThanhVien.addActionListener(this);
        btnLogout.addActionListener(this);

        leftPanel.add(btnOrder);
        leftPanel.add(btnMenu);
        leftPanel.add(btnThanhVien);
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(btnLogout);

        add(leftPanel, BorderLayout.WEST);

        // ======= KHU VỰC CHÍNH =======
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(beige);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);

        JLabel lblIcon = new JLabel("🍴", JLabel.CENTER);
        lblIcon.setFont(new Font("Arial", Font.PLAIN, 60));
        JLabel lblText = new JLabel("CHƯA CÓ ĐƠN HÀNG HIỆN HÀNH", JLabel.CENTER);
        lblText.setFont(new Font("Arial", Font.ITALIC, 14));
        lblText.setForeground(Color.DARK_GRAY);

        centerPanel.add(lblIcon, BorderLayout.CENTER);
        centerPanel.add(lblText, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createSideButton(String text, Color color, String imagePath) {
        JButton btn = new JButton(text);

        ImageIcon icon = new ImageIcon(imagePath);
        Image scaledImage = icon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        btn.setIcon(new ImageIcon(scaledImage));

        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(10);

        btn.setFont(new Font("Arial", Font.BOLD, 13));
        btn.setForeground(color);
        btn.setFocusPainted(false);
        btn.setBackground(Color.WHITE);
        btn.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)));
        btn.setPreferredSize(new Dimension(200, 45));
        btn.setMaximumSize(new Dimension(200, 45));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object o = e.getSource();

        if (o.equals(btnLogout)) {
            Logout();
        } else if (o.equals(btnOrder)) {
            openOrder();
        } else if (o.equals(btnMenu)) {
        //    openMenu();
        } else if (o.equals(btnThanhVien)) {
        //    openThanhVien();
        }
    }

    private void openOrder() {
        try {
            new Order_GUI(1).setVisible(true);
            this.dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không thể mở giao diện Order!");
            ex.printStackTrace();
        }
    }

//    private void openMenu() {
//        try {
//            new Menu_GUI().setVisible(true);
//            this.dispose();
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this, "Không thể mở giao diện Menu!");
//            ex.printStackTrace();
//        }
//    }
//
//    private void openThanhVien() {
//        try {
//            new ThanhVien_GUI().setVisible(true);
//            this.dispose();
//        } catch (Exception ex) {
//            JOptionPane.showMessageDialog(this, "Không thể mở giao diện Thành Viên!");
//            ex.printStackTrace();
//        }
//    }

    private void Logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            new Login().setVisible(true);
            this.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ManHinhChinh().setVisible(true));
    }
}
