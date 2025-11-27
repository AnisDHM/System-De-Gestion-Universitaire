// StudentController.java
package controller;

import model.dao.DataManager;
import model.entities.Student;
import model.entities.User;
import model.entities.Grade;
import view.StudentView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentController {
    private StudentView view;
    private Student student;
    private DataManager dataManager;

    public StudentController(StudentView view, User user) {
        this.view = view;
        this.student = (Student) user;
        this.dataManager = DataManager.getInstance();
        initController();
        loadStudentData();
    }

    private void initController() {
        // Use reflection to access the private button since we don't have getters
        try {
            java.lang.reflect.Field buttonField = StudentView.class.getDeclaredField("consulterNotesBtn");
            buttonField.setAccessible(true);
            JButton consulterNotesBtn = (JButton) buttonField.get(view);
            
            consulterNotesBtn.addActionListener(new ConsulterNotesAction());
            
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback: find button by component traversal
            findAndBindButton();
        }
    }

    private void findAndBindButton() {
        // Alternative method to find the button
        JPanel mainPanel = (JPanel) view.getContentPane().getComponent(0);
        JPanel leftPanel = (JPanel) mainPanel.getComponent(0);
        
        findButtonInContainer(leftPanel);
    }

    private void findButtonInContainer(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if ("Consulter Notes".equals(button.getText())) {
                    button.addActionListener(new ConsulterNotesAction());
                    return;
                }
            } else if (comp instanceof Container) {
                findButtonInContainer((Container) comp);
            }
        }
    }

    private void loadStudentData() {
        try {
            // Use reflection to access the labels
            java.lang.reflect.Field nameField = StudentView.class.getDeclaredField("studentNameLabel");
            java.lang.reflect.Field idField = StudentView.class.getDeclaredField("studentIdLabel");
            
            nameField.setAccessible(true);
            idField.setAccessible(true);
            
            JLabel nameLabel = (JLabel) nameField.get(view);
            JLabel idLabel = (JLabel) idField.get(view);
            
            // Set student information
            nameLabel.setText("Nom: " + student.getFullName());
            idLabel.setText("ID_ETUDIANT: " + student.getCode());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class ConsulterNotesAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showGradesDialog();
        }
    }

    private void showGradesDialog() {
        try {
            // Get grades for this student
            List<Grade> grades = dataManager.getStudentGrades(student.getCode());
            
            if (grades.isEmpty()) {
                JOptionPane.showMessageDialog(view, 
                    "Aucune note disponible pour le moment.", 
                    "Information", 
                    JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Create grades dialog
            JDialog gradesDialog = createGradesDialog(grades);
            gradesDialog.setVisible(true);
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, 
                "Erreur lors du chargement des notes: " + e.getMessage(), 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private JDialog createGradesDialog(List<Grade> grades) {
        JDialog dialog = new JDialog(view, "Mes Notes", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(view);

        // Create title
        JLabel titleLabel = new JLabel("Notes de " + student.getFullName(), JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        dialog.add(titleLabel, BorderLayout.NORTH);

        // Create table model
        String[] columns = {"Module", "Code Module", "Note", "Type", "Date", "Mention"};
        Object[][] data = new Object[grades.size()][columns.length];
        
        double total = 0;
        for (int i = 0; i < grades.size(); i++) {
            Grade grade = grades.get(i);
            data[i][0] = getModuleName(grade.getModuleCode());
            data[i][1] = grade.getModuleCode();
            data[i][2] = String.format("%.2f/20", grade.getValue());
            data[i][3] = grade.getType();
            data[i][4] = grade.getFormattedDate();
            data[i][5] = grade.getMention();
            total += grade.getValue();
        }

        JTable table = new JTable(data, columns);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(table);
        dialog.add(scrollPane, BorderLayout.CENTER);

        // Add summary panel
        JPanel summaryPanel = new JPanel(new GridLayout(2, 2, 10, 5));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        double average = grades.isEmpty() ? 0 : total / grades.size();
        
        summaryPanel.add(new JLabel("Nombre de notes:"));
        summaryPanel.add(new JLabel(String.valueOf(grades.size())));
        summaryPanel.add(new JLabel("Moyenne générale:"));
        summaryPanel.add(new JLabel(String.format("%.2f/20", average)));
        
        dialog.add(summaryPanel, BorderLayout.SOUTH);

        return dialog;
    }

    private String getModuleName(String moduleCode) {
        try {
            model.entities.Module module = dataManager.getModule(moduleCode);
            return module != null ? module.getName() : moduleCode;
        } catch (Exception e) {
            return moduleCode;
        }
    }
}