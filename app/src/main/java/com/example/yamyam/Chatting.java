package com.example.yamyam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

public class Chatting extends AppCompatActivity implements View.OnClickListener {
    private EditText etMsg;
    private Button btnSend;
    private LinearLayout ll;
    private ScrollView sv;
    private String youNick = "", youStar = "", youState = "", youHash1 = "", youHash2 = "", youHash3 = "";
    private String userNick;
    private boolean isRunning = false;
    private Socket memberSocket;
    private String ip = "192.168.0.5";
    private int port = 30001;
    private static String point = "";
    private static String showPoint = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        setTitle("나의상담방");

        etMsg = (EditText) findViewById(R.id.etMsg);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        sv = (ScrollView) findViewById(R.id.sv);
        ll = (LinearLayout) findViewById(R.id.ll);

        if (Login.uState.equals("h")) {
            ConnectionThread thread = new ConnectionThread();
            thread.start();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(Chatting.this);
            builder.setTitle("상담 시작하기").setMessage("상담을 시작하시겠습니까?").setIcon(R.drawable.logo4);
            builder.setCancelable(false);
            builder.setPositiveButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getApplicationContext(), Chat.class);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Login.uState = "h";
                    point = String.valueOf(30);
                    showPoint = "내담자 -30";

                    if (!Chat.youNick.equals("")) {
                        youNick = Chat.youNick;
                        youStar = Chat.youStar;
                        youState = "h";
                        youHash1 = Chat.youHash1;
                        youHash2 = Chat.youHash2;
                        youHash3 = Chat.youHash3;
                    } else if (!Main.youNick.equals("")) {
                        youNick = Main.youNick;
                        youStar = Main.youStar;
                        youState = "h";
                        youHash1 = Main.youHash1;
                        youHash2 = Main.youHash2;
                        youHash3 = Main.youHash3;
                    } else if (!Mypage.youNick.equals("")) {
                        youNick = Mypage.youNick;
                        youStar = Mypage.youStar;
                        youState = "h";
                        youHash1 = Mypage.youHash1;
                        youHash2 = Mypage.youHash2;
                        youHash3 = Mypage.youHash3;
                    }

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

