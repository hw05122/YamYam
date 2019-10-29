package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Login extends AppCompatActivity implements View.OnClickListener {
    Button btnLogin, btnRegister;
    EditText etId, etPw;
    int loginCnt = 0;
    double initTime;
    boolean isRunning;
    Socket memberSocket;
    public String userId, userPw;
    public static Context context;
    public static String url = "192.168.0.8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etId = (EditText) findViewById(R.id.etId);
        etPw = (EditText) findViewById(R.id.etPw);
        etPw.setTransformationMethod(new PasswordTransformationMethod());

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);

        context = this;
    }

    public void onClick(View view) {
        if (view == btnLogin) {//로그인 버튼이 눌리면
            userId = etId.getText().toString();
            userPw = etPw.getText().toString();

            if (userId.isEmpty() || userPw.isEmpty()) {
                Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();

            } else {//사용자가 무언가 입력을 한채 로그인 눌렀다면 LoginThread실행
                LoginThread thread = new LoginThread();
                thread.start();
            }

        } else if (view == btnRegister) {
            Intent intent = new Intent(getApplicationContext(), Register.class);
            startActivity(intent);
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        try {
            memberSocket.close();
            isRunning = false;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //LoginThread는 자바로 dos.writeUTF()를 통해 userId와 userPw를 보내줌
    //보낼 msg들은 무조건 " "띄어쓰기 해서 보내기 (token으로 나누기 위해서)
    //그리고나서 LoginCheckThread 실행
    class LoginThread extends Thread {
        public void run() {
            try {
                final Socket socket = new Socket(url, 30000);
                memberSocket = socket;

                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);

                dos.writeUTF("Login " + userId + " " + userPw);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isRunning = true;

                        LoginCheckThread thread = new LoginCheckThread(socket);
                        thread.start();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //LoginCheckThread는 자바에서 보낸문자열을 dis.readUTF()를 통해 받음
    //자바를 통해 디비내용을 체크한 뒤 msg를 보내줘서 그에 따른 실행이 되도록
    class LoginCheckThread extends Thread {
        Socket socket;
        DataInputStream dis;

        public LoginCheckThread(Socket socket) {
            try {
                this.socket = socket;
                InputStream is = socket.getInputStream();
                dis = new DataInputStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                while (isRunning) {
                    final String msg = dis.readUTF();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (Integer.parseInt(msg) == 1) {
                                Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Main.class);
                                startActivity(intent);
                                finish();
                            } else if (Integer.parseInt(msg) == 2) {//로그인실패
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
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
