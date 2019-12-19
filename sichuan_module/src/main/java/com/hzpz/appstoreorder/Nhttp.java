package com.hzpz.appstoreorder;

import android.util.Log;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lyf on 2019-09-09.
 */
public class Nhttp extends NanoHTTPD {

    private static final String TAG = "NanoHTTPD";

    private OkHttpClient client;
    private final HashMap<String, List<okhttp3.Cookie>> cookieStore = new HashMap<>();


    public Nhttp(int port) {
        super(port);
        try {
            client = new OkHttpClient.Builder()
                    .cookieJar(new CookieJar() {
                        @Override
                        public void saveFromResponse(HttpUrl httpUrl, List<okhttp3.Cookie> list) {
                            cookieStore.put(httpUrl.host(), list);
                        }

                        @Override
                        public List<okhttp3.Cookie> loadForRequest(HttpUrl httpUrl) {
                            List<okhttp3.Cookie> cookies = cookieStore.get(httpUrl.host());
                            return cookies != null ? cookies : new ArrayList<okhttp3.Cookie>();
                        }
                    })
                    .build();

            start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Response serve(IHTTPSession session) {
        Map<String, String> params = session.getParms();

        CookieHandler handler = session.getCookies();
        Iterator<String> iterator = handler.iterator();

        while (iterator.hasNext()) {

        }

        Request.Builder request = new Request.Builder();

        Map<String, String> headers = session.getHeaders();
        Iterator<String> iterator1 = headers.keySet().iterator();
        while (iterator1.hasNext()) {
            String key = iterator1.next();
            String value = headers.get(key);
            request.addHeader(key, value);
        }


        String url = params.get("url");
        if (url != null) {
            params.remove("url");
            if (session.getMethod() == Method.GET) {
                HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
                Iterator<String> iterator2 = params.keySet().iterator();
                while (iterator2.hasNext()) {
                    String key = iterator2.next();
                    String value = params.get(key);
                    urlBuilder.addEncodedQueryParameter(key, value);
                }
                request.url(urlBuilder.build());
            } else if (session.getMethod() == Method.POST) {
                int contentLength = Integer.parseInt(headers.get("content-length"));
                byte[] body;
                try {
                    DataInputStream dataInputStream = new DataInputStream(session.getInputStream());
                    body = new byte[contentLength];
                    dataInputStream.readFully(body, 0, contentLength);

                    RequestBody requestBody = FormBody.create(MediaType.parse(headers.get("content-type")), body);
                    request.url(url).post(requestBody);
                } catch (IOException e) {
                    return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, e.getMessage());
                }
            }
            try {
                okhttp3.Response response = client.newCall(request.build()).execute();//发送请求
                String result = response.body().string();
                Log.d(TAG, "url:" + url + "\nresult: " + result);
                return newFixedLengthResponse(result);
            } catch (IOException e) {
                return newFixedLengthResponse(Response.Status.INTERNAL_ERROR, NanoHTTPD.MIME_PLAINTEXT, e.getMessage());
            }
        }
        String msg = "<html><body><h1>Hello server</h1>\n";
        return newFixedLengthResponse(msg + "</body></html>\n");
    }
}
