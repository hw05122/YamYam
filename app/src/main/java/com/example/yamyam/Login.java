package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Login extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogin, btnRegister;
    private EditText etId, etPw;
    private int loginCnt = 0;
    private double initTime, initTime2;
    private String userId, userPw;
    public static String uName, uId, uPw, uYear, uMonth, uDay, uNick, uGen, uStar, uState, uHash1, uHash2, uHash3, uStop, uFavorites,uRoll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Login");

        etId = (EditText) findViewById(R.id.etId);
        etPw = (EditText) findViewById(R.id.etPw);
        etPw.setTransformationMethod(new PasswordTransformationMethod());

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view == btnLogin) {//로그인 버튼이 눌리면
            userId = etId.getText().toString();
            userPw = etPw.getText().toString();

            if (userId.isEmpty() || userPw.isEmpty()) {
                Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();

            } else {
                final AlertDialog.Builder starB = new AlertDialog.Builder(Login.this);
                starB.setTitle("        로그인 중 입니다").setIcon(R.drawable.logo4);
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View root = inflater.inflate(R.layout.digauto, null);
                starB.setView(root);
                starB.setCancelable(false);
                final AlertDialog adB = starB.create();
                adB.show();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) { //로그인에 성공한 경우
                                uName = jsonObject.getString("userName");
                                uId = jsonObject.getString("userID");
                                uPw = jsonObject.getString("userPassword");
                                uYear = jsonObject.getString("userYear");
                                uMonth = jsonObject.getString("userMonth");
                                uDay = jsonObject.getString("userDay");
                                uNick = jsonObject.getString("userNickname");
                                uGen = jsonObject.getString("userGender");
                                uStar = jsonObject.getString("userStar");
                                uState = jsonObject.getString("userState");
                                uHash1 = jsonObject.getString("userHashtag1");
                                uHash2 = jsonObject.getString("userHashtag2");
                                uHash3 = jsonObject.getString("userHashtag3");
                                uStop = jsonObject.getString("userStop");
                                uFavorites = jsonObject.getString("userFavorites");
                                uRoll = jsonObject.getString("userRoll");

                                Log.d("young", "받아온 아이디:" + uId + " 닉네임:" + uNick + " 정지:" + uStop);
                                if (uStop.equals("o")) {
                                    Toast.makeText(getApplicationContext(), "정지된 회원입니다.", Toast.LENGTH_SHORT).show();
                                    adB.dismiss();
                                } else {
                                    Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login.this, Main.class);
                                    startActivity(intent);
                                    finish();

                                }

                            } else { //로그인에 실패한 경우
                                Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                loginCnt++;
                                if (loginCnt == 4) {//4번째 실패부터 30초 기다려야함
                                    Toast.makeText(getApplicationContext(), "30초 후에 재입력하세요", Toast.LENGTH_SHORT).show();
                                    initTime = System.currentTimeMillis();
                                } else if (loginCnt > 4) {
                                    if (System.currentTimeMillis() - initTime < 30000) {
                                        Toast.makeText(getApplicationContext(), 30 - (int) ((System.currentTimeMillis() - initTime) / 1000) + "초 남았습니다.", Toast.LENGTH_SHORT).show();
                                    } else if (System.currentTimeMillis() - initTime >= 30000) {
                                        loginCnt = 1;
                                        Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                }
                                adB.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(userId, userPw, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Login.this);
                queue.add(loginRequest);
            }

        } else if (view == btnRegister) {
            Intent intent = new Intent(getApplicationContext(), Register.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - initTime2 > 3000) {//처음 누른 상태
                Toast.makeText(getApplicationContext(), "종료하려면 한 번 더 눌러주세요", Toast.LENGTH_SHORT).show();
                initTime2 = System.currentTimeMillis();
                return true;
            } else {//3초이내에 다시 누름
                finish();
                return true;
            }
        }

        return false;
    }
}
