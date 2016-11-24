package com.edu.thongleeuos.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by thongleeuteam on 15/11/2016.
 */

public class CrimePagerActivity extends AppCompatActivity {
    private List<Crime> mCrimeList;
    private ViewPager mViewPager_Crime;
    private static final String EXTRA_CRIME_ID = "extra_crime_id";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_crime);
        mViewPager_Crime = (ViewPager) findViewById(R.id.viewpage_crime);
        FragmentManager fragmentManager = getSupportFragmentManager();
        mCrimeList = CrimeLab.get(this).getCrimeList();
        mViewPager_Crime.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                UUID uuid = mCrimeList.get(position).getId();
                return CrimeFragment.newIntance(uuid);
            }
            @Override
            public int getCount() {
                return mCrimeList.size();
            }
        });
        UUID uuid = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        for(int i = 0; i < mCrimeList.size(); i++){
            if(mCrimeList.get(i).getId().equals(uuid)) {
                mViewPager_Crime.setCurrentItem(i);
                break;
            }
        }
    }
    public static Intent newIntent(Context context, UUID uuid){
        Intent intent = new Intent(context, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, uuid);
        return intent;
    }
}
