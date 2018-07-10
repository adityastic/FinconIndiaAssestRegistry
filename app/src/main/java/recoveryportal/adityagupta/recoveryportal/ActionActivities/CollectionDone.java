package recoveryportal.adityagupta.recoveryportal.ActionActivities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

import recoveryportal.adityagupta.recoveryportal.Placeholders.NOInternetPlaceHolder;
import recoveryportal.adityagupta.recoveryportal.ParentActivity;
import recoveryportal.adityagupta.recoveryportal.R;
import recoveryportal.adityagupta.recoveryportal.Utils.Common;
import recoveryportal.adityagupta.recoveryportal.Utils.ReadJsonFile;

public class CollectionDone extends AppCompatActivity {
    AutoCompleteTextView et1, et2, et3, et4, rec;
    boolean clicked = false;
    TextView total, actual;
    int tt;


    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    ArrayAdapter<String> adapter1, adapter2, adapter3;

    int getNumber(EditText s) {
        if (s.getText().toString().length() > 0)
            return Integer.parseInt(s.getText().toString());
        else
            return 0;
    }

    void setColor() {
        if (Integer.parseInt(actual.getText().toString().substring(2)) >= tt)
            actual.setTextColor(Color.GREEN);
        else if (Integer.parseInt(actual.getText().toString().substring(2)) < tt)
            actual.setTextColor(Color.RED);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_done);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.yourTranslucentColor)));
        setTitle("Collection");

        et1 = findViewById(R.id.emi);
        et2 = findViewById(R.id.bcc);
        et3 = findViewById(R.id.lpp);
        et4 = findViewById(R.id.other);
        rec = findViewById(R.id.recpnumber);

        total = findViewById(R.id.totalAmount);
        actual = findViewById(R.id.received);

        tt = (Integer.parseInt(Common.searchResults.emiAmount) * (Integer.parseInt(Common.searchResults.bucket) + 1)) +
                Integer.parseInt(Common.searchResults.bounceChargeDue) +
                Integer.parseInt(Common.searchResults.lppDue);
        total.setText("₹ " + String.valueOf(tt));

        actual.setText("₹ " + "0");

        adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, new String[]{(Integer.parseInt(Common.searchResults.emiAmount) * (Integer.parseInt(Common.searchResults.bucket) + 1)) + ""});
        et1.setAdapter(adapter1);
        et1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                et1.showDropDown();
            }
        });

        adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, new String[]{Common.searchResults.bounceChargeDue});
        et2.setAdapter(adapter2);
        et2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                et2.showDropDown();
            }
        });

        adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, new String[]{Common.searchResults.lppDue});
        et3.setAdapter(adapter3);
        et3.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                et3.showDropDown();
            }
        });


        et1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int sum = getNumber(et1) + getNumber(et2) + getNumber(et3) + getNumber(et4);
                actual.setText("₹ " + String.valueOf(sum));
                setColor();
            }
        });


        et2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int sum = getNumber(et1) + getNumber(et2) + getNumber(et3) + getNumber(et4);
                actual.setText("₹ " + String.valueOf(sum));
                setColor();
            }
        });


        et3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int sum = getNumber(et1) + getNumber(et2) + getNumber(et3) + getNumber(et4);
                actual.setText("₹ " + String.valueOf(sum));
                setColor();
            }
        });


        et4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int sum = getNumber(et1) + getNumber(et2) + getNumber(et3) + getNumber(et4);
                actual.setText("₹ " + String.valueOf(sum));
                setColor();
            }
        });


        Button button = findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View v) {
                if (Common.isNetworkAvailable(getBaseContext())) {
                    if (rec.getText().toString().length() > 0) {
                        if (!clicked) {
                            clicked = true;

                            String request = null;
                            try {
                                request = String.format(Common.COLLECTION_DONE,
                                        actual.getText().toString().substring(2),
                                        Common.getHTMLString(rec.getText().toString().trim().toUpperCase()),
                                        (et1.getText().toString().length() > 0) ? Common.getHTMLString(et1.getText().toString().trim()) : "0",
                                        (et2.getText().toString().length() > 0) ? Common.getHTMLString(et2.getText().toString().trim()) : "0",
                                        (et3.getText().toString().length() > 0) ? Common.getHTMLString(et3.getText().toString().trim()) : "0",
                                        (et4.getText().toString().length() > 0) ? Common.getHTMLString(et4.getText().toString().trim()) : "0",
                                        Common.loginDetails.ID,
                                        Common.searchResults.id,
                                        Common.getHTMLString(new SimpleDateFormat("hh").format(Common.searchResults.time) +
                                                URLEncoder.encode(":", "UTF8") +
                                                new SimpleDateFormat("mm").format(Common.searchResults.time) +
                                                URLEncoder.encode(":", "UTF8") +
                                                new SimpleDateFormat("ss").format(Common.searchResults.time) +
                                                " " +
                                                new SimpleDateFormat("aa").format(Common.searchResults.time))
                                );
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            Common.startDownload(getBaseContext(),
                                    request,

                                    "CollectionDone.json", new Handler() {

                                        @Override
                                        public void handleMessage(Message msg) {
                                            if (msg.toString().contains("arg1=1")) {
                                                try {
                                                    JSONObject mainObject = new JSONObject(ReadJsonFile.getJSONObject(getCacheDir() + "/alljsons/CollectionDone.json").toString());
                                                    Log.e("Collection", mainObject.toString());

                                                    if (mainObject.getString("code").equals("Success")) {
                                                        Intent i = new Intent(CollectionDone.this, ParentActivity.class);
                                                        i.putExtra("done", true);
                                                        startActivity(i);
                                                        Toast.makeText(CollectionDone.this, "Updated Collection Successfully", Toast.LENGTH_LONG).show();

                                                        finishAffinity();
                                                    } else {
                                                        startActivity(new Intent(CollectionDone.this, NOInternetPlaceHolder.class));
                                                        clicked = false;
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    });
                        }
                    } else {
                        Toast.makeText(CollectionDone.this, "Enter Receipt Number to Proceed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    startActivity(new Intent(CollectionDone.this, NOInternetPlaceHolder.class));
                }

            }
        });
    }
}
