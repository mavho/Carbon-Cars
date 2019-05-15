package ucsc121.carboncars;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;


import java.util.ArrayList;
import java.util.Random;

public class MonthlyActivity extends AppCompatActivity {

    BarChart monthlyBarChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);

        monthlyBarChart = findViewById(R.id.monthlyBarChart);

        monthlyBarChart.setFitBars(true);

        ArrayList co2Data = new ArrayList();

        co2Data.add(new BarEntry(950f, (float)8.64));
        co2Data.add(new BarEntry(1050f, randNumber()));
        co2Data.add(new BarEntry(1150f, randNumber()));
        co2Data.add(new BarEntry(1250f, randNumber()));
        co2Data.add(new BarEntry(1350f, randNumber()));
        co2Data.add(new BarEntry(1450f, randNumber()));
        co2Data.add(new BarEntry(1550f, randNumber()));
        co2Data.add(new BarEntry(1650f, randNumber()));
        co2Data.add(new BarEntry(1750f, randNumber()));
        co2Data.add(new BarEntry(1850f, randNumber()));
        co2Data.add(new BarEntry(1950f, randNumber()));
        co2Data.add(new BarEntry(2050f, randNumber()));

        BarDataSet bardataset = new BarDataSet(co2Data, "CO2 Emissions (Kilograms)");

//        bardataset.setStackLabels(new String[] {"January", "February", "March", "April", "May",
//                "June", "July", "August", "September", "October", "November", "December"});

        String[] labels = {"January", "February", "March", "April", "May",
                "June", "July", "August", "September", "October", "November", "December"};

        monthlyBarChart.animateY(5000);


        BarData data = new BarData(bardataset);

        data.setBarWidth(30f);
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        monthlyBarChart.setData(data);
        monthlyBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
    }

    float randNumber(){
        Random random = new Random();
        return random.nextFloat() * (float)10.0;
    }
}
