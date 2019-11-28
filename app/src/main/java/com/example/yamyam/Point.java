package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class Point extends AppCompatActivity {

    public ArrayList<PointList> pointList = new ArrayList<>();
    private PointListAdapter adapter;
    private ListView pointListView;
    public static String userNickname, userPoint, userPointList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point);
        setTitle("포인트");
        new BackgroundTask().execute();
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

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "https://jsu3229.cafe24.com/dbeditor/PointList_yam.php";
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

            pointListView = (ListView) findViewById(R.id.pointListView);
            pointList = new ArrayList<PointList>();

            //어댑터 초기화부분 userList와 어댑터를 연결해준다.
            adapter = new PointListAdapter(getApplicationContext(), pointList);
            pointListView.setAdapter(adapter);

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

                    userNickname = object.getString("userNickname");
                    userPoint = object.getString("userPoint");
                    userPointList = object.getString("userPointList");
                    if(userPointList.equals("")){
                        TextView getAllPoint = (TextView)findViewById(R.id.getAllPoint);
                        getAllPoint.setText(userPoint);
                    }
                    String[] tmp = userPointList.split("/");

                    for(String item : tmp) {
                        StringTokenizer token = new StringTokenizer(item, " ");
                        while (token.hasMoreTokens()) {
                            String resultPoint1 = token.nextToken();
                            if (userNickname.equals(Login.uNick)) {
                                TextView getAllPoint = (TextView)findViewById(R.id.getAllPoint);
                                getAllPoint.setText(userPoint);
                                final PointList plist = new PointList(resultPoint1, token.nextToken());
                                pointList.add(plist);//리스트뷰에 값을 추가해줍니다
                            }
                        }
                    }
                    count++;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}