package client;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import server.Database;

public class LoginFrame extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerLink;

    public LoginFrame() {
        setTitle("Login to Chat");
        setSize(500, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 500, 600, 30, 30));
        setLayout(new BorderLayout());

      
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(new Color(45, 45, 45));
        titleBar.setPreferredSize(new Dimension(500, 40));

        JLabel titleLabel = new JLabel("  Login to Chat");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Arial", Font.BOLD, 20));
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> System.exit(0));

        titleBar.add(titleLabel, BorderLayout.WEST);
        titleBar.add(closeButton, BorderLayout.EAST);

    
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(new Color(35, 35, 35));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 30, 50));

        JLabel header = new JLabel(
            "<html><div style='text-align:center;'>"
            + "<span style='font-weight:bold; color: white;'>Created by: </span>"
            + "<span style='color:#FF6B6B;'>Marwa , </span>"
            + "<span style='color:#4ECDC4;'>Menat , </span>"
            + "<span style='color:#5D9CEC;'>AlgeRiany</span>"
            + "</div></html>"
        );
        header.setHorizontalAlignment(SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        
        JLabel welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

    
        emailField = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(50, 50, 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                if (isFocusOwner()) {
                    g2.setColor(new Color(100, 149, 237));
                } else {
                    g2.setColor(new Color(70, 70, 70));
                }
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

                super.paintComponent(g);
                g2.dispose();
            }
        };
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailField.setForeground(Color.WHITE);
        emailField.setCaretColor(Color.WHITE);
        emailField.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        emailField.setOpaque(false);
        emailField.setAlignmentX(Component.LEFT_ALIGNMENT);
  
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); 

 
        passwordField = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(50, 50, 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

                if (isFocusOwner()) {
                    g2.setColor(new Color(100, 149, 237));
                } else {
                    g2.setColor(new Color(70, 70, 70));
                }
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);

                super.paintComponent(g);
                g2.dispose();
            }
        };
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        passwordField.setForeground(Color.WHITE);
        passwordField.setCaretColor(Color.WHITE);
        passwordField.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        passwordField.setOpaque(false);
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); 

        loginButton = new JButton("LOGIN") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                GradientPaint gp = new GradientPaint(0, 0,
                        new Color(100, 149, 237), 0, getHeight(),
                        new Color(65, 105, 225));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                super.paintComponent(g);
            }
        };
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setContentAreaFilled(false);
        loginButton.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        registerLink = new JButton("<html><u>Don't have an account? Sign up now</u></html>");
        registerLink.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        registerLink.setForeground(new Color(200, 200, 200));
        registerLink.setContentAreaFilled(false);
        registerLink.setBorderPainted(false);
        registerLink.setFocusPainted(false);
        registerLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        registerLink.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(header);

        mainPanel.add(welcomeLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(createFieldPanel("EMAIL", emailField));
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(createFieldPanel("PASSWORD", passwordField));
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(loginButton);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(registerLink);

        JLabel footer = new JLabel(
                "<html><div style='text-align:center;color:#7f8c8d;font-size:12px;'>"
                        + "© 2025 Chat Application"
                        + "</div></html>", SwingConstants.CENTER);
        footer.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));

        add(titleBar, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(footer, BorderLayout.SOUTH);

        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (email.isEmpty() || password.isEmpty()) {
                showErrorDialog("Please enter email and password");
            } else {
                String username = Database.getUsernameByEmail(email);
                if (username == null || username.isEmpty()) {
                    showErrorDialog("User data corrupted: username not found.");
                    return;
                }

                if (Database.validateUser(email, password)) {
                    new Thread(() -> new ChatFrame("127.0.0.1", 12345, username)).start();
                    dispose();
                } else {
                    showErrorDialog("Invalid email or password.");
                }

            }
        });

        registerLink.addActionListener(e -> {
            new RegisterFrame();
            dispose();
        });

        setVisible(true);
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


    private void showErrorDialog(String message) {
        JOptionPane pane = new JOptionPane(message, JOptionPane.ERROR_MESSAGE);
        JDialog dialog = pane.createDialog(this, "Error");
        dialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new LoginFrame();
        });
    }
}
