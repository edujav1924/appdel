package com.example.edu.delivery;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class BusquedaActivity extends AppCompatActivity{
    JSONArray jsono;
    RecyclerView recyclerView;
    ArrayList<String> latitud= new ArrayList<>();
    ArrayList<String> longitud = new ArrayList<String>();
    MaterialSearchView searchView;
    TextView result_text;
    WebView web;
    RecyclerAdapter adapter;
    List<String> filter_distancias=new ArrayList<>();
    List<String> direcciones=new ArrayList<>();
    List<String> tiempo=new ArrayList<>();
    List<String> distancias=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);
        Toolbar toolbar2 = findViewById(R.id.toolbar2);
        toolbar2.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        result_text = findViewById(R.id.result_text);
        result_text.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.customrecicler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
        getdata();

        /*String[] countryNames = {"Seleccione", "Hamburguesa normal", "Hamburguesa completa", "coca cola", "lomito", "papas fritas", "cerveza", "empanada"};
        String[] subs = {"0", "7000", "10000", "5000", "15000", "5000", "8000", "2000"};
        for (int i = 0; i < di.length; i++) {
            producto.add(countryNames[i]);
            precio.add(Integer.valueOf(subs[i]));
        }*/
        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (hasSubstring(jsono,newText)){
                    result_text.setText("No se encontraron resultados");
                    result_text.setVisibility(View.VISIBLE);
                }
                else {
                    result_text.setVisibility(View.GONE);
                }
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
    }

    public void getdata() {
        RequestQueue queue2 = Volley.newRequestQueue(this);
        String url ="http://192.168.43.158:8000/empresa.json";
        final ProgressDialog dialog2 = ProgressDialog.show(this, "Descargando datos",
                "Cargando", true);
        dialog2.show();
        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog2.dismiss();
                        try {
                            jsono = new JSONArray(response);

                            for (int i=0;i<jsono.length();i++){
                                latitud.add(jsono.getJSONObject(i).getString("latitud"));
                                longitud.add(jsono.getJSONObject(i).getString("longitud"));
                            }
                            getdirections(jsono);
                            /*for (int i=0;i<jsono.length();i++){
                                Log.e("value", String.valueOf(jsono.getJSONObject(i).getJSONArray("productos")));
                                for (int j=0;j<jsono.length();j++){
                                    Log.e("value", String.valueOf(jsono.getJSONObject(i).getJSONArray("productos").getJSONObject(j).getString("producto")));
                                }
                            }*/
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Error1","Error1");
                            dialog2.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog2.dismiss();
                Toast.makeText(getApplicationContext(),
                        "error del servidor",
                        LENGTH_LONG).show();
            }
        });
        queue2.add(stringRequest2);
    }
    public void getdirections(final JSONArray other) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://maps.googleapis.com/maps/api/distancematrix/json?units=meter&origins=-25.267376,-57.492435&destinations=";
        for (int j=0;j<latitud.size();j++){
            url = url + longitud.get(j) +"%2C"+latitud.get(j)+"%7C";
        }
        Log.e("url",url);
        //String url ="http://maps.googleapis.com/maps/api/distancematrix/json?units=meter&origins=40.6655101,-73.89188969999998&destinations=40.6905615%2C-73.9976592";
        final ProgressDialog dialog = ProgressDialog.show(this, "Descargando datos del Servidor",
                "Cargando", true);
        dialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsono = new JSONObject(response);
                            JSONArray arrayLocales = jsono.getJSONArray("destination_addresses");
                            for (int i=0;i<arrayLocales.length();i++){
                                direcciones.add(arrayLocales.getString(i));
                            }
                            JSONArray milocation = jsono.getJSONArray("origin_addresses");
                            JSONArray elements = jsono.getJSONArray("rows").getJSONObject(0).getJSONArray("elements");
                            for (int i=0;i<elements.length();i++){
                                JSONObject aux3 =elements.getJSONObject(i).getJSONObject("distance");
                                distancias.add(aux3.getString("text"));
                                aux3 = elements.getJSONObject(i).getJSONObject("duration");
                                tiempo.add(aux3.getString("text"));
                                //Log.e("get", aux3.getString("text"));

                            }
                            adapter.setItems(other,distancias);
                            adapter.notifyDataSetChanged();
                            dialog.dismiss();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("Error1","Error1");
                            dialog.dismiss();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error2","Error2");

                dialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        "error del servidor",
                        LENGTH_LONG).show();

            }
        });
        queue.add(stringRequest);
    }

    public boolean hasSubstring(JSONArray c, String substring) {
        JSONArray customjson = new JSONArray();
        for (int i=0;i<c.length();i++){
            try {
                if( c.getJSONObject(i).getString("empresa").toLowerCase().contains(substring.toLowerCase())) {
                    customjson.put(c.getJSONObject(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.setItems(customjson, distancias);
        adapter.notifyDataSetChanged();
        if(customjson.length()>0){
            return false;
        }else {
            return true;
        }
    }
    @Override
    public void onBackPressed() {
    if (searchView.isSearchOpen()) {
        searchView.closeSearch();
    } else {
        super.onBackPressed();
    }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.mymenu, menu);
    MenuItem item = menu.findItem(R.id.action_search);
    searchView.setMenuItem(item);

    return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
        return true;
    }
    return super.onOptionsItemSelected(item);
    }

}
