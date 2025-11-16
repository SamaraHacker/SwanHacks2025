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
import android.widget.SeekBar;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jelhackers.icemelt.databinding.ActivityMapsBinding;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Instant;
import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private double radiusMeters;
    public FirebaseFirestore db;
    private POIController poiController;

    private ActivityMapsBinding binding;

    private TextView dropDownImageView;

    private final String[] alertTypes = {
            "ICE RAID"
    };
    private final int[] alertImages = {
            R.drawable.baseline_check_box_outline_blank_24
    };
    private FusedLocationProviderClient fusedLocationClient;
    private LatLng currentLocation;

    private SeekBar distanceControl;
    private TextView distanceControlTxt;

    private ArrayList<POIObject> poiArrayList;
    private POIAdapter adapter;

    RecyclerView recyclerItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);

        FirebaseApp app = FirebaseApp.getInstance();
        String projectId = app.getOptions().getProjectId();
        Log.d("FIREBASE_DEBUG", "Connected to Firestore project: " + projectId);
        radiusMeters = 20000;

        poiController = new POIController();

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

        poiArrayList = new ArrayList<>();
        recyclerItems = findViewById(R.id.recyclerItems);
        adapter = new POIAdapter(poiArrayList, this, position -> {
            // Get the clicked POI
            POIObject poi = poiArrayList.get(position);
            LatLng poiLatLng = new LatLng(poi.getLat(), poi.getLon());

            // Move camera to POI
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(poiLatLng, 18));
        });

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);

        recyclerItems.setLayoutManager(layoutManager);
        recyclerItems.setAdapter(adapter);
        recyclerItems.smoothScrollToPosition(poiArrayList.size());

        // "on click" operations to be performed
        dropDownImageView.setOnClickListener(v -> {
            //Bring up drop down
        });

        distanceControlTxt = findViewById(R.id.distanceRangeTxt);
        distanceControlTxt.setText("Range: " + (int)(radiusMeters / 1000)+ "km");
        distanceControl = findViewById(R.id.distanceSelect);
        distanceControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double minDistance = 100;
                double maxDistance = 2_000_000;
                radiusMeters = minDistance * Math.pow(maxDistance / minDistance, progress / 100.0);
                if((int) (radiusMeters / 1000) < 1){
                    distanceControlTxt.setText("Range: " + (int)(radiusMeters) + "m");
                } else {
                    distanceControlTxt.setText("Range: " + (int)(radiusMeters / 1000)+ "km");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mMap.clear();
                poiArrayList.clear();
                adapter.notifyDataSetChanged();
                mMap.addMarker(new MarkerOptions().position(currentLocation).title("You are here"));
                retrieveAllMarkers();
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
        //Default location
        currentLocation = new LatLng(-34, 151); // fallback
        getCurrentLocation();
        // Add a marker in Sydney and move the camera

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
                currentLocation = userLatLng;
            } else {
                userLatLng = new LatLng(-34, 151); // fallback
                Log.d("LOCATION", "Location null, using fallback");
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15));

            mMap.addMarker(new MarkerOptions().position(userLatLng).title("You are here"));
            retrieveAllMarkers();
        });
    }


    public void retrieveAllMarkers(){
        //Get all POIS and add them to the array list
        poiController.getNearbyPois(currentLocation.latitude, currentLocation.longitude, radiusMeters, new POIController.POIListener() {
            @Override
            public void onPoisRetrieved(ArrayList<POIObject> pois) {
                for (POIObject poi : pois) {
                    if (poi != null) {
                        Log.d("NearbyPOI", "POI: " + poi.getLocationName());
                        Marker marker = mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(poi.getLat(), poi.getLon()))
                                .title(poi.getLocationName())
                                .snippet(alertTypes[poi.getAlertType()])
                                .icon(bitmapDescriptorFromVector(alertImages[poi.getAlertType()])));
                        marker.hideInfoWindow();
                        poiArrayList.add(poi);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("NearbyPOI", "Failed to get nearby POIs", e);
            }
        });
        //Iterate through the arraylist and add a pointer for each POI
        LatLng sydneyLatLong = new LatLng(-34, 151);
        Marker sydney = mMap.addMarker(
                new MarkerOptions()
                        .position(sydneyLatLong)
                        .title("Sydney")
                        .snippet(alertTypes[0])
                        .icon(bitmapDescriptorFromVector(alertImages[0])));
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
            String locationName = locationNameTxt.getText().toString();
            String description = descriptionTxt.getText().toString();
            int typeIndex = reportTypes.getSelectedItemPosition();

            poiController.createPoi((float)latlng.latitude, (float)latlng.longitude, typeIndex, locationName, description);
            dialog.dismiss();


            // Add marker immediately
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title(locationName)
                    .snippet(alertTypes[typeIndex])
                    .icon(bitmapDescriptorFromVector(alertImages[typeIndex]))
            );
            marker.hideInfoWindow();

            Log.d("REPORT_SUBMIT", "User submitted a report for: " + locationNameTxt);
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
        // Get the POI and fill in the data based on that
        poiController.getPoi(marker.getTitle(), new POIController.POICallback() {
            @Override
            public void onResult(POIObject poi) {
                if (poi != null) {
                    txtLocation.setText(marker.getTitle());
                    txtDescription.setText(poi.getLocationDescription());
                    txtReportCount.setText(poi.getReportCount() + " Reports");
                    long timeInSeconds = Instant.now().getEpochSecond() - poi.getStartTime();
                    if(timeInSeconds / 60 /3600 < 1){
                        txtAge.setText(timeInSeconds / 60 + " min(s) ago");
                    } else {
                        txtAge.setText(timeInSeconds / 60 / 3600 + " hr(s) ago");
                    }
                } else {
                    // Handle not found
                }
            }

            @Override
            public void onError(Exception e) {
                // Handle error
            }
        });
        // Create & show dialog
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(popupView)
                .create();

        dialog.show();

        // Button click logic
        btnReport.setOnClickListener(v -> {
            Log.d("REPORT_CLICK", "User clicked info window at: " + marker.getTitle());
            //Delete the POI
            poiController.deletePoi(marker.getTitle());
            marker.remove();
            dialog.dismiss();
        });

        btnConfirmPOI.setOnClickListener(v -> {
            Log.d("REPORT_CLICK", "User clicked info window at: " + marker.getTitle());
            //Update count of POI
            poiController.incrementReportCount(marker.getTitle());
            dialog.dismiss();
        });
    }
}