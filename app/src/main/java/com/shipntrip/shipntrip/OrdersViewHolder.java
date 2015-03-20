package com.shipntrip.shipntrip;

import android.view.View;
import android.widget.TextView;

import uk.co.ribot.easyadapter.ItemViewHolder;
import uk.co.ribot.easyadapter.PositionInfo;
import uk.co.ribot.easyadapter.annotations.LayoutId;
import uk.co.ribot.easyadapter.annotations.ViewId;

/**
 * Created by Freemahn on 21.03.2015.
 */
//Annotate the class with the layout ID of the item.
@LayoutId(R.layout.orders_list_item)
public class OrdersViewHolder extends ItemViewHolder<Order> {

    //Annotate every field with the ID of the view in the layout.
    //The views will automatically be assigned to the fields.
    /*@ViewId(R.id.image_view_person)
    ImageView imageViewPerson;*/

    @ViewId(R.id.order_name)
    TextView textViewName;

    @ViewId(R.id.order_city)
    TextView textViewCity;

    //Extend ItemViewHolder and call super(view)
    public OrdersViewHolder(View view) {
        super(view);
    }

    //Override onSetValues() to set the values of the items in the views.
    @Override
    public void onSetValues(Order order, PositionInfo positionInfo) {
        //imageViewPerson.setImageResource(person.getResDrawableId());
        textViewName.setText(order.name);
        textViewCity.setText(order.city);
    }
}

