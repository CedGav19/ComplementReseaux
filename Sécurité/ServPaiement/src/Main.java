import Modele.BD.AccesBD;
import Modele.Protocole.Login.RequeteLOGIN;
import Vue.ServeurVue;

import Controller.Controller ;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.ResultSet;
import java.sql.SQLException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        Controller c = new Controller();
        Security.addProvider(new BouncyCastleProvider());


    }
}