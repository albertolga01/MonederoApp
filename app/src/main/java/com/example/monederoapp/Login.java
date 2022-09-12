package com.example.monederoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    Button btnLogin, btnLo;
    EditText eTxtUsuario, eTxtContraseña;
    private RequestQueue requestQueue;
    private static final String URL = "https://monedero.grupopetromar.com/apirest/index.php";
    private StringRequest request;
    

    private CheckBox remember_key; // Recordar la casilla de verificación de contraseña
    private CheckBox automatic_login; // Casilla de verificación de inicio de sesión automático
    private SharedPreferences sp;

    private String userNameValue;
    private String passwordValue;

    private Context mContext = this;

    private Boolean rem_isCheck = true;
    private Boolean auto_isCheck = true;

    private Dialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);

        eTxtUsuario = (EditText) findViewById(R.id.eTxtUsuario);
        eTxtContraseña = (EditText) findViewById(R.id.eTxtContraseña);
        remember_key = (CheckBox) findViewById(R.id.remember_key);
        automatic_login = (CheckBox) findViewById(R.id.automatic_login);


        requestQueue = Volley.newRequestQueue(this);

        // Abrir Preferencias, el nombre es userInfo, si existe, ábralo, de lo contrario cree una nueva Preferencias
        //Context.MODE_PRIVATE: especifica que los datos de SharedPreferences solo pueden ser leídos y escritos por esta aplicación
        //Context.MODE_WORLD_READABLE: especifica que los datos de SharedPreferences pueden ser leídos por otras aplicaciones, pero no pueden escribirse
        //Context.MODE_WORLD_WRITEABLE: especifica que los datos de Preferencias Compartidas pueden ser leídos y escritos por otras aplicaciones.
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        rem_isCheck = remember_key.isChecked();
        auto_isCheck = automatic_login.isChecked();

        remember_key.setChecked(true); // Establezca la contraseña de recuerdo para que se inicialice en true


        // Juzgar el estado de la casilla de verificación recordar contraseña
        if (sp.getBoolean("rem_isCheck", false)) {
            // La configuración predeterminada es registrar el estado de la contraseña
            remember_key.setChecked(true);
            eTxtUsuario.setText(sp.getString("USER_NAME", ""));
            eTxtContraseña.setText(sp.getString("PASSWORD", ""));
            Log.e("Restaurar contraseña", "Restaurar automáticamente la contraseña de la cuenta guardada");

            // Juzgar el estado de la casilla de verificación de inicio de sesión automático
            if (sp.getBoolean("auto_isCheck", false)) {
                // Establecer el valor predeterminado es el estado de inicio de sesión automático
                automatic_login.setChecked(true);
                // interfaz de salto
                Intent intent = new Intent(Login.this, MainActivity.class);
                Login.this.startActivity(intent);
                Toast toast1 = Toast.makeText(getApplicationContext(), "Ya inició sesión automáticamente", Toast.LENGTH_SHORT);
                Log.e("Inicio sesiónautomático", "Inicio de sesión automático");

            }
        }

        // Método de monitoreo del botón de inicio de sesión

        //send request, display a message that nip is incorrect or let it continue to the next step
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                userNameValue = eTxtUsuario.getText().toString();
                passwordValue = eTxtContraseña.getText().toString();

                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit().clear().apply();

                login(userNameValue,passwordValue);
              //  SharedPreferences setting = Login.this.getSharedPreferences("", )
              //  sp.edit().clear().commit();

                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                @SuppressLint("MissingPermission") NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    Log.d("MIAPP", "Estás online");

                    Log.d("MIAPP", " Estado actual: " + networkInfo.getState());

                    if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        // Estas conectado a un Wi-Fi

                        Log.d("MIAPP", " Nombre red Wi-Fi: " + networkInfo.getExtraInfo());
                    } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE){

                        Log.d("MIAPP", " Nombre red: " + networkInfo.getExtraInfo());

                    }

                } else {
                    Log.d("MIAPP", "Estás offline");
                    dialog.show();
                    TextView tituloDialog = dialog.findViewById(R.id.tituloDialog);
                    TextView subtituloDialog = dialog.findViewById(R.id.subtituloDialog);
                    ImageView iconoDialog = dialog.findViewById(R.id.iconoDialog);

                    tituloDialog.setText("Alerta");
                    iconoDialog.setImageDrawable(ContextCompat.getDrawable(Login.this, R.drawable.ic_baseline_error_24));
                    subtituloDialog.setText("No estas conectado a internet");

                }

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
    public void login(String userNameValue, String passwordValue){

        String url = "https://monedero.grupopetromar.com/apirest/index.php"; // <----enter your post url here
        request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {   System.out.println("res:   "+response);
                try {


                    String strNew = response.replace("[", "");
                    String strNew1 = strNew.replace("]", "");
                    JSONObject obj = new JSONObject(strNew1);
                    if(obj.getString("res").equals("1")) {
                        System.out.println("Respuesta: " + obj.getString("usuario"));
                        if (remember_key.isChecked()) {
                            // Recuerda nombre de usuario, contraseña,
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("USER_NAME", userNameValue);
                            editor.putString("PASSWORD", passwordValue);
                            editor.putString("IDCLIENTE", obj.getString("idcliente"));
                            editor.putString("RZONSOCIAL", obj.getString("rzonsocial"));
                            editor.putBoolean("rem_isCheck", remember_key.isChecked());
                            editor.putBoolean("auto_isCheck", automatic_login.isChecked());
                            editor.commit();

                            Log.e("Guarde la contraseña", "Cuenta:" + userNameValue +
                                    "\n" + "Contraseña:" + passwordValue +
                                    "\n" + "¿Recuerdas la contraseña:" + rem_isCheck +
                                    "\n" + "Si iniciar sesión automáticamente:" + auto_isCheck);
                            editor.commit();
                            // interfaz de salto
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            Login.this.startActivity(intent);
                        }
                    }else {

                        dialog.show();
                        TextView tituloDialog = dialog.findViewById(R.id.tituloDialog);
                        TextView subtituloDialog = dialog.findViewById(R.id.subtituloDialog);
                        ImageView iconoDialog = dialog.findViewById(R.id.iconoDialog);

                        tituloDialog.setText("Mensaje");
                        iconoDialog.setImageDrawable(ContextCompat.getDrawable(Login.this, R.drawable.ic_baseline_error_24));
                        subtituloDialog.setText("El nombre de usuario o la contraseña son incorrectos, vuelva a iniciar sesión");
                    }

                    //finish();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Login.this, "Error"+e.toString(), Toast.LENGTH_LONG).show();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("id", "C_login");
                hashMap.put("user", eTxtUsuario.getText().toString());
                hashMap.put("pass", eTxtContraseña.getText().toString());

                return hashMap;
            }
        };

        requestQueue.add(request);
    }
}