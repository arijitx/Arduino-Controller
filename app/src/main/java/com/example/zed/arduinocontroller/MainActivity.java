package com.example.zed.arduinocontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements RecognitionListener{


    private Intent recognizerIntent;
    private SpeechRecognizer spR;
    boolean onSpeech=false;
    private Button bU,bD,bL,bR,bCon,bSpeech,bRR,bRL;
    private ProgressBar pb;
    TextView tvCmd;
    EditText etBname;
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    private TextView myLabel;
    @Override
    protected void onPostResume() {
        super.onPostResume();
        spR.startListening(recognizerIntent);
    }
    @Override
    public void onReadyForSpeech(Bundle params) {
        //Toast.makeText(getApplicationContext(),"Speech Ready",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onBeginningOfSpeech() {
        //Toast.makeText(getApplicationContext(),"Speech Started",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRmsChanged(float rmsdB) {

        pb.setProgress((int) rmsdB * pb.getMax() / 20);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {
        //Toast.makeText(getApplicationContext(),"Speech Ended",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {

        ArrayList<String> res=results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        String text=res.get(0).toLowerCase();
        tvCmd.setText(text);
        spR.startListening(recognizerIntent);
        if (text.contains("turn")){
            if (text.contains("left")){
                sendData2('L');
            }
            if(text.contains("right")){
                sendData2('R');
            }
        }else if (text.contains("move")){
            if(text.contains("forward")){
                sendData2('U');
            }
            if(text.contains("backward") || text.contains("back")){
                sendData2('D');
            }

        }else if(text.contains("rotate")){
            if (text.contains("left")){
                sendData2('e');
            }
            if(text.contains("right")){
                sendData2('i');
            }
        }


    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //intialization of all stuffs
        bU=(Button)findViewById(R.id.btnUp);
        bD=(Button)findViewById(R.id.btnDown);
        bL=(Button)findViewById(R.id.btnLeft);
        bR=(Button)findViewById(R.id.btnRight);
        bCon=(Button)findViewById(R.id.btnCon);
        bSpeech=(Button)findViewById(R.id.btnStartSpeech);
        bRR=(Button)findViewById(R.id.btnRR);
        bRL=(Button)findViewById(R.id.btnRL);
        pb=(ProgressBar)findViewById(R.id.progressBar);
        etBname=(EditText)findViewById(R.id.etBname);
        myLabel=(TextView)findViewById(R.id.myLabel);
        tvCmd=(TextView)findViewById(R.id.tvCmd);
        spR=SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        spR.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);


        bCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    openBT();
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        bU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendData('U');
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        bR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendData('R');
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        bD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendData('D');
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        bL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendData('L');
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        bRR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendData('i');
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        bRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendData('e');
                }catch (Exception ex){
                    Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        bSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSpeech=!onSpeech;
                if (onSpeech) {
                    bSpeech.setText("Stop Speech");
                    spR.startListening(recognizerIntent);
                }
                else{
                    bSpeech.setText("Start Speech");
                    spR.stopListening();
                }
            }
        });

    }
    void openBT() throws IOException {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null) {
            myLabel.setText("No bluetooth adapter available");
        }

        if(!mBluetoothAdapter.isEnabled()) {
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, 0);
        }
        boolean found=false;
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if(pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                if (device.getName().equals(etBname.getText().toString())) {
                    mmDevice = device;
                    found=true;
                    break;
                }
            }
        }
        if (found) {
            UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); //Standard //SerialPortService ID
            mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid);
            try {
                mmSocket.connect();
                Log.e("", "Connected");
            } catch (IOException e) {
                Log.e("", e.getMessage());
                try {
                    Log.e("", "trying fallback...");

                    mmSocket = (BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class}).invoke(mmDevice, 1);
                    mmSocket.connect();

                    Log.e("", "Connected");
                } catch (Exception e2) {
                    Log.e("", "Couldn't establish Bluetooth connection!");
                }
            }
            try {
                mmOutputStream = mmSocket.getOutputStream();
                mmInputStream = mmSocket.getInputStream();
                myLabel.setText("Bluetooth Connected");
                myLabel.setTextColor(getResources().getColor(R.color.colorPrimary));
            }catch (Exception ex){
                Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
            }


        }
        else{
            myLabel.setText("Bluetooth Not found");
            myLabel.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }
    void sendData(char m) throws IOException {
        //mmOutputStream.write(msg.getBytes());

        mmOutputStream.write(m);
        myLabel.setText("Data Sent");
        String msg="";
        switch (m) {
            case 'L':msg="LEFT";
                break;
            case 'R':msg="RIGHT";
                break;
            case 'U':msg="FORWARD";
                break;
            case 'D':msg="BACKWARD";
                break;
            case 'i':msg="Rotate RIGHT";
                break;
            case 'e':msg="Rotate LEFT";
                break;
        }
        tvCmd.setText(msg);
    }
    void sendData2(char m){
        try{
            mmOutputStream.write(m);
            myLabel.setText("Data Sent");
            String msg="";
            switch (m) {
                case 'L':msg="LEFT";
                    break;
                case 'R':msg="RIGHT";
                    break;
                case 'U':msg="FORWARD";
                    break;
                case 'D':msg="BACKWARD";
                    break;
                case 'i':msg="Rotate RIGHT";
                    break;
                case 'e':msg="Rotate LEFT";
                    break;
            }
            tvCmd.setText(msg);
        }catch (Exception ex){
            Toast.makeText(getApplicationContext(),ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }


}
