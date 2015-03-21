package com.shipntrip.shipntrip;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Freemahn on 21.03.2015.
 */
public class MakeAcceptTask extends AsyncTask<String, Void, Void> {
    String url = "http://95.85.46.247:8080/shipntrip/";
    String mLogin;
    String mTitle;

    public MakeAcceptTask(String mLogin, String mTitle) {
        this.mLogin = mLogin;
        this.mTitle = mTitle;
    }

    @Override
    protected Void doInBackground(String... strings) {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "utf-8");

        HttpClient httpclient = new DefaultHttpClient(params);
        httpclient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        HttpPost http = new HttpPost(url + "beginWork");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("login", mLogin));
        nameValuePairs.add(new BasicNameValuePair("title", mTitle));
        try {
            http.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpclient.execute(http);
            String line = "";
            StringBuilder total = new StringBuilder();
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            while ((line = rd.readLine()) != null) {
                total.append(line);

            }
            Log.e("Response", total.toString());
            if (total.equals("{}"))
                Log.e("ERROR", "in MakeAcceptTask - Empty server answer");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("UPLOADING", "Success");
        return null;
    }


}