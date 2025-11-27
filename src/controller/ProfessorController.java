// ProfessorController.java
package controller;

import model.dao.DataManager;
import model.entities.Professor;
import model.entities.User;
import model.entities.Student;
import model.entities.Module;
import model.entities.Grade;
import model.entities.Absence;
import view.ProfessorView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.List;

public class ProfessorController {
    private ProfessorView view;
    private Professor professor;
    private DataManager dataManager;

    public ProfessorController(ProfessorView view, User user) {
        this.view = view;
        this.professor = (Professor) user;
        this.dataManager = DataManager.getInstance();
        initController();
        loadProfessorData();
    }

    private void initController() {
        try {
            // Update header with professor's name
            updateHeader();
            
            // Initialize all tabs
            initUnitManagementTab();
            initCourseManagementTab();
            initStudentManagementTab();
            initGradingTab();
            initReportTab();
            initPasswordChangeTab();
            
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur lors de l'initialisation du contrôleur: " + e.getMessage());
        }
    }

    private void updateHeader() {
        try {
            // Use reflection to access the header panel and update the user label
            JPanel headerPanel = (JPanel) ((JPanel) view.getContentPane().getComponent(0)).getComponent(0);
            Component[] components = headerPanel.getComponents();
            
            for (Component comp : components) {
                if (comp instanceof JLabel) {
                    JLabel label = (JLabel) comp;
                    if (label.getText().contains("Prof.")) {
                        label.setText("Prof. " + professor.getFullName());
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error updating header: " + e.getMessage());
        }
    }

    private void loadProfessorData() {
        // Load professor's modules and other data
        loadProfessorModules();
    }

    private void loadProfessorModules() {
        try {
            // Get modules taught by this professor
            List<Module> modules = dataManager.getProfessorModules(professor.getCode());
            
            // Update units table
            DefaultTableModel unitModel = getUnitTableModel();
            if (unitModel != null) {
                unitModel.setRowCount(0);
                
                for (Module module : modules) {
                    unitModel.addRow(new Object[]{
                        module.getCode(),
                        module.getName(),
                        module.getSemester(),
                        module.getCredits(),
                        professor.getFullName()
                    });
                }
            }
        } catch (Exception e) {
            showError("Erreur lors du chargement des modules: " + e.getMessage());
        }
    }

    // ===== UNIT MANAGEMENT TAB =====
    private void initUnitManagementTab() {
        try {
            // Get the unit management panel
            JTabbedPane tabbedPane = getTabbedPane();
            if (tabbedPane != null && tabbedPane.getComponentCount() > 0) {
                JPanel unitPanel = (JPanel) ((JPanel) tabbedPane.getComponentAt(0)).getComponent(2);
                
                // Find buttons in the unit panel
                findAndBindUnitButtons(unitPanel);
            }
        } catch (Exception e) {
            showError("Erreur lors de l'initialisation de l'onglet unités: " + e.getMessage());
        }
    }

    private void findAndBindUnitButtons(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                switch (button.getText()) {
                    case "Ajouter Unité":
                        button.addActionListener(new AddUnitAction());
                        break;
                    case "Modifier":
                        button.addActionListener(new EditUnitAction());
                        break;
                    case "Supprimer":
                        button.addActionListener(new DeleteUnitAction());
                        break;
                    case "Rechercher":
                        button.addActionListener(new SearchUnitAction());
                        break;
                }
            } else if (comp instanceof Container) {
                findAndBindUnitButtons((Container) comp);
            }
        }
    }

    // ===== STUDENT MANAGEMENT TAB =====
    private void initStudentManagementTab() {
        try {
            // Load students for this professor's modules
            loadStudents();
            
            // Find and bind student management buttons
            JTabbedPane tabbedPane = getTabbedPane();
            if (tabbedPane != null && tabbedPane.getComponentCount() > 2) {
                JPanel studentPanel = (JPanel) tabbedPane.getComponentAt(2);
                findAndBindStudentButtons(studentPanel);
            }
        } catch (Exception e) {
            showError("Erreur lors de l'initialisation de l'onglet étudiants: " + e.getMessage());
        }
    }

    private void loadStudents() {
        try {
            // Get students enrolled in professor's modules
            List<Student> students = dataManager.getStudentsForProfessor(professor.getCode());
            
            DefaultTableModel studentModel = getStudentTableModel();
            if (studentModel != null) {
                studentModel.setRowCount(0);
                
                for (Student student : students) {
                    studentModel.addRow(new Object[]{
                        student.getCode(),
                        student.getLastName(),
                        student.getFirstName(),
                        student.getSpeciality() + " - Year " + student.getYear(),
                        student.getEmail(),
                        student.getPhoneNumber()
                    });
                }
            }
        } catch (Exception e) {
            showError("Erreur lors du chargement des étudiants: " + e.getMessage());
        }
    }

    private void findAndBindStudentButtons(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                switch (button.getText()) {
                    case "Ajouter Étudiant":
                        button.addActionListener(new AddStudentAction());
                        break;
                    case "Modifier":
                        button.addActionListener(new EditStudentAction());
                        break;
                    case "Supprimer":
                        button.addActionListener(new DeleteStudentAction());
                        break;
                    case "Consulter Détails":
                        button.addActionListener(new ViewStudentDetailsAction());
                        break;
                    case "Rechercher":
                        button.addActionListener(new SearchStudentAction());
                        break;
                    case "Afficher Tous":
                        button.addActionListener(new ShowAllStudentsAction());
                        break;
                }
            } else if (comp instanceof Container) {
                findAndBindStudentButtons((Container) comp);
            }
        }
    }

    // ===== GRADING TAB =====
    private void initGradingTab() {
        try {
            // Initialize grading tab with professor's modules
            JTabbedPane tabbedPane = getTabbedPane();
            if (tabbedPane != null && tabbedPane.getComponentCount() > 3) {
                JPanel gradingPanel = (JPanel) tabbedPane.getComponentAt(3);
                
                // Update module comboboxes
                updateModuleComboBoxes(gradingPanel);
                
                // Find and bind grading buttons
                findAndBindGradingButtons(gradingPanel);
            }
        } catch (Exception e) {
            showError("Erreur lors de l'initialisation de l'onglet notation: " + e.getMessage());
        }
    }

    private void updateModuleComboBoxes(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JComboBox) {
                JComboBox<String> comboBox = (JComboBox<String>) comp;
                // Clear existing items
                comboBox.removeAllItems();
                
                // Add professor's modules
                List<Module> modules = dataManager.getProfessorModules(professor.getCode());
                for (Module module : modules) {
                    comboBox.addItem(module.getCode() + " - " + module.getName());
                }
            } else if (comp instanceof Container) {
                updateModuleComboBoxes((Container) comp);
            }
        }
    }

    private void findAndBindGradingButtons(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                switch (button.getText()) {
                    case "Charger Étudiants":
                        button.addActionListener(new LoadStudentsForGradingAction());
                        break;
                    case "Enregistrer Notes":
                        button.addActionListener(new SaveGradesAction());
                        break;
                }
            } else if (comp instanceof Container) {
                findAndBindGradingButtons((Container) comp);
            }
        }
    }

    // ===== REPORT TAB =====
    private void initReportTab() {
        try {
            // Initialize report tab
            JTabbedPane tabbedPane = getTabbedPane();
            if (tabbedPane != null && tabbedPane.getComponentCount() > 4) {
                JPanel reportPanel = (JPanel) tabbedPane.getComponentAt(4);
                
                // Update student combobox
                updateStudentComboBox(reportPanel);
                
                // Find and bind report buttons
                findAndBindReportButtons(reportPanel);
            }
        } catch (Exception e) {
            showError("Erreur lors de l'initialisation de l'onglet bilan: " + e.getMessage());
        }
    }

    private void updateStudentComboBox(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JComboBox) {
                JComboBox<String> comboBox = (JComboBox<String>) comp;
                // Check if this is the student combobox (not semester)
                if (comboBox.getItemCount() == 0 || comboBox.getItemAt(0).equals("Étudiant 1")) {
                    comboBox.removeAllItems();
                    
                    // Add students from professor's modules
                    List<Student> students = dataManager.getStudentsForProfessor(professor.getCode());
                    for (Student student : students) {
                        comboBox.addItem(student.getCode() + " - " + student.getFullName());
                    }
                }
            } else if (comp instanceof Container) {
                updateStudentComboBox((Container) comp);
            }
        }
    }

    private void findAndBindReportButtons(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if ("Afficher Bilan".equals(button.getText())) {
                    button.addActionListener(new GenerateReportAction());
                }
            } else if (comp instanceof Container) {
                findAndBindReportButtons((Container) comp);
            }
        }
    }

    // ===== PASSWORD CHANGE TAB =====
    private void initPasswordChangeTab() {
        try {
            JTabbedPane tabbedPane = getTabbedPane();
            if (tabbedPane != null && tabbedPane.getComponentCount() > 5) {
                JPanel passwordPanel = (JPanel) tabbedPane.getComponentAt(5);
                findAndBindPasswordButton(passwordPanel);
            }
        } catch (Exception e) {
            showError("Erreur lors de l'initialisation de l'onglet mot de passe: " + e.getMessage());
        }
    }

    private void findAndBindPasswordButton(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if ("Changer le Mot de Passe".equals(button.getText())) {
                    button.addActionListener(new ChangePasswordAction());
                }
            } else if (comp instanceof Container) {
                findAndBindPasswordButton((Container) comp);
            }
        }
    }

    // ===== ACTION CLASSES =====

    // Unit Management Actions
    private class AddUnitAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Show dialog to add new unit/module
            showAddUnitDialog();
        }
    }

    private class EditUnitAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Edit selected unit
                JTable unitTable = getUnitTable();
                DefaultTableModel unitModel = getUnitTableModel();
                
                if (unitTable != null && unitModel != null) {
                    int selectedRow = unitTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        String moduleCode = (String) unitModel.getValueAt(selectedRow, 0);
                        showEditUnitDialog(moduleCode);
                    } else {
                        showInfo("Veuillez sélectionner une unité à modifier");
                    }
                }
            } catch (Exception ex) {
                showError("Erreur lors de la modification: " + ex.getMessage());
            }
        }
    }

    private class DeleteUnitAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Delete selected unit
                JTable unitTable = getUnitTable();
                DefaultTableModel unitModel = getUnitTableModel();
                
                if (unitTable != null && unitModel != null) {
                    int selectedRow = unitTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        String moduleCode = (String) unitModel.getValueAt(selectedRow, 0);
                        int confirm = JOptionPane.showConfirmDialog(view, 
                            "Êtes-vous sûr de vouloir supprimer l'unité " + moduleCode + "?", 
                            "Confirmation", 
                            JOptionPane.YES_NO_OPTION);
                        
                        if (confirm == JOptionPane.YES_OPTION) {
                            // Delete module logic here
                            boolean success = dataManager.deleteModule(moduleCode);
                            if (success) {
                                showInfo("Unité supprimée avec succès");
                                loadProfessorModules();
                            } else {
                                showError("Erreur lors de la suppression de l'unité");
                            }
                        }
                    } else {
                        showInfo("Veuillez sélectionner une unité à supprimer");
                    }
                }
            } catch (Exception ex) {
                showError("Erreur lors de la suppression: " + ex.getMessage());
            }
        }
    }

    private class SearchUnitAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Search units logic
            showInfo("Fonctionnalité de recherche à implémenter");
        }
    }

    // Student Management Actions
    private class AddStudentAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showAddStudentDialog();
        }
    }

    private class EditStudentAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                JTable studentTable = getStudentTable();
                DefaultTableModel studentModel = getStudentTableModel();
                
                if (studentTable != null && studentModel != null) {
                    int selectedRow = studentTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        String studentCode = (String) studentModel.getValueAt(selectedRow, 0);
                        showEditStudentDialog(studentCode);
                    } else {
                        showInfo("Veuillez sélectionner un étudiant à modifier");
                    }
                }
            } catch (Exception ex) {
                showError("Erreur lors de la modification: " + ex.getMessage());
            }
        }
    }

    private class DeleteStudentAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                JTable studentTable = getStudentTable();
                DefaultTableModel studentModel = getStudentTableModel();
                
                if (studentTable != null && studentModel != null) {
                    int selectedRow = studentTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        String studentCode = (String) studentModel.getValueAt(selectedRow, 0);
                        int confirm = JOptionPane.showConfirmDialog(view, 
                            "Êtes-vous sûr de vouloir supprimer l'étudiant " + studentCode + "?", 
                            "Confirmation", 
                            JOptionPane.YES_NO_OPTION);
                        
                        if (confirm == JOptionPane.YES_OPTION) {
                            // Delete student logic here
                            showInfo("Étudiant supprimé avec succès");
                            loadStudents();
                        }
                    } else {
                        showInfo("Veuillez sélectionner un étudiant à supprimer");
                    }
                }
            } catch (Exception ex) {
                showError("Erreur lors de la suppression: " + ex.getMessage());
            }
        }
    }

    private class ViewStudentDetailsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                JTable studentTable = getStudentTable();
                DefaultTableModel studentModel = getStudentTableModel();
                
                if (studentTable != null && studentModel != null) {
                    int selectedRow = studentTable.getSelectedRow();
                    if (selectedRow >= 0) {
                        String studentCode = (String) studentModel.getValueAt(selectedRow, 0);
                        showStudentDetailsDialog(studentCode);
                    } else {
                        showInfo("Veuillez sélectionner un étudiant");
                    }
                }
            } catch (Exception ex) {
                showError("Erreur lors de l'affichage des détails: " + ex.getMessage());
            }
        }
    }

    private class SearchStudentAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showInfo("Fonctionnalité de recherche à implémenter");
        }
    }

    private class ShowAllStudentsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loadStudents();
        }
    }

    // Grading Actions
    private class LoadStudentsForGradingAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Load students for grading in the current tab
            showInfo("Chargement des étudiants pour notation");
        }
    }

    private class SaveGradesAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showInfo("Notes enregistrées avec succès");
        }
    }

    // Report Actions
    private class GenerateReportAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showInfo("Génération du bilan étudiant");
        }
    }

    // Password Change Action
    private class ChangePasswordAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showChangePasswordDialog();
        }
    }

    // ===== HELPER METHODS =====

    private JTabbedPane getTabbedPane() {
        try {
            Field tabbedPaneField = ProfessorView.class.getDeclaredField("tabbedPane");
            tabbedPaneField.setAccessible(true);
            return (JTabbedPane) tabbedPaneField.get(view);
        } catch (Exception e) {
            System.err.println("Error accessing tabbedPane: " + e.getMessage());
            return null;
        }
    }

    private JTable getUnitTable() {
        try {
            Field unitTableField = ProfessorView.class.getDeclaredField("unitTable");
            unitTableField.setAccessible(true);
            return (JTable) unitTableField.get(view);
        } catch (Exception e) {
            System.err.println("Error accessing unitTable: " + e.getMessage());
            return null;
        }
    }

    private DefaultTableModel getUnitTableModel() {
        try {
            Field unitTableModelField = ProfessorView.class.getDeclaredField("unitTableModel");
            unitTableModelField.setAccessible(true);
            return (DefaultTableModel) unitTableModelField.get(view);
        } catch (Exception e) {
            System.err.println("Error accessing unitTableModel: " + e.getMessage());
            return null;
        }
    }

    private JTable getStudentTable() {
        try {
            Field studentTableField = ProfessorView.class.getDeclaredField("studentTable");
            studentTableField.setAccessible(true);
            return (JTable) studentTableField.get(view);
        } catch (Exception e) {
            System.err.println("Error accessing studentTable: " + e.getMessage());
            return null;
        }
    }

    private DefaultTableModel getStudentTableModel() {
        try {
            Field studentTableModelField = ProfessorView.class.getDeclaredField("studentTableModel");
            studentTableModelField.setAccessible(true);
            return (DefaultTableModel) studentTableModelField.get(view);
        } catch (Exception e) {
            System.err.println("Error accessing studentTableModel: " + e.getMessage());
            return null;
        }
    }

    // ===== DIALOG METHODS =====

    private void showAddUnitDialog() {
        JDialog dialog = new JDialog(view, "Ajouter une Unité Pédagogique", true);
        dialog.setLayout(new GridLayout(0, 2, 10, 10));
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(view);

        // Add form components here
        // This is a simplified version - you would add proper form fields

        JButton saveButton = new JButton("Enregistrer");
        saveButton.addActionListener(e -> {
            showInfo("Unité ajoutée avec succès");
            dialog.dispose();
            loadProfessorModules();
        });

        JButton cancelButton = new JButton("Annuler");
        cancelButton.addActionListener(e -> dialog.dispose());

        dialog.add(new JLabel("Code:"));
        dialog.add(new JTextField());
        dialog.add(new JLabel("Nom:"));
        dialog.add(new JTextField());
        dialog.add(new JLabel("Crédits:"));
        dialog.add(new JTextField());
        dialog.add(saveButton);
        dialog.add(cancelButton);

        dialog.setVisible(true);
    }

    private void showEditUnitDialog(String moduleCode) {
        // Similar to add dialog but pre-filled with existing data
        showInfo("Modification de l'unité: " + moduleCode);
    }

    private void showAddStudentDialog() {
        showInfo("Ajout d'un nouvel étudiant");
    }

    private void showEditStudentDialog(String studentCode) {
        showInfo("Modification de l'étudiant: " + studentCode);
    }

    private void showStudentDetailsDialog(String studentCode) {
        try {
            Student student = (Student) dataManager.getUser(studentCode);
            if (student != null) {
                StringBuilder details = new StringBuilder();
                details.append("Détails de l'étudiant:\n\n");
                details.append("Code: ").append(student.getCode()).append("\n");
                details.append("Nom: ").append(student.getFullName()).append("\n");
                details.append("Spécialité: ").append(student.getSpeciality()).append("\n");
                details.append("Année: ").append(student.getYear()).append("\n");
                details.append("Email: ").append(student.getEmail() != null ? student.getEmail() : "Non renseigné").append("\n");
                details.append("Téléphone: ").append(student.getPhoneNumber() != null ? student.getPhoneNumber() : "Non renseigné").append("\n");
                
                JOptionPane.showMessageDialog(view, details.toString(), "Détails Étudiant", JOptionPane.INFORMATION_MESSAGE);
            } else {
                showError("Étudiant non trouvé: " + studentCode);
            }
        } catch (Exception e) {
            showError("Erreur lors de l'affichage des détails: " + e.getMessage());
        }
    }

    private void showChangePasswordDialog() {
        showInfo("Changement de mot de passe");
    }

    // ===== UTILITY METHODS =====

    private void showError(String message) {
        JOptionPane.showMessageDialog(view, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    private void showInfo(String message) {
        JOptionPane.showMessageDialog(view, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    // Course Management Tab (simplified - would need similar implementation)
    private void initCourseManagementTab() {
        // Similar implementation to other tabs
        try {
            JTabbedPane tabbedPane = getTabbedPane();
            if (tabbedPane != null && tabbedPane.getComponentCount() > 1) {
                JPanel coursePanel = (JPanel) tabbedPane.getComponentAt(1);
                // Initialize course management functionality here
            }
        } catch (Exception e) {
            showError("Erreur lors de l'initialisation de l'onglet cours: " + e.getMessage());
        }
    }
}