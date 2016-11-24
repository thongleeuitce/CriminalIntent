package com.edu.thongleeuos.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by thongleeuteam on 11/11/2016.
 */

public class CrimetListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return new CrimeListFragment();
    }
}
