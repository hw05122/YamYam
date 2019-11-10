package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Chat extends AppCompatActivity {
    private ImageButton btnChatting,btnAuto,btnMy;
    private Button btnCurrent, btnTotal, btnNew, btnSearch, btnOption;
    public ArrayList<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle("상담하기");

        btnCurrent = (Button)findViewById(R.id.btnCurrent);
        btnTotal = (Button)findViewById(R.id.btnTotal);
        btnNew = (Button)findViewById(R.id.btnNew);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        btnOption = (Button)findViewById(R.id.btnOption);
        btnChatting = (ImageButton) findViewById(R.id.btnChatting);
        btnAuto = (ImageButton)findViewById(R.id.btnAuto);
        btnMy = (ImageButton)findViewById(R.id.btnMy);

        userListShow();
    }

    public void userListShow(){

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
        else if(view == btnChatting){
            Intent intent = new Intent(getApplicationContext(),Chatting.class);
            startActivity(intent);
            finish();
        }
        else if(view == btnAuto){
            AlertDialog.Builder starB = new AlertDialog.Builder(Chat.this);
            starB.setTitle("        자동매칭 중 입니다").setIcon(R.drawable.logo4);
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View root = inflater.inflate(R.layout.digauto, null);
            starB.setView(root);
            starB.setNegativeButton("닫기",null);
            starB.setCancelable(false);

            starB.show();
        }
        else if(view == btnMy){
            Intent intent = new Intent(getApplicationContext(),Mypage.class);
            startActivity(intent);
            finish();
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

class User{
    private String name;
    private String id;
    private String year;
    private String nickname;
    private String gen;

    String getName(){
        return name;
    }

    String getId(){
        return id;
    }

    String getYear(){
        return year;
    }

    String getNickname(){
        return nickname;
    }

    String getGen(){
        return gen;
    }
}
