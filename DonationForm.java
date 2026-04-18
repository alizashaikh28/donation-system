import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class DonationForm extends JFrame {

    JTextField donorIdField, amountField;
    JComboBox<String> typeBox, statusBox;

    public DonationForm() {
        setTitle("Add Donation");
        setSize(400, 350);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 243, 238));

        addLabel("Donor ID:",      50, 30);
        donorIdField = addField(180, 30);

        addLabel("Donation Type:", 50, 80);
        typeBox = new JComboBox<>(new String[]{"Money","Blood","Organ","Clothes","Food"});
        typeBox.setBounds(180, 80, 170, 30);
        typeBox.setBackground(Color.WHITE);
        add(typeBox);

        addLabel("Amount (₹):",    50, 130);
        amountField = addField(180, 130);

        addLabel("Status:",        50, 180);
        statusBox = new JComboBox<>(new String[]{"Completed","Pending","Cancelled"});
        statusBox.setBounds(180, 180, 170, 30);
        statusBox.setBackground(Color.WHITE);
        add(statusBox);

        JButton saveBtn = new JButton("Save Donation");
        saveBtn.setBounds(110, 255, 160, 38);
        saveBtn.setBackground(new Color(232, 96, 44));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setFont(new Font("Arial", Font.BOLD, 13));
        saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(saveBtn);

        saveBtn.addActionListener(e -> saveDonation());

        setVisible(true);
    }

    void saveDonation() {
        if (donorIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Donor ID.");
            return;
        }
        try {
            Connection con = DBConnection.getConnection();
            if (con == null) { return; }

            PreparedStatement check = con.prepareStatement("SELECT donor_id FROM donor WHERE donor_id = ?");
            check.setInt(1, Integer.parseInt(donorIdField.getText()));
            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "Donor ID not found! Add the donor first.");
                con.close();
                return;
            }

            String sql = "INSERT INTO donation(donor_id, donation_type, amount, donation_date, status) VALUES(?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(donorIdField.getText()));
            ps.setString(2, typeBox.getSelectedItem().toString());
            double amount = amountField.getText().trim().isEmpty() ? 0 : Double.parseDouble(amountField.getText());
            ps.setDouble(3, amount);
            ps.setDate(4, new java.sql.Date(System.currentTimeMillis()));
            ps.setString(5, statusBox.getSelectedItem().toString());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Donation recorded!");
            con.close();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Donor ID and Amount must be numbers.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 130, 30);
        lbl.setForeground(new Color(55, 65, 81));
        lbl.setFont(new Font("Arial", Font.PLAIN, 13));
        add(lbl);
    }

    private JTextField addField(int x, int y) {
        JTextField field = new JTextField();
        field.setBounds(x, y, 170, 30);
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