package Modele.Serveur;

import Modele.Protocole.Protocole;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.*;
import java.security.cert.CertificateException;

public  class ThreadServeur extends Thread
{

    private FileAttente connexionsEnAttente;
    private ThreadGroup pool;
    private int taillePool;

    protected int port;

    protected Protocole protocole;
    //protected ServerSocket ssocket;
    protected SSLServerSocket sslServerSocket;
   protected SSLSocket sslSocket ;

    public ThreadServeur(int port, Protocole protocole,int taillePool) throws
            IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        super("TH Serveur (port=" + port + ",protocole=" + protocole.getNom() + ")");
        this.port = port;
        this.protocole = protocole;


            // Chargement du keystore du serveur
            KeyStore serverKeyStore = KeyStore.getInstance("JKS");
            char[] serverPassphrase = "cedric".toCharArray();
            try (InputStream keyStoreInputStream = new FileInputStream("C:\\certificat\\server_keystore.jks")) {
                serverKeyStore.load(keyStoreInputStream, serverPassphrase);
            }

            // Configuration du gestionnaire de clés pour le keystore du serveur
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(serverKeyStore, serverPassphrase);

            // Configuration du gestionnaire de confiance pour le keystore du serveur
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
            tmf.init(serverKeyStore);

            // Configuration du contexte SSL
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

            // Création de la SSLServerSocketFactory
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();

            // Création de la SSLServerSocket
             sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port);



        connexionsEnAttente = new FileAttente();
        pool = new ThreadGroup("POOL");
        this.taillePool = taillePool;
    }



    @Override
    public void run()
    {
        try
        {
            for (int i=0 ; i<taillePool ; i++)
            {
                new ThreadPaiement(protocole , connexionsEnAttente,pool).start();
            }
        } catch (IOException e)
        {
           throw new RuntimeException(e);
        }

        // Attente des connexions
        while(!this.isInterrupted())
        {
            SSLSocket csocket = null;
            try
            {
              // ssocket.setSoTimeout(2000);
                 sslSocket = (SSLSocket) sslServerSocket.accept();
                System.out.println("Connexion établie.");
                connexionsEnAttente.addConnexion(sslSocket);

            }
            catch (SocketTimeoutException ex)
            {
                System.out.println("exception socketTimeout dans le thread Serveur");
            }
            catch (IOException ex)
            {
                System.out.println("Erreur I/O");
            }
        }
        pool.interrupt();
        try {
            sslServerSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("le thread serveur est interompu ");

    }
}
