package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity implements View.OnClickListener {
    private EditText etName, etId, etPw1, etPw2, etNick, etYear, etMonth, etDay;
    private Button btnIdChk, btnNickChk, btnOk, btnCancel;
    private RadioGroup rbtnGroup;
    private String userName, userId, userPw1, userPw2, userNick, userYear, userMonth, userDay, userGen;
    private boolean idChk = false, nickChk = false;
    private String userStar = "0.0", userState = "o", userHashtag1 = "", userHashtag2 = "", userHashtag3 = "", userStop = "x", userGrade ="얌";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("회원가입");

        etName = (EditText) findViewById(R.id.etName);
        etId = (EditText) findViewById(R.id.etId);

        btnIdChk = (Button) findViewById(R.id.btnIdChk);
        btnIdChk.setOnClickListener(this);

        etPw1 = (EditText) findViewById(R.id.etPw1);
        etPw2 = (EditText) findViewById(R.id.etPw2);
        etPw1.setTransformationMethod(new PasswordTransformationMethod());
        etPw2.setTransformationMethod(new PasswordTransformationMethod());
        final ImageView ivPwChk = (ImageView) findViewById(R.id.ivPwChk);
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
                    userGen = "M";
                } else if (i == R.id.rbtnWoman) {
                    userGen = "F";
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
            String uId = etId.getText().toString();

            if (uId.isEmpty()) {
                Toast.makeText(getApplicationContext(), "아이디를 입력하세요.", Toast.LENGTH_SHORT).show();

            } else if (uId.equals("admin")) {
                Toast.makeText(getApplicationContext(), "사용불가능한 아이디입니다.", Toast.LENGTH_SHORT).show();
            }else {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) { //중복아이디 있음
                                idChk = false;
                                Toast.makeText(getApplicationContext(), "이미 사용중인 아이디 입니다.", Toast.LENGTH_SHORT).show();
                            } else { //중복아이디 없음
                                idChk = true;
                                Toast.makeText(getApplicationContext(), "사용가능한 아이디 입니다.", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                IdChkRequest idChkRequest = new IdChkRequest(uId, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register.this);
                queue.add(idChkRequest);
            }
        } else if (view == btnNickChk) {
            String uNick = etNick.getText().toString();

            if (uNick.isEmpty()) {
                Toast.makeText(getApplicationContext(), "닉네임을 입력하세요.", Toast.LENGTH_SHORT).show();

            } else if (uNick.equals("admin")) {
                Toast.makeText(getApplicationContext(), "사용불가능한 닉네임입니다.", Toast.LENGTH_SHORT).show();
            } else {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) { //중복닉네임 있음
                                nickChk = false;
                                Toast.makeText(getApplicationContext(), "이미 사용중인 닉네임 입니다.", Toast.LENGTH_SHORT).show();
                            } else { //중복닉네임 없음
                                nickChk = true;
                                Toast.makeText(getApplicationContext(), "사용가능한 닉네임 입니다.", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                NickChkRequest nickChkRequest = new NickChkRequest(uNick, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register.this);
                queue.add(nickChkRequest);
            }
        } else if (view == btnOk) {
            userName = etName.getText().toString();
            userId = etId.getText().toString();
            userPw1 = etPw1.getText().toString();
            userPw2 = etPw2.getText().toString();
            userYear = etYear.getText().toString();
            userMonth = etMonth.getText().toString();
            userDay = etDay.getText().toString();
            userNick = etNick.getText().toString();
            if (!idChk) {
                Toast.makeText(getApplicationContext(), "아이디 중복확인을 눌러주세요", Toast.LENGTH_SHORT).show();
            } else {
                if (userMonth.length() == 1) {
                    userMonth = "0" + userMonth;
                }
                if (userDay.length() == 1) {
                    userDay = "0" + userDay;
                }

                if (!nickChk) {
                    Toast.makeText(getApplicationContext(), "닉네임 중복확인을 눌러주세요", Toast.LENGTH_SHORT).show();
                } else {
                    if ((userPw1.equals(userPw2)) && (userName != null && !userName.isEmpty()) && (userId != null && !userId.isEmpty()) && (userPw1 != null && !userPw1.isEmpty()) && (userPw2 != null && !userPw2.isEmpty()) && (userNick != null && !userNick.isEmpty()) && (userGen != null && !userGen.isEmpty()) && (userYear != null && !userYear.isEmpty()) && (userMonth != null && !userMonth.isEmpty()) && (userDay != null && !userDay.isEmpty())) {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) { //회원등록에 성공한 경우
                                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    boolean success = jsonObject.getBoolean("success");
                                                    if (success) { //레코드등록 성공
                                                        Toast.makeText(getApplicationContext(), "회원 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(Register.this, Login.class);
                                                        startActivity(intent);
                                                        finish();
                                                    } else { //레코드등록 실패
                                                        Toast.makeText(getApplicationContext(), "회원 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };
                                        //서버로 Volley를 이용해서 요청을 함
                                        AutoAddRequest autoAddRequest = new AutoAddRequest(userId,"0","0","0","0","0","0","0","0","0","0","0","0", responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(Register.this);
                                        queue.add(autoAddRequest);
                                    } else { //회원등록에 실패한 경우
                                        Toast.makeText(getApplicationContext(), "회원 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };

                        //서버로 Volley를 이용해서 요청을 함
                        RegisterRequest registerRequest = new RegisterRequest(userName, userId, userPw1, userYear, userMonth, userDay, userNick, userGen, userStar, userState, userHashtag1, userHashtag2, userHashtag3, userStop, userGrade, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Register.this);
                        queue.add(registerRequest);
                    } else {
                        Toast.makeText(getApplicationContext(), "회원가입 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } else if (view == btnCancel) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }

        return false;
    }
}
