package com.example.yamyam;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class IdChkRequest extends StringRequest {
    final static private String URL = "https://jsu3229.cafe24.com/dbeditor/IdChk_yam.php";
    private Map<String, String> map;

    public IdChkRequest(String userID, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("userID", userID);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
