package com.example.mymarket.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mymarket.Model.Article;
import com.example.mymarket.Model.Utilisateur;
import com.example.mymarket.R;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.example.mymarket.controller.SupprimerController ;

public class CartActivity extends AppCompatActivity implements SupprimerController.onSupprimerListener {
    private List<Article> panier = new ArrayList<>();
    private Button retour ;
    private Button supprimer ;
    int articleSelectionne ;
    ArrayAdapter<Article> adaptateur ;
    SupprimerController sc  ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        articleSelectionne=-1 ;

        try {
            sc = new SupprimerController(getApplicationContext()) ;
            panier = Utilisateur.getInstance(null).getMonPanier();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        ListView listView = findViewById(R.id.listView);
        adaptateur = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, panier);

        listView.setAdapter(adaptateur);
        //todo , acheter , enlever un article , vider le panier  avec adaptateur.notifyDataSetChanged();
        TextView total = findViewById(R.id.total);
        try {
            total.setText( String.format("Total : %.2f €", Utilisateur.getInstance(getApplicationContext()).getTotal()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("hello de l'indice = "+ i);
                    articleSelectionne = i ;
            }
        });

        this.retour = (Button)  findViewById(R.id.buttonRetour);
        retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        this.supprimer = (Button)  findViewById(R.id.buttonDelete);
        supprimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("suppression de l'article : "+ articleSelectionne);
                try {
                    sc.supprimerArticle(CartActivity.this , Utilisateur.getInstance(getApplicationContext()).getMonPanier().get(articleSelectionne).getIdAliment());

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("on pass bine apres sc.supprimerarticle");
            }
        });


    }

    @Override
    public void onSupprimerOk() {
        adaptateur.notifyDataSetChanged();
    }

    @Override
    public void onSupprimerError(String errorMessage) {
        Toast.makeText(CartActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}
