package com.example.monederoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    public static final String url = "https://monedero.grupopetromar.com/apirest/index.php" ;

    public CardView Tarjetas, Vehiculos, Historial,Estaciones;
    TextView uno, dos, tres, cuatro, cinco, bienvenido;
    private Button log_out;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Tarjetas = (CardView) findViewById(R.id.Tarjetas);
        Vehiculos = (CardView) findViewById(R.id.Vehiculos);
        Estaciones = (CardView) findViewById(R.id.Estaciones);
        Historial = (CardView) findViewById(R.id.Historial);
        bienvenido = (TextView) findViewById(R.id.bienvenido);


        Tarjetas.setOnClickListener(this);
        Vehiculos.setOnClickListener(this);
        Estaciones.setOnClickListener(this);
        Historial.setOnClickListener(this);


        log_out = (Button) findViewById(R.id.btnLog_out);
        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sp.edit().remove("USER_NAME").commit();
                sp.edit().remove("PASSWORD").commit();
                sp.edit().remove("IDCLIENTE").commit();
                sp.edit().remove("RZONSOCIAL").commit();
                sp.edit().remove("rem_isCheck").commit();
                sp.edit().remove("auto_isCheck").commit();
                startActivity(new Intent(getApplicationContext(),Login.class));

            }
        });
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String a = sp.getString("USER_NAME", "DEF");

        bienvenido.setText(bienvenido.getText() + " \n" + sp.getString("USER_NAME", "---"));

        ObtenerDatos(sp.getString("IDCLIENTE", "NA"));


    }

    public void ObtenerDatos(String idcliente){
        //send request, display a message that nip is incorrect or let it continue to the next step
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println("RESSS: " + response);
                    String strNew = response.replace("[", "");
                    String strNew1 = strNew.replace("]", "");
                    JSONObject obj = new JSONObject(strNew1);
                    uno = (TextView) findViewById(R.id.txviewrzon);
                    dos = (TextView) findViewById(R.id.txviewtiposaldo);
                    tres = (TextView) findViewById(R.id.txviewsaldo);
                    cuatro = (TextView) findViewById(R.id.txviewtarjetas);
                    cinco = (TextView) findViewById(R.id.txviewtrans);
                    uno.setText(obj.getString("rzonsocial"));
                    if(obj.getString("rzonsocial").equals("0")){
                        dos.setText("Saldo Disponible: ");
                        tres.setText("$" +format(obj.getString("saldocontado")));
                    }else{
                        
                        dos.setText("Credito Disponible: ");
                        double limiteCredito, saldoporPagar;
                        try { limiteCredito = Double.parseDouble(obj.getString("saldocontado")); } catch (Exception e){limiteCredito = 0;}
                        try { saldoporPagar = Double.parseDouble(obj.getString("saldoporpagar"));} catch (Exception e) {saldoporPagar = 0;}
                        tres.setText("$" + format(String.valueOf(limiteCredito - saldoporPagar)));
                    }
                    cuatro.setText(obj.getString("tarjetas"));
                    cinco.setText(obj.getString("servicios")+ " / $"+format((obj.getString("importedia"))));
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                MyData.put("id", "tarjetasCTE");
                MyData.put("idcliente", idcliente);
                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);
    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){
            case R.id.Tarjetas:
                i = new Intent(this,Tarjeta.class);
                startActivity(i);
                break;
            case R.id.Vehiculos:
                i = new Intent(this,Vehiculos.class);
                startActivity(i);
                break;
            case R.id.Estaciones:
                i = new Intent(this,Estaciones.class);
                startActivity(i);
                break;
            case R.id.Historial:
                i = new Intent(this,Historial.class);
                startActivity(i);
                break;
        }
    }


    private String format(String amount){
        System.out.println("cantidad: " + amount);
        if(amount.equals("null")){
            return "$0.00";
        }else{
            return NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(amount));
        }

    }

}