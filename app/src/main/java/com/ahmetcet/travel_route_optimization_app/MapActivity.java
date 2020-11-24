package com.ahmetcet.travel_route_optimization_app;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(my_loc, 18.0f));

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(final LatLng latLng) {
                //Intent myIntent = new Intent(view.getContext(), agones.class);
                //startActivityForResult(myIntent, 0);


                final Dialog spinnerDialog = new Dialog(MapActivity.this);


                LayoutInflater inflater = MapActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_route_constraints, null);
                RatingBar ratingBar_priority = (RatingBar) dialogView.findViewById(R.id.ratingBar_priority);

                Button button_setPoint = (Button) dialogView.findViewById(R.id.button_setPoint);
                final EditText editText_pointName = (EditText) dialogView.findViewById(R.id.editText_pointExplanation);

                button_setPoint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(editText_pointName.getText().toString().length()== 0){
                            editText_pointName.setError("Konum ismi girmelisiniz");
                            return;
                        }
                        mMap.addMarker(new MarkerOptions().position(latLng)
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                                .title(editText_pointName.getText().toString())
                        );
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));

                        if(spinnerDialog.isShowing())
                            spinnerDialog.dismiss();
                    }
                });

                spinnerDialog.setContentView(dialogView);
                spinnerDialog.setCancelable(true);
                spinnerDialog.getWindow().setLayout(900,1000);
                //spinnerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                spinnerDialog.show();

            }
        });
    }

    public void OptimizeRoute(View view) {
        ProgressDialog progressDialog = new ProgressDialog(MapActivity.this);
        progressDialog.setMessage("Rotanız Oluşturuluyor...");
        progressDialog.create();
        progressDialog.show();
    }
}