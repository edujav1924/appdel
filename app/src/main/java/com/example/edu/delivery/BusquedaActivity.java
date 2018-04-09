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
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;
import com.github.florent37.expansionpanel.ExpansionLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BusquedaActivity extends AppCompatActivity{
        RecyclerView recyclerView;
        ArrayList<String> pais= new ArrayList<String>();
        ArrayList<Integer> precio = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);
        Toolbar toolbar2 = findViewById(R.id.toolbar2);
        TextView mTitle =  toolbar2.findViewById(R.id.toolbar_title);
        toolbar2.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar2);
        mTitle.setText(toolbar2.getTitle());
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
        //fill with empty objects

        adapter.setItems(pais);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mymenu, menu);
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
class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {
    private final List<String> list = new ArrayList<>();
    private static final int LAYOUT = R.layout.reciclercell;

    private final ExpansionLayoutCollection expansionsCollection = new ExpansionLayoutCollection();

    public void setItems(List<String> producto) {
        this.list.addAll(producto);
        notifyDataSetChanged();
    }
    public RecyclerAdapter() {
        expansionsCollection.openOnlyOne(false);
    }
    @Override
    public RecyclerAdapter.RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(LAYOUT, parent, false);
        return new RecyclerHolder(mView);
    }

    @Override
    public void onBindViewHolder(RecyclerAdapter.RecyclerHolder holder, int position) {
        TextView text = holder.itemView.findViewById(R.id.hola);
        text.setText(list.get(position));
        expansionsCollection.add(holder.getExpansionLayout());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecyclerHolder extends RecyclerView.ViewHolder  {
        ExpansionLayout expansionLayout;
        public RecyclerHolder(View itemView) {
            super(itemView);
            expansionLayout = itemView.findViewById(R.id.expansionLayout);
            expansionLayout.addListener(new ExpansionLayout.Listener() {
                @Override
                public void onExpansionChanged(ExpansionLayout expansionLayout, boolean expanded) {
                    Log.e("ssfd", String.valueOf(getAdapterPosition()));
                }
            });
        }
        public ExpansionLayout getExpansionLayout() {
            return expansionLayout;
        }

    }

}