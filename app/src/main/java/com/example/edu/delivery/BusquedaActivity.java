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
import java.util.Collection;
import java.util.List;

import static android.widget.Toast.LENGTH_LONG;

public class BusquedaActivity extends AppCompatActivity{
    RecyclerView recyclerView;
    ArrayList<String> filter_empresa= new ArrayList<>();
    ArrayList<String> producto= new ArrayList<>();
    ArrayList<Integer> precio = new ArrayList<>();
    MaterialSearchView searchView;
    TextView result_text;
    WebView web;
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
        //web = findViewById(R.id.web);
        //web.getSettings().setJavaScriptEnabled(true);
        //web.getSettings().setBuiltInZoomControls(false);
        //web.loadUrl("https://www.google.com/maps/dir/-25.3189877,-57.5540246/-25.3418079,-57.5143931/@-25.3303479,-57.5405402,15z/data=!3m1!4b1");
        result_text.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.customrecicler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final RecyclerAdapter adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://maps.googleapis.com/maps/api/distancematrix/json?units=meter&origins=-25.301285,-57.564635&destinations=-25.2638806,-57.5120576%7C-25.27008,-57.525027%7C-25.259486,-57.588522%7C-25.314263,-57.459704";
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
                            JSONArray rows = jsono.getJSONArray("rows");
                            JSONObject aux1 = rows.getJSONObject(0);
                            JSONArray elements = aux1.getJSONArray("elements");
                            for (int i=0;i<elements.length();i++){
                                JSONObject a = elements.getJSONObject(i);
                                JSONObject aux3 = a.getJSONObject("distance");
                                distancias.add(aux3.getString("text"));
                                aux3 = a.getJSONObject("duration");
                                tiempo.add(aux3.getString("text"));
                                //Log.e("get", aux3.getString("text"));

                            }
                            dialog.dismiss();
                            adapter.setItems(direcciones,distancias);
                            adapter.notifyDataSetChanged();
                            Log.e("get", String.valueOf(direcciones));
                            Log.e("get", String.valueOf(distancias));
                            Log.e("get", String.valueOf(tiempo));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),
                        "error del servidor",
                        LENGTH_LONG).show();

            }
        });
        queue.add(stringRequest);
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
                hasSubstring(direcciones,newText);
                adapter.notifyDataSetChanged();
                adapter.setItems(filter_empresa,filter_distancias);
                if (filter_empresa.isEmpty()){
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
    public void reciclerviewshow(){

    }
    public boolean hasSubstring(Collection<String> c, String substring) {
    filter_empresa.clear();
    filter_distancias.clear();
    int i=0;
    for(String s : c) {
        if(s.toLowerCase().contains(substring.toLowerCase())) {
            filter_empresa.add(direcciones.get(i));
            filter_distancias.add(distancias.get(i));
        }
        i=i+1;
    }

    return false;
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
