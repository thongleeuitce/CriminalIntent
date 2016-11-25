package com.edu.thongleeuos.criminalintent;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

/**
 * Created by thongleeuteam on 22/11/2016.
 */

public class CrimeCursorWrapper extends CursorWrapper {
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }
    public Crime getCrime(){
        String uuidstring = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Colunms.UUID));
        String title = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Colunms.TITLE));
        long date = getLong(getColumnIndex(CrimeDbSchema.CrimeTable.Colunms.DATE));
        int solved = getInt(getColumnIndex(CrimeDbSchema.CrimeTable.Colunms.SOLVED));
        String suspect = getString(getColumnIndex(CrimeDbSchema.CrimeTable.Colunms.SUSPECT));

        Crime crime = new Crime (UUID.fromString(uuidstring));
        crime.setTitle(title);
        crime.setDate(new Date(date));
        crime.setSolved(solved == 1);
        crime.setSuspect(suspect);
        return crime;

    }
}
