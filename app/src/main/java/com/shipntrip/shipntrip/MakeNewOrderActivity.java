package com.shipntrip.shipntrip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


/**
 * Created by Freemahn on 21.03.2015.
 */
public class MakeNewOrderActivity extends Activity implements AdapterView.OnItemClickListener {


    private static final String LOG_TAG = "Autocomplete";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    AutoCompleteTextView etAutocomplete;
    SeekBar seekBar;
    TextView twDays;
    EditText etAddress;
    EditText etLink;
    EditText etCost;
    EditText etComment;
    EditText etTitle;
    String loginUser;

    private static final String API_KEY = "AIzaSyC54rmw_TZ3p90kAcqXKh3ilgmL3e8P-7c";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginUser = getIntent().getStringExtra("loginUser");
        setContentView(R.layout.activity_make_new_order);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        twDays = (TextView) findViewById(R.id.tw_days);
        etAddress = (EditText) findViewById(R.id.et_address);
        etLink = (EditText) findViewById(R.id.et_link);
        etCost = (EditText) findViewById(R.id.et_cost);
        etComment = (EditText) findViewById(R.id.et_comment);
        etTitle = (EditText) findViewById(R.id.et_title);
        etAutocomplete = (AutoCompleteTextView) findViewById(R.id.text_city_autocomplete);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                twDays.setText(progress + " дн");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
       /* Button b = (Button) findViewById(R.id.btn_return);*/


        etAutocomplete.setAdapter(new GooglePlacesAutocompleteAdapter(this, R.layout.list_item_autocomplete));
        etAutocomplete.setOnItemClickListener(this);
        Button b = (Button) findViewById(R.id.btn_ok);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MakeOrderTask(getOrder()).execute();
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    Order getOrder() {
        Order order = new Order();
        Calendar c = GregorianCalendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, seekBar.getProgress());
        Date d = c.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd");
        String date = sdf.format(d);
        Log.e("DATE", date);
        order.date = date;
        order.address = etAddress.getText().toString();
        order.link = etLink.getText().toString();
        order.city = etAutocomplete.getText().toString();
        order.comment = etComment.getText().toString();
        order.cost = etCost.getText().toString();
        order.title = etTitle.getText().toString();
        order.owner = loginUser;

        return order;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String str = (String) parent.getItemAtPosition(position);
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


    public static ArrayList autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&types=(cities)");
            sb.append("&language=(ru)");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));

            URL url = new URL(sb.toString());
            //  Log.e("TAG", url.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {

                //  System.out.println(predsJsonArray.getJSONObject(i).getJSONArray("terms").get(0));
                // System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }
}
