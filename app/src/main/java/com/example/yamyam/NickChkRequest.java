package com.example.yamyam;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class NickChkRequest extends StringRequest {
    final static private String URL = "https://jsu3229.cafe24.com/dbeditor/NickChk_yam.php";
    private Map<String, String> map;

    public NickChkRequest(String userNickname, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("userNickname", userNickname);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}