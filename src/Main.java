import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Scanner;
import java.lang.reflect.Field;
import javax.swing.Timer;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("=== Test 1: Basic Robot Operations ===");
            RobotLivraison robot1 = new RobotLivraison("ROBOT-001", 0, 0);
            testRobot(robot1);

            System.out.println("\n=== Test 2: Energy Management ===");
            testEnergyManagement();

            System.out.println("\n=== Test 3: Maintenance Test ===");
            testMaintenance();

            System.out.println("\n=== Test 4: Solar Panel Test ===");
            testSolarPanel();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void testSolarPanel() throws Exception {
        RobotLivraison robot = new RobotLivraison("SOLAR-001", 0, 0);
        robot.installerPanneauSolaire();
        robot.panneauSolaire.toggleExposition();

        System.out.println("Initial energy: " + robot.energie + "%");

        Field timerField = PanneauSolaire.class.getDeclaredField("chargeTimer");
        timerField.setAccessible(true);
        Timer timer = (Timer) timerField.get(robot.panneauSolaire);

        timer.getActionListeners()[0].actionPerformed(null);
        System.out.println("After 1st charge: " + robot.energie + "%");

        timer.getActionListeners()[0].actionPerformed(null);
        System.out.println("After 2nd charge: " + robot.energie + "%");

        System.out.println("History:\n" + robot.getHistorique());
    }

    private static void testRobot(RobotLivraison robot) {
        try {
            System.out.println("Initial state: " + robot);

            // Drain energy to 0%
            robot.consommeEnergie(100);  // ADD THIS LINE
            System.out.println("\nAttempting to start with 0% energy...");
            robot.demarrer();
        } catch (RobotException e) {
            System.out.println("Expected error: " + e.getMessage());
        }

        // Recharge and start
        robot.recharger(100);
        try {
            robot.demarrer();
            System.out.println("\nRobot started successfully!");
        } catch (RobotException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // Test connection
        try {
            ((RobotConnecte) robot).connecter("WIFI-ROBOTS");
            System.out.println("\nConnected to network successfully");
        } catch (RobotException e) {
            System.out.println("Connection error: " + e.getMessage());
        }

        // Test data sending
        try {
            ((RobotConnecte) robot).envoyerDonnees("Test message");
            System.out.println("\nData sent successfully");
        } catch (RobotException e) {
            System.out.println("Data sending error: " + e.getMessage());
        }

        // Test package delivery workflow
        try {
            System.out.println("\nStarting delivery test...");
            // First: Charge package
            robot.effectuerTache();
            // Second: Execute delivery
            robot.effectuerTache();  // ADD THIS LINE
            System.out.println("\nAfter delivery attempt: " + robot);
            System.out.println("History:\n" + robot.getHistorique());
        } catch (RobotException e) {
            System.out.println("Delivery error: " + e.getMessage());
        }
    }

    private static void testEnergyManagement() {
        RobotLivraison robot = new RobotLivraison("ROBOT-002", 0, 0);
        robot.recharger(10); // Reduced energy to 10% instead of 30%

        try {
            robot.demarrer();
            System.out.println("Attempting energy-intensive move...");
            // Move to (40, 0) - distance = 40 units
            robot.deplacer(40, 0); // Requires 12% energy (40 * 0.3)
        } catch (RobotException e) {
            System.out.println("Expected energy error: " + e.getMessage());
        }

        System.out.println("Remaining energy: " + robot.energie + "%");
    }



    private static void testMaintenance() {
        RobotLivraison robot = new RobotLivraison("ROBOT-003", 0, 0);
        robot.recharger(100);

        // Simulate 100 hours of use
        robot.heuresUtilisation = 100;

        try {
            System.out.println("Attempting action requiring maintenance...");
            robot.demarrer();
        } catch (RobotException e) {
            System.out.println("Expected maintenance error: " + e.getMessage());
        }
    }

}