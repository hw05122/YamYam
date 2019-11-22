package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Main extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnChat, btnMsg, btnCheck, btnPoint, btnSetting;
    private TextView tvPoint, tvGrade, tvStar;
    public static String[] dateStr;
    private int dateCnt = 0;
    public static String mission, missionChk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateStr =  new String[31];
        dbDate();

        tvPoint = (TextView)findViewById(R.id.tvPoint);

        tvGrade = (TextView)findViewById(R.id.tvGrade);
        tvGrade.setText(Login.uGrade);

        tvStar = (TextView)findViewById(R.id.tvStar);
        tvStar.setText(Login.uStar);


        btnChat = (ImageButton) findViewById(R.id.btnChat);
        btnChat.setOnClickListener(this);

        btnMsg = (ImageButton) findViewById(R.id.btnMsg);
        btnMsg.setOnClickListener(this);

        btnCheck = (ImageButton) findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(this);

        btnPoint = (ImageButton) findViewById(R.id.btnPoint);
        btnPoint.setOnClickListener(this);

        btnSetting = (ImageButton) findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(this);
    }

    public void dbDate(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) { //레코드등록 성공
                        String uId = jsonObject.getString("userID");
                        String date = jsonObject.getString("userDate");
                        String continuity = jsonObject.getString("continuity");
                        mission = jsonObject.getString("mission");
                        missionChk = jsonObject.getString("missionChk");

                        StringTokenizer tokenizer = new StringTokenizer(date, " ");
                        while (tokenizer.hasMoreTokens()) {
                            String str = tokenizer.nextToken();
                            if(str.substring(6,7).equals("0")){
                                dateStr[dateCnt++] = str.substring(7,8);
                            }else{
                                dateStr[dateCnt++] = str.substring(6,8);
                            }
                        }
                    } else { //레코드등록 실패
                        Log.d("young","출석한 적없는 사용자");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        //서버로 Volley를 이용해서 요청을 함
        AttendChkRequest attendChkRequest = new AttendChkRequest(Login.uId, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Main.this);
        queue.add(attendChkRequest);
    }

    public void onClick(View view) {
        if (view == btnChat) {
            Intent intent = new Intent(getApplicationContext(), Chat.class);
            startActivity(intent);
            finish();
        } else if (view == btnMsg) {
            Intent intent = new Intent(getApplicationContext(), Message.class);
            startActivity(intent);
            finish();
        } else if (view == btnCheck) {
            Intent intent = new Intent(getApplicationContext(), Attendance.class);
            startActivity(intent);
            finish();
        } else if (view == btnPoint) {
            Intent intent = new Intent(getApplicationContext(), Point.class);
            startActivity(intent);
            finish();
        } else if (view == btnSetting) {
            Intent intent = new Intent(getApplicationContext(), Setting.class);
            startActivity(intent);
            finish();
        }
    }

    public void bookmarkShow(){

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Toast.makeText(getApplicationContext(),"설정에서 로그아웃을 눌러주세요",Toast.LENGTH_SHORT).show();
        }

        return false;
    }

}


