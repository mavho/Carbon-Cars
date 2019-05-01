package ucsc121.carboncars;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.util.Log;

import com.smartcar.sdk.*;


public class MainActivity extends AppCompatActivity {
        private Context appContext;

        // TODO: Authorization Step 1a. Initialize the Smartcar object
        private static String CLIENT_ID;
        private static String REDIRECT_URI;
        private static String[] SCOPE;
        private SmartcarAuth smartcarAuth;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            appContext = getApplicationContext();

            // TODO: Authorization Step 1b: Initialize the Smartcar object
            CLIENT_ID = getString(R.string.client_id);
            REDIRECT_URI = "sc" + getString(R.string.client_id) + "://exchange";
            SCOPE = new String[]{"read_vehicle_info"};
            // TODO: Authorization Step 3b: Receive the authorization code
            smartcarAuth = new SmartcarAuth(
                CLIENT_ID,
                    REDIRECT_URI,
                    SCOPE,
                    true,
                    new SmartcarCallback() {
                        @Override
                        public void handleResponse(SmartcarResponse smartcarResponse) {
                            Log.i("Main Activity", smartcarResponse.getCode());
                            // TODO: Request Step 1: Obtain an access token

                            // TODO: Request Step 2: Get vehicle information

                        }
                    }
            );
            Button connectButton = (Button) findViewById(R.id.connect_button);

            // TODO: Authorization Step 2: Launch the authorization flow
            smartcarAuth.addClickHandler(appContext, connectButton);
        }
    }

