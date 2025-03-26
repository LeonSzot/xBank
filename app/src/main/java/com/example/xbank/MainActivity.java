package com.example.xbank;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    ImageButton blikBtn;
    TextView kodBlik;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        blikBtn = findViewById(R.id.blikBtn);
        blikBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Uruchomienie zapytania GET w osobnym wątku
                new GetRequestTask(MainActivity.this).execute("http://10.0.2.2:8080/api/newblik/1");

                // Opcjonalnie, można przejść do BlikActivity
                Intent intent = new Intent(MainActivity.this, BlikActivity.class);
                startActivity(intent);
            }
        });
    }

    // AsyncTask do wykonania zapytania GET w tle
    private static class GetRequestTask extends AsyncTask<String, Void, String> {
        private final MainActivity mContext;

        // Konstruktor przyjmujący kontekst
        public GetRequestTask(MainActivity context) {
            this.mContext = context;
        }

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            try {
                // Tworzymy URL
                URL url = new URL(urls[0]);

                // Tworzymy połączenie HTTP
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                // Sprawdzamy odpowiedź
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // 200
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String inputLine;
                    StringBuilder response = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();
                    result = response.toString();
                } else {
                    result = "Error: " + responseCode;
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = "Exception: " + e.getMessage();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // Wyświetlamy Toast z wynikiem w kontekście MainActivity
            Toast.makeText(mContext, "Response: " + result, Toast.LENGTH_LONG).show();
            //TODO make blikCode show on blik activity and fix blik showing when 1st number is 0
        }
    }
}
