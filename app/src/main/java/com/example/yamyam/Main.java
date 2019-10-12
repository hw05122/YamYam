package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main extends AppCompatActivity implements View.OnClickListener{
    Button btnChat, btnMsg, btnGrow, btnCheck, btnPoint, btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnChat = (Button)findViewById(R.id.btnChat);
        btnChat.setOnClickListener(this);

        btnMsg = (Button)findViewById(R.id.btnMsg);
        btnMsg.setOnClickListener(this);

        btnGrow = (Button)findViewById(R.id.btnGrow);
        btnGrow.setOnClickListener(this);

        btnCheck = (Button)findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(this);

        btnPoint = (Button)findViewById(R.id.btnPoint);
        btnPoint.setOnClickListener(this);

        btnSetting= (Button)findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(this);
    }

    public void onClick(View view) {
        if(view == btnChat){

        }
    }
}
