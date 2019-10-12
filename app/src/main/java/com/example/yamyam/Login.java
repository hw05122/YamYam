package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener{
    Button btnLogin, btnRegister;
    EditText etId, etPw;
    int loginCnt = 0;
    double initTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etId = (EditText)findViewById(R.id.etId);
        etPw = (EditText)findViewById(R.id.etPw);
        etPw.setTransformationMethod(new PasswordTransformationMethod());

        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(this);

        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(this);
    }

    public void onClick(View view) {
        if(view == btnLogin){
            if(true){//로그인 성공
                loginCnt = 0;
                Toast.makeText(getApplicationContext(),"로그인에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Main.class);
                startActivity(intent);
                finish();
            }
            else if(false){//로그인실패
                loginCnt++;

                if(loginCnt == 4){//4번째 실패부터 30초 기다려야함
                    Toast.makeText(getApplicationContext(),"30초 후에 재입력하세요",Toast.LENGTH_SHORT).show();
                    initTime = System.currentTimeMillis();
                }
                else if(loginCnt > 4){
                    if(System.currentTimeMillis() - initTime < 30000){
                        Toast.makeText(getApplicationContext(),30-(int)((System.currentTimeMillis() - initTime)/1000)+"초 남았습니다.",Toast.LENGTH_SHORT).show();
                    }
                    else if(System.currentTimeMillis() - initTime >= 30000){
                        loginCnt = 1;
                        Toast.makeText(getApplicationContext(),"아이디 또는 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"아이디 또는 비밀번호가 일치하지 않습니다.",Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if(view == btnRegister){
            Intent intent = new Intent(getApplicationContext(), Register.class);
            startActivity(intent);
        }
    }
}
