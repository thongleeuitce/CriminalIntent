package com.edu.thongleeuos.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.edu.thongleeuos.criminalintent.CrimeDbSchema.CrimeTable;

/**
 * Created by thongleeuteam on 11/11/2016.
 */

public class CrimeLab{
    //private List<Crime> mCrimeList;
    private static CrimeLab sCrimeLab;
    private Context mContext;
    private SQLiteDatabase mSQLiteDatabase;

    public CrimeLab(Context context){
        mContext = context.getApplicationContext();
        mSQLiteDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
      //  mCrimeList = new ArrayList<Crime>();
    }
    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }
    public List<Crime> getCrimeList(){
        //return mCrimeList;
        CrimeCursorWrapper cursor = queryCrime(null, null);
        List<Crime> crimeList = new ArrayList<Crime>();
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                crimeList.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }
        finally {
            cursor.close();
        }
        return crimeList;
    }
    public Crime getCrime(UUID uuid){
        /*for(Crime crime : mCrimeList){
            if(crime.getId().equals(uuid))
                return crime;
        }*/
        CrimeCursorWrapper cursor = queryCrime(CrimeTable.Colunms.UUID + "=?", new String[]{uuid.toString()});

        if (cursor.getCount() == 0)
            return null;
        try{
            cursor.moveToFirst();
            return cursor.getCrime();
        }
        finally {
            cursor.close();
        }
    }
    private CrimeCursorWrapper queryCrime(String whereclause, String[] wherearg){
        Cursor cursor = mSQLiteDatabase.query(CrimeTable.NAME, null, whereclause, wherearg, null, null, null);
        return new CrimeCursorWrapper(cursor);
    }
    public ContentValues getContentValues(Crime crime){
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeTable.Colunms.UUID, crime.getId().toString());
        contentValues.put(CrimeTable.Colunms.TITLE, crime.getTitle());
        contentValues.put(CrimeTable.Colunms.DATE, crime.getDate().getTime());
        contentValues.put(CrimeTable.Colunms.SOLVED, crime.getSolved());

        return contentValues;
    }
    public void updateCrime(Crime crime){
        ContentValues values = getContentValues(crime);
        mSQLiteDatabase.update(CrimeTable.NAME, values, CrimeTable.Colunms.UUID + "=?", new String[]{crime.getId().toString()});
    }
    public void addCrime(Crime crime){
        ContentValues values = getContentValues(crime);
        mSQLiteDatabase.insert(CrimeTable.NAME, null, values);
    }
    public int deleteCrime(Crime crime){
        if (getCrime(crime.getId()) == null)
            return 0;
        mSQLiteDatabase.delete(CrimeTable.NAME, CrimeTable.Colunms.UUID + "=? ", new String[]{crime.getId().toString()});
        return  1;
    }
}
