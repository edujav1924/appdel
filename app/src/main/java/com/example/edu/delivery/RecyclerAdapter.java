package com.example.edu.delivery;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {
    private final List<String> list = new ArrayList<>();
    private final List<String> list2 = new ArrayList<>();
    private static final int LAYOUT = R.layout.reciclercell;
    private String textos,textos2 = "";
    private final ExpansionLayoutCollection expansionsCollection = new ExpansionLayoutCollection();

    public void setItems(List<String> distancias, List<String> producto) {
        this.list.clear();
        this.list2.clear();
        this.list.addAll(producto);
        this.list2.addAll(distancias);
    }
    public RecyclerAdapter() {

        expansionsCollection.openOnlyOne(false);

    }
    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(LAYOUT, parent, false);
        return new RecyclerHolder(mView);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
        for (int i=0;i<list.size();i++){
            textos = textos + "  \uD83C\uDF5D"+list.get(i)+"\n";
            textos2 = textos2 + "  \uD83C\uDF79"+list.get(i)+"\n";
        }
        TextView text = holder.itemView.findViewById(R.id.hola);
        TextView h = holder.itemView.findViewById(R.id.empresa);
        //Log.e("sd",list2.get(position).substring(0,20));
        text.setText(list.get(position));
        h.setText(list2.get(position).substring(0,17));
        TextView content = holder.itemView.findViewById(R.id.content);
        content.setText(textos);
        TextView c = holder.itemView.findViewById(R.id.bebida);
        c.setText(textos2);
        textos = "";
        textos2 = "";

        expansionsCollection.add(holder.getExpansionLayout()).add(holder.getExpansionLayout());
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

                }
            });
            Button btn = itemView.findViewById(R.id.btn);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.e("ir a", String.valueOf(getAdapterPosition()));
                }
            });
        }
        public ExpansionLayout getExpansionLayout() {
            return expansionLayout;
        }

    }

}
