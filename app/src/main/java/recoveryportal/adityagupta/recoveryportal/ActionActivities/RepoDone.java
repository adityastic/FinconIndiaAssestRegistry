package recoveryportal.adityagupta.recoveryportal.ActionActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.angmarch.views.NiceSpinner;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import recoveryportal.adityagupta.recoveryportal.Placeholders.NOInternetPlaceHolder;
import recoveryportal.adityagupta.recoveryportal.ParentActivity;
import recoveryportal.adityagupta.recoveryportal.R;
import recoveryportal.adityagupta.recoveryportal.Utils.Common;
import recoveryportal.adityagupta.recoveryportal.Utils.Locator;
import recoveryportal.adityagupta.recoveryportal.Utils.ReadJsonFile;

public class RepoDone extends AppCompatActivity {
    NiceSpinner niceSpinner;
    EditText policeStation, area;

    String loc = "";
    boolean clicked = false;


//    public void getAddress(double lat, double lng) {
//        Geocoder geocoder = new Geocoder(RepoDone.this, Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
//            Address obj = addresses.get(0);
//            String add = obj.getAddressLine(0);
//            add = add + "\n" + obj.getCountryName();
//            add = add + "\n" + obj.getCountryCode();
//            add = add + "\n" + obj.getAdminArea();
//            add = add + "\n" + obj.getPostalCode();
//            add = add + "\n" + obj.getSubAdminArea();
//            add = add + "\n" + obj.getLocality();
//            add = add + "\n" + obj.getSubThoroughfare();
//
//            Log.e("IGA", "Address :- \n" + add);
//            // Toast.makeText(this, "Address=>" + add,
//            // Toast.LENGTH_SHORT).show();
//
//            // TennisAppActivity.showDialog(add);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repo_done);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.yourTranslucentColor)));
        setTitle("Repo");

        policeStation = findViewById(R.id.policestation);
        area = findViewById(R.id.area);


        Locator locator = new Locator(RepoDone.this);
        locator.getLocation(Locator.Method.NETWORK_THEN_GPS, new Locator.Listener() {
            @Override
            public void onLocationFound(Location location) {
                 loc = location.getLatitude() + " " + location.getLongitude();
                //getAddress(location.getLatitude(),location.getLongitude());
            }

            @Override
            public void onLocationNotFound() {
                loc = "NULL";
                }
        });

        niceSpinner = (NiceSpinner) findViewById(R.id.nice_spinner);
        List<String> dataset = new LinkedList<>(Arrays.asList("Third Party", "Customer"));
        niceSpinner.attachDataSource(dataset);


        Button button = findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getBaseContext())) {
                    if (!clicked) {
                        clicked = true;

                        try {
                            Common.startDownload(getBaseContext(),
                                    String.format(Common.REPO_DONE,
                                            Common.getHTMLString(niceSpinner.getText().toString().trim()),
                                            Common.getHTMLString(policeStation.getText().toString().trim()),
                                            Common.getHTMLString(area.getText().toString().trim() + " " + URLEncoder.encode("(", "UTF8") + " " + loc + " " + URLEncoder.encode(")", "UTF8") + " "),
                                            Common.loginDetails.ID,
                                            Common.searchResults.id,
                                            Common.getHTMLString(new SimpleDateFormat("hh").format(Common.searchResults.time) +
                                                    URLEncoder.encode(":", "UTF8") +
                                                    new SimpleDateFormat("mm").format(Common.searchResults.time) +
                                                    URLEncoder.encode(":", "UTF8") +
                                                    new SimpleDateFormat("ss").format(Common.searchResults.time) +
                                                    " " +
                                                    new SimpleDateFormat("aa").format(Common.searchResults.time))
                                    ),

                                    "RepoDone.json", new Handler() {

                                        @Override
                                        public void handleMessage(Message msg) {
                                            if (msg.toString().contains("arg1=1")) {
                                                try {
                                                    JSONObject mainObject = new JSONObject(ReadJsonFile.getJSONObject(getCacheDir() + "/alljsons/RepoDone.json").toString());
                                                    Log.e("Repo",mainObject.toString());

                                                    if (mainObject.getString("code").equals("Success")) {
                                                        Intent i = new Intent(RepoDone.this, ParentActivity.class);
                                                        i.putExtra("done", true);
                                                        startActivity(i);

                                                        Toast.makeText(RepoDone.this, "Updated Repo Successfully", Toast.LENGTH_LONG).show();

                                                        finishAffinity();
                                                    } else {
                                                        clicked = false;
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    });
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    startActivity(new Intent(RepoDone.this, NOInternetPlaceHolder.class));
                }

            }
        });
    }
}
