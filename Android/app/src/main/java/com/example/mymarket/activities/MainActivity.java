package com.example.mymarket.activities;

import android.content.Intent;
import android.view.Menu;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.mymarket.Model.Article;
import com.example.mymarket.Model.Utilisateur;
import com.example.mymarket.R;
import com.example.mymarket.controller.AcheterController;
import com.example.mymarket.controller.ArticleController;
import com.example.mymarket.databinding.ActivityMainBinding ;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;

public class MainActivity extends MenuActivity implements ArticleController.OnArticleListener {



    Utilisateur u ;
    ArticleController ac ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            u = Utilisateur.getInstance(getApplicationContext());
            ac = new ArticleController(getApplicationContext());
            ac.RecupArticle(u.getNumArticle(), MainActivity.this);
            System.out.println(u.getNumArticle());
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }

       Button btnsvt = (Button) findViewById(R.id.suivantButton) ;
       btnsvt.setOnClickListener(v -> {
           System.out.println("CLICK SUR SUIVANT");
                u.setNumArticle(u.getNumArticle() + 1);
                ac.RecupArticle(u.getNumArticle(),  MainActivity.this);

        });

        Button btnprec = (Button) findViewById(R.id.precedentButton) ;
        btnprec.setOnClickListener(v -> {
            System.out.println("CLICK SUR Precedent");
            if(u.getNumArticle()<2 )u.setNumArticle(21);
            u.setNumArticle(u.getNumArticle() -1 );
            ac.RecupArticle(u.getNumArticle(),  MainActivity.this);

        });
        Button btncart = (Button) findViewById(R.id.caddieButton) ;
        btncart.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
            finish();
        });
        Button btnAcht = (Button) findViewById(R.id.acheterButton) ;

        try {
          AcheterController  ArtCont = new AcheterController(getApplicationContext());
            btnAcht.setOnClickListener(v -> {
            EditText qt = findViewById(R.id.quantity);
            int quantite = Integer.parseInt(qt.getText().toString());
            ArtCont.acheterArticleAsync(quantite, new AcheterController.OnAchatListener() {
                @Override
                public void onAchatSuccess() {
                    ac.RecupArticle(u.getNumArticle(), MainActivity.this);
                    Toast.makeText(MainActivity.this, getString(R.string.AchatArticle), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAchatError( ) {
                    Toast.makeText(MainActivity.this, getString(R.string.errorAchatArticle), Toast.LENGTH_SHORT).show();
                }
            });
        });
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public void onArticleRecup( )
    {
        updateView() ;

    }

    @Override
    public void onArticleRecupError( ) {
    }

    private void updateView() {
        System.out.println("On mets a jours la view ");
        TextView nomArticle = findViewById(R.id.nomArticle);
        TextView prixArticle = findViewById(R.id.prixArticle);
        TextView stockArticle = findViewById(R.id.stockArticle);

        ImageView imageArticle = findViewById(R.id.imageArticle) ;
        nomArticle.setText(u.articleSelect.getIntitule());
        stockArticle.setText(String.format(" %d ", u.articleSelect.getQuantite()));
        prixArticle.setText( String.format("%.2f €", u.articleSelect.getPrix()));


        String nomImage = u.articleSelect.getIntitule().toLowerCase();
        if (nomImage.equals("pommes de terre"))
            nomImage = "pommesdeterre";
        int imageResource = getResources().getIdentifier(nomImage, "drawable", getPackageName());
        imageArticle.setImageResource(imageResource);

    }
}