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

        adapter.setItems(pais,precio);
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
    private final ArrayList<Integer> precio = new ArrayList<Integer>();
    private final ExpansionLayoutCollection expansionsCollection = new ExpansionLayoutCollection();

    public RecyclerAdapter() {
        expansionsCollection.openOnlyOne(false);
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return RecyclerHolder.buildFor(parent);

    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        holder.bind((list.get(position)));
        TextView v= holder.itemView.findViewById(R.id.hola);
        v.setText(list.get(position));
        TextView price = holder.itemView.findViewById(R.id.price);
        price.setText(String.valueOf(precio.get(position)));
        expansionsCollection.add(holder.getExpansionLayout());
        
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setItems(ArrayList<String> items, ArrayList<Integer> precio) {
        this.list.addAll(items);
        this.precio.addAll(precio);
        notifyDataSetChanged();
    }

    public final static class RecyclerHolder extends RecyclerView.ViewHolder  implements View.OnClickListener  {
        private static final int LAYOUT = R.layout.reciclercell;


        @BindView(R.id.expansionLayout)
        ExpansionLayout expansionLayout;

        public static RecyclerHolder buildFor(ViewGroup viewGroup){
            return new RecyclerHolder(LayoutInflater.from(viewGroup.getContext()).inflate(LAYOUT, viewGroup, false));
        }

        public RecyclerHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void bind(String i){
            expansionLayout.collapse(false);
        }

        public ExpansionLayout getExpansionLayout() {
            return expansionLayout;
        }


        @Override
        public void onClick(View view) {

        }
    }
}


