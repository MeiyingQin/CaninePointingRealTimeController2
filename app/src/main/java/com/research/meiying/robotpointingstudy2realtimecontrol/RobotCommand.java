package com.research.meiying.robotpointingstudy2realtimecontrol;

import android.content.Context;
import android.icu.util.Calendar;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RobotCommand {
    private static Socket socket = null;

    RobotCommand() {

    }

    RobotCommand(String ip, int port) {
        if (socket == null) {
            try {
                InetAddress serverAddress = InetAddress.getByName(ip);
                socket = new Socket(serverAddress, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean sendInfoViaSocket(Context context, String message) {
        Log.d("[sendInfoSocketSTART]", Calendar.getInstance().getTime().toString());

        final String requestMessage = message + "\n";
        final Context currentContext = context;

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Callable<Boolean> callableCommand = new Callable<Boolean>() {
            @Override
            public Boolean call() {
                boolean returnValue = false;
                try{
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                    out.println(requestMessage);
                    out.flush();

                    String serverMessage = in.readLine();
                    Log.d("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");

                    returnValue = serverMessage.equals(currentContext.getString(R.string.connection_respond));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return returnValue;
            }
        };
        Future<Boolean> result = executorService.submit(callableCommand);
        boolean returnValue = false;
        try {
            returnValue = result.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        Log.d("[sendInfoSocketEND]", Calendar.getInstance().getTime().toString());

        return returnValue;
    }

}
