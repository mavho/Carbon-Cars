package ucsc121.carboncars;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class tripHistory extends AppCompatActivity {
    private RecyclerView pastTrip;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_history);
        pastTrip = findViewById(R.id.history);
        pastTrip.setNestedScrollingEnabled(true);
        pastTrip.setHasFixedSize(true);
    }
}
