package com.example.edu.delivery;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.medialablk.easygifview.EasyGifView;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class inti extends AppCompatActivity {
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    TextView texto_init, locationgps, locationnet;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    private LocationCallback mLocationCallback;
    private boolean mRequestingLocationUpdates = true;
    LocationManager manager;
    ImageView load;
    Drawable drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inti);
        load = findViewById(R.id.loading);
        load.setVisibility(View.GONE);
        texto_init = findViewById(R.id.text_init);
        locationnet = findViewById(R.id.locationnet);
        locationgps = findViewById(R.id.locationgps);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e("status", "0");

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        }

    }

    private void errors(String a) {
        texto_init.setText(a);


    }

    public void nubication(View view) {
        getlocation();
        onload();
        findViewById(R.id.lastu).setEnabled(false);
    }

    public void lubication(View view) {
        findViewById(R.id.newu).setEnabled(false);
        lastlocation();
    }

    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    private void onload() {
        drawable = load.getDrawable();
        if (drawable instanceof Animatable) {
            Log.e("entre", "entre");
            ((Animatable) drawable).start();
            load.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    private void offload() {
        drawable = load.getDrawable();
        if (drawable instanceof Animatable) {
            Log.e("entre", "entre");
            ((Animatable) drawable).stop();

        }
        ((Animatable) drawable).stop();
        load.setVisibility(View.GONE);
    }

    private void getlocation() {
        Log.e("status", "1");
        mLocationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    Log.e("loation", "null");

                }
                for (Location location : locationResult.getLocations()) {
                    String latitud = location.convert(location.getLatitude(), 0);
                    String longitud = location.convert(location.getLongitude(), 0);

                    locationgps.setText(latitud + " " + longitud);
                    locationnet.setText(String.valueOf(location.getAccuracy()));
                    if (location.getAccuracy() <= 25.0) {
                        Log.e("loation", String.valueOf(location.getAccuracy()));
                        texto_init.setText("obteniendo datos ..");
                        getdata(latitud, longitud);
                        stopLocationUpdates();
                    }
                }
            }

            ;
        };
        createLocationRequest();
        startLocationUpdates();
        texto_init.setText("obteniendo ubicacion");


    }

    private void lastlocation() {
        Log.e("lastlocation","kasasa");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {

                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            String latitud = location.convert(location.getLatitude(), 0);
                            String longitud = location.convert(location.getLongitude(), 0);
                            Log.e("lastlocation", String.valueOf(location));
                            getdata(latitud, longitud);
                        }
                        else {
                            getlocation();
                            onload();

                        }
                    }
                });
    }
    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first


    }
    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            showAlert();
        }
        else {

        }


    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100);
        mLocationRequest.setFastestInterval(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
        task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                Log.e("entre","entre");
                if (!locationSettingsResponse.getLocationSettingsStates().isGpsUsable()){
                    Log.e("soy yo","1");

                }
            }
        });
        task.addOnFailureListener(this, new OnFailureListener() {
            @RequiresApi(api = Build.VERSION_CODES.DONUT)
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    Log.e("gpssd","hola");
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(inti.this,
                                100);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("status","3");

                    getlocation();
                }
                    else {
                    Log.e("status","4");
                    finish();

                }
            }
        }
    }

    private void getdata(final String latitudcel, final String longitudcel) {
        RequestQueue queue2 = Volley.newRequestQueue(this);
        String url ="http://192.168.43.158:8000/empresa.json";
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.DONUT)
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray jsono = new JSONArray(response);
                            List<String> latitud=new ArrayList<>();
                            List<String> longitud=new ArrayList<>();
                            for (int i=0;i<jsono.length();i++){
                                latitud.add(jsono.getJSONObject(i).getString("latitud"));
                                longitud.add(jsono.getJSONObject(i).getString("longitud"));
                            }
                            getdirections(jsono,latitud,longitud,latitudcel,longitudcel);
                            /*for (int i=0;i<jsono.length();i++){
                                Log.e("value", String.valueOf(jsono.getJSONObject(i).getJSONArray("productos")));
                                for (int j=0;j<jsono.length();j++){
                                    Log.e("value", String.valueOf(jsono.getJSONObject(i).getJSONArray("productos").getJSONObject(j).getString("producto")));
                                }
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                           errors("Hubo un error al recibir datos del servidor");
                            Log.e("Error1","Error1");
                            offload();
                        }
                    }
                }, new Response.ErrorListener() {
            @RequiresApi(api = Build.VERSION_CODES.DONUT)
            @Override
            public void onErrorResponse(VolleyError error) {
                errors("no fue posible establecer contacto con el servidor");
                offload();
            }
        });
        queue2.add(stringRequest2);
    }

    private void getdirections(final JSONArray jsono, List<String> latitud, List<String> longitud, final String latitudcel, final String longitudcel) {
        texto_init.setText("obteniendo localizacion...");

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://maps.googleapis.com/maps/api/distancematrix/json?units=meter&origins="+latitudcel+","+longitudcel+"&destinations=";
        for (int j=0;j<latitud.size();j++){
            url = url + longitud.get(j) +"%2C"+latitud.get(j)+"%7C";
        }
        Log.e("url",url);
        //String url ="http://maps.googleapis.com/maps/api/distancematrix/json?units=meter&origins=40.6655101,-73.89188969999998&destinations=40.6905615%2C-73.9976592";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.DONUT)
                    @Override
                    public void onResponse(String response) {
                        List<String> direcciones=new ArrayList<>();
                        List<String> distancias=new ArrayList<>();
                        List<String> tiempo=new ArrayList<>();
                        try {
                            JSONObject other = new JSONObject(response);
                            JSONArray arrayLocales = other.getJSONArray("destination_addresses");
                            for (int i=0;i<arrayLocales.length();i++){
                                direcciones.add(arrayLocales.getString(i));
                            }
                            JSONArray milocation = other.getJSONArray("origin_addresses");
                            JSONArray elements = other.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");
                            for (int i=0;i<elements.length();i++){
                                JSONObject aux3 =elements.getJSONObject(i).getJSONObject("distance");
                                distancias.add(aux3.getString("text"));
                                aux3 = elements.getJSONObject(i).getJSONObject("duration");
                                tiempo.add(aux3.getString("text"));
                                //Log.e("get", aux3.getString("text"));
                            }
                            texto_init.setText("Finalizado");
                            desarrollo(direcciones,distancias,tiempo,jsono,longitudcel,latitudcel);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            offload();
                            texto_init.setText("Hubo un error al recibir datos de localizacion");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                texto_init.setText("no fue posible obtener datos de localizacion");
                offload();
            }
        });
        queue.add(stringRequest);
    }

    private void desarrollo(List<String> direcciones, List<String> distancias, List<String> tiempo, JSONArray jsono, String longitudcel, String latitudcel) {
        Intent intent = new Intent(inti.this, BusquedaActivity.class);
        intent.putExtra("direcciones", (Serializable) direcciones);
        intent.putExtra("distancias", (Serializable) distancias);
        intent.putExtra("tiempo", (Serializable) tiempo);
        intent.putExtra("json", String.valueOf(jsono));
        intent.putExtra("latitud", latitudcel);
        intent.putExtra("longitud",longitudcel);
        //intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        ActivityCompat.finishAffinity(this);
        startActivity(intent);


    }
    private void showAlert() {
        new FancyAlertDialog.Builder(this)
                .setTitle("Localizacion")
                .setBackgroundColor(Color.parseColor("#009933"))  //Don't pass R.color.colorvalue
                .setMessage("Para poder brindarle la mejor atencion necesitamos que active su GPS")
                .setNegativeBtnText("Salir")
                .setPositiveBtnBackground(Color.parseColor("#009933"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("Configuracion")
                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                .setAnimation(Animation.SLIDE)
                .isCancellable(true)
                .setIcon(R.drawable.ic_gps, Icon.Visible)
                .OnPositiveClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .OnNegativeClicked(new FancyAlertDialogListener() {
                    @Override
                    public void OnClick() {
                        finish();
                    }
                })
                .build();


    }
}

