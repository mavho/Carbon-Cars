package ucsc121.carboncars;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;

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
        carbondb = new DataBase(this, "CARBON_DB", null, 1);
        carbondb.insertTripData(123.0, "Toyota",34.3, "Test1","01/27/19");
        carbondb.insertTripData(345.0, "Mazda",56.3, "Test2","01/28/19");
        carbondb.insertTripData(9823.0, "Honda",51.3, "Test3","01/24/19");
        Cursor trips = carbondb.getAllTrips();
        while(trips.moveToNext()){
            //date distance CO2
            String tripNum = trips.getString(trips.getColumnIndex("TRIP"));
            String date = trips.getString(trips.getColumnIndex("DATE"));
            Integer distance = trips.getInt(trips.getColumnIndex("DISTANCE"));
            Integer co2 = trips.getInt(3);
            Log.d("trip history","in while loop");


            String i = tripNum;
            String j = date;
            String k = Integer.toString(distance);
            String l = Integer.toString(co2);
            tripDetailObject cardata1 = new tripDetailObject(i,j,k,l);
//            tripDetailObject cardata2 = new tripDetailObject(j);
//            tripDetailObject cardata3 = new tripDetailObject(k);
//            tripDetailObject cardata4 = new tripDetailObject(l);

            resultsHistory.add(cardata1);
//            resultsHistory.add(cardata2);
//            resultsHistory.add(cardata3);
//            resultsHistory.add(cardata4);
        }
        trips.close();



//        String i = "Trip 1                  5 miles         5kg CO2\n";
//        String j = "Trip 2                  10 miles       12kg CO2\n";
//        String k = "Trip 3                  20 miles       19kg CO2\n";
//        String l = "Trip 4                  12 miles       13kg CO2\n";
//        String m = "Trip 5                  15 miles       14kg CO2\n";
//        String n = "Trip 6                  4 miles        3kg CO2\n";
//        String o = "Trip 7                  22 miles       21kg CO2\n";
//        String p = "Trip 8                  34 miles       29kg CO2\n";
//        String q = "Trip 9                  51 miles       33kg CO2\n";
//        tripDetailObject cardata1 = new tripDetailObject(i);
//        tripDetailObject cardata2 = new tripDetailObject(j);
//        tripDetailObject cardata3 = new tripDetailObject(k);
//        tripDetailObject cardata4 = new tripDetailObject(l);
//        tripDetailObject cardata5 = new tripDetailObject(m);
//        tripDetailObject cardata6 = new tripDetailObject(n);
//        tripDetailObject cardata7 = new tripDetailObject(o);
//        tripDetailObject cardata8 = new tripDetailObject(p);
//        tripDetailObject cardata9 = new tripDetailObject(q);
//        resultsHistory.add(cardata1);
//        resultsHistory.add(cardata2);
//        resultsHistory.add(cardata3);
//        resultsHistory.add(cardata4);
//        resultsHistory.add(cardata5);
//        resultsHistory.add(cardata6);
//        resultsHistory.add(cardata7);
//        resultsHistory.add(cardata8);
//        resultsHistory.add(cardata9);
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
