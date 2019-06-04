package ucsc121.carboncars;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

// TODO: add constraints and ids for this activity's layout

public class CarInputActivity extends AppCompatActivity {

    //reference to the database.
    DataBase carboncars_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_input);
        carboncars_db = new DataBase(this, "CARBON_DB", null, 2);
    }

    public void submitForm(View view){
        Log.d("submitform", "submit form");
        int check_submit = check_submit();
        if(check_submit == 1){
            Log.d("submitfor", "fail 1");
            Toast.makeText(this, "Please fill out missing fields", Toast.LENGTH_LONG);
        }else if(check_submit == 2){
            Log.d("submitfor", "fail 2");
            Toast.makeText(this, "Please enter a number for mpg", Toast.LENGTH_LONG);
        }else{
            Log.d("submitfor", "success");
            Toast.makeText(this, "Successfully inserted car data!", Toast.LENGTH_LONG);
        }
    }

    private int check_submit(){
        String name;
        String type;
        double mpg = 0.0;

        EditText name_view = (EditText)findViewById(R.id.editText);
        RadioGroup type_group = (RadioGroup) findViewById(R.id.radioGroup);
        EditText mpg_view = (EditText)findViewById(R.id.MPG_txt);
        if(name_view.getText().toString().equals("")){
            Log.d("check_submit", "fail: no name entered");
            return 1;
        }else if(type_group.getCheckedRadioButtonId() == -1){
            Log.d("check_submit", "fail: no type entered");
            return 1;
        }else if(mpg_view.getText().toString().equals("")){
            Log.d("check_submit", "fail: no mpg entered");
            return 1;
        }else{
            //check if a number
            try{
                mpg = Double.parseDouble(mpg_view.getText().toString());
            }catch (NumberFormatException | NullPointerException ex){
                return 2;
            }
        }
        RadioButton i = findViewById(type_group.getCheckedRadioButtonId());
        
        name = name_view.getText().toString();
        type = i.getText().toString();

        if(carboncars_db.insertCarData(name, type, mpg)){
            Log.d("check_submit", "successful: " + name + " " + " " + type + " " + mpg);
            return 0;
        }else{
            Log.d("check_submit", "fail: car" + name + " already in the db");
            return 1;
        }
    }
}
