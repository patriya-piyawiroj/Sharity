package itp341.piyawiroj.patriya.sharity.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.List;

import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

import itp341.piyawiroj.patriya.sharity.R;
import itp341.piyawiroj.patriya.sharity.util.FirebaseUtility;
import itp341.piyawiroj.patriya.sharity.models.DonationCenter;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final String LOCATION_PREFERENCE = "piyawiroj.patriya.Sharity.preference.accessed";
    public static final String SHARED_PREFERENCES_ID = "piyawiroj.patriya.Sharity.shared.preferences";

    private Button useCurrentLocationButton;
    private Button findCentersNearMeButton;
    private SearchView searchView;
    private TextView locationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUtility u = new FirebaseUtility(getApplicationContext());
        u.getAll();

        setContentView(R.layout.main_activity);
        searchView = findViewById(R.id.locationSearchView);
        searchView.setQueryHint("Enter a zip code");
        searchView.setIconified(false);
        searchView.clearFocus();
        searchView.getBackground().setLevel(1);

        useCurrentLocationButton = findViewById(R.id.useCurrentLocationButton);
        useCurrentLocationButton.getBackground().setLevel(0);
        useCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check for permissions
                if ( ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
                    //request permissions
                    String[] permissionsList = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION};
                    requestPermissions(permissionsList, 0);
                } else {
                    Intent i = new Intent(getApplicationContext(), ChooseDonationsActivity.class);
                    LocationManager lm = (LocationManager)getSystemService(getApplicationContext().LOCATION_SERVICE);
                    Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    double longitude = location.getLongitude();
                    double latitude = location.getLatitude();
                    i.putExtra(DonationCenter.EXTRA_LATITUDE, latitude);
                    i.putExtra(DonationCenter.EXTRA_LONGITUDE, longitude);
                    i.putExtra(DonationCenter.EXTRA_LOCATION, "none");

                    //save zip code
                    String zip = getZipFromLocation(latitude, longitude);
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(MainActivity.LOCATION_PREFERENCE, zip);
                    editor.commit();

                    startActivity(i);
                }

            }
        });
        findCentersNearMeButton = findViewById(R.id.findCentersButton);
        findCentersNearMeButton.getBackground().setLevel(2);
        findCentersNearMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String zip = searchView.getQuery().toString();
                Intent i = new Intent(getApplicationContext(), ChooseDonationsActivity.class);
                i.putExtra(DonationCenter.EXTRA_LOCATION, zip);

                //save zip code
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(MainActivity.LOCATION_PREFERENCE, zip);
                editor.commit();

                startActivity(i);

            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE);
        String accessed = sharedPreferences.getString(LOCATION_PREFERENCE, "");
        locationTextView = findViewById(R.id.savedLocationTextView);
        if (accessed != "") {
            locationTextView.setText(String.format(getString(R.string.useExistingLocation),accessed));
            locationTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_ID, MODE_PRIVATE);
                    String zip = sharedPreferences.getString(LOCATION_PREFERENCE, "");
                    Intent i = new Intent(getApplicationContext(), ChooseDonationsActivity.class);
                    i.putExtra(DonationCenter.EXTRA_LOCATION, zip);
                    startActivity(i);

                }
            });
        } else {
            locationTextView.setText("");
        }
    }

    public String getZipFromLocation(double lat, double lng) {

        Geocoder coder= new Geocoder(getApplicationContext());
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocation(lat, lng, 1);
            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            return location.getPostalCode();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
