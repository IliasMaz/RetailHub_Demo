package GUI.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import Entities.User;
import Services.UserService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class UserGUI extends JFrame {
    private JButton createButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JTextField searchField;
    private JButton showAllButton;
    private JPanel panel1;
    private JTable table1;
    private UserService userService;

    public UserGUI( UserService userService) {
        this.userService = userService;

        setTitle("RetailHub - Users Menu");
        setContentPane(panel1);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        table1.setModel(new DefaultTableModel(
                new Object[]{"ID", "Name", "Email", "Role", "Username"}, 0
        ));
        refreshTable();



        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshTable();
                searchField.setText("");
                searchField.requestFocus();
                searchField.selectAll();
                searchField.setCaretPosition(0);
                searchField.setEditable(true);
                searchButton.setEnabled(true);
                searchButton.requestFocus();
                searchButton.setFocusable(true);

            }
        });
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int row = table1.getSelectedRow();
                if (row == -1) {
                    JOptionPane.showMessageDialog(UserGUI.this
                            , "Choose customer first!");
                    return;
                }
                int id = (int) table1.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(UserGUI.this, "Delete Customer?", "Deletion", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        boolean deleted = userService.deleteUser(id);
                        if (deleted){
                            refreshTable();
                        }
                    }catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(UserGUI.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(UserGUI.this, "An unexpected error occurred during deletion: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        ex.printStackTrace();
                    }


                }}
        });

        searchButton.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);
            String text = searchField.getText().trim();
            if (!text.isEmpty()) {
                User u = userService.findUserByUsername(text);
                if (u != null) {
                    model.addRow(new Object[]{
                            u.getId(), u.getName(), u.getEmail(), u.getRole(), u.getUsername(),

                    });
                }
            }
        });

        setVisible(true);
    }
        public void refreshTable() {
            DefaultTableModel model = (DefaultTableModel) table1.getModel();
            model.setRowCount(0);
            List<User> users = userService.getAllUsers();
            for (User u : users) {
                model.addRow(new Object[]{
                        u.getId(), u.getName(), u.getEmail(), u.getRole(), u.getUsername(),
                });
            }
        }
    }

