import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PanneauSolaire {
    private static final int CHARGE_AMOUNT = 5;
    private static final int CHARGE_INTERVAL = 300000; // 5 minutes in milliseconds

    private final Robot robot;
    private boolean exposeLumiere;
    private Timer rechargeTemps;

    public PanneauSolaire(Robot robot) {
        this.robot = robot;
        setupTimer();
    }

    private void setupTimer() {
        rechargeTemps = new Timer(CHARGE_INTERVAL, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(exposeLumiere) {
                    robot.recharger(CHARGE_AMOUNT);
                    robot.ajouterHistorique("Chargement solaire (+5%)");
                }
            }
        });
        rechargeTemps.start();
    }

    public void toggleExposition() {
        exposeLumiere = !exposeLumiere;
        String status = exposeLumiere ? "exposé" : "non-exposé";
        robot.ajouterHistorique("Panneau solaire " + status);
    }

    public boolean estExpose() {
        return exposeLumiere;
    }
}