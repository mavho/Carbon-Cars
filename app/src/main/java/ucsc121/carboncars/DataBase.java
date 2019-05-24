package ucsc121.carboncars;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DataBase extends SQLiteOpenHelper {
    //pointer to sqldb, instantiate with getwritebaledatabase
    SQLiteDatabase sqldb;
    Context ctx;

    private static String DB_NAME = "CARBON_DB";
    //table for car information
    private static String CAR_TABLE_NAME = "CARS";
    //table for trip information
    private static String TRIP_TABLE_NAME = "TRIPS";
    private int VERSION = 1;

    //table columns for CARS
    private static String CAR_MODEL = "MODEL";
    private static String CAR_YEAR = "YEAR";
    private static String MPG = "MPG";

    //table columns for TRIPS
    private static String TRIP = "TRIP";
    private static String DATE = "DATE";
    private static String DISTANCE = "DISTANCE";
    private static String CO2_FP = "C02";

    //init
    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        ctx = context;
        VERSION = version;
        DB_NAME = name;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //car db
        sqLiteDatabase.execSQL("CREATE TABLE " + CAR_TABLE_NAME +
            " (" + CAR_MODEL + " TEXT PRIMARY KEY, " + CAR_YEAR + " TEXT, "+MPG + " INTEGER" +")");
        //trip db
        sqLiteDatabase.execSQL("CREATE TABLE " + TRIP_TABLE_NAME +
                " (" + TRIP + " TEXT PRIMARY KEY, " + DATE + " TEXT, "+ DISTANCE + " REAL, " +
                CO2_FP + " REAL " + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(VERSION == newVersion){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CAR_TABLE_NAME + ";");
            VERSION = newVersion;
            onCreate(sqLiteDatabase);
            Toast.makeText(ctx, "TABLE IS UPGRADED", Toast.LENGTH_LONG).show();
        }
    }

    //insert trip data
    public void insertTripData(double distance, double CO2_usage , String trip_name, String date){
        sqldb = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TRIP, trip_name);
        cv.put(DATE, date);
        cv.put(DISTANCE, distance);
        cv.put(CO2_FP, CO2_usage);

        sqldb.insert(TRIP_TABLE_NAME, null, cv);
        sqldb.close();
        Log.d("db", "adding trip info");
    }
}
