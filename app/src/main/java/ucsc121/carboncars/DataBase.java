package ucsc121.carboncars;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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

    //init
    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
        ctx = context;
        VERSION = version;
        DB_NAME = name;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + CAR_TABLE_NAME +
            " (" + CAR_MODEL + " TEXT PRIMARY KEY, " + CAR_YEAR + " TEXT, "+MPG + " INTEGER" +")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVerison, int newVersion) {
        if(VERSION == newVersion){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CAR_TABLE_NAME + ";");
            VERSION = newVersion;
            onCreate(sqLiteDatabase);
            Toast.makeText(ctx, "TABLE IS UPGRADED", Toast.LENGTH_LONG).show();
        }
    }
}
