package com.ahmetcet.travel_route_optimization_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ahmetcet.travel_route_optimization_app.R;
import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.Route;

import java.util.ArrayList;

public class MyRoutesListAdapter extends ArrayAdapter<Route> {

    private int resourceLayout;
    private Context mContext;

    public MyRoutesListAdapter(Context context, int resource, ArrayList<Route> items) {
        super(context, resource, items);
        this.resourceLayout = resource;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }

        Route route = getItem(position);

        if (route != null) {
            TextView tv_routeName = (TextView) v.findViewById(R.id.textView_itemRouteName);
            tv_routeName.setText(route.getRouteName());
            TextView tv_routeDate = (TextView) v.findViewById(R.id.textView_itemRouteDate);
            tv_routeDate.setText(route.getRouteDate());

        }

        return v;
    }

}