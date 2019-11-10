package com.example.yamyam;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Message extends AppCompatActivity implements View.OnClickListener{
    private Button btnMenu, btnSendUser, btnSendAdmin, btnMsgDelete;
    private TextView tvMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        btnMenu = (Button)findViewById(R.id.btnMenu);
        btnMenu.setOnClickListener(this);

        tvMenu = (TextView)findViewById(R.id.tvMenu);

        btnMsgDelete = (Button)findViewById(R.id.btnMsgDelete);
        btnMsgDelete.setOnClickListener(this);

        btnSendUser = (Button)findViewById(R.id.btnSendUser);
        btnSendUser.setOnClickListener(this);

        btnSendAdmin = (Button)findViewById(R.id.btnSendAdmin);
        btnSendAdmin.setOnClickListener(this);
    }

    public void acceptShow(){

    }

    public void sendShow(){

    }

    public void onClick(View view) {
        if(view == btnMenu){
            PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
            MenuInflater inflater = popupMenu.getMenuInflater();
            Menu menu = popupMenu.getMenu();

            inflater.inflate(R.menu.msgtype,menu);

            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.itemAccept:
                            tvMenu.setText("받은쪽지함");
                            break;
                        case R.id.itemSend:
                            tvMenu.setText("보낸쪽지함");
                            break;
                    }
                    return false;
                }
            });

            popupMenu.show();
        }else if(view == btnMsgDelete){

        }else if(view == btnSendAdmin){
            AlertDialog.Builder digAdminMsg = new AlertDialog.Builder(Message.this);
            digAdminMsg.setTitle("관리자에게 쪽지보내기");
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View root = inflater.inflate(R.layout.digadminmsg, null);
            digAdminMsg.setView(root);

            final LinearLayout llR = (LinearLayout)root.findViewById(R.id.llR);
            final LinearLayout llQ = (LinearLayout)root.findViewById(R.id.llQ);
            final EditText etRContent = (EditText)root.findViewById(R.id.etRContent);
            final EditText etQContent = (EditText)root.findViewById(R.id.etQContent);
            RadioGroup rg = (RadioGroup) root.findViewById(R.id.rg);
            rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    if (i == R.id.rbR) {
                        llR.setVisibility(View.VISIBLE);
                        etRContent.setText("");
                        llQ.setVisibility(View.INVISIBLE);
                    } else if (i == R.id.rbQ) {
                        llR.setVisibility(View.INVISIBLE);
                        llQ.setVisibility(View.VISIBLE);
                        etQContent.setText("");
                    }
                }
            });

            digAdminMsg.setNegativeButton("보내기", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(true){//어떻게 하면 조건 확인하고 닫게할까
                        Toast.makeText(getApplicationContext(),"쪽지를 보냈습니다.",Toast.LENGTH_SHORT).show();

                    }
                    else if(false){
                        Toast.makeText(getApplicationContext(),"다시 확인해주세요",Toast.LENGTH_SHORT).show();
                    }
                }
            });

            digAdminMsg.setPositiveButton("취소",null);
            digAdminMsg.show();
        }else if(view == btnSendUser){
            AlertDialog.Builder digUserMsg = new AlertDialog.Builder(Message.this);
            digUserMsg.setTitle("사용자에게 쪽지보내기");
            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View root = inflater.inflate(R.layout.digusermsg, null);
            digUserMsg.setView(root);

            EditText etNick = (EditText)root.findViewById(R.id.etNick);
            Button btnNickChk = (Button)root.findViewById(R.id.btnNickChk);
            btnNickChk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(true){//어떻게 하면 조건 확인하고 닫게할까
                        Toast.makeText(getApplicationContext(),"일치하는 닉네임이 있습니다.",Toast.LENGTH_SHORT).show();

                    }
                    else if(false){
                        Toast.makeText(getApplicationContext(),"일치하는 닉네임이 없습니다.",Toast.LENGTH_SHORT).show();
                    }
                }
            });

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
