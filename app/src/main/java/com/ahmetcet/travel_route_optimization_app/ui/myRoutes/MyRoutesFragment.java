package com.ahmetcet.travel_route_optimization_app.ui.myRoutes;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ahmetcet.travel_route_optimization_app.Adapters.PointListAdapter;
import com.ahmetcet.travel_route_optimization_app.Adapters.RoutesListAdapter;
import com.ahmetcet.travel_route_optimization_app.AppMainActivity;
import com.ahmetcet.travel_route_optimization_app.LocalData.PrefManager;
import com.ahmetcet.travel_route_optimization_app.LocalData.SQLiteDataProvider;
import com.ahmetcet.travel_route_optimization_app.R;
import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.PointWithConstraints;
import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.Route;

import java.util.ArrayList;

public class MyRoutesFragment extends Fragment {


    private ArrayList<Route> routes= new ArrayList<>();
    private ArrayList<PointWithConstraints> pointList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_routes, container, false);
        SQLiteDataProvider sqLiteDataProvider = new SQLiteDataProvider(getContext());
        routes = sqLiteDataProvider.getAllRoutes();
        if(routes.size()==0)
            Toast.makeText(getContext(),"Henüz oluşturduğunuz bir rotanız bulunmamaktadır",Toast.LENGTH_LONG).show();
        else{
            ListView myRoutesListView = (ListView) root.findViewById(R.id.listview_routes);
            RoutesListAdapter customAdapter = new RoutesListAdapter(getContext(), R.layout.routes_list_item, routes);
            myRoutesListView .setAdapter(customAdapter);
            myRoutesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    final Route selectedRoute = routes.get(position);
                    OpenPointListDialog(selectedRoute.getRouteId());
                    /*Intent intent = new Intent(getContext(), SelectedRouteFragment.class);
                    Bundle b = new Bundle();
                    b.putString("routeId", selectedRoute.getRouteId()); //Your id
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                    */

                }
            });
        }


        return root;
    }

    public void OpenPointListDialog(final String selectedRouteId){
        final Dialog routeSettingsDialog = new Dialog(getContext());


        LayoutInflater inflater = MyRoutesFragment.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.selected_route_points_dialog, null);
        Button btn_use_route = (Button) dialogView.findViewById(R.id.btn_useRoute);

        SQLiteDataProvider sqLiteDataProvider = new SQLiteDataProvider(getContext());

        pointList = sqLiteDataProvider.getPointListByRouteId(getContext(),selectedRouteId);

        //ListView pointListView = (ListView)dialogView.findViewById(R.id.pointList);
        //PointListAdapter listAdapter = new PointListAdapter(getContext(), R.layout.points_list_item, pointList);
        //pointListView .setAdapter(listAdapter);


        btn_use_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.setCurrentRouteId(selectedRouteId,getContext());
                startActivity(new Intent(getContext(), AppMainActivity.class));

            }
        });

        routeSettingsDialog.setContentView(dialogView);
        routeSettingsDialog.setCancelable(true);
        routeSettingsDialog.getWindow().setLayout(1000,1400);
        //spinnerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        routeSettingsDialog.show();

    }

}