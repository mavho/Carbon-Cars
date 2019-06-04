package ucsc121.carboncars;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CarsListActivity extends AppCompatActivity implements CustAdapter.OnCarSelectedListener{

    DataBase carboncars_db;
    ListView layout;
    TextView trip_name;
    Intent result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carboncars_db = new DataBase(this, "CARBON_DB", null, 2);
        setContentView(R.layout.activity_cars_list);
        layout = (ListView) findViewById(R.id.car_list);
        view_All();
    }//view all

    public void view_All(){
        //View view = getLayoutInflater().inflate(R.layout.activity_cars_list, null);
        Cursor cars = carboncars_db.getCars();
        ArrayList <String> names = new ArrayList<>();
        while(cars.moveToNext()){
            Log.d("carlistact","poo");
            names.add(cars.getString(0));
        }
        if(names.size() == 0){
            Log.d("image_item", "NULL EMPTY  NADA");
            Toast.makeText(CarsListActivity.this,"Invalid range", Toast.LENGTH_LONG).show();
        }else{
            //there is something to display
            CustAdapter adapter = new CustAdapter(this, names);
            layout.setAdapter(adapter);
        }

    }

    @Override
    public void onCarSelected(String carID) {
        //get carid
        result = new Intent();
        result.putExtra("carname", carID);
        //get tripname
        trip_name = findViewById(R.id.trip_name);
        String tripname = trip_name.getText().toString();

        if(tripname.equals("")){
            Toast.makeText(this, "Please insert a trip name",Toast.LENGTH_SHORT);
            //use just needs to try again
        }else{
            result.putExtra("tripname",tripname);
            //set the result to this
            setResult(1, result);
            finish();
        }
    }

}
