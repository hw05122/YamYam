package com.example.yamyam;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    // 서버 URL 설정 (PHP 파일 연동)
    final static private String URL = "https://jsu3229.cafe24.com/dbeditor/Register_yam.php";
    private Map<String, String> map;

    public RegisterRequest(String userName, String userID, String userPassword, String userYear, String userMonth, String userDay, String userNickname, String userGender, String userStar, String userState, String userHashtag1, String userHashtag2, String userHashtag3, String userStop, String userFavorites, String userRoll, String userPoint,String userPointList, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("userName", userName);
        map.put("userID", userID);
        map.put("userPassword", userPassword);
        map.put("userYear", userYear);
        map.put("userMonth", userMonth);
        map.put("userDay", userDay);
        map.put("userNickname", userNickname);
        map.put("userGender", userGender);
        map.put("userStar", userStar);
        map.put("userState", userState);
        map.put("userHashtag1", userHashtag1);
        map.put("userHashtag2", userHashtag2);
        map.put("userHashtag3", userHashtag3);
        map.put("userStop", userStop);
        map.put("userFavorites", userFavorites);
        map.put("userRoll", userRoll);
        map.put("userPoint", userPoint);
        map.put("userPointList",userPointList);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
