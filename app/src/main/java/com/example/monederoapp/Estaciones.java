package com.example.monederoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Estaciones extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estaciones);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng MZT = new LatLng(23.235668, -106.423103);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(MZT));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(MZT)      // Sets the center of the map to Mountain View
                .zoom(12)                   // Sets the zoom
                .bearing(0)                // Sets the orientation of the camera to east
                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        ObtenerDatos();

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                View view = getLayoutInflater().inflate(R.layout.marker_info, null);

                TextView textViewTitle = (TextView) view.findViewById(R.id.marker_title);
                textViewTitle.setText(marker.getTitle());

                TextView textViewSnippet = (TextView) view.findViewById(R.id.marker_snippet);
                textViewSnippet.setText(marker.getSnippet());

                return view;
            }
        });

    }
    public void ObtenerDatos(){
        //send request, display a message that nip is incorrect or let it continue to the next step
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, MainActivity.url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    List<String> estaciones = new ArrayList<String>();
                    JSONArray cast = obj.getJSONArray("estaciones");

                    JSONArray precios = obj.getJSONArray("precios");
                    for (int i=0; i<cast.length(); i++) {


                        try {

                            JSONObject estacion = cast.getJSONObject(i);

                            String pr = "";
                            String est = estacion.getString("idestacion");
                            for (int a=0; a<precios.length(); a++) {
                                JSONObject precio = precios.getJSONObject(a);
                               if(est.equals(precio.getString("idestacionprod")) ) {
                                   pr = pr + " " +precio.getString("nombreprod") + ": $" + precio.getString("preciouni") ;
                                   System.out.println(precio.getString("idestacionprod") + " " + precio.getString("nombreprod"));
                               }

                            }


                            LatLng sydney = new LatLng(Double.parseDouble(estacion.getString("lat")), Double.parseDouble(estacion.getString("longi")));
                            mMap.addMarker(new MarkerOptions()
                                    .position(sydney)
                                    .snippet(pr)
                                    .title(estacion.getString("nombre"))


                            );
                            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                @Override
                                public boolean onMarkerClick(Marker marker) {
                                    // on marker click we are getting the title of our marker
                                    // which is clicked and displaying it in a toast message.
                                    String markerName = marker.getTitle();
                                    String snippet = marker.getSnippet();
                                  //  Toast.makeText(Estaciones.this, snippet, Toast.LENGTH_SHORT).show();
                                    return false;
                                }
                            });


                        } catch (Exception e){

                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Estaciones.this, "Error: " + e.toString(), Toast.LENGTH_SHORT).show();
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




}