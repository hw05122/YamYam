package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Attendance extends AppCompatActivity implements View.OnClickListener{
    Button btnCheck;
    TextView txtMission;
    int chk = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        btnCheck = (Button)findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(this);

        txtMission = (TextView)findViewById(R.id.txtMission);
    }

    public void onClick(View view) {
        if(view == btnCheck){
            if(chk == 0){
                String[] mission = {"상담 1회 하기","평가 4점이상 받기","쪽지보내기"};
                int rand = (int)(Math.random()*3);

                txtMission.setText(mission[rand]);
                chk = 1;
            }
            else{
                Toast.makeText(getApplicationContext(),"이미 출석하였습니다.",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
