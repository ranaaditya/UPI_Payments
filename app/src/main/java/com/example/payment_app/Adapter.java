package com.example.payment_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private List<Integer> list;
    private Context mcontext;

    public Adapter(List<Integer> list,Context context) {
        this.list = list;
        this.mcontext=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view=LayoutInflater.from(mcontext).inflate(R.layout.adapterlayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(""+list.get(position));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        private TextView textView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.listtextview);
        }
    }
}
