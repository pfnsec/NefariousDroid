package com.example.pete.nefariousdroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private void popToast(String s) {
        if(s != null)
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    private String sendCommand(CommandOp cmd) {
        SendCommandTask cmdTsk = new SendCommandTask(getString(R.string.tv_url));
        cmdTsk.execute(cmd);
        try {
            return cmdTsk.get();
        } catch(Exception e) {
            return e.getMessage();
        }
    }

    public void voteSkip(View v) {
        CommandOp cmd = new CommandOp("{\"command\":\"skip\"}");
        popToast(sendCommand(cmd));
    }

    public void currentEpisode(View v) {
        CommandOp cmd = new CommandOp("{\"command\":\"currentEpisode\"}");
        popToast(sendCommand(cmd));
    }

    public void listShows(View v) {
        CommandOp cmd = new CommandOp("{\"command\":\"idList\"}");
        String[] showList = sendCommand(cmd).split(", ");

        //String showList = "dank, shit, right there, 100";
        Intent i = new Intent(this, ShowListActivity.class);
        Arrays.sort(showList);
        i.putExtra("SHOW_LIST", showList);
        startActivity(i);

    }
}
