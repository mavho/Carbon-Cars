package ucsc121.carboncars;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;


public class LocationService extends Service {
    private LocationManager locationManager;
    private LocationListener locListener = new myLocationListener();
    public double total_distance = 0;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    private Handler handler = new Handler();
    private Runnable rt;
    Intent intent;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Toast.makeText(getBaseContext(),total_distance + " km", Toast.LENGTH_LONG).show();
        Log.d("on destroy", total_distance + " km");
        sendBroadcast();
        handler.removeCallbacks(rt);
        if( locationManager != null)
            locationManager.removeUpdates(locListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.intent = intent;
//        Toast.makeText(getBaseContext(), "Service Started", Toast.LENGTH_SHORT).show();
        //thread to run the code
        rt = new Runnable() {
            public void run() {
                //Log.v("Debug", "starting up thread");
                location();
                //delay in milli, when in the background or something we need to delay this
                handler.postDelayed(this, 5000);
            }
        };
        //delay in milli, when in the background or something we need to delay this
        handler.postDelayed(rt, 5000);
        //https://stackoverflow.com/questions/9093271/start-sticky-and-start-not-sticky
        return START_STICKY;
    }
    //function to send data to an activity
    private void sendBroadcast(){
        try{
            Double mpg = intent.getDoubleExtra("mpg", 28.0);
            double CO2_pounds;
            Intent broadCastIntent = new Intent();
            CO2_pounds = CalculatePoundsCO2(total_distance, 1.0/mpg);
            broadCastIntent.setAction(MainActivity.BROADCAST_ACTION);
            broadCastIntent.putExtra("pOfCO2",CO2_pounds);
            broadCastIntent.putExtra("distance", total_distance);
            sendBroadcast(broadCastIntent);
            Log.d("broadcast", "sent");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //location updates
    public void location() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //get permissions, might be redundant, but fine for more error checking
        try {
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
            Log.d("Exception", "gps not enabled");
        }
        try {
            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
            Log.d("Exception", "network not enabled");
        }
        if (gps_enabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                return;
            }else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
            }
        }else{
            Toast.makeText(getBaseContext(), "Location not enabled!", Toast.LENGTH_LONG).show();
        }
        if (network_enabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                   != PackageManager.PERMISSION_GRANTED
                 && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }else{
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
            }
        }else{
            Toast.makeText(getBaseContext(), "Location not enabled!", Toast.LENGTH_LONG).show();
        }
        Log.v("Debug", "in on create..3");
    }


    private class myLocationListener implements LocationListener {
        double lat_old = 0.0;
        double lon_old = 0.0;
        double lat_new;
        double lon_new;
        double time = 10.00;
        double speed = 0.0;
        boolean first = true;

        @Override
        public void onLocationChanged(Location location) {
            Log.v("Debug", "Location has changed");
            if(location != null){
                locationManager.removeUpdates(locListener);
                lat_new = location.getLongitude();
                lon_new = location.getLatitude();
                String longitude = "Longitude: " +location.getLongitude();
                String latitude = "Latitude: " +location.getLatitude();
                double distance;
                if (first){
                    distance = CalculationByDistance(lat_new, lon_new, lat_new, lon_new);
                    first = false;
                }else{
                    distance = CalculationByDistance(lat_new, lon_new, lat_old, lon_old);
                }
                total_distance += distance;
                //speed
                speed = distance/time;
                Toast.makeText(getApplicationContext(), longitude+"\n"+latitude+"\nDistance is: "
                        +distance+"\nSpeed is: "+speed , Toast.LENGTH_LONG).show();

                //update the longitude and latitude after the distance
                lat_old = lat_new;
                lon_old = lon_new;
            }
        }
        @Override
        public void onProviderDisabled(String provider) {}
        @Override
        public void onProviderEnabled(String provider) {}
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    }
    //standard distance calculations, returns km
    public double CalculationByDistance(double lat1, double lon1, double lat2, double lon2) {
        //radius of the earth in KM
        double radius =  6371.00;
        //Latitudes and longitudes
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return radius * c;
    }

    //CO2 Emissions Equation/Pseudocode
//Equation: (Miles driven during trip) * (miles per gallon of car) *
//        (19.6 pounds of CO2 per gallon) = pounds of CO2 (note the pounds)
//    - Can be converted to kilograms (possible user option?)
//    - When you use the mpg as a variable, do 1/(mpg var) to match the above equation
//example: I (theoretically) have a car that does 28 mpg. I drove 14 miles during my last trip
//Equation: 14 miles * (1gallon/28miles) * (19.6 pounds of CO2 per gallon) = 9.8 pounds of CO2 emitted
// CO2 emissions per gallon taken from https://www.eia.gov/environment/emissions/co2_vol_mass.php

    private double CalculatePoundsCO2(double distance, double mpg){
        double emissions_pounds, distance_miles;
        //km to miles
        distance_miles = distance * 0.62137;
        emissions_pounds = distance_miles * mpg * 19.6;
        return emissions_pounds;
    }
}
