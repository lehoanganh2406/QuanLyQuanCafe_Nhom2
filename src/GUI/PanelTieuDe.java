package GUI;

import javax.swing.*;
import java.awt.*;
public class PanelTieuDe extends JPanel {
	public JPanel jNor;
    public JPanel jNorLeft;
    public JPanel jNorCen;
    public JButton btnMenu;
    public JLabel lblOrder;

    public PanelTieuDe(String tieuDe, String iconPath) {
        setLayout(new BorderLayout());
        setBackground(Color.decode("#865A52"));
        setPreferredSize(new Dimension(0, 60));

        // ===== Panel tổng =====
        jNor = new JPanel(new BorderLayout());
        jNor.setBackground(Color.decode("#865A52"));
        jNor.setPreferredSize(new Dimension(0, 60));

        // ===== Panel trái: nút Menu =====
        jNorLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        jNorLeft.setOpaque(false);

        ImageIcon iconMenu = new ImageIcon(
                new ImageIcon(getClass().getResource("/img/iconMenu.png"))
                        .getImage().getScaledInstance(49, 32, Image.SCALE_SMOOTH)
        );
        btnMenu = new JButton(iconMenu);
        btnMenu.setBackground(Color.decode("#865A52"));
        btnMenu.setBorderPainted(false);
        btnMenu.setFocusPainted(false);
        jNorLeft.add(btnMenu);

        // ===== Panel giữa: tiêu đề + icon =====
        jNorCen = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        jNorCen.setOpaque(false);

        ImageIcon iconOrder = new ImageIcon(
                new ImageIcon(getClass().getResource(iconPath))
                        .getImage().getScaledInstance(38, 48, Image.SCALE_SMOOTH)
        );
        lblOrder = new JLabel(tieuDe, iconOrder, SwingConstants.CENTER);
        lblOrder.setBackground(Color.decode("#E3CFC1"));
        lblOrder.setFont(new Font("Times New Roman", Font.PLAIN, 32));
        lblOrder.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        lblOrder.setForeground(Color.BLACK);
        lblOrder.setOpaque(true);
        lblOrder.setIconTextGap(10);
        lblOrder.setHorizontalAlignment(SwingConstants.LEFT);

        jNorCen.add(lblOrder);

        // ===== Thêm vào thanh =====
        jNor.add(jNorLeft, BorderLayout.WEST);
        jNor.add(jNorCen, BorderLayout.CENTER);

        add(jNor, BorderLayout.NORTH);
    }

    public JButton getBtnMenu() {
        return btnMenu;
    }

    public JLabel getLblOrder() {
        return lblOrder;
    }
}
