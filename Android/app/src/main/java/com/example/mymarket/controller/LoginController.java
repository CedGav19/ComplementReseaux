package com.example.mymarket.controller;

import android.content.Context;
import android.os.AsyncTask;
import com.example.mymarket.Model.Utilisateur;

import java.io.IOException;
import java.sql.SQLException;
public class LoginController {

    private Context context;
    private Utilisateur utilisateur;

    public LoginController(Context context) throws SQLException, IOException, ClassNotFoundException {
        this.context = context;
        this.utilisateur = Utilisateur.getInstance(context);
    }

    public void performLoginAsync(String username, String password, OnLoginCompleteListener listener) {
        new LoginTask(listener).execute(username, password);
    }

    private class LoginTask extends AsyncTask<String, Void, Void> {
        private final OnLoginCompleteListener listener;

        public LoginTask(OnLoginCompleteListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(String... params) {
            String success = "";

            if (params[0] == null || params[0].equals("") || params[1] == null || params[1].equals("")){
                listener.onLoginFailed();
                return null;
            }

            try {
                success = utilisateur.login(params[0], params[1], false);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (listener != null) {
                if (success=="OK")
                    listener.onLoginComplete();
                else
                    listener.onLoginFailed();
            }

            return null;
        }
    }

    public interface OnLoginCompleteListener {
        void onLoginComplete();

        void onLoginFailed();
    }
}
