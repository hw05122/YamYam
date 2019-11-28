package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

public class Chat extends AppCompatActivity {
    private ImageButton btnChatting, btnAuto, btnMy, btnList;
    private Button btnCurrent, btnHash, btnName, btnOption;
    public static ArrayList<User> userList = new ArrayList<>();
    public ArrayList<User> saveList = new ArrayList<>();
    public ArrayList<User> searchList = new ArrayList<>();
    private int genChk = 0;
    private AutoCompleteTextView autoTv;
    private ListView listView;
    private UserListAdapter adapter;
    public static String userNickname, userGender, userStar, userState, userHashtag1, userHashtag2, userHashtag3;
    public static String youNick = "", youStar, youState, youHash1, youHash2, youHash3;
    private String userFavorites="";
    private static String point = "";
    private static String showPoint = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle("상담하기");

        userFavorites = Login.uFavorites;

        btnList = (ImageButton) findViewById(R.id.btnList);
        btnList.setBackgroundColor(Color.LTGRAY);

        btnCurrent = (Button) findViewById(R.id.btnCurrent);
        btnHash = (Button) findViewById(R.id.btnHash);
        btnName = (Button) findViewById(R.id.btnName);

        btnOption = (Button) findViewById(R.id.btnOption);

        btnChatting = (ImageButton) findViewById(R.id.btnChatting);
        btnAuto = (ImageButton) findViewById(R.id.btnAuto);
        btnAuto.setBackgroundResource(R.color.colorPrimary);
        btnMy = (ImageButton) findViewById(R.id.btnMy);

        autoTv = (AutoCompleteTextView) findViewById(R.id.autoTv);

