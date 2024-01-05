package com.example.mymarket.activities;

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

public class CartActivity extends AppCompatActivity {
    private List<Article> panier = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        try {
            panier = Utilisateur.getInstance(null).getMonPanier();
        } catch (SQLException | ClassNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        ListView listView = findViewById(R.id.listView);
        ArrayAdapter<Article> adaptateur = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, panier);

        listView.setAdapter(adaptateur);
        panier.add(new Article()) ;
        panier.add((new Article(2,"salami", 12.3F,1,""))) ;
        adaptateur.notifyDataSetChanged();
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
                // les indice commencent a l'indice 0
            }
        });

    }

}
