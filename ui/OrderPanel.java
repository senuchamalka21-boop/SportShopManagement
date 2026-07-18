package ui;

import models.*;
import services.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class OrderPanel extends JPanel {
    private OrderService orderService;
    private ClientService clientService;
    private ProductService productService;
    private DeliveryAgentService agentService;
    private MainFrame mainFrame;
    private DefaultTableModel tableModel;
    private JTable table;

    public OrderPanel(OrderService orderService, ClientService clientService,
                      ProductService productService, DeliveryAgentService agentService,
                      MainFrame mainFrame) {
        this.orderService = orderService;
        this.clientService = clientService;
        this.productService = productService;
        this.agentService = agentService;
        this.mainFrame = mainFrame;

        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));
        setBackground(new Color(245, 247, 252));

        initUI();
        refreshTable();
    }

    private void initUI() {
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topBar.setBackground(new Color(245, 247, 252));

        JButton newOrderBtn = new JButton("🛒 New Order");
        newOrderBtn.setBackground(new Color(11, 26, 46));
        newOrderBtn.setForeground(Color.WHITE);
        newOrderBtn.setFocusPainted(false);
        newOrderBtn.addActionListener(e -> showNewOrderDialog());

        JButton assignBtn = new JButton("🚚 Assign Agent");
        assignBtn.setBackground(new Color(11, 26, 46));
        assignBtn.setForeground(Color.WHITE);
        assignBtn.setFocusPainted(false);
        assignBtn.addActionListener(e -> showAssignAgentDialog());

        JButton deleteBtn = new JButton("🗑️ Delete Order");
        deleteBtn.setBackground(new Color(200, 50, 50));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.addActionListener(e -> deleteOrder());

        topBar.add(newOrderBtn);
        topBar.add(assignBtn);
        topBar.add(deleteBtn);
        add(topBar, BorderLayout.NORTH);

        String[] columns = {"Order ID", "Client", "Items", "Total (LKR)", "Status", "Agent"};
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
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Order o : orderService.getAllOrders()) {
            String clientName = clientService.getAllClients().stream()
                    .filter(c -> c.getId().equals(o.getClientId()))
                    .map(Client::getName).findFirst().orElse("Unknown");

            String agentName = "Unassigned";
            if (o.getDeliveryAgentId() != null) {
                agentName = agentService.getAllAgents().stream()
                        .filter(a -> a.getId().equals(o.getDeliveryAgentId()))
                        .map(DeliveryAgent::getName).findFirst().orElse("Unknown");
            }

            int itemCount = o.getItems().values().stream().mapToInt(Integer::intValue).sum();
            tableModel.addRow(new Object[]{
                    o.getOrderId(), clientName, itemCount, o.getTotal(), o.getStatus(), agentName
            });
        }
    }

    private void showNewOrderDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Create New Order", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 12, 6, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<Client> clientCombo = new JComboBox<>(clientService.getAllClients().toArray(new Client[0]));
        JComboBox<Product> productCombo = new JComboBox<>(productService.getAllProducts().toArray(new Product[0]));
        JTextField qtyField = new JTextField("1", 10);
        JTextArea itemsArea = new JTextArea(4, 20);
        itemsArea.setEditable(false);
        itemsArea.setBackground(new Color(245, 247, 252));

        Map<String, Integer> selectedItems = new HashMap<>();

        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Client:"), gbc);
        gbc.gridx = 1;
        dialog.add(clientCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Product:"), gbc);
        gbc.gridx = 1;
        dialog.add(productCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        dialog.add(new JLabel("Quantity:"), gbc);
        gbc.gridx = 1;
        dialog.add(qtyField, gbc);

        JButton addItemBtn = new JButton("➕ Add Item");
        addItemBtn.setBackground(new Color(11, 26, 46));
        addItemBtn.setForeground(Color.WHITE);
        addItemBtn.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        dialog.add(addItemBtn, gbc);

        gbc.gridwidth = 2;
        gbc.gridy = 4;
        dialog.add(new JLabel("Order Items:"), gbc);
        gbc.gridy = 5;
        dialog.add(new JScrollPane(itemsArea), gbc);

        addItemBtn.addActionListener(e -> {
            Product p = (Product) productCombo.getSelectedItem();
            try {
                int qty = Integer.parseInt(qtyField.getText().trim());
                if (qty <= 0) throw new NumberFormatException();
                selectedItems.put(p.getId(), selectedItems.getOrDefault(p.getId(), 0) + qty);
                StringBuilder sb = new StringBuilder();
                for (Map.Entry<String, Integer> entry : selectedItems.entrySet()) {
                    String pName = productService.getAllProducts().stream()
                            .filter(pr -> pr.getId().equals(entry.getKey()))
                            .map(Product::getName).findFirst().orElse("Unknown");
                    sb.append(pName).append(" x").append(entry.getValue()).append("\n");
                }
                itemsArea.setText(sb.toString());
                mainFrame.setStatusMessage("✅ Added " + qty + " of " + p.getName());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Enter valid quantity.");
            }
        });

        JButton placeOrderBtn = new JButton("📦 Place Order");
        placeOrderBtn.setBackground(new Color(250, 204, 21));
        placeOrderBtn.setForeground(new Color(11, 26, 46));
        placeOrderBtn.setFocusPainted(false);
        gbc.gridy = 6;
        dialog.add(placeOrderBtn, gbc);

        placeOrderBtn.addActionListener(e -> {
            if (selectedItems.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Add at least one item!");
                return;
            }

            Client client = (Client) clientCombo.getSelectedItem();
            String orderId = orderService.generateId();
            Order order = new Order(orderId, client.getId());

            double total = 0;
            for (Map.Entry<String, Integer> entry : selectedItems.entrySet()) {
                order.getItems().put(entry.getKey(), entry.getValue());
                Product p = productService.getProductById(entry.getKey());
                if (p != null) {
                    total += p.getPrice() * entry.getValue();
                    // Reduce stock
                    p.setStock(p.getStock() - entry.getValue());
                }
            }
            order.setTotal(total);
            orderService.addOrder(order);

            refreshTable();
            // Refresh product and inventory tables
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
                            } else if (child instanceof InventoryPanel) {
                                ((InventoryPanel) child).refreshTable();
                            }
                        }
                    }
                }
            }

            dialog.dispose();
            mainFrame.setStatusMessage("✅ Order " + orderId + " placed! Total: LKR " + total);

            // Simulate email notification
            JOptionPane.showMessageDialog(this,
                    "📧 Email notification sent to " + client.getEmail() + "\nOrder " + orderId + " confirmed!",
                    "Notification Sent", JOptionPane.INFORMATION_MESSAGE);
        });

        dialog.setVisible(true);
    }

    private void showAssignAgentDialog() {
        java.util.List<Order> pendingOrders = orderService.getPendingOrders();
        if (pendingOrders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No pending orders to assign.");
            return;
        }

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Assign Delivery Agent", true);
        dialog.setSize(450, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JComboBox<Order> orderCombo = new JComboBox<>(pendingOrders.toArray(new Order[0]));
        JComboBox<DeliveryAgent> agentCombo = new JComboBox<>(agentService.getAllAgents().toArray(new DeliveryAgent[0]));

        gbc.gridx = 0;
        gbc.gridy = 0;
        dialog.add(new JLabel("Order:"), gbc);
        gbc.gridx = 1;
        dialog.add(orderCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        dialog.add(new JLabel("Delivery Agent:"), gbc);
        gbc.gridx = 1;
        dialog.add(agentCombo, gbc);

        JButton assignBtn = new JButton("🚚 Assign & Dispatch");
        assignBtn.setBackground(new Color(11, 26, 46));
        assignBtn.setForeground(Color.WHITE);
        assignBtn.setFocusPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(assignBtn, gbc);

        assignBtn.addActionListener(e -> {
            Order order = (Order) orderCombo.getSelectedItem();
            DeliveryAgent agent = (DeliveryAgent) agentCombo.getSelectedItem();

            order.setDeliveryAgentId(agent.getId());
            order.setStatus("DISPATCHED");
            order.setDispatchDate(new java.util.Date());
            orderService.updateOrder(order);

            refreshTable();
            dialog.dispose();
            mainFrame.setStatusMessage("🚚 Order " + order.getOrderId() + " assigned to " + agent.getName());

            // Simulate email notifications
            String clientEmail = clientService.getAllClients().stream()
                    .filter(c -> c.getId().equals(order.getClientId()))
                    .map(Client::getEmail).findFirst().orElse("client@example.com");

            JOptionPane.showMessageDialog(this,
                    "📧 Email sent to client: " + clientEmail + "\n   Order dispatched with " + agent.getName() +
                            "\n📧 Email sent to agent: " + agent.getName() + " (" + agent.getPhone() + ")",
                    "Notifications Sent", JOptionPane.INFORMATION_MESSAGE);
        });

        dialog.setVisible(true);
    }

    private void deleteOrder() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an order to delete.");
            return;
        }

        String orderId = (String) tableModel.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete order '" + orderId + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            orderService.deleteOrder(orderId);
            refreshTable();
            mainFrame.setStatusMessage("🗑️ Order deleted: " + orderId);
        }
    }
}