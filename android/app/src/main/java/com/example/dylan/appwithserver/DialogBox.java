package com.example.dylan.appwithserver;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

/**
 * Created by Dylan on 12/14/2017.
 */

public class DialogBox extends DialogFragment {
    protected String message;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessage(int messageId) {
        this.message = getString(messageId);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(this.message)
                .setNeutralButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
