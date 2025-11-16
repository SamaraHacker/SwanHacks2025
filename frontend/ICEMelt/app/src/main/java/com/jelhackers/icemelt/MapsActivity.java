package com.jelhackers.icemelt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jelhackers.icemelt.databinding.ActivityMapsBinding;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    private TextView dropDownImageView;

    private final String[] alertTypes = {
            "ICE RAID"
    };
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng currentLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Assigning ID of textView2 to a variable
        dropDownImageView = findViewById(R.id.textView2);

        // "on click" operations to be performed
        dropDownImageView.setOnClickListener(v -> {
            //Bring up drop down
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        //Location
        getCurrentLocation();
        // Add a marker in Sydney and move the camera

        retrieveAllMarkers();

        mMap.setOnMarkerClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMapClickListener(this::mapClicked);
    }
    // Function to get the current location
    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    44
            );
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            LatLng userLatLng;

            if (location != null) {
                userLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                Log.d("LOCATION", "User location: " + userLatLng);
            } else {
                userLatLng = new LatLng(-34, 151); // fallback
                Log.d("LOCATION", "Location null, using fallback");
            }

            // MOVE CAMERA HERE ✔
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));

            // OPTIONAL: Add marker for user location
            mMap.addMarker(new MarkerOptions().position(userLatLng).title("You are here"));
        });
    }


    public void retrieveAllMarkers(){
        LatLng sydneyLatLong = new LatLng(-34, 151);
        Marker sydney = mMap.addMarker(
                new MarkerOptions()
                        .position(sydneyLatLong)
                        .title("Sydney")
                        .snippet("ICE RAID")
                        .icon(bitmapDescriptorFromVector(R.drawable.baseline_check_box_outline_blank_24)));
        sydney.hideInfoWindow();
    }

    private BitmapDescriptor bitmapDescriptorFromVector(int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(this, vectorResId);
        if (vectorDrawable == null) {
            Log.e("MAP_ICON", "Drawable not found: " + vectorResId);
            return BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        }

        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(
                vectorDrawable.getIntrinsicWidth(),
                vectorDrawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888
        );
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
    public void mapClicked(LatLng latlng)
    {
        Log.d("MAP_CLICK", "User clicked map at: " + latlng);

        View popupView = getLayoutInflater().inflate(R.layout.popup_add_poi, null);

        //Sets the spinner
        Spinner reportTypes = popupView.findViewById(R.id.type_spinner);
        EditText locationNameTxt = popupView.findViewById(R.id.location_name_txt);
        EditText descriptionTxt = popupView.findViewById(R.id.description_txt);
        ImageButton btnSubmit = popupView.findViewById(R.id.report_submit);
        ImageButton btnCancel = popupView.findViewById(R.id.report_cancel);

        //Sets the spinner
        ArrayAdapter<String> adapterUnits = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                alertTypes
        );
        adapterUnits.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportTypes.setAdapter(adapterUnits);


        // Create & show dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(popupView)
                .create();

        dialog.show();

        // Button click logic
        btnSubmit.setOnClickListener(v -> {
            Log.d("REPORT_SUBMIT", "User submitted a report for: " + locationNameTxt);

            String type = reportTypes.getSelectedItem().toString();
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> {
            Log.d("REPORT_CANCEL_SUBMIT", "User canceled a report " + locationNameTxt);
            dialog.dismiss();
        });

    }
    /** Called when the user clicks a marker. */
    @Override
    public boolean onMarkerClick(Marker marker) {

        Log.d("MARKER_CLICK", "User clicked marker at: " + marker.getTitle());
        marker.showInfoWindow();
        return false;
    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        Log.d("INFO_CLICKED", "User clicked info window at: " + marker.getTitle());
        //Popup
        // Inflate custom layout
        View popupView = getLayoutInflater().inflate(R.layout.popup_location, null);

        // Get references to UI elements
        TextView txtLocation = popupView.findViewById(R.id.txtLocation);
        TextView txtAge = popupView.findViewById(R.id.txtAge);
        TextView txtDescription = popupView.findViewById(R.id.txtDescription);
        TextView txtReportCount = popupView.findViewById(R.id.txtReportCount);
        ImageButton btnReport = popupView.findViewById(R.id.reportPOI);
        ImageButton btnConfirmPOI = popupView.findViewById(R.id.confirmPOI);
        // Put your dynamic data here
        txtLocation.setText(marker.getTitle());
        txtAge.setText("Age: 27");
        txtDescription.setText("Description: Cool place!");
        txtReportCount.setText("20 Reports");

        // Create & show dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(popupView)
                .create();

        dialog.show();

        // Button click logic
        btnReport.setOnClickListener(v -> {
            Log.d("REPORT_CLICK", "User clicked info window at: " + marker.getTitle());
            dialog.dismiss();
        });

        btnConfirmPOI.setOnClickListener(v -> {
            Log.d("REPORT_CLICK", "User clicked info window at: " + marker.getTitle());
            dialog.dismiss();
        });
    }
}