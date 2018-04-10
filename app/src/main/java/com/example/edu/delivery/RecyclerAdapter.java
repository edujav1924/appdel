package com.example.edu.delivery;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.florent37.expansionpanel.ExpansionLayout;
import com.github.florent37.expansionpanel.viewgroup.ExpansionLayoutCollection;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerHolder> {
    private final List<String> list = new ArrayList<>();
    private static final int LAYOUT = R.layout.reciclercell;
    private String textos = "";
    private final ExpansionLayoutCollection expansionsCollection = new ExpansionLayoutCollection();

    public void setItems(List<String> producto) {
        this.list.clear();
        this.list.addAll(producto);

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
            textos = textos + "\uD83C\uDF73 \uD83C\uDF78"+list.get(i)+"\n";
            Log.e("fdfd",textos);
        }
        TextView text = holder.itemView.findViewById(R.id.hola);
        text.setText(list.get(position));
        TextView content = holder.itemView.findViewById(R.id.content);
        content.setText(textos);
        textos = "";
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
