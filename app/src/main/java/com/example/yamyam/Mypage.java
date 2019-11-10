package com.example.yamyam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Mypage extends AppCompatActivity implements View.OnClickListener {
    private ImageButton  btnList, btnChatting, btnAuto;
    private Button btnModi;
    private TextView tvNick, tvStar;
    private ImageView state;
    private int stateChk = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        setTitle("마이페이지");

        tvNick = (TextView) findViewById(R.id.tvNick);
        tvNick.setText(Login.uNick);
        tvStar = (TextView) findViewById(R.id.tvStar);
        tvStar.setText("평가");

        btnModi = (Button) findViewById(R.id.btnModi);
        btnModi.setOnClickListener(this);

        state = (ImageView) findViewById(R.id.state);

        btnList = (ImageButton) findViewById(R.id.btnList);
        btnList.setOnClickListener(this);

        btnChatting = (ImageButton) findViewById(R.id.btnChatting);
        btnChatting.setOnClickListener(this);

        btnAuto = (ImageButton) findViewById(R.id.btnAuto);
        btnAuto.setOnClickListener(this);
    }

    public void statsShow(){

    }

    public void onClick(View view) {
        if (view == btnModi) {
            AlertDialog.Builder modiB = new AlertDialog.Builder(Mypage.this);
            modiB.setTitle("    수정하기").setIcon(R.drawable.logo4);
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View root = inflater.inflate(R.layout.digmodi, null);
            modiB.setView(root);
            modiB.setCancelable(false);

            RadioGroup rgState = (RadioGroup)root.findViewById(R.id.rgState);
            rgState.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId == R.id.rbO){
                        stateChk = 1;
                    }
                    else if(checkedId == R.id.rbX){
                        stateChk = 2;
                    }
                }
            });

            modiB.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            modiB.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(stateChk == 1){
                        state.setImageResource(R.drawable.ic_launcher_foreground);
                    }
                    else if(stateChk == 2){
                        state.setImageResource(R.drawable.ic_close);
                    }
                    Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });

            modiB.show();
        } else if (view == btnList) {
            Intent intent = new Intent(getApplicationContext(), Chat.class);
            startActivity(intent);
        } else if (view == btnChatting) {
            Intent intent = new Intent(getApplicationContext(), Chatting.class);
            startActivity(intent);
        } else if (view == btnAuto) {
            AlertDialog.Builder starB = new AlertDialog.Builder(Mypage.this);
            starB.setTitle("        자동매칭 중 입니다").setIcon(R.drawable.logo4);
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View root = inflater.inflate(R.layout.digauto, null);
            starB.setView(root);
            starB.setNegativeButton("닫기",null);

            starB.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(getApplicationContext(), Chat.class);
            startActivity(intent);
            finish();
        }

        return false;
    }
}
