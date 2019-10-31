package com.example.yamyam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

public class Chatting extends AppCompatActivity implements View.OnClickListener {
    EditText etMsg;
    Button btnSend;
    LinearLayout ll;
    ScrollView sv;
    AlertDialog starD;

    boolean isConnect = false, isRunning = false;
    Socket memberSocket;
    String userNick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        etMsg = (EditText) findViewById(R.id.etMsg);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);
        sv = (ScrollView) findViewById(R.id.sv);
        ll = (LinearLayout) findViewById(R.id.ll);

        AlertDialog.Builder builder = new AlertDialog.Builder(Chatting.this);
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
                ConnectionThread thread = new ConnectionThread();
                thread.start();
            }
        });
        builder.show();
    }

    public void onClick(View view) {
        if (view == btnSend) {
            String msg = etMsg.getText().toString();

            SendToServerThread thread = new SendToServerThread(memberSocket, msg);
            thread.start();
        }
    }

    class ConnectionThread extends Thread {
        public void run() {
            try {
                final Socket socket = new Socket(Login.url, 30000);
                memberSocket = socket;

                String nickName = Login.etId.getText().toString();
                userNick = nickName;

                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);

                dos.writeUTF("Chatting " + userNick);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        etMsg.setText("");
                        etMsg.setHint("메세지 입력");
                        btnSend.setText("전송");
                        isConnect = true;
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

                            StringTokenizer token =new StringTokenizer(msg, " ");
                            while(token.hasMoreTokens()){
                                String nick = token.nextToken();
                                if(nick.equals(userNick)){
                                    tvMsg.setBackgroundColor(Color.YELLOW);
                                    tvMsg.setTextSize(20);
                                    llMsg.setGravity(Gravity.RIGHT);

                                    int length = nick.length();
                                    tvMsg.setText(msg.substring(length));
                                    break;
                                }else {
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
                    AlertDialog.Builder starB = new AlertDialog.Builder(Chatting.this);
                    starB.setTitle("평가하기").setIcon(R.drawable.logo4);
                    LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
                    View root = inflater.inflate(R.layout.star,null);
                    starB.setView(root);

                    starB.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    starB.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final TextView tvResult = (TextView)findViewById(R.id.tvResult);
                            RatingBar ratingBar = (RatingBar)findViewById(R.id.ratingBar);
                            ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                                @Override
                                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                                    tvResult.setText(String.valueOf(rating));
                                }
                            });

                            if(tvResult.getText().toString().equals("0")){
                                Toast.makeText(getApplicationContext(),"평가를 완료해주세요",Toast.LENGTH_SHORT).show();
                            }else{
                                Intent intent = new Intent(getApplicationContext(),Chat.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });

                    starD = starB.create();
                    starD.show();
                }
            });
            builder.show();
        }

        return super.onOptionsItemSelected(item);
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
