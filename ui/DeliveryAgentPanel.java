package ui;

import models.DeliveryAgent;
import services.DeliveryAgentService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class DeliveryAgentPanel extends JPanel {
    private DeliveryAgentService agentService;
    private DefaultTableModel tableModel;
    private JTable table;

    public DeliveryAgentPanel(DeliveryAgentService agentService) {
        this.agentService = agentService;
        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));
        setBackground(new Color(245, 247, 252));

        initUI();
        refreshTable();
    }

    private void initUI() {
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topBar.setBackground(new Color(245, 247, 252));

        JButton addBtn = new JButton("➕ Add Agent");
        addBtn.setBackground(new Color(11, 26, 46));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(e -> showAddDialog());

        JButton editBtn = new JButton("✏️ Edit Agent");
        editBtn.setBackground(new Color(11, 26, 46));
        editBtn.setForeground(Color.WHITE);
        editBtn.setFocusPainted(false);
        editBtn.addActionListener(e -> showEditDialog());

        JButton deleteBtn = new JButton("🗑️ Delete Agent");
        deleteBtn.setBackground(new Color(200, 50, 50));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.addActionListener(e -> deleteAgent());

        topBar.add(addBtn);
        topBar.add(editBtn);
        topBar.add(deleteBtn);

        add(topBar, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Phone", "Vehicle", "Plate", "Zone"};
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
        for (DeliveryAgent a : agentService.getAllAgents()) {
            tableModel.addRow(new Object[]{
                    a.getId(), a.getName(), a.getPhone(), a.getVehicleType(), a.getVehiclePlate(), a.getDeliveryZone()
            });
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Delivery Agent", true);
        dialog.setSize(450, 330);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 12, 6, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(15);
        JTextField phoneField = new JTextField(15);
        JTextField vehicleField = new JTextField(15);
        JTextField plateField = new JTextField(15);
        JTextField zoneField = new JTextField(15);

        String[] labels = {"Name:", "Phone:", "Vehicle Type:", "Plate Number:", "Delivery Zone:"};
        JTextField[] fields = {nameField, phoneField, vehicleField, plateField, zoneField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            dialog.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            dialog.add(fields[i], gbc);
        }

        JButton saveBtn = new JButton("💾 Save Agent");
        saveBtn.setBackground(new Color(11, 26, 46));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            String id = agentService.generateId();
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String vehicle = vehicleField.getText().trim();
            String plate = plateField.getText().trim();
            String zone = zoneField.getText().trim();

            if (name.isEmpty() || phone.isEmpty() || vehicle.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Name, Phone and Vehicle are required!");
                return;
            }
            agentService.addAgent(new DeliveryAgent(id, name, phone, vehicle, plate, zone));
            refreshTable();
            dialog.dispose();
            setStatus("✅ Agent added: " + name);
        });

        dialog.setVisible(true);
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an agent to edit.");
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        DeliveryAgent agent = agentService.getAgentById(id);
        if (agent == null) return;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Delivery Agent", true);
        dialog.setSize(450, 330);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 12, 6, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(agent.getName(), 15);
        JTextField phoneField = new JTextField(agent.getPhone(), 15);
        JTextField vehicleField = new JTextField(agent.getVehicleType(), 15);
        JTextField plateField = new JTextField(agent.getVehiclePlate(), 15);
        JTextField zoneField = new JTextField(agent.getDeliveryZone() != null ? agent.getDeliveryZone() : "", 15);

        String[] labels = {"Name:", "Phone:", "Vehicle Type:", "Plate Number:", "Delivery Zone:"};
        JTextField[] fields = {nameField, phoneField, vehicleField, plateField, zoneField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            dialog.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            dialog.add(fields[i], gbc);
        }

        JButton updateBtn = new JButton("🔄 Update Agent");
        updateBtn.setBackground(new Color(11, 26, 46));
        updateBtn.setForeground(Color.WHITE);
        updateBtn.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(updateBtn, gbc);

        updateBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String vehicle = vehicleField.getText().trim();
            String plate = plateField.getText().trim();
            String zone = zoneField.getText().trim();

            if (name.isEmpty() || phone.isEmpty() || vehicle.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Name, Phone and Vehicle are required!");
                return;
            }

            agent.setName(name);
            agent.setPhone(phone);
            agent.setVehicleType(vehicle);
            agent.setVehiclePlate(plate);
            agent.setDeliveryZone(zone);

            agentService.updateAgent(agent);
            refreshTable();
            dialog.dispose();
            setStatus("✅ Agent updated: " + name);
        });

        dialog.setVisible(true);
    }

    private void deleteAgent() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an agent to delete.");
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete agent '" + name + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            agentService.deleteAgent(id);
            refreshTable();
            setStatus("🗑️ Agent deleted: " + name);
        }
    }

    private void setStatus(String message) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (parent instanceof MainFrame) {
            ((MainFrame) parent).setStatusMessage(message);
        }
    }
}