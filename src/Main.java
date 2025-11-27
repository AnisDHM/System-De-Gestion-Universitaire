// Main.java

import view.LoginView;
import controller.LoginController;
import model.dao.DataManager;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Set the system look and feel for better appearance
        setLookAndFeel();
        
        // Initialize the DataManager (this will load sample data)
        DataManager dataManager = DataManager.getInstance();
        
        // Print startup information
        printStartupInfo(dataManager);
        
        // Create and display the login view on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                createAndShowLoginView();
            } catch (Exception e) {
                showErrorDialog("Erreur lors du démarrage de l'application", e);
            }
        });
    }
    
    private static void setLookAndFeel() {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            // Optional: Customize some UI settings for better appearance
            customizeUISettings();
            
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du look and feel: " + e.getMessage());
            // Continue with default look and feel
        }
    }
    
    private static void customizeUISettings() {
        // Customize some UI settings for better appearance
        UIManager.put("Button.foreground", new Color(70, 130, 240));
        UIManager.put("Button.select", new Color(200, 220, 255));
        UIManager.put("TabbedPane.selected", new Color(70, 130, 240));
        UIManager.put("TabbedPane.foreground", Color.BLACK);
        UIManager.put("TabbedPane.selectedForeground", Color.WHITE);
        
        // Set font for better readability
        Font defaultFont = new Font("Segoe UI", Font.PLAIN, 12);
        UIManager.put("Button.font", defaultFont);
        UIManager.put("Label.font", defaultFont);
        UIManager.put("TextField.font", defaultFont);
        UIManager.put("PasswordField.font", defaultFont);
        UIManager.put("ComboBox.font", defaultFont);
        UIManager.put("TabbedPane.font", defaultFont.deriveFont(Font.BOLD));
    }
    
    private static void printStartupInfo(DataManager dataManager) {
        System.out.println("=========================================");
        System.out.println("  Système de Gestion Éducative USTHB");
        System.out.println("=========================================");
        System.out.println("Chargement des données...");
        System.out.println("• " + dataManager.getTotalUsers() + " utilisateurs chargés");
        System.out.println("• " + dataManager.getTotalModules() + " modules chargés");
        System.out.println("• " + dataManager.getTotalGrades() + " notes chargées");
        System.out.println("• " + dataManager.getTotalAbsences() + " absences chargées");
        System.out.println("• " + dataManager.getTotalInscriptions() + " inscriptions chargées");
        System.out.println();
        System.out.println("Comptes de démonstration:");
        System.out.println("• Étudiant: 10000001 / password");
        System.out.println("• Professeur: 20000001 / password");
        System.out.println("• Vice-Doyen: 30000001 / password");
        System.out.println("=========================================");
        System.out.println();
    }
    
    private static void createAndShowLoginView() {
        try {
            // Create the login view
            LoginView loginView = new LoginView();
            
            // Create the login controller
            LoginController loginController = new LoginController(loginView);
            
            // Center the window on screen
            loginView.setLocationRelativeTo(null);
            
            // Make the window visible
            loginView.setVisible(true);
            
            // Add window listener for cleanup on close
            addWindowListeners(loginView);
            
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la création de la vue de connexion", e);
        }
    }
    
    private static void addWindowListeners(LoginView loginView) {
        // Add window listener to handle application exit
        loginView.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Optional: Add a shutdown hook to save data when application exits
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Sauvegarde des données...");
            DataManager.getInstance().saveAllData();
            System.out.println("Application fermée.");
        }));
    }
    
    private static void showErrorDialog(String message, Exception e) {
        // Log the error
        System.err.println(message);
        e.printStackTrace();
        
        // Show error dialog to user
        JOptionPane.showMessageDialog(
            null,
            message + "\n\nDétails: " + e.getMessage(),
            "Erreur de l'application",
            JOptionPane.ERROR_MESSAGE
        );
        
        // Exit the application
        System.exit(1);
    }
    
    // Utility method to get the application version
    public static String getVersion() {
        return "1.0.0";
    }
    
    // Utility method to get the application name
    public static String getAppName() {
        return "Système de Gestion Éducative USTHB";
    }
}