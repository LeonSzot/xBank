package com.example.xbank;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.WindowInsetsCompat;


import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button infoBtn;
    ImageButton blikBtn;
    TextView balanceTxt;
    TextView cardNumber;
    TextView CVV;
    TextView date;
    TextView name;
    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
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
        balanceTxt = findViewById(R.id.balanceTxt);
        infoBtn = findViewById(R.id.infoBtn);
        blikBtn = findViewById(R.id.blikBtn);
        cardNumber = findViewById(R.id.cardNumber);
        CVV = findViewById(R.id.CVV);
        date = findViewById(R.id.date);
        name = findViewById(R.id.name);
        double balance = getIntent().getDoubleExtra("balance", 0);
        String accountNumber = getIntent().getStringExtra("accountNumber");
        String accountType = getIntent().getStringExtra("accountType");
        balanceTxt.setText(Double.toString(balance));
        blikBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Uruchomienie zapytania GET w osobnym wątku
                new GetRequestTask(MainActivity.this).execute("http://10.0.2.2:8080/api/newblik/1");
            }
        });
        infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Toast.makeText(MainActivity.this, accountNumber + "\n" + accountType, Toast.LENGTH_SHORT).show();
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
            String zeros = "";
            for (int i = 6 - result.length(); i > 0; i--) {
                zeros += '0';
            }
            zeros += result;

            Intent intent = new Intent(mContext, BlikActivity.class);
            intent.putExtra("BLIK_CODE", zeros);  // Przekazujemy kod Blik
            mContext.startActivity(intent);

        }

    }
}
