package ui;

import models.Product;
import services.ProductService;
import services.OrderService;
import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportsPanel extends JPanel {
    private ProductService productService;
    private OrderService orderService;
    private JTextArea reportArea;

    public ReportsPanel(ProductService productService, OrderService orderService) {
        this.productService = productService;
        this.orderService = orderService;

        setLayout(new BorderLayout(0, 16));
        setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));
        setBackground(new Color(245, 247, 252));

        initUI();
    }

    private void initUI() {
        JLabel title = new JLabel("📋 Monthly Sales & Inventory Reports");
        title.setFont(new Font("Inter", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        reportArea = new JTextArea();
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        reportArea.setBackground(Color.WHITE);
        reportArea.setEditable(false);
        reportArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 210, 225)),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        generateReport();

        JScrollPane scroll = new JScrollPane(reportArea);
        add(scroll, BorderLayout.CENTER);

        JButton refreshBtn = new JButton("🔄 Generate Report");
        refreshBtn.setBackground(new Color(11, 26, 46));
        refreshBtn.setForeground(Color.WHITE);
        refreshBtn.setFocusPainted(false);
        refreshBtn.addActionListener(e -> generateReport());

        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomBar.setBackground(new Color(245, 247, 252));
        bottomBar.add(refreshBtn);
        add(bottomBar, BorderLayout.SOUTH);
    }

    private void generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════════\n");
        sb.append("          SPORTSHOP MONTHLY REPORT\n");
        sb.append("          Generated: ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())).append("\n");
        sb.append("═══════════════════════════════════════════════════════════════\n\n");

        // Inventory Summary
        sb.append("📦 INVENTORY SUMMARY\n");
        sb.append("───────────────────────────────────────────────────────────────\n");
        sb.append(String.format("%-20s %-12s %-12s %s\n", "Product", "Stock", "Reorder", "Status"));
        sb.append("───────────────────────────────────────────────────────────────\n");
        for (Product p : productService.getAllProducts()) {
            String status = p.getStock() <= p.getReorderLevel() ? "⚠️ LOW" : "✅ OK";
            String name = p.getName().length() > 20 ? p.getName().substring(0, 17) + "..." : p.getName();
            sb.append(String.format("%-20s %-12d %-12d %s\n", name, p.getStock(), p.getReorderLevel(), status));
        }

        // Sales Summary
        sb.append("\n💰 SALES SUMMARY\n");
        sb.append("───────────────────────────────────────────────────────────────\n");
        double totalRevenue = orderService.getTotalRevenue();
        sb.append("Total Orders: ").append(orderService.getAllOrders().size()).append("\n");
        sb.append("Total Revenue: LKR ").append(String.format("%.2f", totalRevenue)).append("\n");
        sb.append("Pending Orders: ").append(orderService.getPendingOrders().size()).append("\n");
        sb.append("Delivered: ").append(orderService.getOrdersByStatus("DELIVERED").size()).append("\n");

        // Low Stock Alerts
        sb.append("\n📊 LOW STOCK ALERTS\n");
        sb.append("───────────────────────────────────────────────────────────────\n");
        boolean hasLow = false;
        for (Product p : productService.getLowStockProducts()) {
            sb.append("⚠️  ").append(p.getName()).append(" (Stock: ").append(p.getStock())
                    .append(", Reorder: ").append(p.getReorderLevel()).append(")\n");
            hasLow = true;
        }
        if (!hasLow) sb.append("✅ All products are well-stocked.\n");

        reportArea.setText(sb.toString());
    }
}