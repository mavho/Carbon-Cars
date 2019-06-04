package ucsc121.carboncars;

import android.app.LauncherActivity;
import android.app.ListActivity;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class CarsListActivity extends AppCompatActivity{

    DataBase carboncars_db;
    ListView layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        carboncars_db = new DataBase(this, "CARBON_DB", null, 1);
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
}
