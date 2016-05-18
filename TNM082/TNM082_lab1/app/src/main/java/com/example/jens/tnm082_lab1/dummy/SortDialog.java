package com.example.jens.tnm082_lab1.dummy;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;

import com.example.jens.tnm082_lab1.R;

/**
 * Created by Jens on 2016-05-18.
 */
public class SortDialog extends DialogFragment{

    private int mSelectedItems;

    /* The activity that creates an instance of this dialog fragment must
* implement this interface in order to receive event callbacks.
* Each method passes the DialogFragment in case the host needs to query it. */
    public interface SortDialogListener {
        public void onSortDialogPositiveClick(DialogFragment dialog, int mSelectedItems);
        public void onSortDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    SortDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (SortDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement SortDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        // Build the dialog and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_sort_title)
                .setMultiChoiceItems(R.array.sorting, null,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which,
                                                boolean isChecked) {
                                if (isChecked) {
                                    // If the user checked the item, add it to the selected items
                                    mSelectedItems = which;
                                }
                            }
                        })
                // Set the action buttons
                .setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // User clicked OK, so save the mSelectedItems results somewhere
                        // or return them to the component that opened the dialog
                        mListener.onSortDialogPositiveClick(SortDialog.this, mSelectedItems);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onSortDialogNegativeClick(SortDialog.this);
                    }
                });
        return builder.create();
    }
}


