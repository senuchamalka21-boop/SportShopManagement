package ui;

import models.Product;
import services.ProductService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InventoryPanel extends JPanel {
    private ProductService productService;
    private DefaultTableModel tableModel;
    private JTable table;

    public InventoryPanel(ProductService productService) {
        this.productService = productService;
        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));
        setBackground(new Color(245, 247, 252));

        initUI();
        refreshTable();
    }

    private void initUI() {
        JLabel title = new JLabel("📈 Inventory & Low Stock Alerts");
        title.setFont(new Font("Inter", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        String[] columns = {"Product", "Category", "Stock", "Reorder Level", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(32);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(210, 218, 230)));
        add(scroll, BorderLayout.CENTER);

        JPanel bottomBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomBar.setBackground(new Color(245, 247, 252));

        JButton stockInBtn = new JButton("📦 Stock-In Products");
        stockInBtn.setBackground(new Color(11, 26, 46));
        stockInBtn.setForeground(Color.WHITE);
        stockInBtn.setFocusPainted(false);
        stockInBtn.addActionListener(e -> showStockInDialog());
        bottomBar.add(stockInBtn);

        add(bottomBar, BorderLayout.SOUTH);
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Product p : productService.getAllProducts()) {
            String status = p.getStock() <= p.getReorderLevel() ? "⚠️ LOW STOCK" : "✅ OK";
            tableModel.addRow(new Object[]{
                    p.getName(), p.getCategory(), p.getStock(), p.getReorderLevel(), status
            });
        }
    }

    private void showStockInDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Stock-In Products", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Product[] products = productService.getAllProducts().toArray(new Product[0]);
        JComboBox<Product> productCombo = new JComboBox<>(products);
        JTextField quantityField = new JTextField(10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Product:"), gbc);
        gbc.gridx = 1;
        dialog.add(productCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Quantity to add:"), gbc);
        gbc.gridx = 1;
        dialog.add(quantityField, gbc);

        JButton saveBtn = new JButton("📦 Add Stock");
        saveBtn.setBackground(new Color(11, 26, 46));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            try {
                Product p = (Product) productCombo.getSelectedItem();
                int qty = Integer.parseInt(quantityField.getText().trim());
                if (qty <= 0) throw new NumberFormatException();

                productService.addStock(p.getId(), qty);
                refreshTable();

                // Find and refresh ProductPanel
                Container parent = getParent();
                while (parent != null && !(parent instanceof JFrame)) {
                    parent = parent.getParent();
                }
                if (parent instanceof JFrame) {
                    for (Component comp : ((JFrame) parent).getContentPane().getComponents()) {
                        if (comp instanceof JPanel) {
                            for (Component child : ((JPanel) comp).getComponents()) {
                                if (child instanceof ProductPanel) {
                                    ((ProductPanel) child).refreshTable();
                                }
                            }
                        }
                    }
                }

                dialog.dispose();
                setStatus("✅ Stock added: " + qty + " of " + p.getName());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Enter a valid positive number.");
            }
        });

        dialog.setVisible(true);
    }

    private void setStatus(String message) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (parent instanceof MainFrame) {
            ((MainFrame) parent).setStatusMessage(message);
        }
    }
}