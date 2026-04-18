import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CampaignForm extends JFrame {

    JTextField nameField, startDateField, endDateField, targetField;
    JComboBox<String> typeBox;

    public CampaignForm() {
        setTitle("Add Campaign");
        setSize(400, 380);
        setLayout(null);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(245, 243, 238));

        addLabel("Campaign Name:", 50, 30);
        nameField = addField(190, 30);

        addLabel("Type:",          50, 80);
        typeBox = new JComboBox<>(new String[]{"Blood Drive","Food Drive","Fundraiser","Medical Aid","Other"});
        typeBox.setBounds(190, 80, 170, 30);
        typeBox.setBackground(Color.WHITE);
        add(typeBox);

        addLabel("Start Date:",    50, 130);
        startDateField = addField(190, 130);
        addHint(startDateField, "YYYY-MM-DD");

        addLabel("End Date:",      50, 180);
        endDateField = addField(190, 180);
        addHint(endDateField, "YYYY-MM-DD");

        addLabel("Target Amount:", 50, 230);
        targetField = addField(190, 230);

        JButton saveBtn = new JButton("Save Campaign");
        saveBtn.setBounds(110, 300, 160, 38);
        saveBtn.setBackground(new Color(232, 96, 44));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setFont(new Font("Arial", Font.BOLD, 13));
        saveBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        add(saveBtn);

        saveBtn.addActionListener(e -> saveCampaign());

        setVisible(true);
    }

    void saveCampaign() {
        if (nameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a campaign name.");
            return;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date startUtil = sdf.parse(startDateField.getText());
            java.util.Date endUtil   = sdf.parse(endDateField.getText());
            java.sql.Date startDate  = new java.sql.Date(startUtil.getTime());
            java.sql.Date endDate    = new java.sql.Date(endUtil.getTime());

            Connection con = DBConnection.getConnection();
            if (con == null) { return; }

            String sql = "INSERT INTO campaign(name, type, start_date, end_date, target_amount) VALUES(?,?,?,?,?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, nameField.getText());
            ps.setString(2, typeBox.getSelectedItem().toString());
            ps.setDate(3, startDate);
            ps.setDate(4, endDate);
            ps.setDouble(5, Double.parseDouble(targetField.getText()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Campaign added!");
            con.close();

        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Date format must be YYYY-MM-DD (e.g. 2024-06-15)");
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

    private void addHint(JTextField field, String hint) {
        field.setToolTipText(hint);
        field.setForeground(Color.GRAY);
        field.setText(hint);
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (field.getText().equals(hint)) {
                    field.setText("");
                    field.setForeground(new Color(55, 65, 81));
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(hint);
                }
            }
        });
    }
}