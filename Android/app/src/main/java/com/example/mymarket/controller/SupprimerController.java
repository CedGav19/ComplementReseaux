package com.example.mymarket.controller;

import android.content.Context;
import android.os.AsyncTask;
import com.example.mymarket.Model.Utilisateur;

import java.io.IOException;
import java.sql.SQLException;

public class SupprimerController {
    private Context context;
    private Utilisateur u;

    public SupprimerController(Context context) throws SQLException, IOException, ClassNotFoundException {
        this.context = context;
        this.u = Utilisateur.getInstance(context);
    }

    public void supprimerArticle( onSupprimerListener listener , int i ) {
        new SupprimerArticleTask(listener).execute(i);
    }

    public class SupprimerArticleTask extends AsyncTask<Integer, Void, Boolean> {

        onSupprimerListener listener;

        public SupprimerArticleTask(onSupprimerListener sl) {

            this.listener = sl;

        }

        @Override
        protected Boolean doInBackground(Integer... params) {

            try {
                return u.cancell(params[0]);
                
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(Boolean cancelSuccessful) {
            if (cancelSuccessful)
                listener.onSupprimerOk();
            else
                listener.onSupprimerError("error lors de la suppression de l'article" );

        }

    }
    public interface onSupprimerListener {
        void onSupprimerOk();

        void onSupprimerError(String errorMessage);
    }
}