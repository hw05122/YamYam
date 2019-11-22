package com.example.yamyam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Mypage extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnList, btnChatting, btnAuto, btnMy;
    private Button btnModi;
    private TextView tvNick, tvStar, tvHash1, tvHash2, tvHash3, tvChart;
    private TextView tv1, tv2, tv3, tv4, tv5, tvCnt1, tvCnt2, tvCnt3, tvCnt4, tvCnt5;
    private ImageView ivState;
    private int stateChk = 0, hashChk = 0;
    private String star, state, hash1, hash2, hash3;
    private String[] hashtag;
    private LinearLayout llchart1, llchart2, llchart3, llchart4, llchart5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        setTitle("마이페이지");

        tvNick = (TextView) findViewById(R.id.tvNick);
        tvNick.setText(Login.uNick);
        tvStar = (TextView) findViewById(R.id.tvStar);
        tvStar.setText(Login.uStar);
        star = Login.uStar;

        tvHash1 = (TextView) findViewById(R.id.tvHash1);
        tvHash1.setText("#" + Login.uHash1);
        hash1 = Login.uHash1;

        tvHash2 = (TextView) findViewById(R.id.tvHash2);
        tvHash2.setText("#" + Login.uHash2);
        hash2 = Login.uHash2;

        tvHash3 = (TextView) findViewById(R.id.tvHash3);
        tvHash3.setText("#" + Login.uHash3);
        hash3 = Login.uHash3;

        btnModi = (Button) findViewById(R.id.btnModi);
        btnModi.setOnClickListener(this);

        ivState = (ImageView) findViewById(R.id.state);
        if (Login.uState.equals("o")) {
            state = "o";
            if (Login.uGen.equals("M")) {
                ivState.setImageResource(R.drawable.man);
            } else if (Login.uGen.equals("F")) {
                ivState.setImageResource(R.drawable.woman);
            }
        } else if (Login.uState.equals("x")) {
            state = "x";
            ivState.setImageResource(R.drawable.ic_close);
        } else if (Login.uState.equals("h")) {
            state = "h";
            ivState.setImageResource(R.drawable.ic_heart);
        }

        hashtag = new String[12];
        tvChart = (TextView) findViewById(R.id.tvChart);
        chartGet();

        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (TextView) findViewById(R.id.tv5);
        tvCnt1 = (TextView) findViewById(R.id.tvCnt1);
        tvCnt2 = (TextView) findViewById(R.id.tvCnt2);
        tvCnt3 = (TextView) findViewById(R.id.tvCnt3);
        tvCnt4 = (TextView) findViewById(R.id.tvCnt4);
        tvCnt5 = (TextView) findViewById(R.id.tvCnt5);
        llchart1 = (LinearLayout) findViewById(R.id.llchart1);
        llchart2 = (LinearLayout) findViewById(R.id.llchart2);
        llchart3 = (LinearLayout) findViewById(R.id.llchart3);
        llchart4 = (LinearLayout) findViewById(R.id.llchart4);
        llchart5 = (LinearLayout) findViewById(R.id.llchart5);

        btnList = (ImageButton) findViewById(R.id.btnList);
        btnList.setOnClickListener(this);

        btnChatting = (ImageButton) findViewById(R.id.btnChatting);
        btnChatting.setOnClickListener(this);

        btnAuto = (ImageButton) findViewById(R.id.btnAuto);
        btnAuto.setOnClickListener(this);
        btnAuto.setBackgroundResource(R.color.colorPrimary);

        btnMy = (ImageButton) findViewById(R.id.btnMy);
        btnMy.setBackgroundColor(Color.LTGRAY);
    }


    public void chartGet() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) { //레코드등록 성공
                        String uId = jsonObject.getString("userID");
                        hashtag[0] = jsonObject.getString("h1");
                        hashtag[1] = jsonObject.getString("h2");
                        hashtag[2] = jsonObject.getString("h3");
                        hashtag[3] = jsonObject.getString("h4");
                        hashtag[4] = jsonObject.getString("h5");
                        hashtag[5] = jsonObject.getString("h6");
                        hashtag[6] = jsonObject.getString("h7");
                        hashtag[7] = jsonObject.getString("h8");
                        hashtag[8] = jsonObject.getString("h9");
                        hashtag[9] = jsonObject.getString("h10");
                        hashtag[10] = jsonObject.getString("h11");
                        hashtag[11] = jsonObject.getString("h12");

                        charShow();
                    } else { //레코드등록 실패
                        Log.d("young", "출석한 적없는 사용자");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        //서버로 Volley를 이용해서 요청을 함
        AutoRequest autoRequest = new AutoRequest(Login.uId, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Mypage.this);
        queue.add(autoRequest);
    }

    public void charShow() {
        String[] hash = {"고백", "권태기", "다툼", "데이트", "미련", "바람", "사랑", "썸", "소개팅", "이별", "짝사랑", "첫사랑"};

        int hCnt = 0;
        for (int i = 0; i < 12; i++) {
            if (hashtag[i].equals("0")) {
                hCnt++;
            }
        }

        int[] hash12 = new int[12];
        String[] tmpH = new String[12];
        for (int i = 0; i < 12; i++) {
            tmpH[i] = hashtag[i];
            hash12[i] = i;
        }

        if (hCnt == 12) {
            tvChart.setVisibility(View.VISIBLE);
            tvChart.setText("상담한 내역이 없습니다.");
        } else {

            for (int i = 0; i < 11; i++) {
                for (int j = i + 1; j < 12; j++) {
                    if (Integer.parseInt(tmpH[i]) > Integer.parseInt(tmpH[j])) {
                        String t = tmpH[j];
                        tmpH[j] = tmpH[i];
                        tmpH[i] = t;

                        int tt = hash12[j];
                        hash12[j] = hash12[i];
                        hash12[i] = tt;
                    }
                }
            }

            tv1.setText(hash[hash12[11]]);
            tv2.setText(hash[hash12[10]]);
            tv3.setText(hash[hash12[9]]);
            tv4.setText(hash[hash12[8]]);
            tv5.setText(hash[hash12[7]]);

            tvCnt1.setText(tmpH[11]);
            tvCnt2.setText(tmpH[10]);
            tvCnt3.setText(tmpH[9]);
            tvCnt4.setText(tmpH[8]);
            tvCnt5.setText(tmpH[7]);

            switch (tmpH[11]) {
                case "5":
                    llchart1.setBackgroundResource(R.drawable.heartchart5);
                    break;
                case "4":
                    llchart1.setBackgroundResource(R.drawable.heartchart4);
                    break;
                case "3":
                    llchart1.setBackgroundResource(R.drawable.heartchart3);
                    break;
                case "2":
                    llchart1.setBackgroundResource(R.drawable.heartchart2);
                    break;
                case "1":
                    llchart1.setBackgroundResource(R.drawable.heartchart1);
                    break;
            }

            switch (tmpH[10]) {
                case "5":
                    llchart2.setBackgroundResource(R.drawable.heartchart5);
                    break;
                case "4":
                    llchart2.setBackgroundResource(R.drawable.heartchart4);
                    break;
                case "3":
                    llchart2.setBackgroundResource(R.drawable.heartchart3);
                    break;
                case "2":
                    llchart2.setBackgroundResource(R.drawable.heartchart2);
                    break;
                case "1":
                    llchart2.setBackgroundResource(R.drawable.heartchart1);
                    break;
            }

            switch (tmpH[9]) {
                case "5":
                    llchart3.setBackgroundResource(R.drawable.heartchart5);
                    break;
                case "4":
                    llchart3.setBackgroundResource(R.drawable.heartchart4);
                    break;
                case "3":
                    llchart3.setBackgroundResource(R.drawable.heartchart3);
                    break;
                case "2":
                    llchart3.setBackgroundResource(R.drawable.heartchart2);
                    break;
                case "1":
                    llchart3.setBackgroundResource(R.drawable.heartchart1);
                    break;
            }

            switch (tmpH[8]) {
                case "5":
                    llchart4.setBackgroundResource(R.drawable.heartchart5);
                    break;
                case "4":
                    llchart4.setBackgroundResource(R.drawable.heartchart4);
                    break;
                case "3":
                    llchart4.setBackgroundResource(R.drawable.heartchart3);
                    break;
                case "2":
                    llchart4.setBackgroundResource(R.drawable.heartchart2);
                    break;
                case "1":
                    llchart4.setBackgroundResource(R.drawable.heartchart1);
                    break;
            }

            switch (tmpH[7]) {
                case "5":
                    llchart5.setBackgroundResource(R.drawable.heartchart5);
                    break;
                case "4":
                    llchart5.setBackgroundResource(R.drawable.heartchart4);
                    break;
                case "3":
                    llchart5.setBackgroundResource(R.drawable.heartchart3);
                    break;
                case "2":
                    llchart5.setBackgroundResource(R.drawable.heartchart2);
                    break;
                case "1":
                    llchart5.setBackgroundResource(R.drawable.heartchart1);
                    break;
            }
        }
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
                        state = "o";
                    } else if (checkedId == R.id.rbX) {
                        stateChk = 2;
                        state = "x";
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
                        ivState.setImageResource(R.drawable.ic_launcher_foreground);
                    } else if (stateChk == 2) {
                        ivState.setImageResource(R.drawable.ic_close);
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
                        if (hashChk == 1) {
                            tvHash1.setText("#" + hashList.get(0));
                            tvHash2.setText("#");
                            tvHash3.setText("#");

                            hash1 = hashList.get(0);
                            hash2 = "";
                            hash3 = "";
                        } else if (hashChk == 2) {
                            tvHash1.setText("#" + hashList.get(0));
                            tvHash2.setText("#" + hashList.get(1));
                            tvHash3.setText("#");

                            hash1 = hashList.get(0);
                            hash2 = hashList.get(1);
                            hash3 = "";
                        } else if (hashChk == 3) {
                            tvHash1.setText("#" + hashList.get(0));
                            tvHash2.setText("#" + hashList.get(1));
                            tvHash3.setText("#" + hashList.get(2));

                            hash1 = hashList.get(0);
                            hash2 = hashList.get(1);
                            hash3 = hashList.get(2);
                        }
                        for (int i = 0; i < hashList.size(); i++) {
                            hashList.remove(i);
                        }
                        hashChk = 0;

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");

                                    if (success) { //수정 됨
                                        Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();
                                    } else { //
                                        Toast.makeText(getApplicationContext(), "수정되지 않았습니다.", Toast.LENGTH_SHORT).show();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        UpdateRequest nickChkRequest = new UpdateRequest(Login.uId, star, state, hash1, hash2, hash3, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Mypage.this);
                        queue.add(nickChkRequest);
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
            btnAuto.setBackgroundColor(Color.LTGRAY);
            btnMy.setBackgroundResource(R.color.colorPrimary);

            AlertDialog.Builder starB = new AlertDialog.Builder(Mypage.this);
            starB.setTitle("        자동매칭 중 입니다").setIcon(R.drawable.logo4);
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View root = inflater.inflate(R.layout.digauto, null);
            starB.setView(root);
            starB.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    btnMy.setBackgroundColor(Color.LTGRAY);
                    btnAuto.setBackgroundResource(R.color.colorPrimary);
                }
            });
            final AlertDialog adB = starB.create();
            adB.show();

            adB.dismiss();
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
