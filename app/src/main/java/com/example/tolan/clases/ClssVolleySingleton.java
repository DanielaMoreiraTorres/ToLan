package com.example.tolan.clases;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class ClssVolleySingleton {

    private static ClssVolleySingleton intanciaVolley;
    private RequestQueue request;
    private static Context contexto;

    private ClssVolleySingleton(Context context) {
        contexto = context;
        request = getRequestQueue();
    }


    public static synchronized ClssVolleySingleton getIntanciaVolley(Context context) {
        if (intanciaVolley == null) {
            intanciaVolley = new ClssVolleySingleton(context);
        }

        return intanciaVolley;
    }

    public RequestQueue getRequestQueue() {
        if (request == null) {
            request = Volley.newRequestQueue(contexto.getApplicationContext());
        }

        return request;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(request);
    }

}