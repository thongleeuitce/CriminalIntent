package com.edu.thongleeuos.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by thongleeuteam on 11/11/2016.
 */

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrime_Recycler_View;
    private CrimeAdapter mCrimeAdater;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrime_Recycler_View = (RecyclerView) view.findViewById(R.id.recyclerview_crime);
        mCrime_Recycler_View.setLayoutManager(new LinearLayoutManager(getActivity()));
        UpdateListCrime();
        return view;
    }
    private class CrimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTextView_Title_Crime;
        private TextView mTextView_Date_crime;
        private CheckBox mCheckBox_Solved;
        private Crime mCrime;

        public CrimeViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTextView_Title_Crime = (TextView) itemView.findViewById(R.id.Txtv_Title_Crime);
            mCheckBox_Solved = (CheckBox) itemView.findViewById(R.id.checkbox_solved);
            mTextView_Date_crime = (TextView) itemView.findViewById(R.id.txtv_Date_Crime);
        }
        public void getCrimeHolder(Crime crime){
            mCrime = crime;
        }
        @Override
        public void onClick(View view) {
            Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
            startActivityForResult(intent,0);
            Toast.makeText(getActivity(),mCrime.getTitle()+" Clickded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != getActivity().RESULT_OK)
            return;
        if(requestCode == 0){

        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeViewHolder>{

        public List<Crime> mCrimes;
        public  CrimeAdapter(List<Crime> crimes){
            mCrimes = crimes;
        }
        @Override
        public CrimeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view =  layoutInflater.inflate(R.layout.list_item_crime, parent, false);
            return new CrimeViewHolder(view);
        }
        @Override
        public void onBindViewHolder(CrimeViewHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.mTextView_Title_Crime.setText(crime.getTitle());
            holder.mTextView_Date_crime.setText(crime.getDate().toString());
            holder.mCheckBox_Solved.setChecked(crime.getSolved());
            holder.getCrimeHolder(crime);
        }
        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_menu_new_menu:
                Crime crime = new Crime();
                Intent intent = CrimeActivity.newintent(getActivity(), crime.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void UpdateListCrime(){
        List<Crime> crimeList = CrimeLab.get(getActivity()).getCrimeList();
        mCrimeAdater = new CrimeAdapter(crimeList);
        mCrime_Recycler_View.setAdapter(mCrimeAdater);
    }

    @Override
    public void onResume() {
        super.onResume();
        UpdateListCrime();
    }
}

