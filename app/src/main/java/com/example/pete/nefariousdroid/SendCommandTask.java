package com.example.pete.nefariousdroid;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by pete on 25/03/2017.
 */

public class SendCommandTask extends AsyncTask<CommandOp, Integer, String> {

    private String tvUrl;

    public SendCommandTask(String tvUrl) {
        this.tvUrl = tvUrl;
    }

    protected String doInBackground(CommandOp... cmdlist) {

        String cmd = cmdlist[0].command;

        try {
            InetAddress addr = InetAddress.getByName(tvUrl);
            InetSocketAddress tvAddr = new InetSocketAddress(addr, 5005);

            Socket s = new Socket();
            s.setTcpNoDelay(true);

            s.connect(tvAddr, 5000);
            OutputStream os = s.getOutputStream();
            InputStream is = s.getInputStream();
            os.write(cmd.getBytes("UTF-8"));
            os.flush();

            int bytesRead = 0;
            int bytesCount = 0;
            byte[] readBuf = new byte[256];
            ByteArrayOutputStream bufStrm = new ByteArrayOutputStream();

            do {
                bytesRead = is.read(readBuf, 0, 256);
                Log.d("Response", new String(readBuf) + " " + bytesRead);

                if(bytesRead > 0)
                    bufStrm.write(readBuf, 0, bytesRead);

                bytesCount += bytesRead;
            } while(bytesRead == 256);

            Log.d("Response", bufStrm.toString() + " " + bytesCount);


            os.close();
            is.close();

            cmdlist[0].setResponse(bufStrm.toString());
            return bufStrm.toString();

        } catch (SocketTimeoutException e) {
            e.printStackTrace();
            cmdlist[0].setResponse("Timed Out!");
        } catch (IOException i) {
            i.printStackTrace();
            cmdlist[0].setResponse(i.getMessage());
        } catch (Exception er) {
            er.printStackTrace();
            cmdlist[0].setResponse(er.getMessage());
        }

        return cmdlist[0].getResponse();

    }

    protected void onPostExecute(String result) {

    }
}
