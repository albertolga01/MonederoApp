package com.example.monederoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.monederoapp.Vehiculo.MyAppModelVehiculos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.example.monederoapp.Vehiculo.MyAppAdapterVehiculos;


public class Vehiculos extends AppCompatActivity {

    private SharedPreferences sp;
    private Dialog dialog;
    private final int codigodatos = 1;
    private RecyclerView recyclerView;
    ArrayList<MyAppModelVehiculos> myappModelArrayListVehiculo;
    private MyAppAdapterVehiculos myAppAdapterVehiculos;

    public static EditText eTxtVehiculo, eTxtModelo, eTxtano, eTxtPlacas, eTxtNoEconomico, eTxtTipoVehiculo, eTxtControlaOdometro, eTxtKmMaximoCarga
            , eTxtVariacion, eTxtOdometroInicial, eTxtRendPromedio, eTxtCentroCostoDpto, eTxtTarjeta, eTxtTipo;
    private Button btnActVehiculo, btnLimpiarDatosVehiculo;
    public static CheckBox Activo;

    TextView textView3 = null;
    TextView modelo2 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehiculos);


        MainActivity main = new MainActivity();


        btnActVehiculo = findViewById(R.id.btnActualizarVehiculo);
        btnLimpiarDatosVehiculo = findViewById(R.id.btnLimpiarDatosVehiculo);
        eTxtVehiculo = findViewById(R.id.eTxtVehiculo);
        eTxtModelo = findViewById(R.id.eTxtModelo);
        eTxtano = findViewById(R.id.eTxtano);
        eTxtPlacas = (EditText) findViewById(R.id.eTxtPlacas);
        eTxtNoEconomico = findViewById(R.id.eTxtNoEconomico);
        eTxtTipoVehiculo = findViewById(R.id.eTxtTipoVehiculo);
        eTxtControlaOdometro = findViewById(R.id.eTxtControlaOdometro);
        eTxtKmMaximoCarga = findViewById(R.id.eTxtKmMaximoCarga);
        eTxtVariacion = findViewById(R.id.eTxtVariacion);
        eTxtOdometroInicial = findViewById(R.id.eTxtOdometroInicial);
        eTxtRendPromedio = findViewById(R.id.eTxtRendPromedio);
        eTxtCentroCostoDpto = findViewById(R.id.eTxtCentroCostoDpto);
        eTxtTarjeta = findViewById(R.id.eTxtTarjeta);
        Activo = findViewById(R.id.checkBoxActivo);
        textView3 = findViewById(R.id.textView3);
        modelo2 = findViewById(R.id.fechaSeleccionada);

        btnActVehiculo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String idvehiculo = eTxtVehiculo.getText().toString();
                String placas = eTxtPlacas.getText().toString();
                String noeconomico = eTxtNoEconomico.getText().toString();
                String tarjeta = eTxtTarjeta.getText().toString();
                String activo = Activo.getText().toString();
                String vehiculoActivo = "0";
                if(Activo.isChecked()){
                    vehiculoActivo = "1";
                }else{
                    vehiculoActivo = "0";
                }

                ActualizarDatosVehiculos(idvehiculo,placas,noeconomico,tarjeta,vehiculoActivo);



            }
        });


        btnLimpiarDatosVehiculo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                LimpiarDatosVehiculo(getApplicationContext());
            }
        });

        // Llamo al layout para el RecyclerView
        recyclerView = findViewById(R.id.contenedorVehiculos);

        // Llamo al método para leer el archivo JSON

        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String idcliente = sp.getString("IDCLIENTE", "DEF");
        leerJSON(idcliente);

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


    }


    @SuppressLint("StaticFieldLeak")
    private void leerJSON(String idcliente){

        // Inicio una tare Asíncrona
        new AsyncTask<Void, Void, String>(){

            // La tarea se llevará acabo de fondo
            protected String doInBackground(Void[] params) {

                String response = "";

                // Declaro un HashMap
                HashMap<String, String> map = new HashMap<>();
                map.put("id", "getVehiculosCteApp");
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
                    System.out.println(response);
                    // A mi modelo le paso el método obtenerInformacion(), este método lo crearé más adelante.
                    myappModelArrayListVehiculo = obtenerInformacion(response);

                    // A mi Adaptador le paso mi modelo
                    myAppAdapterVehiculos = new MyAppAdapterVehiculos(this, myappModelArrayListVehiculo);

                    // Le paso mi adaptador al RecyclerView
                    recyclerView.setAdapter(myAppAdapterVehiculos);

                    // Cargo el Layout del RecyclerView
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recyclerView.setHasFixedSize(true);

                }else {
                    // Si hubo error, lo muestro en un Toast
                    Toast.makeText(Vehiculos.this, obtenerCodigoError(response), Toast.LENGTH_SHORT).show();
                }
        }
    }

    public ArrayList<MyAppModelVehiculos> obtenerInformacion(String response) {

        // Creo un array con los datos JSON que he obtenido
        ArrayList<MyAppModelVehiculos> listaArray = new ArrayList<>();

        // Solicito los datos al archivo JSON
        try {
            //System.out.print(response);
            JSONObject jsonObject = new JSONObject(response);


            // Accedo a la fila 'tarjetas del archivo JSON
            JSONArray dataArray = jsonObject.getJSONArray("vehiculos");

            // Recorro los datos que hay en la fila 'postres' del archivo JSON
            for (int i = 0; i < dataArray.length(); i++) {

                // Creo la variable 'datosModelo' y le paso mi modelo 'MyAppModel'
                MyAppModelVehiculos datosModelo = new MyAppModelVehiculos();

                // Creo la  variable 'objetos' y recupero los valores
                JSONObject objetos = dataArray.getJSONObject(i);

                // Selecciono dato por dato
                datosModelo.setModelo(objetos.getString("modelo"));
                datosModelo.setano(objetos.getString("ano"));
                datosModelo.setPlacas(objetos.getString("placas"));
                datosModelo.setNoEconomico(objetos.getString("noeconomico"));
                datosModelo.setIdVehiculo(objetos.getString("idvehiculo"));
                datosModelo.setTipoVehiculo(objetos.getString("tipovehiculo"));
                datosModelo.setControlaOdometro(objetos.getString("controlaodometro"));
                datosModelo.setKmMaximoCarga(objetos.getString("kmmax"));
                datosModelo.setVariacion(objetos.getString("variacion"));
                datosModelo.setOdometroInicial(objetos.getString("odometro"));
                datosModelo.setRendPromedio(objetos.getString("rendimiento"));
                datosModelo.setCentroCostoDpto(objetos.getString("centrocosto"));
                datosModelo.setTarjeta(objetos.getString("idtarjeta"));
                datosModelo.setTipo(objetos.getString("activo"));

                // Meto los datos en el array que definí más arriba 'listaArray'
                listaArray.add(datosModelo);

            }

        } catch (JSONException e) {
            e.printStackTrace();

            Toast.makeText(Vehiculos.this, "Error", Toast.LENGTH_LONG);
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
            return "No";
        }else{
            return  "Sí";
        }
    }


    public static String tipoVehiculo(String tipo) {
        if (tipo.equals("1")) {
            return "Auto";
        } else if (tipo.equals("2")){
            return  "Moto";
        } else if (tipo.equals("3")){
            return  "Camión";
        } else if (tipo.equals("4")){
            return  "Otro";
        }else{
            return "NA";
        }

    }

    public void obtenerInformacionVehiculo(String idvehiculo, Context context){

        ObtenerDatosV(idvehiculo, context);

    }

    public void ObtenerDatosV(String idvehiculo, Context context){
        //send request, display a message that nip is incorrect or let it continue to the next step
        // Instantiate the cache
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, MainActivity.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String strNew = response.replace("[", "");
                    String strNew1 = strNew.replace("]", "");
                    JSONObject obj = new JSONObject(strNew1);
                    eTxtVehiculo.setText(obj.getString("idvehiculo"));
                    eTxtModelo.setText(obj.getString("modelo"));
                    eTxtano.setText(obj.getString("ano"));
                    eTxtPlacas.setText(obj.getString("placas"));
                    eTxtNoEconomico.setText(obj.getString("noeconomico"));
                    eTxtTipoVehiculo.setText(tipoVehiculo(obj.getString("tipovehiculo")));
                    eTxtTarjeta.setText(obj.getString("notarjeta"));
                    eTxtCentroCostoDpto.setText(obj.getString("centrocosto"));
                    eTxtControlaOdometro.setText(estado(obj.getString("controlaodometro")));
                    eTxtKmMaximoCarga.setText(obj.getString("kmmax"));
                    eTxtVariacion.setText(obj.getString("variacion"));
                    eTxtOdometroInicial.setText(obj.getString("odometro"));
                    eTxtRendPromedio.setText(obj.getString("rendimiento"));

                    if(obj.getString("activo").equals("1")){
                        Activo.setChecked(true);
                    }else{
                        Activo.setChecked(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("id", "getInformacionvehiculoApp");
                MyData.put("idvehiculo", idvehiculo);
                return MyData;
            }
        };
        queue.add(MyStringRequest);
    }

    private void LimpiarDatosVehiculo(Context context){


        //send request, display a message that nip is incorrect or let it continue to the next step
        // Instantiate the cache
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, MainActivity.url, new Response.Listener<String>() {



            @Override
            public void onResponse(String response) {
                for (int f = 0; f < response.length(); f++) {
                    try {
                        eTxtVehiculo.setText("");
                        eTxtModelo.setText("");
                        eTxtano.setText("");
                        eTxtPlacas.setText("");
                        eTxtNoEconomico.setText("");
                        eTxtTipoVehiculo.setText("");
                        eTxtTarjeta.setText("");
                        eTxtCentroCostoDpto.setText("");
                        eTxtControlaOdometro.setText("");
                        eTxtKmMaximoCarga.setText("");
                        eTxtVariacion.setText("");
                        eTxtOdometroInicial.setText("");
                        eTxtRendPromedio.setText("");


                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("id", "getactualizarVehiculosApp");
                return MyData;
            }
        };

        dialog.show();
        TextView tituloDialog = dialog.findViewById(R.id.tituloDialog);
        TextView subtituloDialog = dialog.findViewById(R.id.subtituloDialog);
        ImageView iconoDialog = dialog.findViewById(R.id.iconoDialog);

        tituloDialog.setText("Mensaje");
        iconoDialog.setImageDrawable(ContextCompat.getDrawable(Vehiculos.this, R.drawable.ic_baseline_check));
        subtituloDialog.setText("Datos limpiados correctamente");
        queue.add(MyStringRequest);
    }


    public void ActualizarDatosVehiculos(String idvehiculo, String placas, String noeconomico, String tarjeta, String activo){
        //send request, display a message that nip is incorrect or let it continue to the next step
        // Instantiate the cache
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, MainActivity.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    if (response.trim().equals("1")) {
                        //Toast.makeText(Vehiculos.this, "Datos actualizados", Toast.LENGTH_SHORT).show();

                        String idcliente = sp.getString("IDCLIENTE", "DEF");
                        leerJSON(idcliente);

                        dialog.show();
                        TextView tituloDialog = dialog.findViewById(R.id.tituloDialog);
                        TextView subtituloDialog = dialog.findViewById(R.id.subtituloDialog);
                        ImageView iconoDialog = dialog.findViewById(R.id.iconoDialog);

                        tituloDialog.setText("Mensaje");
                        iconoDialog.setImageDrawable(ContextCompat.getDrawable(Vehiculos.this, R.drawable.ic_baseline_check));
                        subtituloDialog.setText("Datos Actualizados Correctamente");

                    }else{
                        //Toast.makeText(Vehiculos.this, "Error al actualizar los datos", Toast.LENGTH_SHORT).show();
                        dialog.show();
                        TextView tituloDialog = dialog.findViewById(R.id.tituloDialog);
                        TextView subtituloDialog = dialog.findViewById(R.id.subtituloDialog);
                        ImageView iconoDialog = dialog.findViewById(R.id.iconoDialog);

                        tituloDialog.setText("Alerta");
                        iconoDialog.setImageDrawable(ContextCompat.getDrawable(Vehiculos.this, R.drawable.ic_baseline_error_24));
                        subtituloDialog.setText("Error al actualizar los datos");

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Vehiculos.this, "Error al actualizar" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("id", "editarVehiculoApp");
                MyData.put("idvehiculo", idvehiculo);
                MyData.put("placas", placas);
                MyData.put("noeconomico", noeconomico);
                MyData.put("tarjeta", tarjeta);
                MyData.put("activo", activo);
                return MyData;
            }
        };
        queue.add(MyStringRequest);
    }

}


