package ucsc121.carboncars;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Random;

public class WeeklyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly);


    }

    float randNumber(){
        Random random = new Random();
        return random.nextFloat() * (float)10.0;
    }

}
