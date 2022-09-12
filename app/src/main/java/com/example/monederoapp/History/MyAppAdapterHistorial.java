package com.example.monederoapp.History;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.monederoapp.Historial;
import com.example.monederoapp.R;
import com.example.monederoapp.RImpresionTicket;

import java.util.ArrayList;

public class MyAppAdapterHistorial extends RecyclerView.Adapter<MyAppAdapterHistorial.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<MyAppModelHistorial> myappModelArrayListHistorial;

    public MyAppAdapterHistorial(Context ctx, ArrayList<MyAppModelHistorial> myappModelArrayListHistorial){

        inflater = LayoutInflater.from(ctx);
        this.myappModelArrayListHistorial = myappModelArrayListHistorial;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item3, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.folio.setText(myappModelArrayListHistorial.get(position).getfolio());
        holder.fecha.setText(myappModelArrayListHistorial.get(position).getfecha());
        holder.estacion.setText(myappModelArrayListHistorial.get(position).getestacion());

        String idtarjeta = (myappModelArrayListHistorial.get(position).getfolio());
        Historial tarjeta = new Historial();
        holder.LayoutTarjetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), RImpresionTicket.class);
                intent.putExtra("folio", idtarjeta);
                intent.putExtra("odometro", "3");
                v.getContext().startActivity(intent);


                //    tarjeta.obtenerInformacionTarjeta(idtarjeta, inflater.getContext());

            }
        });

    }

    @Override
    public int getItemCount() {
        return myappModelArrayListHistorial.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView folio, fecha, estacion;

        ImageView img2;

        LinearLayout LayoutTarjetas;

        public MyViewHolder(View itemView) {
            super(itemView);

            folio = (TextView) itemView.findViewById(R.id.folio);
            fecha = (TextView) itemView.findViewById(R.id.fecha);
            estacion = (TextView) itemView.findViewById(R.id.estacion);
            LayoutTarjetas =(LinearLayout) itemView.findViewById(R.id.layoutTarjetas);
        }

    }
}
