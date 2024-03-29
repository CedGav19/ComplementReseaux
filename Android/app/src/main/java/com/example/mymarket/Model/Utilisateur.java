package com.example.mymarket.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import java.sql.SQLException;
import java.util.ArrayList;
import java.io.*;
import java.net.*;

public class Utilisateur {

    private final Context context;
    public Boolean conect = false;

    public Article articleSelect = new Article();
    private Socket cSocket;
    private DataOutputStream dos;
    private DataInputStream dis;

    private String resultat = "";

    public String MessageErr = "Rien";

    private ReadProperties prop ;

    private int idUtilisateur = 0;


    public void setIdUtilisateur(int idUtil){this.idUtilisateur = idUtil;}
    public int getIdUtilisateur(){return idUtilisateur;}

    private String requete;
    public void setRequete(String requete){this.requete = requete;}
    public String getRequete(){return requete;}

    private int numArticle = 0;
    public void setNumArticle(int numArt){this.numArticle = numArt;}
    public int getNumArticle() {return numArticle;}

    private static volatile Utilisateur instance  ;

    private ArrayList<Article> monPanier = new ArrayList<>();

    public ArrayList<Article> getMonPanier(){
        return monPanier;
    }

    public float getTotal(){
        float total =0;
        for (int i = 0; getMonPanier() != null && monPanier.size() > i ; i++) {
            total = total + monPanier.get(i).getPrix()*monPanier.get(i).getQuantite();
        }
        return total;
    }

    public void addArticlePanier(Article A){
        monPanier.add(A);
    }
    public void addArt(Article A){
        Article addTemp = new Article();
        boolean trouve = false;
        for (int i = 0;( getMonPanier() != null && monPanier.size() > i ) && trouve == false; i++) {
            if(monPanier.get(i).getIdAliment() == A.getIdAliment())
            {
                addTemp = monPanier.get(i);
                addTemp.setquantite(addTemp.getQuantite()+ A.getQuantite());
                trouve = true;
            }
        }

        if (trouve == false)
        {
            addArticlePanier(A);
        }

        for (int i = 0; getMonPanier() != null && monPanier.size() > i ; i++) {
            System.out.println("Panier client " + i + ":" + monPanier.get(i));
        }
    }
    public void removeArticlePanier(Article A){
        monPanier.remove(A);
    }

    public static Utilisateur getInstance(Context c) throws SQLException, ClassNotFoundException, IOException {
        if (instance == null) {
            synchronized (Utilisateur.class) {
                if (instance == null) {
                    instance = new Utilisateur(c);
                }
            }
        }
        return instance;
    }
    /////////////////////////////////////////////////////
    //////////////Fabrication des requetes//////////////
    ///////////////////////////////////////////////////
    public void consult() throws IOException {
        System.out.println("debut de consult ");
        requete = "CONSULT#" + (numArticle%21 +1);
        echange(requete);

        String[] mots = resultat.split("#");
        if(mots[1].equals("ok"))
        {
            articleSelect.setidAliment(Integer.parseInt(mots[2]));
            articleSelect.setintitule(mots[3]);
            articleSelect.setquantite(Integer.parseInt(mots[4]));
            articleSelect.setprix(Float.parseFloat(mots[5]));
            articleSelect.setAdrImage(mots[6]);
            //System.out.println( articleSelect);
        }
        else
        {
            System.out.println("Erreur de consult");
            MessageErr = "Erreur de consult";
        }
    }

    public boolean achat(Object quantite) throws IOException {
        if((Integer)quantite >0)
        {
            requete = "ACHAT#" + articleSelect.getIdAliment() + "#" + quantite;
            echange(requete);

            String[] mots = resultat.split("#");
            if(mots[1].equals("ok"))
            {
                Article tampon;
                tampon = new Article();
                tampon.setidAliment(articleSelect.getIdAliment());
                tampon.setintitule(mots[2]);
                tampon.setquantite((Integer) quantite);
                tampon.setprix(Float.parseFloat(mots[3]));

                addArt(tampon);
                return true ;
            }
            else
            {
                System.out.println("Erreur d achat");
                MessageErr = "Erreur d achat stock insuffisant";
                return false ;
            }
        }
        else
        {
            System.out.println("Quantite pas valide");
            MessageErr = "Quantite pas valide";
            return false ;
        }
    }

    public boolean cancell(int numArt) throws IOException {
        if(numArt != -1 )
        {
            requete = "CANCEL#" + numArt;
            echange(requete);

            String[] mots = resultat.split("#");
            if(mots[1].equals("ok"))
            {
                for (Article artPass : monPanier) {
                    if(artPass.getIdAliment() == numArt)
                    {
                        removeArticlePanier(artPass);
                        System.out.println("CANCELL_OK");
                        return true ;
                    }
                }
            }
            else
            {
                System.out.println("Erreur_CANCELL");
                MessageErr = "Erreur de cancel";

            }
        }
        else
        {
            System.out.println("CANCEL_NO_SELECT");
            MessageErr = "Veuillez sélectionner un article a supprimer !";
        }
        return false ;

    }

