package com.allanlopez.imc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends Activity {
    private EditText nombre, peso, altura;
    private Spinner sexo;
    private String sexoSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nombre = findViewById(R.id.nombre);
        peso = findViewById(R.id.peso);
        altura = findViewById(R.id.altura);
        sexo = findViewById(R.id.sexo);
        final String[] sexos = {"Mujer", "Hombre"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, sexos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sexoSeleccionado = sexos[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sexo.setAdapter(adapter);

    }

    public void imc(View view){
        Boolean pass = true;
        String n = nombre.getText().toString();
        String p = peso.getText().toString();
        String a = altura.getText().toString();


        if (TextUtils.isEmpty(n)){
            nombre.setError("Captura un valor");
            pass = false;

        }

        if (TextUtils.isEmpty(p)){
            peso.setError("Captura un valor");
            pass = false;
        }

        if (TextUtils.isEmpty(a)){
            altura.setError("Captura un valor");
           pass = false;
        }

        if (pass){
            Persona persona = new Persona();
            persona.nombre = nombre.getText().toString();
            persona.peso = Double.parseDouble(peso.getText().toString());
            persona.altura = Double.parseDouble(altura.getText().toString());
            persona.sexo = sexoSeleccionado;

            Intent intent = new Intent(MainActivity.this, ResultadosActivity.class);
            intent.putExtra("persona", persona);
            startActivity(intent);
        }else {
            return;
        }




    }

}
