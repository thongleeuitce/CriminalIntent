package com.edu.thongleeuos.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by thongleeuteam on 30/10/2016.
 */
public class CrimeActivity extends SingleFragmentActivity {
    private static final String EXTRA_CRIME_ID = "com.edu.thongleeuoss.criminalItent.extra_crime_id";
    @Override
    protected Fragment createFragment(){
        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        return CrimeFragment.newIntance(uuid);
    }
    public static Intent newintent(Context context, UUID uuid){
        Intent intent = new Intent(context, CrimeActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, uuid);
        return intent;
    }

}
