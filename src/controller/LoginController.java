package controller;
import strategy.*;
import view.*;
import model.entities.*;
import model.dao.*;

public class LoginController {
    private LoginView view;
    private AuthenticationStrategy authStrategy;

    public LoginController(LoginView view) {
        this.view = view;
    }

    public boolean login(String code , String password){
        if(code == null || code.isEmpty() || password == null || password.isEmpty()){
            view.showError("Veuillez remplir tous les champs");
            return false;
        }

        selectAuthStrategy(code);

        if(authStrategy == null){
            view.showError("Format de code invalide.\n" +
                          "Le code doit commencer par:\n" +
                          "- 1 pour Étudiant\n" +
                          "- 2 pour Professeur\n" +
                          "- 3 pour Vice-Doyen");
            return false;
        }
        if (!authStrategy.validateCodeFormat(code)) {
            view.showError("Format de code invalide pour ce type d'utilisateur.\n" +
                          "Le code doit avoir exactement 8 chiffres.");
            return false;
        }

        if(authStrategy.authenticate(code, password)){
            User user = authStrategy.getAuthenticatedUser(code);
            DataManager.getInstance().saveToFile(password);
            openUserInterface(user);
            return true;
        }else{
            view.showError("Code ou mot de passe incorrect.");
            return false;
        }
    }

    private void selectAuthStrategy(String code){
        if (code == null || code.isEmpty()){
            authStrategy = null;
            return;
        }
        char firstChar = code.charAt(0);
        switch(firstChar){
            case '1':
                authStrategy = new StudentAuthStrategy();
                System.out.println(" Stratégie Étudiant sélectionnée");
                break;

            case '2':
                authStrategy = new ProfessorAuthStrategy();
                System.out.println(" Stratégie Professeur sélectionnée");
                break;

            case '3':
                authStrategy = new ViceDeanAuthStrategy();
                System.out.println(" Stratégie Vice-Doyen sélectionnée");
                break;

            default:
                authStrategy = null;
                System.out.println("Aucune stratégie sélectionnée");
                break;
        }
    }

    private void openUserInterface(User user) {
        view.dispose();
        String code = user.getCode();
        if (code.startsWith("1")) {
            StudentView studentView = new StudentView(user);
            StudentController studentController = new StudentController(studentView, user);
            studentView.setVisible(true);
            
            System.out.println("Interface Étudiant ouverte pour: " + user.getFullName());
            
        }else if(code.startWith("2")){
            ProfessorView professorView = new ProfessorView(user);
            ProfessorController professorController = new ProfessorController(professorView, user);
            professorView.setVisible(true);
            
            System.out.println("Interface Professeur ouverte pour: " + user.getFullName());
        }else if(code.startWith("3")){
            ViceDeanView viceDeanView = new ViceDeanView(user);
            ViceDeanController viceDeanController = new ViceDeanController(viceDeanView, user);
            viceDeanView.setVisible(true);
            
            System.out.println(" Interface Vice-Doyen ouverte pour: " + user.getFullName());
        }else{
            view.showError("Erreur: Type d'utilisateur non reconnu");
        }
    }
    public void cleanup() {
        authStrategy = null;
    }
    
}
