package controller;
import model.dao.DataManager;
import model.entities.*;
import model.observers.GradeSubject;
import view.StudentView;
import view.LoginView;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
/**
 * Contrôleur pour l'étudiant
 * 
 * Responsabilités:
 * - Charger les données de l'étudiant (notes, absences, inscriptions)
 * - Gérer les inscriptions aux modules
 * - Calculer les moyennes
 * - Gérer la déconnexion
 */

public class StudentController implements PropertyChangeListener{
    private StudentView view;
    private User currentUser;
    private DataManager dataManager;
    private GradeSubject gradeSubject;

    public StudentController(StudentView view, User currentUser) {
        this.view = view;
        this.currentUser = currentUser;
        this.dataManager = DataManager.getInstance();
        this.gradeSubject = new GradeSubject();
        gradeSubject.attach(view);
        view.addPropertyChangeListener(this);
        loadStudentData();
        
        System.out.println("StudentController initialisé pour: " + currentUser.getFullName());
    }

    private void loadStudentData() {
        String studentCode = currentUser.getCode();
        
        try {
            
            List<Grade> grades = dataManager.getStudentGrades(studentCode);
            view.displayGrades(grades);
            System.out.println( grades.size() + " notes chargées");
            
            List<Absence> absences = dataManager.getStudentAbsences(studentCode);
            view.displayAbsences(absences);
            System.out.println(absences.size() + " absences chargées");
            
            List<model.entities.Module> modules = dataManager.getAllModules();
            view.displayAvailableModules(modules);
            System.out.println( modules.size() + " modules disponibles");
            
            loadInscriptions();
            
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des données: " + e.getMessage());
            e.printStackTrace();
        }
    }
    private void loadInscriptions() {
        String studentCode = currentUser.getCode();
        List<Inscription> inscriptions = dataManager.getStudentInscriptions(studentCode);
        
        StringBuilder text = new StringBuilder();
        text.append("═══════════════════════════════\n");
        text.append("       MES INSCRIPTIONS\n");
        text.append("═══════════════════════════════\n\n");
        
        if (inscriptions.isEmpty()) {
            text.append("Aucune inscription pour le moment.\n\n");
            text.append("Conseil: Inscrivez-vous aux modules\n");
            text.append("dans l'onglet 'Inscription'.\n");
        } else {
            int validees = 0;
            int enAttente = 0;
            
            for (Inscription inscription : inscriptions) {
                model.entities.Module module = dataManager.getModule(inscription.getModuleCode());
                if (module != null) {
                    String status = inscription.isValidated() ? "Validée" : "En attente";
                    
                    text.append("• ").append(module.getName());
                    text.append(" (").append(module.getCode()).append(")");
                    text.append("\n  └─ ").append(status);
                    text.append(" - ").append(module.getCredits()).append(" crédits");
                    text.append("\n\n");
                    
                    if (inscription.isValidated()) {
                        validees++;
                    } else {
                        enAttente++;
                    }
                }
            }
            
            text.append("───────────────────────────────\n");
            text.append("Total: ").append(inscriptions.size()).append(" inscriptions\n");
            text.append("  • Validées: ").append(validees).append("\n");
            text.append("  • En attente: ").append(enAttente).append("\n");
        }
        
        view.updateInscriptions(text.toString());
    }
    
    /**
     * Gestion des événements de la vue (patron PropertyChange)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propertyName = evt.getPropertyName();
        Object newValue = evt.getNewValue();
        
        switch (propertyName) {
            case "register":
                handleRegistration((String) newValue);
                break;
                
            case "logout":
                handleLogout();
                break;
                
            case "refresh":
                loadStudentData();
                break;
                
            default:
                System.out.println("⚠️ Événement non géré: " + propertyName);
                break;
        }
    }

    private void handleRegistration(String selectedModule) {
        if (selectedModule == null || selectedModule.isEmpty()) {
            view.showError("Veuillez sélectionner un module");
            return;
        }
        
        try {
            
            String moduleCode;
            if (selectedModule.contains(" - ")) {
                moduleCode = selectedModule.substring(0, selectedModule.indexOf(" - ")).trim();
            } else {
                moduleCode = selectedModule.trim();
            }
            
            String studentCode = currentUser.getCode();
            
            
            List<Inscription> inscriptions = dataManager.getStudentInscriptions(studentCode);
            for (Inscription insc : inscriptions) {
                if (insc.getModuleCode().equals(moduleCode)) {
                    view.showError("Vous êtes déjà inscrit à ce module.\n\n" +
                                  "Statut: " + (insc.isValidated() ? "Validée" : "En attente de validation"));
                    return;
                }
            }
            
            
            Inscription inscription = new Inscription(studentCode, moduleCode);
            dataManager.addInscription(inscription);
            
            
            model.entities.Module module = dataManager.getModule(moduleCode);
            String moduleName = module != null ? module.getName() : moduleCode;
            
            view.showMessage("Inscription réussie!\n\n" +
                           "Module: " + moduleName + "\n" +
                           "Statut: En attente de validation par l'administration.\n\n" +
                           "Vous serez notifié une fois validée.");
            
            
            loadInscriptions();
            
            System.out.println("Inscription ajoutée: " + studentCode + " → " + moduleCode);
            
        } catch (Exception e) {
            view.showError("Erreur lors de l'inscription:\n" + e.getMessage());
            e.printStackTrace();
        }
    }
    private void handleLogout() {
        try {
            
            dataManager.saveAllData();
            System.out.println("Données sauvegardées");
            
            
            gradeSubject.detach(view);
            
            
            view.dispose();
            System.out.println("Déconnexion de: " + currentUser.getFullName());
            
            
            LoginView loginView = new LoginView();
            new LoginController(loginView);
            loginView.setVisible(true);
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la déconnexion: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void refreshData() {
        loadStudentData();
    }
   
    public double calculateAverage() {
        List<Grade> grades = dataManager.getStudentGrades(currentUser.getCode());
        if (grades.isEmpty()) {
            return 0.0;
        }
        
        double sum = 0.0;
        for (Grade grade : grades) {
            sum += grade.getValue();
        }
        return sum / grades.size();
    }
    
    public int countUnjustifiedAbsences() {
        List<Absence> absences = dataManager.getStudentAbsences(currentUser.getCode());
        int count = 0;
        for (Absence absence : absences) {
            if (!absence.isJustified()) {
                count++;
            }
        }
        return count;
    }
    public void cleanup() {
        if (gradeSubject != null && view != null) {
            gradeSubject.detach(view);
        }
    }
    
}
