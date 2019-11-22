package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Chat extends AppCompatActivity {
    private ImageButton btnChatting, btnAuto, btnMy, btnList;
    private Button btnCurrent, btnHash, btnName, btnOption;
    public static ArrayList<User> userList = new ArrayList<>();
    public ArrayList<User> saveList = new ArrayList<>();
    private int genChk = 0;
    private AutoCompleteTextView autoTv;
    private ListView listView;
    private UserListAdapter adapter;
    public static String userNickname, userGender, userStar, userState, userHashtag1, userHashtag2, userHashtag3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        setTitle("상담하기");

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

        new BackgroundTask("https://jsu3229.cafe24.com/dbeditor/List_userStar.php").execute();
    }

    public void onClick(View view) {

        if (view == btnCurrent) {//현재순위
            new BackgroundTask("https://jsu3229.cafe24.com/dbeditor/List_userStar.php").execute();
        } else if (view == btnHash) {//해시태그
            new BackgroundTask("https://jsu3229.cafe24.com/dbeditor/List_userHash.php").execute();
        } else if (view == btnName) {//닉네임순
            new BackgroundTask("https://jsu3229.cafe24.com/dbeditor/List.php").execute();
        } else if (view == btnOption) {
            AlertDialog.Builder starB = new AlertDialog.Builder(Chat.this);
            starB.setTitle("조건검색").setIcon(R.drawable.logo4);
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            final View root = inflater.inflate(R.layout.digoption, null);
            starB.setView(root);

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
                        new BackgroundTask("https://jsu3229.cafe24.com/dbeditor/List_Man.php").execute();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    } else if (genChk == 2) {//여
                        new BackgroundTask("https://jsu3229.cafe24.com/dbeditor/List_Woman.php").execute();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    }

                    RatingBar ratingBar = (RatingBar) root.findViewById(R.id.ratingBar);
                    float rating = ratingBar.getRating();
                    if(rating == 1){
                        new BackgroundTask("https://jsu3229.cafe24.com/dbeditor/List_Star1.php").execute();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    }else if(rating == 2){
                        new BackgroundTask("https://jsu3229.cafe24.com/dbeditor/List_Star2.php").execute();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    }else if(rating == 3){
                        new BackgroundTask("https://jsu3229.cafe24.com/dbeditor/List_Star3.php").execute();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    }else if(rating == 4){
                        new BackgroundTask("https://jsu3229.cafe24.com/dbeditor/List_Star4.php").execute();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    }else if(rating == 5){
                        new BackgroundTask("https://jsu3229.cafe24.com/dbeditor/List_Star5.php").execute();
                        Toast.makeText(getApplicationContext(), "조건검색을 하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            starB.setPositiveButton("취소", null);
            starB.setCancelable(false);

            starB.show();
        } else if (view == btnChatting) {
            Intent intent = new Intent(getApplicationContext(), Chatting.class);
            startActivity(intent);
            finish();
        } else if (view == btnAuto) {
            btnList.setBackgroundResource(R.color.colorPrimary);
            btnAuto.setBackgroundColor(Color.LTGRAY);
            AlertDialog.Builder starB = new AlertDialog.Builder(Chat.this);
            starB.setTitle("        자동매칭 중 입니다").setIcon(R.drawable.logo4);
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View root = inflater.inflate(R.layout.digauto, null);
            starB.setView(root);
            starB.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    btnAuto.setBackgroundResource(R.color.colorPrimary);
                    btnList.setBackgroundColor(Color.LTGRAY);
                }
            });
            starB.setCancelable(false);
            starB.show();
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

        public BackgroundTask(String target){
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

                    User user = new User(userNickname, userGender, userStar, userState, userHashtag1, userHashtag2, userHashtag3);
                    userList.add(user);//리스트뷰에 값을 추가해줍니다
                    saveList.add(user);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
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
                                    if (true) {//상담방 없는 경우
                                        Intent intent = new Intent(getApplicationContext(), Chatting.class);
                                        startActivity(intent);
                                        finish();
                                    } else {

                                    }
                                }
                            });

                            final TextView tvNick = (TextView) viewProfile.findViewById(R.id.tvNick);
                            ImageButton ibMsg = (ImageButton) viewProfile.findViewById(R.id.btnMsg);
                            ibMsg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AlertDialog.Builder digUserMsg = new AlertDialog.Builder(Chat.this);
                                    digUserMsg.setTitle("사용자에게 쪽지보내기");
                                    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                                    View root = inflater.inflate(R.layout.digusermsg, null);
                                    digUserMsg.setView(root);

                                    EditText etNick = (EditText) root.findViewById(R.id.etNick);
                                    etNick.setText(tvNick.getText().toString());
                                    Button btnNickChk = (Button) root.findViewById(R.id.btnNickChk);
                                    btnNickChk.setVisibility(View.INVISIBLE);

                                    EditText etUerContent = (EditText) root.findViewById(R.id.etUserContent);
                                    etUerContent.setText("");

                                    digUserMsg.setNegativeButton("보내기", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            if (true) {
                                                Toast.makeText(getApplicationContext(), "쪽지를 보냈습니다.", Toast.LENGTH_SHORT).show();

                                            } else if (false) {
                                                Toast.makeText(getApplicationContext(), "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    digUserMsg.setPositiveButton("취소", null);
                                    digUserMsg.show();
                                }
                            });

                            final ImageButton ibStar = (ImageButton) viewProfile.findViewById(R.id.ibStar);
                            ibStar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (ibStar.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.ic_star_empty).getConstantState())) {//빈 별
                                        Toast.makeText(getApplicationContext(), "즐겨찾기추가", Toast.LENGTH_SHORT).show();
                                        ibStar.setImageResource(R.drawable.ic_star_full);
                                    } else {
                                        Toast.makeText(getApplicationContext(), "즐겨찾기해제", Toast.LENGTH_SHORT).show();
                                        ibStar.setImageResource(R.drawable.ic_star_empty);
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
}

