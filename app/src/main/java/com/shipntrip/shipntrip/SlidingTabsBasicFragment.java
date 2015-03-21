/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shipntrip.shipntrip;

import com.example.android.common.logger.Log;
import com.example.android.common.view.SlidingTabLayout;
import com.google.gson.Gson;

import android.content.Context;
import android.os.AsyncTask;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * A basic sample which shows how to use {@link com.example.android.common.view.SlidingTabLayout}
 * to display a custom {@link ViewPager} title strip which gives continuous feedback to the user
 * when scrolling.
 */
public class SlidingTabsBasicFragment extends Fragment {

    static final String LOG_TAG = "SlidingTabsBasicFragment";
    public static final int REQUEST_CODE_MAKE_ORDER = 0;
    public static final int REQUEST_CODE_ACCEPT_DIALOG = 1;
    List<Order> pendingList;
    List<Order> taskList;
    List<Order> orderList;
    ServerAnswer user;
    ListView pendingLW;
    ListView orderLW;
    ListView taskLW;
    MyCustomAdapter myPendingAdapter;
    MyCustomAdapter myOrderAdapter;
    MyCustomAdapter myTaskAdapter;
    String loginUser = "xaker";
    Fragment t;

    /**
     * A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        t = this;
        taskList = new ArrayList<>();
        pendingList = new ArrayList<>();
        orderList = new ArrayList<>();
//        if (myTaskAdapter == null)
        myTaskAdapter = new MyCustomAdapter(getActivity().getApplicationContext());
//        if (myPendingAdapter == null)
        myPendingAdapter = new MyCustomAdapter(getActivity().getApplicationContext());
//        if (myOrderAdapter == null)
        myOrderAdapter = new MyCustomAdapter(getActivity().getApplicationContext());
    }

    private ViewPager mViewPager;

    /**
     * Inflates the {@link View} which will be displayed by this {@link Fragment}, from the app's
     * resources.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sample, container, false);
    }

    // BEGIN_INCLUDE (fragment_onviewcreated)

    /**
     * This is called after the {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} has finished.
     * Here we can pick out the {@link View}s we need to configure from the content view.
     * <p/>
     * We set the {@link ViewPager}'s adapter to be an instance of {@link SamplePagerAdapter}. The
     * {@link SlidingTabLayout} is then given the {@link ViewPager} so that it can populate itself.
     *
     * @param view View created in {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // BEGIN_INCLUDE (setup_viewpager)
        // Get the ViewPager and set it's PagerAdapter so that it can display items
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager);
        mViewPager.setAdapter(new SamplePagerAdapter());
        // END_INCLUDE (setup_viewpager)

        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.horizontal_tap_item, R.id.item);
        mSlidingTabLayout.setViewPager(mViewPager);
        // END_INCLUDE (setup_slidingtablayout)
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
    // END_INCLUDE (fragment_onviewcreated)

    /**
     * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
     * The individual pages are simple and just display two lines of text. The important section of
     * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
     * {@link SlidingTabLayout}.
     */
    class SamplePagerAdapter extends PagerAdapter {

        /**
         * @return the number of pages to display
         */
        @Override
        public int getCount() {
            return 3;
        }

        /**
         * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
         * same object as the {@link View} added to the {@link ViewPager}.
         */
        @Override
        public boolean isViewFromObject(View view, Object o) {
            return o == view;
        }

        // BEGIN_INCLUDE (pageradapter_getpagetitle)

        /**
         * Return the title of the horizontal_tap_item at {@code position}. This is important as what this method
         * returns is what is displayed in the {@link SlidingTabLayout}.
         * <p/>
         * Here we construct one using the position value, but for real application the title should
         * refer to the horizontal_tap_item's contents.
         */
        @Override
        public CharSequence getPageTitle(int position) {
            
            /*return "Item " + (position + 1);*/
            if (position == 0) return "Задачи";
            if (position == 1) return "Профиль";
            if (position == 2) return "Покупки";
            return "nill";
        }
        // END_INCLUDE (pageradapter_getpagetitle)

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // Inflate a new layout from our resources

