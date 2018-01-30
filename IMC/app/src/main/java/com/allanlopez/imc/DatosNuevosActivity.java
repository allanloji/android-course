package com.allanlopez.imc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class DatosNuevosActivity extends Activity {
    private EditText peso, altura;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_nuevos);

        peso = findViewById(R.id.peso);
        altura = findViewById(R.id.altura);
    }

    public void imc(View view){
        Boolean pass = true;
        String p = peso.getText().toString();
        String a = altura.getText().toString();

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
            persona.peso = Double.parseDouble(peso.getText().toString());
            persona.altura = Double.parseDouble(altura.getText().toString());

            Intent intent = new Intent();
            intent.putExtra("persona", persona);
            setResult(RESULT_OK, intent);
            super.finish();
        }else {
            return;
        }




    }
}
