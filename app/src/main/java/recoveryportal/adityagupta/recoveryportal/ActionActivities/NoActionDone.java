package recoveryportal.adityagupta.recoveryportal.ActionActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import recoveryportal.adityagupta.recoveryportal.Utils.ReadJsonFile;

public class NoActionDone extends AppCompatActivity {
    NiceSpinner niceSpinner;

    boolean clicked = false;

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_action_done);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.yourTranslucentColor)));
        setTitle("No Action");


        niceSpinner = (NiceSpinner) findViewById(R.id.nice_spinner);
        List<String> dataset = new LinkedList<>(Arrays.asList("Critical", "Client Disappear", "Skip"));
        niceSpinner.attachDataSource(dataset);

        Button button = findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getBaseContext())) {
                    if (!clicked) {
                        clicked = true;
                        try{

                        Common.startDownload(getBaseContext(),
                                String.format(Common.NO_ACTION_DONE,
                                        Common.getHTMLString(niceSpinner.getText().toString()),
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

                                "NoActionDone.json", new Handler() {

                                    @Override
                                    public void handleMessage(Message msg) {
                                        if (msg.toString().contains("arg1=1")) {
                                            try {
                                                JSONObject mainObject = new JSONObject(ReadJsonFile.getJSONObject(getCacheDir() + "/alljsons/NoActionDone.json").toString());

                                                if (mainObject.getString("code").equals("Success")) {
                                                    Intent i = new Intent(NoActionDone.this,ParentActivity.class);
                                                    i.putExtra("done",true);
                                                    startActivity(i);
                                                    Toast.makeText(NoActionDone.this, "Updated Action Successfully", Toast.LENGTH_LONG).show();

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
                    startActivity(new Intent(NoActionDone.this, NOInternetPlaceHolder.class));
                }

            }
        });
    }

}
