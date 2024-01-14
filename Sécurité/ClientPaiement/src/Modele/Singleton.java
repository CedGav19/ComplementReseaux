package Modele;

import Modele.Protocole.Facture.ReponseGetFacture;
import Modele.Protocole.Facture.ReponseGetFactureTab;
import Modele.Protocole.Facture.RequeteGetFacture;
import Modele.Protocole.LOGOUT.RequeteLOGOUT;
import Modele.Protocole.Login.ReponseLOGIN;
import Modele.Protocole.Login.RequeteLOGIN;
import Modele.Protocole.Paiement.ReponsePaiement;
import Modele.Protocole.Paiement.RequetePaiement;

import javax.net.ssl.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

public class Singleton {

    ArrayList<ReponseGetFacture> factures ;
    private static Singleton instance;

    static {
        try {
            instance = new Singleton();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Singleton getInstance() {
        return instance;
    }

    private String numVISA ;
    private String nomVISA ;
    private String idClient ;
    private String nomEmploye ;
    private String mdpEmploye;

    SSLSocket sslSocket;

    // Création de la socket et connexion sur le serveur


    private ObjectOutputStream oos;
    private ObjectInputStream ois;


    private Singleton() throws IOException {

        oos =null ;
        ois = null  ;



    }
    public void seConnecter()
    {

    }

    public boolean envoyerRequeteLOGIN(String u , String mdp) throws IOException, ClassNotFoundException, KeyStoreException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {

        Properties P = new Properties() ;
        P.load(new FileInputStream(System.getProperty("user.dir")+"\\properties.properties"));


        String serverAddress = "localhost"; // Remplacez par l'adresse de votre serveur
        int serverPort = 12345; // Remplacez par le port que votre serveur utilise

            // Chargement du keystore du client
            KeyStore clientKeyStore = KeyStore.getInstance("JKS");
            char[] clientPassphrase = "cedric".toCharArray();
            try (InputStream keyStoreInputStream = new FileInputStream("C:\\certificat\\client_keystore.jks")) {
                clientKeyStore.load(keyStoreInputStream, clientPassphrase);
            } catch (CertificateException e) {
                throw new RuntimeException(e);
            }

            // Configuration du gestionnaire de clés pour le keystore du client
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(clientKeyStore, clientPassphrase);

            // Configuration du gestionnaire de confiance pour le keystore du client
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(clientKeyStore);

            // Configuration du contexte SSL
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            // Création de la SSLSocketFactory
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            // Création de la SSLSocket
             sslSocket = (SSLSocket) sslSocketFactory.createSocket(P.getProperty("ipAdress"),Integer.parseInt(P.getProperty("socket")));

         oos = new ObjectOutputStream(sslSocket.getOutputStream());
         ois = new ObjectInputStream(sslSocket.getInputStream());



        RequeteLOGIN requete = new RequeteLOGIN(u,mdp);

        oos.writeObject(requete);
        ReponseLOGIN reponse = (ReponseLOGIN) ois.readObject();
        System.out.println("reponse du serveur pour login : " + reponse.isValide());
        if (reponse.isValide())
        {
            System.out.println("connexion confirmée");
            return true  ;
        }
        else {

            sslSocket.close();
            return false  ;

        }

    }
    public void envoyerRequeteLOGOUT() throws IOException, ClassNotFoundException {

        RequeteLOGOUT requete = new RequeteLOGOUT("admin");
        oos.writeObject(requete);
        sslSocket.close();


    }
    public boolean envoyerRequeteGetFactures(String id) throws IOException, ClassNotFoundException {

        factures=new ArrayList<>() ;
        RequeteGetFacture requete = new RequeteGetFacture(id);
        oos.writeObject(requete);
        ReponseGetFactureTab reponse = (ReponseGetFactureTab) ois.readObject();

        for(int i = 0 ; i<reponse.getTabFactures().size();i++)
        {
            if(reponse.getTabFactures().get(i).isPaye()==false)
            {
                System.out.println(reponse.getTabFactures().get(i).getMontant());
                factures.add(reponse.getTabFactures().get(i)) ;
            }

        }
        if(reponse.getTabFactures().size()!=0)
        {
            return true ;
        }
        else return false ;

    }

    public boolean envoyerRequetePayer(String id , String nom ,String visa ) throws IOException, ClassNotFoundException {


        RequetePaiement requete = new RequetePaiement(id , nom , visa);
        oos.writeObject(requete);
        ReponsePaiement  reponse = (ReponsePaiement) ois.readObject();
        System.out.println("reponse du serveur pour la requete de payement de la facture : id " + reponse);
        
        return reponse.isOk() ;
    }


    public ArrayList<ReponseGetFacture> getFactures()
    {
        return factures ;
    }
    public String getIdClient() {
        return idClient;
    }

    public String getMdpEmploye() {
        return mdpEmploye;
    }

    public String getNomEmploye() {
        return nomEmploye;
    }

    public String getNomVISA() {
        return nomVISA;
    }

    public String getNumVISA() {
        return numVISA;
    }

    public Socket getCsocket() {
        return sslSocket;
    }


}

