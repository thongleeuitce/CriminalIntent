package com.edu.thongleeuos.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by thongleeuteam on 01/11/2016.
 */

public class CrimeFragment extends Fragment {
    private static final String ARG_CRIME_ID = "arg_crime_id";
    private static final String DATE_DIALOG = "date_dialog";
    private static final String EXTRA_DATE = "extra_date";
    private Crime mCrime;
    private EditText medtxt_title_crime;
    private Button mbtn_Date;
    private Button mbtn_saved;
    private CheckBox mCheckBox_Solved;
    private boolean Check_Crime;
    private List<Crime> mCrimes;
    private CrimeLab mCrimeLab;
    private Button mbtn_delete;
    private Button mbtn_sendreport;

    public static CrimeFragment newIntance(UUID uuid) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID, uuid);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCrimeLab = CrimeLab.get(getActivity());
        mCrime = new Crime();
        mCrime.setId((UUID) getArguments().getSerializable(ARG_CRIME_ID));
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        medtxt_title_crime = (EditText) v.findViewById(R.id.etxt_tittle_crime);
        mbtn_Date = (Button) v.findViewById(R.id.btn_Date);
        mCheckBox_Solved = (CheckBox) v.findViewById(R.id.chbx_Solve);
        mbtn_saved =(Button) v.findViewById(R.id.btn_saved);
        mbtn_delete = (Button) v.findViewById(R.id.btn_delete);
        mbtn_sendreport = (Button) v.findViewById(R.id.btn_report);

        mbtn_sendreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getReportCrime());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_suspect));
                startActivity(intent);
            }
        });

        mbtn_Date.setText(mCrime.getDate().toString());
        mbtn_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getDate());
                datePickerFragment.setTargetFragment(CrimeFragment.this, 0);
                datePickerFragment.show(fragmentManager, DATE_DIALOG);
            }
        });
        if(mCrimeLab.getCrime(mCrime.getId()) != null) {
            mCrime = CrimeLab.get(getActivity()).getCrime(mCrime.getId());
            mCheckBox_Solved.setChecked(mCrime.getSolved());
            medtxt_title_crime.setText(mCrime.getTitle());
        }
        else
            mCrime.setSolved(Boolean.FALSE);
        mCheckBox_Solved.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mCrime.setSolved(b);
                }
        });
        mbtn_saved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCrime.setTitle(medtxt_title_crime.getText().toString());
                if(mCrimeLab.getCrime(mCrime.getId()) == null) {
                    mCrimeLab.addCrime(mCrime);
                    mbtn_saved.setEnabled(false);
                }
                else
                    mCrimeLab.updateCrime(mCrime);
                Toast.makeText(getActivity(), R.string.toast_saved, Toast.LENGTH_SHORT).show();
            }
        });
        mbtn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mCrimeLab.deleteCrime(mCrime) != 0){
                    Toast.makeText(getActivity(), R.string.toast_deleted, Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(getActivity(), R.string.toast_cant_delete, Toast.LENGTH_SHORT).show();
            }
        });
        return v;
    }

    public String getReportCrime(){
        String stringSolved = null;
        String stringSuspect = mCrime.getSuspect();
        String stringDate = android.text.format.DateFormat.format("EEEE, MM dd", mCrime.getDate()).toString();

        if(mCrime.getSolved())
            stringSolved = getString(R.string.crime_report_solved);
        else
        if(mCrime.getSuspect() != null)
            stringSuspect = getString(R.string.crime_report_suspect, mCrime.getSuspect());
        else
            stringSuspect = getString(R.string.crime_report_no_suspect);
        String report = getString(R.string.report_crime, mCrime.getTitle(), stringDate, stringSolved, stringSuspect);
            return report;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        {
            if (resultCode != Activity.RESULT_OK)
                return;
            if (requestCode == 0) {
                Date date = (Date) data.getSerializableExtra(DatePickerFragment.TARG_DATE);
                mCrime.setDate(date);
                mbtn_Date.setText(mCrime.getDate().toString());
            }
            return;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}