package com.example.yamyam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

public class Main extends AppCompatActivity implements View.OnClickListener {
    Button btnChat, btnMsg, btnGrow, btnCheck, btnPoint, btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnChat = (Button) findViewById(R.id.btnChat);
        btnChat.setOnClickListener(this);

        btnMsg = (Button) findViewById(R.id.btnMsg);
        btnMsg.setOnClickListener(this);

        btnGrow = (Button) findViewById(R.id.btnGrow);
        btnGrow.setOnClickListener(this);

        btnCheck = (Button) findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(this);

        btnPoint = (Button) findViewById(R.id.btnPoint);
        btnPoint.setOnClickListener(this);

        btnSetting = (Button) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view == btnChat) {
            Intent intent = new Intent(getApplicationContext(), Chat.class);
            startActivity(intent);
        } else if (view == btnMsg) {

        } else if (view == btnGrow) {

        } else if (view == btnCheck) {
            Intent intent = new Intent(getApplicationContext(), Attendance.class);
            startActivity(intent);
        } else if (view == btnPoint) {

        } else if (view == btnSetting) {
            Intent intent = new Intent(getApplicationContext(), Setting.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Toast.makeText(getApplicationContext(),"설정에서 로그아웃을 눌러주세요",Toast.LENGTH_SHORT).show();
        }

        return false;
    }
}
