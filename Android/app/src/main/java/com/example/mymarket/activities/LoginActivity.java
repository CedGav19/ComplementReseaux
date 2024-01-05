package com.example.mymarket.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mymarket.Model.Utilisateur;
import com.example.mymarket.R;
import com.example.mymarket.controller.LoginController;
import com.example.mymarket.databinding.ActivityLoginBinding;
import java.io.IOException;
import java.sql.SQLException;

public class LoginActivity extends AppCompatActivity {
     private Button login ;
     LoginController lc ;
    Utilisateur u;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.login = (Button)  findViewById(R.id.buttonLogin);
        try {
            u= Utilisateur.getInstance(getApplicationContext());
            lc= new LoginController(getApplicationContext());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        login.setOnClickListener( new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                System.out.println("hello trying to login...");

                EditText name = (EditText) findViewById(R.id.Name);
                EditText pwd = (EditText) findViewById(R.id.Password);
                lc.performLoginAsync(
                        name.getText().toString() ,
                        pwd.getText().toString(),
                        new LoginController.OnLoginCompleteListener() {
                            @Override
                            public void onLoginComplete() {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onLoginFailed() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this, "Login failed. Please try again.", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }
                        }
                );

            }

        });
    }
}
