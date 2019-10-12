package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Register extends AppCompatActivity implements View.OnClickListener {
    EditText etName, etId, etPw1, etPw2, etNick, etYear, etMonth, etDay;
    Button btnIdChk, btnNickChk, btnOk, btnCancel;
    RadioGroup rbtnGroup;
    ImageView ivPwChk;

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
            //성별 체크안할 시 고려하기!!!!!!
            //비번X일 경우 회원가입실패뜨도록
            //생년월일은 숫자
            if (etName.getText().toString().isEmpty() || etId.getText().toString().isEmpty() || etPw1.getText().toString().isEmpty() || etPw2.getText().toString().isEmpty() || etNick.getText().toString().isEmpty() || etYear.getText().toString().isEmpty() || etMonth.getText().toString().isEmpty() || etDay.getText().toString().isEmpty()) {
                Toast.makeText(getApplicationContext(), "회원가입 형식이 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        } else if (view == btnCancel) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
    }
}
