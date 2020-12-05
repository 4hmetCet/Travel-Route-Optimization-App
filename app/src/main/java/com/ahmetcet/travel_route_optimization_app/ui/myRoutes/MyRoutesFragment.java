package com.ahmetcet.travel_route_optimization_app.ui.myRoutes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ahmetcet.travel_route_optimization_app.Adapters.MyRoutesListAdapter;
import com.ahmetcet.travel_route_optimization_app.AppMainActivity;
import com.ahmetcet.travel_route_optimization_app.LocalData.SQLiteDataProvider;
import com.ahmetcet.travel_route_optimization_app.R;
import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.Route;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MyRoutesFragment extends Fragment {


    private ArrayList<Route> routes= new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_routes, container, false);
        SQLiteDataProvider sqLiteDataProvider = new SQLiteDataProvider(getContext());
        routes = sqLiteDataProvider.getAllRoutes();
        if(routes.size()==0)
            Toast.makeText(getContext(),"Henüz oluşturduğunuz bir rotanız bulunmamaktadır",Toast.LENGTH_LONG).show();
        else{
            ListView myRoutesListView = (ListView) root.findViewById(R.id.listview_routes);
            MyRoutesListAdapter customAdapter = new MyRoutesListAdapter(getContext(), R.layout.my_routes_list_item, routes);
            myRoutesListView .setAdapter(customAdapter);
        }


        return root;
    }
}