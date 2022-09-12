package com.example.monederoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.monederoapp.History.MyAppAdapterHistorial;
import com.example.monederoapp.History.MyAppModelHistorial;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Historial extends AppCompatActivity {

    private SharedPreferences sp;
    private final int codigodatos = 1;
    private RecyclerView recyclerView;
    ArrayList<MyAppModelHistorial> myappModelArrayListHistorial;
    private MyAppAdapterHistorial myAppAdapterHistorial;

    private Button btnBuscarFecha = null;

    private EditText eTxtBuscaFecha = null;
    DatePickerDialog.OnDateSetListener setListener;


    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        // Llamo al layout para el RecyclerView
        recyclerView = findViewById(R.id.contenedorHistorial);

        btnBuscarFecha = findViewById(R.id.btnBuscarFecha);
        eTxtBuscaFecha =findViewById(R.id.eTxtBuscaFecha);
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        eTxtBuscaFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Historial.this, android.R.style.Theme_DeviceDefault_Light_Dialog
                        ,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
                datePickerDialog.show();

            }
        });


        setListener = new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = year+"-"+month+"-"+dayOfMonth;
                eTxtBuscaFecha.setText(date);
            }
        };

        btnBuscarFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fechab = "";
                try{
                    fechab = eTxtBuscaFecha.getText().toString();
                }catch (Exception e){

                }

                String idcliente = sp.getString("IDCLIENTE", "DEF");
                if(!fechab.equals("")) {

                    leerJSON(fechab, idcliente);
                }else{
                    dialog.show();
                    TextView tituloDialog = dialog.findViewById(R.id.tituloDialog);
                    TextView subtituloDialog = dialog.findViewById(R.id.subtituloDialog);
                    ImageView iconoDialog = dialog.findViewById(R.id.iconoDialog);

                    tituloDialog.setText("Mensaje");
                    iconoDialog.setImageDrawable(ContextCompat.getDrawable(Historial.this, R.drawable.ic_baseline_error_24));
                    subtituloDialog.setText("Selecciona una fecha");
                }

                eTxtBuscaFecha.setText(eTxtBuscaFecha.getText().toString());

            }
        });


        //Create the Dialog here
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dialog_continuar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false); //Optional
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog

        Button Okay = dialog.findViewById(R.id.btn_okay);

        Okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        eTxtBuscaFecha.setText(fDate);

    }






    @SuppressLint("StaticFieldLeak")
    private void leerJSON(String fecha, String idcliente){

        // Inicio una tare Asíncrona
        new AsyncTask<Void, Void, String>(){

            // La tarea se llevará acabo de fondo
            protected String doInBackground(Void[] params) {

                String response = "";

                // Declaro un HashMap
                HashMap<String, String> map = new HashMap<>();
                map.put("id", "getServiciosApp");
                map.put("fecha", fecha);
                map.put("idcliente", idcliente);
                // Hago la petición de los datos
                try {
                    HttpRequest req = new HttpRequest(MainActivity.url);
                    response = req.prepare(HttpRequest.Method.POST).withData(map).sendAndReadString();
                } catch (Exception e) {
                    response = e.getMessage();
                }
                return response;
            }

            // Después de realizar la petición de los datos, llamo al método tareaCompletada()
            // El método tareaCompletada() lo crearé a continuación
            protected void onPostExecute(String resultado) {
                tareaCompletada(resultado, codigodatos);
            }

        }.execute();
    }

    public void tareaCompletada(String response, int serviceCode) {
        switch (serviceCode) {

            // Uso un case y le paso la variable 'codigodatos'
            case codigodatos:

                // Verifico si los datos se recibieron con el método siCorrecto()
                // El método siCorrecto() lo crearé más adelante.
                if (siCorrecto(response)) {
                    System.out.println("response: " + response);
                    // A mi modelo le paso el método obtenerInformacion(), este método lo crearé más adelante.
                    myappModelArrayListHistorial = obtenerInformacion(response);

                    // A mi Adaptador le paso mi modelo
                    myAppAdapterHistorial = new MyAppAdapterHistorial(this, myappModelArrayListHistorial);

                    // Le paso mi adaptador al RecyclerView
                    recyclerView.setAdapter(myAppAdapterHistorial);

                    // Cargo el Layout del RecyclerView
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recyclerView.setHasFixedSize(true);

                }else {
                    // Si hubo error, lo muestro en un Toast
                    Toast.makeText(Historial.this, obtenerCodigoError(response), Toast.LENGTH_SHORT).show();
                }
        }
    }

    public ArrayList<MyAppModelHistorial> obtenerInformacion(String response) {

        // Creo un array con los datos JSON que he obtenido
        ArrayList<MyAppModelHistorial> listaArray = new ArrayList<>();

        // Solicito los datos al archivo JSON
        try {
            //System.out.print(response);
            JSONObject jsonObject = new JSONObject(response);


            // Accedo a la fila 'tarjetas del archivo JSON
            JSONArray dataArray = jsonObject.getJSONArray("Servicios");

            // Recorro los datos que hay en la fila 'postres' del archivo JSON
            for (int i = 0; i < dataArray.length(); i++) {

                // Creo la variable 'datosModelo' y le paso mi modelo 'MyAppModel'
                MyAppModelHistorial datosModelo = new MyAppModelHistorial();

                // Creo la  variable 'objetos' y recupero los valores
                JSONObject objetos = dataArray.getJSONObject(i);

                // Selecciono dato por dato
                datosModelo.setfolio(objetos.getString("folio"));
                datosModelo.setfecha(objetos.getString("fecha"));
                datosModelo.setestacion(objetos.getString("estacion"));


                // Meto los datos en el array que definí más arriba 'listaArray'
                listaArray.add(datosModelo);

            }

        } catch (JSONException e) {
            e.printStackTrace();

            //dialogo
            dialog.show();
            TextView tituloDialog = dialog.findViewById(R.id.tituloDialog);
            TextView subtituloDialog = dialog.findViewById(R.id.subtituloDialog);
            ImageView iconoDialog = dialog.findViewById(R.id.iconoDialog);

            tituloDialog.setText("Alerta");
            iconoDialog.setImageDrawable(ContextCompat.getDrawable(Historial.this, R.drawable.ic_baseline_error_24));
            subtituloDialog.setText("No se encontraron registros para el día seleccionado");

        }

        // Devuelvo el array con los datos del archivo JSON
        return listaArray;
    }

    public boolean siCorrecto(String response) {

        // Verificamos si la petición de los datos ha sido correcta
        try {

            return true; // Retorno 'true' si es correcto


        } catch ( Exception e) {

        }

        // Si nada se lleva acabo retorno 'false'
        return false;
    }

    public String obtenerCodigoError(String response) {

        // Solicitamos el código de error que se encuentra en el archivo JSON
        try {
            // El archivo JSON contiene el dato 'message'
            JSONObject jsonObject = new JSONObject(response);

            return jsonObject.getString("message");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Si no hay datos en el archivo JSON, muestro un mensaje
        return "No hay datos";
    }

    public static String estado(String estado) {
        if (estado.equals("0")) {
            return "Inactivo";
        }else{
            return  "Activo";
        }
    }


}