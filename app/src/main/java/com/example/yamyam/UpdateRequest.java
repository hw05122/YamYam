package com.example.yamyam;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class UpdateRequest extends StringRequest {

    // 서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "https://jsu3229.cafe24.com/dbeditor/Update_yam.php";
    private Map<String, String> map;

    public UpdateRequest(String userNickname, String userStar, String userState, String userHashtag1, String userHashtag2, String userHashtag3, String userRoll, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("userNickname", userNickname);
        map.put("userStar", userStar);
        map.put("userState", userState);
        map.put("userHashtag1", userHashtag1);
        map.put("userHashtag2", userHashtag2);
        map.put("userHashtag3", userHashtag3);
        map.put("userRoll",userRoll);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
