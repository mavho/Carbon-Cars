package ucsc121.carboncars;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class DatavizActivity extends AppCompatActivity {

    DataBase carbonDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataviz);

        carbonDB = new DataBase(this, "CARBON_DB", null, 2);

        Cursor size = carbonDB.getAllTrips();

        ArrayList<String> dateList = new ArrayList();

        int count = 0;
        double totalCarbon = 0;

        ImageView image = (ImageView) findViewById(R.id.traffic_light);
        TextView sum = findViewById(R.id.summary);
        TextView co2Pounds = findViewById(R.id.co2Tons2);

        if(!carbonDB.isEmpty("TRIPS")){
            Log.d("dataViz", "TRIPS LOGGED");
            if(size.moveToFirst()){
                do{
                    String date = size.getString(2);
                    Double co2 = size.getDouble(4);
                    Log.d("dataViz", "Trip " + count + ": " + co2);

                    totalCarbon += co2;

                    dateList.add(date);

                    count++;
                } while(size.moveToNext());
            }

            Log.d("dataViz", "LOGGED " + count + " trips");

            size.close();

            Double numDays = (double)getDays(dateList.get(0), dateList.get(count - 1));
            Log.d("dataViz", "numDays: " + numDays );



            double dailyCarbon = totalCarbon/numDays;
            String roundedDailyCarbon = String.format("%.2f", dailyCarbon);
            Log.d("dataViz", "dailyCarbon: " + roundedDailyCarbon);

            co2Pounds.setText(roundedDailyCarbon + " pounds");


            if(dailyCarbon < 21.0){
                image.setImageResource(R.drawable.green_traffic_light);
                sum.setText("You're doing great! Keep it up, and try and stay below the American average of 26 lbs/day!");
            }
            else if(dailyCarbon >= 21.0 && dailyCarbon <= 31.0){
                image.setImageResource(R.drawable.yellow_traffic_light);
                sum.setText("You're doing well, but there's always room for improvement!");
            }
            else{
                image.setImageResource(R.drawable.red_traffic_light);
                sum.setText("Try a little harder to get below the American average of 26 lbs/day. You can do it!");
            }
        }
        else{
            Log.d("dataViz", "NO TRIPS LOGGED");
            image.setImageResource(R.drawable.yellow_traffic_light);
            sum.setText("You haven't logged any trips yet. Try using the location tracker to do so!");
        }






        Button monthlyButton = findViewById(R.id.monthlyButton);

        monthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent monthlyActivityIntent = new Intent(getApplicationContext(), MonthlyActivity.class);
                startActivity(monthlyActivityIntent);
            }
        });

        Button weeklyButton = findViewById(R.id.weeklyButton);

        weeklyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent weeklyActivityIntent = new Intent(getApplicationContext(), WeeklyActivity.class);
                startActivity(weeklyActivityIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public long getDays(String date1, String date2){
        String nums1[] = date1.split("/");
        int day1 = Integer.parseInt(nums1[0]);
        int month1 = Integer.parseInt(nums1[1]);
        int year1 = Integer.parseInt(nums1[2]);

        Calendar myCal1 = Calendar.getInstance();
        myCal1.set(Calendar.DAY_OF_MONTH, day1);
        myCal1.set(Calendar.MONTH, month1);
        myCal1.set(Calendar.YEAR, year1);

        String nums2[] = date2.split("/");
        int day2 = Integer.parseInt(nums2[0]);
        int month2 = Integer.parseInt(nums2[1]);
        int year2 = Integer.parseInt(nums2[2]);

        Calendar myCal2 = Calendar.getInstance();
        myCal2.set(Calendar.DAY_OF_MONTH, day2);
        myCal2.set(Calendar.MONTH, month2);
        myCal2.set(Calendar.YEAR, year2);

        long startTime = myCal1.getTimeInMillis();
        long endTime = myCal2.getTimeInMillis();

        return TimeUnit.MILLISECONDS.toDays(Math.abs(endTime - startTime)) + 1;
    }
}
