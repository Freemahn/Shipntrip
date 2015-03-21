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
public class MakeOrderTask extends AsyncTask<String, Void, Void> {
    String url = "http://95.85.46.247:8080/shipntrip/";
    Order mOrder;

    MakeOrderTask(Order order) {
        mOrder = order;

    }

    @Override
    protected Void doInBackground(String... strings) {
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "utf-8");

        HttpClient httpclient = new DefaultHttpClient(params);
        httpclient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
        HttpPost http = new HttpPost(url + "makeOrder");
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("address", mOrder.address));
        nameValuePairs.add(new BasicNameValuePair("city", mOrder.city));
        nameValuePairs.add(new BasicNameValuePair("comment", mOrder.comment));
        nameValuePairs.add(new BasicNameValuePair("cost", mOrder.cost));
        nameValuePairs.add(new BasicNameValuePair("date", mOrder.date));
        nameValuePairs.add(new BasicNameValuePair("link", mOrder.link));
        nameValuePairs.add(new BasicNameValuePair("title", mOrder.title));
        nameValuePairs.add(new BasicNameValuePair("login", mOrder.owner));


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
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("UPLOADING", "Success");
        return null;
    }


}
