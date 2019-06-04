package ucsc121.carboncars;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
    private static String CAR_TYPE= "TYPE";
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
            " (" + CAR_MODEL + " TEXT PRIMARY KEY, " + CAR_TYPE + " TEXT, " + MPG + " REAL" + ")");
        //trip db
        //we include CARMODEL so we know which car they used, and we can pull info from that
        sqLiteDatabase.execSQL("CREATE TABLE " + TRIP_TABLE_NAME +
                " (" + TRIP + " TEXT PRIMARY KEY, " + CAR_MODEL + " TEXT, " + DATE + " TEXT, "+ DISTANCE + " REAL, " +
                CO2_FP + " REAL " + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        if(VERSION == newVersion){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CAR_TABLE_NAME + ";");
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TRIP_TABLE_NAME + ";");
            VERSION = newVersion;
            onCreate(sqLiteDatabase);
            Toast.makeText(ctx, "TABLE IS UPGRADED", Toast.LENGTH_LONG).show();
        }
    }

    //insert trip data
    public boolean insertTripData(double distance, String car_model, double CO2_usage , String trip_name, String date){
        sqldb = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(TRIP, trip_name);
        cv.put(CAR_MODEL, car_model);
        cv.put(DATE, date);
        cv.put(DISTANCE, distance);
        cv.put(CO2_FP, CO2_usage);

        String query = "Select * from " + TRIP_TABLE_NAME + " where " + TRIP + " = '" + trip_name + "'";
        Cursor cursor = sqldb.rawQuery(query, null);

        if (cursor == null){
            cursor.close();
            Log.d("db", "error");
            return false;
        }else{
            Log.d("db", "adding trip info");
            sqldb.insert(TRIP_TABLE_NAME, null, cv);
        }
        sqldb.close();
        Log.d("db", "adding trip info");
        return true;
    }

    public boolean insertCarData(String car_model, String car_type, double mpg){

        sqldb = getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(CAR_MODEL, car_model);
        cv.put(CAR_TYPE, car_type);
        cv.put(MPG, mpg);

        String query = "Select * from " + CAR_TABLE_NAME + " where " + CAR_MODEL + " = '" + car_model + "'";
        //determine if key is already in there
        Cursor cursor = sqldb.rawQuery(query, null);

        if(cursor != null){
            Log.d("db", "adding trip info");
            sqldb.insert(CAR_TABLE_NAME, null, cv);
        }else{
            Log.d("db", "error");
            cursor.close();
            return false;
        }
        sqldb.close();
        return true;
    }

    public Cursor getCars() {
        sqldb = getWritableDatabase();
        String query = "SELECT * FROM " + CAR_TABLE_NAME;
        Cursor image_ptr = sqldb.rawQuery(query, null);
        return image_ptr;
    }

    public void clearTripsDB(){
        sqldb = getWritableDatabase();
        String query = "DELETE FROM " + TRIP_TABLE_NAME;
        sqldb.execSQL(query);
    }

    public void clearCarsDB() {
        sqldb = getWritableDatabase();
        String query = "DELETE FROM " + CAR_TABLE_NAME;
        sqldb.execSQL(query);
    }

    public Cursor getAllTrips(){
        sqldb = getWritableDatabase();
        String selectAll = "SELECT * FROM " + TRIP_TABLE_NAME;
        Cursor tripData = sqldb.rawQuery(selectAll,null);
        return tripData;
    }
}
