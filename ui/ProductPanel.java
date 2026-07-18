package ui;

import models.Product;
import services.ProductService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProductPanel extends JPanel {
    private ProductService productService;
    private DefaultTableModel tableModel;
    private JTable table;

    public ProductPanel(ProductService productService) {
        this.productService = productService;
        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));
        setBackground(new Color(245, 247, 252));

        initUI();
        refreshTable();
    }

    private void initUI() {
        // Top bar with buttons
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topBar.setBackground(new Color(245, 247, 252));

        JButton addBtn = new JButton("➕ Add Product");
        addBtn.setBackground(new Color(11, 26, 46));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(e -> showAddDialog());

        JButton editBtn = new JButton("✏️ Edit Product");
        editBtn.setBackground(new Color(11, 26, 46));
        editBtn.setForeground(Color.WHITE);
        editBtn.setFocusPainted(false);
        editBtn.addActionListener(e -> showEditDialog());

        JButton deleteBtn = new JButton("🗑️ Delete Product");
        deleteBtn.setBackground(new Color(200, 50, 50));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.addActionListener(e -> deleteProduct());

        JButton refreshBtn = new JButton("🔄 Refresh");
        refreshBtn.addActionListener(e -> refreshTable());

        topBar.add(addBtn);
        topBar.add(editBtn);
        topBar.add(deleteBtn);
        topBar.add(refreshBtn);

        add(topBar, BorderLayout.NORTH);

        // Table
        String[] columns = {"ID", "Name", "Category", "Sport", "Price (LKR)", "Stock", "Reorder", "Eco-Rating"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(32);
        table.getTableHeader().setFont(new Font("Inter", Font.BOLD, 13));
        table.setFont(new Font("Inter", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(220, 235, 255));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(210, 218, 230)));
        add(scroll, BorderLayout.CENTER);
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Product p : productService.getAllProducts()) {
            tableModel.addRow(new Object[]{
                    p.getId(), p.getName(), p.getCategory(), p.getSportType(),
                    p.getPrice(), p.getStock(), p.getReorderLevel(), p.getEcoRating()
            });
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Product", true);
        dialog.setSize(450, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 12, 6, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(15);
        JTextField categoryField = new JTextField(15);
        JTextField sportField = new JTextField(15);
        JTextField priceField = new JTextField(15);
        JTextField stockField = new JTextField(15);
        JTextField reorderField = new JTextField(15);
        JTextField ecoField = new JTextField(15);

        String[] labels = {"Name:", "Category:", "Sport Type:", "Price (LKR):", "Stock:", "Reorder Level:", "Eco-Rating:"};
        JTextField[] fields = {nameField, categoryField, sportField, priceField, stockField, reorderField, ecoField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            dialog.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            dialog.add(fields[i], gbc);
        }

        JButton saveBtn = new JButton("💾 Save Product");
        saveBtn.setBackground(new Color(11, 26, 46));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            try {
                String id = productService.generateId();
                String name = nameField.getText().trim();
                String category = categoryField.getText().trim();
                String sport = sportField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int stock = Integer.parseInt(stockField.getText().trim());
                int reorder = Integer.parseInt(reorderField.getText().trim());
                String eco = ecoField.getText().trim();

                if (name.isEmpty() || category.isEmpty() || sport.isEmpty() || eco.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "All fields must be filled!");
                    return;
                }

                productService.addProduct(new Product(id, name, category, sport, price, stock, reorder, eco));
                refreshTable();
                dialog.dispose();
                setStatus("✅ Product added: " + name);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers for price, stock, reorder.");
            }
        });

        dialog.setVisible(true);
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to edit.");
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        Product product = productService.getProductById(id);
        if (product == null) return;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Product", true);
        dialog.setSize(450, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 12, 6, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(product.getName(), 15);
        JTextField categoryField = new JTextField(product.getCategory(), 15);
        JTextField sportField = new JTextField(product.getSportType(), 15);
        JTextField priceField = new JTextField(String.valueOf(product.getPrice()), 15);
        JTextField stockField = new JTextField(String.valueOf(product.getStock()), 15);
        JTextField reorderField = new JTextField(String.valueOf(product.getReorderLevel()), 15);
        JTextField ecoField = new JTextField(product.getEcoRating(), 15);

        String[] labels = {"Name:", "Category:", "Sport Type:", "Price (LKR):", "Stock:", "Reorder Level:", "Eco-Rating:"};
        JTextField[] fields = {nameField, categoryField, sportField, priceField, stockField, reorderField, ecoField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            dialog.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            dialog.add(fields[i], gbc);
        }

        JButton updateBtn = new JButton("🔄 Update Product");
        updateBtn.setBackground(new Color(11, 26, 46));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(updateBtn, gbc);

        updateBtn.addActionListener(e -> {
            try {
                String name = nameField.getText().trim();
                String category = categoryField.getText().trim();
                String sport = sportField.getText().trim();
                double price = Double.parseDouble(priceField.getText().trim());
                int stock = Integer.parseInt(stockField.getText().trim());
                int reorder = Integer.parseInt(reorderField.getText().trim());
                String eco = ecoField.getText().trim();

                if (name.isEmpty() || category.isEmpty() || sport.isEmpty() || eco.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "All fields must be filled!");
                    return;
                }

                product.setName(name);
                product.setCategory(category);
                product.setSportType(sport);
                product.setPrice(price);
                product.setStock(stock);
                product.setReorderLevel(reorder);
                product.setEcoRating(eco);

                productService.updateProduct(product);
                refreshTable();
                dialog.dispose();
                setStatus("✅ Product updated: " + name);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Please enter valid numbers.");
            }
        });

        dialog.setVisible(true);
    }

    private void deleteProduct() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a product to delete.");
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete product '" + name + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            productService.deleteProduct(id);
            refreshTable();
            setStatus("🗑️ Product deleted: " + name);
        }
    }

    private void setStatus(String message) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (parent instanceof MainFrame) {
            ((MainFrame) parent).setStatusMessage(message);
        }
    }
}