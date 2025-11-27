package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ViceDeanView extends JFrame {
    private JTabbedPane onglets;
    private JTextField rechercheEnseignantField;
    private JTable tableEnseignants;
    private JTable tableEtudiants;
    private JComboBox<String> uniteComboBox;
    private JComboBox<String> enseignantComboBox;

    public ViceDeanView() {
        setTitle("Doyen");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        // Configuration de la fenêtre principale
        setSize(900, 700);
        setLocationRelativeTo(null);

        // Création des onglets
        onglets = new JTabbedPane();
        onglets.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Ajout des différents panels
        onglets.addTab("Affecter Unité", createAffecterUnitePanel());
        onglets.addTab("Bilan Étudiant", createBilanEtudiantPanel());
        onglets.addTab("Chercher Enseignant", createChercherEnseignantPanel());
        onglets.addTab("Octroyer Compte", createOctroyerComptePanel());
        add(onglets);
    }

    // Panel creation for " affecter une unité à un enseignant"
    private JPanel createAffecterUnitePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Titre
        JLabel titre = new JLabel("Affecter une Unité à un Enseignant");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titre.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(titre, BorderLayout.NORTH);

        // Panel central avec formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Recherche d'enseignant
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Rechercher Enseignant:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JPanel recherchePanel = new JPanel(new BorderLayout(5, 0));
        rechercheEnseignantField = new JTextField(20);
        recherchePanel.add(rechercheEnseignantField, BorderLayout.CENTER);
        JButton rechercherBtn = new JButton("Rechercher");
        rechercherBtn.setBackground(new Color(70, 130, 180));
        rechercherBtn.setForeground(Color.WHITE);
        recherchePanel.add(rechercherBtn, BorderLayout.EAST);
        formPanel.add(recherchePanel, gbc);

        // Sélection de l'unité
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("Unité à affecter:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        String[] unites = { "Module", "Module", "Module",
                "Module" };
        uniteComboBox = new JComboBox<>(unites);
        formPanel.add(uniteComboBox, gbc);

        // Bouton d'affectation
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        JButton affecterBtn = new JButton("Affecter l'Unité");
        affecterBtn.setBackground(new Color(60, 179, 113));
        affecterBtn.setForeground(Color.WHITE);
        affecterBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        affecterBtn.setPreferredSize(new Dimension(200, 35));
        formPanel.add(affecterBtn, gbc);

        panel.add(formPanel, BorderLayout.CENTER);
        return panel;
    }

    // Panel pour afficher le bilan étudiant
    private JPanel createBilanEtudiantPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Titre
        JLabel titre = new JLabel("Bilan des Étudiants");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titre.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(titre, BorderLayout.NORTH);

        // Panel de recherche
        JPanel recherchePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        recherchePanel.setBackground(Color.WHITE);
        recherchePanel.add(new JLabel("Rechercher étudiant:"));
        JTextField rechercheEtudiantField = new JTextField(20);
        recherchePanel.add(rechercheEtudiantField);
        JButton rechercherBtn = new JButton("Rechercher");
        rechercherBtn.setBackground(new Color(70, 130, 180));
        rechercherBtn.setForeground(Color.WHITE);
        recherchePanel.add(rechercherBtn);

        panel.add(recherchePanel, BorderLayout.NORTH);

        // Tableau des étudiants avec leurs bilans
        String[] colonnesEtudiants = { "ID", "Nom", "Prénom", "Moyenne", "Statut", "Dernier Accès" };
        Object[][] donneesEtudiants = {
                { "ETU001", "kkkk", "kkkk", 10, "kkkk", "kkkk" },
                { "ETU002", "kkkk", "kkkk", 10, "kkkk", "kkkk" },
                { "ETU003", "kkkk", "kkkk", 10, "kkkk", "kkkk" },
                { "ETU004", "kkkk", "kkkk", 10, "kkkk", "kkkk" },
                { "ETU005", "kkkk", "kkkk", 10, "kkkk", "kkkk" }
        };
        tableEtudiants = new JTable(new DefaultTableModel(donneesEtudiants, colonnesEtudiants));
        JScrollPane scrollEtudiants = new JScrollPane(tableEtudiants);

        panel.add(scrollEtudiants, BorderLayout.CENTER);

        // Panel des actions
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionsPanel.setBackground(Color.WHITE);
        JButton detailsBtn = new JButton("Voir Détails Complet");
        detailsBtn.setBackground(new Color(70, 130, 180));
        detailsBtn.setForeground(Color.WHITE);
        actionsPanel.add(detailsBtn);

        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Panel pour chercher un enseignant
    private JPanel createChercherEnseignantPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Titre
        JLabel titre = new JLabel("Recherche d'Enseignant");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titre.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(titre, BorderLayout.NORTH);

        // Panel de recherche avancée
        JPanel recherchePanel = new JPanel(new GridLayout(2, 2, 10, 10));
        recherchePanel.setBackground(Color.WHITE);
        recherchePanel.setBorder(BorderFactory.createTitledBorder("Critères de recherche"));

        recherchePanel.add(new JLabel("Nom:"));
        JTextField nomField = new JTextField();
        recherchePanel.add(nomField);

        recherchePanel.add(new JLabel("Spécialité:"));
        JComboBox<String> specialiteCombo = new JComboBox<>(new String[] { "Module", "Module", "Module", "Module" });
        recherchePanel.add(specialiteCombo);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        JButton rechercherBtn = new JButton("Lancer la Recherche");
        rechercherBtn.setBackground(new Color(70, 130, 180));
        rechercherBtn.setForeground(Color.WHITE);
        buttonPanel.add(rechercherBtn);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(recherchePanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);

        // Actions
        JPanel actionsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actionsPanel.setBackground(Color.WHITE);
        JButton octroyerBtn = new JButton("Octroyer un Compte");
        octroyerBtn.setBackground(new Color(60, 179, 113));
        octroyerBtn.setForeground(Color.WHITE);
        actionsPanel.add(octroyerBtn);

        panel.add(actionsPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Panel pour octroyer un compte
    private JPanel createOctroyerComptePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);

        // Titre
        JLabel titre = new JLabel("Octroyer un Compte Enseignant");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titre.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        panel.add(titre, BorderLayout.NORTH);

        // Formulaire de création de compte
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder("Nouveau Compte Enseignant"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nom d'utilisateur
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Nom d'utilisateur:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JTextField usernameField = new JTextField(20);
        formPanel.add(usernameField, gbc);

        // Mot de passe temporaire
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("Mot de passe temporaire:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        JPasswordField passwordField = new JPasswordField(20);
        formPanel.add(passwordField, gbc);

        // Rôle
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("Rôle:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        JComboBox<String> roleCombo = new JComboBox<>(new String[] { "Enseignant", "Etudiant" });
        formPanel.add(roleCombo, gbc);

        // Bouton de création
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton creerBtn = new JButton("Créer le Compte");
        creerBtn.setBackground(new Color(60, 179, 113));
        creerBtn.setForeground(Color.WHITE);
        creerBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        creerBtn.setPreferredSize(new Dimension(200, 35));

        formPanel.add(creerBtn, gbc);

        panel.add(formPanel, BorderLayout.CENTER);
        return panel;
    }

}