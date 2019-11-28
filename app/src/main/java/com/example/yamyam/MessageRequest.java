package com.example.yamyam;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MessageRequest extends StringRequest {

    // 서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "https://jsu3229.cafe24.com/dbeditor/Message_yam.php";
    private Map<String, String> map;

    public MessageRequest(String userFrom, String userTo, String Text, String userAccusation, String userInquiry, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("userFrom", userFrom);
        map.put("userTo", userTo);
        map.put("Text",Text);
        map.put("userAccusation",userAccusation);
        map.put("userInquiry",userInquiry);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
