package com.example.mymarket.controller;

import android.content.Context;
import android.os.AsyncTask;
import com.example.mymarket.Model.Utilisateur;

import java.io.IOException;
import java.sql.SQLException;

public class ArticleController {
    private Utilisateur utilisateur;

    public ArticleController(Context context) throws SQLException, IOException, ClassNotFoundException {
        this.utilisateur = Utilisateur.getInstance(context);
    }

    public void RecupArticle(int articleId, OnArticleListener listener) {
        new ArticleTask(listener).execute(articleId);
    }

    private class ArticleTask extends AsyncTask<Integer, Void, Void> {
        private final OnArticleListener listener;

        public ArticleTask(OnArticleListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            try {
                System.out.println("backgorund avant consult ");
                 utilisateur.consult();
                 listener.onArticleRecup();
            } catch (Exception e) {
                // Gérer l'erreur de manière appropriée (affichage à l'utilisateur, journalisation, etc.)
                e.printStackTrace();
                listener.onArticleRecupError("error lors de la recup de l'article");
            }
            return null;
        }


    }

    // Interface pour écouter la fin de la récupération d'article
    public interface OnArticleListener {
        void onArticleRecup( );
        void onArticleRecupError(String errorMessage);
    }
}