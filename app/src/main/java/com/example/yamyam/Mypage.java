package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Mypage extends AppCompatActivity implements View.OnClickListener{
    Button btnModi, btnOk, btnCancel, btnList, btnChatting, btnAuto;
    LinearLayout llModiContent;
    RadioGroup rg;
    ImageView state;
    int stateChk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        llModiContent = (LinearLayout)findViewById(R.id.llModiContent);
        btnModi = (Button)findViewById(R.id.btnModi);
        btnModi.setOnClickListener(this);

        rg = (RadioGroup)findViewById(R.id.rg);
        state = (ImageView)findViewById(R.id.state);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.btnConsultOk) {
                    stateChk = 1;
                } else if (i == R.id.btnConsultNo) {
                    stateChk = 2;
                }
            }
        });

        btnOk = (Button)findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);

        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(this);

        btnList = (Button)findViewById(R.id.btnList);
        btnList.setOnClickListener(this);

        btnChatting = (Button)findViewById(R.id.btnChatting);
        btnChatting.setOnClickListener(this);

        btnAuto = (Button)findViewById(R.id.btnAuto);
        btnAuto.setOnClickListener(this);
    }

    public void onClick(View view) {
        if(view == btnModi){
            llModiContent.setVisibility(View.VISIBLE);
        }
        else if(view == btnOk){
            if(stateChk == 1){
                state.setImageResource(R.drawable.woman);
            }
            else if(stateChk == 2){
                state.setImageResource(R.drawable.x);
            }
            Toast.makeText(getApplicationContext(),"수정하였습니다.",Toast.LENGTH_SHORT).show();
            llModiContent.setVisibility(View.INVISIBLE);
        }
        else if(view == btnCancel){
            llModiContent.setVisibility(View.INVISIBLE);
        }
        else if(view == btnList){
            Intent intent = new Intent(getApplicationContext(), Chat.class);
            startActivity(intent);
        }
        else if(view == btnChatting){
            Intent intent = new Intent(getApplicationContext(), Chatting.class);
            startActivity(intent);
        }
        else if(view == btnAuto){

        }
    }
}
