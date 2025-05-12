import javax.xml.stream.FactoryConfigurationError;
import java.util.Locale;
import java.util.Scanner;

public class RobotLivraison extends RobotConnecte{
    protected int colisActuel;
    protected String destination;
    protected boolean enLivraison;
    protected static final int ENERGIE_LIVRAISON = 15;
    protected static final int ENERGIE_CHARGEMENT = 5;

    public RobotLivraison(String id, int x, int y) {
        super(id, x, y);
        colisActuel =0;
        destination =null;
        enLivraison = false;
    }

    private double distance (int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    @Override
    public void deplacer(int x, int y) throws RobotException{
        double distance= distance(this.x,this.y,x,y);
        if(distance >100)
            throw new RobotException("La distance entre la position actuelle et la destination est supérieure à 100 \nDistance = " + distance);
        verifierMaintenance();
        int coutEnergieDeplacement = (int)Math.ceil(distance*0.3);
        verifierEnergie(coutEnergieDeplacement);
        consommeEnergie(coutEnergieDeplacement);
        heuresUtilisation+= (int)Math.ceil(distance/10);
        this.x = x;
        this.y = y;
        ajouterHistorique("Robot déplacé vers les coordonnées : ("+x+","+y+")");;

    }
    protected void faireLivraison(int Destx, int Desty) throws RobotException{
        verifierMaintenance();
        verifierEnergie(ENERGIE_LIVRAISON);
        consommeEnergie(ENERGIE_LIVRAISON);
        deplacer(Destx, Desty);
        colisActuel--;
        enLivraison = false;
        ajouterHistorique("Livraison terminée à "+destination);
    }

    protected void chargerColis(String destination) throws RobotException{
        verifierMaintenance();
        if(enLivraison){
            throw new RobotException("Robot en Livraison");
        }
        if(colisActuel != 0){
            throw new RobotException("Il ya déja une colis actuellement");
        }
        verifierEnergie(ENERGIE_CHARGEMENT);
        colisActuel += 1;
        enLivraison = true;
        this.destination = destination;
        consommeEnergie(ENERGIE_CHARGEMENT);
        ajouterHistorique("Colis ajouté à la destination :"+ destination);
    }
    @Override
    public void effectuerTache() throws RobotException{
        if(!enMarche){
            throw new RobotException("Le robot doit être démarré pour effectuer une tâche");
        }
        if(enLivraison){
            Scanner s = new Scanner(System.in);
            System.out.println("Donner les coordonnees (x,y)");
            System.out.println("x = ");
            int xDest = s.nextInt();
            System.out.println("y = ");
            int yDest = s.nextInt();
            faireLivraison(xDest, yDest);
        }else {
            Scanner s2 = new Scanner(System.in);
            String reponse;
            do{
                System.out.println("Voulez vous charger un nouveau colis ? [Oui/Non] : ");
                reponse = s2.nextLine();
            }while(!reponse.equalsIgnoreCase("OUI") && !reponse.equalsIgnoreCase("NON"));
            if (reponse.equalsIgnoreCase("OUI")) {
                System.out.println("Entrez la destination : ");
                String dest = s2.nextLine();
                chargerColis(dest);
            }else {
                ajouterHistorique("En attente de colis");
            }
        }
    }
    @Override
    public String toString(){
        String isConnected;
        if(connecte){
            isConnected = "Oui";
        }else {
            isConnected = "Non";
        }
        return "Robot[ID : " + id + ", Position : (" + x + "," + y + "), Energie : " + energie + "%, Heures : " + heuresUtilisation + ",Colis :"+ colisActuel + ",Destination : "+ destination + ",Connecté : " + isConnected +"]";
    }
}
