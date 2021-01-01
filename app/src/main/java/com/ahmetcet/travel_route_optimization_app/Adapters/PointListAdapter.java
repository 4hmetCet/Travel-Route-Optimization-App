package com.ahmetcet.travel_route_optimization_app.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ahmetcet.travel_route_optimization_app.R;
import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.PointWithConstraints;
import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.Route;

import java.util.ArrayList;

public class PointListAdapter extends ArrayAdapter<PointWithConstraints> {

    private int resourceLayout;
    private Context mContext;

    public PointListAdapter(Context context, int resource, ArrayList<PointWithConstraints> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        PointWithConstraints point = getItem(position);

        if (point != null) {
            TextView tv_pointOrder = (TextView) v.findViewById(R.id.textView_itemPointOrder);
            tv_pointOrder.setText(Integer.toString(point.getOrder()));
            TextView tv_pointName = (TextView) v.findViewById(R.id.textView_itemPointName);
            tv_pointName.setText(point.getPointName());

        }

        return v;
    }

}
