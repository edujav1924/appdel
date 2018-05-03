package com.example.edu.delivery;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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
import com.example.circulardialog.CDialog;
import com.example.circulardialog.extras.CDConstants;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import greco.lorenzo.com.lgsnackbar.core.LGSnackbar;
import studio.carbonylgroup.textfieldboxes.SimpleTextChangedWatcher;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class send extends AppCompatActivity {
    private boolean aux_nombre=true;
    private boolean aux_apellido=true;
    private boolean aux_telefono=true;
    private JSONObject datos;
    basedatos mDbHelper;
    CheckBox remember,recubi;
    int flag=-1;
    String nombre_text,apellido_text,telefono_text;
    private  TextFieldBoxes nombre,apellido,telefono;
    EditText nombre_box,apellido_box,celular_box;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        remember = findViewById(R.id.recordar);
        nombre_box= findViewById(R.id.box_edit_nombre);
        apellido_box = findViewById(R.id.box_edit_apellido);
        celular_box = findViewById(R.id.box_edit_telefono);
        recubi = findViewById(R.id.recubi);
        mDbHelper= new basedatos(send.this);

        try {
            flag = leerinfo();
            verify();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"erroralleerinfo",Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        try {
            datos = new JSONObject(getIntent().getExtras().getString("datos"));
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
            String token = FirebaseInstanceId.getInstance().getToken();
            datos.put("token",token);
            datos.put("nombre",nombre_box.getText().toString());
            datos.put("apellido",apellido_box.getText().toString());
            datos.put("celular",celular_box.getText().toString());

            RequestQueue request = Volley.newRequestQueue(this);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, "https://192.168.43.158:443/cliente/",datos, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response.toString());
                                String valor = jsonObject.getString("status");
                                Log.e("respnse",valor);
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
                                    if(remember.isChecked() || recubi.isChecked() ){
                                        Thread hilo = new Thread(){
                                            @Override
                                            public void run() {
                                                try {
                                                    if (flag==0){
                                                        guardarinfo(nombre_text,apellido_text,telefono_text,remember.isChecked(),recubi.isChecked());
                                                    }else if(flag>0){
                                                        actualizar();
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };
                                        hilo.start();
                                    }


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
    private void actualizar(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        ContentValues values = new ContentValues();
        if (remember.isChecked()){
            values.put(basedatos.FeedEntry.COLUMN_NOMBRE, nombre_box.getText().toString());
            values.put(basedatos.FeedEntry.COLUMN_APELLIDO, apellido_box.getText().toString());
            values.put(basedatos.FeedEntry.COLUMN_CELULAR ,celular_box.getText().toString());

        }
        if(recubi.isChecked()){
            try {
                values.put(basedatos.FeedEntry.COLUMN_LONGITUD,datos.getString("longitud"));
                values.put(basedatos.FeedEntry.COLUMN_LATITUD,datos.getString("latitud"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String selection = basedatos.FeedEntry._ID +"=?";
        String[] selectionArgs = { "1" };

        int count = db.update(
                basedatos.FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        db.close();
    }

    private int leerinfo() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                basedatos.FeedEntry._ID,
                basedatos.FeedEntry.COLUMN_NOMBRE,
                basedatos.FeedEntry.COLUMN_APELLIDO,
                basedatos.FeedEntry.COLUMN_CELULAR,
                basedatos.FeedEntry.COLUMN_LONGITUD,
                basedatos.FeedEntry.COLUMN_LATITUD

        };
        String selection = basedatos.FeedEntry._ID +"=?";
        String[] selectionArgs = { "1" };

        //Cursor c = db.rawQuery(" SELECT * FROM entry Where 'basedatos.FeedEntry._ID=0' ", null);
        Cursor c = db.query(basedatos.FeedEntry.TABLE_NAME,projection,selection, selectionArgs,null,null,null);
        c.moveToFirst();
        if(c.getCount()>0){
            Log.e("id", String.valueOf(c.getCount()));
            Log.e("id",c.getString(1));
            Log.e("id1",c.getString(2));
            Log.e("id2",c.getString(3));
            nombre_box.setText(c.getString(1));
            apellido_box.setText(c.getString(2));
            celular_box.setText(c.getString(3));
            aux_telefono = false;
            aux_nombre = false;
            aux_apellido =false;


        }
        else{
            Log.e("status", "vacio");
        }
        db.close();
        return c.getCount();

    }

    private void guardarinfo(String nombre, String apellido, String celular, boolean datchecked, boolean recubiChecked) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        if (datchecked) {
            values.put(basedatos.FeedEntry.COLUMN_NOMBRE, nombre);
            values.put(basedatos.FeedEntry.COLUMN_APELLIDO, apellido);
            values.put(basedatos.FeedEntry.COLUMN_CELULAR, celular);
        }

        try {
            if(recubiChecked){
                values.put(basedatos.FeedEntry.COLUMN_LONGITUD,datos.getString("longitud"));
                values.put(basedatos.FeedEntry.COLUMN_LATITUD,datos.getString("latitud"));
            }else{
                values.put(basedatos.FeedEntry.COLUMN_LONGITUD,"0");
                values.put(basedatos.FeedEntry.COLUMN_LATITUD,"0");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(basedatos.FeedEntry.TABLE_NAME, null, values);
        db.close();
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
