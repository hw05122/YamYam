package com.example.yamyam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.StringTokenizer;

public class Mypage extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnList, btnChatting, btnAuto, btnMy;
    private Button btnModi;
    private TextView tvNick, tvStar, tvHash1, tvHash2, tvHash3, tvChart;
    private TextView tv1, tv2, tv3, tv4, tv5, tvCnt1, tvCnt2, tvCnt3, tvCnt4, tvCnt5;
    private ImageView ivState;
    private int stateChk = 0, hashChk = 0;
    private String[] hashtag;
    private LinearLayout llchart1, llchart2, llchart3, llchart4, llchart5;
    private String state;
    public static String youNick = "", youStar, youState, youHash1, youHash2, youHash3;
    public static ArrayList<User> userList = new ArrayList<>();
    public ArrayList<User> saveList = new ArrayList<>();
    private String userNickname, userGender, userStar, userState, userHashtag1, userHashtag2, userHashtag3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);
        setTitle("마이페이지");

        tvNick = (TextView) findViewById(R.id.tvNick);
        tvNick.setText(Login.uNick);
        tvStar = (TextView) findViewById(R.id.tvStar);
        Response.Listener<String> responseListener1 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) { //로그인에 성공한 경우
                        tvStar.setText(jsonObject.getString("userStar"));
                        Login.uStar = jsonObject.getString("userStar");
                        Login.uState = jsonObject.getString("userState");
                        Login.uRoll = jsonObject.getString("userRoll");
                        Login.uStop = jsonObject.getString("userStop");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(Login.uId, Login.uPw, responseListener1);
        RequestQueue queue1 = Volley.newRequestQueue(Mypage.this);
        queue1.add(loginRequest);



        tvHash1 = (TextView) findViewById(R.id.tvHash1);
        tvHash1.setText("#" + Login.uHash1);

        tvHash2 = (TextView) findViewById(R.id.tvHash2);
        tvHash2.setText("#" + Login.uHash2);

        tvHash3 = (TextView) findViewById(R.id.tvHash3);
        tvHash3.setText("#" + Login.uHash3);

        btnModi = (Button) findViewById(R.id.btnModi);
        btnModi.setOnClickListener(this);

        state = Login.uState;
        ivState = (ImageView) findViewById(R.id.state);
        if (Login.uState.equals("o")) {
            if (Login.uGen.equals("M")) {
                ivState.setImageResource(R.drawable.man);
            } else if (Login.uGen.equals("F")) {
                ivState.setImageResource(R.drawable.woman);
            }
        } else if (Login.uState.equals("x")) {
            ivState.setImageResource(R.drawable.ic_close);
        } else if (Login.uState.equals("h")) {
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

                        chartShow();
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

    public void chartShow() {
        String[] hash = {"고백", "권태기", "다툼", "데이트", "미련", "바람", "사과", "소개팅", "썸", "이별", "짝사랑", "첫사랑"};

        int hCnt = 0;

        int[] hash12 = new int[12];
        String[] tmpH = new String[12];
        for (int i = 0; i < 12; i++) {
            tmpH[i] = hashtag[i];
            hash12[i] = i;
            if (hashtag[i].equals("0")) {
                hCnt++;
            }
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

            if (Integer.parseInt(tmpH[11]) >= 5) {
                llchart1.setBackgroundResource(R.drawable.heartchart5);
            } else if (Integer.parseInt(tmpH[11]) == 4){
                llchart1.setBackgroundResource(R.drawable.heartchart4);
            }else if (Integer.parseInt(tmpH[11]) == 3){
                llchart1.setBackgroundResource(R.drawable.heartchart3);
            }else if (Integer.parseInt(tmpH[11]) == 2){
                llchart1.setBackgroundResource(R.drawable.heartchart2);
            }else if (Integer.parseInt(tmpH[11]) == 1){
                llchart1.setBackgroundResource(R.drawable.heartchart1);
            }

            if (Integer.parseInt(tmpH[10]) >= 5) {
                llchart2.setBackgroundResource(R.drawable.heartchart5);
            } else if (Integer.parseInt(tmpH[10]) == 4){
                llchart2.setBackgroundResource(R.drawable.heartchart4);
            }else if (Integer.parseInt(tmpH[10]) == 3){
                llchart2.setBackgroundResource(R.drawable.heartchart3);
            }else if (Integer.parseInt(tmpH[10]) == 2){
                llchart2.setBackgroundResource(R.drawable.heartchart2);
            }else if (Integer.parseInt(tmpH[10]) == 1){
                llchart2.setBackgroundResource(R.drawable.heartchart1);
            }

            if (Integer.parseInt(tmpH[9]) >= 5) {
                llchart3.setBackgroundResource(R.drawable.heartchart5);
            } else if (Integer.parseInt(tmpH[9]) == 4){
                llchart3.setBackgroundResource(R.drawable.heartchart4);
            }else if (Integer.parseInt(tmpH[9]) == 3){
                llchart3.setBackgroundResource(R.drawable.heartchart3);
            }else if (Integer.parseInt(tmpH[9]) == 2){
                llchart3.setBackgroundResource(R.drawable.heartchart2);
            }else if (Integer.parseInt(tmpH[9]) == 1){
                llchart3.setBackgroundResource(R.drawable.heartchart1);
            }

            if (Integer.parseInt(tmpH[8]) >= 5) {
                llchart4.setBackgroundResource(R.drawable.heartchart5);
            } else if (Integer.parseInt(tmpH[8]) == 4){
                llchart4.setBackgroundResource(R.drawable.heartchart4);
            }else if (Integer.parseInt(tmpH[8]) == 3){
                llchart4.setBackgroundResource(R.drawable.heartchart3);
            }else if (Integer.parseInt(tmpH[8]) == 2){
                llchart4.setBackgroundResource(R.drawable.heartchart2);
            }else if (Integer.parseInt(tmpH[8]) == 1){
                llchart4.setBackgroundResource(R.drawable.heartchart1);
            }

            if (Integer.parseInt(tmpH[7]) >= 5) {
                llchart5.setBackgroundResource(R.drawable.heartchart5);
            } else if (Integer.parseInt(tmpH[7]) == 4){
                llchart5.setBackgroundResource(R.drawable.heartchart4);
            }else if (Integer.parseInt(tmpH[7]) == 3){
                llchart5.setBackgroundResource(R.drawable.heartchart3);
            }else if (Integer.parseInt(tmpH[7]) == 2){
                llchart5.setBackgroundResource(R.drawable.heartchart2);
            }else if (Integer.parseInt(tmpH[7]) == 1){
                llchart5.setBackgroundResource(R.drawable.heartchart1);
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
                    if (Login.uState.equals("h") && stateChk == 1 || Login.uState.equals("h") && stateChk == 2) {
                        Toast.makeText(getApplicationContext(), "현재 상담을 끝낸 후 나의상태를 수정해주세요.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (stateChk == 1) {
                            Login.uState = "o";
                            if (Login.uGen.equals("M")) {
                                ivState.setImageResource(R.drawable.man);
                            } else if (Login.uGen.equals("F")) {
                                ivState.setImageResource(R.drawable.woman);
                            }

                        } else if (stateChk == 2) {
                            Login.uState = "x";
                            ivState.setImageResource(R.drawable.ic_close);
                        }

                        ArrayList<String> hashList = new ArrayList<String>();
                        if (chk1.isChecked()) {
                            hashChk++;
                            hashList.add("고백");
                        }
                        if (chk2.isChecked()) {
                            hashChk++;
                            hashList.add("권태기");
                        }
                        if (chk3.isChecked()) {
                            hashChk++;
                            hashList.add("다툼");
                        }
                        if (chk4.isChecked()) {
                            hashChk++;
                            hashList.add("데이트");
                        }
                        if (chk5.isChecked()) {
                            hashChk++;
                            hashList.add("미련");
                        }
                        if (chk6.isChecked()) {
                            hashChk++;
                            hashList.add("바람");
                        }
                        if (chk7.isChecked()) {
                            hashChk++;
                            hashList.add("사과");
                        }
                        if (chk8.isChecked()) {
                            hashChk++;
                            hashList.add("소개팅");
                        }
                        if (chk9.isChecked()) {
                            hashChk++;
                            hashList.add("썸");
                        }
                        if (chk10.isChecked()) {
                            hashChk++;
                            hashList.add("이별");
                        }
                        if (chk11.isChecked()) {
                            hashChk++;
                            hashList.add("짝사랑");
                        }
                        if (chk12.isChecked()) {
                            hashChk++;
                            hashList.add("첫사랑");
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

                                Login.uHash1 = hashList.get(0);
                                Login.uHash2 = "";
                                Login.uHash3 = "";
                            } else if (hashChk == 2) {
                                tvHash1.setText("#" + hashList.get(0));
                                tvHash2.setText("#" + hashList.get(1));
                                tvHash3.setText("#");

                                Login.uHash1 = hashList.get(0);
                                Login.uHash2 = hashList.get(1);
                                Login.uHash3 = "";
                            } else if (hashChk == 3) {
                                tvHash1.setText("#" + hashList.get(0));
                                tvHash2.setText("#" + hashList.get(1));
                                tvHash3.setText("#" + hashList.get(2));

                                Login.uHash1 = hashList.get(0);
                                Login.uHash2 = hashList.get(1);
                                Login.uHash3 = hashList.get(2);
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

                            UpdateRequest nickChkRequest = new UpdateRequest(Login.uNick, Login.uStar, Login.uState, Login.uHash1, Login.uHash2, Login.uHash3, Login.uRoll, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(Mypage.this);
                            queue.add(nickChkRequest);
                        }
                    }
                }
            });

            modiB.show();
        } else if (view == btnList) {
            Intent intent = new Intent(getApplicationContext(), Chat.class);
            startActivity(intent);
            finish();
        } else if (view == btnChatting) {
            if (Login.uState.equals("h")) {
                Intent intent = new Intent(getApplicationContext(), Chatting.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "상담방이 생성되지 않았습니다.", Toast.LENGTH_SHORT).show();
            }
        } else if (view == btnAuto) {
            if (Login.uState.equals("h")) {
                Toast.makeText(getApplicationContext(), "현재 상담을 끝낸 후에 상담하기를 눌러주세요.", Toast.LENGTH_SHORT).show();
            } else {
                new BackgroundTask("https://jsu3229.cafe24.com/dbeditor/List_userStar.php").execute();

                btnMy.setBackgroundResource(R.color.colorPrimary);
                btnAuto.setBackgroundColor(Color.LTGRAY);
                android.app.AlertDialog.Builder starB = new android.app.AlertDialog.Builder(Mypage.this);
                starB.setTitle("자동매칭 중 입니다").setIcon(R.drawable.logo4);
                starB.setMessage("     상담통계를 토대로 분석을 하고 있습니다.");
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View root = inflater.inflate(R.layout.digauto, null);
                starB.setView(root);
                starB.setCancelable(false);
                final android.app.AlertDialog adB = starB.create();
                adB.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        final String[] hashtag = new String[12];

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

                                        String[] hash = {"고백", "권태기", "다툼", "데이트", "미련", "바람", "사과", "소개팅", "썸", "이별", "짝사랑", "첫사랑"};
                                        int hCnt = 0;

                                        int[] hash12 = new int[12];
                                        String[] tmpH = new String[12];
                                        for (int i = 0; i < 12; i++) {
                                            tmpH[i] = hashtag[i];
                                            hash12[i] = i;
                                            if (hashtag[i].equals("0")) {
                                                hCnt++;
                                            }
                                        }

                                        adB.dismiss();
                                        if (hCnt == 12) {//상담내역없음
                                            android.app.AlertDialog.Builder starA = new android.app.AlertDialog.Builder(Mypage.this);
                                            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                            View root = inflater.inflate(R.layout.autoprofile, null);
                                            starA.setView(root);
                                            starA.setTitle("해당 상담사와 상담을 시작하겠습니까?");

                                            userList.clear();
                                            for (int i = 0; i < saveList.size(); i++) {
                                                if (saveList.get(i).getUserState().equals("o") && !saveList.get(i).getUserNickname().equals(Login.uNick)) {
                                                    userList.add(saveList.get(i));
                                                }
                                            }

                                            final int rand = (int) (Math.random() * userList.size());
                                            Log.d("young", userList.size() + " " + String.valueOf(rand));

                                            ImageView ivState = (ImageView) root.findViewById(R.id.ivState);
                                            TextView tvGender = (TextView) root.findViewById(R.id.tvGender);
                                            if (userList.get(rand).getUserState().equals("o")) {
                                                if (userList.get(rand).userGender.equals("M")) {
                                                    ivState.setImageResource(R.drawable.man);
                                                } else if (userList.get(rand).userGender.equals("F")) {
                                                    ivState.setImageResource(R.drawable.woman);
                                                }
                                            }

                                            TextView nick = (TextView) root.findViewById(R.id.tvNick);
                                            nick.setText(userList.get(rand).getUserNickname());
                                            TextView star = (TextView) root.findViewById(R.id.tvStar);
                                            star.setText(userList.get(rand).getUserStar());
                                            TextView hash1 = (TextView) root.findViewById(R.id.tvHash1);
                                            hash1.setText("#" + userList.get(rand).getUserHashtag1());
                                            TextView hash2 = (TextView) root.findViewById(R.id.tvHash2);
                                            hash2.setText("#" + userList.get(rand).getUserHashtag2());
                                            TextView hash3 = (TextView) root.findViewById(R.id.tvHash3);
                                            hash3.setText("#" + userList.get(rand).getUserHashtag3());

                                            starA.setNegativeButton("네", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject = new JSONObject(response);
                                                                boolean success = jsonObject.getBoolean("success");

                                                                if (success) { //로그인에 성공한 경우
                                                                    String uNick = jsonObject.getString("userNickname");
                                                                    String uPoint = jsonObject.getString("userPoint");
                                                                    String uPL = jsonObject.getString("userPointList");
                                                                    Log.d("young", uPoint);

                                                                    if (Integer.parseInt(uPoint) - 30 < 0) {//포인트부족
                                                                        Toast.makeText(getApplicationContext(), "포인트가 부족합니다.", Toast.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent(getApplicationContext(), Mypage.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    } else {
                                                                        youNick = userList.get(rand).getUserNickname();
                                                                        youStar = userList.get(rand).getUserStar();
                                                                        youState = userList.get(rand).getUserState();
                                                                        youHash1 = userList.get(rand).getUserHashtag1();
                                                                        youHash2 = userList.get(rand).getUserHashtag2();
                                                                        youHash3 = userList.get(rand).getUserHashtag3();

                                                                        Intent intent = new Intent(getApplicationContext(), Chatting.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    };

                                                    PointRequest pointRequest = new PointRequest(Login.uNick, responseListener);
                                                    RequestQueue queue = Volley.newRequestQueue(Mypage.this);
                                                    queue.add(pointRequest);
                                                }
                                            });
                                            starA.setPositiveButton("아니요", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    btnAuto.setBackgroundResource(R.color.colorPrimary);
                                                    btnMy.setBackgroundColor(Color.LTGRAY);
                                                }
                                            });
                                            starA.setCancelable(false);
                                            starA.show();

                                        } else {//상담통계 있음
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

                                            android.app.AlertDialog.Builder starA = new android.app.AlertDialog.Builder(Mypage.this);
                                            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                            View root = inflater.inflate(R.layout.autoprofile, null);
                                            starA.setView(root);
                                            starA.setTitle("해당 상담사와 상담을 시작하겠습니까?");

                                            userList.clear();
                                            for (int i = 0; i < saveList.size(); i++) {
                                                String h1 = saveList.get(i).getUserHashtag1();
                                                String h2 = saveList.get(i).getUserHashtag2();
                                                String h3 = saveList.get(i).getUserHashtag3();
                                                if (saveList.get(i).getUserState().equals("o") && !saveList.get(i).getUserNickname().equals(Login.uNick) && (h1.equals(hash[hash12[11]]) || h1.equals(hash[hash12[10]]) || h1.equals(hash[hash12[9]]) || h2.equals(hash[hash12[11]]) || h2.equals(hash[hash12[10]]) || h2.equals(hash[hash12[9]]) || h3.equals(hash[hash12[11]]) || h3.equals(hash[hash12[10]]) || h3.equals(hash[hash12[9]]))) {
                                                    userList.add(saveList.get(i));
                                                    Log.d("young", h1 + " " + h2 + " " + h3);
                                                }
                                            }

                                            if (userList.size() == 0) {
                                                userList.clear();
                                                for (int i = 0; i < saveList.size(); i++) {
                                                    if (saveList.get(i).getUserState().equals("o") && !saveList.get(i).getUserNickname().equals(Login.uNick)) {
                                                        userList.add(saveList.get(i));
                                                    }
                                                }
                                            }

                                            final int rand = (int) (Math.random() * userList.size());
                                            Log.d("young", userList.size() + " " + String.valueOf(rand));

                                            ImageView ivState = (ImageView) root.findViewById(R.id.ivState);
                                            TextView tvGender = (TextView) root.findViewById(R.id.tvGender);
                                            if (userList.get(rand).getUserState().equals("o")) {
                                                if (userList.get(rand).userGender.equals("M")) {
                                                    ivState.setImageResource(R.drawable.man);
                                                } else if (userList.get(rand).userGender.equals("F")) {
                                                    ivState.setImageResource(R.drawable.woman);
                                                }
                                            }

                                            TextView tvNick = (TextView) root.findViewById(R.id.tvNick);
                                            tvNick.setText(userList.get(rand).getUserNickname());
                                            TextView tvStar = (TextView) root.findViewById(R.id.tvStar);
                                            tvStar.setText(userList.get(rand).getUserStar());

                                            TextView tvHash1 = (TextView) root.findViewById(R.id.tvHash1);
                                            tvHash1.setText("#" + userList.get(rand).getUserHashtag1());
                                            TextView tvHash2 = (TextView) root.findViewById(R.id.tvHash2);
                                            tvHash2.setText("#" + userList.get(rand).getUserHashtag2());
                                            TextView tvHash3 = (TextView) root.findViewById(R.id.tvHash3);
                                            tvHash3.setText("#" + userList.get(rand).getUserHashtag3());

                                            starA.setNegativeButton("네", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject = new JSONObject(response);
                                                                boolean success = jsonObject.getBoolean("success");

                                                                if (success) { //로그인에 성공한 경우
                                                                    String uNick = jsonObject.getString("userNickname");
                                                                    String uPoint = jsonObject.getString("userPoint");
                                                                    String uPL = jsonObject.getString("userPointList");
                                                                    Log.d("young", uPoint);

                                                                    if (Integer.parseInt(uPoint) - 30 < 0) {//포인트부족
                                                                        Toast.makeText(getApplicationContext(), "포인트가 부족합니다.", Toast.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent(getApplicationContext(), Mypage.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    } else {
                                                                        youNick = userList.get(rand).getUserNickname();
                                                                        youStar = userList.get(rand).getUserStar();
                                                                        youState = userList.get(rand).getUserState();
                                                                        youHash1 = userList.get(rand).getUserHashtag1();
                                                                        youHash2 = userList.get(rand).getUserHashtag2();
                                                                        youHash3 = userList.get(rand).getUserHashtag3();

                                                                        Intent intent = new Intent(getApplicationContext(), Chatting.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    };

                                                    PointRequest pointRequest = new PointRequest(Login.uNick, responseListener);
                                                    RequestQueue queue = Volley.newRequestQueue(Mypage.this);
                                                    queue.add(pointRequest);
                                                }
                                            });
                                            starA.setPositiveButton("아니요", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    btnAuto.setBackgroundResource(R.color.colorPrimary);
                                                    btnList.setBackgroundColor(Color.LTGRAY);
                                                }
                                            });
                                            starA.setCancelable(false);
                                            starA.show();
                                        }
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
                }, 3000);
            }

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

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        public BackgroundTask(String target) {
            this.target = target;
        }

        @Override
        protected void onPreExecute() {
            //List.php은 파싱으로 가져올 웹페이지
            //target = "https://jsu3229.cafe24.com/dbeditor/List_userStar.php";
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                URL url = new URL(target);//URL 객체 생성

                //URL을 이용해서 웹페이지에 연결하는 부분
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                //바이트단위 입력스트림 생성 소스는 httpURLConnection
                InputStream inputStream = httpURLConnection.getInputStream();

                //웹페이지 출력물을 버퍼로 받음 버퍼로 하면 속도가 더 빨라짐
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;

                //문자열 처리를 더 빠르게 하기 위해 StringBuilder클래스를 사용함
                StringBuilder stringBuilder = new StringBuilder();

                //한줄씩 읽어서 stringBuilder에 저장함
                while ((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");//stringBuilder에 넣어줌
                }

                //사용했던 것도 다 닫아줌
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();//trim은 앞뒤의 공백을 제거함

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            Intent intent = getIntent();

            userList = new ArrayList<User>();
            saveList = new ArrayList<User>();
            //어댑터 초기화부분 userList와 어댑터를 연결해준다.

            try {
                //intent로 값을 가져옵니다 이때 JSONObject타입으로 가져옵니다
                JSONObject jsonObject = new JSONObject(result);


                //List.php 웹페이지에서 response라는 변수명으로 JSON 배열을 만들었음..
                JSONArray jsonArray = jsonObject.getJSONArray("response");
                int count = 0;

                //JSON 배열 길이만큼 반복문을 실행
                while (count < jsonArray.length()) {
                    //count는 배열의 인덱스를 의미
                    JSONObject object = jsonArray.getJSONObject(count);

                    userNickname = object.getString("userNickname");//여기서 ID가 대문자임을 유의
                    userGender = object.getString("userGender");
                    userStar = object.getString("userStar");
                    userState = object.getString("userState");
                    userHashtag1 = object.getString("userHashtag1");
                    userHashtag2 = object.getString("userHashtag2");
                    userHashtag3 = object.getString("userHashtag3");

                    final User user = new User(userNickname, userGender, userStar, userState, userHashtag1, userHashtag2, userHashtag3);
                    userList.add(user);//리스트뷰에 값을 추가해줍니다
                    saveList.add(user);
                    count++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
