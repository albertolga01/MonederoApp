package com.example.monederoapp.Vehiculo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.monederoapp.R;
import com.example.monederoapp.Vehiculos;
//import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAppAdapterVehiculos extends RecyclerView.Adapter<MyAppAdapterVehiculos.MyViewHolder> {

    private LayoutInflater inflater;
    private ArrayList<MyAppModelVehiculos> myappModelArrayListVehiculo;

    public MyAppAdapterVehiculos(Context ctx, ArrayList<MyAppModelVehiculos> myappModelArrayListVehiculos){

        inflater = LayoutInflater.from(ctx);
        this.myappModelArrayListVehiculo = myappModelArrayListVehiculos;
    }

    @Override
    public MyAppAdapterVehiculos.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item2, parent, false);
        MyViewHolder holder = new MyViewHolder(view);

        return holder;
    }


      @Override
          public void onBindViewHolder(MyViewHolder holder, int position) {

              holder.Modelo.setText(myappModelArrayListVehiculo.get(position).getModelo());
              holder.ano.setText(myappModelArrayListVehiculo.get(position).getano());
              holder.Placas.setText(myappModelArrayListVehiculo.get(position).getPlacas());
//              holder.NoEconomico.setText((myappModelArrayListVehiculo.get(position).getNoEconomico()));
              /**
              holder.IdVehiculo.setText((myappModelArrayListVehiculo.get(position).getIdVehiculo()));
              holder.TipoVehiculo.setText((myappModelArrayListVehiculo.get(position).getTipoVehiculo()));
              holder.ControlaOdometro.setText((myappModelArrayListVehiculo.get(position).getControlaOdometro()));
              holder.KmMaximoCarga.setText((myappModelArrayListVehiculo.get(position).getKmMaximoCarga()));
              holder.Variacion.setText((myappModelArrayListVehiculo.get(position).getVariacion()));
              holder.OdometroInicial.setText((myappModelArrayListVehiculo.get(position).getOdometroInicial()));
              holder.RendPromedio.setText((myappModelArrayListVehiculo.get(position).getRendPromedio()));
              holder.CentroCostoDpto.setText((myappModelArrayListVehiculo.get(position).getCentroCostoDpto()));
              holder.Tarjeta.setText((myappModelArrayListVehiculo.get(position).getTarjeta()));
              holder.Tipo.setText((myappModelArrayListVehiculo.get(position).getTipo()));
               */

              String idvehiculo = (myappModelArrayListVehiculo.get(position).getIdVehiculo());
              //Picasso.get().load(myappModelArrayListVehiculo.get(position).getimageurl()).into(holder.img2);
          Vehiculos vehiculos = new Vehiculos();
              holder.Layout.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {

                      vehiculos.obtenerInformacionVehiculo(idvehiculo,  v.getContext());
                  }
              });


    }




    @Override
    public int getItemCount() { return myappModelArrayListVehiculo.size(); }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView Modelo, ano, Placas, IdVehiculo, NoEconomico, TipoVehiculo, ControlaOdometro, KmMaximoCarga, Variacion, OdometroInicial, RendPromedio, CentroCostoDpto, Tarjeta, Tipo;

        ImageView img2;

        LinearLayout Layout;


        public MyViewHolder(View itemView) {
            super(itemView);

           // LinearLayout layout = itemView.findViewById(R.id.contenedorVehiculos);
            Modelo = (TextView) itemView.findViewById(R.id.fechaSeleccionada);
            ano = (TextView) itemView.findViewById(R.id.ano);
            Placas = (TextView) itemView.findViewById(R.id.placas);
            //NoEconomico =(TextView) itemView.findViewById(R.id.noeconomico);
            Layout =(LinearLayout) itemView.findViewById(R.id.layoutVehiculos);

            //img2 = (ImageView) itemView.findViewById(R.id.img2);
        }

    }

}
