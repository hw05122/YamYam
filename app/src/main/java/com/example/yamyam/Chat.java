package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Chat extends AppCompatActivity {
    private ImageButton btnChatting,btnAuto,btnMy;
    private Button btnCurrent, btnTotal, btnNew, btnSearch, btnOption;
    public ArrayList<User> userList = new ArrayList<>();
    private int genChk = 0;
    private AutoCompleteTextView autoTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle("상담하기");

        btnCurrent = (Button)findViewById(R.id.btnCurrent);
        btnTotal = (Button)findViewById(R.id.btnTotal);
        btnNew = (Button)findViewById(R.id.btnNew);
        btnSearch = (Button)findViewById(R.id.btnSearch);
        btnOption = (Button)findViewById(R.id.btnOption);
        btnChatting = (ImageButton) findViewById(R.id.btnChatting);
        btnAuto = (ImageButton)findViewById(R.id.btnAuto);
        btnMy = (ImageButton)findViewById(R.id.btnMy);

        String[] arrWords = new String[]{"가방","가구","가나다라","나비","다람쥐","young","yolo"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arrWords);
        autoTv = (AutoCompleteTextView)findViewById(R.id.autoTv);
        autoTv.setAdapter(arrayAdapter);

        userListShow();
    }

    public void userListShow(){
        LinearLayout llUserList = (LinearLayout)View.inflate(Chat.this,R.layout.userlist,null);
        LinearLayout llList = (LinearLayout)findViewById(R.id.llList);
        llList.addView(llUserList);
        llList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View viewProfile = (View)View.inflate(Chat.this,R.layout.profile,null);
                AlertDialog.Builder digProfile = new AlertDialog.Builder(Chat.this);
                digProfile.setView(viewProfile).setTitle("프로필");

                ImageButton ibChatting = (ImageButton)viewProfile.findViewById(R.id.btnChatting);
                ibChatting.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(true){//상담방 없는 경우
                            Intent intent = new Intent(getApplicationContext(),Chatting.class);
                            startActivity(intent);
                            finish();
                        }else{

                        }
                    }
                });

                final TextView tvNick = (TextView)viewProfile.findViewById(R.id.tvNick);
                ImageButton ibMsg = (ImageButton)viewProfile.findViewById(R.id.btnMsg);
                ibMsg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder digUserMsg = new AlertDialog.Builder(Chat.this);
                        digUserMsg.setTitle("사용자에게 쪽지보내기");
                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View root = inflater.inflate(R.layout.digusermsg, null);
                        digUserMsg.setView(root);

                        EditText etNick = (EditText)root.findViewById(R.id.etNick);
                        etNick.setText(tvNick.getText().toString());
                        Button btnNickChk = (Button)root.findViewById(R.id.btnNickChk);
                        btnNickChk.setVisibility(View.INVISIBLE);

                        EditText etUerContent = (EditText)root.findViewById(R.id.etUserContent);
                        etUerContent.setText("");

                        digUserMsg.setNegativeButton("보내기", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(true){
                                    Toast.makeText(getApplicationContext(),"쪽지를 보냈습니다.",Toast.LENGTH_SHORT).show();

                                }
                                else if(false){
                                    Toast.makeText(getApplicationContext(),"내용을 입력하세요",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        digUserMsg.setPositiveButton("취소",null);
                        digUserMsg.show();
                    }
                });

                final ImageButton ibStar = (ImageButton)viewProfile.findViewById(R.id.ibStar);
                ibStar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(ibStar.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.ic_star_empty).getConstantState())) {//빈 별
                            Toast.makeText(getApplicationContext(), "즐겨찾기추가", Toast.LENGTH_SHORT).show();
                            ibStar.setImageResource(R.drawable.ic_star_full);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "즐겨찾기제외", Toast.LENGTH_SHORT).show();
                            ibStar.setImageResource(R.drawable.ic_star_empty);
                        }
                    }
                });

                digProfile.setNegativeButton("닫기",null);
                digProfile.show();
            }
        });
    }

    public void onClick(View view) {

        if(view == btnCurrent){

        }
        else if(view == btnTotal){

        }
        else if(view == btnNew){

        }
        else if(view == btnSearch){
            final String uNick = autoTv.getText().toString();

            if (uNick.isEmpty()) {
                Toast.makeText(getApplicationContext(), "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();

            } else {
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) { //사용자 있음
                                LinearLayout llUserList = (LinearLayout)View.inflate(Chat.this,R.layout.userlist,null);
                                LinearLayout llList = (LinearLayout)findViewById(R.id.llList);

                                TextView nick = (TextView)llUserList.findViewById(R.id.tvNick);
                                nick.setText(uNick);
                                llList.addView(llUserList);
                                llList.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        View viewProfile = (View)View.inflate(Chat.this,R.layout.profile,null);
                                        AlertDialog.Builder digProfile = new AlertDialog.Builder(Chat.this);
                                        digProfile.setView(viewProfile).setTitle("프로필");

                                        ImageButton ibChatting = (ImageButton)viewProfile.findViewById(R.id.btnChatting);
                                        ibChatting.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if(true){//상담방 없는 경우
                                                    Intent intent = new Intent(getApplicationContext(),Chatting.class);
                                                    startActivity(intent);
                                                    finish();
                                                }else{

                                                }
                                            }
                                        });

                                        final TextView tvNick = (TextView)viewProfile.findViewById(R.id.tvNick);
                                        tvNick.setText(uNick);

                                        ImageButton ibMsg = (ImageButton)viewProfile.findViewById(R.id.btnMsg);
                                        ibMsg.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                AlertDialog.Builder digUserMsg = new AlertDialog.Builder(Chat.this);
                                                digUserMsg.setTitle("사용자에게 쪽지보내기");
                                                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                                View root = inflater.inflate(R.layout.digusermsg, null);
                                                digUserMsg.setView(root);

                                                EditText etNick = (EditText)root.findViewById(R.id.etNick);
                                                etNick.setText(tvNick.getText().toString());
                                                Button btnNickChk = (Button)root.findViewById(R.id.btnNickChk);
                                                btnNickChk.setVisibility(View.INVISIBLE);

                                                EditText etUerContent = (EditText)root.findViewById(R.id.etUserContent);
                                                etUerContent.setText("");

                                                digUserMsg.setNegativeButton("보내기", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        if(true){
                                                            Toast.makeText(getApplicationContext(),"쪽지를 보냈습니다.",Toast.LENGTH_SHORT).show();

                                                        }
                                                        else if(false){
                                                            Toast.makeText(getApplicationContext(),"내용을 입력하세요",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });

                                                digUserMsg.setPositiveButton("취소",null);
                                                digUserMsg.show();
                                            }
                                        });

                                        final ImageButton ibStar = (ImageButton)viewProfile.findViewById(R.id.ibStar);
                                        ibStar.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if(ibStar.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.ic_star_empty).getConstantState())) {//빈 별
                                                    Toast.makeText(getApplicationContext(), "즐겨찾기추가", Toast.LENGTH_SHORT).show();
                                                    ibStar.setImageResource(R.drawable.ic_star_full);
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(), "즐겨찾기제외", Toast.LENGTH_SHORT).show();
                                                    ibStar.setImageResource(R.drawable.ic_star_empty);
                                                }
                                            }
                                        });

                                        digProfile.setNegativeButton("닫기",null);
                                        digProfile.show();
                                    }
                                });
                            } else { //사용자 없음
                                Toast.makeText(getApplicationContext(), "일치하는 사용자가 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                NickChkRequest nickChkRequest = new NickChkRequest(uNick, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Chat.this);
                queue.add(nickChkRequest);
            }
        }
        else if(view == btnOption){
            AlertDialog.Builder starB = new AlertDialog.Builder(Chat.this);
            starB.setTitle("조건검색").setIcon(R.drawable.logo4);
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View root = inflater.inflate(R.layout.digoption, null);
            starB.setView(root);

            RadioGroup rgState = (RadioGroup)root.findViewById(R.id.rgGen);
            rgState.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId == R.id.rbM){
                        genChk = 1;
                    }
                    else if(checkedId == R.id.rbW){
                        genChk = 2;
                    }
                }
            });
            starB.setNegativeButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(genChk == 1){//남

                    }
                    else if(genChk == 2){//여

                    }
                    Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                }
            });
            starB.setPositiveButton("취소",null);
            starB.setCancelable(false);

            starB.show();
        }
        else if(view == btnChatting){
            Intent intent = new Intent(getApplicationContext(),Chatting.class);
            startActivity(intent);
            finish();
        }
        else if(view == btnAuto){
            AlertDialog.Builder starB = new AlertDialog.Builder(Chat.this);
            starB.setTitle("        자동매칭 중 입니다").setIcon(R.drawable.logo4);
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View root = inflater.inflate(R.layout.digauto, null);
            starB.setView(root);
            starB.setNegativeButton("닫기",null);
            starB.setCancelable(false);

            starB.show();
        }
        else if(view == btnMy){
            Intent intent = new Intent(getApplicationContext(),Mypage.class);
            startActivity(intent);
            finish();
        }
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
}

class User{
    private String name;
    private String id;
    private String year;
    private String nickname;
    private String gen;

    String getName(){
        return name;
    }

    String getId(){
        return id;
    }

    String getYear(){
        return year;
    }

    String getNickname(){
        return nickname;
    }

    String getGen(){
        return gen;
    }
}
