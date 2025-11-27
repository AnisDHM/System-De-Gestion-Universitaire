package view;

import javax.swing.*;
import java.awt.*;

public class StudentView extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    private JButton consulterNotesBtn;
    private JLabel studentNameLabel;
    private JLabel studentIdLabel;

    public StudentView() {
        setTitle("Profil Étudiant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Panel principal avec BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(Color.WHITE);

        // ===== PANEL GAUCHE - PROFIL ÉTUDIANT =====
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(new Color(240, 245, 250));
        leftPanel.setPreferredSize(new Dimension(300, 500));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        // Titre "Profil Étudiant" centré
        JLabel profileTitle = new JLabel("Profil Étudiant");
        profileTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        profileTitle.setForeground(new Color(60, 60, 60));
        profileTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        profileTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

        // Informations étudiant
        studentNameLabel = new JLabel("Nom: ");
        studentNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        studentNameLabel.setForeground(new Color(80, 80, 80));
        studentNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        studentIdLabel = new JLabel("ID_ETUDIANT:");
        studentIdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        studentIdLabel.setForeground(new Color(120, 120, 120));
        studentIdLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        studentIdLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 30, 0));

        // Bouton Consulter Notes avec style moderne
        consulterNotesBtn = new JButton("Consulter Notes");
        consulterNotesBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        consulterNotesBtn.setBackground(new Color(70, 130, 240));
        consulterNotesBtn.setForeground(Color.WHITE);
        consulterNotesBtn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        consulterNotesBtn.setBorder(BorderFactory.createEmptyBorder(12, 30, 12, 30));
        consulterNotesBtn.setFocusPainted(false);
        consulterNotesBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        consulterNotesBtn.setBorderPainted(false);
        consulterNotesBtn.setMaximumSize(new Dimension(200, 50));

        // Ajout des composants au panel gauche
        leftPanel.add(profileTitle);
        leftPanel.add(studentNameLabel);
        leftPanel.add(studentIdLabel);
        leftPanel.add(Box.createVerticalGlue());
        leftPanel.add(consulterNotesBtn);
        leftPanel.add(Box.createVerticalGlue());

        // ===== CONFIGURATION FINALE =====
        mainPanel.add(leftPanel, BorderLayout.CENTER);

        add(mainPanel);

        setSize(800, 500);
        setLocationRelativeTo(null);
    }

}