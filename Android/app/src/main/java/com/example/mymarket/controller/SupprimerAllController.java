package com.example.mymarket.controller;

import android.content.Context;
import android.os.AsyncTask;
import com.example.mymarket.Model.Utilisateur;
import com.example.mymarket.activities.CartActivity;

import java.io.IOException;
import java.sql.SQLException;

public class SupprimerAllController {
    private Context context;
    private Utilisateur u;

    public SupprimerAllController(Context context) throws SQLException, IOException, ClassNotFoundException {
        this.context = context;
        this.u = Utilisateur.getInstance(context);
    }

    public void supprimerAllArticle(CartActivity listener) {
        new SupprimerAllController.SupprimerAllArticleTask(listener).execute();
    }

    public class SupprimerAllArticleTask extends AsyncTask< Integer, Void, Boolean> {

        onSupprimerAllListener listener;

        public SupprimerAllArticleTask(onSupprimerAllListener sl) {

            this.listener = sl;
        }

        @Override
        protected Boolean doInBackground(Integer... params) {

            try {
                return u.cancellall();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        protected void onPostExecute(Boolean cancelSuccessful) {
            if (cancelSuccessful)
                listener.onSupprimerAllOk();
            else
                listener.onSupprimerAllError( );
        }
    }

    public interface onSupprimerAllListener {
        void onSupprimerAllOk();

        void onSupprimerAllError( );
    }
}
