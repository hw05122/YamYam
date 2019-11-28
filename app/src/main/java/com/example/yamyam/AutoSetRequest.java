package com.example.yamyam;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AutoSetRequest extends StringRequest {
    final static private String URL = "https://jsu3229.cafe24.com/dbeditor/AutoSet_yam.php";
    private Map<String, String> map;

    public AutoSetRequest(String userID, String h1, String h2, String h3, String h4, String h5, String h6, String h7, String h8, String h9, String h10, String h11, String h12, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("userID", userID);
        map.put("h1",h1);
        map.put("h2",h2);
        map.put("h3",h3);
        map.put("h4",h4);
        map.put("h5",h5);
        map.put("h6",h6);
        map.put("h7",h7);
        map.put("h8",h8);
        map.put("h9",h9);
        map.put("h10",h10);
        map.put("h11",h11);
        map.put("h12",h12);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