        Response.Listener<String> responseListener1 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) { //로그인에 성공한 경우
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
        RequestQueue queue1 = Volley.newRequestQueue(Chat.this);
        queue1.add(loginRequest);

        new BackgroundTask("https://jsu3229.cafe24.com/dbeditor/List_userStar.php").execute();
    }

    public void onClick(View view) {

        if (view == btnCurrent) {//현재순위(별점순)
            new BackgroundTask("https://jsu3229.cafe24.com/dbeditor/List_userStar.php").execute();

        } else if (view == btnHash) {//해시태그
            new BackgroundTask("https://jsu3229.cafe24.com/dbeditor/List_userHash.php").execute();
        } else if (view == btnName) {//닉네임순
            userList.clear();
            for (int i = 0; i < saveList.size(); i++) {
                Collections.sort(searchList, comparator_nickname);
                userList.add(searchList.get(i));
            }
            adapter.notifyDataSetChanged();

        } else if (view == btnOption) {
            AlertDialog.Builder starB = new AlertDialog.Builder(Chat.this);
            starB.setTitle("조건검색").setIcon(R.drawable.logo4);
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            final View root = inflater.inflate(R.layout.digoption, null);
            starB.setView(root);

            final LinearLayout llHashtag = (LinearLayout) root.findViewById(R.id.llHashtag);
            final LinearLayout llGender = (LinearLayout) root.findViewById(R.id.llGender);
            final LinearLayout llStar = (LinearLayout) root.findViewById(R.id.llStar);
            RadioGroup rgOption = (RadioGroup) root.findViewById(R.id.rgOption);
            rgOption.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.rbHashtag) {
                        llHashtag.setVisibility(View.VISIBLE);
                        llGender.setVisibility(View.INVISIBLE);
                        llStar.setVisibility(View.INVISIBLE);
                    } else if (checkedId == R.id.rbGender) {
                        llHashtag.setVisibility(View.INVISIBLE);
                        llGender.setVisibility(View.VISIBLE);
                        llStar.setVisibility(View.INVISIBLE);
                    } else if (checkedId == R.id.rbStar) {
                        llHashtag.setVisibility(View.INVISIBLE);
                        llGender.setVisibility(View.INVISIBLE);
                        llStar.setVisibility(View.VISIBLE);
                    }
                }
            });

            RadioGroup rgState = (RadioGroup) root.findViewById(R.id.rgGen);
            rgState.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (checkedId == R.id.rbM) {
                        genChk = 1;
                    } else if (checkedId == R.id.rbW) {
                        genChk = 2;
                    }
                }
            });

            starB.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (genChk == 1) {//남
                        userList.clear();
                        for (int i = 0; i < saveList.size(); i++) {
                            if (saveList.get(i).getUserGender().contains("M")) {
                                userList.add(saveList.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if (genChk == 2) {//여
                        userList.clear();
                        for (int i = 0; i < saveList.size(); i++) {
                            if (saveList.get(i).getUserGender().contains("F")) {
                                userList.add(saveList.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    }

                    RatingBar ratingBar = (RatingBar) root.findViewById(R.id.ratingBar);
                    float rating = ratingBar.getRating();
                    if (rating == 1) {
                        userList.clear();
                        for (int i = 0; i < saveList.size(); i++) {
                            if (saveList.get(i).getUserStar().contains("1.")) {
                                userList.add(saveList.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if (rating == 2) {
                        userList.clear();
                        for (int i = 0; i < saveList.size(); i++) {
                            if (saveList.get(i).getUserStar().contains("2.")) {
                                userList.add(saveList.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if (rating == 3) {
                        userList.clear();
                        for (int i = 0; i < saveList.size(); i++) {
                            if (saveList.get(i).getUserStar().contains("3.")) {
                                userList.add(saveList.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if (rating == 4) {
                        userList.clear();
                        for (int i = 0; i < saveList.size(); i++) {
                            if (saveList.get(i).getUserStar().contains("4.")) {
                                userList.add(saveList.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if (rating == 5) {
                        userList.clear();
                        for (int i = 0; i < saveList.size(); i++) {
                            if (saveList.get(i).getUserStar().contains("5.")) {
                                userList.add(saveList.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    }

                    CheckBox chk1 = (CheckBox) root.findViewById(R.id.chk1);
                    CheckBox chk2 = (CheckBox) root.findViewById(R.id.chk2);
                    CheckBox chk3 = (CheckBox) root.findViewById(R.id.chk3);
                    CheckBox chk4 = (CheckBox) root.findViewById(R.id.chk4);
                    CheckBox chk5 = (CheckBox) root.findViewById(R.id.chk5);
                    CheckBox chk6 = (CheckBox) root.findViewById(R.id.chk6);
                    CheckBox chk7 = (CheckBox) root.findViewById(R.id.chk7);
                    CheckBox chk8 = (CheckBox) root.findViewById(R.id.chk8);
                    CheckBox chk9 = (CheckBox) root.findViewById(R.id.chk9);
                    CheckBox chk10 = (CheckBox) root.findViewById(R.id.chk10);
                    CheckBox chk11 = (CheckBox) root.findViewById(R.id.chk11);
                    CheckBox chk12 = (CheckBox) root.findViewById(R.id.chk12);

                    if (chk1.isChecked()) {
                        userList.clear();
                        for (int i = 0; i < saveList.size(); i++) {
                            if (saveList.get(i).getUserHashtag1().contains("고백") || saveList.get(i).getUserHashtag2().contains("고백") || saveList.get(i).getUserHashtag3().contains("고백")) {
                                userList.add(saveList.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if (chk2.isChecked()) {
                        userList.clear();
                        for (int i = 0; i < saveList.size(); i++) {
                            if (saveList.get(i).getUserHashtag1().contains("권태기") || saveList.get(i).getUserHashtag2().contains("권태기") || saveList.get(i).getUserHashtag3().contains("권태기")) {
                                userList.add(saveList.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if (chk3.isChecked()) {
                        userList.clear();
                        for (int i = 0; i < saveList.size(); i++) {
                            if (saveList.get(i).getUserHashtag1().contains("다툼") || saveList.get(i).getUserHashtag2().contains("다툼") || saveList.get(i).getUserHashtag3().contains("다툼")) {
                                userList.add(saveList.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if (chk4.isChecked()) {
                        userList.clear();
                        for (int i = 0; i < saveList.size(); i++) {
                            if (saveList.get(i).getUserHashtag1().contains("데이트") || saveList.get(i).getUserHashtag2().contains("데이트") || saveList.get(i).getUserHashtag3().contains("데이트")) {
                                userList.add(saveList.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if (chk5.isChecked()) {
                        userList.clear();
                        for (int i = 0; i < saveList.size(); i++) {
                            if (saveList.get(i).getUserHashtag1().contains("미련") || saveList.get(i).getUserHashtag2().contains("미련") || saveList.get(i).getUserHashtag3().contains("미련")) {
                                userList.add(saveList.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if (chk6.isChecked()) {
                        userList.clear();
                        for (int i = 0; i < saveList.size(); i++) {
                            if (saveList.get(i).getUserHashtag1().contains("바람") || saveList.get(i).getUserHashtag2().contains("바람") || saveList.get(i).getUserHashtag3().contains("바람")) {
                                userList.add(saveList.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if (chk7.isChecked()) {
                        userList.clear();
                        for (int i = 0; i < saveList.size(); i++) {
                            if (saveList.get(i).getUserHashtag1().contains("사과") || saveList.get(i).getUserHashtag2().contains("사과") || saveList.get(i).getUserHashtag3().contains("사과"))
                                userList.add(saveList.get(i));
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if (chk8.isChecked()) {
                        userList.clear();
                        for (int i = 0; i < saveList.size(); i++) {
                            if (saveList.get(i).getUserHashtag1().contains("소개팅") || saveList.get(i).getUserHashtag2().contains("소개팅") || saveList.get(i).getUserHashtag3().contains("소개팅")) {
                                userList.add(saveList.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if (chk9.isChecked()) {
                        userList.clear();
                        for (int i = 0; i < saveList.size(); i++) {
                            if (saveList.get(i).getUserHashtag1().contains("썸") || saveList.get(i).getUserHashtag2().contains("썸") || saveList.get(i).getUserHashtag3().contains("썸")) {
                                userList.add(saveList.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if (chk10.isChecked()) {
                        userList.clear();
                        for (int i = 0; i < saveList.size(); i++) {
                            if (saveList.get(i).getUserHashtag1().contains("이별") || saveList.get(i).getUserHashtag2().contains("이별") || saveList.get(i).getUserHashtag3().contains("이별")) {
                                userList.add(saveList.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if (chk11.isChecked()) {
                        userList.clear();
                        for (int i = 0; i < saveList.size(); i++) {
                            if (saveList.get(i).getUserHashtag1().contains("짝사랑") || saveList.get(i).getUserHashtag2().contains("짝사랑") || saveList.get(i).getUserHashtag3().contains("짝사랑")) {
                                userList.add(saveList.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if (chk12.isChecked()) {
                        userList.clear();
                        for (int i = 0; i < saveList.size(); i++) {
                            if (saveList.get(i).getUserHashtag1().contains("첫사랑") || saveList.get(i).getUserHashtag2().contains("첫사랑") || saveList.get(i).getUserHashtag3().contains("첫사랑")) {
                                userList.add(saveList.get(i));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            starB.setPositiveButton("취소", null);
            starB.setCancelable(false);

            starB.show();
        } else if (view == btnChatting) {
            Response.Listener<String> responseListener1 = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");

                        if (success) { //로그인에 성공한 경우
                            Login.uStar = jsonObject.getString("userStar");
                            Login.uState = jsonObject.getString("userState");
                            Login.uRoll = jsonObject.getString("userRoll");
                            Login.uStop = jsonObject.getString("userStop");

                            if (Login.uState.equals("h")) {
                                Intent intent = new Intent(getApplicationContext(), Chatting.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "상담방이 생성되지 않았습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            LoginRequest loginRequest = new LoginRequest(Login.uId, Login.uPw, responseListener1);
            RequestQueue queue1 = Volley.newRequestQueue(Chat.this);
            queue1.add(loginRequest);

        } else if (view == btnAuto) {
            if (Login.uState.equals("h")) {
                Toast.makeText(getApplicationContext(), "현재 상담을 끝낸 후에 상담하기를 눌러주세요.", Toast.LENGTH_SHORT).show();
            } else {
                btnList.setBackgroundResource(R.color.colorPrimary);
                btnAuto.setBackgroundColor(Color.LTGRAY);
                AlertDialog.Builder starB = new AlertDialog.Builder(Chat.this);
                starB.setTitle("자동매칭 중 입니다").setIcon(R.drawable.logo4);
                starB.setMessage("     상담통계를 토대로 분석을 하고 있습니다.");
                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View root = inflater.inflate(R.layout.digauto, null);
                starB.setView(root);
                starB.setCancelable(false);
                final AlertDialog adB = starB.create();
                adB.show();
                starB.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adB.dismiss();
                    }
                });

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
                                            AlertDialog.Builder starA = new AlertDialog.Builder(Chat.this);
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
                                                                        Intent intent = new Intent(getApplicationContext(), Chat.class);
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
                                                    RequestQueue queue = Volley.newRequestQueue(Chat.this);
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

                                            AlertDialog.Builder starA = new AlertDialog.Builder(Chat.this);
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
                                                                        Intent intent = new Intent(getApplicationContext(), Chat.class);
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
                                                    RequestQueue queue = Volley.newRequestQueue(Chat.this);
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
                        RequestQueue queue = Volley.newRequestQueue(Chat.this);
                        queue.add(autoRequest);
                    }
                }, 3000);
            }

        } else if (view == btnMy) {
            Intent intent = new Intent(getApplicationContext(), Mypage.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(getApplicationContext(), Main.class);
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

            listView = (ListView) findViewById(R.id.listView);
            userList = new ArrayList<User>();
            saveList = new ArrayList<User>();
            searchList = new ArrayList<User>();

            //어댑터 초기화부분 userList와 어댑터를 연결해준다.
            adapter = new UserListAdapter(getApplicationContext(), userList, saveList);
            listView.setAdapter(adapter);

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
                    searchList.add(user);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            View viewProfile = (View) View.inflate(Chat.this, R.layout.profile, null);
                            AlertDialog.Builder digProfile = new AlertDialog.Builder(Chat.this);
                            digProfile.setView(viewProfile).setTitle("프로필");
                            TextView proNick = (TextView) viewProfile.findViewById(R.id.tvNick);
                            proNick.setText(userList.get(position).getUserNickname());
                            TextView proGen = (TextView) viewProfile.findViewById(R.id.tvGender);
                            proGen.setText(userList.get(position).getUserGender());
                            TextView proStar = (TextView) viewProfile.findViewById(R.id.tvStar);
                            proStar.setText(userList.get(position).getUserStar());
                            ImageView proState = (ImageView) viewProfile.findViewById(R.id.ivState);
                            if (userList.get(position).getUserState().equals("o")) {
                                if (userList.get(position).userGender.equals("M")) {
                                    proState.setImageResource(R.drawable.man);
                                } else if (userList.get(position).userGender.equals("F")) {
                                    proState.setImageResource(R.drawable.woman);
                                }
                            } else if (userList.get(position).getUserState().equals("x")) {
                                proState.setImageResource(R.drawable.ic_close);
                            } else if (userList.get(position).getUserState().equals("h")) {
                                proState.setImageResource(R.drawable.ic_heart);
                            }
                            TextView proHashtag1 = (TextView) viewProfile.findViewById(R.id.tvHash1);
                            proHashtag1.setText("#" + userList.get(position).getUserHashtag1());
                            TextView proHashtag2 = (TextView) viewProfile.findViewById(R.id.tvHash2);
                            proHashtag2.setText("#" + userList.get(position).getUserHashtag2());
                            TextView proHashtag3 = (TextView) viewProfile.findViewById(R.id.tvHash3);
                            proHashtag3.setText("#" + userList.get(position).getUserHashtag3());

                            ImageButton ibChatting = (ImageButton) viewProfile.findViewById(R.id.btnChatting);
                            ibChatting.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (Login.uState.equals("h")) {
                                        Toast.makeText(getApplicationContext(), "현재 상담을 끝낸 후에 상담하기를 눌러주세요.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (userList.get(position).getUserState().equals("h")) {//상담방 없는 경우
                                            Toast.makeText(getApplicationContext(), "현재 상담중인 사용자입니다.", Toast.LENGTH_SHORT).show();
                                        } else if (userList.get(position).getUserState().equals("x")) {
                                            Toast.makeText(getApplicationContext(), "현재 휴업중인 사용자입니다.", Toast.LENGTH_SHORT).show();
                                        } else if (userList.get(position).getUserNickname().equals(Login.uNick)) {
                                            Toast.makeText(getApplicationContext(), "자신과는 상담을 할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                        } else {
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
                                                            } else {
                                                                youNick = userList.get(position).getUserNickname();
                                                                youStar = userList.get(position).getUserStar();
                                                                youState = userList.get(position).getUserState();
                                                                youHash1 = userList.get(position).getUserHashtag1();
                                                                youHash2 = userList.get(position).getUserHashtag2();
                                                                youHash3 = userList.get(position).getUserHashtag3();

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
                                            RequestQueue queue = Volley.newRequestQueue(Chat.this);
                                            queue.add(pointRequest);
                                        }
                                    }
                                }
                            });

                            final TextView tvNick = (TextView) viewProfile.findViewById(R.id.tvNick);
                            ImageButton ibMsg = (ImageButton) viewProfile.findViewById(R.id.btnMsg);
                            ibMsg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (userList.get(position).getUserNickname().equals(Login.uNick)) {
                                        Toast.makeText(getApplicationContext(), "자신에게 쪽지를 보낼 수 없습니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        AlertDialog.Builder digUserMsg = new AlertDialog.Builder(Chat.this);
                                        digUserMsg.setTitle("사용자에게 쪽지보내기");
                                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                        View root = inflater.inflate(R.layout.digusermsg, null);
                                        digUserMsg.setView(root);

                                        EditText etNick = (EditText) root.findViewById(R.id.etNick);
                                        etNick.setText(tvNick.getText().toString());
                                        etNick.setEnabled(false);
                                        Button btnNickChk = (Button) root.findViewById(R.id.btnNickChk);
                                        btnNickChk.setVisibility(View.INVISIBLE);

                                        final EditText etUserContent = (EditText) root.findViewById(R.id.etUserContent);

                                        digUserMsg.setNegativeButton("보내기", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                String uText = etUserContent.getText().toString();
                                                String Text = uText;
                                                if (uText.isEmpty()) {
                                                    Toast.makeText(getApplicationContext(), "내용을 입력하세요.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject = new JSONObject(response);
                                                                boolean success = jsonObject.getBoolean("success");
                                                                if (success) {
                                                                    Toast.makeText(getApplicationContext(), "메시지를 보냈습니다.", Toast.LENGTH_SHORT).show();

                                                                    Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String response) {
                                                                            try {
                                                                                JSONObject jsonObject = new JSONObject(response);
                                                                                boolean success = jsonObject.getBoolean("success");

                                                                                if (success) { //이때까지 출석한 적있음
                                                                                    String uId = jsonObject.getString("userID");
                                                                                    String uDate = jsonObject.getString("userDate");
                                                                                    String uContinuity = jsonObject.getString("continuity");
                                                                                    String userMission = jsonObject.getString("mission");
                                                                                    String userMissionChk = jsonObject.getString("missionChk");
                                                                                    Log.d("young", uId + " " + uDate + " " + uContinuity + " " + userMission + " " + userMissionChk);

                                                                                    long now = System.currentTimeMillis();
                                                                                    final Date date = new Date(now);

                                                                                    final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
                                                                                    final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
                                                                                    final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);
                                                                                    String TODAY = curYearFormat.format(date) + curMonthFormat.format(date) + curDayFormat.format(date);
                                                                                    StringTokenizer tokenizer = new StringTokenizer(uDate, " ");
                                                                                    boolean todayChk = false;

                                                                                    while (tokenizer.hasMoreTokens()) {
                                                                                        String str = tokenizer.nextToken();
                                                                                        if (str.equals(TODAY)) {
                                                                                            todayChk = true;
                                                                                        }
                                                                                    }

                                                                                    if (todayChk && userMission.equals("쪽지보내기") && userMissionChk.equals("x")) {
                                                                                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                                                            @Override
                                                                                            public void onResponse(String response) {
                                                                                                try {
                                                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                                                    boolean success = jsonObject.getBoolean("success");
                                                                                                    if (success) { //레코드등록 성공
                                                                                                        point = String.valueOf(100);
                                                                                                        showPoint = "미션성공 +100";

                                                                                                        Response.Listener<String> responseListener1 = new Response.Listener<String>() {
                                                                                                            @Override
                                                                                                            public void onResponse(String response) {
                                                                                                                try {
                                                                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                                                                    boolean success = jsonObject.getBoolean("success");
                                                                                                                    if (success) {
                                                                                                                        Toast.makeText(getApplicationContext(), "오늘의 미션 성공!!", Toast.LENGTH_SHORT).show();
                                                                                                                        Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                                                                                                                            @Override
                                                                                                                            public void onResponse(String response) {
                                                                                                                                try {
                                                                                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                                                                                    boolean success = jsonObject.getBoolean("success");
                                                                                                                                    if (success) {
                                                                                                                                        //Toast.makeText(getApplicationContext(), "concat 성공!!", Toast.LENGTH_SHORT).show();
                                                                                                                                    } else {
                                                                                                                                        //Toast.makeText(getApplicationContext(), "concat 실패!!.", Toast.LENGTH_SHORT).show();
                                                                                                                                    }
                                                                                                                                } catch (JSONException e) {
                                                                                                                                    e.printStackTrace();
                                                                                                                                }
                                                                                                                            }
                                                                                                                        };
                                                                                                                        ShowPointRequest showPointRequest = new ShowPointRequest(showPoint, Login.uNick, responseListener2);
                                                                                                                        RequestQueue queue2 = Volley.newRequestQueue(Chat.this);
                                                                                                                        queue2.add(showPointRequest);

                                                                                                                    } else {
                                                                                                                        //Toast.makeText(getApplicationContext(), "포인트 추가 실패!!.", Toast.LENGTH_SHORT).show();
                                                                                                                    }
                                                                                                                } catch (JSONException e) {
                                                                                                                    e.printStackTrace();
                                                                                                                }
                                                                                                            }
                                                                                                        };
                                                                                                        PointAddRequest pointAddRequest = new PointAddRequest(point, Login.uNick, responseListener1);
                                                                                                        RequestQueue queue1 = Volley.newRequestQueue(Chat.this);
                                                                                                        queue1.add(pointAddRequest);
                                                                                                    }
                                                                                                } catch (JSONException e) {
                                                                                                    e.printStackTrace();
                                                                                                }

                                                                                            }
                                                                                        };
                                                                                        //서버로 Volley를 이용해서 요청을 함
                                                                                        AttendSetRequest attendSetRequest = new AttendSetRequest(Login.uId, uDate, uContinuity, "오늘의 미션 성공", "o", responseListener);
                                                                                        RequestQueue queue = Volley.newRequestQueue(Chat.this);
                                                                                        queue.add(attendSetRequest);
                                                                                    } else {

                                                                                    }
                                                                                }
                                                                            } catch (JSONException e) {
                                                                                e.printStackTrace();
                                                                            }
                                                                        }
                                                                    };

                                                                    AttendChkRequest attendChkRequest = new AttendChkRequest(Login.uId, responseListener2);
                                                                    RequestQueue queue2 = Volley.newRequestQueue(Chat.this);
                                                                    queue2.add(attendChkRequest);
                                                                } else {
                                                                    Toast.makeText(getApplicationContext(), "메시지를 보내지 못하였습니다.", Toast.LENGTH_SHORT).show();

                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    };
                                                    MessageRequest messageRequest = new MessageRequest(Login.uNick, tvNick.getText().toString(), Text, "", "", responseListener);
                                                    RequestQueue queue = Volley.newRequestQueue(Chat.this);
                                                    queue.add(messageRequest);
                                                }
                                            }
                                        });

                                        digUserMsg.setPositiveButton("취소", null);
                                        digUserMsg.show();
                                    }
                                }
                            });

                            final ImageButton ibStar = (ImageButton) viewProfile.findViewById(R.id.ibStar);
                            StringTokenizer tokenizer = new StringTokenizer(userFavorites, " ");
                            while (tokenizer.hasMoreTokens()) {
                                String str = tokenizer.nextToken();
                                if (str.equals(tvNick.getText().toString())) {
                                    ibStar.setImageResource(R.drawable.ic_star_full);
                                }
                            }

                            ibStar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (userList.get(position).getUserNickname().equals(Login.uNick)) {
                                        Toast.makeText(getApplicationContext(), "자신을 즐겨찾기 할 수 없습니다.", Toast.LENGTH_SHORT).show();

                                    } else {
                                        if (ibStar.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.ic_star_empty).getConstantState())) {//빈 별
                                            userFavorites += tvNick.getText().toString() + " ";
                                            Toast.makeText(getApplicationContext(), "즐겨찾기에 추가하였습니다.", Toast.LENGTH_SHORT).show();
                                            ibStar.setImageResource(R.drawable.ic_star_full);
                                            Login.uFavorites = userFavorites;
                                        } else {
                                            String tmpFavorites = "";
                                            StringTokenizer tokenizer = new StringTokenizer(userFavorites, " ");
                                            while (tokenizer.hasMoreTokens()) {
                                                String str = tokenizer.nextToken();
                                                if (str.equals(tvNick.getText().toString())) {

                                                } else {
                                                    tmpFavorites += str + " ";
                                                }
                                            }
                                            userFavorites = tmpFavorites;
                                            Toast.makeText(getApplicationContext(), "즐겨찾기를 해제하였습니다.", Toast.LENGTH_SHORT).show();
                                            ibStar.setImageResource(R.drawable.ic_star_empty);
                                            Login.uFavorites = userFavorites;
                                        }

                                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    boolean success = jsonObject.getBoolean("success");

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        };
                                        //서버로 Volley를 이용해서 요청을 함
                                        FavoritesSetRequest favoritesSetRequest = new FavoritesSetRequest(userFavorites, Login.uId, responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(Chat.this);
                                        queue.add(favoritesSetRequest);
                                    }
                                }
                            });

                            digProfile.setNegativeButton("닫기", null);
                            digProfile.show();
                        }
                    });
                    count++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            autoTv.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    searchUser(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

        public void searchUser(String search) {
            userList.clear();
            for (int i = 0; i < saveList.size(); i++) {
                if (saveList.get(i).getUserNickname().contains(search)) {
                    userList.add(saveList.get(i));
                }

            }
            adapter.notifyDataSetChanged();
        }
    }

    Comparator<User> comparator_nickname = new Comparator<User>() {
        private final Collator collator = Collator.getInstance();

        @Override
        public int compare(User o1, User o2) {
            return collator.compare(o1.getUserNickname(), o2.getUserNickname());
        }
    };

    Comparator<User> comparator_star = new Comparator<User>() {
        private final Collator collator = Collator.getInstance();

        @Override
        public int compare(User o1, User o2) {
            if(o1.getUserStar().equals(o2.getUserStar())){
                Log.d("young","같음");
            }
            return collator.compare(o1.getUserStar(), o2.getUserStar());
        }
    };

    Comparator<User> comparator_hashtag = new Comparator<User>() {
        private final Collator collator = Collator.getInstance();

        @Override
        public int compare(User o1, User o2) {
            return collator.compare(o1.getUserHashtag1(), o2.getUserHashtag1());
        }
    };

}

