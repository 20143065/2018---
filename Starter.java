package com.example.pi_test1.myapplication4;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Starter extends BroadcastReceiver {
@Override
public void onReceive(Context context, Intent intent) {

    try {
        Thread.sleep(5000);
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    // TODO Auto-generated method stub
    String action = intent.getAction();
    if(action.equals("android.intent.action.BOOT_COMPLETED")) {
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}
}
