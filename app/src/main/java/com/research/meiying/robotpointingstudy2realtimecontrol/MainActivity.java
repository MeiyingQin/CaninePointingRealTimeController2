package com.research.meiying.robotpointingstudy2realtimecontrol;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private String ip = "";
    private int port = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void connect(View view) {
        ip = ((EditText) findViewById(R.id.serverIPTextview)).getText().toString();
        port = Integer.parseInt(((EditText) findViewById(R.id.serverPortTextview)).getText().toString());

        if (ip.isEmpty() || port == -1) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                RobotCommand robotCommand = new RobotCommand(ip, port);
                boolean is_connected = robotCommand.sendInfoViaSocket(getString(R.string.initial_connection_request)).equals(getString(R.string.connection_respond));
                if (is_connected) {
                    Intent intent = new Intent(getApplicationContext(), HeadController.class);
                    startActivity(intent);
                }
            }
        }).start();
    }
}

