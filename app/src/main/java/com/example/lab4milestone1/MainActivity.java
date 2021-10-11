package com.example.lab4milestone1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    private Button startButton;
    private TextView downloadProgressText;
    private volatile boolean stopThread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startDownload(View view) {
        stopThread = false;
        ExampleRunnable runnable = new ExampleRunnable();
        new Thread(runnable).start();
    }

    public void stopDownload(View view) {
        stopThread = true;
    }

    class ExampleRunnable implements Runnable {

        public void mockFileDownloader() {

            startButton = findViewById(R.id.startButton);
            downloadProgressText = findViewById(R.id.downloadProgress);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startButton.setText("Downloading...");
                }
            });

            for (int downloadProgress = 0; downloadProgress <= 100; downloadProgress += 10) {
                if (stopThread) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            startButton.setText("Start");
                            downloadProgressText.setText("");
                        }
                    });
                    return;
                }

                int finalDownloadProgress = downloadProgress;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        downloadProgressText.setText("Download progress: " + finalDownloadProgress + "%");
                    }
                });

                Log.d(TAG, "Download progress: " + downloadProgress + "%");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startButton.setText("Start");
                    downloadProgressText.setText("");
                }
            });
        }

        @Override
        public void run() {
            mockFileDownloader();
        }
    }
}

