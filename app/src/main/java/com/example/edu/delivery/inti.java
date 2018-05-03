package com.example.edu.delivery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.medialablk.easygifview.EasyGifView;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class inti extends AppCompatActivity {
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    TextView texto_init, locationnet;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    private LocationCallback mLocationCallback;
    private boolean mRequestingLocationUpdates = true;
    LocationManager manager;
    ImageView load;
    private boolean band = false;
    Drawable drawable;
    Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inti);

        load = findViewById(R.id.loading);
        load.setVisibility(View.GONE);
        texto_init = findViewById(R.id.text_init);
        locationnet = findViewById(R.id.locationnet);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e("status", "0");
            flag=true;
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
    }

    public void lubication(View view) {
        basedatos mDbHelper = new basedatos(inti.this);
        leerubicacion(mDbHelper);
    }

    private void leerubicacion(basedatos mDbHelper) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Log.e("database", String.valueOf(db));
// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                basedatos.FeedEntry._ID,
                basedatos.FeedEntry.COLUMN_LONGITUD,
                basedatos.FeedEntry.COLUMN_LATITUD

        };
        String selection = basedatos.FeedEntry._ID +"=?";
        String[] selectionArgs = { "1" };

        //Cursor c = db.rawQuery(" SELECT * FROM entry Where 'basedatos.FeedEntry._ID=0' ", null);
        Cursor c = db.query(basedatos.FeedEntry.TABLE_NAME,projection,selection, selectionArgs,null,null,null);
        c.moveToFirst();
        Log.e("count", String.valueOf(c.getCount()));
        if(c.getCount()>0){
            if(!c.getString(1).equals("0")){
                //getdata(c.getString(0), c.getString(1));
                Log.e("id1",c.getString(1) +" "+c.getString(2));
                getdata(c.getString(2),c.getString(1));
            }

        }
        db.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    private void onload() {
        drawable = load.getDrawable();
        if (drawable instanceof Animatable) {
            Log.e("entre", "onload");
            ((Animatable) drawable).start();
            load.setVisibility(View.VISIBLE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    private void offload() {
        drawable = load.getDrawable();
        if (drawable instanceof Animatable) {
            Log.e("entre", "offload");
            ((Animatable) drawable).stop();

        }
        ((Animatable) drawable).stop();
        load.setVisibility(View.GONE);
    }

    private void getlocation() {
        if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) && !flag) {
            showAlert();
        }else {
            Log.e("status", "1");
            mLocationCallback = new LocationCallback() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {
                        Log.e("loation", "null");

                    }
                    for (Location location : locationResult.getLocations()) {
                        String latitud = location.convert(location.getLatitude(), 0);
                        String longitud = location.convert(location.getLongitude(), 0);
                        Log.e("customlatitud", String.valueOf(latitud.indexOf(',')));
                        if (latitud.indexOf(',')>=0){
                            latitud = latitud.replace(',', '.');
                            longitud = longitud.replace(',', '.');
                        }

                        locationnet.setText("aproximando a: " + String.valueOf(location.getAccuracy()) + " metros");
                        if (location.getAccuracy() <= 25.0) {
                            Log.e("loation", String.valueOf(location.getAccuracy()));
                            texto_init.setText("obteniendo datos ..");
                            getdata(latitud, longitud);
                            stopLocationUpdates();
                        }
                    }
                }


            };
            createLocationRequest();
            startLocationUpdates();
            texto_init.setText("obteniendo ubicacion");
        }

    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first
        Log.e("onremis","pause");


    }
    @Override
    public void onRestart() {
        super.onRestart();  // Always call the superclass method first
        Log.e("onremis","restar");


    }
    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        Log.e("onremis","onresume");
        if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) && !flag) {
            showAlert();
        }



    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        if(band)
            stopLocationUpdates();

    }
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(100);
        mLocationRequest.setFastestInterval(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
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
        band = false;
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        band = true;
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
                    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                    flag=false;
                }
                    else {
                    Log.e("status","4");
                    finish();

                }
            }
        }
    }
    public void Disable_Certificate_Validation_Java_SSL_Connections() {

// Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }
        };

        // Install the all-trusting trust manager
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            Log.d("test_disable", "OK========");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }

    private void getdata(final String latitudcel, final String longitudcel) {
        Disable_Certificate_Validation_Java_SSL_Connections();

        RequestQueue queue2 = Volley.newRequestQueue(this);
        String url ="https://192.168.43.158:443/empresa.json";
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
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                   errors("Error al conectar con el servidor");
                } else if (error instanceof AuthFailureError) {
                    errors("Error de autenticacion");

                } else if (error instanceof ServerError) {

                    errors("Error interna del servidor");

                } else if (error instanceof NetworkError) {

                    errors("Error de conexion");
                } else if (error instanceof ParseError) {

                    errors("Error de formato de datos");
                }
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
                            Log.e("error", String.valueOf(e));
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

