package com.example.xbank;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginActivity extends AppCompatActivity {

    private EditText loginInput, passwordInput;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> attemptLogin());
    }

    private void attemptLogin() {
        String email = loginInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Uzupełnij wszystkie pola", Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkUserCredentials(email, password)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Nieprawidłowy login lub hasło", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkUserCredentials(String login, String password) {
        String json = "{ \"login\": " + login +
                ", \"haslo\": " + password + " }";

        String urlString = "http://localhost:8080/api/login";

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    connection.getResponseCode() >= 200 && connection.getResponseCode() < 300 ?
                            connection.getInputStream() : connection.getErrorStream()))) {

                boolean response = Boolean.parseBoolean(reader.readLine().trim());
                // System.out.println("Server response: " + response);

                if (response) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    Toast.makeText(this, "Login unsuccessful", Toast.LENGTH_SHORT).show();
                    return false;

                }
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
