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

import android.os.AsyncTask;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.ribot.easyadapter.EasyAdapter;

/**
 * A basic sample which shows how to use {@link com.example.android.common.view.SlidingTabLayout}
 * to display a custom {@link ViewPager} title strip which gives continuous feedback to the user
 * when scrolling.
 */
public class SlidingTabsBasicFragment extends Fragment {

    static final String LOG_TAG = "SlidingTabsBasicFragment";

    /**
     * A custom {@link ViewPager} title strip which looks much like Tabs present in Android v4.0 and
     * above, but is designed to give continuous feedback to the user when scrolling.
     */
    private SlidingTabLayout mSlidingTabLayout;

    /**
     * A {@link ViewPager} which will be used in conjunction with the {@link SlidingTabLayout} above.
     */
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
            final View view;
            if (position == 0) {//current page is tasks
                view = getActivity().getLayoutInflater().inflate(R.layout.pager_item_orders,
                        container, false);

            } else if (position == 1) {//current page is profile
                view = getActivity().getLayoutInflater().inflate(R.layout.pager_item_profile,
                        container, false);
                //TODO get orders from server
                Button b = (Button) view.findViewById(R.id.btn_add_next_city);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new GetUserInfoTask("freemahn").execute();
                    }
                });


            } else {//current page is goods
                view = getActivity().getLayoutInflater().inflate(R.layout.pager_item_goods,
                        container, false);
                view.findViewById(R.id.btn_new_order).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(view.getContext(), MakeNewOrderActivity.class);
                        startActivityForResult(intent, 0);
                    }
                });
            }
            // Add the newly created View to the ViewPager
            container.addView(view);

            ListView ordersLW = (ListView) view.findViewById(R.id.lw_orders);
            List<Order> ordersList = new ArrayList<>();
           // ordersList.add(new Order("vine", "Moscow"));
           // ordersList.add(new Order("cheese", "Penza"));
            if (position == 0)
                ordersList.add(new Order("ZERO", "Penza"));
            if (position == 1) {
                ordersLW.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        DialogFragment dlgAccept = new DialogAcceptOrder();
                        dlgAccept.show(getFragmentManager(), "dlg_tag");
                    }
                });
            }
            if (position == 2)
               // ordersList.add(new Order("TWO", "Penza"));
            ordersLW.setAdapter(new EasyAdapter<Order>(view.getContext(),
                    OrdersViewHolder.class, ordersList
            ));

            Log.i(LOG_TAG, "instantiateItem() [position: " + position + "]");
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            Log.i(LOG_TAG, "destrItem() [position: " + position + "]");

        }
    }


    public class GetUserInfoTask extends AsyncTask<Void, Void, Void> {

        final String url = "http://95.85.46.247:8080/shipntrip/";
        final String mLogin;

        GetUserInfoTask(String login) {
            mLogin = login;
        }

        @Override
        protected Void doInBackground(Void... strings) {
            String json = null;
            try {
                json = Jsoup.connect(url + "getUserInfo?login=" + mLogin).ignoreContentType(true).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            Log.d("USER", json + "");
            ServerAnswer user = gson.fromJson(json, ServerAnswer.class);
            android.util.Log.d("GETUSER", user.taskList + "");
            return null;
        }

        @Override
        protected void onPostExecute(final Void success) {
            // nameFirst.setText(user.name);
            //nameLast.setText(user.name.split(" ")[1]);
        }
    }


}
