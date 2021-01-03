package com.ahmetcet.travel_route_optimization_app.ui.home;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.ahmetcet.travel_route_optimization_app.LocalData.PrefManager;
import com.ahmetcet.travel_route_optimization_app.LocalData.SQLiteDataProvider;
import com.ahmetcet.travel_route_optimization_app.R;
import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.PointWithConstraints;
import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.Route;
import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Optimize;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class HomeFragment extends Fragment implements OnMapReadyCallback, LocationListener{

    private GoogleMap map;
    private SupportMapFragment mapView;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    private ArrayList<PointWithConstraints> pointList;
    private String currRouteId;
    private Button btn_nextPoint;
    private SQLiteDataProvider sqLiteDataProvider;
    private Optimize optimize;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        // Gets the MapView from the XML layout and creates it
        mapView = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_currRoute);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this); //this is important

        btn_nextPoint = (Button) root.findViewById(R.id.btn_nextPoint);
        btn_nextPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (PointWithConstraints point:
                        pointList) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(point.getPointLocation(),18));

                }
            }
        });
        sqLiteDataProvider = new SQLiteDataProvider(getContext());
        currRouteId = PrefManager.getCurrentRouteId(getContext());
        optimize = new Optimize(getContext(),new Route());
        pointList = sqLiteDataProvider.getPointListByRouteId(getContext(),currRouteId);

        return root;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                return;
            }else{
                map.setMyLocationEnabled(true);
            }
        }
        else {
            map.setMyLocationEnabled(true);
        }

        map.setTrafficEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().isCompassEnabled();
        map.setBuildingsEnabled(true);


        pointList = optimize.OrderPointsByCurrentLocation(pointList,new LatLng(38.5043,27.7045));
        mapRoute();


    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }
        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        mCurrLocationMarker = map.addMarker(markerOptions);

        //move map camera
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()),18));





    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    private void mapRoute(){
        try {
            if(pointList == null || pointList.size() == 0){
                btn_nextPoint.setVisibility(View.INVISIBLE);
            }
            for (PointWithConstraints point :
                    pointList) {
                Bitmap.Config conf = Bitmap.Config.ARGB_8888;
                Bitmap bmp = Bitmap.createBitmap(200, 50, conf);
                Canvas canvas = new Canvas(bmp);
                Paint tPaint = new Paint();
                tPaint.setTextSize(35);
                tPaint.setColor(Color.BLUE);
                tPaint.setStyle(Paint.Style.FILL_AND_STROKE);

                canvas.drawText(String.valueOf(point.getOrder()), 100, 50, tPaint); // paint defines the text color, stroke width, size



                map.addMarker(new MarkerOptions().position(point.getPointLocation())
                        .title(point.getPointName())
                        .icon(BitmapDescriptorFactory.fromBitmap(bmp))
                        .anchor(0.5f, 1)
                );
            }
        } catch (SQLException e) {

        }
    }


}