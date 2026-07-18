package ui;

import services.*;
import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private ProductService productService;
    private ClientService clientService;
    private DeliveryAgentService agentService;
    private OrderService orderService;

    public DashboardPanel(ProductService productService, ClientService clientService,
                          DeliveryAgentService agentService, OrderService orderService) {
        this.productService = productService;
        this.clientService = clientService;
        this.agentService = agentService;
        this.orderService = orderService;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(24, 28, 28, 28));
        setBackground(new Color(245, 247, 252));

        initUI();
    }

    private void initUI() {
        // Welcome header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(245, 247, 252));
        JLabel welcome = new JLabel("🏏 Welcome to SportShop Manager");
        welcome.setFont(new Font("Inter", Font.BOLD, 22));
        header.add(welcome, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Stats grid
        JPanel statsGrid = new JPanel(new GridLayout(2, 4, 16, 16));
        statsGrid.setBackground(new Color(245, 247, 252));

        long lowStock = productService.getLowStockProducts().size();

        String[][] stats = {
                {"📦", "Products", String.valueOf(productService.getAllProducts().size())},
                {"👥", "Clients", String.valueOf(clientService.getAllClients().size())},
                {"🚚", "Agents", String.valueOf(agentService.getAllAgents().size())},
                {"🛒", "Orders", String.valueOf(orderService.getAllOrders().size())},
                {"⚠️", "Low Stock", String.valueOf(lowStock)},
                {"💰", "Revenue", "LKR " + String.format("%.2f", orderService.getTotalRevenue())},
                {"📈", "Pending", String.valueOf(orderService.getPendingOrders().size())},
                {"✅", "Delivered", String.valueOf(orderService.getOrdersByStatus("DELIVERED").size())}
        };

        for (String[] s : stats) {
            JPanel card = new JPanel();
            card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
            card.setBackground(Color.WHITE);
            card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 225, 235), 1),
                    BorderFactory.createEmptyBorder(16, 16, 16, 16)
            ));
            card.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel icon = new JLabel(s[0]);
            icon.setFont(new Font("Segoe UI", Font.PLAIN, 28));
            icon.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel value = new JLabel(s[2]);
            value.setFont(new Font("Inter", Font.BOLD, 24));
            value.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel label = new JLabel(s[1]);
            label.setFont(new Font("Inter", Font.PLAIN, 13));
            label.setForeground(new Color(100, 110, 130));
            label.setAlignmentX(Component.CENTER_ALIGNMENT);

            card.add(icon);
            card.add(Box.createRigidArea(new Dimension(0, 4)));
            card.add(value);
            card.add(Box.createRigidArea(new Dimension(0, 2)));
            card.add(label);
            statsGrid.add(card);
        }

        add(statsGrid, BorderLayout.CENTER);
    }
}