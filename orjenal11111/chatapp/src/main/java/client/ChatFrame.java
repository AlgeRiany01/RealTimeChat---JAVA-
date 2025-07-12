package client;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChatFrame extends JFrame {
    private JPanel messagesPanel;
    private JScrollPane chatScroll;
    private JTextField inputField;
    private JButton sendButton, logoutButton;
    private DefaultListModel<String> userListModel;
    private JList<String> userList;
    private JTextField userSearchField;
    private JPanel titleBar;
    private JLabel titleLabel;

    private ClientSocket clientSocket;
    private String currentRoom;
    private String currentUser;
    private List<String> allUsersList = new ArrayList<>();

    private final Color primaryColor = new Color(59, 89, 152);
    private final Color secondaryColor = new Color(66, 103, 178);
    private final Color darkBg = new Color(18, 18, 18);
    private final Color lightBg = new Color(30, 30, 30);
    private final Color inputBg = new Color(40, 40, 40);
    private final Color textColor = new Color(230, 230, 230);
    private final Color accentColor = new Color(0, 168, 255);
    private final Color sendBtnHover = new Color(0, 150, 255);
    private final Color logoutRed = new Color(210, 50, 50);
    private final Color logoutHover = new Color(230, 70, 70);

    public ChatFrame(String serverIP, int serverPort, String username) {
        this.currentUser = username;
        try {
            clientSocket = new ClientSocket(serverIP, serverPort);

            if ("ENTER_USERNAME".equals(clientSocket.receiveMessage())) {
                clientSocket.sendMessage(username);
            }

            String roomsLine = clientSocket.receiveMessage();
            String[] rooms = new String[0];
            if (roomsLine.startsWith("ROOMS_LIST")) {
                String[] parts = roomsLine.split(" ", 2);
                rooms = parts.length > 1 ? parts[1].split(",") : new String[0];
            }

            currentRoom = showRoomSelectionDialog(rooms);
            if (currentRoom == null || currentRoom.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Room name cannot be empty.");
                System.exit(0);
            }

            clientSocket.receiveMessage();
            clientSocket.sendMessage(currentRoom);

            setUndecorated(true);
            setShape(new RoundRectangle2D.Double(0, 0, 1000, 700, 30, 30));
            initComponents();
            listenFromServer();

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to server.");
            System.exit(0);
        }
    }
private JButton createGradientButton(String text, Color topColor, Color bottomColor) {
    JButton button = new JButton(text) {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gp = new GradientPaint(0, 0, topColor, 0, getHeight(), bottomColor);
            g2.setPaint(gp);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            g2.setColor(new Color(0, 0, 0, 50));
            g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(getText())) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.setColor(Color.WHITE);
            g2.drawString(getText(), x, y);
            g2.dispose();
        }
    };
    button.setFont(new Font("Segoe UI", Font.BOLD, 14));
    button.setContentAreaFilled(false);
    button.setBorderPainted(false);
    button.setFocusPainted(false);
    button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
    return button;
}

    private String showRoomSelectionDialog(String[] rooms) {
        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setSize(500, 450);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());
        dialog.setUndecorated(true);
        dialog.setShape(new RoundRectangle2D.Double(0, 0, 500, 450, 30, 30));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(darkBg);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));
        
        JLabel title = new JLabel("Select or Create Chat Room");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(textColor);
        
        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Arial", Font.BOLD, 20));
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setForeground(textColor);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> System.exit(0));
        
        titlePanel.add(title, BorderLayout.WEST);
        titlePanel.add(closeButton, BorderLayout.EAST);

        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (String room : rooms) listModel.addElement(room);

        JList<String> roomList = new JList<>(listModel);
        roomList.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        roomList.setBackground(lightBg);
        roomList.setForeground(textColor);
        roomList.setSelectionBackground(accentColor);
        roomList.setSelectionForeground(Color.WHITE);
        roomList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(roomList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(lightBg);

        JPanel createPanel = new JPanel(new BorderLayout(10, 10));
        createPanel.setBackground(darkBg);
        createPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        
        JTextField newRoomField = createStyledTextField("Enter new room name...");
        newRoomField.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        JButton createButton = createGradientButton("Create Room", new Color(76, 175, 80), new Color(56, 142, 60));
        createPanel.add(newRoomField, BorderLayout.CENTER);
        createPanel.add(createButton, BorderLayout.EAST);

        JButton joinButton = createGradientButton("Join Selected", new Color(76, 175, 80), new Color(56, 142, 60));
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(darkBg);
        buttonPanel.add(joinButton);

        dialog.add(titlePanel, BorderLayout.NORTH);
        dialog.add(scrollPane, BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(darkBg);
        bottomPanel.add(createPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        dialog.add(bottomPanel, BorderLayout.SOUTH);

        final String[] selectedRoom = {null};

        createButton.addActionListener(e -> {
            String newRoom = newRoomField.getText().trim();
            if (!newRoom.isEmpty()) {
                selectedRoom[0] = newRoom;
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Please enter a room name.");
            }
        });

        joinButton.addActionListener(e -> {
            String selected = roomList.getSelectedValue();
            if (selected != null) {
                selectedRoom[0] = selected;
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a room from the list.");
            }
        });

        WindowDragger dragger = new WindowDragger(dialog);
        titlePanel.addMouseListener(dragger);
        titlePanel.addMouseMotionListener(dragger);

        dialog.setVisible(true);
        return selectedRoom[0];
    }

    private void initComponents() {
        setTitle("Chat Room - " + currentRoom);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(darkBg);
        titleBar.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));
        
        titleLabel = new JLabel(currentUser + " - Chat Room: " + currentRoom);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(textColor);
        
        JButton closeButton = new JButton("×");
        closeButton.setFont(new Font("Arial", Font.BOLD, 20));
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setForeground(textColor);
        closeButton.setFocusPainted(false);
        closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        closeButton.addActionListener(e -> System.exit(0));
        
        titleBar.add(titleLabel, BorderLayout.WEST);
        titleBar.add(closeButton, BorderLayout.EAST);
        add(titleBar, BorderLayout.NORTH);

  
        messagesPanel = new JPanel();
        messagesPanel.setLayout(new BoxLayout(messagesPanel, BoxLayout.Y_AXIS));
        messagesPanel.setBackground(darkBg);
        messagesPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        chatScroll = new JScrollPane(messagesPanel);
        chatScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        chatScroll.getVerticalScrollBar().setUnitIncrement(20);
        chatScroll.setBorder(BorderFactory.createEmptyBorder());
        chatScroll.getVerticalScrollBar().setBackground(darkBg);
        chatScroll.getVerticalScrollBar().setPreferredSize(new Dimension(10, Integer.MAX_VALUE));

        
        inputField = createStyledTextField("Type your message here...");
        inputField.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

     
        sendButton = new JButton();
sendButton.setFont(new Font("Segoe UI Symbol", Font.BOLD, 20));
sendButton.setText("\u27A4");
        sendButton.setBackground(primaryColor);
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        sendButton.setToolTipText("Send Message");
        sendButton.setPreferredSize(new Dimension(60, 45));
        sendButton.setOpaque(true);
        sendButton.setBorder(new RoundedBorderButton(22));

        sendButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                sendButton.setBackground(sendBtnHover);
            }
            public void mouseExited(MouseEvent e) {
                sendButton.setBackground(primaryColor);
            }
        });

       
        logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setBackground(logoutRed);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setBorder(new RoundedBorderButton(12));
        logoutButton.setPreferredSize(new Dimension(100, 40));
        logoutButton.setOpaque(true);

        logoutButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                logoutButton.setBackground(logoutHover);
            }
            public void mouseExited(MouseEvent e) {
                logoutButton.setBackground(logoutRed);
            }
        });


        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setBackground(lightBg);
        inputPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(60, 60, 60)));
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(sendButton, BorderLayout.EAST);

   
        userSearchField = createStyledTextField("Search members...");
        userSearchField.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));


        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setBackground(lightBg);
        userList.setForeground(new Color(0, 255, 127));
        userList.setSelectionBackground(new Color(0, 150, 80));
        userList.setFont(new Font("Segoe UI Semibold", Font.BOLD, 15));
        userList.setFixedCellHeight(30);
        userList.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 70)),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));

        JScrollPane userScroll = new JScrollPane(userList);
        userScroll.setBorder(BorderFactory.createEmptyBorder());
        userScroll.getViewport().setBackground(lightBg);

        JPanel rightPanel = new JPanel(new BorderLayout(0, 10));
        rightPanel.setBackground(lightBg);
        rightPanel.setPreferredSize(new Dimension(250, getHeight()));
        rightPanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0, new Color(60, 60, 60)));
        
        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBackground(lightBg);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        searchPanel.add(userSearchField, BorderLayout.CENTER);
        
        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        logoutPanel.setBackground(lightBg);
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        logoutPanel.add(logoutButton);
        
        rightPanel.add(searchPanel, BorderLayout.NORTH);
        rightPanel.add(userScroll, BorderLayout.CENTER);
        rightPanel.add(logoutPanel, BorderLayout.SOUTH);

        add(chatScroll, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);
        add(rightPanel, BorderLayout.EAST);

       
        userSearchField.getDocument().addDocumentListener(new DocumentListener() {
            private void filter() {
                String filterText = userSearchField.getText().toLowerCase();
                userListModel.clear();
                for (String user : allUsersList) {
                    if (user.toLowerCase().contains(filterText)) {
                        userListModel.addElement(user);
                    }
                }
            }
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
        });

        sendButton.addActionListener(e -> sendMessage());
        inputField.addActionListener(e -> sendMessage());
        logoutButton.addActionListener(e -> logout());

        WindowDragger dragger = new WindowDragger(this);
        titleBar.addMouseListener(dragger);
        titleBar.addMouseMotionListener(dragger);

        setVisible(true);
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(inputBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                g2.setColor(isFocusOwner() ? accentColor : new Color(70, 70, 70));
                g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 15, 15);
                
                super.paintComponent(g);
                
                if (getText().isEmpty() && !isFocusOwner()) {
                    g2.setColor(new Color(150, 150, 150));
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    FontMetrics fm = g2.getFontMetrics();
                    int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(placeholder, 15, y);
                }
                
                g2.dispose();
            }
        };
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setForeground(textColor);
        field.setCaretColor(textColor);
        field.setOpaque(false);
        field.setBorder(BorderFactory.createEmptyBorder());
        return field;
    }

    private void addMessageBubble(String message, boolean isSelf) {
        JPanel bubbleWrapper = new JPanel(new BorderLayout());
        bubbleWrapper.setOpaque(false);
        bubbleWrapper.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        String sender = "";
        String body = message;
        if (message.contains(":")) {
            int index = message.indexOf(":");
            sender = message.substring(0, index).trim();
            body = message.substring(index + 1).trim();
        }

        JPanel bubble = new JPanel();
        bubble.setLayout(new BoxLayout(bubble, BoxLayout.Y_AXIS));
        bubble.setBorder(BorderFactory.createEmptyBorder(12, 16, 12, 16));

        if (isSelf) {
            bubble.setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
            bubble.setBackground(primaryColor);
            bubble.setOpaque(true);
            bubble.setAlignmentX(Component.RIGHT_ALIGNMENT);
            bubble.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(20),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
            ));
            bubble.setForeground(Color.WHITE);
            bubble.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            bubble.setToolTipText("You");
        } else {
            bubble.setMaximumSize(new Dimension(720, Integer.MAX_VALUE));
            bubble.setBackground(new Color(45, 45, 45));
            bubble.setOpaque(true);
            bubble.setAlignmentX(Component.LEFT_ALIGNMENT);
            bubble.setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(20),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
            ));
            bubble.setForeground(new Color(180, 220, 255));
            bubble.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            bubble.setToolTipText(sender);
        }

        JLabel senderLabel = new JLabel(sender);
        senderLabel.setFont(new Font("Segoe UI Semibold", Font.BOLD, 14));
        senderLabel.setForeground(isSelf ? Color.WHITE : new Color(130, 200, 255));
        senderLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea msgLabel = new JTextArea(body);
        msgLabel.setWrapStyleWord(true);
        msgLabel.setLineWrap(true);
        msgLabel.setEditable(false);
        msgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        msgLabel.setForeground(isSelf ? Color.WHITE : new Color(220, 220, 220));
        msgLabel.setOpaque(false);
        msgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel timeLabel = new JLabel(java.time.LocalTime.now().withNano(0).toString());
        timeLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        timeLabel.setForeground(new Color(160, 160, 160));
        timeLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        bubble.add(senderLabel);
        bubble.add(Box.createVerticalStrut(6));
        bubble.add(msgLabel);
        bubble.add(Box.createVerticalStrut(8));
        bubble.add(timeLabel);

        if (isSelf) {
            bubbleWrapper.add(bubble, BorderLayout.EAST);
        } else {
            bubbleWrapper.add(bubble, BorderLayout.WEST);
        }

        messagesPanel.add(bubbleWrapper);
        messagesPanel.revalidate();
        JScrollBar vertical = chatScroll.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    private void addNotificationText(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(130, 130, 130));
        label.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        wrapper.add(label, BorderLayout.CENTER);
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        messagesPanel.add(wrapper);
        messagesPanel.revalidate();
    }

    private void sendMessage() {
        String msg = inputField.getText().trim();
        if (!msg.isEmpty()) {
            clientSocket.sendMessage(msg);
            inputField.setText("");
        }
    }

    private void logout() {
        clientSocket.sendMessage("/logout");
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void listenFromServer() {
        new Thread(() -> {
            try {
                String line;
                while ((line = clientSocket.receiveMessage()) != null) {
                    final String currentLine = line;

                    SwingUtilities.invokeLater(() -> {
                        if (currentLine.startsWith("NEW_MESSAGE ")) {
                            String content = currentLine.substring(12);
                            boolean isSelf = content.startsWith(currentUser + ":");
                            addMessageBubble(content, isSelf);
                        } else if (currentLine.startsWith("NOTIFICATION ")) {
                            addNotificationText("[Notice] " + currentLine.substring(13));
                        } else if (currentLine.startsWith("USERS_LIST ")) {
                            updateUsers(currentLine.substring(11));
                        } else if (currentLine.equals("OLD_MESSAGES_START")) {
                            addNotificationText("---- Previous Messages ----");
                        } else if (currentLine.equals("OLD_MESSAGES_END")) {
                            addNotificationText("---- End of History ----");
                        }
                    });
                }
            } catch (IOException e) {
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(this, "Disconnected from server.");
                    System.exit(0);
                });
            }
        }).start();
    }

    private void updateUsers(String csv) {
        allUsersList.clear();
        userListModel.clear();
        for (String user : csv.split(",")) {
            if (!user.trim().isEmpty()) {
                allUsersList.add(user.trim());
            }
        }
        userListModel.addAll(allUsersList);
    }


    private static class RoundedBorder extends AbstractBorder {
        private final int radius;

        public RoundedBorder(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(100, 100, 100, 150));
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = radius / 2;
            return insets;
        }
    }

  
    private static class RoundedBorderButton extends AbstractBorder {
        private final int radius;

        public RoundedBorderButton(int radius) {
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(60, 60, 60));
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.left = insets.top = insets.right = insets.bottom = radius / 2;
            return insets;
        }
    }

    class WindowDragger extends MouseAdapter {
        private final Component component;
        private Point mouseDownCompCoords = null;

        public WindowDragger(Component component) {
            this.component = component;
        }

        public void mouseReleased(MouseEvent e) {
            mouseDownCompCoords = null;
        }

        public void mousePressed(MouseEvent e) {
            mouseDownCompCoords = e.getPoint();
        }

        public void mouseDragged(MouseEvent e) {
            Point currCoords = e.getLocationOnScreen();
            component.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
        }
    }
}