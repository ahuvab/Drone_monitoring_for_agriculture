package com.example.user.airscort_agriculture.DB;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static  final int DATABASE_VERSION= 10;
    public static final String DATABASE_NAME="fields.db";

    public DBHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + FieldContracts.FieldsEntry.FIELDS_TABLE + " (" + FieldContracts.FieldsEntry.NAME + " TEXT, "
                        + FieldContracts.FieldsEntry.FIELD_ID + " INTEGER, "
                        + FieldContracts.FieldsEntry.PATH_FRAME_LAT + " TEXT, "
                        + FieldContracts.FieldsEntry.PATH_FRAME_LON + " TEXT, "
                        + FieldContracts.FieldsEntry.DRONE_PATH_LAT  + " TEXT, "
                        + FieldContracts.FieldsEntry.DRONE_PATH_LON  + " TEXT, "
                        + FieldContracts.FieldsEntry.DISTANCE  + " REAL, "
                        + FieldContracts.FieldsEntry._ID  + " INTEGER PRIMARY KEY AUTOINCREMENT"+");"
        );
        db.execSQL("CREATE TABLE " + FieldContracts.FieldsEntry.HISTORY_TABLE + " ("
                        + FieldContracts.FieldsEntry.DATE + " TEXT, "
                        + FieldContracts.FieldsEntry.MISSION_ID + " INTEGER, "
                        + FieldContracts.FieldsEntry.FIELDS_LIST + " TEXT, "
                        + FieldContracts.FieldsEntry._ID  + " INTEGER PRIMARY KEY AUTOINCREMENT"+");"
        );
        db.execSQL("CREATE TABLE " + FieldContracts.FieldsEntry.SCANNING_TABLE + " ("
                        + FieldContracts.FieldsEntry.SCAN_DATE + " TEXT, "
                        + FieldContracts.FieldsEntry.FIELD + " TEXT, "
                        + FieldContracts.FieldsEntry.RESOLUTION + " TEXT, "
                        + FieldContracts.FieldsEntry.HIGH + " INTEGER, "
                        + FieldContracts.FieldsEntry._ID  + " INTEGER PRIMARY KEY AUTOINCREMENT"+");"
        );

        db.execSQL("CREATE TABLE " + FieldContracts.FieldsEntry.USER_TABLE + " ("
                + FieldContracts.FieldsEntry.FIRST_NAME + " TEXT, "
                + FieldContracts.FieldsEntry.LAST_NAME + " TEXT, "
                + FieldContracts.FieldsEntry.EMAIL + " TEXT, "
                + FieldContracts.FieldsEntry.PASSWORD + " TEXT, "
                + FieldContracts.FieldsEntry.USER_ID + " INTEGER, "
                + FieldContracts.FieldsEntry.HOME_LAT  + " TEXT, "
                + FieldContracts.FieldsEntry.HOME_LON  + " TEXT, "
                + FieldContracts.FieldsEntry._ID  + " INTEGER PRIMARY KEY AUTOINCREMENT"+");"
        );

        db.execSQL("CREATE TABLE " + FieldContracts.FieldsEntry.TIME_TABLE + " ("
                        + FieldContracts.FieldsEntry.SCANNING_TIME + " TEXT, "
                        + FieldContracts.FieldsEntry._ID  + " INTEGER PRIMARY KEY AUTOINCREMENT"+");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + FieldContracts.FieldsEntry.FIELDS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FieldContracts.FieldsEntry.HISTORY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FieldContracts.FieldsEntry.SCANNING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FieldContracts.FieldsEntry.USER_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FieldContracts.FieldsEntry.TIME_TABLE);
        onCreate(db);
    }
}
