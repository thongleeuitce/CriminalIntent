package com.edu.thongleeuos.criminalintent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.edu.thongleeuos.criminalintent.CrimeDbSchema.CrimeTable;

/**
 * Created by thongleeuteam on 22/11/2016.
 */

public class CrimeBaseHelper extends SQLiteOpenHelper {
    private static final String NAME = "CrimeBase.db";
    private static final int VERSION = 1;

    public CrimeBaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + CrimeTable.NAME + "(" + "_id integer primary key autoincrement," + CrimeTable.Colunms.UUID + "," + CrimeTable.Colunms.TITLE + "," + CrimeTable.Colunms.DATE + "," + CrimeTable.Colunms.SOLVED + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }
}
