package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Setting extends AppCompatActivity implements View.OnClickListener {
    Switch swChat, swMsg, swGrow;
    Button btnLogout, btnEnd;
    View viewDigpw;
    EditText etPw;
    boolean isRunning;
    Socket memberSocket;
    String userId, userPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("알림 테스트");
            notificationManager.createNotificationChannel(notificationChannel);

        }

        swChat = (Switch) findViewById(R.id.swChat);
        swChat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        String Noti_Channel_ID = "Noti1";
                        String Noti_Channel_Group_ID = "Noti_Group1";

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        NotificationChannel notificationChannel = new NotificationChannel(Noti_Channel_ID, Noti_Channel_Group_ID, importance);

                        if (notificationManager.getNotificationChannel(Noti_Channel_ID) != null) {//채널존재
                        } else {//채널없어서 만듦
                            notificationManager.createNotificationChannel(notificationChannel);
                        }

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), Noti_Channel_ID)
                                .setLargeIcon(null).setSmallIcon(R.drawable.logo4)
                                .setWhen(System.currentTimeMillis()).setShowWhen(true).
                                        setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_MAX)
                                .setContentTitle("상담을 신청하였습니다");

                        notificationManager.notify(0, builder.build());
                    }
                }
            }
        });

        swMsg = (Switch) findViewById(R.id.swMsg);
        swMsg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        String Noti_Channel_ID = "Noti2";
                        String Noti_Channel_Group_ID = "Noti_Group2";

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        NotificationChannel notificationChannel = new NotificationChannel(Noti_Channel_ID, Noti_Channel_Group_ID, importance);

                        if (notificationManager.getNotificationChannel(Noti_Channel_ID) != null) {//채널존재
                        } else {//채널없어서 만듦
                            notificationManager.createNotificationChannel(notificationChannel);
                        }

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), Noti_Channel_ID)
                                .setLargeIcon(null).setSmallIcon(R.drawable.logo4)
                                .setWhen(System.currentTimeMillis()).setShowWhen(true).
                                        setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_MAX)
                                .setContentTitle("쪽지가 도착했습니다.");

                        notificationManager.notify(0, builder.build());
                    }
                }
            }
        });

        swGrow = (Switch) findViewById(R.id.swGrow);
        swGrow.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        int importance = NotificationManager.IMPORTANCE_HIGH;
                        String Noti_Channel_ID = "Noti3";
                        String Noti_Channel_Group_ID = "Noti_Group3";

                        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        NotificationChannel notificationChannel = new NotificationChannel(Noti_Channel_ID, Noti_Channel_Group_ID, importance);

                        if (notificationManager.getNotificationChannel(Noti_Channel_ID) != null) {//채널존재
                        } else {//채널없어서 만듦
                            notificationManager.createNotificationChannel(notificationChannel);
                        }

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), Noti_Channel_ID)
                                .setLargeIcon(null).setSmallIcon(R.drawable.logo4)
                                .setWhen(System.currentTimeMillis()).setShowWhen(true).
                                        setAutoCancel(true).setPriority(NotificationCompat.PRIORITY_MAX)
                                .setContentTitle("yamyam이가 시들고있어요");

                        notificationManager.notify(0, builder.build());
                    }
                }
            }
        });

        btnLogout = (Button) findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(this);
        btnEnd = (Button) findViewById(R.id.btnEnd);
        btnEnd.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view == btnLogout) {
            Toast.makeText(getApplicationContext(), "로그아웃을 하였습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else if (view == btnEnd) {
            AlertDialog.Builder digEnd = new AlertDialog.Builder(Setting.this);
            digEnd.setMessage("탈퇴를 하시겠습니까?");
            digEnd.setNegativeButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    viewDigpw = (View)View.inflate(Setting.this,R.layout.digpw,null);
                    etPw = (EditText)viewDigpw.findViewById(R.id.etPw);
                    etPw.setTransformationMethod(new PasswordTransformationMethod());

                    AlertDialog.Builder digPw = new AlertDialog.Builder(Setting.this);
                    digPw.setTitle("비밀번호를 입력하세요");
                    digPw.setView(viewDigpw);
                    digPw.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            userPw = etPw.getText().toString();
                            userId =((Login)Login.context).userId;

                            if (userPw.isEmpty()) {//서버에 접속
                                Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                            } else {
                                 EndThread thread = new EndThread();
                                 thread.start();
                            }
                        }
                    });
                    digPw.setPositiveButton("취소",null);
                    digPw.show();
                }
            });
            digEnd.setPositiveButton("아니요",null);
            digEnd.show();
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

    class EndThread extends Thread{
        public void run() {
            try {
                final Socket socket = new Socket(Login.url, 30000);
                memberSocket = socket;

                OutputStream os = socket.getOutputStream();
                DataOutputStream dos = new DataOutputStream(os);

                dos.writeUTF("End " + userId + " " + userPw);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isRunning = true;

                        EndCheckThread thread = new EndCheckThread(socket);
                        thread.start();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class EndCheckThread extends Thread {
        Socket socket;
        DataInputStream dis;

        public EndCheckThread(Socket socket) {
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
                            if (Integer.parseInt(msg) == 1) {
                                Toast.makeText(getApplicationContext(),"탈퇴되었습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                                finish();
                            }else if(Integer.parseInt(msg) == 2){
                                Toast.makeText(getApplicationContext(), "비밀번호가 일치하지않습니다.", Toast.LENGTH_SHORT).show();
                            }
                            else if (Integer.parseInt(msg) == 3) {//탈퇴실패
                                Toast.makeText(getApplicationContext(),"탈퇴하지 못하였습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
