package com.example.edu.delivery;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import java.util.ArrayList;


public class ScrollingActivity extends AppCompatActivity {
    ImageView logo;
    ScrollableNumberPicker numberPicker;
    ListView pedidos_seleccionados;
    TextView total_text;
    Spinner categoria;
    ArrayList<String> pedido_list = new ArrayList<>();
    ArrayList<Integer> precio_list = new ArrayList<>();
    ArrayList<Integer> cant_list = new ArrayList<>();
    ArrayList<String> pedido_list_spinner = new ArrayList<>();
    ArrayList<String> precio_list_spinner = new ArrayList<>();
    private int id_seleccion;
    customadapterlist madapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        categoria = findViewById(R.id.categoria);
        pedidos_seleccionados = findViewById(R.id.lista);
        total_text = findViewById(R.id.total);
        logo = findViewById(R.id.logo);
        toolbars();
        spinners();
        numberpickers();

    }
    public void total(){
        int total=0;
        for (int i=0;i<pedido_list.size();i++){
            total = total + precio_list.get(i);
        }
        total_text.setText((String.valueOf(total)+" Gs."));

    }
    public void btn_agregar(View view){
        int canti = numberPicker.getValue();
        if (id_seleccion!=0 && canti!=0) {

            Integer precio = Integer.parseInt(precio_list_spinner.get(id_seleccion));
            String producto = pedido_list_spinner.get(id_seleccion);
            if (!pedido_list.contains(producto)) {
                precio_list.add(precio*canti);
                pedido_list.add(producto);
                cant_list.add(canti);

            }
            else {
                Log.e("yaexste","fbg");
                int id = pedido_list.indexOf(producto);
                precio_list.set(id,precio*canti);
                cant_list.set(id,canti);
                Log.e("id",String.valueOf(id));
                Log.e("yaexst", String.valueOf(precio_list));
                Log.e("yaexste", String.valueOf(cant_list));
            }
            Log.e("pedido_list", String.valueOf(pedido_list));
            numberPicker.setValue(numberPicker.getMinValue());
            listviews();
            Utility.setListViewHeightBasedOnChildren(pedidos_seleccionados);
            total();


        }

    }
    private void spinners() {
        String[] countryNames = {"Seleccione", "Hamburguesa normal", "Hamburguesa completa", "coca cola", "lomito", "papas fritas", "cerveza", "empanada"};
        String[] subs = {"-----", "7000", "10000", "5000", "15000", "5000", "8000", "2000"};
        for (int i = 0; i < countryNames.length; i++) {
            pedido_list_spinner.add(countryNames[i]);
            precio_list_spinner.add(subs[i]);
        }
        customadapterspinner adaptador = new customadapterspinner(this, pedido_list_spinner, precio_list_spinner);
        categoria.setAdapter(adaptador);
        categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                id_seleccion = adapterView.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });
    }
    private void toolbars(){
        final CollapsingToolbarLayout collapsingToolbarLayout =  findViewById(R.id.toolbar_layout);
        AppBarLayout appBarLayout =  findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = true;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset <= 127) {
                    collapsingToolbarLayout.setTitle("Delivery ON");

                    logo.animate().alpha(0.0f).setDuration(500);


                    isShow = true;
                } else if(isShow) {
                    logo.animate().alpha(1.0f).setDuration(500);
                    collapsingToolbarLayout.setTitle(" ");
                    isShow = false;
                }
            }
        });

    }
    private void listviews(){
        madapter = new customadapterlist(this,pedido_list,precio_list,cant_list);
        pedidos_seleccionados.setAdapter(madapter);
        pedidos_seleccionados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showAlert(i);

            }
        });
    }
    private void numberpickers(){
        numberPicker = findViewById(R.id.number);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.custommenu, menu);
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

    public static class Utility {
        static void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = 0;
            int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }
    private void showAlert(final int id) {
        Log.e("id", String.valueOf(id));
        final android.support.v7.app.AlertDialog.Builder dialog = new android.support.v7.app.AlertDialog.Builder(this);
        dialog.setTitle("Pedido")
                .setMessage("Desea quitar '"+cant_list.get(id)+" "+pedido_list.get(id)+"'s de la lista de pedidos?")
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        pedido_list.remove(id);
                        precio_list.remove(id);
                        cant_list.remove(id);
                        total();
                        listviews();
                        Utility.setListViewHeightBasedOnChildren(pedidos_seleccionados);


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {

                    }
                });
        dialog.show();

    }

}
