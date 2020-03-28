package com.example.caseconvertmessenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ConvertService extends Service {
    final static int request = 1;
    final static int response = 2;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return obj.getBinder();
    }

    Messenger obj = new Messenger(new Inner());

    public class Inner extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case request:
                    String s = msg.getData().getString("Data");
                    String out = s.toUpperCase();
                    Message ob = Message.obtain(null, response);
                    Bundle b = new Bundle();
                    b.putString("output",out);
                    ob.setData(b);
                    Log.i("output : ", out);
                    try {
                        msg.replyTo.send(ob);
                    } catch (Exception e) {
                        Log.i("Error", e.getMessage());
                    }
                default:
                    super.handleMessage(msg);
            }
        }
    }
}
