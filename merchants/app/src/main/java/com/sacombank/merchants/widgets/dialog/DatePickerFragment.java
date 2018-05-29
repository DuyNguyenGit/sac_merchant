package com.sacombank.merchants.widgets.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static final String TAG = DatePickerFragment.class.getSimpleName();
    DatePickerListener datePickerListener;

    public static DatePickerFragment newInstance(DatePickerListener listener){

        DatePickerFragment dialogFragment = new DatePickerFragment();
        dialogFragment.datePickerListener = listener;
        return dialogFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        month++;
        String formatMonth = (String.valueOf(month).length() == 1) ? "0".concat(String.valueOf(month)) : String.valueOf(month);
        String formatDate = (String.valueOf(day).length() == 1) ? "0".concat(String.valueOf(day)) : String.valueOf(day);
        String newformatDate = String.valueOf(year).concat(String.valueOf(formatMonth)).concat(String.valueOf(formatDate));
        String date = String.valueOf(formatDate).concat("/").concat(String.valueOf(formatMonth)).concat("/").concat(String.valueOf(year));
        Log.e(TAG, "onDateSet: " + formatDate);
        Log.e(TAG, "onDateSet: " + date);
        datePickerListener.setDate(date, newformatDate);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        datePickerListener.setOnCancel("");
    }

    public interface DatePickerListener {
        void setDate(String date, String formatDate);
        void setOnCancel(String temp);
    }

}