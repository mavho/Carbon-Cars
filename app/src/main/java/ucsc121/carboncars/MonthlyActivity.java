package ucsc121.carboncars;

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
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

// AnyCharts code here based off code from the AnyCharts
// sample code in https://github.com/AnyChart/AnyChart-Android

public class MonthlyActivity extends AppCompatActivity {

    DataBase carbonDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);

        AnyChartView anyChartView = findViewById(R.id.any_chart_view);
        anyChartView.setProgressBar(findViewById(R.id.progress_bar1));

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 25d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Pounds of CO2 Emitted in the Past Month");

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
        if(trips.moveToFirst()){
            do{
                if(count == 30) break;
                Log.d("MonthlyActivity", "Entered main loop " + nextCount );
                String date = trips.getString(2); // get date
                Log.d("MonthlyActivity", "Date: + " + date);

                Double co2 = trips.getDouble(4); // get co2

                String nums[] = date.split("/");
                if (nums[1].charAt(0) == '0')
                    nums[1] = nums[1].substring(1); // check if theres a 0 in day string
                if (nums[0].charAt(0) == '0')
                    nums[0] = nums[0].substring(1); // check if theres a 0 in month string

                String finalDate = nums[0] + "/" + nums[1];

                seriesData.add(new CustomDataEntry(finalDate, co2));
                count++;
            }while(trips.moveToNext());
        }





////        while(cursorCounter != 30) {
//        if(trips.moveToFirst()) {
//            do {
//                if(nextCount == numEntries) break;
////            if(first){
////                trips.moveToNext();
////                first = false;
////            }
//                // get date and co2
//                Log.d("MonthlyActivity", "Entered main loop " + nextCount );
//                String date = trips.getString(2); // get date
//                Log.d("MonthlyActivity", "Date: + " + date);
//
//
//                Double co2 = trips.getDouble(4); // get co2
//
//                String nums[] = date.split("/");
//                if (nums[1].charAt(0) == '0')
//                    nums[1] = nums[1].substring(1); // check if theres a 0 in day string
//                if (nums[0].charAt(0) == '0')
//                    nums[0] = nums[0].substring(1); // check if theres a 0 in month string
//
//                int day = Integer.parseInt(nums[1]); // turn day into int
////            if (cursorCounter == 0) cursorCounter = day; // initialize cursor counter to first day
//                int month = Integer.parseInt(nums[0]);
//                int year = Integer.parseInt(nums[2]);
//
//                newEntryDate.set(Calendar.DAY_OF_MONTH, day);
//                newEntryDate.set(Calendar.MONTH, month);
//                newEntryDate.set(Calendar.YEAR, year);
//
//                long daysBetween = daysBetweenDates(lastDate, newEntryDate);
//                if (!setLastDate) { // if last date isnt set (first entry processing)
//                    lastDate = newEntryDate; // set last date
//                    setLastDate = true;
//                    lastCO2 = 0.0;
//                    seriesData.add(new CustomDataEntry(nums[1], co2));
//                    Log.d("monthly", "Added !setLastDate Date: " + date + " " + trips.getString(2));
//                    cursorCounter++;
//                    boolean movedNext = trips.moveToNext();
//                    nextCount++;
//                    if (!movedNext) break;
//                    continue;
//                } else {
//                    Log.d("monthly", "In the else");
//                    // if new entry is after last date and last = newEntry previously
//                    if (lastCO2 != 0.0 && savedDay != "" && newEntryDate.after(lastDate)) {
//                        Log.d("monthly", "In the collective");
//                        seriesData.add(new CustomDataEntry(savedDay, co2));
//                        Log.d("monthly", "Added collective Date: " + date + " " + trips.getString(2));
//                        lastCO2 = 0.0;
//                        savedDay = "";
//
//                        // check if there are gaps
//                        if (daysBetween != 1) {
//                            int dayOfMonth = lastDate.get(Calendar.DAY_OF_MONTH);
//                            lastDate.add(Calendar.DATE, 1); // add a day to the last date
//
//                            seriesData.add(new CustomDataEntry(Integer.toString(dayOfMonth), 0.0)); // add blank val
//                            Log.d("monthly", "Added collective blank Date: " + date + " " + trips.getString(2));
//                            cursorCounter++; // add 1 to the day counter
//                            continue;
//                        } else { // add as normal
//                            if(nextCount == (numEntries - 1)){
//                                seriesData.add(new CustomDataEntry(nums[1], co2));
//                                Log.d("monthly", "Added collective normal Date: " + date + " " + trips.getString(2));
//                                lastDate = newEntryDate;
//                                cursorCounter++;
//                                break;
//                            }
//
//                            seriesData.add(new CustomDataEntry(nums[1], co2));
//                            Log.d("monthly", "Added collective normal Date: " + date + " " + trips.getString(2));
//                            lastDate = newEntryDate;
//                            cursorCounter++;
//                            nextCount++;
//                            boolean movedNext = trips.moveToNext();
//                            if (!movedNext) break;
//                        }
//                        //check for gap days
//                    } else if (newEntryDate.equals(lastDate)) {
//                        Log.d("monthly", "In the equal");
//                        lastCO2 += co2; // save co2 and last day
//                        savedDay = nums[1];
//
//                        Log.d("monthly", "nextCount: " + nextCount);
//
//                        if(nextCount == (numEntries - 1)){
//                            seriesData.add(new CustomDataEntry(nums[1], lastCO2));
//                            Log.d("monthly", "Added collective sum Date: " + date + " " + trips.getString(2));
//                            cursorCounter++;
//                            break;
//                        }
//                        else{
//                            nextCount++;
//                            if (!trips.moveToNext()) { // if no other entries, add whats been added up so far
//                                seriesData.add(new CustomDataEntry(nums[1], lastCO2));
//                                Log.d("monthly", "Added collective sum Date: " + date + " " + trips.getString(2));
//                                cursorCounter++;
//                                break;
//                            }
//                            else continue;
//                        }
////
//
//                    } else if (daysBetween != 1) { // same as daysbetween != 1 previously
//                        Log.d("monthly", "In the gap");
//                        int dayOfMonth = lastDate.get(Calendar.DAY_OF_MONTH);
//                        lastDate.add(Calendar.DATE, 1);
//
//                        seriesData.add(new CustomDataEntry(Integer.toString(dayOfMonth), 0.0));
//                        Log.d("monthly", "Added gap Date: " + date + " " + trips.getString(2));
//                        cursorCounter++;
//                        continue;
//                    } else { // as normal
//                        if(nextCount == (numEntries - 1)){
//                            Log.d("monthly", "In the else else nextcount");
//                            lastDate = newEntryDate;
//                            seriesData.add(new CustomDataEntry(nums[1], co2));
//                            Log.d("monthly", "Added normal Date: " + date + " " + trips.getString(2));
//                            cursorCounter++;
//                            break;
//                        }
//
//                        Log.d("monthly", "In the else else");
//                        lastDate = newEntryDate;
//                        seriesData.add(new CustomDataEntry(nums[1], co2));
//                        Log.d("monthly", "Added normal Date: " + date + " " + trips.getString(2));
//                        cursorCounter++;
//                        boolean movedNext = trips.moveToNext();
//                        nextCount++;
//                        if (!movedNext) break;
//                    }
//                }
//            }while(cursorCounter != 30);
//        }