            final View viewProfile;
            final View viewTask;
            final View viewOrder;
            if (position == 0) {//current page is tasks
                viewTask = getActivity().getLayoutInflater().inflate(R.layout.pager_item_orders,
                        container, false);
                container.addView(viewTask);
                taskLW = (ListView) viewTask.findViewById(R.id.lw_orders);
                taskLW.setAdapter(myTaskAdapter);
                return viewTask;
            } else if (position == 1) {//current page is profile
                viewProfile = getActivity().getLayoutInflater().inflate(R.layout.pager_item_profile,
                        container, false);
                Button btnAddNextCity = (Button) viewProfile.findViewById(R.id.btn_add_next_city);
                btnAddNextCity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new GetUserInfoTask(loginUser).execute();
                    }
                });
                pendingLW = (ListView) viewProfile.findViewById(R.id.lw_orders);
                pendingLW.setAdapter(myPendingAdapter);
                pendingLW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        DialogAcceptOrder dlgAccept = new DialogAcceptOrder();
                        Bundle bundle = new Bundle();
                        bundle.putCharSequence("order", new Gson().toJson(parent.getItemAtPosition(position)));
                        bundle.putInt("position", position);
                        dlgAccept.setArguments(bundle);
                        dlgAccept.setTargetFragment(t, REQUEST_CODE_ACCEPT_DIALOG);
                        dlgAccept.show(getFragmentManager(), "dlg_tag");
                    }
                });
                container.addView(viewProfile);
                return viewProfile;

            } else {//current page is goods
                viewOrder = getActivity().getLayoutInflater().inflate(R.layout.pager_item_goods,
                        container, false);
                orderLW = (ListView) viewOrder.findViewById(R.id.lw_orders);
                orderLW.setAdapter(myOrderAdapter);
                viewOrder.findViewById(R.id.btn_new_order).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(viewOrder.getContext(), MakeNewOrderActivity.class);
                        intent.putExtra("loginUser", loginUser);
                        startActivityForResult(intent, REQUEST_CODE_MAKE_ORDER);
                    }
                });
                container.addView(viewOrder);
                return viewOrder;
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.i(LOG_TAG, "destrItem() [position: " + position + "]");

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ACCEPT_DIALOG) {
            int position = data.getIntExtra("position", 0);
            if (resultCode == 1) {//ok
                new MakeAcceptTask(loginUser, myPendingAdapter.getItem(position).title).execute();

            } else {//dismiss
                myPendingAdapter.removeItem(position);
            }
        }
        if (requestCode == REQUEST_CODE_MAKE_ORDER) {

        }
    }

    public class GetUserInfoTask extends AsyncTask<Void, Void, Void> {

        final String url = "http://95.85.46.247:8080/shipntrip/";
        final String mLogin;

        GetUserInfoTask(String login) {
            mLogin = login;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... strings) {
            String json = null;
            try {
                json = Jsoup.connect(url + "getUserInfo?login=" + mLogin).timeout(3000).ignoreContentType(true).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            Log.d("USER", json + "");
            user = gson.fromJson(json, ServerAnswer.class);
            return null;
        }

        @Override
        protected void onPostExecute(final Void success) {
            //Log.e("before", pendingLW.getAdapter().getCount() + "");
            myPendingAdapter.clear();
            myOrderAdapter.clear();
            myTaskAdapter.clear();

            for (Order o : user.pendingList)
                myPendingAdapter.addItem(o);
            pendingLW.setAdapter(myPendingAdapter);
            for (Order o : user.taskList)
                myTaskAdapter.addItem(o);
            taskLW.setAdapter(myTaskAdapter);
            for (Order o : user.orderList)
                myOrderAdapter.addItem(o);
            orderLW.setAdapter(myOrderAdapter);
            Log.e("after", orderLW.getAdapter().getCount() + "");

        }
    }


    private class MyCustomAdapter extends BaseAdapter {
        private Context mContext;
        private ArrayList<Order> mData = new ArrayList();
        private LayoutInflater mInflater;

        public MyCustomAdapter(Context context) {
            mContext = context;
            mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void addItem(final Order item) {
            mData.add(item);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mData.size();
        }


        public void clear() {
            mData = new ArrayList<>();
        }

        public void removeItem(int position) {
            mData.remove(position);
        }

        @Override
        public Order getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            System.out.println("getView " + position + " " + convertView);
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.orders_list_item, null);
                holder = new ViewHolder();
                holder.twName = (TextView) convertView.findViewById(R.id.order_name);
                //      holder.twCity = (TextView) convertView.findViewById(R.id.order_city);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.twName.setText(mData.get(position).title);
            //     holder.twCity.setText(mData.get(position).city);
            return convertView;
        }

    }

    public static class ViewHolder {
        public TextView twName;
        //  public TextView twCity;
    }


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
                android.util.Log.e("Response", total.toString());
                if (total.toString().equals("{}"))
                    android.util.Log.e("ERROR", "in MakeAcceptTask - Empty server answer");
            } catch (Exception e) {
                e.printStackTrace();
            }
            android.util.Log.d("UPLOADING", "Success");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            new GetUserInfoTask(loginUser);
        }
    }


}