    public boolean cancellall() throws IOException {
        requete = "CANCELALL";
        echange(requete);

        String[] mots = resultat.split("#");
        if(mots[1].equals("ok"))
        {
            for(int j = 0 , taille = monPanier.size(); taille > j ; j++)
            {
                System.out.println("Suppression de :"+ monPanier.get(0));
                removeArticlePanier(monPanier.get(0));
            }
            System.out.println("CANCELLALL_OK");
            MessageErr = "Panier bien supprimé !";
            return true ;
        }
        else
        {
            System.out.println("CANCELALL_ERROR");
            MessageErr = "Erreur suppression panier!";
            return false;
        }
    }

    public boolean confirm() throws IOException {
        requete = "CONFIRMER#" + idUtilisateur;
        echange(requete);

        String[] mots = resultat.split("#");
        if(mots[1].equals("ok"))
        {
            monPanier.clear();
            System.out.println("Confirm_OK");
            MessageErr = "Achat bien effectué !";
            return true ;
        }
        else
        {
            System.out.println("Confirm_ERROR");
            MessageErr = "Erreur de la confirmation d achat";
        }
        return false ;
    }

    public String login(String nom , String mdp, Boolean newuser) throws IOException {

        if(nom.length() == 0 || mdp.length() == 0)
        {
            return "Les champs ne peuvent pas etre vide !";
        }

        if(newuser == true)
        {
            requete = "LOGIN#" + nom + "#" + mdp + "#1";
        }
        else
        {
            requete = "LOGIN#" + nom + "#" + mdp + "#0";
        }
        echange(requete);

        String[] mots = resultat.split("#");
        if(mots[1].equals("ok"))
        {
            System.out.println("Connect_OK");
            idUtilisateur = Integer.parseInt(mots[3]);
            MessageErr = "Bienvenue " + nom + " !";
            return "OK";
        }
        else
        {
            System.out.println("Connect_error");
            return mots[2];
        }
    }

    public Boolean logout() throws IOException {
        requete = "LOGOUT";
        echange(requete);

        String[] mots = resultat.split("#");
        if(mots[1].equals("ok"))
        {
            System.out.println("Deconnect_OK");
            idUtilisateur = 0;
            numArticle = 1;
            return true;
        }
        else
        {
            System.out.println("Deconnect_error");
            MessageErr = "Erreur lors du logout !";
            return false;
        }
        //dos.close();
        //dis.close();
        //cSocket.close();
    }

    /////////////////////////////////////////////////////
    ///////////////////Send et receive//////////////////
    ///////////////////////////////////////////////////
    public void connect() throws IOException {
        //ipServeur = getcongfig()

        prop   = new ReadProperties(context);

        String ipServeur = prop.getServerAddress();
        System.out.println(prop.getServerAddress());
        int socket = prop.getServerPort();
        cSocket = new Socket(ipServeur,socket);

        //Création des flux d'entrée et de sortie
        dos = new DataOutputStream(cSocket.getOutputStream());
        dis = new DataInputStream(cSocket.getInputStream());
    }

    private void echange(String requete) throws IOException {
        resultat = "";
        send(requete);
        resultat = receive();
        System.out.println("Recu :"+ resultat);
    }

    private void send(String trame) throws IOException {
        trame = trame + "#)";
        System.out.println("Envoyé :" + trame);
        dos.write(trame.getBytes());
        dos.flush();
    }

    private String receive() throws IOException {
        StringBuffer buffer = new StringBuffer();
        boolean EOT = false;
        byte b1,b2;

        while(!EOT)
        {
            b1 = dis.readByte();
            if(b1 ==  (byte)'#')
            {
                b2 = dis.readByte();
                if(b2 == (byte)')')
                {
                    EOT = true;
                    buffer.append((char)b1);
                    buffer.append((char)b2);
                }
                else
                {
                    buffer.append((char)b1);
                    buffer.append((char)b2);
                }
            }
            else
            {
                buffer.append((char)b1);
            }
        }
        return buffer.toString();
    }

    /////////////////////////////////////////////////////
    ///////////////////////AUTRES///////////////////////
    ///////////////////////////////////////////////////

    public void precedent() throws IOException {
        if(numArticle == 1)
        {
            numArticle = 21;
        }
        else
        {
            numArticle--;
        }
        consult();
    }

    public void suivant() throws IOException {
        if(numArticle == 21)
        {
            numArticle = 1;
        }
        else
        {
            numArticle++;
        }
        consult();
    }

    @SuppressLint("StaticFieldLeak")
    private Utilisateur(Context c1) throws IOException {
        this.context = c1.getApplicationContext();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    connect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

}