//            // if the last processed date is the same, store the co2 and move to next
//            if(lastDate.equals(date)){
//                lastCO2 += co2;
//                boolean movedNext = trips.moveToNext();
//                if(!movedNext) break;
//            }

//            lastDate = date; // set new lastdate

            // split date by / delimiter

            // if the first day number starts with 0 (01/1/1) then remove the 0
//            if(nums[1].charAt(0) == '0') nums[1] = nums[1].substring(1); // check if theres a 0 in day string
//            if(nums[0].charAt(0) == '0') nums[0] = nums[0].substring(1); // check if theres a 0 in month string
//
//            if(lastMonth == -1){
//                lastMonth = month; // if no months before, set last month to new month
//            }

//            //if the current day skipped a day, add to day counter
//            if(day - 1 != cursorCounter){
//                cursorCounter = cursorCounter + 1;
//                if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12){
//                    cursorCounter = cursorCounter % 31; //if the current day is
//                }
//                else if(month == 4 || month == 6 || month == 9 || month == 11){
//                    cursorCounter = cursorCounter % 30;
//                }
//                else{
//                    cursorCounter = cursorCounter % 28;
//                }
//
//                co2 = 0.0;
//                continue;
//            }

//            if(cursorCounter != 31) cursorCounter = cursorCounter % 31;
//            cursorCounter = cursorCounter + 1;

//            lastDate = date;



        trips.close();

//        seriesData.add(new CustomDataEntry("Jan", 3.6));
//        seriesData.add(new CustomDataEntry("Feb", 5.6));
//        seriesData.add(new CustomDataEntry("Mar", 8.6));
//        seriesData.add(new CustomDataEntry("Apr", 10.6));
//        seriesData.add(new CustomDataEntry("May", 4.6));
//        seriesData.add(new CustomDataEntry("Jun", 1.6));
//        seriesData.add(new CustomDataEntry("Jul", 2.6));
//        seriesData.add(new CustomDataEntry("Aug", 7.6));
//        seriesData.add(new CustomDataEntry("Sep", 6.6));
//        seriesData.add(new CustomDataEntry("Oct", 8.6));
//        seriesData.add(new CustomDataEntry("Nov", 9.6));
//        seriesData.add(new CustomDataEntry("Dec", 11.6));

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

    public String getMonth(String date){
        String nums[] = date.split("/");
        int month = Integer.parseInt(nums[0]);
        String result = "";

        switch(month){
            case 1:
                result = "Jan";
                break;
            case 2:
                result = "Feb";
                break;
            case 3:
                result = "Mar";
                break;
            case 4:
                result = "Apr";
                break;
            case 5:
                result = "May";
                break;
            case 6:
                result = "Jun";
                break;
            case 7:
                result = "Jul";
                break;
            case 8:
                result = "Aug";
                break;
            case 9:
                result = "Sep";
                break;
            case 10:
                result = "Oct";
                break;
            case 11:
                result = "Nov";
                break;
            case 12:
                result = "Dec";
                break;
        }

        return result;
    }

    // finding difference in time based on
    // https://stackoverflow.com/questions/3796841/getting-the-difference-between-date-in-days-in-java
    public long daysBetweenDates(Calendar first, Calendar last){
        Date startTime = first.getTime();
        Date lastTime = last.getTime();

        long realStartTime = startTime.getTime();
        long realLastTime = lastTime.getTime();
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
