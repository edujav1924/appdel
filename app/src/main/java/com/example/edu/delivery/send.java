package com.example.edu.delivery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import studio.carbonylgroup.textfieldboxes.SimpleTextChangedWatcher;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

public class send extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);
        final TextFieldBoxes nombre = findViewById(R.id.text_field_nombre);
        nombre.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {
              if (isError){
                  nombre.setError("10 caracteres maximo",false);
              }

            }
        });
        final TextFieldBoxes apellido = findViewById(R.id.text_field_apellido);
        apellido.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {
                if (isError){
                    apellido.setError("10 caracteres maximo",false);
                }

            }
        });
        final TextFieldBoxes telefono = findViewById(R.id.text_telefono);
        telefono.setSimpleTextChangeWatcher(new SimpleTextChangedWatcher() {
            @Override
            public void onTextChanged(String theNewText, boolean isError) {
                if (isError){
                    telefono.setError("10 caracteres maximo",false);
                }
            }
        });
    }
}
