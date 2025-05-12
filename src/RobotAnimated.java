import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class RobotAnimated extends JFrame {
    private RobotLivraison robot;
    private JTextField xField, yField, destField;
    private JTextArea logArea;
    private JLabel energieLabel;
    private RobotPanel robotPanel;
    private JButton toggleSolarBtn;
    private JLabel solarStatus;

    public RobotAnimated() {
        robot = new RobotLivraison("R1", 0, 0);

        setTitle("Interface de Contrôle du Robot");
        setSize(600, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel northContainer = new JPanel();
        northContainer.setLayout(new BoxLayout(northContainer, BoxLayout.Y_AXIS));

        JPanel controlPanel = new JPanel(new GridLayout(4, 2));
        controlPanel.setBackground(new Color(220, 240, 255));

        xField = new JTextField();
        yField = new JTextField();
        destField = new JTextField();

        controlPanel.add(new JLabel("X:"));
        controlPanel.add(xField);
        controlPanel.add(new JLabel("Y:"));
        controlPanel.add(yField);
        controlPanel.add(new JLabel("Destination:"));
        controlPanel.add(destField);

        energieLabel = new JLabel("Énergie: " + robot.getEnergie() + "%");
        controlPanel.add(energieLabel);

        JPanel solarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        solarPanel.setBackground(new Color(220, 240, 255));
        JButton installSolarBtn = new JButton("🟡 Installer Panneau");
        toggleSolarBtn = new JButton("🌞 Toggle Lumière");
        solarStatus = new JLabel("Panneau: Non installé");

        installSolarBtn.setBackground(new Color(255, 204, 51));
        toggleSolarBtn.setEnabled(false);

        solarPanel.add(installSolarBtn);
        solarPanel.add(toggleSolarBtn);
        solarPanel.add(solarStatus);

        northContainer.add(controlPanel);
        northContainer.add(solarPanel);
        add(northContainer, BorderLayout.NORTH);

        logArea = new JTextArea(6, 20);
        logArea.setEditable(false);
        logArea.setBackground(new Color(245, 245, 245));
        JScrollPane scrollPane = new JScrollPane(logArea);
        add(scrollPane, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        robotPanel = new RobotPanel();
        centerPanel.add(robotPanel, BorderLayout.CENTER);

        JPanel boutonPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        boutonPanel.setBackground(new Color(220, 240, 255));

        JButton startBtn = new JButton("Démarrer");
        JButton stopBtn = new JButton("Arrêter");
        JButton moveBtn = new JButton("Déplacer");
        JButton chargeBtn = new JButton("Charger");
        JButton deliverBtn = new JButton("Livrer");
        JButton rechargeBtn = new JButton("Recharger");

        boutonPanel.add(startBtn);
        boutonPanel.add(stopBtn);
        boutonPanel.add(moveBtn);
        boutonPanel.add(chargeBtn);
        boutonPanel.add(deliverBtn);
        boutonPanel.add(rechargeBtn);

        centerPanel.add(boutonPanel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        installSolarBtn.addActionListener(e -> {
            robot.installerPanneauSolaire();
            solarStatus.setText("Panneau: Installé");
            toggleSolarBtn.setEnabled(true);
            installSolarBtn.setEnabled(false);
            log("Panneau solaire installé");
        });

        toggleSolarBtn.addActionListener(e -> {
            robot.panneauSolaire.toggleExposition();
            String status = robot.panneauSolaire.estExpose() ? "Exposé" : "Non exposé";
            solarStatus.setText("Panneau: " + status);
            log("Exposition solaire: " + status);
        });

        startBtn.addActionListener(e -> {
            try {
                robot.demarrer();
                log("Robot démarré.");
            } catch (Exception ex) {
                log("Erreur : " + ex.getMessage());
            }
        });

        stopBtn.addActionListener(e -> {
            robot.arreter();
            log("Robot arrêté.");
        });

        moveBtn.addActionListener(e -> {
            try {
                int x = Integer.parseInt(xField.getText());
                int y = Integer.parseInt(yField.getText());
                robot.deplacer(x, y);
                log("Déplacement vers (" + x + ", " + y + ") effectué.");
                updateUIState();
            } catch (Exception ex) {
                log("Erreur : " + ex.getMessage());
            }
        });

        chargeBtn.addActionListener(e -> {
            try {
                String destination = destField.getText();
                robot.chargerColis(destination);
                log("Colis chargé pour : " + destination);
                updateUIState();
            } catch (Exception ex) {
                log("Erreur : " + ex.getMessage());
            }
        });

        deliverBtn.addActionListener(e -> {
            try {
                int x = Integer.parseInt(xField.getText());
                int y = Integer.parseInt(yField.getText());
                robot.faireLivraison(x, y);
                log("Livraison effectuée vers (" + x + ", " + y + ")");
                updateUIState();
            } catch (Exception ex) {
                log("Erreur : " + ex.getMessage());
            }
        });

        rechargeBtn.addActionListener(e -> {
            robot.recharger(20);
            log("Recharge effectuée (+20%). Énergie actuelle : " + robot.getEnergie() + "%");
            updateUIState();
        });

        Timer updateTimer = new Timer(1000, e -> updateUIState());
        updateTimer.start();

        setVisible(true);
    }

    private void log(String message) {
        logArea.append(message + "\n");
    }

    private void updateUIState() {
        energieLabel.setText("Énergie: " + robot.getEnergie() + "%");
        robotPanel.repaint();
    }

    class RobotPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.white);

            g.setColor(Color.BLUE);
            int size = 20;
            g.fillOval(robot.getX() * 5, robot.getY() * 5, size, size);

            g.setColor(Color.BLACK);
            g.drawString("Robot: " + robot.getId(), robot.getX() * 5, robot.getY() * 5 - 5);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RobotAnimated::new);
    }
}