
package com.example.mymarket.controller;
import android.content.Context;
import android.os.AsyncTask;
import com.example.mymarket.Model.Article;
import com.example.mymarket.Model.Utilisateur;

import java.io.IOException;
import java.sql.SQLException;

public class AcheterController {

    private Utilisateur u;

    public AcheterController(Context context) throws IOException, SQLException, ClassNotFoundException {
        this.u = u.getInstance(context);
    }

    public void acheterArticleAsync(int quantite, OnAchatListener listener) {
        new AchatArticleTask(listener).execute(quantite);
    }

    private class AchatArticleTask extends AsyncTask<Integer, Void, Boolean> {
        private final OnAchatListener listener;

        public AchatArticleTask(OnAchatListener listener) {
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            try {
                int quantite = params[0];
                return u.achat(quantite);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

            @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                listener.onAchatSuccess();
            } else {
                listener.onAchatError();
            }
        }
    }

    // Interface pour écouter la fin de l'achat d'article
    public interface OnAchatListener {
        void onAchatSuccess();
        void onAchatError();
    }
}
