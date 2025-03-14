package com.example.xbank;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BlikActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView countdownText;
    private int countdownTime = 30000;  // Time in milliseconds (30 seconds)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blik);

        progressBar = findViewById(R.id.progressBar);
        countdownText = findViewById(R.id.countdownText);

        startCountdown(countdownTime);
    }

    private void startCountdown(final int timeInMillis) {
        new CountDownTimer(timeInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                // Update ProgressBar and TextView on each tick
                int progress = (int) ((millisUntilFinished / (float) timeInMillis) * 100);
                progressBar.setProgress(progress);
                countdownText.setText("Time Remaining: " + millisUntilFinished / 1000 + " seconds");
            }

            @Override
            public void onFinish() {
                // When the timer finishes
                progressBar.setProgress(0);
                countdownText.setText("Time's up!");
            }
        }.start();
    }
}