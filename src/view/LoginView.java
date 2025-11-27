package view;

import java.awt.*;
import javax.swing.*;

import javax.swing.SwingConstants;

public class LoginView extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JTextField user;
    private JPasswordField password;

    public LoginView() {
        setTitle("Connexion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false); // To make the size unmodifiable

        // Main panel with the Layout Manager BorderLayout to separate image and form
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(Color.WHITE);

        // Panel for the image (Left Panel)
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(new Color(240, 245, 250));
        leftPanel.setPreferredSize(new Dimension(350, 500));

        // Panel to center Image vertically
        JPanel imageContainer = new JPanel(new GridBagLayout());
        imageContainer.setBackground(new Color(240, 245, 250));

        // Image loading and changing size for adaption
        ImageIcon originalIcon = new ImageIcon("C:\\Users\\Admin\\Downloads\\USTHB-1.jpg");
        Image scaledImage = originalIcon.getImage().getScaledInstance(400, 400, Image.SCALE_SMOOTH);
        JLabel imageLabel = new JLabel(new ImageIcon(scaledImage));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        imageContainer.add(imageLabel);
        leftPanel.add(imageContainer, BorderLayout.CENTER);

        // Panel for the form
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(60, 50, 60, 50));

        // Title centered on the top
        JLabel titleLabel = new JLabel("Connectez-vous");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(60, 60, 60));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        // Subtitle necessary
        JLabel subtitleLabel = new JLabel("Faculté d'Informatique");
        subtitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        subtitleLabel.setForeground(new Color(120, 120, 120));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 40, 0));

        // Panel for the textFields
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setBackground(Color.WHITE);
        fieldsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Field user name
        JLabel userLabel = new JLabel("Nom d'utilisateur");
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(new Color(80, 80, 80));

        user = new JTextField();
        user.setMaximumSize(new Dimension(350, 45));
        user.setPreferredSize(new Dimension(350, 45));
        user.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)));
        user.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        user.setBackground(new Color(250, 250, 250));

        // Space between Fields
        Component verticalStrut1 = Box.createVerticalStrut(20);

        // Field password
        JLabel passLabel = new JLabel("Mot de Passe");
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passLabel.setForeground(new Color(80, 80, 80));

        password = new JPasswordField();
        password.setMaximumSize(new Dimension(350, 45));
        password.setPreferredSize(new Dimension(350, 45));
        password.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)));
        password.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        password.setBackground(new Color(250, 250, 250));

        // Space before the button
        Component verticalStrut2 = Box.createVerticalStrut(30);

        // Connection button
        JButton loginButton = new JButton("Se Connecter");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setBackground(new Color(70, 130, 240));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setBorder(BorderFactory.createEmptyBorder(15, 50, 15, 50));
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setBorderPainted(false);

        // Components Connection

        // Adding components to panel
        fieldsPanel.add(userLabel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        fieldsPanel.add(user);
        fieldsPanel.add(verticalStrut1);
        fieldsPanel.add(passLabel);
        fieldsPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        fieldsPanel.add(password);

        // Adding Components to the right Panel
        rightPanel.add(titleLabel);
        rightPanel.add(subtitleLabel);
        rightPanel.add(fieldsPanel);
        rightPanel.add(verticalStrut2);
        rightPanel.add(loginButton);

        // Final Configuration

        // Vertical Separator between the two Panels
        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setBackground(new Color(220, 220, 220));
        separator.setForeground(new Color(220, 220, 220));

        // Main Panel Connection
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(separator, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        add(mainPanel);

        // Constant Size for the frame to not modify
        setSize(800, 500);
        setLocationRelativeTo(null); // Center the Window on the screen
    }
}
