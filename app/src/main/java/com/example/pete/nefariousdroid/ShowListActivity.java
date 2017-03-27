package com.example.pete.nefariousdroid;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by pete on 22/03/2017.
 */

public class ShowListActivity extends ListActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_showlist, R.id.showText, Arrays.asList(i.getStringArrayExtra("SHOW_LIST"))));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        String show = (String) getListAdapter().getItem(position);
        Log.d("List Item", show);
        CommandOp cmd = new CommandOp("{\"command\":\"nextShow\", \"tvShow\":\"" + show + "\"}");
        SendCommandTask cmdTask = new SendCommandTask(getString(R.string.tv_url));
        cmdTask.execute(cmd);
        try {
            popToast(cmdTask.get());
        } catch(Exception e) {
            popToast(e.getMessage());
        }
    }

    private void popToast(String s) {
        if(s != null)
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }
}
