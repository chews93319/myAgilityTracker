/*
*
* References
*   1. https://guides.codepath.com/android/Local-Databases-with-SQLiteOpenHelper#overview
*
*
*
* */



package oregonstate.chews.myagilitytracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class AgilityDatabaseHelper extends SQLiteOpenHelper {
    private static AgilityDatabaseHelper sInstance;

    public static synchronized AgilityDatabaseHelper getInstance(Context context) {
        //  Use the application context, which will ensure that you
        //  don't accidentally leak an Activity's context.
        if (sInstance == null) {
            sInstance = new AgilityDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private AgilityDatabaseHelper(Context context) {
        super(context,
                EventsTblContract.EventsTable.DB_NAME,
                null,
                EventsTblContract.EventsTable.DB_VERSION);
    }

    //  Called when the database connection is being configured
    //  Configure database settings for things like foreign key support
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
    }

    //  Called when the database is created for the FIRST time
    //  If a database already exists on disk with same DB_NAME, this will be skipped
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EventsTblContract.EventsTable.SQL_CREATE_EVENTSTABLE);

        /*
        //  TestValues for debugging Table Creation
        db.execSQL(EventsTblContract.EventsTable.SQL_TEST_EVENT_INSERT);

        ContentValues testValues = new ContentValues();
        testValues.put(EventsTblContract.EventsTable.COL_EVENTNAME, "onCreate Name");
        testValues.put(EventsTblContract.EventsTable.COL_LOCLAT, "33.3");
        testValues.put(EventsTblContract.EventsTable.COL_LOCLON, "144.4");
        db.insert(EventsTblContract.EventsTable.TABLE_NAME,null, testValues);
        */
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(EventsTblContract.EventsTable.SQL_DROP_EVENTSTABLE);
        onCreate(db);
    }
}

final class EventsTblContract {
    private EventsTblContract(){};

    public final class EventsTable implements BaseColumns {

        // Database Info
        public static final String DB_NAME = "agility_db";
        public static final int DB_VERSION = 1;

        // Table Names
        public static final String TABLE_NAME = "events_tbl";

        // Events Table Columns
        public static final String COL_EVENTNAME = "eventname";
        public static final String COL_LOCLAT = "loc_lat";
        public static final String COL_LOCLON = "loc_lon";


        public static final String SQL_CREATE_EVENTSTABLE = "CREATE TABLE " +
                EventsTable.TABLE_NAME + " (" +
                EventsTable._ID + " INTEGER PRIMARY KEY NOT NULL" + ", " +
                EventsTable.COL_EVENTNAME + " VARCHAR(255)" + ", " +
                EventsTable.COL_LOCLAT + " VARCHAR(255)" + ", " +
                EventsTable.COL_LOCLON + " VARCHAR(255)" + ");";

        public static final String SQL_DROP_EVENTSTABLE = "DROP TABLE IF EXISTS " +
                EventsTable.TABLE_NAME;

        public static final String SQL_TEST_EVENT_INSERT = "INSERT INTO " +
                EventsTable.TABLE_NAME + " (" +
                EventsTable.COL_EVENTNAME + ", " +
                EventsTable.COL_LOCLAT + ", " +
                EventsTable.COL_LOCLON + ") " +
                "VALUES ('DBContract name','22.2','111.1');";

    }
}