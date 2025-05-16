package com.example.xbank;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;


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

        loginButton.setOnClickListener(v -> {
            try {
                attemptLogin();
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void attemptLogin() throws JSONException {
        String login = loginInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (login.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Uzupełnij wszystkie pola", Toast.LENGTH_SHORT).show();
            return;
        }

//        new Thread(() -> {
//            boolean success = false;
//            try {
//                success = checkUserCredentials(login, password);
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
//            boolean finalSuccess = success;
//            runOnUiThread(() -> {
//                if (finalSuccess) {
//                    try {
//                        AccountData account = getData(login);
//                        Toast.makeText(this, account.getAccountNumber(), Toast.LENGTH_SHORT).show();
//                    } catch (JSONException e) {
//                        throw new RuntimeException(e);
//                    }
//                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    Toast.makeText(LoginActivity.this, "Nieprawidłowy login lub hasło", Toast.LENGTH_SHORT).show();
//                }
//            });
//        }).start();

        new Thread(() -> {
            boolean success;
            try {
                success = checkUserCredentials(login, password);

                if (success) {
                    AccountData account = getData(login);  // now in background thread

                    runOnUiThread(() -> {
                        String accountNumber = account.getAccountNumber();

                        // Sprawdzenie, czy numer konta nie jest pusty
                        if (accountNumber != null && !accountNumber.isEmpty()) {
                            Toast.makeText(this, accountNumber, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Brak numeru konta", Toast.LENGTH_SHORT).show();
                        }

                        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    });

                } else {
                    runOnUiThread(() ->
                            Toast.makeText(LoginActivity.this, "Nieprawidłowy login lub hasło", Toast.LENGTH_SHORT).show()
                    );
                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }).start();


    }

    private boolean checkUserCredentials(String login, String password) throws JSONException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("login", login);
        jsonBody.put("haslo", password);
        String json = jsonBody.toString();

        String urlString = "http://10.0.2.2:8080/api/login";

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

            int responseCode = connection.getResponseCode();
            Log.d("LoginActivity", "Server response code: " + responseCode);  // Log the response code

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    responseCode >= 200 && responseCode < 300 ? connection.getInputStream() : connection.getErrorStream()))) {

                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }

                String responseJson = responseBuilder.toString();
                Log.d("LoginActivity", "Response from server: " + responseJson);  // Log the response

                boolean response = Boolean.parseBoolean(responseJson.trim());
                return response;

            }

        } catch (IOException e) {
            Log.e("LoginActivity", "Error during HTTP request", e);  // Log the error
            throw new RuntimeException(e);
        }
    }

    private AccountData getData(String login) throws JSONException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("login", login);
        String json = jsonBody.toString();

        String urlString = "http://10.0.2.2:8080/api/getAccount";

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

            int responseCode = connection.getResponseCode();
            Log.d("LoginActivity", "Get account response code: " + responseCode);  // Log response code

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    responseCode >= 200 && responseCode < 300 ? connection.getInputStream() : connection.getErrorStream()))) {

                StringBuilder responseBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    responseBuilder.append(line);
                }

                String responseJson = responseBuilder.toString();
                Log.d("LoginActivity", "Response from server: " + responseJson);  // Log server response

                // Zakładam, że odpowiedź z serwera to String JSON
                JSONObject response = new JSONObject(responseJson);
                double saldo = response.getDouble("saldo");
                String numerKonta = response.getString("numerKonta");
                String typKonta = response.getString("typKonta");

// Tworzymy obiekt Account
                AccountData account = new AccountData(saldo, numerKonta, typKonta);


                return account;

            }

        } catch (IOException e) {
            Log.e("LoginActivity", "Error during HTTP request", e);  // Log the error
            throw new RuntimeException(e);
        }
    }


}
