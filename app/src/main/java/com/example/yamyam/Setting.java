package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Setting extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogout, btnEnd;
    private String userPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTitle("설정");

        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
        btnEnd = (Button) findViewById(R.id.btnEnd);
        btnEnd.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view == btnLogout) {
            Toast.makeText(getApplicationContext(), "로그아웃을 하였습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else if (view == btnEnd) {
            AlertDialog.Builder digEnd = new AlertDialog.Builder(Setting.this);
            digEnd.setMessage("탈퇴를 하시겠습니까?");
            digEnd.setNegativeButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    AlertDialog.Builder digPw = new AlertDialog.Builder(Setting.this);
                    digPw.setTitle("비밀번호를 입력하세요");
                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                    View root = inflater.inflate(R.layout.digpw, null);
                    digPw.setView(root);

                    final EditText etPw = (EditText)root.findViewById(R.id.etPw);
                    etPw.setTransformationMethod(new PasswordTransformationMethod());

                    digPw.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            userPw = etPw.getText().toString();
                            String uId = Login.uId;
                            String uPw = Login.uPw;

                            if (userPw.isEmpty()) {
                                Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                            } else if (userPw.equals(uPw)) {
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean success = jsonObject.getBoolean("success");
                                            if(success){
                                                Toast.makeText(getApplicationContext(), "탈퇴되었습니다.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(Setting.this, Login.class);
                                                startActivity(intent);
                                            } else {
                                                Toast.makeText(getApplicationContext(),"탈퇴실패하였습니다.", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                };

                                EndRequest endRequest = new EndRequest(uId, uPw,Login.uNick, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(Setting.this);
                                queue.add(endRequest);

                            } else {
                                Toast.makeText(getApplicationContext(), "비밀번호가 일치하지않습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    digPw.setPositiveButton("취소",null);
                    digPw.show();
                }
            });
            digEnd.setPositiveButton("아니요",null);
            digEnd.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(getApplicationContext(), Main.class);
            startActivity(intent);
            finish();
        }

        return false;
    }
}
