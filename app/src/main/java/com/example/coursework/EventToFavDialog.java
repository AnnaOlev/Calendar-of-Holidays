package com.example.coursework;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;


public class EventToFavDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        assert getArguments() != null;
        String getArgument = getArguments().getString("EVENT_DATA");
        builder.setTitle("Adding an event to favourites")
                .setMessage("Event: " + getArgument)
                .setPositiveButton("Got it", (dialog, id) -> dialog.cancel());
        return builder.create();
    }

}
