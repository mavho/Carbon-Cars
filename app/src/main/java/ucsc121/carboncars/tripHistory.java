package ucsc121.carboncars;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tripHistoryInputs.HistoryAdapter;
import tripHistoryInputs.tripDetailObject;

public class tripHistory extends AppCompatActivity {
    private RecyclerView pastTrip;
    private RecyclerView.Adapter historyadapter;
    private RecyclerView.LayoutManager historylayoutmanager;
    private ArrayList resultsHistory = new ArrayList<tripDetailObject>();
    private static final String TAG = "in trip history.java debug";





    private ArrayList<tripDetailObject> getData() {
        return resultsHistory;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);
        pastTrip = findViewById(R.id.history);

        String i = "Trip 1                  5 miles         5kg CO2\n";
        String j = "Trip 2                  10 miles       12kg CO2\n";
        String k = "Trip 3                  20 miles       19kg CO2\n";
        tripDetailObject cardata = new tripDetailObject(i);
        tripDetailObject cardata2 = new tripDetailObject(j);
        tripDetailObject cardata3 = new tripDetailObject(k);
        resultsHistory.add(cardata);
        resultsHistory.add(cardata2);
        resultsHistory.add(cardata3);
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
