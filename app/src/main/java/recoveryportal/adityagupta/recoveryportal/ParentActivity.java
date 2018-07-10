package recoveryportal.adityagupta.recoveryportal;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import me.relex.circleindicator.CircleIndicator;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import recoveryportal.adityagupta.recoveryportal.Fragments.SearchByEngFragment;
import recoveryportal.adityagupta.recoveryportal.Fragments.SearchFragment;
import recoveryportal.adityagupta.recoveryportal.Fragments.SearchHistoryFragment;
import recoveryportal.adityagupta.recoveryportal.Fragments.SearchMain;
import recoveryportal.adityagupta.recoveryportal.Utils.Common;

import static com.android.volley.VolleyLog.TAG;
import static recoveryportal.adityagupta.recoveryportal.Utils.Common.generateHistory;

public class ParentActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView headerName, headerEmail;
    SharedPreferences sharedPreferences;
    boolean back = false;
    KonfettiView viewKonfetti;
    boolean donecheck = true;
    DrawerLayout drawer;


    public final int REQUEST_CHECK_SETTINGS = 1;

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case REQUEST_CHECK_SETTINGS:
                showMessageOKCancel("Looks like your gps is not turned on yet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        displayLocationSettingsRequest(ParentActivity.this);
                    }
                });
                break;
        }
    }

    private void displayLocationSettingsRequest(Context context) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(10000 / 2);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        Log.i(TAG, "All location settings are satisfied.");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");

                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the result
                            // in onActivityResult().
                            status.startResolutionForResult(ParentActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }


    public void permission_check(final int code) {
        int hasFineLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCourseLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocation != PackageManager.PERMISSION_GRANTED && hasCourseLocation != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) && !ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

                showMessageOKCancel("For accessing your location, you need to provide us the permission",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                ActivityCompat.requestPermissions(ParentActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        code);
                                ActivityCompat.requestPermissions(ParentActivity.this,
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        code);
                            }
                        });
                return;
            }

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    code);
            ActivityCompat.requestPermissions(ParentActivity.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    code);
            return;
        }

        displayLocationSettingsRequest(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Common.checkApp(this);
        Common.generateHistory(this);

        viewKonfetti = findViewById(R.id.viewKonfetti);

        viewKonfetti.post(new Runnable() {
            @Override
            public void run() {
                if (getIntent().hasExtra("done") && donecheck) {
                    donecheck = false;
                    viewKonfetti.reset();
                    viewKonfetti.build()
                            .addColors(Color.parseColor("#fce18a"), Color.parseColor("#ff726d"), Color.parseColor("#b48def"), Color.parseColor("#f4306d"))
                            .setDirection(0.0, 359.0)
                            .setSpeed(1f, 2f)
                            .setFadeOutEnabled(true)
                            .setTimeToLive(1000L)
                            .addShapes(Shape.RECT, Shape.CIRCLE)
                            .addSizes(new Size(16, 6f), new Size(12, 1f))
                            .setPosition(-50f, viewKonfetti.getWidth() + 50f, -100f, -150f)
                            .stream(300, 5000L);
                }
            }
        });

        if (Build.VERSION.SDK_INT >= 23) {
            permission_check(1);
        }

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        setTitle("Search Customer");


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerLayout =
                navigationView.inflateHeaderView(R.layout.nav_header_parent);

        headerEmail = headerLayout.findViewById(R.id.email_header);
        headerName = headerLayout.findViewById(R.id.user_name_header);

        headerName.setText(Common.loginDetails.Full_Name);
        headerEmail.setText(Common.loginDetails.Email_Id);

        navigationView.setCheckedItem(R.id.search);

        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, SearchMain.newInstance()).addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (back)
                this.finishAffinity();
            else {
                back = true;
                Toast.makeText(this, "Press Back Again to Close the App", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.search) {
            setTitle("Search Customer");
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, SearchMain.newInstance()).addToBackStack(null).commit();
        } else if (id == R.id.search_history) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, SearchHistoryFragment.newInstance()).addToBackStack(null).commit();
            setTitle("Search History");
            //circleIndicator.setVisibility(View.GONE);
        } else if (id == R.id.logout) {
            sharedPreferences.edit().remove("Username").apply();
            sharedPreferences.edit().remove("Password").apply();
            this.finishAffinity();
        } else if (id == R.id.nav_send) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{"info@finconindia.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Feedback - Android");
            try {
                startActivity(Intent.createChooser(i, "Send Mail using .."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(ParentActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_share) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_SUBJECT, "Fincon India Asset Security");
            i.putExtra(Intent.EXTRA_TEXT, "You can download Fincon India's Android app at :- \n" +
                    "http://finconindia.com/Documents/FinconAndroidApp.apk");
            startActivity(Intent.createChooser(i, "Share on .."));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
