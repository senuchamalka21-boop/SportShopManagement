package ui;

import services.*;
import utils.DataGenerator;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JLabel statusLabel;

    // Services
    private ProductService productService;
    private ClientService clientService;
    private DeliveryAgentService agentService;
    private OrderService orderService;

    public MainFrame() {
        // Initialize services
        productService = new ProductService();
        clientService = new ClientService();
        agentService = new DeliveryAgentService();
        orderService = new OrderService();

        // Load sample data
        DataGenerator.generateSampleData(productService, clientService, agentService, orderService);

        setTitle("🏏 SportShop Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 650));

        initUI();
        setVisible(true);
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sidebar = createSidebar();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(new Color(11, 26, 46));
        add(sidebar, BorderLayout.WEST);

        // Main content with CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(new Color(245, 247, 252));

        // Create panels
        mainPanel.add(new DashboardPanel(productService, clientService, agentService, orderService), "Dashboard");
        mainPanel.add(new ProductPanel(productService), "Products");
        mainPanel.add(new ClientPanel(clientService), "Clients");
        mainPanel.add(new InventoryPanel(productService), "Inventory");
        mainPanel.add(new DeliveryAgentPanel(agentService), "Agents");
        mainPanel.add(new OrderPanel(orderService, clientService, productService, agentService, this), "Orders");
        mainPanel.add(new ReportsPanel(productService, orderService), "Reports");

        add(mainPanel, BorderLayout.CENTER);

        // Status bar
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(230, 235, 245));
        statusBar.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        statusLabel = new JLabel("🏸 Ready  |  SportShop v2.0");
        statusLabel.setFont(new Font("Inter", Font.PLAIN, 12));
        statusBar.add(statusLabel, BorderLayout.WEST);
        add(statusBar, BorderLayout.SOUTH);

        cardLayout.show(mainPanel, "Dashboard");
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 16, 20, 16));

        JLabel logo = new JLabel("🏏 SPORTSHOP");
        logo.setFont(new Font("Inter", Font.BOLD, 20));
        logo.setForeground(Color.WHITE);
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidebar.add(logo);
        sidebar.add(Box.createRigidArea(new Dimension(0, 30)));

        String[][] menuItems = {
                {"📊", "Dashboard"},
                {"📦", "Products"},
                {"👥", "Clients"},
                {"📈", "Inventory"},
                {"🚚", "Agents"},
                {"🛒", "Orders"},
                {"📋", "Reports"}
        };

        for (String[] item : menuItems) {
            JButton btn = new JButton(item[0] + " " + item[1]);
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(new Dimension(180, 44));
            btn.setBackground(new Color(30, 58, 95));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(50, 80, 120), 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            btn.setFont(new Font("Inter", Font.PLAIN, 14));
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            final String cardName = item[1];
            btn.addActionListener(e -> {
                cardLayout.show(mainPanel, cardName);
                statusLabel.setText("📍 " + cardName + "  |  SportShop v2.0");
            });

            sidebar.add(btn);
            sidebar.add(Box.createRigidArea(new Dimension(0, 8)));
        }

        sidebar.add(Box.createVerticalGlue());

        JButton logout = new JButton("🚪 Logout");
        logout.setAlignmentX(Component.CENTER_ALIGNMENT);
        logout.setMaximumSize(new Dimension(180, 40));
        logout.setBackground(new Color(200, 50, 50));
        logout.setForeground(Color.WHITE);
        logout.setFocusPainted(false);
        logout.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        logout.setFont(new Font("Inter", Font.PLAIN, 14));
        logout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Exit SportShop?", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) System.exit(0);
        });
        sidebar.add(logout);

        return sidebar;
    }

    public void setStatusMessage(String message) {
        if (statusLabel != null) {
            statusLabel.setText(message);
        }
    }
}