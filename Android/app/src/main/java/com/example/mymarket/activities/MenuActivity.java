package com.example.mymarket.activities;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymarket.R;
import com.example.mymarket.controller.LanguageController;
import com.example.mymarket.controller.LogoutController;

import java.io.IOException;
import java.sql.SQLException;

public class MenuActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menuParametre) {
            handleMenuLangue();
            return true;
        } else if (id == R.id.menuDeconnexion) {
            handleMenuLogout();
            return true;
        }
        if (id == android.R.id.home) {
            handleMenuBack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuLangue() {
        LanguageController.changeLanguage(this);
        recreate();
    }

    protected void handleMenuBack() {
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(intent);
    }

    protected void handleMenuLogout() {
        try {
            new LogoutController().LogoutAsync(() -> {
                Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
                startActivity(intent);
            });
        } catch (SQLException | IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
