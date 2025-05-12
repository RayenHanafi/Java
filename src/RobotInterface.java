import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RobotInterface extends JFrame {
    private RobotLivraison robot;
    private JTextField xField, yField, destField;
    private JTextArea logArea;
    private JLabel energyLabel;
    private JLabel solarStatus;
    private JButton toggleSolarButton;

    public RobotInterface() {
        robot = new RobotLivraison("R1", 0, 0);

        setTitle("Interface de Contrôle du Robot");
        setSize(700, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 240, 240));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(new Color(240, 240, 240));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(4, 2, 10, 10));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Contrôle"));
        controlPanel.setBackground(Color.white);

        xField = new JTextField();
        yField = new JTextField();
        destField = new JTextField();

        controlPanel.add(new JLabel("X:"));
        controlPanel.add(xField);
        controlPanel.add(new JLabel("Y:"));
        controlPanel.add(yField);
        controlPanel.add(new JLabel("Destination:"));
        controlPanel.add(destField);

        JButton startButton = new JButton("▶ Démarrer");
        JButton stopButton = new JButton("■ Arrêter");
        startButton.setBackground(new Color(0, 153, 76));
        startButton.setForeground(Color.white);
        stopButton.setBackground(new Color(204, 0, 0));
        stopButton.setForeground(Color.white);
        controlPanel.add(startButton);
        controlPanel.add(stopButton);

        JPanel solarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        solarPanel.setBorder(BorderFactory.createTitledBorder("Panneau Solaire"));
        solarPanel.setBackground(Color.white);

        JButton installSolarButton = new JButton("🟡 Installer");
        toggleSolarButton = new JButton("🌞 Toggle Lumière");
        solarStatus = new JLabel("État: Non installé");

        installSolarButton.setBackground(new Color(255, 204, 51));
        toggleSolarButton.setEnabled(false);

        solarPanel.add(installSolarButton);
        solarPanel.add(toggleSolarButton);
        solarPanel.add(solarStatus);

        JPanel energyPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        energyPanel.setBackground(Color.white);
        energyLabel = new JLabel("🔋 Énergie: 100%");
        energyPanel.add(energyLabel);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(250, 250, 250));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));

        JButton moveButton = new JButton("Déplacer");
        JButton chargeButton = new JButton("Charger Colis");
        JButton deliverButton = new JButton("Livrer");

        for (JButton btn : new JButton[]{moveButton, chargeButton, deliverButton}) {
            btn.setPreferredSize(new Dimension(130, 30));
        }

        buttonPanel.add(moveButton);
        buttonPanel.add(chargeButton);
        buttonPanel.add(deliverButton);

        logArea = new JTextArea(10, 1);
        logArea.setEditable(false);
        logArea.setBackground(Color.white);
        logArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        logArea.setBorder(BorderFactory.createTitledBorder("Journal"));

        JScrollPane scrollPane = new JScrollPane(logArea);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        startButton.addActionListener(e -> {
            try {
                robot.demarrer();
                log("Robot démarré.");
                updateEnergy();
            } catch (Exception ex) {
                log("Erreur : " + ex.getMessage());
            }
        });

        stopButton.addActionListener(e -> {
            robot.arreter();
            log("Robot arrêté.");
            updateEnergy();
        });

        moveButton.addActionListener(e -> {
            try {
                int x = Integer.parseInt(xField.getText());
                int y = Integer.parseInt(yField.getText());
                robot.deplacer(x, y);
                log("Déplacement vers (" + x + ", " + y + ") effectué.");
                updateEnergy();
            } catch (Exception ex) {
                log("Erreur : " + ex.getMessage());
            }
        });

        chargeButton.addActionListener(e -> {
            try {
                String destination = destField.getText();
                robot.chargerColis(destination);
                log("Colis chargé pour : " + destination);
                updateEnergy();
            } catch (Exception ex) {
                log("Erreur : " + ex.getMessage());
            }
        });

        deliverButton.addActionListener(e -> {
            try {
                int x = Integer.parseInt(xField.getText());
                int y = Integer.parseInt(yField.getText());
                robot.faireLivraison(x, y);
                log("Livraison effectuée vers (" + x + ", " + y + ")");
                updateEnergy();
            } catch (Exception ex) {
                log("Erreur : " + ex.getMessage());
            }
        });

        installSolarButton.addActionListener(e -> {
            robot.installerPanneauSolaire();
            solarStatus.setText("État: Installé");
            toggleSolarButton.setEnabled(true);
            installSolarButton.setEnabled(false);
            log("Panneau solaire installé");
        });

        toggleSolarButton.addActionListener(e -> {
            robot.panneauSolaire.toggleExposition();
            String status = robot.panneauSolaire.estExpose() ? "Activé" : "Désactivé";
            solarStatus.setText("État: " + status);
            log("Exposition solaire: " + status);
        });

        Timer updateTimer = new Timer(1000, e -> updateEnergy());
        updateTimer.start();

        topPanel.add(controlPanel, BorderLayout.NORTH);
        topPanel.add(solarPanel, BorderLayout.CENTER);
        topPanel.add(energyPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void log(String message) {
        logArea.append(message + "\n");
    }

    private void updateEnergy() {
        SwingUtilities.invokeLater(() -> {
            energyLabel.setText(String.format("🔋 Énergie: %d%%", robot.energie));
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RobotInterface());
    }
}