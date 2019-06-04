package ucsc121.carboncars;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import tripHistoryInputs.HistoryAdapter;
import tripHistoryInputs.tripDetailObject;

public class tripHistory extends AppCompatActivity {
    private RecyclerView pastTrip;
    private RecyclerView.Adapter historyadapter;
    private RecyclerView.LayoutManager historylayoutmanager;
    private ArrayList resultsHistory = new ArrayList<tripDetailObject>();
    private static final String TAG = "in trip history.java debug";
    DataBase carbondb;



    private ArrayList<tripDetailObject> getData() {
        return resultsHistory;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);
        pastTrip = findViewById(R.id.history);

        carbondb = new DataBase(this, "CARBON_DB", null, 2);
        Cursor trips = carbondb.getAllTrips();
        DecimalFormat df = new DecimalFormat("#.###");
        while(trips.moveToNext()){
            //trip,carmodel,date,distance,co2
            String tripNum = trips.getString(0);
            String model = trips.getString(1);
            String date = trips.getString(2);
            Double distance = trips.getDouble(3);
            Double co2 = trips.getDouble(4);

            Log.d("trip history","in while loop");

            double tdistance = Double.parseDouble(df.format(distance));
            double tco2 = Double.parseDouble(df.format(co2));

            String i = tripNum;
            String j = date;
            String k = tdistance + " km";
            String l = tco2 + " lbs of CO2";
            String m = model;
            tripDetailObject cardata1 = new tripDetailObject(i,m,j,k,l);
//            tripDetailObject cardata2 = new tripDetailObject(j);
//            tripDetailObject cardata3 = new tripDetailObject(k);
//            tripDetailObject cardata4 = new tripDetailObject(l);

            resultsHistory.add(cardata1);
//            resultsHistory.add(cardata2);
//            resultsHistory.add(cardata3);
//            resultsHistory.add(cardata4);
        }
        trips.close();
        Log.d(TAG, "adding data?");


        pastTrip.setNestedScrollingEnabled(false);
        pastTrip.setHasFixedSize(true);
        historylayoutmanager = new LinearLayoutManager(tripHistory.this);
        pastTrip.setLayoutManager(historylayoutmanager);
        historyadapter = new HistoryAdapter(getData(),tripHistory.this);
        pastTrip.setAdapter(historyadapter);




        historyadapter.notifyDataSetChanged();
    }


}
