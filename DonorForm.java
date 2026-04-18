import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DonorForm extends JFrame {

    JTextField nameField, emailField, phoneField, addressField;
    JComboBox<String> typeBox, bloodBox;

    public DonorForm() {
        setTitle("Add New Donor");
        setSize(420, 420);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 243, 238));

        addLabel("Name:",        50, 30);
        nameField = addField(160, 30);

        addLabel("Email:",       50, 80);
        emailField = addField(160, 80);

        addLabel("Phone:",       50, 130);
        phoneField = addField(160, 130);

        addLabel("Address:",     50, 180);
        addressField = addField(160, 180);

        addLabel("Donor Type:",  50, 230);
        typeBox = new JComboBox<>(new String[]{"Individual","Organization","Blood","Organ"});
        typeBox.setBounds(160, 230, 200, 30);
        typeBox.setBackground(Color.WHITE);
        add(typeBox);

        addLabel("Blood Group:", 50, 280);
        bloodBox = new JComboBox<>(new String[]{"N/A","A+","A-","B+","B-","AB+","AB-","O+","O-"});
        bloodBox.setBounds(160, 280, 200, 30);
        bloodBox.setBackground(Color.WHITE);
        add(bloodBox);

        JButton saveBtn = new JButton("Save Donor");
        saveBtn.setBounds(130, 330, 150, 38);
        saveBtn.setBackground(new Color(232, 96, 44));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setFont(new Font("Arial", Font.BOLD, 13));
        saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(saveBtn);

        saveBtn.addActionListener(e -> saveDonor());

        setVisible(true);
    }

    void saveDonor() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a name.");
            return;
        }
        try {
            Connection con = DBConnection.getConnection();
            if (con == null) {
                JOptionPane.showMessageDialog(this, "Database connection failed!");
                return;
            }
            String sql = "INSERT INTO donor(name, email, phone, address, donor_type, blood_group) VALUES(?,?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nameField.getText());
            ps.setString(2, emailField.getText());
            ps.setString(3, phoneField.getText());
            ps.setString(4, addressField.getText());
            ps.setString(5, typeBox.getSelectedItem().toString());
            ps.setString(6, bloodBox.getSelectedItem().toString());
            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Donor added successfully!");
                clearFields();
            }
            con.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    void clearFields() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
    }

    private void addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 100, 30);
        lbl.setForeground(new Color(55, 65, 81));
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        add(lbl);
    }

    private JTextField addField(int x, int y) {
        JTextField field = new JTextField();
        field.setBounds(x, y, 200, 30);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 207, 200), 1),
            BorderFactory.createEmptyBorder(2, 8, 2, 8)
        ));
        field.setFont(new Font("Arial", Font.PLAIN, 13));
        add(field);
        return field;
    }
}