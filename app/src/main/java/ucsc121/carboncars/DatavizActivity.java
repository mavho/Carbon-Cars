package ucsc121.carboncars;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DatavizActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dataviz);

        Button monthlyButton = findViewById(R.id.monthlyButton);

        monthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent monthlyActivityIntent = new Intent(getApplicationContext(), MonthlyActivity.class);
                startActivity(monthlyActivityIntent);
            }
        });
    }
}
