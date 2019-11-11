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
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Mypage extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnList, btnChatting, btnAuto;
    private Button btnModi;
    private TextView tvNick, tvStar, tvHash1, tvHash2, tvHash3;
    private ImageView state;
    private int stateChk = 0, hashChk = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        setTitle("마이페이지");

        tvNick = (TextView) findViewById(R.id.tvNick);
        tvNick.setText(Login.uNick);
        tvStar = (TextView) findViewById(R.id.tvStar);
        tvStar.setText("평가");

        tvHash1 = (TextView) findViewById(R.id.tvHash1);
        tvHash1.setText("#");
        tvHash2 = (TextView) findViewById(R.id.tvHash2);
        tvHash2.setText("#");
        tvHash3 = (TextView) findViewById(R.id.tvHash3);
        tvHash3.setText("#");

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

    public void statsShow() {

    }

    public void onClick(View view) {
        if (view == btnModi) {
            AlertDialog.Builder modiB = new AlertDialog.Builder(Mypage.this);
            modiB.setTitle("    수정하기").setIcon(R.drawable.logo4);
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View root = inflater.inflate(R.layout.digmodi, null);
            modiB.setView(root);
            modiB.setCancelable(false);

            RadioGroup rgState = (RadioGroup) root.findViewById(R.id.rgState);
            rgState.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.rbO) {
                        stateChk = 1;
                    } else if (checkedId == R.id.rbX) {
                        stateChk = 2;
                    }
                }
            });

            final CheckBox chk1 = (CheckBox) root.findViewById(R.id.chk1);
            final CheckBox chk2 = (CheckBox) root.findViewById(R.id.chk2);
            final CheckBox chk3 = (CheckBox) root.findViewById(R.id.chk3);
            final CheckBox chk4 = (CheckBox) root.findViewById(R.id.chk4);
            final CheckBox chk5 = (CheckBox) root.findViewById(R.id.chk5);
            final CheckBox chk6 = (CheckBox) root.findViewById(R.id.chk6);
            final CheckBox chk7 = (CheckBox) root.findViewById(R.id.chk7);
            final CheckBox chk8 = (CheckBox) root.findViewById(R.id.chk8);
            final CheckBox chk9 = (CheckBox) root.findViewById(R.id.chk9);
            final CheckBox chk10 = (CheckBox) root.findViewById(R.id.chk10);
            final CheckBox chk11 = (CheckBox) root.findViewById(R.id.chk11);
            final CheckBox chk12 = (CheckBox) root.findViewById(R.id.chk12);

            modiB.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            modiB.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (stateChk == 1) {
                        state.setImageResource(R.drawable.ic_launcher_foreground);
                    } else if (stateChk == 2) {
                        state.setImageResource(R.drawable.ic_close);
                    }

                    ArrayList<String> hashList = new ArrayList<String>();
                    if (chk1.isChecked()) {
                        hashChk++;
                        hashList.add(chk1.getText().toString());
                    }
                    if (chk2.isChecked()) {
                        hashChk++;
                        hashList.add(chk2.getText().toString());
                    }
                    if (chk3.isChecked()) {
                        hashChk++;
                        hashList.add(chk3.getText().toString());
                    }
                    if (chk4.isChecked()) {
                        hashChk++;
                        hashList.add(chk4.getText().toString());
                    }
                    if (chk5.isChecked()) {
                        hashChk++;
                        hashList.add(chk5.getText().toString());
                    }
                    if (chk6.isChecked()) {
                        hashChk++;
                        hashList.add(chk6.getText().toString());
                    }
                    if (chk7.isChecked()) {
                        hashChk++;
                        hashList.add(chk7.getText().toString());
                    }
                    if (chk8.isChecked()) {
                        hashChk++;
                        hashList.add(chk8.getText().toString());
                    }
                    if (chk9.isChecked()) {
                        hashChk++;
                        hashList.add(chk9.getText().toString());
                    }
                    if (chk10.isChecked()) {
                        hashChk++;
                        hashList.add(chk10.getText().toString());
                    }
                    if (chk11.isChecked()) {
                        hashChk++;
                        hashList.add(chk11.getText().toString());
                    }
                    if (chk12.isChecked()) {
                        hashChk++;
                        hashList.add(chk12.getText().toString());
                    }

                    if (hashChk > 3) {
                        Toast.makeText(getApplicationContext(), "해시태그는 최대 3개까지 가능합니다.", Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < hashList.size(); i++) {
                            hashList.remove(i);
                        }
                        hashChk = 0;
                    } else {
                        Toast.makeText(getApplicationContext(), "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        if (hashChk == 1) {
                            tvHash1.setText("#" + hashList.get(0));
                            tvHash2.setText("#");
                            tvHash3.setText("#");
                        } else if (hashChk == 2) {
                            tvHash1.setText("#" + hashList.get(0));
                            tvHash2.setText("#" + hashList.get(1));
                            tvHash3.setText("#");
                        } else if (hashChk == 3) {
                            tvHash1.setText("#" + hashList.get(0));
                            tvHash2.setText("#" + hashList.get(1));
                            tvHash3.setText("#" + hashList.get(2));
                        }
                        for (int i = 0; i < hashList.size(); i++) {
                            hashList.remove(i);
                        }
                        hashChk = 0;
                    }
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
            starB.setNegativeButton("닫기", null);

            starB.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(getApplicationContext(), Chat.class);
            startActivity(intent);
            finish();
        }

        return false;
    }
}
