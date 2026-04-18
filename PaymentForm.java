import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class PaymentForm extends JFrame {

    JTextField donationIdField, txnRefField;
    JComboBox<String> statusBox;

    public PaymentForm() {
        setTitle("Add Payment");
        setSize(400, 300);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 243, 238));

        addLabel("Donation ID:",     50, 30);
        donationIdField = addField(190, 30);

        addLabel("Status:",          50, 80);
        statusBox = new JComboBox<>(new String[]{"Success","Pending","Failed"});
        statusBox.setBounds(190, 80, 170, 30);
        statusBox.setBackground(Color.WHITE);
        add(statusBox);

        addLabel("Transaction Ref:", 50, 130);
        txnRefField = addField(190, 130);

        JButton saveBtn = new JButton("Save Payment");
        saveBtn.setBounds(110, 210, 160, 38);
        saveBtn.setBackground(new Color(232, 96, 44));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setFont(new Font("Arial", Font.BOLD, 13));
        saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(saveBtn);

        saveBtn.addActionListener(e -> savePayment());

        setVisible(true);
    }

    void savePayment() {
        if (donationIdField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Donation ID.");
            return;
        }
        try {
            Connection con = DBConnection.getConnection();
            if (con == null) return;

            // Check donation exists
            PreparedStatement check = con.prepareStatement(
                "SELECT donation_id FROM donation WHERE donation_id = ?");
            check.setInt(1, Integer.parseInt(donationIdField.getText().trim()));
            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                JOptionPane.showMessageDialog(this,
                    "Donation ID not found!\nPlease enter a valid Donation ID.");
                con.close();
                return;
            }

            // Insert payment with only existing columns
            String sql = "INSERT INTO payment(donation_id, status, transaction_ref) VALUES(?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, Integer.parseInt(donationIdField.getText().trim()));
            ps.setString(2, statusBox.getSelectedItem().toString());
            ps.setString(3, txnRefField.getText().trim());
            ps.executeUpdate();

            // Auto-generate receipt
            ResultSet keys = ps.getGeneratedKeys();
            if (keys.next()) {
                int paymentId = keys.getInt(1);
                try {
                    PreparedStatement receiptPs = con.prepareStatement(
                        "INSERT INTO receipt(payment_id, receipt_date) VALUES(?,?)");
                    receiptPs.setInt(1, paymentId);
                    receiptPs.setDate(2, new java.sql.Date(System.currentTimeMillis()));
                    receiptPs.executeUpdate();
                } catch (SQLException ex) {
                    // receipt table may have different structure, ignore
                }
            }

            JOptionPane.showMessageDialog(this, "Payment saved successfully!");
            donationIdField.setText("");
            txnRefField.setText("");
            con.close();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Donation ID must be a number.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addLabel(String text, int x, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(x, y, 140, 30);
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