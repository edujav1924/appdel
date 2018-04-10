package com.example.edu.delivery;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;
import com.github.florent37.expansionpanel.ExpansionLayout;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BusquedaActivity extends AppCompatActivity{
    RecyclerView recyclerView;
    ArrayList<String> filter_producto= new ArrayList<>();
    ArrayList<Integer> filter_precio= new ArrayList<>();
    ArrayList<String> pais= new ArrayList<String>();
    ArrayList<Integer> precio = new ArrayList<>();
    MaterialSearchView searchView;
    TextView result_text;
    ListView busqueda;
    customadapterbusqueda adapter3;
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
        final RecyclerAdapter adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
        String[] countryNames = {"Seleccione", "Hamburguesa normal", "Hamburguesa completa", "coca cola", "lomito", "papas fritas", "cerveza", "empanada"};
        String[] subs = {"0", "7000", "10000", "5000", "15000", "5000", "8000", "2000"};
        for (int i = 0; i < countryNames.length; i++) {
           pais.add(countryNames[i]);
           precio.add(Integer.valueOf(subs[i]));
        }
        adapter.setItems(pais);
        adapter.notifyDataSetChanged();
        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                hasSubstring(pais,newText);
                adapter.notifyDataSetChanged();
                adapter.setItems(filter_producto);
                if (filter_producto.isEmpty()){
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

   public boolean hasSubstring(Collection<String> c, String substring) {
        filter_producto.clear();
        filter_precio.clear();
        int i=0;
        for(String s : c) {
            if(s.toLowerCase().contains(substring.toLowerCase())) {
                filter_producto.add(pais.get(i));
                filter_precio.add(precio.get(i));

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
