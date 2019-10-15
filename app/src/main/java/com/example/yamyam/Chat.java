package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Chat extends AppCompatActivity {
    Button btnCurrent, btnTotal, btnNew, btnSearch, btnOption, btnList, btnChatting, btnAuto, btnMy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        btnCurrent = (Button)findViewById(R.id.btnCurrent);
        btnTotal = (Button)findViewById(R.id.btnTotal);
        btnNew = (Button)findViewById(R.id.btnNew);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        btnOption = (Button)findViewById(R.id.btnOption);
        btnList = (Button)findViewById(R.id.btnList);
        btnChatting = (Button) findViewById(R.id.btnChatting);
        btnAuto = (Button)findViewById(R.id.btnAuto);
        btnMy = (Button)findViewById(R.id.btnMy);
    }

    public void onClick(View view) {

        if(view == btnCurrent){

        }
        else if(view == btnTotal){

        }
        else if(view == btnNew){

        }
        else if(view == btnSearch){

        }
        else if(view == btnOption){

        }
        else if(view == btnList){

        }
        else if(view == btnChatting){
            Intent intent = new Intent(getApplicationContext(),Chatting.class);
            startActivity(intent);
        }
        else if(view == btnAuto){

        }
        else if(view == btnMy){
            Intent intent = new Intent(getApplicationContext(),Mypage.class);
            startActivity(intent);
        }
    }
}
