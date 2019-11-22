package com.example.yamyam;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AttendSetRequest extends StringRequest {
    final static private String URL = "https://jsu3229.cafe24.com/dbeditor/AttendSet_yam.php";
    private Map<String, String> map;

    public AttendSetRequest(String userID, String userDate, String continuity, String mission, String missionChk, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("userID", userID);
        map.put("userDate",userDate);
        map.put("continuity",continuity);
        map.put("mission",mission);
        map.put("missionChk",missionChk);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}