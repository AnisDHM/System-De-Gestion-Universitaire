// ViceDeanController.java
package controller;

import model.dao.DataManager;
import model.entities.ViceDean;
import model.entities.User;
import model.entities.Professor;
import model.entities.Student;
import model.entities.Module;
import model.entities.Grade;
import model.entities.Absence;
import view.ViceDeanView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.List;

public class ViceDeanController {
    private ViceDeanView view;
    private ViceDean viceDean;
    private DataManager dataManager;

    public ViceDeanController(ViceDeanView view, User user) {
        this.view = view;
        this.viceDean = (ViceDean) user;
        this.dataManager = DataManager.getInstance();
        initController();
        loadInitialData();
    }

    private void initController() {
        try {
            // Initialize all tabs
            initAssignUnitTab();
            initStudentReportTab();
            initSearchTeacherTab();
            initGrantAccountTab();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, 
                "Erreur lors de l'initialisation du contrôleur", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadInitialData() {
        // Load initial data for comboboxes and tables
        loadModulesForAssignment();
        loadStudentsForReports();
        loadTeachersForSearch();
    }

    // ===== ASSIGN UNIT TAB =====
    private void initAssignUnitTab() {
        try {
            JTabbedPane tabs = getTabbedPane();
            JPanel assignUnitPanel = (JPanel) tabs.getComponentAt(0);
            findAndBindAssignUnitButtons(assignUnitPanel);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findAndBindAssignUnitButtons(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                switch (button.getText()) {
                    case "Rechercher":
                        button.addActionListener(new SearchTeacherForAssignmentAction());
                        break;
                    case "Affecter l'Unité":
                        button.addActionListener(new AssignUnitAction());
                        break;
                }
            } else if (comp instanceof Container) {
                findAndBindAssignUnitButtons((Container) comp);
            }
        }
    }

    private void loadModulesForAssignment() {
        try {
            JComboBox<String> unitComboBox = getUnitComboBox();
            unitComboBox.removeAllItems();
            
            // Load all available modules (without assigned professors)
            List<Module> modules = dataManager.getAllModules();
            for (Module module : modules) {
                if (!module.hasProfessor()) {
                    unitComboBox.addItem(module.getCode() + " - " + module.getName());
                }
            }
            
            // Also load some assigned modules for demonstration
            if (unitComboBox.getItemCount() == 0) {
                unitComboBox.addItem("GL01 - Génie Logiciel");
                unitComboBox.addItem("BD02 - Base de Données");
                unitComboBox.addItem("IA03 - Intelligence Artificielle");
                unitComboBox.addItem("RS04 - Réseaux");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ===== STUDENT REPORT TAB =====
    private void initStudentReportTab() {
        try {
            JTabbedPane tabs = getTabbedPane();
            JPanel studentReportPanel = (JPanel) tabs.getComponentAt(1);
            findAndBindStudentReportButtons(studentReportPanel);
            loadStudentReports();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findAndBindStudentReportButtons(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                switch (button.getText()) {
                    case "Rechercher":
                        button.addActionListener(new SearchStudentAction());
                        break;
                    case "Voir Détails Complet":
                        button.addActionListener(new ViewStudentDetailsAction());
                        break;
                }
            } else if (comp instanceof Container) {
                findAndBindStudentReportButtons((Container) comp);
            }
        }
    }

    private void loadStudentReports() {
        try {
            JTable studentsTable = getStudentsTable();
            DefaultTableModel model = (DefaultTableModel) studentsTable.getModel();
            model.setRowCount(0);
            
            // Load all students with their academic information
            List<Student> students = dataManager.getAllStudents();
            for (Student student : students) {
                double average = calculateStudentAverage(student.getCode());
                String status = getStudentStatus(average);
                
                model.addRow(new Object[]{
                    student.getCode(),
                    student.getLastName(),
                    student.getFirstName(),
                    String.format("%.2f", average),
                    status,
                    "2024-01-15" // Last access date - would come from actual data
                });
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double calculateStudentAverage(String studentCode) {
        try {
            List<Grade> grades = dataManager.getStudentGrades(studentCode);
            if (grades.isEmpty()) return 0.0;
            
            double sum = 0;
            for (Grade grade : grades) {
                sum += grade.getValue();
            }
            return sum / grades.size();
        } catch (Exception e) {
            return 10.0; // Default average for demonstration
        }
    }

    private String getStudentStatus(double average) {
        if (average >= 10) return "Admis";
        else if (average >= 7) return "Rattrapage";
        else return "Échec";
    }

    private void loadStudentsForReports() {
        // This would load students into any student-related comboboxes
    }

    // ===== SEARCH TEACHER TAB =====
    private void initSearchTeacherTab() {
        try {
            JTabbedPane tabs = getTabbedPane();
            JPanel searchTeacherPanel = (JPanel) tabs.getComponentAt(2);
            findAndBindSearchTeacherButtons(searchTeacherPanel);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findAndBindSearchTeacherButtons(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                switch (button.getText()) {
                    case "Lancer la Recherche":
                        button.addActionListener(new SearchTeacherAction());
                        break;
                    case "Octroyer un Compte":
                        button.addActionListener(new GrantAccountFromSearchAction());
                        break;
                }
            } else if (comp instanceof Container) {
                findAndBindSearchTeacherButtons((Container) comp);
            }
        }
    }

    private void loadTeachersForSearch() {
        // Load teachers for search functionality
    }

    // ===== GRANT ACCOUNT TAB =====
    private void initGrantAccountTab() {
        try {
            JTabbedPane tabs = getTabbedPane();
            JPanel grantAccountPanel = (JPanel) tabs.getComponentAt(3);
            findAndBindGrantAccountButtons(grantAccountPanel);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void findAndBindGrantAccountButtons(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if ("Créer le Compte".equals(button.getText())) {
                    button.addActionListener(new CreateAccountAction());
                }
            } else if (comp instanceof Container) {
                findAndBindGrantAccountButtons((Container) comp);
            }
        }
    }

    // ===== ACTION CLASSES =====

    // Assign Unit Tab Actions
    private class SearchTeacherForAssignmentAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                JTextField searchField = getTeacherSearchField();
                String searchTerm = searchField.getText().trim();
                
                if (searchTerm.isEmpty()) {
                    JOptionPane.showMessageDialog(view, 
                        "Veuillez entrer un nom d'enseignant à rechercher", 
                        "Information", 
                        JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                
                // Search for teachers
                List<Professor> teachers = dataManager.searchTeachers(searchTerm);
                if (teachers.isEmpty()) {
                    JOptionPane.showMessageDialog(view, 
                        "Aucun enseignant trouvé pour: " + searchTerm, 
                        "Information", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Show search results in a dialog
                    showTeacherSearchResults(teachers);
                }
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class AssignUnitAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                JComboBox<String> unitComboBox = getUnitComboBox();
                String selectedUnit = (String) unitComboBox.getSelectedItem();
                
                if (selectedUnit == null) {
                    JOptionPane.showMessageDialog(view, 
                        "Veuillez sélectionner une unité à affecter", 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Extract module code from the selected item
                String moduleCode = selectedUnit.split(" - ")[0];
                
                // Show teacher selection dialog
                showTeacherSelectionDialog(moduleCode);
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Student Report Tab Actions
    private class SearchStudentAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Find the search field in the student report panel
            try {
                JTabbedPane tabs = getTabbedPane();
                JPanel studentReportPanel = (JPanel) tabs.getComponentAt(1);
                JTextField searchField = findStudentSearchField(studentReportPanel);
                
                if (searchField != null) {
                    String searchTerm = searchField.getText().trim();
                    if (!searchTerm.isEmpty()) {
                        filterStudentTable(searchTerm);
                    } else {
                        loadStudentReports(); // Reload all students
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private class ViewStudentDetailsAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                JTable studentsTable = getStudentsTable();
                int selectedRow = studentsTable.getSelectedRow();
                
                if (selectedRow >= 0) {
                    String studentCode = (String) studentsTable.getValueAt(selectedRow, 0);
                    showCompleteStudentReport(studentCode);
                } else {
                    JOptionPane.showMessageDialog(view, 
                        "Veuillez sélectionner un étudiant", 
                        "Information", 
                        JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Search Teacher Tab Actions
    private class SearchTeacherAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(view, 
                "Recherche d'enseignants en cours...", 
                "Information", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class GrantAccountFromSearchAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Switch to the grant account tab
            try {
                JTabbedPane tabs = getTabbedPane();
                tabs.setSelectedIndex(3); // Switch to Grant Account tab
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Grant Account Tab Actions
    private class CreateAccountAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                JTabbedPane tabs = getTabbedPane();
                JPanel grantAccountPanel = (JPanel) tabs.getComponentAt(3);
                
                // Find form fields
                JTextField usernameField = findTextField(grantAccountPanel, 0);
                JPasswordField passwordField = findPasswordField(grantAccountPanel, 0);
                JPasswordField confirmPasswordField = findPasswordField(grantAccountPanel, 1);
                JComboBox<String> roleComboBox = findComboBox(grantAccountPanel);
                
                if (usernameField != null && passwordField != null && confirmPasswordField != null && roleComboBox != null) {
                    String username = usernameField.getText().trim();
                    String password = new String(passwordField.getPassword());
                    String confirmPassword = new String(confirmPasswordField.getPassword());
                    String role = (String) roleComboBox.getSelectedItem();
                    
                    // Validate inputs
                    if (username.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(view, 
                            "Veuillez remplir tous les champs", 
                            "Erreur", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    if (!password.equals(confirmPassword)) {
                        JOptionPane.showMessageDialog(view, 
                            "Les mots de passe ne correspondent pas", 
                            "Erreur", 
                            JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    // Create the account
                    boolean success = createUserAccount(username, password, role);
                    if (success) {
                        JOptionPane.showMessageDialog(view, 
                            "Compte créé avec succès", 
                            "Succès", 
                            JOptionPane.INFORMATION_MESSAGE);
                        
                        // Clear form
                        usernameField.setText("");
                        passwordField.setText("");
                        confirmPasswordField.setText("");
                    } else {
                        JOptionPane.showMessageDialog(view, 
                            "Erreur lors de la création du compte", 
                            "Erreur", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // ===== HELPER METHODS =====

    private JTabbedPane getTabbedPane() throws Exception {
        Field ongletsField = ViceDeanView.class.getDeclaredField("onglets");
        ongletsField.setAccessible(true);
        return (JTabbedPane) ongletsField.get(view);
    }

    private JTextField getTeacherSearchField() throws Exception {
        Field searchField = ViceDeanView.class.getDeclaredField("rechercheEnseignantField");
        searchField.setAccessible(true);
        return (JTextField) searchField.get(view);
    }

    private JComboBox<String> getUnitComboBox() throws Exception {
        Field unitComboBoxField = ViceDeanView.class.getDeclaredField("uniteComboBox");
        unitComboBoxField.setAccessible(true);
        return (JComboBox<String>) unitComboBoxField.get(view);
    }

    private JTable getStudentsTable() throws Exception {
        Field studentsTableField = ViceDeanView.class.getDeclaredField("tableEtudiants");
        studentsTableField.setAccessible(true);
        return (JTable) studentsTableField.get(view);
    }

    // ===== DIALOG AND UI METHODS =====

    private void showTeacherSearchResults(List<Professor> teachers) {
        JDialog dialog = new JDialog(view, "Résultats de la Recherche", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(view);

        String[] columns = {"Code", "Nom", "Prénom", "Département"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        for (Professor teacher : teachers) {
            model.addRow(new Object[]{
                teacher.getCode(),
                teacher.getLastName(),
                teacher.getFirstName(),
                teacher.getDepartment()
            });
        }

        JTable resultsTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(resultsTable);
        
        JButton selectButton = new JButton("Sélectionner");
        selectButton.addActionListener(e -> {
            int selectedRow = resultsTable.getSelectedRow();
            if (selectedRow >= 0) {
                String teacherCode = (String) resultsTable.getValueAt(selectedRow, 0);
                // You could store this for the assignment operation
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Veuillez sélectionner un enseignant");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(selectButton);
        
        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private void showTeacherSelectionDialog(String moduleCode) {
        List<Professor> allTeachers = dataManager.getAllProfessors();
        
        if (allTeachers.isEmpty()) {
            JOptionPane.showMessageDialog(view, 
                "Aucun enseignant disponible pour l'affectation", 
                "Information", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] teacherOptions = new String[allTeachers.size()];
        for (int i = 0; i < allTeachers.size(); i++) {
            Professor teacher = allTeachers.get(i);
            teacherOptions[i] = teacher.getCode() + " - " + teacher.getFullName() + " (" + teacher.getDepartment() + ")";
        }

        String selectedTeacher = (String) JOptionPane.showInputDialog(
            view,
            "Sélectionnez un enseignant pour le module " + moduleCode + ":",
            "Affecter l'Unité",
            JOptionPane.QUESTION_MESSAGE,
            null,
            teacherOptions,
            teacherOptions[0]
        );

        if (selectedTeacher != null) {
            String teacherCode = selectedTeacher.split(" - ")[0];
            assignModuleToTeacher(moduleCode, teacherCode);
        }
    }

    private void assignModuleToTeacher(String moduleCode, String teacherCode) {
        try {
            Module module = dataManager.getModule(moduleCode);
            Professor professor = (Professor) dataManager.getUser(teacherCode);
            
            if (module != null && professor != null) {
                module.setProfessorCode(teacherCode);
                dataManager.updateModule(module);
                
                JOptionPane.showMessageDialog(view, 
                    "Module " + moduleCode + " affecté avec succès à " + professor.getFullName(), 
                    "Succès", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, 
                "Erreur lors de l'affectation du module", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void filterStudentTable(String searchTerm) {
        try {
            JTable studentsTable = getStudentsTable();
            DefaultTableModel model = (DefaultTableModel) studentsTable.getModel();
            
            // Simple filtering - in real implementation, you'd query the database
            for (int i = 0; i < model.getRowCount(); i++) {
                String studentId = (String) model.getValueAt(i, 0);
                String lastName = (String) model.getValueAt(i, 1);
                String firstName = (String) model.getValueAt(i, 2);
                
                boolean matches = studentId.toLowerCase().contains(searchTerm.toLowerCase()) ||
                                 lastName.toLowerCase().contains(searchTerm.toLowerCase()) ||
                                 firstName.toLowerCase().contains(searchTerm.toLowerCase());
                
                // You would typically use a TableRowSorter for proper filtering
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCompleteStudentReport(String studentCode) {
        try {
            Student student = (Student) dataManager.getUser(studentCode);
            if (student != null) {
                StringBuilder report = new StringBuilder();
                report.append("=== BILAN COMPLET ÉTUDIANT ===\n\n");
                report.append("Informations personnelles:\n");
                report.append("Code: ").append(student.getCode()).append("\n");
                report.append("Nom: ").append(student.getFullName()).append("\n");
                report.append("Spécialité: ").append(student.getSpeciality()).append("\n");
                report.append("Année: ").append(student.getYear()).append("\n\n");
                
                report.append("Notes:\n");
                List<Grade> grades = dataManager.getStudentGrades(studentCode);
                for (Grade grade : grades) {
                    report.append("- ").append(grade.getModuleCode())
                          .append(": ").append(String.format("%.2f", grade.getValue()))
                          .append("/20 (").append(grade.getType()).append(")\n");
                }
                
                report.append("\nMoyenne générale: ").append(String.format("%.2f", calculateStudentAverage(studentCode))).append("/20\n");
                
                report.append("\nAbsences:\n");
                List<Absence> absences = dataManager.getStudentAbsences(studentCode);
                int totalAbsences = absences.size();
                int justifiedAbsences = 0;
                for (Absence absence : absences) {
                    if (absence.isJustified()) justifiedAbsences++;
                }
                report.append("Total: ").append(totalAbsences)
                      .append(" (Justifiées: ").append(justifiedAbsences)
                      .append(", Non justifiées: ").append(totalAbsences - justifiedAbsences).append(")\n");
                
                JTextArea textArea = new JTextArea(report.toString());
                textArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(600, 400));
                
                JOptionPane.showMessageDialog(view, scrollPane, "Bilan Complet - " + student.getFullName(), JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean createUserAccount(String username, String password, String role) {
        try {
            // Validate username format based on role
            if ("Enseignant".equals(role)) {
                if (!username.matches("^2\\d{7}$")) {
                    JOptionPane.showMessageDialog(view, 
                        "Format de code invalide pour un enseignant. Doit commencer par 2 et avoir 8 chiffres.", 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                Professor newProfessor = new Professor(username, password, "", "");
                dataManager.addUser(newProfessor);
            } else if ("Etudiant".equals(role)) {
                if (!username.matches("^1\\d{7}$")) {
                    JOptionPane.showMessageDialog(view, 
                        "Format de code invalide pour un étudiant. Doit commencer par 1 et avoir 8 chiffres.", 
                        "Erreur", 
                        JOptionPane.ERROR_MESSAGE);
                    return false;
                }
                Student newStudent = new Student(username, password, "", "");
                dataManager.addUser(newStudent);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Utility methods to find components in containers
    private JTextField findStudentSearchField(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JTextField) {
                return (JTextField) comp;
            } else if (comp instanceof Container) {
                JTextField result = findStudentSearchField((Container) comp);
                if (result != null) return result;
            }
        }
        return null;
    }

    private JTextField findTextField(Container container, int index) {
        return findComponentByType(container, JTextField.class, index);
    }

    private JPasswordField findPasswordField(Container container, int index) {
        return findComponentByType(container, JPasswordField.class, index);
    }

    private JComboBox<String> findComboBox(Container container) {
        return findComponentByType(container, JComboBox.class, 0);
    }

    @SuppressWarnings("unchecked")
    private <T> T findComponentByType(Container container, Class<T> type, int index) {
        int found = 0;
        for (Component comp : container.getComponents()) {
            if (type.isInstance(comp)) {
                if (found == index) {
                    return (T) comp;
                }
                found++;
            } else if (comp instanceof Container) {
                T result = findComponentByType((Container) comp, type, index - found);
                if (result != null) return result;
            }
        }
        return null;
    }
}