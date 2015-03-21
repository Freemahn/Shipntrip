package com.shipntrip.shipntrip;

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

import com.google.gson.Gson;

public class DialogAcceptOrder extends DialogFragment {
    public Order order;
    final String LOG_TAG = "myLogs";
    int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("BUNDLE", getArguments().getString("order"));
        order = new Gson().fromJson(getArguments().getString("order"), Order.class);
        position = getArguments().getInt("position");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getDialog().setTitle("Информация о заказе");
        View v = inflater.inflate(R.layout.layout_dialog_accept_order, null);
        String info = "Товар: " + order.title + "\n";
        info += "Город: " + order.city + "\n";
        info += "Цена: " + order.cost + "\n";
        info += "Дата: " + order.date;
        ((TextView) v.findViewById(R.id.textView1)).setText(info);
        v.findViewById(R.id.btnYes).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("position", position);
                getTargetFragment().onActivityResult(1, 1, intent);
            }
        });
        v.findViewById(R.id.btnNo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("position", position);
                getTargetFragment().onActivityResult(1, -1, intent);
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
}