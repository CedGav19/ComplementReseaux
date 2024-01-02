package com.example.mymarket.Model;


import android.content.Context;
import android.content.res.AssetManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadProperties {
    private static final String CONFIG_FILE = "local.properties";
    private Properties properties;

    public ReadProperties(Context context) {
        properties = new Properties();
        AssetManager assetManager = context.getAssets();
        try (InputStream inputStream = assetManager.open("prop.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getServerAddress() {
        return properties.getProperty("serveur_adresse");
    }

    public int getServerPort() {
        return Integer.parseInt(properties.getProperty("serveur_port"));
    }



}
