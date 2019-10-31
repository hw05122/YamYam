package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Register extends AppCompatActivity implements View.OnClickListener {
    EditText etName, etId, etPw1, etPw2, etNick, etYear, etMonth, etDay;
    Button btnIdChk, btnNickChk, btnOk, btnCancel;
    RadioGroup rbtnGroup;
    ImageView ivPwChk;
    boolean isRunning;
    Socket memberSocket;
    String userName, userId, userPw1, userPw2, userNick, userYear, userMonth, userDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = (EditText) findViewById(R.id.etName);
        etId = (EditText) findViewById(R.id.etId);

        btnIdChk = (Button) findViewById(R.id.btnIdChk);
        btnIdChk.setOnClickListener(this);

        etPw1 = (EditText) findViewById(R.id.etPw1);
        etPw2 = (EditText) findViewById(R.id.etPw2);
        etPw1.setTransformationMethod(new PasswordTransformationMethod());
        etPw2.setTransformationMethod(new PasswordTransformationMethod());
        ivPwChk = (ImageView) findViewById(R.id.ivPwChk);
        etPw1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etPw1.getText().toString().equals((etPw2.getText().toString()))) {
                    ivPwChk.setImageResource(R.drawable.chko);
                } else {
                    ivPwChk.setImageResource(R.drawable.chkx);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        etPw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etPw1.getText().toString().equals((etPw2.getText().toString()))) {
                    ivPwChk.setImageResource(R.drawable.chko);
                } else {
                    ivPwChk.setImageResource(R.drawable.chkx);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        etNick = (EditText) findViewById(R.id.etNick);
        btnNickChk = (Button) findViewById(R.id.btnNickChk);
        btnNickChk.setOnClickListener(this);

        rbtnGroup = (RadioGroup) findViewById(R.id.rg);
        rbtnGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rbtnMan) {

                } else if (i == R.id.rbtnWoman) {

                }
            }
        });

        etYear = (EditText) findViewById(R.id.etYear);
        etMonth = (EditText) findViewById(R.id.etMonth);
        etDay = (EditText) findViewById(R.id.etDay);

        btnOk = (Button) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view == btnIdChk) {
            if (true) {
                Toast.makeText(getApplicationContext(), "사용가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
            } else if (false) {
                Toast.makeText(getApplicationContext(), "이미 사용중인 아이디입니다..", Toast.LENGTH_SHORT).show();
            }
        } else if (view == btnNickChk) {
            if (true) {
                Toast.makeText(getApplicationContext(), "사용가능한 닉네임입니다.", Toast.LENGTH_SHORT).show();
            } else if (false) {
                Toast.makeText(getApplicationContext(), "이미 사용중인 닉네임입니다..", Toast.LENGTH_SHORT).show();
            }
        } else if (view == btnOk) {
            userName = etName.getText().toString();
            userId = etId.getText().toString();
            userPw1 = etPw1.getText().toString();
            userPw2 = etPw2.getText().toString();
            userNick = etNick.getText().toString();
            userYear = etYear.getText().toString();
            userMonth = etMonth.getText().toString();
            userDay = etDay.getText().toString();
            //성별 체크안할 시 고려하기!!!!!!
            //비번X일 경우 회원가입실패뜨도록
            //생년월일은 숫자
            if (userName.isEmpty() || userId.isEmpty() || userPw1.isEmpty() || userPw2.isEmpty() || userNick.isEmpty() || userYear.isEmpty() || userMonth.isEmpty() || userDay.isEmpty()) {
                Toast.makeText(getApplicationContext(), "회원가입 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            } else {
                RegisterThread thread = new RegisterThread();
                thread.start();
            }
        } else if (view == btnCancel) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
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

    class RegisterThread extends Thread {
        public void run() {
            try {
                final Socket socket = new Socket(Login.url, 30000);
                memberSocket = socket;

                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);

                dos.writeUTF("Register "+userId+" "+userPw1);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isRunning = true;

                        RegisterCheckThread thread = new RegisterCheckThread(socket);
                        thread.start();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class RegisterCheckThread extends Thread{
        Socket socket;
        DataInputStream dis;

        public RegisterCheckThread(Socket socket){
            try {
                this.socket = socket;
                InputStream is = socket.getInputStream();
                dis = new DataInputStream(is);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        public void run(){
            try{
                while(isRunning){
                    final String msg = dis.readUTF();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(Integer.parseInt(msg) == 1){//회원가입성공
                                Toast.makeText(getApplicationContext(), "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                                finish();
                            }
                            else if(Integer.parseInt(msg) == 2){//회원가입실패
                                Toast.makeText(getApplicationContext(), "회원가입에 실패하였습니다.", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        return false;
    }
}
