package com.example.edu.delivery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

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
    private FusedLocationProviderClient mFusedLocationClient;
    LocationRequest mLocationRequest;
    TextView texto_init, locationnet;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    private LocationCallback mLocationCallback;
    LocationManager manager;
    private boolean band = false;
    Drawable drawable;
    Boolean flag = false;
    View spin;
    Button last_ubication;
    private String base_latitud,base_longitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inti);
        spin = findViewById(R.id.spin_kit);
        spin.setVisibility(View.GONE);
        texto_init = findViewById(R.id.text_init);
        locationnet = findViewById(R.id.locationnet);
        last_ubication = findViewById(R.id.lastu);
        last_ubication.setVisibility(View.GONE);
        Log.e("leleef","df");
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
            Log.e("status", "0");

            basedatos mDbHelper = new basedatos(inti.this);
            Log.e("leerubicatiob","si");
            leerubicacion(mDbHelper);
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        }

    }

    private void errors(String a) {
        texto_init.setText(a);
        locationnet.setVisibility(View.GONE);
        spin.setVisibility(View.GONE);

    }

    public void nubication(View view) {
        getlocation();
    }

    public void lubication(View view) {
        spin.setVisibility(View.VISIBLE);
        getdata(base_latitud,base_longitud);
    }
    private void texto_inicio(String texto){
        texto_init.setText(texto);
    }
    private void leerubicacion(basedatos mDbHelper) {

        SQLiteDatabase db = mDbHelper.getReadableDatabase();
// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                basedatos.FeedEntry._ID,
                basedatos.FeedEntry.COLUMN_LONGITUD,
                basedatos.FeedEntry.COLUMN_LATITUD

        };
        String selection = basedatos.FeedEntry._ID +"=?";
        String[] selectionArgs = { "1" };

        Cursor c = db.query(basedatos.FeedEntry.TABLE_NAME,projection,selection, selectionArgs,null,null,null);
        c.moveToFirst();
        Log.e("valor de C", String.valueOf(c.getCount()));
        if(c.getCount()>0){
            if(!c.getString(1).equals("0")){
                base_latitud = c.getString(2);
                base_longitud = c.getString(1);
                //getdata(c.getString(2),c.getString(1));
                last_ubication.setVisibility(View.VISIBLE);
            }
        }else {
            Log.e("aca","aca");
        }
        db.close();
    }


    private void getlocation() {
        texto_inicio("Obteniendo localizacion");
        spin.setVisibility(View.VISIBLE);

        if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) && !flag) {
            showAlert();
        }else {
            Log.e("status", "1");
            mLocationCallback = new LocationCallback() {

                @SuppressLint("SetTextI18n")
                @Override
                public void onLocationResult(LocationResult locationResult) {
                    if (locationResult == null) {

                    }
                    for (Location location : locationResult.getLocations()) {
                        String latitud = location.convert(location.getLatitude(), 0);
                        String longitud = location.convert(location.getLongitude(), 0);
                        if (latitud.indexOf(',')>=0){
                            latitud = latitud.replace(',', '.');
                            longitud = longitud.replace(',', '.');
                        }

                        locationnet.setText("aproximando a: " + String.valueOf(location.getAccuracy()) + " metros");
                        if (location.getAccuracy() <= 25.0) {
                            stopLocationUpdates();
                            getdata(latitud, longitud);

                        }
                    }
                }


            };
            createLocationRequest();
            startLocationUpdates();
        }

    }

    @Override
    public void onPause() {
        super.onPause();  // Always call the superclass method first


    }
    @Override
    public void onRestart() {
        super.onRestart();  // Always call the superclass method first


    }
    @RequiresApi(api = Build.VERSION_CODES.DONUT)
    @Override
    public void onResume() {
        super.onResume();
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
                    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                    flag=false;
                }
                    else {
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
        texto_inicio("obteniendo datos");
        Disable_Certificate_Validation_Java_SSL_Connections();
        RequestQueue queue2 = Volley.newRequestQueue(this);
        String url ="https://delivery.simplelectronica.com:443/empresa.json";
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
            }
        });
        queue2.add(stringRequest2);
    }

    private void getdirections(final JSONArray jsono, List<String> latitud, List<String> longitud, final String latitudcel, final String longitudcel) {

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
                            desarrollo(direcciones,distancias,tiempo,jsono,longitudcel,latitudcel);
                        } catch (JSONException e) {
                            Log.e("error", String.valueOf(e));
                            e.printStackTrace();
                            texto_init.setText("Hubo un error al recibir datos de localizacion");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                texto_init.setText("no fue posible obtener datos de localizacion");
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

