package com.edu.thongleeuos.criminalintent;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Path;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
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
    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_SUSPECT = 1;
    private static final int REQUEST_PHOTO = 2;

    private Crime mCrime;
    private EditText medtxt_title_crime;
    private Button mbtn_Date;
    private Button mbtn_saved;
    private CheckBox mCheckBox_Solved;
    private CrimeLab mCrimeLab;
    private Button mbtn_delete;
    private Button mbtn_sendreport;
    private Button mbtn_choose_suspect;
    private ImageButton mbtn_camera;
    private ImageView mimgv_photo;
    private File mPhotoFile;

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
        mPhotoFile = mCrimeLab.getPhotoFile(mCrime);
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
        mbtn_choose_suspect = (Button) v.findViewById(R.id.btn_suspect);
        mbtn_camera = (ImageButton) v.findViewById(R.id.btn_camera);
        mimgv_photo = (ImageView) v.findViewById(R.id.imgv_photo);
        final PackageManager packagemanager = getActivity().getPackageManager();
        UpdatePhotoView();

        mbtn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                if(mPhotoFile != null && intent.resolveActivity(packagemanager) != null){
                    Uri uri = Uri.fromFile(mPhotoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult(intent, REQUEST_PHOTO);
                }
            }
        });
        if(mCrimeLab.getCrime(mCrime.getId()) != null){
            mCrime = mCrimeLab.getCrime(mCrime.getId());
            if(mCrime.getSuspect() != null)
                mbtn_choose_suspect.setText(mCrimeLab.getCrime(mCrime.getId()).getSuspect());
        }

        mbtn_choose_suspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent,REQUEST_SUSPECT);
            }
        });

        mbtn_sendreport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, getReportCrime());
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_suspect, mCrime.getSuspect()));
                intent = Intent.createChooser(intent, getString(R.string.send_report));
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

    public void UpdatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists())
            mimgv_photo.setImageDrawable(null);
        else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mimgv_photo.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        {
            if (resultCode != Activity.RESULT_OK)
                return;
            if (requestCode == REQUEST_DATE) {
                Date date = (Date) data.getSerializableExtra(DatePickerFragment.TARG_DATE);
                mCrime.setDate(date);
                mbtn_Date.setText(mCrime.getDate().toString());
            }
            if (requestCode == REQUEST_SUSPECT && data != null){
                Uri uri = data.getData();
                String[] queryfiled = new String[]{
                        ContactsContract.Contacts.DISPLAY_NAME
                };
                Cursor cursor = getActivity().getContentResolver().query(uri, queryfiled, null, null, null);
                try{
                    if(cursor.getCount() == 0)
                        return;
                    cursor.moveToFirst();
                    mCrime.setSuspect(cursor.getString(0));
                    mbtn_choose_suspect.setText(cursor.getString(0));
                }
                finally {
                    cursor.close();
                }
            }
            if (requestCode == REQUEST_PHOTO)
                UpdatePhotoView();
            return;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}