package com.example.coursework;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;


public class ChosenDayDialog extends DialogFragment {

    private final static int REQUEST_CODE= 1;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        assert getArguments() != null;
        String getArgument = getArguments().getString("CURRENT_DAY_DATA");
        //assert getArguments() != null;
        //String getArgument = getArguments().getString("CURRENT_DAY_DATA");
        builder.setTitle("Adding an event to favourites")
                //.setMessage("Event: " + getArgument)
                .setPositiveButton("To events list", (dialog, id) -> {
                    Intent intent = new Intent(getContext(), TodayHolidaysActivity.class);
                    intent.putExtra("clickedDate", getArgument);
                    mListener.onComplete(intent);
                });
        return builder.create();
    }

    public interface OnCompleteListener {
        void onComplete(Intent intent);
    }

    private OnCompleteListener mListener;

    // make sure the Activity implemented it
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

}
