package com.example.maledettatreest2;

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
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.maledettatreest2.databinding.ActivityMappaBinding;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MappaActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String MAP_LOG = "MAP_LOG";
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 01;
    private static final int duration = Toast.LENGTH_SHORT;

    private FusedLocationProviderClient fusedLocationClient;

    /**
     * per aggioranmento real time
     */
    private LocationCallback locationCallback;
    private boolean requestingLocationUpdates = true;

    private GoogleMap mMap;
    private ActivityMappaBinding binding;

    private boolean primaPosizionePresa;

    /**
     * istanza Model
     */
    MyModel istanza = MyModel.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(MAP_LOG, "onCreate");
        super.onCreate(savedInstanceState);

        binding = ActivityMappaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        primaPosizionePresa = false;

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //richiestaLocation(); //era per current location

        /** per aggiornamento real time */
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Log.d(MAP_LOG, "Nuova posizione ricevuta: " + location.toString());
                    if (!primaPosizionePresa) {
                        LatLng posizioneUtente = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posizioneUtente, 12));
                        primaPosizionePresa = true;
                    }
                }
            }
        };
        checkPermessiPosizione();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(MAP_LOG, "onMapReady");
        mMap = googleMap;
        ArrayList<LatLng> latLong = new ArrayList<>();
        for (Stazione s : istanza.getStazioni()) {
            LatLng marcatore = new LatLng(s.getLatitudine(), s.getLongitudine());
            mMap.addMarker(new MarkerOptions()
                    .position(marcatore)
                    .title(s.getNome())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
            latLong.add(marcatore);
        }

        /** implementazione delle linee (Polylines) */
        Polyline polyline1 = googleMap.addPolyline(new PolylineOptions()
                .clickable(false)
                .addAll(latLong));
        polyline1.setTag("stazioni");

        if (requestingLocationUpdates) {
            startLocationUpdates();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(MAP_LOG, "onResume");

    }

    @Override
    protected void onPause() {
        Log.d(MAP_LOG, "onPause");
        super.onPause();
        fusedLocationClient.removeLocationUpdates(locationCallback);
        requestingLocationUpdates = false;
    }

    /**
     * verifica se i permessi per la posizione sono stati concessi dall'utente
     */
    public boolean checkPermessiPosizione() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //I permessi non sono stati (ancora) concessi
            Log.d(MAP_LOG, "I permessi non sono stati (ancora) concessi");

            //Chiedo i permessi (istruzione sotto)
            Log.d(MAP_LOG, "Chiedo i permessi");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            return false;

        } else {
            //I permessi sono già stati concessi
            Log.d(MAP_LOG, "I permessi sono già stati concessi");
            return true;
        }
    }

/*
    @SuppressLint("MissingPermission")
    public void richiestaCurrentLocation() {
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
                    Log.d(MAP_LOG, "Permessi concessi");
                    //richiestaLocation(); //era per current location
                    startLocationUpdates();
                } else {
                    Log.d(MAP_LOG, "Permessi non concessi");
                    LatLng posizioneTemporanea = new LatLng(istanza.getStazioni().get(0).getLatitudine(), istanza.getStazioni().get(0).getLongitudine());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posizioneTemporanea, 12));
                    Toast toast = Toast.makeText(this, "Non ci sono i permessi di posizione", duration);
                    toast.show();
                }

            }
        }
    }

    @SuppressLint("MissingPermission")
    public void startLocationUpdates() {
        if (checkPermessiPosizione()) {
            requestingLocationUpdates = true;
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setInterval(1000); //in ms.
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
            mMap.setMyLocationEnabled(true);
        }
    }


}