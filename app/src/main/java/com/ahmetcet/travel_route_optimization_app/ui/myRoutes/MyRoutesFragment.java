package com.ahmetcet.travel_route_optimization_app.ui.myRoutes;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.ahmetcet.travel_route_optimization_app.Tools;

import java.util.ArrayList;

public class MyRoutesFragment extends Fragment {


    private ArrayList<Route> routes= new ArrayList<>();
    private ArrayList<PointWithConstraints> pointList;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_my_routes, container, false);
        setRoutesData();

        return root;
    }

    public void setRoutesData(){
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
                }
            });
        }

    }

    public void OpenPointListDialog(final String selectedRouteId){
        final Dialog pointListDialog = new Dialog(getContext());


        LayoutInflater inflater = MyRoutesFragment.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.selected_route_points_dialog, null);
        Button btn_use_route = (Button) dialogView.findViewById(R.id.btn_useRoute);
        ImageButton btn_delete_route = (ImageButton) dialogView.findViewById(R.id.btn_deleteRoute);
        ImageButton btn_share_route = (ImageButton) dialogView.findViewById(R.id.btn_share);

        final SQLiteDataProvider sqLiteDataProvider = new SQLiteDataProvider(getContext());

        pointList = sqLiteDataProvider.getPointListByRouteId(getContext(),selectedRouteId);

        ListView pointListView = (ListView)dialogView.findViewById(R.id.pointList);
        PointListAdapter listAdapter = new PointListAdapter(getContext(), R.layout.points_list_item, pointList);
        pointListView.setAdapter(listAdapter);


        btn_use_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrefManager.setCurrentRouteId(selectedRouteId,getContext());
                startActivity(new Intent(getContext(), AppMainActivity.class));

            }
        });

        btn_delete_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sqLiteDataProvider.deleteRouteData(selectedRouteId);
                pointListDialog.dismiss();
                setRoutesData();

            }
        });

        btn_share_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tools.ShowAlertDialog(getContext(),"Rotanızı diğer kullanıcılar ile paylaşmak istediğinize emin misiniz?",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               ProgressDialog progressDialog = new ProgressDialog(getContext());
                               progressDialog.setMessage("Rotanız paylaşılıyor, lütfen bekleyiniz...");
                               progressDialog.setCancelable(true);
                               progressDialog.show();
                            }
                        },true);
            }
        });

        pointListDialog.setContentView(dialogView);
        pointListDialog.setCancelable(true);
        pointListDialog.getWindow().setLayout(1000,1400);
        //spinnerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pointListDialog.show();

    }

}