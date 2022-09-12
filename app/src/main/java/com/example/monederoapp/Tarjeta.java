package com.example.monederoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.lang.String;
import java.lang.Object;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.monederoapp.ModelEstaciones.MyAppModelEstaciones;
import com.example.monederoapp.MyAppModel;
import com.example.monederoapp.MyAppAdapter;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class Tarjeta extends AppCompatActivity {

    ///////
    public static ListView list, listProductos;
    public static EditText editTextLitros, editTextDinero, editTextPlacas, editTextTipo,eTxtTarjeta,editTextPeriodo;
    public static CheckBox Lunes, Martes, Miercoles, Jueves, Viernes, Sabado , Domingo;
    private Button btnActualizarTarjetas;
    private CheckBox chkAll;
    //


    private SharedPreferences sp;
    RequestQueue queue;
    /////

    private CheckBox mMap;
    private Dialog dialog;


    private final int codigodatos = 1;
    private RecyclerView recyclerView;
    ArrayList<MyAppModel> myappModelArrayList;
    private MyAppAdapter myappAdapter;
    private static ProgressDialog mProgressDialog;


    public static Button timeButtton, timeButttonFin;
    int hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tarjetas);

        btnActualizarTarjetas = findViewById(R.id.btnActualizarTarjetas);
        eTxtTarjeta = findViewById(R.id.eTxtTarjeta);
        editTextPlacas = findViewById(R.id.editTextPlacas);
        timeButtton = findViewById(R.id.btnHoraInicio);
        timeButttonFin = findViewById(R.id.btnHoraFin);
        editTextLitros = findViewById(R.id.editTextLitros);
        editTextDinero = findViewById(R.id.editTextDinero);
        editTextPeriodo = findViewById(R.id.editTextPeriodo);

        // Llamo al layout para el RecyclerView
        recyclerView = findViewById(R.id.contenedor);
        list = (ListView) findViewById(R.id.list);
        listProductos = (ListView) findViewById(R.id.listProductos);

        // Llamo al método para leer el archivo JSON

        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String idcliente = sp.getString("IDCLIENTE", "DEF");
        leerJSON(idcliente);

        ObtenerDatosEstaciones();
        ObtenerDatosProductos();


          Lunes = (CheckBox) findViewById(R.id.checkBoxLunes);
          Martes = (CheckBox) findViewById(R.id.checkBoxMartes);
          Miercoles = (CheckBox) findViewById(R.id.checkBoxMiercoles);
          Jueves = (CheckBox) findViewById(R.id.checkBoxJueves);
          Viernes = (CheckBox) findViewById(R.id.checkBoxViernes);
          Sabado = (CheckBox) findViewById(R.id.checkBoxSabado);
          Domingo = (CheckBox) findViewById(R.id.checkBoxDomingo);

        btnActualizarTarjetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String placas = editTextPlacas.getText().toString();
                String idtarjeta = eTxtTarjeta.getText().toString();
                String limitedinero = editTextDinero.getText().toString();
                String limitelitros = editTextLitros.getText().toString();

                ActualizarTarjetas(placas,idtarjeta,limitedinero,limitelitros);
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
               map.put("id", "getTarjetasCteApp"); //

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
                    myappModelArrayList = obtenerInformacion(response);

                    // A mi Adaptador le paso mi modelo
                    myappAdapter = new MyAppAdapter(this,myappModelArrayList);

                    // Le paso mi adaptador al RecyclerView
                    recyclerView.setAdapter(myappAdapter);

                    // Cargo el Layout del RecyclerView
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                    recyclerView.setHasFixedSize(true);

                }else {
                    // Si hubo error, lo muestro en un Toast
                    Toast.makeText(Tarjeta.this, obtenerCodigoError(response), Toast.LENGTH_SHORT).show();
                }
        }
    }

    public ArrayList<MyAppModel> obtenerInformacion(String response) {

        // Creo un array con los datos JSON que he obtenido
        ArrayList<MyAppModel> listaArray = new ArrayList<>();

        // Solicito los datos al archivo JSON
        try {
            //System.out.print(response);
            JSONObject jsonObject = new JSONObject(response);


                // Accedo a la fila 'tarjetas del archivo JSON
                JSONArray dataArray = jsonObject.getJSONArray("tarjetas");

                // Recorro los datos que hay en la fila del archivo JSON
                for (int i = 0; i < dataArray.length(); i++) {

                    // Creo la variable 'datosModelo' y le paso mi modelo 'MyAppModel'
                    MyAppModel datosModelo = new MyAppModel();

                    // Creo la  variable 'objetos' y recupero los valores
                    JSONObject objetos = dataArray.getJSONObject(i);

                    // Selecciono dato por dato

                    datosModelo.setName("Tarjeta: "+formatCard(objetos.getString("notarjeta")));
                    datosModelo.setdescription("Estado: " + estado(objetos.getString("activo")));
                    datosModelo.setIdTarjeta(objetos.getString("folio"));

                    // Meto los datos en el array que definí más arriba 'listaArray'
                    listaArray.add(datosModelo);

                }

        } catch (JSONException e) {
            e.printStackTrace();

            Toast.makeText(Tarjeta.this, "Error", Toast.LENGTH_LONG);
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


    /** Botones TimePicker **/


    public void popTimePicker(View view) {

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeButtton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };

         //int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, false);

        timePickerDialog.setTitle("Selecciona hora");
        timePickerDialog.show();
    }

    public void popTimePicker2(View view) {

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeButttonFin.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };

        // int style = AlertDialog.THEME_HOLO_DARK;
        


        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, false);

        timePickerDialog.setTitle("Selecciona hora");
        timePickerDialog.show();
    }


    public static String formatCard(String cardNumber) {
        if (cardNumber == null) return null;
        char delimiter = ' ';
        return cardNumber.replaceAll(".{4}(?!$)", "$0" + delimiter);
    }


    public static String estado(String estado) {
        if (estado.equals("0")) {
            return "Inactivo";
        }else{
            return  "Activo";
        }
    }


    public void ObtenerDatosEstaciones(){

       //send request, display a message that nip is incorrect or let it continue to the next step
       RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
       StringRequest MyStringRequest = new StringRequest(Request.Method.POST, MainActivity.url, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {
               try {
                   JSONObject obj = new JSONObject(response);
                   List<String> estaciones2 = new ArrayList<String>();
                   JSONArray cast = obj.getJSONArray("estaciones");
                   MyAppModelEstaciones estaciones = new MyAppModelEstaciones();
                   List<String> dato = new ArrayList<>();

                   dato.add(estaciones.getnombreEstacion());
                   dato.add(estaciones.getidEstacion());
                   ArrayList<Category> MyArraylist = new ArrayList<>();

                   for (int i=0; i<cast.length(); i++) {
                       Category genres = new Category();
                       JSONObject MyJsonObject = cast.getJSONObject(i);
                       genres.setCateogry_id(Integer.parseInt(MyJsonObject.getString("idestacion")));
                       genres.setCategory_Name(MyJsonObject.getString("nombre"));
                       MyArraylist.add(genres);


                   }
                   ListView mListViewBooks = (ListView) findViewById(R.id.list);
                   final CategoryAdapter categoryAdapter = new CategoryAdapter(getApplicationContext(), R.layout.row, MyArraylist);

                   int totalHeight = 0;
                   for (int i = 0; i < categoryAdapter.getCount(); i++) {
                       View listItem = categoryAdapter.getView(i, null, list);
                       listItem.measure(0, 0);
                       totalHeight += listItem.getMeasuredHeight() + (listItem.getMeasuredHeightAndState());
                   }
                   totalHeight = totalHeight / 2;
                   ViewGroup.LayoutParams params = list.getLayoutParams();
                   params.height = totalHeight + (list.getDividerHeight() * (categoryAdapter.getCount() - 1));
                   list.setLayoutParams(params);
                   list.requestLayout();
                   mListViewBooks.setAdapter(categoryAdapter);

               } catch (JSONException e) {
                   e.printStackTrace();
                   Toast.makeText(Tarjeta.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
               }
           }
       }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
           @Override
           public void onErrorResponse(VolleyError error) {
           }
       }) {
           protected Map<String, String> getParams() {
               Map<String, String> MyData = new HashMap<String, String>();
               MyData.put("id", "coordenadasEstaciones");

               return MyData;
           }

       };
       MyRequestQueue.add(MyStringRequest);
   }

    public void ObtenerDatosProductos(){

        //send request, display a message that nip is incorrect or let it continue to the next step
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, MainActivity.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    List<String> estaciones2 = new ArrayList<String>();
                    JSONArray cast = obj.getJSONArray("productos");
                    MyAppModelEstaciones estaciones = new MyAppModelEstaciones();
                    List<String> dato = new ArrayList<>();

                    dato.add(estaciones.getnombreEstacion());
                    dato.add(estaciones.getidEstacion());
                    ArrayList<Category> MyArraylist = new ArrayList<>();

                    for (int i=0; i<cast.length(); i++) {
                        Category genres = new Category();
                        JSONObject MyJsonObject = cast.getJSONObject(i);
                        genres.setCategory_Name(MyJsonObject.getString("nombre"));
                        MyArraylist.add(genres);


                    }
                    ListView mListViewBooks = (ListView) findViewById(R.id.listProductos);
                    final CategoryAdapter categoryAdapter = new CategoryAdapter(getApplicationContext(), R.layout.row, MyArraylist);
                    mListViewBooks.setAdapter(categoryAdapter);


                    int totalHeight = 0;
                    for (int i = 0; i < categoryAdapter.getCount(); i++) {
                        View listItem = categoryAdapter.getView(i, null, listProductos);
                        listItem.measure(0, 0);
                        totalHeight += listItem.getMeasuredHeight() + (listItem.getMeasuredHeightAndState()/2);
                    }

                    totalHeight = totalHeight -100;
                    ViewGroup.LayoutParams params = listProductos.getLayoutParams();
                    params.height = totalHeight + (listProductos.getDividerHeight() * (categoryAdapter.getCount() - 1));
                    listProductos.setLayoutParams(params);
                    listProductos.requestLayout();
                    mListViewBooks.setAdapter(categoryAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Tarjeta.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            protected Map<String, String> getParams() {
                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("id", "getProductos");

                return MyData;
            }

        };
        MyRequestQueue.add(MyStringRequest);
    }

    public void obtenerInformacionTarjeta(String idtarjeta, Context context){
        int itemsCount = list.getChildCount();
        for (int x = 0; x < itemsCount; x++) {
            View view = list.getChildAt(x);
            CheckBox check = ((CheckBox) view.findViewById(R.id.chkItem));

                check.setChecked(false);

        }
        int itemsCountProductos = listProductos.getChildCount();
        for (int x = 0; x < itemsCountProductos; x++) {
            View view = listProductos.getChildAt(x);
            CheckBox check = ((CheckBox) view.findViewById(R.id.chkItem));

            check.setChecked(false);

        }
        Lunes.setChecked(false);
        Martes.setChecked(false);
        Miercoles.setChecked(false);
        Jueves.setChecked(false);
        Viernes.setChecked(false);
        Sabado.setChecked(false);
        Domingo.setChecked(false);
        ObtenerEstacionesAutorizadas(idtarjeta, context);
       // Toast.makeText(context, idtarjeta, Toast.LENGTH_SHORT).show();
    }

    public void ActualizarTarjetas(String placas,String idtarjeta, String limitedinero, String limitelitros){

        //send request, display a message that nip is incorrect or let it continue to the next step
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, MainActivity.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                        System.out.println("actualizar tarjetas: " + response);

                    if (response.trim().equals("1")) {

                        String idcliente = sp.getString("IDCLIENTE", "DEF");
                        leerJSON(idcliente);

                        dialog.show();
                        TextView tituloDialog = dialog.findViewById(R.id.tituloDialog);
                        TextView subtituloDialog = dialog.findViewById(R.id.subtituloDialog);
                        ImageView iconoDialog = dialog.findViewById(R.id.iconoDialog);

                        tituloDialog.setText("Mensaje");
                        iconoDialog.setImageDrawable(ContextCompat.getDrawable(Tarjeta.this, R.drawable.ic_baseline_check));
                        subtituloDialog.setText("Actualizado correctamente");
                    }else{

                        dialog.show();
                        TextView tituloDialog = dialog.findViewById(R.id.tituloDialog);
                        TextView subtituloDialog = dialog.findViewById(R.id.subtituloDialog);
                        ImageView iconoDialog = dialog.findViewById(R.id.iconoDialog);

                        tituloDialog.setText("Mensaje");
                        iconoDialog.setImageDrawable(ContextCompat.getDrawable(Tarjeta.this, R.drawable.ic_baseline_error_24));
                        subtituloDialog.setText("Error al actualizar los datos");

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Tarjeta.this, "Error al actualizar" , Toast.LENGTH_SHORT).show();
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
                MyData.put("Content-Type","application/json");
                MyData.put("id", "editarTarjetaApp");
                MyData.put("idtarjeta", idtarjeta);
                MyData.put("placas", placas);
                MyData.put("limitedinero",limitedinero);
                MyData.put("limitelitros",limitelitros);

                int itemsCount = list.getChildCount();
                for (int x = 0; x < itemsCount; x++) {
                    View view = list.getChildAt(x);
                    CheckBox check = ((CheckBox) view.findViewById(R.id.chkItem));
                    String nombre = ((TextView) view.findViewById(R.id.textV)).getText().toString();


                    MyData.put("estadoestacion["+x+"]",String.valueOf(check.isChecked()));
                    MyData.put("nombreestacion["+x+"]",nombre);

                }

                int itemsCountP = listProductos.getChildCount();


                for (int x = 0; x < itemsCountP; x++) {
                    View view = listProductos.getChildAt(x);
                    CheckBox check = ((CheckBox) view.findViewById(R.id.chkItem));
                    String nombre = ((TextView) view.findViewById(R.id.textV)).getText().toString();


                    MyData.put("estadoproducto["+x+"]",String.valueOf(check.isChecked()));
                    MyData.put("nombreproducto["+x+"]",nombre);


                }

                MyData.put("lunes", checkBool(String.valueOf(Lunes.isChecked())).toString());
                MyData.put("martes", checkBool(String.valueOf(Martes.isChecked())).toString());
                MyData.put("miercoles", checkBool(String.valueOf(Miercoles.isChecked())).toString());
                MyData.put("jueves", checkBool(String.valueOf(Jueves.isChecked())).toString());
                MyData.put("viernes", checkBool(String.valueOf(Viernes.isChecked())).toString());
                MyData.put("sabado", checkBool(String.valueOf(Sabado.isChecked())).toString());
                MyData.put("domingo", checkBool(String.valueOf(Domingo.isChecked())).toString());

                MyData.put("horarioinicial", timeButtton.getText().toString());
                MyData.put("horariofinal", timeButttonFin.getText().toString());



                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);

    }


    public void ObtenerEstacionesAutorizadas(String idtarjeta, Context context){
        //send request, display a message that nip is incorrect or let it continue to the next step
        // Instantiate the cache
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, MainActivity.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    JSONObject obj = new JSONObject(response);
                    JSONArray cast = obj.getJSONArray("estaciones");


                    for (int i=0; i<cast.length(); i++) {
                        JSONObject MyJsonObject = cast.getJSONObject(i);

                        int itemsCount = list.getChildCount();
                        for (int x = 0; x < itemsCount; x++) {
                            View view = list.getChildAt(x);
                            CheckBox check = ((CheckBox) view.findViewById(R.id.chkItem));
                            String nombre = ((TextView) view.findViewById(R.id.textV)).getText().toString();


                            if(nombre.equals(MyJsonObject.getString("nombre"))){
                                check.setChecked(true);
                            }

                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "No se encontró información de las estaciones", Toast.LENGTH_SHORT).show();
                }
                try {

                    JSONObject obj = new JSONObject(response);
                    JSONArray cast = obj.getJSONArray("productos");


                    for (int i=0; i<cast.length(); i++) {
                        JSONObject MyJsonObject = cast.getJSONObject(i);
                        System.out.println(MyJsonObject.getString("nombre"));

                        int itemsCount = listProductos.getChildCount();
                        for (int x = 0; x < itemsCount; x++) {
                            View view = listProductos.getChildAt(x);
                            CheckBox check = ((CheckBox) view.findViewById(R.id.chkItem));
                            String nombre = ((TextView) view.findViewById(R.id.textV)).getText().toString();

                            if(nombre.equals(MyJsonObject.getString("nombre"))){
                                check.setChecked(true);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "No se encontró información de productos", Toast.LENGTH_SHORT).show();
                }

                try {

                    JSONObject obj = new JSONObject(response);
                    JSONArray cast = obj.getJSONArray("tarjetas");


                    for (int i=0; i<cast.length(); i++) {
                        JSONObject MyJsonObject = cast.getJSONObject(i);

                        if(MyJsonObject.getString("lunes").equals("1")){
                            Lunes.setChecked(true);
                        }
                        if(MyJsonObject.getString("martes").equals("1")){
                            Martes.setChecked(true);
                        }
                        if(MyJsonObject.getString("miercoles").equals("1")){
                            Miercoles.setChecked(true);
                        }
                        if(MyJsonObject.getString("jueves").equals("1")){
                            Jueves.setChecked(true);
                        }
                        if(MyJsonObject.getString("viernes").equals("1")){
                            Viernes.setChecked(true);
                        }
                        if(MyJsonObject.getString("sabado").equals("1")){
                            Sabado.setChecked(true);
                        }
                        if(MyJsonObject.getString("domingo").equals("1")){
                            Domingo.setChecked(true);
                        }

                        String limiteDinero = MyJsonObject.getString("limitedinero");
                        String limiteLitros = MyJsonObject.getString("limitelitros");
                        String horarioInicial = MyJsonObject.getString("horarioinicial");
                        String horarioFinal = MyJsonObject.getString("horariofinal");
                        String folio_tarjeta = MyJsonObject.getString("folio");


                        editTextLitros.setText(limiteLitros);
                        editTextDinero.setText(limiteDinero);
                        timeButtton.setText(horarioInicial);
                        timeButttonFin.setText(horarioFinal);
                        eTxtTarjeta.setText(MyJsonObject.getString("notarjeta"));
                        editTextPlacas.setText(MyJsonObject.getString("placas"));
                        editTextPeriodo.setText(formatPeriodo(MyJsonObject.getString("tipoperiodo")));

                        //tipo de limite

                        //LITROS
                        if (MyJsonObject.getString("tipolimite").contains("1"))  {
                            editTextLitros.setVisibility(View.INVISIBLE);

                            //IMPORTE
                        } else if (MyJsonObject.getString("tipolimite").contains("2")) {
                            editTextDinero.setVisibility(View.INVISIBLE);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(context, "No se encontró información tarjeta", Toast.LENGTH_SHORT).show();
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
                MyData.put("id", "getEstacionesAutorizadas");
                MyData.put("idtarjeta", idtarjeta);
                return MyData;
            }
        };
        queue.add(MyStringRequest);
    }

    private String formatPeriodo(String periodo){
        if(periodo.equals("1")){
            return "Diario";
        }
        if(periodo.equals("2")){
            return "Semanal";
        }
        if(periodo.equals("3")){
            return "Quincenal";
        }
        if(periodo.equals("4")){
            return "Mensual";
        }
        return "";
    }

    public Integer checkBool(String bool) {

        if(bool.equals("true")){
            return 1;
        }else{
            return 0;
        }

    }


}