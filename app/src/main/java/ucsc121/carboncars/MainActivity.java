package ucsc121.carboncars;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    boolean start = true;
    String tripname;
    String carname;
    //identifier
    final static int REQUEST_CODE = 100;
    Intent myIntent;
    /*******So I can receive data from service from activities******/
    public static String BROADCAST_ACTION = "ucsc121.carboncars";
    MyBroadCastReceiver broadCastReceiver;
    //startButton
    Button startButton;
    //reference to the database.
    DataBase carboncars_db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        carboncars_db = new DataBase(this, "CARBON_DB", null, 2);
        //listener for start button
        startButton = findViewById(R.id.location_service_but);
        startButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (start){
                    startCarService(v);
                }else{
                    startButton.setBackground(getDrawable(R.drawable.round_butt));
                    startButton.setText("START LOCATION TRACKER");
                    stopService(v);
                    start = true;
                }
            }
        });
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.history_nav:
                        Intent historyIntent = new Intent(getApplicationContext(), tripHistory.class);
                        startActivity(historyIntent);
                        break;
                    case R.id.carinput_nav:
                        Intent intent = new Intent(getApplicationContext(), CarInputActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.datViz_nav:
                        Intent dataVizActivityIntent = new Intent(getApplicationContext(), DatavizActivity.class);
                        startActivity(dataVizActivityIntent);
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(broadCastReceiver);
    }

    //function to start the car input activity
    public void startCarService(View view){
        Intent intent = new Intent(this, CarsListActivity.class);
        startActivityForResult(intent, 1);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data != null) {
            tripname = data.getStringExtra("tripname");
            carname = data.getStringExtra("carname");
            Log.d("main", "adf + " + carname + " " + tripname);
            Cursor car = carboncars_db.find(carname);
            if (car == null) {
                Log.d("main", "didnt find anythign");
            } else {
                Log.d("main", "anythign");
            }
            double mpg = 0.0;
            while (car.moveToNext()) {
                mpg = car.getDouble(2);
            }
            switch (requestCode) {
                //one is from startservice
                case 1:
                    //start service once selected car
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        //request permissions
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}
                                , REQUEST_CODE);
                    } else {
                        myIntent = new Intent(this, LocationService.class);
                        myIntent.putExtra("mpg", mpg);
                        //we got a return so start
                        startButton.setBackground(getDrawable(R.drawable.round_rbutt));
                        startButton.setText("STOP LOCATION TRACKER");
                        start = false;
                        startService(myIntent);
                        registerReceiver();
                        Log.d("debug", "Service Started");
                    }
                    break;
            }
        }
    }

    public void stopService(View view){
        myIntent = new Intent(this,LocationService.class);
        stopService(myIntent);
        TextView out = findViewById(R.id.total_distance);
        out.setText("Carbon calculations are calculated after the service stops");
        Log.d("debug", "Service Stopped");
    }

    //register a broadcast receiver to receive data from our service
    private void registerReceiver(){
        try{
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(BROADCAST_ACTION);
            broadCastReceiver = new MyBroadCastReceiver();
            registerReceiver(broadCastReceiver, intentFilter);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //private class for our broadcast receiver
    //receives when the data is stopped
    private class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                Log.d("Receiver", "Received data");
                double distance = intent.getDoubleExtra("distance", 0);
                double poundsCO2 = intent.getDoubleExtra("pOfCO2", 0.0);
                TextView out = findViewById(R.id.total_distance);
                out.setText("Total distance " + distance + " km\n" + "Approx C02 emitted " + poundsCO2);
                String date = getCurrentDate();
                carboncars_db.insertTripData(distance,carname, poundsCO2, tripname, date);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    //get the date, convert to dd/mm/yyyy
    private String getCurrentDate() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        String formattedDate = format.format(c);
        return formattedDate;
    }
}

