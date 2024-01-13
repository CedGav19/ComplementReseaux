package com.example.mymarket.controller;

import android.os.AsyncTask;
import com.example.mymarket.Model.Utilisateur;

import java.io.IOException;
import java.sql.SQLException;

public class LogoutController {
    private Utilisateur u;

    public LogoutController() throws SQLException, IOException, ClassNotFoundException {
        this.u = Utilisateur.getInstance(null);
    }

    public void LogoutAsync(OnLogoutCompleteListener listener) {
        new LogoutTask(listener).execute();
    }

    private class LogoutTask extends AsyncTask<Void, Void, Void> {
        private final OnLogoutCompleteListener listener;

        public LogoutTask(OnLogoutCompleteListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                u.logout();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (listener != null) {
                listener.onLogoutSuccess();
            }

            return null;
        }
    }

    public interface OnLogoutCompleteListener {
        void onLogoutSuccess();
    }
}
