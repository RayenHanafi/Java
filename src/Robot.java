import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
public abstract class Robot{
    protected String id;
    protected int x;
    protected int y;
    protected int energie;
    protected int heuresUtilisation;
    protected boolean enMarche;
    protected List<String> historiqueActions;
    protected PanneauSolaire panneauSolaire;

    public Robot(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.energie = 100;
        this.heuresUtilisation = 0;
        this.enMarche = false;
        this.historiqueActions = new ArrayList<>();
        ajouterHistorique("Robot créé");

    }
    protected void ajouterHistorique(String action) {
        LocalDateTime maintenant = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MMMM yyyy HH:mm:ss");
        String dateAction = maintenant.format(format) + " " + action;
        historiqueActions.add(dateAction);
    }
    protected void verifierEnergie(int energieRequise) throws EnergieInsuffisanteException{
        if (energieRequise > energie){
            throw new EnergieInsuffisanteException("Energie Insuffisante !!!");
        }
    }
    protected void verifierMaintenance() throws MaintenanceRequiseException{
        if (heuresUtilisation >= 100){
            throw new MaintenanceRequiseException("Maintenance Requise !!!\nNombre d'heure d'utilisation = "+ heuresUtilisation+">= 100 heures !! ");
        }
    }
    public void demarrer() throws RobotException{
        verifierMaintenance();
        if (energie >= 10){
            enMarche = true;
            ajouterHistorique("Robot demarrer");
        }
        else{
            throw new RobotException("Pas assez d'énergie pour démarrer\nEnergie = " + energie + "%, Energie demandée = 10%");
        }
    }
    public void arreter(){
        enMarche = false;
        ajouterHistorique("Robot arreter");
    }
    public void consommeEnergie(int quantite){
        energie = Math.max(0, energie - quantite);
    }
    public void recharger(int quantite){
        energie = Math.min(100, energie + quantite);
    }
    public abstract void deplacer(int x, int y) throws RobotException;
    public abstract void effectuerTache() throws RobotException;
    public String getHistorique(){
        return String.join("\n", historiqueActions);
    }
    @Override
    public String toString() {
        return "Robot[ID : " + id + ", Position : (" + x + "," + y + "), Energie : " + energie + "%, Heures : " + heuresUtilisation + "]";
    }

    public String getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getEnergie() {
        return energie;
    }

    public void installerPanneauSolaire() {
        if(panneauSolaire == null) {
            panneauSolaire = new PanneauSolaire(this);
            ajouterHistorique("Panneau solaire installé");
        }
    }

    public boolean aPanneauSolaire() {
        return panneauSolaire != null;
    }
}
