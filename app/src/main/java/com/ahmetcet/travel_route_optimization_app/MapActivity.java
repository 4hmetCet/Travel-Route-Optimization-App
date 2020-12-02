package com.ahmetcet.travel_route_optimization_app;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TimePicker;

import com.ahmetcet.travel_route_optimization_app.LocalData.SQLiteDataProvider;
import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.PointWithConstraints;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.Key;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final static float cameraZoomLevel = 18.0f;
    private String routeId = null;
    private ArrayList<PointWithConstraints> pointList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        routeId = UUID.randomUUID().toString();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng my_loc = new LatLng(40.8238, 29.3718);
        mMap.addMarker(new MarkerOptions().position(my_loc).title("Buradasınız"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(my_loc, cameraZoomLevel));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                //Intent myIntent = new Intent(view.getContext(), agones.class);
                //startActivityForResult(myIntent, 0);


                final Dialog spinnerDialog = new Dialog(MapActivity.this);


                LayoutInflater inflater = MapActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_route_constraints, null);
                final RatingBar ratingBar_priority = (RatingBar) dialogView.findViewById(R.id.ratingBar_priority);
                ratingBar_priority.setNumStars(5);
                ratingBar_priority.setStepSize(1f);
                Button button_setPoint = (Button) dialogView.findViewById(R.id.button_setPoint);
                final EditText editText_pointName = (EditText) dialogView.findViewById(R.id.editText_pointExplanation);
                final EditText editText_earliestTimePicker = (EditText) dialogView.findViewById(R.id.editText_earliestTime);
                GetTimePickerEvent(editText_earliestTimePicker);

                final EditText editText_latestTimePicker = (EditText) dialogView.findViewById(R.id.editText_latestTime);
                GetTimePickerEvent(editText_latestTimePicker);


                button_setPoint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PointWithConstraints point = new PointWithConstraints();
                        if(editText_pointName.getText().toString().length()== 0){
                            editText_pointName.setError("Konum ismi girmelisiniz");
                            return;
                        }

                        mMap.addMarker(new MarkerOptions().position(latLng)
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                .title(editText_pointName.getText().toString())
                        );
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, cameraZoomLevel));

                        point.setPointName(editText_pointName.getText().toString());
                        point.setPointId(UUID.randomUUID().toString());
                        point.setRouteId(routeId);
                        point.setPointLocation(latLng);
                        point.setPriority((int)ratingBar_priority.getRating());
                        pointList.add(point);
                        if(spinnerDialog.isShowing())
                            spinnerDialog.dismiss();
                    }
                });

                spinnerDialog.setContentView(dialogView);
                spinnerDialog.setCancelable(true);
                spinnerDialog.getWindow().setLayout(1000,1400);
                //spinnerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                spinnerDialog.show();

            }
        });
    }

    public void OptimizeRoute(View view) {

        ProgressDialog progressDialog = new ProgressDialog(MapActivity.this);
        progressDialog.setMessage("Rotanız Oluşturuluyor, bu işlem biraz uzun sürebilir");
        progressDialog.create();
        progressDialog.show();
        SQLiteDataProvider sqLiteDataProvider = new SQLiteDataProvider(MapActivity.this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void GetTimePickerEvent(final EditText editText_timePicker){

        editText_timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = View.inflate(MapActivity.this, R.layout.dialog_time_picker, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(MapActivity.this).create();
                final TimePicker timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
                timePicker.setIs24HourView(true);

                dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String time = timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute();
                        editText_timePicker.setText(time);
                        alertDialog.dismiss();
                    }});

                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });
    }
}