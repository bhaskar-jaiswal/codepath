package com.example.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by bhaskarjaiswal on 6/18/16.
 */
public class DatePickerFragment extends DialogFragment  {

    private DatePickerDialog.OnDateSetListener onDateSetListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof DatePickerDialog.OnDateSetListener) {
            onDateSetListener = (DatePickerDialog.OnDateSetListener) activity;
        }
    }

    public Dialog onCreateDialog(Bundle savedInstanceState){

        String date = getArguments().get("date").toString();
        int month = Integer.parseInt(date.substring(0,date.indexOf("-")))-1;
        int day = Integer.parseInt(date.substring(date.indexOf("-")+1,date.lastIndexOf("-")));
        int year = Integer.parseInt(date.substring(date.lastIndexOf("-")+1));

        return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) onDateSetListener, year, month, day);
    }

}
