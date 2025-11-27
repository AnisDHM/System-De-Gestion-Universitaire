// LoginController.java
package controller;

import model.dao.DataManager;
import model.entities.User;
import strategy.AuthenticationStrategy;
import strategy.StudentAuthStrategy;
import strategy.ProfessorAuthStrategy;
import strategy.ViceDeanAuthStrategy;
import view.LoginView;
import view.StudentView;
import view.ProfessorView;
import view.ViceDeanView;
import java.awt.Component;
import java.awt.Container;
import controller.StudentController;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

public class LoginController {
    private LoginView view;
    private DataManager dataManager;

    public LoginController(LoginView view) {
        this.view = view;
        this.dataManager = DataManager.getInstance();
        initController();
    }

    private void initController() {
        try {
            // Use reflection to access the private loginButton field
            Field loginButtonField = LoginView.class.getDeclaredField("loginButton");
            loginButtonField.setAccessible(true);
            JButton loginButton = (JButton) loginButtonField.get(view);
            
            // Add action listener to the login button
            loginButton.addActionListener(new LoginAction());
            
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback: try to find the button by component traversal
            findAndBindLoginButton();
        }
    }

    private void findAndBindLoginButton() {
        JPanel mainPanel = (JPanel) view.getContentPane().getComponent(0);
        findButtonInContainer(mainPanel, "Se Connecter");
    }

    private void findButtonInContainer(Container container, String buttonText) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JButton) {
                JButton button = (JButton) comp;
                if (buttonText.equals(button.getText())) {
                    button.addActionListener(new LoginAction());
                    return;
                }
            } else if (comp instanceof Container) {
                findButtonInContainer((Container) comp, buttonText);
            }
        }
    }

    private class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Use reflection to access the username and password fields
                Field userField = LoginView.class.getDeclaredField("user");
                Field passwordField = LoginView.class.getDeclaredField("password");
                
                userField.setAccessible(true);
                passwordField.setAccessible(true);
                
                JTextField usernameField = (JTextField) userField.get(view);
                JPasswordField passwordFieldObj = (JPasswordField) passwordField.get(view);
                
                String username = usernameField.getText().trim();
                String password = new String(passwordFieldObj.getPassword()).trim();

                // Validate input
                if (username.isEmpty() || password.isEmpty()) {
                    showErrorMessage("Veuillez remplir tous les champs");
                    return;
                }

                // Authenticate user
                AuthenticationResult result = authenticateUser(username, password);
                
                if (result.isSuccess()) {
                    openUserDashboard(result.getUser());
                    view.dispose();
                } else {
                    showErrorMessage(result.getMessage());
                }
                
            } catch (Exception ex) {
                ex.printStackTrace();
                showErrorMessage("Erreur lors de la connexion: " + ex.getMessage());
            }
        }
    }

    private AuthenticationResult authenticateUser(String username, String password) {
        AuthenticationStrategy strategy = getAuthStrategy(username);
        
        if (strategy == null) {
            return new AuthenticationResult(false, "Format de code utilisateur invalide", null);
        }

        if (!strategy.validateCodeFormat(username)) {
            return new AuthenticationResult(false, "Format de code invalide pour ce type d'utilisateur", null);
        }

        if (strategy.authenticate(username, password)) {
            User user = strategy.getAuthenticatedUser(username);
            return new AuthenticationResult(true, "Authentification réussie", user);
        } else {
            return new AuthenticationResult(false, "Code utilisateur ou mot de passe incorrect", null);
        }
    }

    private AuthenticationStrategy getAuthStrategy(String code) {
        if (code.matches("^1\\d{7}$")) {
            return new StudentAuthStrategy();
        } else if (code.matches("^2\\d{7}$")) {
            return new ProfessorAuthStrategy();
        } else if (code.matches("^3\\d{7}$")) {
            return new ViceDeanAuthStrategy();
        }
        return null;
    }

    private void openUserDashboard(User user) {
        switch (user.getRole()) {
            case "Student":
                openStudentDashboard(user);
                break;
            case "Professor":
                openProfessorDashboard(user);
                break;
            case "Vice Dean":
                openViceDeanDashboard(user);
                break;
            default:
                showErrorMessage("Rôle utilisateur non reconnu: " + user.getRole());
        }
    }

    private void openStudentDashboard(User user) {
        try {
            StudentView studentView = new StudentView();
            StudentController studentController = new StudentController(studentView, user);
            studentView.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorMessage("Erreur lors de l'ouverture du tableau de bord étudiant");
        }
    }

    // In LoginController.java - update the openProfessorDashboard method:
    private void openProfessorDashboard(User user) {
       try {
           ProfessorView professorView = new ProfessorView();
           ProfessorController professorController = new ProfessorController(professorView, user);
           professorView.setVisible(true);
       } catch (Exception e) {
          e.printStackTrace();
          showErrorMessage("Erreur lors de l'ouverture du tableau de bord professeur");
       }
    }

    // In LoginController.java - update the openViceDeanDashboard method:
    private void openViceDeanDashboard(User user) {
       try {
          ViceDeanView viceDeanView = new ViceDeanView();
          ViceDeanController viceDeanController = new ViceDeanController(viceDeanView, user);
          viceDeanView.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
          showErrorMessage("Erreur lors de l'ouverture du tableau de bord vice-doyen");
        }
    }

    private void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(view, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }

    // Helper class to handle authentication results
    private static class AuthenticationResult {
        private boolean success;
        private String message;
        private User user;

        public AuthenticationResult(boolean success, String message, User user) {
            this.success = success;
            this.message = message;
            this.user = user;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public User getUser() {
            return user;
        }
    }
}