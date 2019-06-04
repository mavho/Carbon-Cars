package ucsc121.carboncars;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
import java.util.List;
import java.util.Random;

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
        Cursor trips = carbonDB.getAllTrips();

        List<DataEntry> seriesData = new ArrayList<>();

        int cursorTracker = 0;
        while(trips.moveToNext()){
            if(cursorTracker > 7) break;

            String date = trips.getString(2);
            Double co2 = trips.getDouble(4);
            String day = getDay(date);

            seriesData.add(new CustomDataEntry(day, co2));

            cursorTracker++;
        }
        trips.close();

        seriesData.add(new CustomDataEntry("Mon", 3.6));
        seriesData.add(new CustomDataEntry("Tue", 5.6));
        seriesData.add(new CustomDataEntry("Wed", 8.6));
        seriesData.add(new CustomDataEntry("Thu", 10.6));
        seriesData.add(new CustomDataEntry("Fri", 4.6));
        seriesData.add(new CustomDataEntry("Sat", 1.6));
        seriesData.add(new CustomDataEntry("Sun", 2.6));

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

    private class CustomDataEntry extends ValueDataEntry {

        CustomDataEntry(String x, Number value) {
            super(x, value);
        }

    }

}
