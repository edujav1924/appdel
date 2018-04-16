package com.example.edu.delivery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.medialablk.easygifview.EasyGifView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import  com.tuyenmonkey.mkloader.*;

public class inti extends AppCompatActivity {

    TextView texto_init;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inti);
        texto_init = findViewById(R.id.text_init);
        texto_init.setText("obteniendo datos...");
        RequestQueue queue2 = Volley.newRequestQueue(this);
        String url ="http://192.168.43.158:8000/empresa.json";
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
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
                            getdirections(jsono,latitud,longitud);
                            /*for (int i=0;i<jsono.length();i++){
                                Log.e("value", String.valueOf(jsono.getJSONObject(i).getJSONArray("productos")));
                                for (int j=0;j<jsono.length();j++){
                                    Log.e("value", String.valueOf(jsono.getJSONObject(i).getJSONArray("productos").getJSONObject(j).getString("producto")));
                                }
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                            texto_init.setText("Hubo un error al recibir datos del servidor");
                            Log.e("Error1","Error1");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                findViewById(R.id.MKLoader).setVisibility(View.GONE);
                texto_init.setText("no fue posible establecer contacto con el servidor");

            }
        });
        queue2.add(stringRequest2);
    }

    private void getdirections(JSONArray jsono, List<String> latitud, List<String> longitud) {
        texto_init.setText("obteniendo localizacion...");

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://maps.googleapis.com/maps/api/distancematrix/json?units=meter&origins=-25.267376,-57.492435&destinations=";
        for (int j=0;j<latitud.size();j++){
            url = url + longitud.get(j) +"%2C"+latitud.get(j)+"%7C";
        }
        Log.e("url",url);
        //String url ="http://maps.googleapis.com/maps/api/distancematrix/json?units=meter&origins=40.6655101,-73.89188969999998&destinations=40.6905615%2C-73.9976592";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
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
                                findViewById(R.id.MKLoader).setVisibility(View.GONE);
                                texto_init.setText("Finalizado");

                            }
                        } catch (JSONException e) {
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
}

