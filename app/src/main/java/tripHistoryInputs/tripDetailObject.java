package tripHistoryInputs;

import android.database.Cursor;

public class tripDetailObject {
    private String rideID;
    private String dateID;
    private String distanceID;
    private String co2ID;
    private String modelID;


    public tripDetailObject(String rideid,String model,String date, String distance, String c02){
        this.rideID = rideid;
        this.dateID = date;
        this.distanceID = distance;
        this.co2ID = c02;
        this.modelID = model;

    }
    public String getRideID(){
        return rideID;
    }
    public String getDateID(){
        return dateID;
    }
    public String getDistanceID(){
        return distanceID;
    }
    public String getCo2ID(){
        return co2ID;
    }
    public String getModelID(){
        return modelID;
    }




}
