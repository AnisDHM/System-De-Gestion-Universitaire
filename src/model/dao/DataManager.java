// DataManager.java
package model.dao;

import model.entities.*;
import model.observers.GradeSubject;
import model.observers.Subject;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class DataManager {
    private static DataManager instance;
    
    // Data storage
    private Map<String, User> users;
    private Map<String, model.entities.Module> modules;
    private List<Grade> grades;
    private List<Absence> absences;
    private List<Inscription> inscriptions;
    
    // Observer pattern for grade changes
    private Subject gradeSubject;
    
    // File paths for persistence
    private static final String USERS_FILE = "data/users.dat";
    private static final String MODULES_FILE = "data/modules.dat";
    private static final String GRADES_FILE = "data/grades.dat";
    private static final String ABSENCES_FILE = "data/absences.dat";
    private static final String INSCRIPTIONS_FILE = "data/inscriptions.dat";
    
    private DataManager() {
        initializeData();
        loadData();
        gradeSubject = new GradeSubject();
    }
    
    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }
    
    private void initializeData() {
        users = new HashMap<>();
        modules = new HashMap<>();
        grades = new ArrayList<>();
        absences = new ArrayList<>();
        inscriptions = new ArrayList<>();
        
        // Create sample data for demonstration
        createSampleData();
    }
    
    private void createSampleData() {
        // Create sample students
        Student student1 = new Student("10000001", "password", "Alice", "Martin");
        student1.setEmail("alice.martin@usthb.dz");
        student1.setPhoneNumber("0550123456");
        student1.setSpeciality("Informatique");
        student1.setYear(2);
        
        Student student2 = new Student("10000002", "password", "Bob", "Durand");
        student2.setEmail("bob.durand@usthb.dz");
        student2.setPhoneNumber("0550123457");
        student2.setSpeciality("IA");
        student2.setYear(3);
        
        Student student3 = new Student("10000003", "password", "Clara", "Leroy");
        student3.setEmail("clara.leroy@usthb.dz");
        student3.setPhoneNumber("0550123458");
        student3.setSpeciality("Informatique");
        student3.setYear(2);
        
        // Create sample professors
        Professor prof1 = new Professor("20000001", "password", "Dr. Jean", "Petit");
        prof1.setEmail("jean.petit@usthb.dz");
        prof1.setPhoneNumber("0550111111");
        prof1.setDepartment("Informatique");
        prof1.setAcademicRank("Professeur");
        
        Professor prof2 = new Professor("20000002", "password", "Dr. Marie", "Grand");
        prof2.setEmail("marie.grand@usthb.dz");
        prof2.setPhoneNumber("0550111112");
        prof2.setDepartment("IA");
        prof2.setAcademicRank("Maître de Conférences");
        
        // Create sample vice dean
        ViceDean viceDean = new ViceDean("30000001", "password", "Pr. Ahmed", "Benziane");
        viceDean.setEmail("ahmed.benziane@usthb.dz");
        viceDean.setPhoneNumber("0550222222");
        viceDean.setDepartment("Faculté d'Informatique");
        viceDean.setTitle("Professeur");
        
        // Add users
        users.put(student1.getCode(), student1);
        users.put(student2.getCode(), student2);
        users.put(student3.getCode(), student3);
        users.put(prof1.getCode(), prof1);
        users.put(prof2.getCode(), prof2);
        users.put(viceDean.getCode(), viceDean);
        
        // Create sample modules
        model.entities.Module module1 = new model.entities.Module("GL01", "Génie Logiciel", 5, prof1.getCode(), 1.5, 1, "Concepts du génie logiciel et méthodes de développement");
        model.entities.Module module2 = new model.entities.Module("BD02", "Base de Données", 4, prof1.getCode(), 1.5, 1, "Systèmes de gestion de bases de données relationnelles");
        model.entities.Module module3 = new model.entities.Module("IA03", "Intelligence Artificielle", 6, prof2.getCode(), 2.0, 2, "Fondements de l'intelligence artificielle");
        model.entities.Module module4 = new model.entities.Module("RS04", "Réseaux", 4, null, 1.0, 2, "Réseaux informatiques et protocoles");
        model.entities.Module module5 = new model.entities.Module("SE05", "Systèmes d'Exploitation", 5, null, 1.5, 1, "Fonctionnement des systèmes d'exploitation");
        
        // Add modules
        modules.put(module1.getCode(), module1);
        modules.put(module2.getCode(), module2);
        modules.put(module3.getCode(), module3);
        modules.put(module4.getCode(), module4);
        modules.put(module5.getCode(), module5);
        
        // Add modules to professors
        prof1.addTaughtModule(module1);
        prof1.addTaughtModule(module2);
        prof2.addTaughtModule(module3);
        
        // Create sample grades
        grades.add(new Grade(student1.getCode(), module1.getCode(), 15.5, Grade.TYPE_EXAM, java.time.LocalDate.now().minusDays(30)));
        grades.add(new Grade(student1.getCode(), module1.getCode(), 14.0, Grade.TYPE_CC, java.time.LocalDate.now().minusDays(45)));
        grades.add(new Grade(student1.getCode(), module2.getCode(), 12.5, Grade.TYPE_EXAM, java.time.LocalDate.now().minusDays(25)));
        grades.add(new Grade(student2.getCode(), module1.getCode(), 16.0, Grade.TYPE_EXAM, java.time.LocalDate.now().minusDays(30)));
        grades.add(new Grade(student2.getCode(), module3.getCode(), 18.5, Grade.TYPE_EXAM, java.time.LocalDate.now().minusDays(20)));
        grades.add(new Grade(student3.getCode(), module2.getCode(), 11.0, Grade.TYPE_EXAM, java.time.LocalDate.now().minusDays(25)));
        
        // Create sample absences
        absences.add(new Absence(student1.getCode(), module1.getCode(), java.time.LocalDate.now().minusDays(15), false, null, Absence.SESSION_COURSE));
        absences.add(new Absence(student1.getCode(), module1.getCode(), java.time.LocalDate.now().minusDays(10), true, "Maladie", Absence.SESSION_TD));
        absences.add(new Absence(student2.getCode(), module3.getCode(), java.time.LocalDate.now().minusDays(5), false, null, Absence.SESSION_COURSE));
        
        // Create sample inscriptions
        inscriptions.add(new Inscription(student1.getCode(), module1.getCode()));
        inscriptions.add(new Inscription(student1.getCode(), module2.getCode()));
        inscriptions.add(new Inscription(student2.getCode(), module1.getCode()));
        inscriptions.add(new Inscription(student2.getCode(), module3.getCode()));
        inscriptions.add(new Inscription(student3.getCode(), module2.getCode()));
        
        // Validate some inscriptions
        inscriptions.get(0).validate(viceDean.getCode());
        inscriptions.get(2).validate(viceDean.getCode());
        inscriptions.get(4).validate(viceDean.getCode());
    }
    
    // ===== USER MANAGEMENT =====
    
    public User getUser(String code) {
        return users.get(code);
    }
    
    public boolean addUser(User user) {
        if (user != null && !users.containsKey(user.getCode())) {
            users.put(user.getCode(), user);
            saveUsers();
            return true;
        }
        return false;
    }
    
    public boolean updateUser(User user) {
        if (user != null && users.containsKey(user.getCode())) {
            users.put(user.getCode(), user);
            saveUsers();
            return true;
        }
        return false;
    }
    
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    // ===== STUDENT-RELATED METHODS =====
    
    public List<Grade> getStudentGrades(String studentCode) {
        return grades.stream()
                .filter(grade -> grade.getStudentCode().equals(studentCode))
                .collect(Collectors.toList());
    }
    
    public List<Absence> getStudentAbsences(String studentCode) {
        return absences.stream()
                .filter(absence -> absence.getStudentCode().equals(studentCode))
                .collect(Collectors.toList());
    }
    
    public List<Inscription> getStudentInscriptions(String studentCode) {
        return inscriptions.stream()
                .filter(inscription -> inscription.getStudentCode().equals(studentCode))
                .collect(Collectors.toList());
    }
    
    public List<model.entities.Module> getAvailableModulesForStudent(String studentCode) {
        // Get modules that the student is not already enrolled in
        Set<String> enrolledModuleCodes = getStudentInscriptions(studentCode).stream()
                .map(Inscription::getModuleCode)
                .collect(Collectors.toSet());
        
        return modules.values().stream()
                .filter(module -> !enrolledModuleCodes.contains(module.getCode()))
                .collect(Collectors.toList());
    }
    
    public boolean isStudentRegistered(String studentCode, String moduleCode) {
        return inscriptions.stream()
                .anyMatch(inscription -> inscription.getStudentCode().equals(studentCode) 
                        && inscription.getModuleCode().equals(moduleCode));
    }
    
    public boolean addInscription(Inscription inscription) {
        if (inscription != null && !inscriptions.contains(inscription)) {
            inscriptions.add(inscription);
            saveInscriptions();
            return true;
        }
        return false;
    }
    
    // ===== PROFESSOR-RELATED METHODS =====
    
    public List<model.entities.Module> getProfessorModules(String professorCode) {
        return modules.values().stream()
                .filter(module -> professorCode.equals(module.getProfessorCode()))
                .collect(Collectors.toList());
    }
    
    public List<Student> getStudentsForProfessor(String professorCode) {
        // Get modules taught by professor
        List<String> professorModuleCodes = getProfessorModules(professorCode).stream()
                .map(model.entities.Module::getCode)
                .collect(Collectors.toList());
        
        // Get students enrolled in those modules
        Set<String> studentCodes = inscriptions.stream()
                .filter(inscription -> professorModuleCodes.contains(inscription.getModuleCode()))
                .map(Inscription::getStudentCode)
                .collect(Collectors.toSet());
        
        return studentCodes.stream()
                .map(this::getUser)
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user)
                .collect(Collectors.toList());
    }
    
    public List<Professor> searchTeachers(String searchTerm) {
        String lowerSearchTerm = searchTerm.toLowerCase();
        return users.values().stream()
                .filter(user -> user instanceof Professor)
                .map(user -> (Professor) user)
                .filter(professor -> 
                    professor.getFirstName().toLowerCase().contains(lowerSearchTerm) ||
                    professor.getLastName().toLowerCase().contains(lowerSearchTerm) ||
                    professor.getCode().contains(searchTerm) ||
                    professor.getDepartment().toLowerCase().contains(lowerSearchTerm))
                .collect(Collectors.toList());
    }
    
    public List<Professor> getAllProfessors() {
        return users.values().stream()
                .filter(user -> user instanceof Professor)
                .map(user -> (Professor) user)
                .collect(Collectors.toList());
    }
    
    // ===== VICE DEAN-RELATED METHODS =====
    
    public List<model.entities.Module> getAllModules() {
        return new ArrayList<>(modules.values());
    }
    
    public List<Student> getAllStudents() {
        return users.values().stream()
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user)
                .collect(Collectors.toList());
    }
    
    // ===== MODULE MANAGEMENT =====
    
    public model.entities.Module getModule(String code) {
        return modules.get(code);
    }
    
    public boolean addModule(model.entities.Module module) {
        if (module != null && !modules.containsKey(module.getCode())) {
            modules.put(module.getCode(), module);
            saveModules();
            return true;
        }
        return false;
    }
    
    public boolean updateModule(model.entities.Module module) {
        if (module != null && modules.containsKey(module.getCode())) {
            modules.put(module.getCode(), module);
            saveModules();
            return true;
        }
        return false;
    }
    
    public boolean deleteModule(String code) {
        if (modules.containsKey(code)) {
            modules.remove(code);
            
            // Also remove related grades, absences, and inscriptions
            grades.removeIf(grade -> grade.getModuleCode().equals(code));
            absences.removeIf(absence -> absence.getModuleCode().equals(code));
            inscriptions.removeIf(inscription -> inscription.getModuleCode().equals(code));
            
            saveModules();
            saveGrades();
            saveAbsences();
            saveInscriptions();
            return true;
        }
        return false;
    }
    
    // ===== GRADE MANAGEMENT =====
    
    public boolean addGrade(Grade grade) {
        if (grade != null) {
            grades.add(grade);
            saveGrades();
            
            // Notify observers
            if (gradeSubject instanceof GradeSubject) {
                ((GradeSubject) gradeSubject).gradeAdded(grade.getStudentCode(), grade.getModuleCode(), grade.getValue());
            }
            return true;
        }
        return false;
    }
    
    public boolean updateGrade(Grade grade) {
        if (grade != null) {
            // Remove existing grade and add updated one
            grades.removeIf(g -> g.getStudentCode().equals(grade.getStudentCode()) 
                    && g.getModuleCode().equals(grade.getModuleCode()) 
                    && g.getType().equals(grade.getType()));
            grades.add(grade);
            saveGrades();
            
            // Notify observers
            if (gradeSubject instanceof GradeSubject) {
                ((GradeSubject) gradeSubject).gradeModified(grade.getStudentCode(), grade.getModuleCode(), grade.getValue());
            }
            return true;
        }
        return false;
    }
    
    // ===== ABSENCE MANAGEMENT =====
    
    public boolean addAbsence(Absence absence) {
        if (absence != null) {
            absences.add(absence);
            saveAbsences();
            return true;
        }
        return false;
    }
    
    public boolean updateAbsence(Absence absence) {
        if (absence != null) {
            // Remove existing absence and add updated one
            absences.removeIf(a -> a.getStudentCode().equals(absence.getStudentCode()) 
                    && a.getModuleCode().equals(absence.getModuleCode()) 
                    && a.getDate().equals(absence.getDate()));
            absences.add(absence);
            saveAbsences();
            return true;
        }
        return false;
    }
    
    // ===== OBSERVER PATTERN =====
    
    public Subject getGradeSubject() {
        return gradeSubject;
    }
    
    // ===== DATA PERSISTENCE =====
    
    @SuppressWarnings("unchecked")
    private void loadData() {
        try {
            // Create data directory if it doesn't exist
            new File("data").mkdirs();
            
            // Load users
            if (new File(USERS_FILE).exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(USERS_FILE))) {
                    users = (Map<String, User>) ois.readObject();
                }
            }
            
            // Load modules
            if (new File(MODULES_FILE).exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(MODULES_FILE))) {
                    modules = (Map<String, model.entities.Module>) ois.readObject();
                }
            }
            
            // Load grades
            if (new File(GRADES_FILE).exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(GRADES_FILE))) {
                    grades = (List<Grade>) ois.readObject();
                }
            }
            
            // Load absences
            if (new File(ABSENCES_FILE).exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ABSENCES_FILE))) {
                    absences = (List<Absence>) ois.readObject();
                }
            }
            
            // Load inscriptions
            if (new File(INSCRIPTIONS_FILE).exists()) {
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(INSCRIPTIONS_FILE))) {
                    inscriptions = (List<Inscription>) ois.readObject();
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error loading data: " + e.getMessage());
            // If loading fails, keep the sample data
        }
    }
    
    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(USERS_FILE))) {
            oos.writeObject(users);
        } catch (Exception e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }
    
    private void saveModules() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(MODULES_FILE))) {
            oos.writeObject(modules);
        } catch (Exception e) {
            System.err.println("Error saving modules: " + e.getMessage());
        }
    }
    
    private void saveGrades() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(GRADES_FILE))) {
            oos.writeObject(grades);
        } catch (Exception e) {
            System.err.println("Error saving grades: " + e.getMessage());
        }
    }
    
    private void saveAbsences() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ABSENCES_FILE))) {
            oos.writeObject(absences);
        } catch (Exception e) {
            System.err.println("Error saving absences: " + e.getMessage());
        }
    }
    
    private void saveInscriptions() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(INSCRIPTIONS_FILE))) {
            oos.writeObject(inscriptions);
        } catch (Exception e) {
            System.err.println("Error saving inscriptions: " + e.getMessage());
        }
    }
    
    public void saveAllData() {
        saveUsers();
        saveModules();
        saveGrades();
        saveAbsences();
        saveInscriptions();
    }
    
    // ===== UTILITY METHODS =====
    
    public int getTotalUsers() {
        return users.size();
    }
    
    public int getTotalModules() {
        return modules.size();
    }
    
    public int getTotalGrades() {
        return grades.size();
    }
    
    public int getTotalAbsences() {
        return absences.size();
    }
    
    public int getTotalInscriptions() {
        return inscriptions.size();
    }
    
    // Method to clear all data (for testing)
    public void clearAllData() {
        users.clear();
        modules.clear();
        grades.clear();
        absences.clear();
        inscriptions.clear();
        createSampleData();
    }
}