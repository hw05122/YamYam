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

import java.util.List;

/**
 * Created by kch on 2018. 2. 17..
 */

public class ToListAdapter extends BaseAdapter {

    private Context context;
    private List<Msmg> mguserList;
    private List<Msmg> mgusaveList;

    public ToListAdapter(Context context, List<Msmg> mguserList, List<Msmg>mgsaveList){
        this.context = context;
        this.mguserList = mguserList;
        this.mgusaveList = mgsaveList;
    }

    //출력할 총갯수를 설정하는 메소드
    @Override
    public int getCount() {
        return mguserList.size();
    }

    //특정한 유저를 반환하는 메소드
    @Override
    public Object getItem(int i) {
        return mguserList.get(i);
    }

    //아이템별 아이디를 반환하는 메소드
    @Override
    public long getItemId(int i) {
        return i;
    }

    //가장 중요한 부분
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = View.inflate(context, R.layout.message_to, null);
        //뷰에 다음 컴포넌트들을 연결시켜줌


        TextView userTo = (TextView)v.findViewById(R.id.tvTotv);

        TextView text_to = (TextView)v.findViewById(R.id.tvText_totv);

        userTo.setText(mguserList.get(i).getuserTo());

        text_to.setText(mguserList.get(i).getText());




        //만든뷰를 반환함
        return v;
    }

}