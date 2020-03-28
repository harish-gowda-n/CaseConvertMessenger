package com.example.caseconvertmessenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    boolean bound = false;
    EditText ed;
    TextView tv;
    Button b;
    Messenger m;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ed = findViewById(R.id.editText);
        tv = findViewById(R.id.textView);
        b = findViewById(R.id.button);
        Intent intent = new Intent(this, ConvertService.class);
        bindService(intent, con, Context.BIND_AUTO_CREATE);
        b.setOnClickListener(this);
    }
    private ServiceConnection con = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            m = new Messenger(service);
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    @Override
    public void onClick(View v) {
            String inp = ed.getText().toString();
            Log.i("input", ""+inp);
            Message message = Message.obtain(null, ConvertService.request, 0, 0);
            message.replyTo = new Messenger(new ReceiveHand());
            Bundle b = new Bundle();
            b.putString("Data", inp);
            message.setData(b);
            try {
                 m.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

        }
        class ReceiveHand extends Handler{
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case ConvertService.response:
                        String output = msg.getData().getString("output");
                        Toast.makeText(getApplicationContext(), output, Toast.LENGTH_LONG).show();
                        tv.setText(output);
                        break;
                    default : super.handleMessage(msg);
                }

            }
        }

    }

