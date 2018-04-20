package com.example.edu.delivery;

import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.example.circulardialog.CDialog;
import com.example.circulardialog.extras.CDConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

import greco.lorenzo.com.lgsnackbar.LGSnackbarManager;
import greco.lorenzo.com.lgsnackbar.core.LGSnackbar;
import greco.lorenzo.com.lgsnackbar.style.LGSnackBarStyle;
import greco.lorenzo.com.lgsnackbar.style.LGSnackBarTheme;
import studio.carbonylgroup.textfieldboxes.SimpleTextChangedWatcher;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

import static android.widget.Toast.LENGTH_LONG;

public class send extends AppCompatActivity {
    private boolean aux_nombre=true;
    private boolean aux_apellido=true;
    private boolean aux_telefono=true;
    private JSONObject datos;
    String nombre_text,apellido_text,telefono_text;
    private  TextFieldBoxes nombre,apellido,telefono;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        try {
            datos = new JSONObject(getIntent().getExtras().getString("datos"));
            Log.e("datos",datos.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        nombre = findViewById(R.id.text_field_nombre);
        nombre.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {
                nombre_text = theNewText;
              if (isError){
                  nombre.setError("3 caracteres minimo",false);
                  aux_nombre = true;
              }
              else{
                  aux_nombre = false;
              }
              verify();
            }
        });
        apellido = findViewById(R.id.text_field_apellido);
        apellido.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {
                apellido_text = theNewText;
                if (isError){
                    apellido.setError("3 caracteres minimo",false);
                    aux_apellido = true;
                }
                else{
                    aux_apellido = false;
                }
                verify();
            }
        });
        telefono = findViewById(R.id.text_telefono);
        telefono.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {
                telefono_text = theNewText;
                if (isError){
                    telefono.setError("3 caracteres minimo",false);
                    aux_telefono = true;
                }
                else {
                    aux_telefono = false;
                }
                verify();
            }
        });
    }
    public void verify(){
        if(!aux_apellido && !aux_nombre && !aux_telefono){
            findViewById(R.id.button_enviar).setEnabled(true);
        }
        else {
            findViewById(R.id.button_enviar).setEnabled(false);

        }
    }
    public void enviar(View v){
        try {

            datos.put("nombre",nombre_text);
            datos.put("apellido",apellido_text);
            datos.put("celular",telefono_text);
            Log.e("datos",datos.toString());

            RequestQueue request = Volley.newRequestQueue(this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, "http://192.168.43.158:8000/cliente/",datos, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                String valor = jsonObject.getString("status");
                                if (valor.equals("exitoso")){
                                    new CDialog(send.this).createAlert(valor,
                                            CDConstants.SUCCESS,   // Type of dialog
                                            CDConstants.LARGE)    //  size of dialog
                                            .setAnimation(CDConstants.SCALE_FROM_RIGHT_TO_LEFT)     //  Animation for enter/exit
                                            .setDuration(3000)   // in milliseconds
                                            .setTextSize(CDConstants.NORMAL_TEXT_SIZE)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                                            .show();
                                    Thread thread = new Thread(){
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(3500); // As I am using LENGTH_LONG in Toast
                                                send.this.finish();

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    thread.start();

                                }else {
                                    new CDialog(send.this).createAlert(valor,
                                            CDConstants.WARNING,   // Type of dialog
                                            CDConstants.LARGE)    //  size of dialog
                                            .setAnimation(CDConstants.SCALE_FROM_RIGHT_TO_LEFT)     //  Animation for enter/exit
                                            .setDuration(5000)   // in milliseconds
                                            .setTextSize(CDConstants.NORMAL_TEXT_SIZE)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                                            .show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {

                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                alert("Error al conectar con el servidor",0);
                            } else if (error instanceof AuthFailureError) {
                                alert("Error de autenticacion",0);

                            } else if (error instanceof ServerError) {

                                alert("Error interna del servidor",0);

                            } else if (error instanceof NetworkError) {

                                alert("Error de conexion",0);
                            } else if (error instanceof ParseError) {

                                alert("Error de formato de datos",0);
                            }
                        }
                    });

            request.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void alert(String text, int type){
        switch (type){
            case 0:
                new LGSnackbar.LGSnackbarBuilder(getApplicationContext(), text)
                        .duration(3000)
                        .actionTextColor(Color.RED)
                        .backgroundColor(Color.RED)
                        .minHeightDp(50)
                        .textColor(Color.WHITE)
                        .iconID(R.drawable.ic_custom_error)
                        .callback(null)
                        .action(null)
                        .show();
                break;
            case 1:
                new LGSnackbar.LGSnackbarBuilder(getApplicationContext(), text)
                        .duration(3000)
                        .actionTextColor(Color.RED)
                        .backgroundColor(Color.RED)
                        .minHeightDp(50)
                        .textColor(Color.WHITE)
                        .iconID(R.drawable.ic_custom_error)
                        .callback(null)
                        .action(null)
                        .show();
                break;
        }

    }
}
