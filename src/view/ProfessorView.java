package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ProfessorView extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedPane;
    private JTable studentTable;
    private JTable unitTable;
    private DefaultTableModel studentTableModel;
    private DefaultTableModel unitTableModel;

    public ProfessorView() {
        setTitle("Espace Professeur");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Components Initialization
        initializeComponents();
        
        // Interface Creation
        createUI();
        
        
        //Window Centering
        setLocationRelativeTo(null);
    }
    
    // Initialization method Implementation
    private void initializeComponents() {
        tabbedPane = new JTabbedPane();
        
        // Table model for the students
        String[] studentColumns = {"Matricule", "Nom", "Prénom", "Classe", "Email", "Téléphone"};
        studentTableModel = new DefaultTableModel(studentColumns, 0);
        studentTable = new JTable(studentTableModel);
        
        // Table model for the pedagogical unities
        String[] unitColumns = {"Code", "Intitulé", "Semestre", "Crédits", "Responsable"};
        unitTableModel = new DefaultTableModel(unitColumns, 0);
        unitTable = new JTable(unitTableModel);
 
    }

    private void createUI() {
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Creation de l'en tete
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Contenu principal avec onglets
        tabbedPane.addTab("Gérer Unités Pédagogiques", createUnitManagementPanel());
        tabbedPane.addTab("Gérer Cours/TD", createCourseManagementPanel());
        tabbedPane.addTab("Gérer Étudiants", createStudentManagementPanel());
        tabbedPane.addTab("Noter Étudiants", createGradingPanel());
        tabbedPane.addTab("Établir Bilan", createReportPanel());
        tabbedPane.addTab("Changer Mot de Passe", createPasswordChangePanel());
        
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        
        add(mainPanel);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(140, 140, 140));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        headerPanel.setPreferredSize(new Dimension(100, 60));
        
        JLabel titleLabel = new JLabel("Espace Professeur");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel userLabel = new JLabel("Prof. Nom du prof connecté");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userLabel, BorderLayout.EAST);
        
        return headerPanel;
    }

    // GESTION DES UNITÉS PÉDAGOGIQUES
    private JPanel createUnitManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de recherche
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Rechercher");
        searchPanel.add(new JLabel("Rechercher:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        // Panel des boutons d'action
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Ajouter Unité");
        JButton editButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        // Table des unités pédagogiques
        JScrollPane tableScrollPane = new JScrollPane(unitTable);
        unitTable.setFillsViewportHeight(true);
        
        // Ajout des composants au panel
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    // GESTION DES COURS/TD
    private JPanel createCourseManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Gestion des Cours et TD");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        // Panel avec onglets pour Cours/TD
        JTabbedPane courseTabbedPane = new JTabbedPane();
        
        // Onglet Cours
        courseTabbedPane.addTab("Cours", createCoursePanel("Cours"));
        // Onglet TD
        courseTabbedPane.addTab("TD", createCoursePanel("TD"));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(courseTabbedPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createCoursePanel(String type) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Table pour afficher les cours/TD
        String[] columns = {"Code", "Intitulé", "Unités", "Heures", "Salle"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable table = new JTable(model);
        
        JScrollPane scrollPane = new JScrollPane(table);
        
        // Boutons d'action
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Ajouter " + type);
        JButton editButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    // GESTION DES ÉTUDIANTS
    private JPanel createStudentManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel de recherche avancée
        JPanel searchPanel = createStudentSearchPanel();
        
        // Panel des boutons d'action
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Ajouter Étudiant");
        JButton editButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        JButton viewButton = new JButton("Consulter Détails");
        
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(viewButton);
        
        // Table des étudiants
        JScrollPane tableScrollPane = new JScrollPane(studentTable);
        //studentTable.setFillsViewportHeight(true);
        
        // Ajout des composants
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(tableScrollPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createStudentSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Rechercher Étudiant"));
        
        JTextField searchField = new JTextField(20);
        JComboBox<String> searchType = new JComboBox<>(new String[]{"Par matricule", "Par nom"});
        JButton searchButton = new JButton("Rechercher");
        JButton showAllButton = new JButton("Afficher Tous");
        
        searchPanel.add(new JLabel("Type:"));
        searchPanel.add(searchType);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);
        
        return searchPanel;
    }

    // NOTATION DES ÉTUDIANTS
    private JPanel createGradingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Système de Notation des Étudiants");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        // Panel avec onglets pour différents types de notes
        JTabbedPane gradingTabbedPane = new JTabbedPane();
        
        // Onglet Contrôle
        gradingTabbedPane.addTab("Noter Contrôle", createGradingSubPanel("Contrôle"));
        // Onglet Examen
        gradingTabbedPane.addTab("Noter Examen", createGradingSubPanel("Examen"));
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(gradingTabbedPane, BorderLayout.CENTER);
        
        return panel;
    }

    private JPanel createGradingSubPanel(String gradeType) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Sélection de l'unité pédagogique
        JPanel unitSelectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JComboBox<String> unitComboBox = new JComboBox<>(new String[]{"Genie Logiciel", "Base de données"});
        JButton loadStudentsButton = new JButton("Charger Étudiants");
        
        unitSelectionPanel.add(new JLabel("Unité Pédagogique:"));
        unitSelectionPanel.add(unitComboBox);
        unitSelectionPanel.add(loadStudentsButton);
        
        // Table pour la notation
        String[] columns = {"Matricule", "Nom", "Prénom", "Note"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        JTable gradingTable = new JTable(model);
        
        JScrollPane scrollPane = new JScrollPane(gradingTable);
        
        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Enregistrer Notes");
        
        buttonPanel.add(saveButton);
        
        panel.add(unitSelectionPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    // ÉTABLIR BILAN 
    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel titleLabel = new JLabel("Établir le Bilan des Étudiants");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        
        // Panel de sélection
        JPanel selectionPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        selectionPanel.setBorder(BorderFactory.createTitledBorder("Sélection"));
        
        JComboBox<String> studentComboBox = new JComboBox<>(new String[]{"Étudiant 1", "Étudiant 2", "Étudiant 3"});
        JComboBox<String> semesterComboBox = new JComboBox<>(new String[]{"Semestre 1", "Semestre 2", "Année complète"});
        JButton displayButton = new JButton("Afficher Bilan");
        
        selectionPanel.add(new JLabel("Étudiant:"));
        selectionPanel.add(studentComboBox);
        selectionPanel.add(new JLabel("Période:"));
        selectionPanel.add(semesterComboBox);
        
        // Zone d'affichage du bilan
        JTextArea reportArea = new JTextArea(20, 50);
        reportArea.setBorder(BorderFactory.createTitledBorder("Bilan"));
        reportArea.setEditable(false);
        JScrollPane reportScrollPane = new JScrollPane(reportArea);
        
        // Panel des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(displayButton);
        
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(selectionPanel, BorderLayout.CENTER);
        panel.add(reportScrollPane, BorderLayout.SOUTH);
        panel.add(buttonPanel, BorderLayout.EAST);
        
        return panel;
    }

    // CHANGEMENT DE MOT DE PASSE
    private JPanel createPasswordChangePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("Changer le Mot de Passe");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);
        
        gbc.gridwidth = 1;
        
        // Ancien mot de passe
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Ancien mot de passe:"), gbc);
        gbc.gridx = 1;
        JPasswordField oldPasswordField = new JPasswordField(20);
        panel.add(oldPasswordField, gbc);
        
        // Nouveau mot de passe
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Nouveau mot de passe:"), gbc);
        gbc.gridx = 1;
        JPasswordField newPasswordField = new JPasswordField(20);
        panel.add(newPasswordField, gbc);
        
        // Confirmation du nouveau mot de passe
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Confirmer le mot de passe:"), gbc);
        gbc.gridx = 1;
        JPasswordField confirmPasswordField = new JPasswordField(20);
        panel.add(confirmPasswordField, gbc);
        
        // Bouton
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton changeButton = new JButton("Changer le Mot de Passe");
        panel.add(changeButton, gbc);
        
        return panel;
    }

    public static void main(String[] args) {
    	ProfessorView prof=new ProfessorView();
            prof.setVisible(true);
    }
}
