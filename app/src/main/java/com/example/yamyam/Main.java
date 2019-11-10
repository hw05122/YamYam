package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class Main extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnChat, btnMsg, btnCheck, btnPoint, btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnChat = (ImageButton) findViewById(R.id.btnChat);
        btnChat.setOnClickListener(this);

        btnMsg = (ImageButton) findViewById(R.id.btnMsg);
        btnMsg.setOnClickListener(this);

        btnCheck = (ImageButton) findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(this);

        btnPoint = (ImageButton) findViewById(R.id.btnPoint);
        btnPoint.setOnClickListener(this);

        btnSetting = (ImageButton) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view == btnChat) {
            Intent intent = new Intent(getApplicationContext(), Chat.class);
            startActivity(intent);
            finish();
        } else if (view == btnMsg) {
            Intent intent = new Intent(getApplicationContext(), Message.class);
            startActivity(intent);
            finish();
        } else if (view == btnCheck) {
            Intent intent = new Intent(getApplicationContext(), Attendance.class);
            startActivity(intent);
            finish();
        } else if (view == btnPoint) {
            Intent intent = new Intent(getApplicationContext(), Point.class);
            startActivity(intent);
            finish();
        } else if (view == btnSetting) {
            Intent intent = new Intent(getApplicationContext(), Setting.class);
            startActivity(intent);
            finish();
        }
    }

    public void bookmarkShow(){

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Toast.makeText(getApplicationContext(),"설정에서 로그아웃을 눌러주세요",Toast.LENGTH_SHORT).show();
        }

        return false;
    }
}
