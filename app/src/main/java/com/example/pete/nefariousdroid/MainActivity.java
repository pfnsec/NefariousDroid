package com.example.pete.nefariousdroid;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;



public class MainActivity extends AppCompatActivity {

    private class sendCommandTask extends AsyncTask<String, Integer, String> {
        protected String doInBackground(String... cmdlist) {

            String cmd = cmdlist[0];

            try {
                InetAddress addr = InetAddress.getByName(getString(R.string.tv_url));
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
                byte[] response = new byte[0];
                byte[] readBuf = new byte[256];

                do {
                    bytesRead = is.read(readBuf, bytesCount, 256);
                    bytesCount += bytesRead;
                } while(bytesRead != 0);

                os.close();
                is.close();
                return new String(readBuf, "UTF-8");

            } catch (SocketTimeoutException e) {
                e.printStackTrace();
                return "Timed Out!";
            } catch (IOException i) {
                i.printStackTrace();
                return i.getLocalizedMessage();
            } catch (Exception er) {
                er.printStackTrace();
                return er.getLocalizedMessage();
            }
        }

        protected void onPostExecute(String result) {
            popToast(result);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void popToast(String s) {
        if(s != null)
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    public void voteSkip(View v) {
        new sendCommandTask().execute("{\"command\":\"skip\"}");
    }
}
