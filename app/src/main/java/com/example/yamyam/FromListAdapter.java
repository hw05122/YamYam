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

public class FromListAdapter extends BaseAdapter {

    private Context context;
    private List<Msmg> mguserList;
    private List<Msmg> mgsaveList;

    public FromListAdapter(Context context, List<Msmg> mguserList, List<Msmg> mgsaveList){
        this.context = context;
        this.mguserList = mguserList;
        this.mgsaveList = mgsaveList;

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
        View v = View.inflate(context, R.layout.message_from, null);
        //뷰에 다음 컴포넌트들을 연결시켜줌

        TextView userFrom = (TextView)v.findViewById(R.id.tvFromtv);

        TextView text_from = (TextView)v.findViewById(R.id.tvText_fromtv);

        userFrom.setText(mguserList.get(i).getuserFrom());

        text_from.setText(mguserList.get(i).getText());



        //만든뷰를 반환함
        return v;
    }

}