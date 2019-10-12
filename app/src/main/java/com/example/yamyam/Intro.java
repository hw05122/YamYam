package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class Intro extends AppCompatActivity {
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
    }

    protected void onResume(){
        super.onResume();
        handler.postDelayed(runnable, 2500);
    }

    protected  void onPause(){
        super.onPause();
        handler.removeCallbacks(runnable);
    }
}
