package com.example.jens.tnm082_lab1.dummy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.jens.tnm082_lab1.R;
import com.example.jens.tnm082_lab1.database.Datasource;

/**
 * Created by Jens on 2016-05-18.
 */
public class AddDialog extends DialogFragment{

    private Datasource mDS;

    /* The activity that creates an instance of this dialog fragment must
* implement this interface in order to receive event callbacks.
* Each method passes the DialogFragment in case the host needs to query it. */
    public interface AddDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog, String mName, String mDesc);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    AddDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (AddDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final View v_iew=inflater.inflate(R.layout.dialog_add, null) ;
        builder.setView(v_iew)
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        //Get user input
                        EditText uName = (EditText) v_iew.findViewById(R.id.dialog_name);
                        EditText uDesc = (EditText) v_iew.findViewById(R.id.dialog_desc);
                        String mName = uName.getText().toString();
                        String mDesc = uDesc.getText().toString();
                        // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(AddDialog.this, mName,mDesc);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the negative button event back to the host activity
                        mListener.onDialogNegativeClick(AddDialog.this);
                    }
                });
        return builder.create();
    }
}


