package com.example.monederoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

//import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAppAdapter extends RecyclerView.Adapter<MyAppAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<MyAppModel> myappModelArrayList;

    public MyAppAdapter(Context ctx, ArrayList<MyAppModel> myappModelArrayList){

        inflater = LayoutInflater.from(ctx);
        this.myappModelArrayList = myappModelArrayList;
    }

    @Override
    public MyAppAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyAppAdapter.MyViewHolder holder, int position) {

        holder.name.setText(myappModelArrayList.get(position).getName());
        holder.description.setText(myappModelArrayList.get(position).getdescription());

        String idtarjeta = (myappModelArrayList.get(position).getIdTarjeta());
        Tarjeta tarjeta = new Tarjeta();
            holder.LayoutTarjetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                tarjeta.obtenerInformacionTarjeta(idtarjeta, inflater.getContext());

            }
        });

    }

    @Override
    public int getItemCount() {
        return myappModelArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name, description;

        ImageView img;

        LinearLayout LayoutTarjetas;

        public MyViewHolder(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.nombre);
            description = (TextView) itemView.findViewById(R.id.precio);
            img = (ImageView) itemView.findViewById(R.id.img);
            LayoutTarjetas =(LinearLayout) itemView.findViewById(R.id.layoutTarjetas);
        }

    }
}
