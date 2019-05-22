package ucsc121.carboncars;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    //identifier
    final static int REQUEST_CODE = 100;
    Intent myIntent;
    /*******So I can receive data from service from activities******/
    public static String BROADCAST_ACTION = "ucsc121.carboncars";
    MyBroadCastReceiver broadCastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button dataVizButton = findViewById(R.id.dataVizButton);
        Button historyButton = findViewById(R.id.historybutton);
        //history button
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent historyIntent = new Intent(getApplicationContext(), tripHistory.class);
                startActivity(historyIntent);
            }
        });
        //dat viz button
        dataVizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dataVizActivityIntent = new Intent(getApplicationContext(), DatavizActivity.class);
                startActivity(dataVizActivityIntent);
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(broadCastReceiver);
    }
    //right now if user allows the thing, they'll have to click the button again to go through the
    //if statement
    public void startService(View view){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //request permissions
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}
                    ,REQUEST_CODE);
        }else{
            myIntent = new Intent(this,LocationService.class);
            startService(myIntent);
            registerReceiver();
            Log.d("debug", "Service Started");
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
    private class MyBroadCastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                Log.d("Receiver", "Received data");
                String data = intent.getStringExtra("data");
                Log.d("Receiver", "Received data" + data);
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}

