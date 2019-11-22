package com.example.yamyam;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

public class Attendance extends AppCompatActivity {
    private TextView tvDate;
    private GridAdapter gridAdapter;
    private ArrayList<String> dayList;
    private GridView gridView;
    private Calendar mCal;
    ViewHolder holder = null, holder2 = null;
    String posit, sToday;
    TextView txtMission;
    public static String TODAY, userDate, mission, day;
    private int continuityCnt;
    private boolean todayGetMission;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_attendance);
        setTitle("출석하기");

        txtMission = (TextView) findViewById(R.id.txtMission);
        tvDate = (TextView) findViewById(R.id.tv_date);
        gridView = (GridView) findViewById(R.id.gridview);

        long now = System.currentTimeMillis();
        final Date date = new Date(now);

        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);
        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);
        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);

        continuityCnt = 1;
        String beforDate = "";
        if (Main.dateStr[0] != null) {
            beforDate = Main.dateStr[0];
            int c = 1;
            for (; Main.dateStr[c] != null; c++) {
                if (Integer.parseInt(beforDate) + 1 == Integer.parseInt(Main.dateStr[c])) {
                    continuityCnt++;
                }
                Log.d("young", beforDate + " " + Main.dateStr[c] + " " + continuityCnt);
                beforDate = Main.dateStr[c];
            }

            if (Integer.parseInt(Main.dateStr[c - 1]) + 1 == Integer.parseInt(curDayFormat.format(date))) {
                continuityCnt++;
                Log.d("young", Main.dateStr[c - 1] + " " + curDayFormat.format(date) + " " + continuityCnt);
            }
        }

        TODAY = curYearFormat.format(date) + curMonthFormat.format(date) + curDayFormat.format(date);
        tvDate.setText(curYearFormat.format(date) + "/" + curMonthFormat.format(date));

        day = curDayFormat.format(date);
        getMission();

        dayList = new ArrayList<String>();
        dayList.add("일");
        dayList.add("월");
        dayList.add("화");
        dayList.add("수");
        dayList.add("목");
        dayList.add("금");
        dayList.add("토");

        mCal = Calendar.getInstance();

        mCal.set(Integer.parseInt(curYearFormat.format(date)), Integer.parseInt(curMonthFormat.format(date)) - 1, 1);

        int dayNum = mCal.get(Calendar.DAY_OF_WEEK);
        for (int i = 1; i < dayNum; i++) {
            dayList.add("");
        }
        setCalendarDate(mCal.get(Calendar.MONTH) + 1);
        gridAdapter = new GridAdapter(getApplicationContext(), dayList);
        gridView.setAdapter(gridAdapter);

    }

    public void getMission() {
        if (day.substring(0, 1).equals("0")) {
            day = day.substring(1, 2);
        }

        todayGetMission = false;
        for (int i = 0; Main.dateStr[i] != null; i++) {
            if (Main.dateStr[i].equals(day)) {
                txtMission.setText(Main.mission);
                todayGetMission = true;
            }
        }
        if (todayGetMission) {
            txtMission.setText(Main.mission);
        } else {
            String[] missions = {"상담 1회 하기", "상담해주고 평가 4점이상 받기", "쪽지보내기"};
            int rand = (int) (Math.random() * 3);
            mission = missions[rand];
        }
    }

    private void setCalendarDate(int month) {
        mCal.set(Calendar.MONTH, month - 1);
        for (int i = 0; i < mCal.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dayList.add("" + (i + 1));
        }
    }

    private class GridAdapter extends BaseAdapter {
        private final List<String> list;
        private final LayoutInflater inflater;

        public GridAdapter(Context context, List<String> list) {
            this.list = list;
            this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_calendar_gridview, parent, false);
                holder = new ViewHolder();

                holder.tvItemGridView = (TextView) convertView.findViewById(R.id.tv_item_gridview);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.tvItemGridView.setText(getItem(position));
            holder.tvItemGridView.setGravity(Gravity.CENTER);
            int gridviewW = gridView.getWidth() / 7;
            holder.tvItemGridView.setWidth(gridviewW);

            mCal = Calendar.getInstance();
            Integer today = mCal.get(Calendar.DAY_OF_MONTH);
            sToday = String.valueOf(today);

            if (sToday.equals(getItem(position))) {
                posit = getItem(position);
                holder.tvItemGridView.setTextColor(getResources().getColor(R.color.colorDeeppink));
                holder2 = holder;
            }

            for (int i = 0; Main.dateStr[i] != null; i++) {
                if (Main.dateStr[i].equals(getItem(position))) {
                    holder.tvItemGridView.setBackgroundResource(R.drawable.ic_favorite_border);
                    holder2 = holder;
                }
            }

            return convertView;
        }
    }

    private class ViewHolder {
        TextView tvItemGridView;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.check, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.itemCheck) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        boolean todayChk = false;

                        if (success) { //이때까지 출석한 적있음
                            String uId = jsonObject.getString("userID");
                            String date = jsonObject.getString("userDate");
                            String continuity = jsonObject.getString("continuity");
                            String userMission = jsonObject.getString("mission");
                            String userMissionChk = jsonObject.getString("missionChk");
                            Log.d("young", uId + " " + date + " " + continuity + " " + userMission + " " + userMissionChk);

                            StringTokenizer tokenizer = new StringTokenizer(date, " ");
                            while (tokenizer.hasMoreTokens()) {
                                String str = tokenizer.nextToken();
                                if (str.equals(TODAY)) {
                                    Toast.makeText(getApplicationContext(), "이미 출석하였습니다.", Toast.LENGTH_SHORT).show();
                                    todayChk = true;
                                } else {

                                }
                            }

                            if (!todayChk) {
                                date += " " + TODAY;
                                userDate = date;

                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean success = jsonObject.getBoolean("success");
                                            if (success) { //레코드등록 성공
                                                Toast.makeText(getApplicationContext(), "출석되었습니다", Toast.LENGTH_SHORT).show();
                                                holder2.tvItemGridView.setBackgroundResource(R.drawable.ic_favorite_border);
                                                txtMission.setText(mission);
                                            } else { //레코드등록 실패
                                                Toast.makeText(getApplicationContext(), "출석에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                };
                                //서버로 Volley를 이용해서 요청을 함
                                AttendSetRequest attendSetRequest = new AttendSetRequest(Login.uId, userDate, String.valueOf(continuityCnt), mission, "x", responseListener);
                                RequestQueue queue = Volley.newRequestQueue(Attendance.this);
                                queue.add(attendSetRequest);
                            }

                        } else { //이때까지 출석한 적없음
                            userDate = TODAY;

                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");
                                        if (success) { //레코드등록 성공
                                            Toast.makeText(getApplicationContext(), "출석되었습니다", Toast.LENGTH_SHORT).show();
                                            holder2.tvItemGridView.setBackgroundResource(R.drawable.ic_favorite_border);
                                            txtMission.setText(mission);
                                        } else { //레코드등록 실패
                                            Toast.makeText(getApplicationContext(), "출석에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            };

                            //서버로 Volley를 이용해서 요청을 함
                            AttendAddRequest attendAddRequest = new AttendAddRequest(Login.uId, userDate, "1", mission, "x", responseListener);
                            RequestQueue queue = Volley.newRequestQueue(Attendance.this);
                            queue.add(attendAddRequest);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };

            AttendChkRequest attendChkRequest = new AttendChkRequest(Login.uId, responseListener);
            RequestQueue queue = Volley.newRequestQueue(Attendance.this);
            queue.add(attendChkRequest);
        }

        return super.onOptionsItemSelected(item);
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
}
