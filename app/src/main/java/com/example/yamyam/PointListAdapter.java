package com.example.yamyam;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kch on 2018. 2. 17..
 */

public class PointListAdapter extends BaseAdapter {

    private Context context;
    private List<PointList> pointList;
//    private List<User> saveList;

    public PointListAdapter(Context context, List<PointList> pointList){
        this.context = context;
        this.pointList = pointList;
//        this.saveList = saveList;
    }

    //출력할 총갯수를 설정하는 메소드
    @Override
    public int getCount() {
        return pointList.size();
    }

    //특정한 유저를 반환하는 메소드
    @Override
    public Object getItem(int i) {
        return pointList.get(i);
    }

    //아이템별 아이디를 반환하는 메소드
    @Override
    public long getItemId(int i) {
        return i;
    }

    //가장 중요한 부분
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.pointlist, null);
        //뷰에 다음 컴포넌트들을 연결시켜줌
//        TextView userNickname = (TextView)v.findViewById(R.id.getPointDay);
        TextView pointGetResult1 = (TextView)v.findViewById(R.id.pointGetResult1);
        TextView pointGetResult2 = (TextView)v.findViewById(R.id.pointGetResult2);
//        TextView getPointDay = (TextView)v.findViewById(R.id.getPoint);
//        ImageView userState = (ImageView) v.findViewById(R.id.userState);
//        TextView userHashtag1 = (TextView)v.findViewById(R.id.userHashtag1);
//        TextView userHashtag2 = (TextView)v.findViewById(R.id.userHashtag2);
//        TextView userHashtag3 = (TextView)v.findViewById(R.id.userHashtag3);

//        userNickname.setText(pointList.get(i).getUserNickname());
        pointGetResult1.setText(pointList.get(i).getUserPointResult1());  //포인트 얻은 이유
        pointGetResult2.setText(pointList.get(i).getUserPointResult2());  //포인트 얻은 이유
//        getPointDay.setText(pointList.get(i).getPoint());  //포인트 현황
//        if (userList.get(i).getUserState().equals("o")) {
//            if (userList.get(i).userGender.equals("M")) {
//                userState.setImageResource(R.drawable.man);
//            } else if (userList.get(i).userGender.equals("F")) {
//                userState.setImageResource(R.drawable.woman);
//            }
//        } else if (userList.get(i).getUserState().equals("x")) {
//            userState.setImageResource(R.drawable.ic_close);
//        } else if (userList.get(i).getUserState().equals("h")) {
//            userState.setImageResource(R.drawable.ic_heart);
//        }
//        userHashtag1.setText("#"+userList.get(i).getUserHashtag1());
//        userHashtag2.setText("#"+userList.get(i).getUserHashtag2());
//        userHashtag3.setText("#"+userList.get(i).getUserHashtag3());

        //이렇게하면 findViewWithTag를 쓸 수 있음 없어도 되는 문장임
        v.setTag(pointList.get(i).getUserNickname());

        //만든뷰를 반환함
        return v;
    }

}