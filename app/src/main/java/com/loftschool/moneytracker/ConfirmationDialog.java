package com.loftschool.moneytracker;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class ConfirmationDialog extends DialogFragment{

    private boolean dialogButtonStatus = false;

    public interface ConfirmationDialogListener {

        void onDialogOk(DialogFragment dialogFragment);

        void onDialogCancel(DialogFragment dialogFragment);
    }

    ConfirmationDialogListener confirmationDialogListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        confirmationDialogListener = (ConfirmationDialogListener) getTargetFragment();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setCancelable(false)
                .setTitle(R.string.app_name)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmationDialogListener.onDialogOk(ConfirmationDialog.this);
                        dialogButtonStatus = true;
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        confirmationDialogListener.onDialogCancel(ConfirmationDialog.this);
                        dialogButtonStatus = true;
                    }
                });

        return builder.create();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (confirmationDialogListener != null) {
            if (!dialogButtonStatus) {
                confirmationDialogListener.onDialogCancel(ConfirmationDialog.this);
            }
        }
    }
}
