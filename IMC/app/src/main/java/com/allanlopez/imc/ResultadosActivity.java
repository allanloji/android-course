package com.allanlopez.imc;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class ResultadosActivity extends Activity {
    private TextView nombre, imc, ideal, energia;
    private ImageView cuerpo;
    private final int REQUEST_CODE = 7007;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);

        nombre = findViewById(R.id.nombre);
        imc = findViewById(R.id.imc);
        ideal = findViewById(R.id.ideal);
        energia = findViewById(R.id.energia);
        cuerpo = findViewById(R.id.cuerpo);

        Persona p = (Persona) getIntent().getSerializableExtra( "persona");
        calculos(p);

    }

    public void recalcular(View view){
        Intent intent = new Intent( this, DatosNuevosActivity.class);
        startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            if (data.hasExtra("persona")){
                Persona nuevoDato = (Persona) data.getSerializableExtra("persona");
                Persona p = (Persona) getIntent().getSerializableExtra( "persona");
                p.peso = nuevoDato.peso;
                p.altura = nuevoDato.altura;
                calculos(p);
            }
        }
    }

    public void openBMI(View view){
        String url = "https://es.wikipedia.org/wiki/%C3%8Dndice_de_masa_corporal";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void calculos(Persona p){
        double imcDouble = p.peso / Math.pow(p.altura, 2.0);
        double idealDouble = Math.pow(p.altura, 2.0) * 22;
        double energiaDouble = idealDouble * 30;

        nombre.setText(p.nombre);

        imc.setText(String.valueOf(imcDouble));
        ideal.setText(String.valueOf(idealDouble));
        energia.setText(String.valueOf(energiaDouble));

        if (p.sexo.equalsIgnoreCase("mujer")){
            if (imcDouble <= 17.5){
                cuerpo.setImageResource(R.drawable.woman_bmi_17_5);
            }else if (imcDouble <= 18.5){
                cuerpo.setImageResource(R.drawable.woman_bmi_18_5);
            }else if (imcDouble <= 22.0){
                cuerpo.setImageResource(R.drawable.woman_bmi_22);
            }else if (imcDouble <= 24.9){
                cuerpo.setImageResource(R.drawable.woman_bmi_24_9);
            }else if (imcDouble <= 30.0){
                cuerpo.setImageResource(R.drawable.woman_bmi_30);
            }else {
                cuerpo.setImageResource(R.drawable.woman_bmi_40);
            }
        }else {
            if (imcDouble <= 17.5){
                cuerpo.setImageResource(R.drawable.men_bmi_17_5);
            }else if (imcDouble <= 18.5){
                cuerpo.setImageResource(R.drawable.men_bmi_18_5);
            }else if (imcDouble <= 22.0){
                cuerpo.setImageResource(R.drawable.men_bmi_22_0);
            }else if (imcDouble <= 24.9){
                cuerpo.setImageResource(R.drawable.men_bmi_24_9);
            }else if (imcDouble <= 30.0){
                cuerpo.setImageResource(R.drawable.men_bmi_30);
            }else {
                cuerpo.setImageResource(R.drawable.men_bmi_40);
            }
        }
    }

    public void compartirResultados(View view){
        Intent textShareIntent = new Intent(Intent.ACTION_SEND);
        textShareIntent.putExtra(Intent.EXTRA_SUBJECT, "Mensaje");
        textShareIntent.putExtra(Intent.EXTRA_TEXT, "Mi IMC es: " + imc.getText().toString());
        textShareIntent.setType("text/plain");
        startActivity(Intent.createChooser(textShareIntent, "Compartir con ..."));

    }

    public void reiniciar(View view){
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
