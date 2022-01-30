package com.example.tolan.clases;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.io.File;

public class MultipartRequest extends Request<String> {

    private final Response.Listener<String> mListener;
    private final File mFilePart;


    public MultipartRequest(int method, String url, @Nullable Response.ErrorListener listener, Response.Listener<String> mListener, File mFilePart) {
        super(Method.POST, url, listener);
        this.mListener = mListener;
        this.mFilePart = mFilePart;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        return null;
    }

    @Override
    protected void deliverResponse(String response) {

    }
}
