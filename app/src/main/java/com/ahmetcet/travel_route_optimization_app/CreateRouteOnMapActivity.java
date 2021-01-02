package com.ahmetcet.travel_route_optimization_app;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ahmetcet.travel_route_optimization_app.LocalData.PrefManager;
import com.ahmetcet.travel_route_optimization_app.LocalData.SQLiteDataProvider;
import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.PointWithConstraints;
import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.Route;
import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Optimize;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.UUID;

public class CreateRouteOnMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final static float cameraZoomLevel = 18.0f;
    private Route current_route = null;
    private ArrayList<PointWithConstraints> currentPointList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route_on_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        OpenRouteSettingsDialog();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(CreateRouteOnMapActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(CreateRouteOnMapActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            mMap.setMyLocationEnabled(true);
        }
        mMap.setTrafficEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().isCompassEnabled();
        mMap.setBuildingsEnabled(true);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {

                if(currentPointList.size() >= 16){
                    Toast.makeText(CreateRouteOnMapActivity.this,"Bir rota için en fazla 16 tane nokta seçebilirsiniz",Toast.LENGTH_LONG).show();
                    return;
                }

                final Dialog spinnerDialog = new Dialog(CreateRouteOnMapActivity.this);


                LayoutInflater inflater = CreateRouteOnMapActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_point_constraints, null);

                Button button_setPoint = (Button) dialogView.findViewById(R.id.button_setPoint);
                final EditText editText_pointName = (EditText) dialogView.findViewById(R.id.editText_pointExplanation);
                final Spinner spin_priority = (Spinner) dialogView.findViewById(R.id.spinner_priority);
                ArrayList<String> priorityList = new ArrayList<>();
                priorityList.add("Seçiniz");
                priorityList.add("Önemli");
                priorityList.add("Normal");
                priorityList.add("Öncelikli Değil");
                ArrayAdapter<String> adapter_spinnerPriority = new ArrayAdapter<String>(CreateRouteOnMapActivity.this, android.R.layout.simple_spinner_item,priorityList);
                spin_priority.setAdapter(adapter_spinnerPriority);
                final EditText editText_earliestTimePicker = (EditText) dialogView.findViewById(R.id.editText_earliestTime);
                GetTimePickerEvent(editText_earliestTimePicker);

                final EditText editText_latestTimePicker = (EditText) dialogView.findViewById(R.id.editText_latestTime);
                GetTimePickerEvent(editText_latestTimePicker);

                final Spinner spin_relatedPoints = (Spinner) dialogView.findViewById(R.id.spinner_relatedPoints);

                ArrayList<String> pointNameList = new ArrayList<>();
                pointNameList.add("Seçiniz");
                for (PointWithConstraints point :
                        currentPointList) {
                    pointNameList.add(point.getPointName());
                }
                ArrayAdapter<String> adapter_relatedPoints = new ArrayAdapter<String>(CreateRouteOnMapActivity.this, android.R.layout.simple_spinner_item,pointNameList);
                spin_relatedPoints.setAdapter(adapter_relatedPoints);


                button_setPoint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PointWithConstraints point = new PointWithConstraints();
                        if(editText_pointName.getText().toString().length()== 0){
                            editText_pointName.setError("Konum ismi girmelisiniz");
                            return;
                        }
                        for (PointWithConstraints item_point :
                                currentPointList) {
                            if(editText_pointName.getText().toString().equals(item_point.getPointName())){
                                editText_pointName.setError("Aynı rota için ikinci kez aynı isimli nokta belirleyemezsiniz");
                                return;
                            }

                        }


                        mMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                .title(editText_pointName.getText().toString())
                        );
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, cameraZoomLevel));

                        point.setPointName(editText_pointName.getText().toString());
                        point.setPointId(UUID.randomUUID().toString());
                        point.setRouteId(current_route.getRouteId());
                        point.setPointLocation(latLng);
                        if(spin_priority.getSelectedItem().toString().equals("Önemli"))
                            point.setPriority(3);
                        else if (spin_priority.getSelectedItem().toString().equals("Normal"))
                            point.setPriority(2);
                        else if (spin_priority.getSelectedItem().toString().equals("Öncelikli Değil"))
                            point.setPriority(1);
                        else
                            point.setPriority(0);

                        if (!spin_relatedPoints.getSelectedItem().toString().equals("Seçiniz")) {
                            for (PointWithConstraints item_point :
                                    currentPointList) {
                                if(item_point.getPointName().equals(spin_relatedPoints.getSelectedItem().toString())){
                                    point.setPreviousPointId(item_point.getPointId());
                                    break;
                                }
                            }
                        }

                        currentPointList.add(point);
                        if(spinnerDialog.isShowing())
                            spinnerDialog.dismiss();
                    }
                });

                spinnerDialog.setContentView(dialogView);
                spinnerDialog.setCancelable(true);
                spinnerDialog.getWindow().setLayout(1000,1400);
                spinnerDialog.show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void GetTimePickerEvent(final EditText editText_timePicker){
        editText_timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = View.inflate(CreateRouteOnMapActivity.this, R.layout.dialog_time_picker, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(CreateRouteOnMapActivity.this).create();
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

    public void OpenRouteSettingsDialog(){
        final Dialog routeSettingsDialog = new Dialog(CreateRouteOnMapActivity.this);


        LayoutInflater inflater = CreateRouteOnMapActivity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_route_settings, null);
        Button btn_save_route_settings = (Button) dialogView.findViewById(R.id.button_saveRouteSettings);
        Button btn_cancel_route = (Button) dialogView.findViewById(R.id.button_cancel);
        final EditText editText_routeDatePicker = (EditText) dialogView.findViewById(R.id.editText_routeDate);
        GetDatePickerEvent(editText_routeDatePicker);

        final EditText editText_routeName = (EditText) dialogView.findViewById(R.id.editText_routeName);
        final ImageButton imageButton_walk = (ImageButton) dialogView.findViewById(R.id.imageButton_walk);
        final ImageButton imageButton_car = (ImageButton) dialogView.findViewById(R.id.imageButton_car);
        final ImageButton imageButton_bus = (ImageButton) dialogView.findViewById(R.id.imageButton_bus);
        imageButton_walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButton_walk.setBackgroundColor(getResources().getColor(R.color.colorButton));
                imageButton_car.setBackgroundColor(getResources().getColor(R.color.ic_launcher_background));
                imageButton_bus.setBackgroundColor(getResources().getColor(R.color.ic_launcher_background));
            }
        });
        imageButton_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButton_car.setBackgroundColor(getResources().getColor(R.color.colorButton));
                imageButton_walk.setBackgroundColor(getResources().getColor(R.color.ic_launcher_background));
                imageButton_bus.setBackgroundColor(getResources().getColor(R.color.ic_launcher_background));
            }
        });
        imageButton_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageButton_bus.setBackgroundColor(getResources().getColor(R.color.colorButton));
                imageButton_car.setBackgroundColor(getResources().getColor(R.color.ic_launcher_background));
                imageButton_walk.setBackgroundColor(getResources().getColor(R.color.ic_launcher_background));
            }
        });



        btn_save_route_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editText_routeDatePicker.getText().toString().length()==0){
                    editText_routeDatePicker.setError("Bu alan boş bırakılamaz");
                    return;
                }
                if(editText_routeName.getText().toString().length()==0){
                    editText_routeName.setError("Bu alan boş bırakılamaz");
                    return;
                }


                current_route = new Route();
                //todo:Travel type should be selectable
                current_route.setTravelType(AppConstants.travelType_walk);
                current_route.setRouteId(UUID.randomUUID().toString());
                current_route.setRouteName(editText_routeName.getText().toString());
                current_route.setRouteDate(editText_routeDatePicker.getText().toString());
                current_route.setUserId(PrefManager.getPref_UserInfo(PrefManager.key_userId, CreateRouteOnMapActivity.this));
                if(routeSettingsDialog.isShowing()){
                    routeSettingsDialog.dismiss();
                }
            }
        });

        btn_cancel_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(routeSettingsDialog.isShowing()) {
                    routeSettingsDialog.dismiss();
                finish();
                startActivity(new Intent(CreateRouteOnMapActivity.this,AppMainActivity.class));
                }
            }
        });

        routeSettingsDialog.setContentView(dialogView);
        routeSettingsDialog.setCancelable(false);
        routeSettingsDialog.getWindow().setLayout(1000,1400);
        //spinnerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        routeSettingsDialog.show();

    }

    public void GetDatePickerEvent(final EditText editText_datePicker){
        editText_datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogView = View.inflate(CreateRouteOnMapActivity.this, R.layout.dialog_date_picker, null);
                final AlertDialog alertDialog = new AlertDialog.Builder(CreateRouteOnMapActivity.this).create();
                final DatePicker datePicker = (DatePicker) dialogView.findViewById(R.id.date_picker);

                dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String date = datePicker.getDayOfMonth() + "-" + datePicker.getMonth() + "-" + datePicker.getYear();
                        editText_datePicker.setText(date);
                        alertDialog.dismiss();
                    }});

                alertDialog.setView(dialogView);
                alertDialog.show();
            }
        });
    }

    public void OptimizeRoute(View view) {

        ProgressDialog progressDialog = new ProgressDialog(CreateRouteOnMapActivity.this);
        progressDialog.setMessage("Rotanız Oluşturuluyor, bu işlem biraz uzun sürebilir");
        progressDialog.create();
        progressDialog.show();
        SQLiteDataProvider sqLiteDataProvider = new SQLiteDataProvider(CreateRouteOnMapActivity.this);
        current_route.setPointList(currentPointList);
        Optimize optimize = new Optimize(CreateRouteOnMapActivity.this,current_route);
        Route optimizedRoute = optimize.GetOptimizedRoute(null);
        if(sqLiteDataProvider.insertRoute(optimizedRoute))
            startActivity(new Intent(CreateRouteOnMapActivity.this,AppMainActivity.class));

    }

}