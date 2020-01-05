package recoveryportal.adityagupta.recoveryportal.Placeholders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.jetradar.desertplaceholder.DesertPlaceholder;

import recoveryportal.adityagupta.recoveryportal.R;
import recoveryportal.adityagupta.recoveryportal.Utils.Common;

public class NOInternetPlaceHolder extends AppCompatActivity {

    boolean back = false;

    @Override
    public void onBackPressed() {
        if (back) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nointernet_place_holder);


        final DesertPlaceholder desertPlaceholder = findViewById(R.id.desert_placeholder);

        if (getIntent().hasExtra("mess")) {
            if (getIntent().getExtras().getString("mess").trim().toLowerCase().contains("update app")) {
                desertPlaceholder.setButtonText("Download New Version");
            }
            desertPlaceholder.setMessage(getIntent().getExtras().getString("mess"));
        }
        desertPlaceholder.setOnButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!getIntent().hasExtra("mess")) {
                    if (Common.isNetworkAvailable(NOInternetPlaceHolder.this)) {
                        back = true;
                        onBackPressed();
                    }
                } else {
                    if (getIntent().getExtras().getString("mess").trim().toLowerCase().contains("update app")) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse("http://finconindia.com/Documents/FinconAndroidApp.apk"));
                        startActivity(i);
                    }
                }
            }
        });
    }

}
