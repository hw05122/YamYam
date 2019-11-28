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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

public class Main extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnChat, btnMsg, btnCheck, btnPoint, btnSetting;
    private TextView tvPoint, tvStar;
    public static String[] dateStr;
    private int dateCnt = 0;
    public static String mission, missionChk;
    private ArrayList<User> userList;
    private ArrayList<User> saveList;
    private String userFavorites;
    public static String userNickname, userGender, userStar, userState, userHashtag1, userHashtag2, userHashtag3;
    public static String youNick = "", youStar, youState, youHash1, youHash2, youHash3;
    private ListView listView;
    private UserListAdapter adapter;
    private static String point = "";
    private static String showPoint = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Main");

        userList = new ArrayList<User>();
        saveList = new ArrayList<User>();

        dateStr = new String[31];
        dbDate();

        userFavorites = Login.uFavorites;
        new BackgroundTask().execute();

        tvPoint = (TextView) findViewById(R.id.tvPoint);
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
                        tvPoint.setText(uPoint);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        PointRequest pointRequest = new PointRequest(Login.uNick, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Main.this);
        queue.add(pointRequest);


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
        RequestQueue queue1 = Volley.newRequestQueue(Main.this);
        queue1.add(loginRequest);


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

    class BackgroundTask extends AsyncTask<Void, Void, String> {
        String target;

        @Override
        protected void onPreExecute() {
            target = "https://jsu3229.cafe24.com/dbeditor/List_userStar.php";
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

            listView = (ListView) findViewById(R.id.lvFavorites);
            userList = new ArrayList<User>();
            saveList = new ArrayList<User>();

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

                    StringTokenizer token = new StringTokenizer(userFavorites, " ");
                    while (token.hasMoreTokens()) {
                        String nick = token.nextToken();
                        if (userNickname.equals(nick)) {
                            final User user = new User(nick, userGender, userStar, userState, userHashtag1, userHashtag2, userHashtag3);
                            userList.add(user);//리스트뷰에 값을 추가해줍니다
                            saveList.add(user);
                        }
                    }

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            View viewProfile = (View) View.inflate(Main.this, R.layout.profile, null);
                            AlertDialog.Builder digProfile = new AlertDialog.Builder(Main.this);
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
                                            RequestQueue queue = Volley.newRequestQueue(Main.this);
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
                                        AlertDialog.Builder digUserMsg = new AlertDialog.Builder(Main.this);
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
                                                                                                                        Toast.makeText(getApplicationContext(), "포인트가 추가 성공!!", Toast.LENGTH_SHORT).show();
                                                                                                                        Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                                                                                                                            @Override
                                                                                                                            public void onResponse(String response) {
                                                                                                                                try {
                                                                                                                                    JSONObject jsonObject = new JSONObject(response);
                                                                                                                                    boolean success = jsonObject.getBoolean("success");
                                                                                                                                    if (success) {
                                                                                                                                        Toast.makeText(getApplicationContext(), "concat 성공!!", Toast.LENGTH_SHORT).show();
                                                                                                                                    } else {
                                                                                                                                        Toast.makeText(getApplicationContext(), "concat 실패!!.", Toast.LENGTH_SHORT).show();
                                                                                                                                    }
                                                                                                                                } catch (JSONException e) {
                                                                                                                                    e.printStackTrace();
                                                                                                                                }
                                                                                                                            }
                                                                                                                        };
                                                                                                                        ShowPointRequest showPointRequest = new ShowPointRequest(showPoint, Login.uNick, responseListener2);
                                                                                                                        RequestQueue queue2 = Volley.newRequestQueue(Main.this);
                                                                                                                        queue2.add(showPointRequest);

                                                                                                                    } else {
                                                                                                                        Toast.makeText(getApplicationContext(), "포인트 추가 실패!!.", Toast.LENGTH_SHORT).show();
                                                                                                                    }
                                                                                                                } catch (JSONException e) {
                                                                                                                    e.printStackTrace();
                                                                                                                }
                                                                                                            }
                                                                                                        };
                                                                                                        PointAddRequest pointAddRequest = new PointAddRequest(point, Login.uNick, responseListener1);
                                                                                                        RequestQueue queue1 = Volley.newRequestQueue(Main.this);
                                                                                                        queue1.add(pointAddRequest);
                                                                                                    }
                                                                                                } catch (JSONException e) {
                                                                                                    e.printStackTrace();
                                                                                                }

                                                                                            }
                                                                                        };
                                                                                        //서버로 Volley를 이용해서 요청을 함
                                                                                        AttendSetRequest attendSetRequest = new AttendSetRequest(Login.uId, uDate, uContinuity, "오늘의 미션 성공", "o", responseListener);
                                                                                        RequestQueue queue = Volley.newRequestQueue(Main.this);
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
                                                                    RequestQueue queue2 = Volley.newRequestQueue(Main.this);
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
                                                    RequestQueue queue = Volley.newRequestQueue(Main.this);
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
                                            userFavorites = Login.uFavorites;
                                            userFavorites += tvNick.getText().toString() + " ";
                                            Toast.makeText(getApplicationContext(), "즐겨찾기에 추가하였습니다.", Toast.LENGTH_SHORT).show();
                                            ibStar.setImageResource(R.drawable.ic_star_full);
                                            Login.uFavorites = userFavorites;

                                            Intent intent = new Intent(getApplicationContext(), Main.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            userFavorites = Login.uFavorites;
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

                                            Intent intent = new Intent(getApplicationContext(), Main.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        Log.d("young", Login.uFavorites);

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
                                        RequestQueue queue = Volley.newRequestQueue(Main.this);
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
        }
    }


    public void dbDate() {
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
                            if (str.substring(6, 7).equals("0")) {
                                dateStr[dateCnt++] = str.substring(7, 8);
                            } else {
                                dateStr[dateCnt++] = str.substring(6, 8);
                            }
                        }
                    } else { //레코드등록 실패
                        dateStr = new String[31];
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(getApplicationContext(), "설정에서 로그아웃을 눌러주세요", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

}


