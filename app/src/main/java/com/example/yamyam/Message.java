package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

public class Message extends AppCompatActivity implements View.OnClickListener {
    private Button btnMenu, btnSendUser, btnSendAdmin;
    private TextView tvMenu;
    private String userFrom, userTo, Text;
    public static String userfrom, userto, text;
    public static ArrayList<Msmg> messageList = new ArrayList<>();
    public ArrayList<Msmg> mgsaveList = new ArrayList<>();
    private static String point = "";
    private static String showPoint = "";
    private ListView MglistView;
    private FromListAdapter adapter;
    private ToListAdapter adapter1;
    private int adminChk = 0;
    private boolean nickChk = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        setTitle("쪽지함");

        userFrom = Login.uNick;

        btnMenu = (Button) findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(this);

        tvMenu = (TextView) findViewById(R.id.tvMenu);

        btnSendUser = (Button) findViewById(R.id.btnSendUser);
        btnSendUser.setOnClickListener(this);

        btnSendAdmin = (Button) findViewById(R.id.btnSendAdmin);
        btnSendAdmin.setOnClickListener(this);

        MglistView = (ListView) findViewById(R.id.MglistView);

        new BackgroundTask().execute();

    }

    public void onClick(View view) {
        if (view == btnMenu) {
            PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
            MenuInflater inflater = popupMenu.getMenuInflater();
            Menu menu = popupMenu.getMenu();
            inflater.inflate(R.menu.msgtype, menu);

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()) {
                        case R.id.itemAccept:
                            tvMenu.setText("보낸쪽지함");
                            Toast.makeText(getApplicationContext(), "보낸쪽지함.", Toast.LENGTH_SHORT).show();
                            MglistView.setAdapter(adapter);
                            messageList.clear();
                            for (int i = 0; i < mgsaveList.size(); i++) {
                                if (mgsaveList.get(i).getuserTo().equals(Login.uNick)) {
                                    messageList.add(mgsaveList.get(i));
                                }
                            }
                            adapter.notifyDataSetChanged();
                            break;

                        case R.id.itemSend:
                            tvMenu.setText("받은쪽지함");
                            Toast.makeText(getApplicationContext(), "받은쪽지함.", Toast.LENGTH_SHORT).show();
                            MglistView.setAdapter(adapter1);
                            messageList.clear();
                            for (int i = 0; i < mgsaveList.size(); i++) {
                                if (mgsaveList.get(i).getuserFrom().equals(Login.uNick)) {
                                    messageList.add(mgsaveList.get(i));
                                }
                            }
                            adapter1.notifyDataSetChanged();

                            MglistView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

                            break;
                    }
                    return false;
                }
            });

            popupMenu.show();

        } else if (view == btnSendAdmin) {
            AlertDialog.Builder digAdminMsg = new AlertDialog.Builder(Message.this);
            digAdminMsg.setTitle("관리자에게 쪽지보내기");
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View root = inflater.inflate(R.layout.digadminmsg, null);
            digAdminMsg.setView(root);

            final LinearLayout llR = (LinearLayout) root.findViewById(R.id.llR);
            final LinearLayout llQ = (LinearLayout) root.findViewById(R.id.llQ);
            final EditText etRContent = (EditText) root.findViewById(R.id.etRContent);
            final EditText etQContent = (EditText) root.findViewById(R.id.etQContent);

            RadioGroup rg = (RadioGroup) root.findViewById(R.id.rg);
            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (i == R.id.rbR) {
                        llR.setVisibility(View.VISIBLE);
                        etRContent.setText("");
                        llQ.setVisibility(View.INVISIBLE);
                        adminChk = 1;
                    } else if (i == R.id.rbQ) {
                        llR.setVisibility(View.INVISIBLE);
                        llQ.setVisibility(View.VISIBLE);
                        etQContent.setText("");
                        adminChk = 2;
                    }
                }
            });


            digAdminMsg.setNegativeButton("보내기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String A = "x", I = "x";

                    if (adminChk == 1) {//신고
                        Text = etRContent.getText().toString();
                        A = "o";
                        I = "x";
                    } else if (adminChk == 2) {//문의
                        Text = etQContent.getText().toString();
                        I = "o";
                        A = "x";
                    }

                    if (etRContent.getText().toString().isEmpty() && etQContent.getText().toString().isEmpty()) {
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

                                                    if (success) {
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
                                                                                            //Toast.makeText(getApplicationContext(), "포인트가 추가 성공!!", Toast.LENGTH_SHORT).show();
                                                                                            Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                                                                                                @Override
                                                                                                public void onResponse(String response) {
                                                                                                    try {
                                                                                                        JSONObject jsonObject = new JSONObject(response);
                                                                                                        boolean success = jsonObject.getBoolean("success");
                                                                                                        if (success) {
                                                                                                            Intent intent = new Intent(getApplicationContext(),Message.class);
                                                                                                            startActivity(intent);
                                                                                                            finish();
                                                                                                            //Toast.makeText(getApplicationContext(), "concat 성공!!", Toast.LENGTH_SHORT).show();
                                                                                                        } else {
                                                                                                            Intent intent = new Intent(getApplicationContext(),Message.class);
                                                                                                            startActivity(intent);
                                                                                                            finish();
                                                                                                            //Toast.makeText(getApplicationContext(), "concat 실패!!.", Toast.LENGTH_SHORT).show();
                                                                                                        }
                                                                                                    } catch (JSONException e) {
                                                                                                        e.printStackTrace();
                                                                                                    }
                                                                                                }
                                                                                            };
                                                                                            ShowPointRequest showPointRequest = new ShowPointRequest(showPoint, Login.uNick, responseListener2);
                                                                                            RequestQueue queue2 = Volley.newRequestQueue(Message.this);
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
                                                                            RequestQueue queue1 = Volley.newRequestQueue(Message.this);
                                                                            queue1.add(pointAddRequest);
                                                                        }
                                                                    } catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }

                                                                }
                                                            };
                                                            //서버로 Volley를 이용해서 요청을 함
                                                            AttendSetRequest attendSetRequest = new AttendSetRequest(Login.uId, uDate, uContinuity, "오늘의 미션 성공", "o", responseListener);
                                                            RequestQueue queue = Volley.newRequestQueue(Message.this);
                                                            queue.add(attendSetRequest);
                                                        } else {
                                                            Intent intent = new Intent(getApplicationContext(),Message.class);
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };

                                        AttendChkRequest attendChkRequest = new AttendChkRequest(Login.uId, responseListener2);
                                        RequestQueue queue2 = Volley.newRequestQueue(Message.this);
                                        queue2.add(attendChkRequest);

                                        Intent intent = new Intent(getApplicationContext(),Message.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "메시지를 보내지 못하였습니다.", Toast.LENGTH_SHORT).show();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        MessageRequest messageRequest = new MessageRequest(userFrom, "admin", Text, A, I, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Message.this);
                        queue.add(messageRequest);
                    }
                }
            });
            digAdminMsg.setPositiveButton("취소", null);
            digAdminMsg.show();
        } else if (view == btnSendUser) {
            AlertDialog.Builder digUserMsg = new AlertDialog.Builder(Message.this);
            digUserMsg.setTitle("사용자에게 쪽지보내기");
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View root = inflater.inflate(R.layout.digusermsg, null);
            digUserMsg.setView(root);

            final EditText etNickname = (EditText) root.findViewById(R.id.etNick);
            Button btnNickChk = (Button) root.findViewById(R.id.btnNickChk);
            btnNickChk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String uNickname = etNickname.getText().toString();
                    userTo = uNickname;
                    if (uNickname.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "닉네임을 입력해 주세요.", Toast.LENGTH_SHORT).show();
                    } else if (uNickname.equals(Login.uNick)) {
                        Toast.makeText(getApplicationContext(), "자신에게 쪽지를 보낼 수는 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");

                                    if (success) { //사용자 있음
                                        nickChk = true;
                                        Toast.makeText(getApplicationContext(), "일치하는 사용자가 있습니다.", Toast.LENGTH_SHORT).show();
                                    } else { //사용자 없음
                                        Toast.makeText(getApplicationContext(), "일치하는 사용자가 없습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        NickChkRequest nickChkRequest = new NickChkRequest(uNickname, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Message.this);
                        queue.add(nickChkRequest);
                    }

                }
            });

            final EditText etUserContent = (EditText) root.findViewById(R.id.etUserContent);

            digUserMsg.setNegativeButton("보내기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (nickChk) {
                        final String uText = etUserContent.getText().toString();
                        Text = uText;
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
                                                                                                //Toast.makeText(getApplicationContext(), "포인트가 추가 성공!!", Toast.LENGTH_SHORT).show();
                                                                                                Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                                                                                                    @Override
                                                                                                    public void onResponse(String response) {
                                                                                                        try {
                                                                                                            JSONObject jsonObject = new JSONObject(response);
                                                                                                            boolean success = jsonObject.getBoolean("success");
                                                                                                            if (success) {
                                                                                                                Intent intent = new Intent(getApplicationContext(),Message.class);
                                                                                                                startActivity(intent);
                                                                                                                finish();
                                                                                                                //Toast.makeText(getApplicationContext(), "concat 성공!!", Toast.LENGTH_SHORT).show();
                                                                                                            } else {
                                                                                                                Intent intent = new Intent(getApplicationContext(),Message.class);
                                                                                                                startActivity(intent);
                                                                                                                finish();
                                                                                                                //Toast.makeText(getApplicationContext(), "concat 실패!!.", Toast.LENGTH_SHORT).show();
                                                                                                            }
                                                                                                        } catch (JSONException e) {
                                                                                                            e.printStackTrace();
                                                                                                        }
                                                                                                    }
                                                                                                };
                                                                                                ShowPointRequest showPointRequest = new ShowPointRequest(showPoint, Login.uNick, responseListener2);
                                                                                                RequestQueue queue2 = Volley.newRequestQueue(Message.this);
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
                                                                                RequestQueue queue1 = Volley.newRequestQueue(Message.this);
                                                                                queue1.add(pointAddRequest);
                                                                            }
                                                                        } catch (JSONException e) {
                                                                            e.printStackTrace();
                                                                        }

                                                                    }
                                                                };
                                                                //서버로 Volley를 이용해서 요청을 함
                                                                AttendSetRequest attendSetRequest = new AttendSetRequest(Login.uId, uDate, uContinuity, "오늘의 미션 성공", "o", responseListener);
                                                                RequestQueue queue = Volley.newRequestQueue(Message.this);
                                                                queue.add(attendSetRequest);
                                                            } else {
                                                                Intent intent = new Intent(getApplicationContext(),Message.class);
                                                                startActivity(intent);
                                                                finish();
                                                            }
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            };

                                            AttendChkRequest attendChkRequest = new AttendChkRequest(Login.uId, responseListener2);
                                            RequestQueue queue2 = Volley.newRequestQueue(Message.this);
                                            queue2.add(attendChkRequest);

                                            Intent intent = new Intent(getApplicationContext(),Message.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), "메시지를 보내지 못하였습니다.", Toast.LENGTH_SHORT).show();

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            };
                            MessageRequest messageRequest = new MessageRequest(userFrom, userTo, Text, "", "", responseListener);
                            RequestQueue queue = Volley.newRequestQueue(Message.this);
                            queue.add(messageRequest);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "닉네임을 확인해주세요.", Toast.LENGTH_SHORT).show();

                    }

                }
            });

            digUserMsg.setPositiveButton("취소", null);
            digUserMsg.show();
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

        @Override
        protected void onPreExecute() {
            //List.php은 파싱으로 가져올 웹페이지
            target = "https://jsu3229.cafe24.com/dbeditor/MessageList.php";
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

            MglistView = (ListView) findViewById(R.id.MglistView);
            messageList = new ArrayList<Msmg>();
            mgsaveList = new ArrayList<Msmg>();

            //어댑터 초기화부분 userList와 어댑터를 연결해준다.
            adapter = new FromListAdapter(getApplicationContext(), messageList, mgsaveList);
            adapter1 = new ToListAdapter(getApplicationContext(), messageList, mgsaveList);
            MglistView.setAdapter(adapter1);
            MglistView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

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

                    userfrom = object.getString("userFrom");
                    userto = object.getString("userTo");
                    text = object.getString("Text");


                    final Msmg msmg = new Msmg(userfrom, userto, text);
                    messageList.add(msmg);//리스트뷰에 값을 추가해줍니다
                    mgsaveList.add(msmg);

                    count++;

                }
                messageList.clear();
                for (int i = 0; i < mgsaveList.size(); i++) {
                    if (mgsaveList.get(i).getuserFrom().equals(Login.uNick)) {
                        messageList.add(mgsaveList.get(i));
                    }
                }
                adapter1.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}