package com.example.mymarket.controller;
import android.content.Context;
import android.os.AsyncTask;
import com.example.mymarket.Model.Utilisateur;

import java.io.IOException;
import java.sql.SQLException;

public class ConfirmerAchatController {

    private Utilisateur u;

    public ConfirmerAchatController(Context context) throws IOException, SQLException, ClassNotFoundException {
        this.u = u.getInstance(context);
    }

    public void confirmerAchatAsync(OnAchatConfirmListener listener) {
        new confirmerAchatTask(listener).execute();
    }

    private class confirmerAchatTask extends AsyncTask<Void, Void, Boolean> {
        private final OnAchatConfirmListener listener;

        public confirmerAchatTask(OnAchatConfirmListener listener) {
            this.listener = listener;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                return u.confirm()  ;
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
    public interface OnAchatConfirmListener {
        void onAchatSuccess();
        void onAchatError();
    }
}
