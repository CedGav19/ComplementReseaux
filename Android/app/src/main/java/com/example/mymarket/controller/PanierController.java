package com.example.mymarket.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mymarket.Model.Article;
import com.example.mymarket.R;

import java.util.List;

public class PanierController extends RecyclerView.Adapter<PanierController.ArticleViewHolder> {
    private final List<Article> articles;

    public PanierController(List<Article> articles) {
        this.articles = articles;
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder {
        TextView nomOP;
        TextView prixOP;
        TextView quantityOP;
        ImageButton deleteOP;

        ArticleViewHolder(View itemView) {
            super(itemView);
            nomOP = itemView.findViewById(R.id.nomOP);
            prixOP = itemView.findViewById(R.id.prixOP);
            quantityOP = itemView.findViewById(R.id.quantityOP);
            deleteOP = itemView.findViewById(R.id.deleteOP);
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.objet_panier, parent, false);
        return new ArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.nomOP.setText(article.getIntitule());
        holder.prixOP.setText(String.valueOf(article.getPrix()));
        holder.quantityOP.setText(String.valueOf(article.getQuantite()));

    }



}
