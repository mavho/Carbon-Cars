package ucsc121.carboncars;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Line;
import com.anychart.data.Mapping;
import com.anychart.data.Set;
import com.anychart.enums.Anchor;
import com.anychart.enums.MarkerType;
import com.anychart.enums.TooltipPositionMode;
import com.anychart.graphics.vector.Stroke;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class WeeklyActivity extends AppCompatActivity {

    DataBase carbonDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 25d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Pounds of CO2 Emitted in the Past Week");

        cartesian.yAxis(0).title("Pounds of CO2 Emitted");
        cartesian.xAxis(0).labels().padding(2d, 0d, 2d, 0d);

        carbonDB = new DataBase(this, "CARBON_DB", null, 2);
        List<DataEntry> seriesData = new ArrayList<>();

        boolean first = true;
        int cursorCounter = 0;
        int lastMonth = -1;
        String savedDay = "";
        Double lastCO2 = 0.0;
//        String lastDate = "";
        Boolean setLastDate = false;

        Calendar lastDate = Calendar.getInstance();
        Calendar newEntryDate = Calendar.getInstance();

        int numEntries = 0;

        Cursor size = carbonDB.getAllTrips();
        if(size.moveToFirst()){
            do{
                numEntries++;
            }while(size.moveToNext());
        }

        Log.d("monthly", "numEntries: " + numEntries);

        int nextCount = 0;

        size.close();

        Cursor trips = carbonDB.getAllTrips();
//        trips.moveToNext();

        int count = 0;
        boolean isSame = false;
        Double cumulative = 0.0;
        String prevDate = "";

        // iterate through the db trip entries and add dates to chart
        if(trips.moveToFirst()){
            do{
                String date = trips.getString(2); // get date
                String nums[] = date.split("/");
                if (nums[1].charAt(0) == '0')
                    nums[1] = nums[1].substring(1); // check if theres a 0 in day string
                if (nums[0].charAt(0) == '0')
                    nums[0] = nums[0].substring(1); // check if theres a 0 in month string

                Calendar currentDate = Calendar.getInstance();
                currentDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(nums[0]));
                currentDate.set(Calendar.MONTH, Integer.parseInt(nums[1]));
                currentDate.set(Calendar.YEAR, Integer.parseInt(nums[2]));

                Date todayDate = Calendar.getInstance().getTime();
                Calendar today = Calendar.getInstance();
                today.setTime(todayDate);

                long daysBetweenDates = daysBetweenDates(currentDate, today);
                if(daysBetweenDates <= 7) break;

            }while(trips.moveToNext());

            do{
                Log.d("monthly", "prevDate: " + prevDate );
                if(count >= 7) break; // stop adding entries if added 30

                // get date and co2
                Log.d("MonthlyActivity", "Entered main loop " + nextCount );
                String date = trips.getString(2); // get date
                Log.d("MonthlyActivity", "Date: + " + date);
                Double co2 = trips.getDouble(4); // get co2

                if(isSame){ // if the date is the same as before (as tracked by the boolean)
                    if(checkIfNextSame(trips, date)){ // check if the next date is the same, and prepare
                        cumulative += co2;
                        isSame = true;
                        continue;
                    }
                    else{ // add a new entry with the cumulative co2 for the day
                        String nums[] = date.split("/");
                        if (nums[1].charAt(0) == '0')
                            nums[1] = nums[1].substring(1); // check if theres a 0 in day string
                        if (nums[0].charAt(0) == '0')
                            nums[0] = nums[0].substring(1); // check if theres a 0 in month string

                        Calendar currentDate = Calendar.getInstance();
                        currentDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(nums[0]));
                        currentDate.set(Calendar.MONTH, Integer.parseInt(nums[1]));
                        currentDate.set(Calendar.YEAR, Integer.parseInt(nums[2]));

                        // if there isa prev date set
                        if(!prevDate.equals("")){
                            String nums2[] = prevDate.split("/");
                            if (nums2[1].charAt(0) == '0')
                                nums2[1] = nums2[1].substring(1); // check if theres a 0 in day string
                            if (nums2[0].charAt(0) == '0')
                                nums2[0] = nums2[0].substring(1); // check if theres a 0 in month string

                            Calendar calPrevDate = Calendar.getInstance();
                            calPrevDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(nums2[0]));
                            calPrevDate.set(Calendar.MONTH, Integer.parseInt(nums2[1]));
                            calPrevDate.set(Calendar.YEAR, Integer.parseInt(nums2[2]));

                            long daysBetween = daysBetweenDates(calPrevDate, currentDate);
                            Log.d("monthly", "Days between: " + daysBetween);

                            if(daysBetween > 1){
                                Calendar tempDate = calPrevDate;
                                tempDate.add(Calendar.DAY_OF_YEAR, 1);

                                for(int i = 0; i < daysBetween - 1; i++){
                                    String month = Integer.toString(tempDate.get(Calendar.MONTH));
                                    String day = Integer.toString(tempDate.get(Calendar.DAY_OF_MONTH));

                                    String newDate = day + "/" + month;
                                    seriesData.add(new CustomDataEntry(newDate, 0.0));

                                    tempDate.add(Calendar.DAY_OF_YEAR, 1);
                                    count++;
                                    if(count >= 7) break;
                                }
                            }
                        }

                        if(count >= 7) break;

                        // add the current date as an entry
                        String finalDate = nums[0] + "/" + nums[1];
                        seriesData.add(new CustomDataEntry(finalDate, co2 + cumulative));

                        // reset the same day trackers
                        cumulative = 0.0;
                        isSame = false;

                        prevDate = date;
                        count++;

                        continue;
                    }
                }

                // first check to see if the next date will be the same
                if(checkIfNextSame(trips, date)){
                    Log.d("monthly", "Next is same");
                    cumulative += co2;
                    isSame = true;
                    continue;
                }

                String nums[] = date.split("/");
                if (nums[1].charAt(0) == '0')
                    nums[1] = nums[1].substring(1); // check if theres a 0 in day string
                if (nums[0].charAt(0) == '0')
                    nums[0] = nums[0].substring(1); // check if theres a 0 in month string

                Calendar currentDate = Calendar.getInstance();
                currentDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(nums[0]));
                currentDate.set(Calendar.MONTH, Integer.parseInt(nums[1]));
                currentDate.set(Calendar.YEAR, Integer.parseInt(nums[2]));

                // if there isa prev date set
                if(!prevDate.equals("")){
                    String nums2[] = prevDate.split("/");
                    if (nums2[1].charAt(0) == '0')
                        nums2[1] = nums2[1].substring(1); // check if theres a 0 in day string
                    if (nums2[0].charAt(0) == '0')
                        nums2[0] = nums2[0].substring(1); // check if theres a 0 in month string

                    Calendar calPrevDate = Calendar.getInstance();
                    calPrevDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(nums2[0]));
                    calPrevDate.set(Calendar.MONTH, Integer.parseInt(nums2[1]));
                    calPrevDate.set(Calendar.YEAR, Integer.parseInt(nums2[2]));

                    long daysBetween = daysBetweenDates(calPrevDate, currentDate);
                    Log.d("monthly", "Days between: " + daysBetween);

                    if(daysBetween > 1){
                        Calendar tempDate = calPrevDate;
                        tempDate.add(Calendar.DAY_OF_YEAR, 1);

                        for(int i = 0; i < daysBetween - 1; i++){
                            String month = Integer.toString(tempDate.get(Calendar.MONTH));
                            String day = Integer.toString(tempDate.get(Calendar.DAY_OF_MONTH));

                            String newDate = day + "/" + month;
                            seriesData.add(new CustomDataEntry(newDate, 0.0));

                            tempDate.add(Calendar.DAY_OF_YEAR, 1);
                            count++;

                            if(count >= 7) break;
                        }
                    }
                }

                if(count >= 7) break;

                String finalDate = nums[0] + "/" + nums[1];

                prevDate = date;

                seriesData.add(new CustomDataEntry(finalDate, co2));

                count++;
            }while(trips.moveToNext());
        }

        Log.d("monthly", "Count: " + count);



        Set set = Set.instantiate();
        set.data(seriesData);
        Mapping series1Mapping = set.mapAs("{ x: 'x', value: 'value' }");

        Line series1 = cartesian.line(series1Mapping);
        series1.name("Month");
        series1.stroke("{ color: '#8fd694', thickness: 3, lineJoin: 'bevel', lineCap: 'round' }");
        series1.hovered().markers().enabled(true);
        series1.hovered().markers()
                .type(MarkerType.CIRCLE)
                .size(4d);
        series1.tooltip()
                .position("right")
                .anchor(Anchor.LEFT_CENTER)
                .offsetX(5d)
                .offsetY(5d);

        anyChartView.setChart(cartesian);
    }

    public String getDay(String date){
        String nums[] = date.split("/");
        int day = Integer.parseInt(nums[1]);
        int month = Integer.parseInt(nums[0]);
        int year = Integer.parseInt(nums[2]);

        Calendar myCal = Calendar.getInstance();
        myCal.set(Calendar.DAY_OF_MONTH, day);
        myCal.set(Calendar.MONTH, month);
        myCal.set(Calendar.YEAR, year);
        int dayOfWeek = myCal.get(Calendar.DAY_OF_WEEK);

        String result = "";

        switch(dayOfWeek){
            case 1:
                result = "Sun";
                break;
            case 2:
                result = "Mon";
                break;
            case 3:
                result = "Tue";
                break;
            case 4:
                result = "Wed";
                break;
            case 5:
                result = "Thu";
                break;
            case 6:
                result = "Fri";
                break;
            case 7:
                result = "Sat";
                break;
        }

        return result;
    }

    public boolean checkIfNextSame(Cursor c, String date){
        if(c.moveToNext()){
            String newDate = c.getString(2);
            c.moveToPrevious();
            if(date.equals(newDate)) return true;
            return false;
        }
        return false;
    }

    // finding difference in time based on
    // https://stackoverflow.com/questions/3796841/getting-the-difference-between-date-in-days-in-java
    public long daysBetweenDates(Calendar first, Calendar last){
        Date startTime = first.getTime();
        Date lastTime = last.getTime();

        long realStartTime = startTime.getTime();
        long realLastTime = lastTime.getTime();
        Log.d("time", "start " + realStartTime );
        Log.d("time", "end " + realLastTime );

        long dif = realLastTime - realStartTime;
        long result = dif/(1000 * 60 * 60 * 24);

        return result;
    }

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value) {
            super(x, value);
        }

    }

}
