package com.shipntrip.shipntrip;

/**
 * Created by Freemahn on 21.03.2015.
 */

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;


/**
 * Created by Freemahn on 21.03.2015.
 */

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DialogAddCity extends DialogFragment {
    public Order order;
    final String LOG_TAG = "myLogs";
    int position;
    AppLocationService appLocationService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // appLocationService = new AppLocationService(getActivity().getApplication());
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle("Выбор города");
        View v = inflater.inflate(R.layout.layout_dialog_add_city, null);

        final EditText et = ((EditText) v.findViewById(R.id.et_city_add));
       // final String city = showNWLocation();
       /* if (city != null)
            et.setText(city);*/
        v.findViewById(R.id.btn_yes_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("city", et.getText().toString());
                getTargetFragment().onActivityResult(SlidingTabsBasicFragment.REQUEST_CODE_ADD_DIALOG, 1, intent);
                dismiss();
            }
        });
        v.findViewById(R.id.btn_no_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                getTargetFragment().onActivityResult(SlidingTabsBasicFragment.REQUEST_CODE_ADD_DIALOG, -1, intent);
                dismiss();
            }
        });
        return v;
    }


    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.d(LOG_TAG, "Dialog 1: onDismiss");
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        Log.d(LOG_TAG, "Dialog 1: onCancel");
    }

   /* String showNWLocation() {
        Location nwLocation = appLocationService
                .getLocation(LocationManager.NETWORK_PROVIDER);

        if (nwLocation != null) {
            double latitude = nwLocation.getLatitude();
            double longitude = nwLocation.getLongitude();
            Toast.makeText(
                    getActivity().getApplicationContext(),
                    "Mobile Location (NW): \nLatitude: " + latitude
                            + "\nLongitude: " + longitude,
                    Toast.LENGTH_LONG).show();
            return getCity(latitude, longitude);
        } else {
            //showSettingsAlert("NETWORK");
            Toast.makeText(getActivity().getApplicationContext(), "NETWORK", Toast.LENGTH_LONG).show();
            return null;
        }


    }

    String getCity(double latitude, double longitude) {
        Geocoder geoCoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
        StringBuilder builder = new StringBuilder();
        try {
            List<Address> address = geoCoder.getFromLocation(latitude, longitude, 1);

            int maxLines = address.get(0).getMaxAddressLineIndex();
            for (int i = 0; i < maxLines; i++) {
                String addressStr = address.get(0).getAddressLine(i);
                builder.append(addressStr);
                builder.append(" ");
            }
            com.example.android.common.logger.Log.e("getCity", address.get(0).getAdminArea().split(" ")[1]);
            return address.get(0).getAdminArea().split(" ")[1];
        } catch (IOException e) {
        }

        return null;
    }*/
}