                                    if (todayChk && userMission.equals("상담 1회 하기") && userMissionChk.equals("x")) {
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
                                                                        //Toast.makeText(getApplicationContext(), "포인트가 추가 성공!!", Toast.LENGTH_SHORT).show();
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
                                                                        RequestQueue queue2 = Volley.newRequestQueue(Chatting.this);
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
                                                        RequestQueue queue1 = Volley.newRequestQueue(Chatting.this);
                                                        queue1.add(pointAddRequest);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                            }
                                        };
                                        //서버로 Volley를 이용해서 요청을 함
                                        AttendSetRequest attendSetRequest = new AttendSetRequest(Login.uId, uDate, uContinuity, "오늘의 미션 성공", "o", responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(Chatting.this);
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
                    RequestQueue queue2 = Volley.newRequestQueue(Chatting.this);
                    queue2.add(attendChkRequest);


                    Response.Listener<String> responseListener1 = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    //Toast.makeText(getApplicationContext(), "포인트 차감 성공!!", Toast.LENGTH_SHORT).show();

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
                                    RequestQueue queue2 = Volley.newRequestQueue(Chatting.this);
                                    queue2.add(showPointRequest);
                                } else {
                                    //Toast.makeText(getApplicationContext(), "포인트 추가 실패!!.", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    PointSubRequest pointRequest = new PointSubRequest(point, Login.uNick, responseListener1);
                    RequestQueue queue1 = Volley.newRequestQueue(Chatting.this);
                    queue1.add(pointRequest);

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");

                                if (success) { //수정 됨
                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                boolean success = jsonObject.getBoolean("success");

                                                if (success) { //수정 됨
                                                    ConnectionThread thread = new ConnectionThread();
                                                    thread.start();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };

                                    UpdateRequest nickChkRequest = new UpdateRequest(youNick, youStar, youState, youHash1, youHash2, youHash3, "상담사 " + Login.uNick + " " + Login.uStar + " " + Login.uState + " " + Login.uHash1 + " " + Login.uHash2 + " " + Login.uHash3, responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(Chatting.this);
                                    queue.add(nickChkRequest);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    UpdateRequest nickChkRequest = new UpdateRequest(Login.uNick, Login.uStar, "h", Login.uHash1, Login.uHash2, Login.uHash3, "내담자 " + youNick + " " + youStar + " " + youState + " " + youHash1 + " " + youHash2 + " " + youHash3, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Chatting.this);
                    queue.add(nickChkRequest);

                    Login.uRoll = "내담자 " + youNick + " " + youStar + " " + youState + " " + youHash1 + " " + youHash2 + " " + youHash3;
                }
            });
            builder.show();
        }
    }

    class ConnectionThread extends Thread {
        public void run() {
            try {
                final Socket socket = new Socket(ip, port);
                memberSocket = socket;
                userNick = Login.uNick;

                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);

                dos.writeUTF("Chatting " + userNick);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        etMsg.setText("");
                        etMsg.setHint("메세지 입력");
                        btnSend.setText("전송");
                        //isConnect = true;
                        isRunning = true;

                        MessageThraed thraed = new MessageThraed(socket);
                        thraed.start();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class MessageThraed extends Thread {
        Socket socket;
        DataInputStream dis;

        public MessageThraed(Socket socket) {
            try {
                this.socket = socket;
                InputStream is = socket.getInputStream();
                dis = new DataInputStream(is);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                while (isRunning) {
                    final String msg = dis.readUTF();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            LinearLayout llMsg = new LinearLayout(Chatting.this);
                            TextView tvMsg = new TextView(Chatting.this);
                            tvMsg.setTextColor(Color.BLACK);

                            StringTokenizer token = new StringTokenizer(msg, " ");
                            while (token.hasMoreTokens()) {
                                String nick = token.nextToken();
                                if (nick.equals(userNick)) {
                                    tvMsg.setBackgroundColor(Color.YELLOW);
                                    tvMsg.setTextSize(20);
                                    llMsg.setGravity(Gravity.RIGHT);

                                    int length = nick.length();
                                    tvMsg.setText(msg.substring(length));
                                    break;
                                } else {
                                    tvMsg.setBackgroundColor(Color.WHITE);
                                    tvMsg.setTextSize(20);
                                    llMsg.setGravity(Gravity.LEFT);
                                    int length = nick.length();
                                    tvMsg.setText(msg.substring(length));
                                    break;
                                }
                            }

                            llMsg.addView(tvMsg);
                            ll.addView(llMsg);
                            sv.fullScroll(View.FOCUS_DOWN);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class SendToServerThread extends Thread {
        Socket socket;
        String msg;
        DataOutputStream dos;

        public SendToServerThread(Socket socket, String msg) {
            try {
                this.socket = socket;
                this.msg = msg;
                OutputStream os = socket.getOutputStream();
                dos = new DataOutputStream(os);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                // 서버로 데이터를 보낸다.
                dos.writeUTF(msg);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        etMsg.setText("");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        try {
            memberSocket.close();
            isRunning = false;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.out, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.itemOut) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Chatting.this);
            builder.setTitle("상담방 나가기").setMessage("상담방을 나가겠습니까?").setIcon(R.drawable.logo4);
            builder.setCancelable(false);
            builder.setPositiveButton("아니요", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.setNegativeButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (Login.uRoll.substring(0, 3).equals("내담자")) {
                        AlertDialog.Builder starB = new AlertDialog.Builder(Chatting.this);
                        starB.setTitle("평가하기").setIcon(R.drawable.logo4);
                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        final View root = inflater.inflate(R.layout.digstar, null);
                        starB.setView(root);

                        starB.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });

                        starB.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RatingBar ratingBar = (RatingBar) root.findViewById(R.id.ratingBar);
                                final float rating = ratingBar.getRating();

                                if (rating == 0) {
                                    Toast.makeText(getApplicationContext(), "평가를 완료해주세요", Toast.LENGTH_SHORT).show();
                                } else {
                                    StringTokenizer tokenizer = new StringTokenizer(Login.uRoll, " ");
                                    int cnt = 0;
                                    while (tokenizer.hasMoreTokens()) {
                                        switch (cnt) {
                                            case 0:
                                                String str = tokenizer.nextToken();
                                                break;
                                            case 1:
                                                youNick = tokenizer.nextToken();
                                                break;
                                            case 2:
                                                youStar = tokenizer.nextToken();
                                                break;
                                            case 3:
                                                youState = tokenizer.nextToken();
                                                break;
                                            case 4:
                                                youHash1 = tokenizer.nextToken();
                                                break;
                                            case 5:
                                                youHash2 = tokenizer.nextToken();
                                                break;
                                            case 6:
                                                youHash3 = tokenizer.nextToken();
                                                break;
                                        }
                                        cnt++;
                                    }

                                    if (youStar.equals("0.0")) {
                                        youStar = String.valueOf(rating);
                                    } else {
                                        youStar = String.valueOf((Float.valueOf(youStar) + rating) / 2.0);
                                    }

                                    Login.uState = "o";
                                    Login.uRoll = "";

                                    point = String.valueOf(100);
                                    showPoint = "상담사 +100";

                                    Response.Listener<String> responseListener1 = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                boolean success = jsonObject.getBoolean("success");
                                                if (success) {
                                                    //Toast.makeText(getApplicationContext(), "포인트 추가 성공!!", Toast.LENGTH_SHORT).show();
                                                    Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject = new JSONObject(response);
                                                                boolean success = jsonObject.getBoolean("success");
                                                                if (success) {
                                                                    if (!Chat.youNick.equals("")) {
                                                                        Chat.youNick = "";
                                                                        Chat.youStar = "";
                                                                        Chat.youState = "";
                                                                        Chat.youHash1 = "";
                                                                        Chat.youHash2 = "";
                                                                        Chat.youHash3 = "";
                                                                    }
                                                                    if (!Main.youNick.equals("")) {
                                                                        Main.youNick = "";
                                                                        Main.youStar = "";
                                                                        Main.youState = "";
                                                                        Main.youHash1 = "";
                                                                        Main.youHash2 = "";
                                                                        Main.youHash3 = "";
                                                                    }
                                                                    if (!Mypage.youNick.equals("")) {
                                                                        Mypage.youNick = "";
                                                                        Mypage.youStar = "";
                                                                        Mypage.youState = "";
                                                                        Mypage.youHash1 = "";
                                                                        Mypage.youHash2 = "";
                                                                        Mypage.youHash3 = "";
                                                                    }

                                                                    //Toast.makeText(getApplicationContext(), "concat 성공!!", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    //Toast.makeText(getApplicationContext(), "concat 실패!!.", Toast.LENGTH_SHORT).show();
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    };
                                                    ShowPointRequest showPointRequest = new ShowPointRequest(showPoint, youNick, responseListener2);
                                                    RequestQueue queue2 = Volley.newRequestQueue(Chatting.this);
                                                    queue2.add(showPointRequest);

                                                } else {
                                                    //Toast.makeText(getApplicationContext(), "포인트 추가 실패!!.", Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    PointAddRequest pointAddRequest = new PointAddRequest(point, youNick, responseListener1);
                                    RequestQueue queue1 = Volley.newRequestQueue(Chatting.this);
                                    queue1.add(pointAddRequest);

                                    final String[] hashtag = new String[12];
                                    final String[] hash = {"고백", "권태기", "다툼", "데이트", "미련", "바람", "사과", "소개팅", "썸", "이별", "짝사랑", "첫사랑"};


                                    Response.Listener<String> responseListener2 = new Response.Listener<String>() {
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

                                                    for (int i = 0; i < 12; i++) {
                                                        if (hash[i].equals(youHash1) || hash[i].equals(youHash2) || hash[i].equals(youHash3)) {
                                                            hashtag[i] = String.valueOf(Integer.valueOf(hashtag[i]) + 1);
                                                        }
                                                    }


                                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject = new JSONObject(response);
                                                                boolean success = jsonObject.getBoolean("success");

                                                                if (success) { //수정 됨

                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    };

                                                    AutoSetRequest autoSetRequest = new AutoSetRequest(Login.uId, hashtag[0], hashtag[1], hashtag[2], hashtag[3], hashtag[4], hashtag[5], hashtag[6], hashtag[7], hashtag[8], hashtag[9], hashtag[10], hashtag[11], responseListener);
                                                    RequestQueue queue = Volley.newRequestQueue(Chatting.this);
                                                    queue.add(autoSetRequest);

                                                } else { //레코드등록 실패
                                                    Log.d("young", "출석한 적없는 사용자");
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    };
                                    //서버로 Volley를 이용해서 요청을 함
                                    AutoRequest autoRequest = new AutoRequest(Login.uId, responseListener2);
                                    RequestQueue queue2 = Volley.newRequestQueue(Chatting.this);
                                    queue2.add(autoRequest);


                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                boolean success = jsonObject.getBoolean("success");

                                                if (success) { //수정 됨
                                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                                        @Override
                                                        public void onResponse(String response) {
                                                            try {
                                                                JSONObject jsonObject = new JSONObject(response);
                                                                boolean success = jsonObject.getBoolean("success");

                                                                if (success) { //수정 됨
                                                                    Intent intent = new Intent(getApplicationContext(), Chat.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                    };

                                                    UpdateRequest nickChkRequest = new UpdateRequest(youNick, youStar, "o", youHash1, youHash2, youHash3, "", responseListener);
                                                    RequestQueue queue = Volley.newRequestQueue(Chatting.this);
                                                    queue.add(nickChkRequest);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };

                                    UpdateRequest nickChkRequest = new UpdateRequest(Login.uNick, Login.uStar, Login.uState, Login.uHash1, Login.uHash2, Login.uHash3, Login.uRoll, responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(Chatting.this);
                                    queue.add(nickChkRequest);
                                }
                            }
                        });

                        starB.show();
                    } else if (Login.uRoll.substring(0, 3).equals("상담사")) {
                        Login.uState = "o";
                        Login.uRoll = "";
                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");

                                    if (success) { //수정 됨
                                        Intent intent = new Intent(getApplicationContext(), Chat.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        };

                        UpdateRequest nickChkRequest = new UpdateRequest(Login.uNick, Login.uStar, Login.uState, Login.uHash1, Login.uHash2, Login.uHash3, Login.uRoll, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Chatting.this);
                        queue.add(nickChkRequest);
                    }
                }
            });
            builder.show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick(View view) {
        if (view == btnSend) {
            String msg = etMsg.getText().toString();

            SendToServerThread thread = new SendToServerThread(memberSocket, msg);
            thread.start();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//잠시 나감
            Intent intent = new Intent(getApplicationContext(), Chat.class);
            startActivity(intent);
            finish();
        }

        return false;
    }
}
