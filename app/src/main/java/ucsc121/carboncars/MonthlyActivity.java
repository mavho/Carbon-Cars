package ucsc121.carboncars;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
        anyChartView.setProgressBar(findViewById(R.id.progress_bar));

        Cartesian cartesian = AnyChart.line();

        cartesian.animation(true);

        cartesian.padding(10d, 20d, 25d, 20d);

        cartesian.crosshair().enabled(true);
        cartesian.crosshair()
                .yLabel(true)
                .yStroke((Stroke) null, null, null, (String) null, (String) null);

        cartesian.tooltip().positionMode(TooltipPositionMode.POINT);

        cartesian.title("Pounds of CO2 Emitted in the Past Year");

        cartesian.yAxis(0).title("Pounds of CO2 Emitted");
        cartesian.xAxis(0).labels().padding(2d, 0d, 2d, 0d);

        carbonDB = new DataBase(this, "CARBON_DB", null, 2);
        Cursor trips = carbonDB.getAllTrips();

        List<DataEntry> seriesData = new ArrayList<>();

        int cursorCounter = 0;
        for(int i = 0; i < 30; i++){
            String date = trips.getString(2); // get date
            Double co2 = trips.getDouble(4); // get co2
            String nums[] = date.split("/");
            Log.d("MonthlyActivity", "Day: + " + nums[0]);
            if(nums[0].charAt(0) == '0') nums[0] = nums[0].substring(1); // check if theres a 0 in day string
            int day = Integer.parseInt(nums[0]); // turn day into int
            if(cursorCounter == 0) cursorCounter = day; // initialize cursor counter to first day

            if(day - 1 != cursorCounter){
                day = cursorCounter + 1;
                if(cursorCounter != 31) cursorCounter = cursorCounter % 31;
                co2 = 0.0;
            }
            cursorCounter = cursorCounter + 1;

            seriesData.add(new CustomDataEntry(Integer.toString(day), co2));
        }
        trips.close();

        seriesData.add(new CustomDataEntry("Jan", 3.6));
        seriesData.add(new CustomDataEntry("Feb", 5.6));
        seriesData.add(new CustomDataEntry("Mar", 8.6));
        seriesData.add(new CustomDataEntry("Apr", 10.6));
        seriesData.add(new CustomDataEntry("May", 4.6));
        seriesData.add(new CustomDataEntry("Jun", 1.6));
        seriesData.add(new CustomDataEntry("Jul", 2.6));
        seriesData.add(new CustomDataEntry("Aug", 7.6));
        seriesData.add(new CustomDataEntry("Sep", 6.6));
        seriesData.add(new CustomDataEntry("Oct", 8.6));
        seriesData.add(new CustomDataEntry("Nov", 9.6));
        seriesData.add(new CustomDataEntry("Dec", 11.6));

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

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value) {
            super(x, value);
        }

    }
}
