package ui;

import models.Client;
import services.ClientService;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClientPanel extends JPanel {
    private ClientService clientService;
    private DefaultTableModel tableModel;
    private JTable table;

    public ClientPanel(ClientService clientService) {
        this.clientService = clientService;
        setLayout(new BorderLayout(0, 12));
        setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));
        setBackground(new Color(245, 247, 252));

        initUI();
        refreshTable();
    }

    private void initUI() {
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        topBar.setBackground(new Color(245, 247, 252));

        JButton addBtn = new JButton("➕ Add Client");
        addBtn.setBackground(new Color(11, 26, 46));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.addActionListener(e -> showAddDialog());

        JButton editBtn = new JButton("✏️ Edit Client");
        editBtn.setBackground(new Color(11, 26, 46));
        editBtn.setForeground(Color.WHITE);
        editBtn.setFocusPainted(false);
        editBtn.addActionListener(e -> showEditDialog());

        JButton deleteBtn = new JButton("🗑️ Delete Client");
        deleteBtn.setBackground(new Color(200, 50, 50));
        deleteBtn.setForeground(Color.WHITE);
        deleteBtn.setFocusPainted(false);
        deleteBtn.addActionListener(e -> deleteClient());

        topBar.add(addBtn);
        topBar.add(editBtn);
        topBar.add(deleteBtn);

        add(topBar, BorderLayout.NORTH);

        String[] columns = {"ID", "Name", "Team", "Email", "Phone", "Address"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(32);
        table.getTableHeader().setFont(new Font("Inter", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(210, 218, 230)));
        add(scroll, BorderLayout.CENTER);
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (Client c : clientService.getAllClients()) {
            tableModel.addRow(new Object[]{
                    c.getId(), c.getName(), c.getTeamName(), c.getEmail(), c.getPhone(), c.getAddress()
            });
        }
    }

    private void showAddDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add New Client", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 12, 6, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(15);
        JTextField teamField = new JTextField(15);
        JTextField emailField = new JTextField(15);
        JTextField phoneField = new JTextField(15);
        JTextField addressField = new JTextField(15);

        String[] labels = {"Name:", "Team Name:", "Email:", "Phone:", "Address:"};
        JTextField[] fields = {nameField, teamField, emailField, phoneField, addressField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            dialog.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            dialog.add(fields[i], gbc);
        }

        JButton saveBtn = new JButton("💾 Save Client");
        saveBtn.setBackground(new Color(11, 26, 46));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);

        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        dialog.add(saveBtn, gbc);

        saveBtn.addActionListener(e -> {
            String id = clientService.generateId();
            String name = nameField.getText().trim();
            String team = teamField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();

            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Name and Email are required!");
                return;
            }
            clientService.addClient(new Client(id, name, email, phone, address, team));
            refreshTable();
            dialog.dispose();
            setStatus("✅ Client added: " + name);
        });

        dialog.setVisible(true);
    }

    private void showEditDialog() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a client to edit.");
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        Client client = clientService.getClientById(id);
        if (client == null) return;

        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Edit Client", true);
        dialog.setSize(450, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 12, 6, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField(client.getName(), 15);
        JTextField teamField = new JTextField(client.getTeamName() != null ? client.getTeamName() : "", 15);
        JTextField emailField = new JTextField(client.getEmail(), 15);
        JTextField phoneField = new JTextField(client.getPhone() != null ? client.getPhone() : "", 15);
        JTextField addressField = new JTextField(client.getAddress() != null ? client.getAddress() : "", 15);

        String[] labels = {"Name:", "Team Name:", "Email:", "Phone:", "Address:"};
        JTextField[] fields = {nameField, teamField, emailField, phoneField, addressField};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            dialog.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1;
            dialog.add(fields[i], gbc);
        }

        JButton updateBtn = new JButton("🔄 Update Client");
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
            String team = teamField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();

            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Name and Email are required!");
                return;
            }

            client.setName(name);
            client.setTeamName(team);
            client.setEmail(email);
            client.setPhone(phone);
            client.setAddress(address);

            clientService.updateClient(client);
            refreshTable();
            dialog.dispose();
            setStatus("✅ Client updated: " + name);
        });

        dialog.setVisible(true);
    }

    private void deleteClient() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a client to delete.");
            return;
        }

        String id = (String) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Delete client '" + name + "'?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            clientService.deleteClient(id);
            refreshTable();
            setStatus("🗑️ Client deleted: " + name);
        }
    }

    private void setStatus(String message) {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        if (parent instanceof MainFrame) {
            ((MainFrame) parent).setStatusMessage(message);
        }
    }
}