public abstract class RobotConnecte extends Robot implements Connectable{
    protected boolean connecte;
    protected String reseauConnecte;


    public RobotConnecte(String id, int x, int y) {
        super(id, x, y);
        connecte = false;
        reseauConnecte = null;
    }
    @Override
    public void connecter(String reseau) throws RobotException {
        verifierMaintenance();
        verifierEnergie(5);
        reseauConnecte = reseau;
        connecte = true;
        consommeEnergie(5);
        ajouterHistorique("Robot connecté au réseau '" + reseau + "'");
    }
    @Override
    public void deconnecter(){
        try{
            verifierMaintenance();
            reseauConnecte = null;
            connecte = false;
            ajouterHistorique("Robot est deconnecté");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    @Override
    public void envoyerDonnees(String donnees) throws RobotException {
        if (!connecte) {
            throw new RobotException("Robot non connecté à un réseau");
        }
        verifierMaintenance();
        verifierEnergie(3);
        consommeEnergie(3);
        ajouterHistorique("Données envoyées : " + donnees);
    }

}
