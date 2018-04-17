package com.example.edu.delivery;

import android.content.Intent;
import android.net.Uri;
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

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class BusquedaActivity extends AppCompatActivity{
    JSONArray jsono;
    RecyclerView recyclerView;
    ArrayList<String> latitud= new ArrayList<>();
    ArrayList<String> longitud = new ArrayList<String>();
    MaterialSearchView searchView;
    TextView result_text;
    WebView web;
    RecyclerAdapter adapter;
    ArrayList<String> filter_distancias=new ArrayList<>();
    ArrayList<String> direcciones=new ArrayList<>();
    ArrayList<String> tiempo=new ArrayList<>();
    ArrayList<String> distancias=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);
        distancias= getIntent().getExtras().getStringArrayList("distancias");
        tiempo = getIntent().getExtras().getStringArrayList("tiempo");
        direcciones = getIntent().getExtras().getStringArrayList("direcciones");
        try {
            jsono = new JSONArray(getIntent().getExtras().getString("json"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Toolbar toolbar2 = findViewById(R.id.toolbar2);
        toolbar2.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        result_text = findViewById(R.id.result_text);
        result_text.setVisibility(View.GONE);
        recyclerView = findViewById(R.id.customrecicler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        adapter.setItems(jsono,distancias);
        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (hasSubstring(jsono,newText)){
                    result_text.setText(("No se encontraron resultados"));
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
    MenuItem loc = menu.findItem(R.id.location);
    loc.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            Log.e("sd","pulsado");
            Uri uri = Uri.parse("https://www.google.com/maps/d/viewer?mid=136OvfGkpWnvZP6G1uDAmw_AWx-1JNHqJ&ll=-25.28863824629068%2C-57.51148585000004&z=15");
            Intent newintent = new Intent(Intent.ACTION_VIEW,uri);
            startActivity(newintent);
            return false;
        }
    });
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
