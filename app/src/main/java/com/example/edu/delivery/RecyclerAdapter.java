package com.example.edu.delivery;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {

    private static final int LAYOUT = R.layout.reciclercell;
    private String textos,textos2 = "";
    private final ExpansionLayoutCollection expansionsCollection = new ExpansionLayoutCollection();
    private JSONArray jsonarray = new JSONArray();
    private List<String> distancias=new ArrayList<>();
    private Context context;

    public void setItems(JSONArray jsono, List<String> distancias) {
        this.jsonarray = jsono;
        this.distancias.clear();
        this.distancias.addAll(distancias);
    }
    public RecyclerAdapter(BusquedaActivity busquedaActivity) {
        this.context = busquedaActivity;
        expansionsCollection.openOnlyOne(false);

    }
    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(LAYOUT, parent, false);
        return new RecyclerHolder(mView);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        String text = "";
        try {

            for (int j=0;j<jsonarray.getJSONObject(position).getJSONArray("productos").length();j++){
                try {
                    text = text + String.valueOf(jsonarray.getJSONObject(position).getJSONArray("productos").getJSONObject(j).getString("producto")+"\n");
                    //Log.e("value", String.valueOf(jsonarray.getJSONObject(position).getJSONArray("productos").getJSONObject(j).getString("producto"))+"\n");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView t = holder.itemView.findViewById(R.id.empresa);
        try {
            t.setText(jsonarray.getJSONObject(position).getString("empresa"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView content = holder.itemView.findViewById(R.id.content);
        content.setText(text);
        TextView dist = holder.itemView.findViewById(R.id.hola);
        dist.setText(distancias.get(position));
        /*
        TextView text = holder.itemView.findViewById(R.id.hola);
        TextView h = holder.itemView.findViewById(R.id.empresa);
        Log.e("sd",list2.get(position).substring(0,20));
        text.setText(list.get(position));
        h.setText(list2.get(position).substring(0,17));

        TextView c = holder.itemView.findViewById(R.id.bebida);
        c.setText(textos2);
        textos = "";
        textos2 = "";
         */
        expansionsCollection.add(holder.getExpansionLayout()).add(holder.getExpansionLayout());
    }

    @Override
    public int getItemCount() {
        return jsonarray.length();
    }



    public class RecyclerHolder extends RecyclerView.ViewHolder  {
        ExpansionLayout expansionLayout;
        public RecyclerHolder(View itemView) {
            super(itemView);
            expansionLayout = itemView.findViewById(R.id.expansionLayout);
            expansionLayout.addListener(new ExpansionLayout.Listener() {
                @Override
                public void onExpansionChanged(ExpansionLayout expansionLayout, boolean expanded) {

                }
            });
            Button btn = itemView.findViewById(R.id.btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("ir a", String.valueOf(getAdapterPosition()));
                    Intent intent = new Intent(context,ScrollingActivity.class);
                    intent.putExtra("objeto", String.valueOf(jsonarray));
                    intent.putExtra("posicion", String.valueOf(getAdapterPosition()));
                    context.startActivity(intent);

                }
            });
            Button btnmap = itemView.findViewById(R.id.mapa);
            btnmap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse("https://www.google.com/maps/dir/-25.2669752,-57.505732/-25.265015,-57.512561/@-25.2663761,-57.513863,16z/data=!3m1!4b1");
                    Intent newintent = new Intent(Intent.ACTION_VIEW,uri);
                    context.startActivity(newintent);
                }
            });
        }
        public ExpansionLayout getExpansionLayout() {
            return expansionLayout;
        }

    }

}
