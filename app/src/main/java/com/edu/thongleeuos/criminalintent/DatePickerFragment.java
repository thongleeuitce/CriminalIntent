package com.edu.thongleeuos.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by thongleeuteam on 19/11/2016.
 */

public class DatePickerFragment extends android.support.v4.app.DialogFragment {
    private static final String DATE = "date";
    public static final String TARG_DATE = "target_date";
    private DatePicker mdatepicker;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.date_picker_view, null);
        Date date = (Date) getArguments().getSerializable(DATE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(calendar.YEAR);
        int month = calendar.get(calendar.MONTH);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        mdatepicker = (DatePicker) view.findViewById(R.id.dialog_date_picker);
        mdatepicker.init(year, month, day, null);
        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setView(view).setTitle(R.string.Title_DatePicker).setPositiveButton(R.string.button_oke, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int year = mdatepicker.getYear();
                int month = mdatepicker.getMonth();
                int day = mdatepicker.getDayOfMonth();
                Date date = new GregorianCalendar(year, month, day).getTime();
                sendResult(Activity.RESULT_OK, date);
            }
        }).create();
        return dialog;
    }
    public void sendResult(int resultcode, Date date){
        if(getTargetFragment() == null)
            return;
        Intent intent = new Intent();
        intent.putExtra(TARG_DATE, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultcode , intent);
    }
    public static DatePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(DATE, date);
        DatePickerFragment datepickerfragment = new DatePickerFragment();
        datepickerfragment.setArguments(args);
        return datepickerfragment;
    }
}
