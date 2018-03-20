package mx.itesm.edu.earthone.spherochallenge;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.orbotix.ConvenienceRobot;
import com.orbotix.DualStackDiscoveryAgent;
import com.orbotix.async.CollisionDetectedAsyncData;
import com.orbotix.common.DiscoveryException;
import com.orbotix.common.ResponseListener;
import com.orbotix.common.Robot;
import com.orbotix.common.RobotChangedStateListener;
import com.orbotix.common.internal.AsyncMessage;
import com.orbotix.common.internal.DeviceResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends Activity implements RobotChangedStateListener, ResponseListener, TextToSpeech.OnInitListener {

    private Button go, stop, izq,der,back;
    private TextToSpeech textToSpeech = null;private final int CHECK_TTS = 1000;
    private final int CHECK_STT = 1007;


    private TextView textView;
    private EditText editText;
    private Button bRead, bListen;

    private final int REQUEST_PERMISSION = 42;    //Pedir permiso
    private float ROBOT_SPEED = 0.2f;   //Velocidad de robot

    private int direction;
    private ConvenienceRobot convenienceRobot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        go = (Button) findViewById(R.id.go);
        stop = (Button) findViewById(R.id.stop);
        izq = (Button) findViewById(R.id.izq);
        der = (Button) findViewById(R.id.der);
        back = (Button) findViewById(R.id.back);

        bRead = (Button) findViewById(R.id.bTalk);
        bListen = (Button) findViewById(R.id.bListen);
        editText = (EditText) findViewById(R.id.eText);
        textView = (TextView) findViewById(R.id.textView);
        bRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String texto = editText.getText().toString();
                talkToMe(texto, 1);
            }
        });

        bListen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivityForResult(intent, CHECK_STT);
                }else{
                    Toast.makeText(getApplicationContext(), "You do not have Speech To Text", Toast.LENGTH_LONG).show();
                }
            }
        });

        Intent ttsIntent = new Intent();
        ttsIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(ttsIntent, CHECK_TTS);


        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convenienceRobot.setLed(0.5f,0.5f,0f);
                direction = 180;
                convenienceRobot.drive(direction, ROBOT_SPEED);

            }
        });

        izq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convenienceRobot.setLed(0.0f,0.5f,0f);
                direction = 90;
                convenienceRobot.drive(direction, ROBOT_SPEED);

            }
        });

        der.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convenienceRobot.setLed(0.0f,0.0f,1.0f);
                direction = 270;
                convenienceRobot.drive(direction, ROBOT_SPEED);

            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convenienceRobot.setLed(1.0f,0.0f,0.0f);
                direction = 0;
                convenienceRobot.drive(direction, ROBOT_SPEED);

            }
        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                convenienceRobot.setLed(0.7201f,0.5643f,0.8221f);
                convenienceRobot.stop();

            }
        });
        DualStackDiscoveryAgent.getInstance().addRobotStateListener(this);
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            int hasLocationPermission = checkSelfPermission( Manifest.permission.ACCESS_COARSE_LOCATION );
            if( hasLocationPermission != PackageManager.PERMISSION_GRANTED ) {
                Log.e( "Sphero", "Location permission has not already been granted" );
                List<String> permissions = new ArrayList<String>();
                permissions.add( Manifest.permission.ACCESS_COARSE_LOCATION);
                requestPermissions(permissions.toArray(new String[permissions.size()] ), REQUEST_PERMISSION );
            } else {
                Log.d( "Sphero", "Location permission already granted" );
            }
        }
    }

    @Override
    public void handleResponse(DeviceResponse deviceResponse, Robot robot) {

    }

    @Override
    public void handleStringResponse(String s, Robot robot) {

    }

    @Override
    public void handleAsyncMessage(AsyncMessage asyncMessage, Robot robot) {
        if( asyncMessage instanceof CollisionDetectedAsyncData) {
            talkToMe("Ay me dolió mucho", 0);
        }
    }

    @Override
    public void handleRobotChangedState(Robot robot, RobotChangedStateNotificationType robotChangedStateNotificationType) {
        switch (robotChangedStateNotificationType){
            case Online:
                convenienceRobot = new ConvenienceRobot(robot);
                convenienceRobot.addResponseListener(this);
                convenienceRobot.enableCollisions(true);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch ( requestCode ) {
            case REQUEST_PERMISSION: {
                for( int i = 0; i < permissions.length; i++ ) {
                    if( grantResults[i] == PackageManager.PERMISSION_GRANTED ) {
                        startDiscovery();
                        Log.d( "Permissions", "Permission Granted: " + permissions[i] );
                    } else if( grantResults[i] == PackageManager.PERMISSION_DENIED ) {
                        Log.d( "Permissions", "Permission Denied: " + permissions[i] );
                    }
                }
            }
            break;
            default: {
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        startDiscovery();

    }

    private void startDiscovery() {
        if( !DualStackDiscoveryAgent.getInstance().isDiscovering() ) {
            try {
                DualStackDiscoveryAgent.getInstance().startDiscovery( this );
            } catch (DiscoveryException e) {
                Log.e("Sphero", "DiscoveryException: " + e.getMessage());
            }
        }
    }

    @Override
    protected void onStop() {
        if( DualStackDiscoveryAgent.getInstance().isDiscovering() ) {
            DualStackDiscoveryAgent.getInstance().stopDiscovery();
        }
        if( convenienceRobot != null ) {
            convenienceRobot.disconnect();
            convenienceRobot = null;
        }

        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case CHECK_TTS:
                if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                    textToSpeech = new TextToSpeech(this, this);
                } else {

                    Intent installIntent = new Intent();
                    installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                    startActivity(installIntent);
                }
                break;
            case CHECK_STT:
                if(resultCode == RESULT_OK && data != null){
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    textView.setText(result.get(0));
                    switch (result.get(0)){
                        case "adelante":
                            convenienceRobot.setLed(0.5f,0.5f,0f);
                            direction = 180;
                            convenienceRobot.drive(direction, ROBOT_SPEED);
                            break;

                        case "abajo":
                            convenienceRobot.setLed(1.0f,0.0f,0.0f);
                            direction = 0;
                            convenienceRobot.drive(direction, ROBOT_SPEED);
                            break;

                        case "izquierda":
                            convenienceRobot.setLed(0.0f,0.5f,0f);
                            direction = 90;
                            convenienceRobot.drive(direction, ROBOT_SPEED);
                            break;

                        case "derecha":
                            convenienceRobot.setLed(0.0f,0.0f,1.0f);
                            direction = 270;
                            convenienceRobot.drive(direction, ROBOT_SPEED);
                            break;
                    }
                }

                break;

        }


    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            if (textToSpeech != null) {
                int result = textToSpeech.setLanguage(Locale.getDefault());
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                        result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(this, "TTS language is not supported", Toast.LENGTH_LONG).show();
                } else {
                    talkToMe("TTS is ready", 0);
                }
            }
        } else {
            Toast.makeText(this, "TTS initialization failed",
                    Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    private void talkToMe(String text, int qmode) {
        if (qmode == 1)
            textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, null);
        else
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}
