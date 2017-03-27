package com.example.pete.nefariousdroid;

import android.util.Log;

/**
 * Created by pete on 25/03/2017.
 */

class CommandOp {
    String command;
    String response;

    CommandOp(String command) {
        Log.d("CommandOp", command);
        this.command = command;
        this.response = "";
    }

    void setResponse(String s) {
        this.response = s;

    }

    String getResponse() {
        return this.response;
    }
}
