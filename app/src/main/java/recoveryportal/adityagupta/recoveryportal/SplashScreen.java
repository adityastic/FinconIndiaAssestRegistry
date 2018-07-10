package recoveryportal.adityagupta.recoveryportal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.File;

import recoveryportal.adityagupta.recoveryportal.Data.LoginDetails;
import recoveryportal.adityagupta.recoveryportal.Utils.Common;
import recoveryportal.adityagupta.recoveryportal.Utils.ReadJsonFile;

public class SplashScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    TextView textView;

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        textView = findViewById(R.id.textView);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.yourTranslucentColor)));
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Common.trimCache(this);

        if(!sharedPreferences.getString("Username","NULL").equals("NULL"))
        {
            if(Common.isNetworkAvailable(this))
            {
                Common.startDownload(this, String.format(Common.LOGIN_LINK,sharedPreferences.getString("Username",""),sharedPreferences.getString("Password","")), "Login.json", new Handler() {

                    @Override
                    public void handleMessage(Message msg) {
                        if (msg.toString().contains("arg1=1")) {
                            try
                            {
                                JSONObject mainObject = new JSONObject(ReadJsonFile.getJSONObject(getCacheDir()+"/alljsons/Login.json").toString());

                                if(mainObject.has("Full_Name"))
                                {
                                    Common.loginDetails = new LoginDetails(
                                            mainObject.getString("Full_Name"),
                                            mainObject.getString("User_Name"),
                                            mainObject.getString("Address"),
                                            mainObject.getString("Mobile_No"),
                                            mainObject.getString("Email_Id"),
                                            mainObject.getString("ID"),
                                            mainObject.getString("Gender"),
                                            mainObject.getString("Password")
                                    );

                                    Intent i = new Intent(SplashScreen.this,ParentActivity.class);
                                    startActivity(i);
                                }
                            }catch(Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }else
            {
                textView.setVisibility(View.VISIBLE);
               textView.setText("NO INTERNET CONNECTION...\nRestart App When Connected To The Internet");
            }
        }else{
            startActivity(new Intent(SplashScreen.this,LoginActivity.class));
        }
    }
}
