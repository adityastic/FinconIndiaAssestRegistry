package recoveryportal.adityagupta.recoveryportal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import recoveryportal.adityagupta.recoveryportal.ActionActivities.CollectionDone;
import recoveryportal.adityagupta.recoveryportal.ActionActivities.NewDetailsDone;
import recoveryportal.adityagupta.recoveryportal.ActionActivities.NoActionDone;
import recoveryportal.adityagupta.recoveryportal.ActionActivities.RepoDone;
import recoveryportal.adityagupta.recoveryportal.Placeholders.NOInternetPlaceHolder;
import recoveryportal.adityagupta.recoveryportal.Utils.Common;

import static com.android.volley.VolleyLog.TAG;

public class DetailsActivity extends AppCompatActivity {

    TextView repostatus,customerName, vehModel, loanNo, engNo, chaNo, bucket, prinOS, emiAmount, bounceCharge, lppdue, firstEmi, loanEndDate, branch, client;
    FloatingActionMenu fmenu;
    Context context;
    LinearLayout ll;

    boolean clicked = false;

    @Override
    public void onBackPressed() {
        if(fmenu.isOpened())
        {
            fmenu.close(true);
        }else
        {
            Toast.makeText(context, "Cannot go back without entering Action. Please enter ACTION from the floating button", Toast.LENGTH_SHORT).show();
        }
    }

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
                        displayLocationSettingsRequest(DetailsActivity.this);
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
                            status.startResolutionForResult(DetailsActivity.this, REQUEST_CHECK_SETTINGS);
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setTitle(Common.searchResults.regNo);

        context = this;
        clicked = false;

        displayLocationSettingsRequest(this);

        customerName = findViewById(R.id.name);
        vehModel = findViewById(R.id.vehicleModel);
        loanNo = findViewById(R.id.loanNo);
        engNo = findViewById(R.id.engineNumber);
        chaNo = findViewById(R.id.chassisNumber);
        bucket = findViewById(R.id.bucket);
        prinOS = findViewById(R.id.principalOS);
        emiAmount = findViewById(R.id.emiAmount);
        bounceCharge = findViewById(R.id.bounceChargeDue);
        lppdue = findViewById(R.id.lppDue);
        firstEmi = findViewById(R.id.firstEMIDueDate);
        loanEndDate = findViewById(R.id.loanEndDate);
        branch = findViewById(R.id.branch);
        client = findViewById(R.id.client);
        repostatus = findViewById(R.id.repostatus);

        customerName.setText(Common.searchResults.customerName);
        vehModel.setText(Common.searchResults.vehicleModel);
        loanNo.setText(Common.searchResults.loanNo);
        engNo.setText(Common.searchResults.engineNo);
        chaNo.setText(Common.searchResults.chassisNo);
        bucket.setText(Common.searchResults.bucket);
        prinOS.setText(Common.searchResults.principalOS);
        emiAmount.setText(Common.searchResults.emiAmount);
        bounceCharge.setText(Common.searchResults.bounceChargeDue);
        lppdue.setText(Common.searchResults.lppDue);
        firstEmi.setText(Common.searchResults.firstEmiueDate);
        loanEndDate.setText(Common.searchResults.loanEndDate);
        branch.setText(Common.searchResults.branch);
        client.setText(Common.searchResults.client);

        ll = findViewById(R.id.repolayout);
        ll.setVisibility(View.GONE);
        if (Integer.parseInt(bucket.getText().toString()) < 3)
            bucket.setTextColor(Color.GREEN);
        else if (Integer.parseInt(bucket.getText().toString()) > 2)
            bucket.setTextColor(Color.RED);

        Bitmap fab1bit = BitmapFactory.decodeResource(getResources(), R.drawable.pen);
        Bitmap fab2bit = BitmapFactory.decodeResource(getResources(), R.drawable.rupee);
        Bitmap fab3bit = BitmapFactory.decodeResource(getResources(), R.drawable.dot);
        Bitmap fab4bit = BitmapFactory.decodeResource(getResources(), R.drawable.shine);

        fmenu = findViewById(R.id.menu_red);

        if(Common.searchResults.status.equals("null"))
        {
            Snackbar.make(fmenu,"No Repo Done",Snackbar.LENGTH_LONG).show();
        }else
        {
            repostatus.setText(Common.searchResults.status);
            ll.setVisibility(View.VISIBLE);
            TransitionDrawable transition = (TransitionDrawable) ll.getBackground();
            transition.startTransition(1000);
        }

        com.github.clans.fab.FloatingActionButton fab1 = findViewById(R.id.fab1);
        com.github.clans.fab.FloatingActionButton fab2 = findViewById(R.id.fab2);
        com.github.clans.fab.FloatingActionButton fab3 = findViewById(R.id.fab3);
        com.github.clans.fab.FloatingActionButton fab4 = findViewById(R.id.fab4);

        fab1.setImageBitmap(Common.scaleDown(fab1bit, 64, true));
        fab2.setImageBitmap(Common.scaleDown(fab2bit, 64, true));
        fab3.setImageBitmap(Common.scaleDown(fab3bit, 64, true));
        fab4.setImageBitmap(Common.scaleDown(fab4bit, 64, true));

        fab1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"StaticFieldLeak", "HandlerLeak"})
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getBaseContext())) {
                    if (!clicked) {
                        clicked = true;
                        Intent i = new Intent(DetailsActivity.this, RepoDone.class);
                        startActivity(i);
                        clicked = false;
                    } else {
                        clicked = false;
                    }
                } else {
                    startActivity(new Intent(DetailsActivity.this, NOInternetPlaceHolder.class));
                    clicked = false;
                }

            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"StaticFieldLeak", "HandlerLeak"})
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getBaseContext())) {
                    if (!clicked) {
                        clicked = true;
                        Intent i = new Intent(DetailsActivity.this, CollectionDone.class);
                        startActivity(i);
                        clicked = false;
                    } else {
                        clicked = false;
                    }
                } else {
                    startActivity(new Intent(DetailsActivity.this, NOInternetPlaceHolder.class));
                    clicked = false;
                }


            }
        });


        fab3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"StaticFieldLeak", "HandlerLeak"})
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getBaseContext())) {
                    if (!clicked) {
                        clicked = true;
                        Intent i = new Intent(DetailsActivity.this, NoActionDone.class);
                        startActivity(i);
                        clicked = false;
                    } else {
                        clicked = false;
                    }
                } else {
                    startActivity(new Intent(DetailsActivity.this, NOInternetPlaceHolder.class));
                    clicked = false;
                }

            }
        });

        fab4.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"StaticFieldLeak", "HandlerLeak"})
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getBaseContext())) {
                    if (!clicked) {
                        clicked = true;
                        Intent i = new Intent(DetailsActivity.this, NewDetailsDone.class);
                        startActivity(i);
                        clicked = false;
                    } else {
                        clicked = false;
                    }
                } else {
                    startActivity(new Intent(DetailsActivity.this, NOInternetPlaceHolder.class));
                    clicked = false;
                }

            }
        });

        NestedScrollView nsv = (NestedScrollView) findViewById(R.id.nsv);
        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    fmenu.hideMenu(true);
                } else {
                    fmenu.showMenu(true);
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }
}
