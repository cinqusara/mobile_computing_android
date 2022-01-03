package com.example.maledettatreest2;

import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.maledettatreest2.databinding.ActivityMappaBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MappaActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String MTE_LOG = "MTE_LOG";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 01;

    private FusedLocationProviderClient fusedLocationClient;
    //per aggioranmento real time
    private LocationCallback locationCallback;
    private boolean requestingLocationUpdates = true;

    private GoogleMap mMap;
    private ActivityMappaBinding binding;


    /**istanza Model */
    MyModel istanza = MyModel.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMappaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //richiestaLocation(); //era per current location

        //per aggioranmento real time
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Log.d("Location", "New Location received: " + location.toString());

                    LatLng posizioneUtente = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posizioneUtente, 12));
                }
            };
        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ArrayList<LatLng> latLong=new ArrayList<>();
        for (Stazione s : istanza.getStazioni()) {
            LatLng marcatore = new LatLng(s.getLatitudine(), s.getLongitudine());
            mMap.addMarker(new MarkerOptions().position(marcatore).title(s.getNome()));
            latLong.add(marcatore);
        }


        /** implementazione delle linee (Polylines) */
        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(false)
                .addAll(latLong));
        polyline1.setTag("stazioni");


    }

    @Override
    protected void onResume() { super.onResume();
        if (requestingLocationUpdates) {
            startLocationUpdates(); }
    }

    @Override
    protected void onPause() {
        super.onPause(); fusedLocationClient.removeLocationUpdates(locationCallback); requestingLocationUpdates = false;
    }

    public boolean checkPermessiPosizione() {
        //verifico se utente ha gia concesso i permessi di posizione
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //I permessi non sono stati (ancora) concessi
            Log.d(MTE_LOG, "I permessi non sono stati (ancora) concessi");

            //Chiedo i permessi (istruzione sottto)
            Log.d(MTE_LOG, "Chiedo i permessi");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return false;

        } else {
            //I permessi sono già stati concessi
            Log.d(MTE_LOG, "I permessi sono già stati concessi");
            return true;
        }
    }


    /*
    @SuppressLint("MissingPermission")
    public void richiestaLocation() {
        if(checkPermessiPosizione() ) {
            fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, new CancellationToken() {
                @Override
                public boolean isCancellationRequested() {
                    return false;
                }

                @NonNull
                @Override
                public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                    return null;
                }
            }).addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got current known location. In some rare situations this can be null.
                    if (location != null) {
                        mMap.setMyLocationEnabled(true);
                    } else {
                        Log.d(MTE_LOG, "Current Known location not available");
                    }
                }
            });
        }
    }


     */


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    Log.d(MTE_LOG, "Now the permission is granted");
                    //richiestaLocation(); //era per current location
                    startLocationUpdates();
                } else {
                    Log.d(MTE_LOG, "Permission still not granted");
                    LatLng posizioneTemporanea = new LatLng(istanza.getStazioni().get(0).getLatitudine(), istanza.getStazioni().get(0).getLongitudine());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posizioneTemporanea, 12));
                }
                return;
            }
        }
    }

    @SuppressLint("MissingPermission")
    public void startLocationUpdates() {
        if(checkPermessiPosizione() ) {
            requestingLocationUpdates = true;
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(1000); //in ms.
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            mMap.setMyLocationEnabled(true);
        }
    }


}