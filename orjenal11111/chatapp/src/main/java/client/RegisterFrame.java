package client;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegisterFrame extends JFrame {
    private JTextField nameField, emailField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton loginLink;

    public RegisterFrame() {
        setTitle("Join Us");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 500, 600, 30, 30));
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(35, 35, 35));

    
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(45, 45, 45));
        titleBar.setPreferredSize(new Dimension(500, 40));

        JLabel titleLabel = new JLabel("  Join Us");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Arial", Font.BOLD, 20));
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> dispose());

        titleBar.add(titleLabel, BorderLayout.WEST);
        titleBar.add(closeButton, BorderLayout.EAST);

        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(35, 35, 35));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JLabel logoLabel = new JLabel(new ImageIcon("https://cdn-icons-png.flaticon.com/512/4406/4406227.png"));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel welcomeLabel = new JLabel("Join Us");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        nameField = createRoundedTextField();
        emailField = createRoundedTextField();
        passwordField = createRoundedPasswordField();

        registerButton = new JButton("Create Account") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(100, 149, 237), 0, getHeight(), new Color(65, 105, 225));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
            }
        };
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        registerButton.setContentAreaFilled(false);
        registerButton.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
        registerButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginLink = new JButton("<html><u>Already have an account? Login</u></html>");
        loginLink.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginLink.setForeground(new Color(200, 200, 200));
        loginLink.setContentAreaFilled(false);
        loginLink.setBorderPainted(false);
        loginLink.setFocusPainted(false);
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(logoLabel);
        mainPanel.add(welcomeLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(createFieldPanel("Full Name", nameField));
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(createFieldPanel("Email", emailField));
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(createFieldPanel("Password", passwordField));
        mainPanel.add(Box.createVerticalStrut(25));
        mainPanel.add(registerButton);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(loginLink);

        JLabel footer = new JLabel(
            "<html><div style='text-align:center;color:#7f8c8d;font-size:12px;'>© 2025 Chat Application</div></html>",
            SwingConstants.CENTER);
        footer.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        add(titleBar, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        registerButton.addActionListener(e -> saveToDatabase());
        loginLink.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        setVisible(true);
    }

    private JTextField createRoundedTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(50, 50, 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(isFocusOwner() ? new Color(100, 149, 237) : new Color(70, 70, 70));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        field.setOpaque(false);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        return field;
    }

    private JPasswordField createRoundedPasswordField() {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(50, 50, 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(isFocusOwner() ? new Color(100, 149, 237) : new Color(70, 70, 70));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        field.setOpaque(false);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        return field;
    }

private JPanel createFieldPanel(String label, JComponent field) {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(new Color(35, 35, 35));
    panel.setAlignmentX(Component.CENTER_ALIGNMENT); 

    JLabel lbl = new JLabel(label);
    lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
    lbl.setForeground(new Color(150, 150, 150));
    lbl.setAlignmentX(Component.CENTER_ALIGNMENT);  
    lbl.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));

    field.setAlignmentX(Component.CENTER_ALIGNMENT); 

    panel.add(lbl);
    panel.add(Box.createVerticalStrut(5));
    panel.add(field);

    field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

    return panel;
}



    private void saveToDatabase() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showErrorDialog("Please fill all fields");
            return;
        }

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/chat_db", "root", "");
            String sql = "INSERT INTO users (username, email, password, created_at) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, name);
            stmt.setString(2, email);
            stmt.setString(3, password);
            stmt.setString(4, createdAt);
            stmt.executeUpdate();
            stmt.close();
            conn.close();

            JOptionPane.showMessageDialog(this,
                    "<html><div style='padding:10px;font-family:Segoe UI;'>" +
                            "<h3 style='color:#2ecc71;margin-top:0;'>Success</h3>" +
                            "<p style='color:#000000;'>Account created successfully!</p>" +
                            "</div></html>",
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            SwingUtilities.invokeLater(() -> {
                new LoginFrame();
                dispose();
            });

        } catch (Exception ex) {
            showErrorDialog("Database error: " + ex.getMessage());
        }
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this,
                "<html><div style='padding:10px;font-family:Segoe UI;'>" +
                        "<h3 style='color:#ff6b6b;margin-top:0;'>Error</h3>" +
                        "<p >" + message + "</p>" +
                        "</div></html>",
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new RegisterFrame();
        });
    }
}