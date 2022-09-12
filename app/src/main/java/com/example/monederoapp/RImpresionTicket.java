package com.example.monederoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class RImpresionTicket extends AppCompatActivity {
    String folio, odometro, nombreChofer;
    String value, tarjeta;
    Button btnAnterior;
    TextView textVNomEstacion, textVCalle, textVColonia, textVRzonSocial, textVRFC, textSIIC;
    TextView Tvtfechaticket, Tvtfolioticket, Tvtbombaticket, Tvtproductoticket, Infocantidadticket, Infoprecioticket, Infoimporteticket,
            TvtClienteticketCopia, TvtTarjetaticketCopia, Tvttotalticket, TvtPlacasticketCopia, TvtChoferticketCopia, TvtOdometroticketCopia,
            TvttotalMnticket, TvtAutorizacionticketCopia,TvtSaldoProxCompraticketCopia;



    String textToPrint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rimpresion_ticket);

        btnAnterior = (Button) findViewById(R.id.bttnAnterior);
        textVNomEstacion = (TextView) findViewById(R.id.textVNomEstacion);
        textVCalle = (TextView) findViewById(R.id.textVCalle);
        textVColonia = (TextView) findViewById(R.id.textVColonia);
        textVRzonSocial = (TextView) findViewById(R.id.textVRzonSocial);
        textVRFC = (TextView) findViewById(R.id.textVRFC);
        textSIIC = (TextView) findViewById(R.id.textSIIC);
        Tvtfechaticket = (TextView) findViewById(R.id.Tvtfechaticket);
        Tvtfolioticket = (TextView) findViewById(R.id.Tvtfolioticket);
        Tvtbombaticket = (TextView) findViewById(R.id.Tvtbombaticket);
        Tvtproductoticket = (TextView) findViewById(R.id.Tvtproductoticket);
        Infocantidadticket = (TextView) findViewById(R.id.Infocantidadticket);
        Infoprecioticket = (TextView) findViewById(R.id.Infoprecioticket);
        Infoimporteticket = (TextView) findViewById(R.id.Infoimporteticket);
        TvtClienteticketCopia = (TextView) findViewById(R.id.TvtClienteticketCopia);
        TvtTarjetaticketCopia = (TextView) findViewById(R.id.TvtTarjetaticketCopia);
        Tvttotalticket = (TextView) findViewById(R.id.Tvttotalticket);
        TvtPlacasticketCopia = (TextView) findViewById(R.id.TvtPlacasticketCopia);
        TvtChoferticketCopia = (TextView) findViewById(R.id.TvtChoferticketCopia);
        TvtOdometroticketCopia = (TextView) findViewById(R.id.TvtOdometroticketCopia);
        TvttotalMnticket = (TextView) findViewById(R.id.TvttotalMnticket);
        TvtAutorizacionticketCopia = (TextView) findViewById(R.id.TvtAutorizacionticketCopia);
        TvtSaldoProxCompraticketCopia = (TextView) findViewById(R.id.TvtSaldoProxCompraticketCopia);


        ObtenerDatosEstacion();

        btnAnterior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RImpresionTicket.this, MainActivity.class);
                startActivity(i);
            }
        });

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            folio = bundle.getString("folio");
            odometro = bundle.getString("odometro");
            nombreChofer = bundle.getString("nombreChofer");
        }

        ObtenerDatosServicio(folio);

    }

    public void ObtenerDatosEstacion(){
        //send request, display a message that nip is incorrect or let it continue to the next step
        // Instantiate the cache
        RequestQueue queue = Volley.newRequestQueue(RImpresionTicket.this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, "https://monedero.grupopetromar.com/apirest/despachadorapp/index.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.print("resEstacion"+response);
                    String strNew = response.replace("[", "");
                    String strNew1 = strNew.replace("]", "");
                    JSONObject obj = new JSONObject(strNew1);

                    textVNomEstacion.setText(obj.getString("nombre"));
                    textVCalle.setText(obj.getString("calle"));
                    textVColonia.setText(obj.getString("colonia"));
                    textVRzonSocial.setText(obj.getString("rzonsocial"));
                    textVRFC.setText(obj.getString("rfc"));
                    textSIIC.setText(obj.getString("siic"));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RImpresionTicket.this, "Error: ", Toast.LENGTH_SHORT).show();
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
                MyData.put("id", "getDatosEstTicket");
                MyData.put("noEstacion", "0000112394");
                return MyData;
            }
        };
        queue.add(MyStringRequest);
    }





    public void ObtenerDatosServicio(String folio){
        //send request, display a message that nip is incorrect or let it continue to the next step

        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);// <----enter your post url here
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, "https://monedero.grupopetromar.com/apirest/despachadorapp/index.php", new Response.Listener<String>() {
            @Override
            public void onResponse (String response) {
                System.out.print("res: " + response.trim());
                try {

                    JSONObject obj = new JSONObject(response.trim());
                    JSONArray servicio = obj.getJSONArray("datosservicio");
                    JSONArray otros = obj.getJSONArray("otrosdatos");
                    JSONArray datoschofer = obj.getJSONArray("nombrechofer");

                    Tvtfechaticket.setText(servicio.getJSONObject(0).getString("fecha"));
                    Tvtfolioticket.setText(servicio.getJSONObject(0).getString("folio"));
                    TvtAutorizacionticketCopia.setText(servicio.getJSONObject(0).getString("folio"));
                    Tvtproductoticket.setText(servicio.getJSONObject(0).getString("nombreproducto"));
                    //Tvtbombaticket.setText(servicio.getJSONObject(0).getString(""));

                    Infocantidadticket.setText(format(servicio.getJSONObject(0).getString("litros")));
                    Infoprecioticket.setText("$"+servicio.getJSONObject(0).getString("preciounitario"));
                    Infoimporteticket.setText("$"+format(servicio.getJSONObject(0).getString("importe")));
                    TvtClienteticketCopia.setText(servicio.getJSONObject(0).getString("nombrecliente"));
                    Tvtbombaticket.setText(servicio.getJSONObject(0).getString("bomba"));
                    Tvttotalticket.setText("$"+format(servicio.getJSONObject(0).getString("importe")));
                    TvttotalMnticket.setText(servicio.getJSONObject(0).getString("importeletra"));
                    TvtTarjetaticketCopia.setText(TvtTarjetaticketCopia.getText() + servicio.getJSONObject(0).getString("tarjeta").substring(servicio.getJSONObject(0).getString("tarjeta").length() -4));
                    TvtPlacasticketCopia.setText(otros.getJSONObject(0).getString("placas"));
                    TvtOdometroticketCopia.setText(format(odometro)+"Km");
                    TvtChoferticketCopia.setText(datoschofer.getJSONObject(0).getString("nombre"));
                    TvtSaldoProxCompraticketCopia.setText("$"+format(servicio.getJSONObject(0).getString("saldoProxima")));



                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RImpresionTicket.this, "Error datos no encontrados ", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
                Toast.makeText(RImpresionTicket.this,  error.toString(), Toast.LENGTH_SHORT).show();

            }
        }) {
            protected Map<String, String> getParams() {

                Map<String, String> MyData = new HashMap<String, String>();
                MyData.put("id", "getDatosServTicket");
                MyData.put("folio", folio);

                return MyData;
            }
        };
        MyRequestQueue.add(MyStringRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(RImpresionTicket.this, "Compilado: " , Toast.LENGTH_SHORT).show();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(RImpresionTicket.this, "Compilado: " , Toast.LENGTH_SHORT).show();
                //Printing is ok

            } else {
                if (data != null) {
                    String errorMessage = data.getStringExtra("errorMessage");

                    Toast.makeText(RImpresionTicket.this, "Error no datos" , Toast.LENGTH_SHORT).show();
                    //Printing with error
                }
            }
        }else{
            Toast.makeText(RImpresionTicket.this, "Imporewso" , Toast.LENGTH_SHORT).show();
        }
    }


    private String format(String amount){
        return NumberFormat.getNumberInstance(Locale.US).format(Double.parseDouble(amount));
    }


}