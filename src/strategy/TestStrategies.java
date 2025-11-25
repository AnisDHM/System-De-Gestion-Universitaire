package strategy;

public class TestStrategies {
    public static void main(String[] args) {
        // Test StudentAuthStrategy
        AuthenticationStrategy studentAuth = new StudentAuthStrategy();
        
        System.out.println("=== Test Student ===");
        System.out.println("12345678: " + studentAuth.validateCodeFormat("12345678")); // true
        System.out.println("23456789: " + studentAuth.validateCodeFormat("23456789")); // false
        System.out.println("1234567: " + studentAuth.validateCodeFormat("1234567"));   // false
        
        // Test ProfessorAuthStrategy
        AuthenticationStrategy profAuth = new ProfessorAuthStrategy();
        
        System.out.println("\n=== Test Professor ===");
        System.out.println("23456789: " + profAuth.validateCodeFormat("23456789")); // true
        System.out.println("12345678: " + profAuth.validateCodeFormat("12345678")); // false
        
        // Test ViceDeanAuthStrategy
        AuthenticationStrategy deanAuth = new ViceDeanAuthStrategy();
        
        System.out.println("\n=== Test ViceDean ===");
        System.out.println("34567890: " + deanAuth.validateCodeFormat("34567890")); // true
        System.out.println("12345678: " + deanAuth.validateCodeFormat("12345678")); // false
    }
}