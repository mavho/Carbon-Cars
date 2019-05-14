package ucsc121.carboncars;

import android.Manifest;
import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;


public class LocationService extends Service {
    private LocationManager locationManager;
    private LocationListener locListener = new myLocationListener();
    public int total_distance = 0;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    private Handler handler = new Handler();
    private Runnable rt;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getBaseContext(),total_distance + " km", Toast.LENGTH_LONG).show();
        handler.removeCallbacks(rt);
        locationManager.removeUpdates(locListener);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(getBaseContext(), "Service Started", Toast.LENGTH_SHORT).show();
        //thread to run the code
        rt = new Runnable() {
            public void run() {
                //Log.v("Debug", "starting up thread");
                location();
                //delay in milli, when in the background or something we need to delaty this
                handler.postDelayed(this, 5000);
            }
        };
        //delay in milli, when in the background or something we need to delaty this
        handler.postDelayed(rt, 5000);
        //https://stackoverflow.com/questions/9093271/start-sticky-and-start-not-sticky
        return START_STICKY;
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
        }
        if (network_enabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                   != PackageManager.PERMISSION_GRANTED
                 && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }else{
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
                Log.v("Debug", "Disabled..");
            }
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
        int count = 0;

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
                if (count == 0){
                    distance = CalculationByDistance(lat_new, lon_new, lat_new, lon_new);
                    count++;
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
}
