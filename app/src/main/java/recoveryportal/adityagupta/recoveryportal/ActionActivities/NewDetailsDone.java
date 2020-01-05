package recoveryportal.adityagupta.recoveryportal.ActionActivities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;

import recoveryportal.adityagupta.recoveryportal.ParentActivity;
import recoveryportal.adityagupta.recoveryportal.Placeholders.NOInternetPlaceHolder;
import recoveryportal.adityagupta.recoveryportal.R;
import recoveryportal.adityagupta.recoveryportal.Utils.Common;
import recoveryportal.adityagupta.recoveryportal.Utils.ImageUtils;
import recoveryportal.adityagupta.recoveryportal.Utils.ReadJsonFile;

public class NewDetailsDone extends AppCompatActivity implements ImageUtils.ImageAttachmentListener {

    static final int BUFFER_SIZE = 4096;
    ImageUtils imageutils;

    EditText et1;
    Activity activity;
    boolean clicked = false;
    Button button;
    private String file_name = "";

    @Override
    public void onBackPressed() {
        this.finish();
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageutils.onActivityResult(requestCode, resultCode, data);
        TransitionDrawable transition = (TransitionDrawable) button.getBackground();
        transition.startTransition(1500);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        imageutils.request_permission_result(requestCode, permissions, grantResults);
    }

    @Override
    public void image_attachment(int from, String filename, Bitmap file, Uri uri) {
        this.file_name = filename;

        String path = Environment.getExternalStorageDirectory() + File.separator + "RecoveryPortal" + File.separator;
        imageutils.createImage(file, filename, path, false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_details_done);

        activity = this;

        et1 = findViewById(R.id.number);
        imageutils = new ImageUtils(this);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.yourTranslucentColor)));
        setTitle("New Details");

        et1.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
            }
        });

        button = findViewById(R.id.addressproof);
        Button submit = findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View v) {
                imageutils.imagepicker(1);
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("HandlerLeak")
            @Override
            public void onClick(View v) {
                if (et1.getText().length() > 9) {
                    if (file_name != "") {
                        if (Common.isNetworkAvailable(getBaseContext())) {
                            if (!clicked) {
                                clicked = true;
                                new sendFile().execute(Common.NEW_DETAIL_DONE);
                                try {
                                    Common.startDownload(getBaseContext(),
                                            String.format(Common.NEW_DETAIL_DONE + "?number=%s&file=%s&exeid=%s&excelid=%s&time=%s",
                                                    Common.getHTMLString(et1.getText().toString().trim()),
                                                    file_name,
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

                                            "NewDetails.json", new Handler() {

                                                @Override
                                                public void handleMessage(Message msg) {
                                                    if (msg.toString().contains("arg1=1")) {
                                                        try {
                                                            JSONObject mainObject = new JSONObject(ReadJsonFile.getJSONObject(getCacheDir() + "/alljsons/NewDetails.json").toString());
                                                            Log.e("NewDetails", mainObject.toString());

                                                            if (mainObject.getString("code").equals("Success")) {
                                                                Intent i = new Intent(NewDetailsDone.this, ParentActivity.class);
                                                                i.putExtra("done", true);
                                                                startActivity(i);
                                                                Toast.makeText(NewDetailsDone.this, "Updated Details Successfully", Toast.LENGTH_LONG).show();

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
                            startActivity(new Intent(NewDetailsDone.this, NOInternetPlaceHolder.class));
                        }

                    } else
                        Toast.makeText(NewDetailsDone.this, "Please Choose a Image", Toast.LENGTH_SHORT).show();
                } else {
                    et1.setError("Too Short Mobile Number");
                }
            }
        });
    }

    private class sendFile extends AsyncTask<String, Void, String> {

        private ProgressDialog dialog;

        @Override
        protected String doInBackground(String... urls) {
            HttpURLConnection httpConn = null;

            try {
                String file = Environment.getExternalStorageDirectory() + File.separator + "RecoveryPortal" + File.separator + file_name;
                File uploadFile = new File(file);
                FileInputStream inputStream = new FileInputStream(uploadFile);

                // creates a HTTP connection
                URL url1 = new URL(urls[0]);
                httpConn = (HttpURLConnection) url1.openConnection();
                httpConn.setUseCaches(false);
                httpConn.setDoOutput(true);
                httpConn.setRequestMethod("POST");
                httpConn.setRequestProperty("fileName", uploadFile.getName());
                httpConn.connect();
                // sets file name as a HTTP header

                // opens output stream of the HTTP connection for writing data

                OutputStream outputStream = httpConn.getOutputStream();

                // Opens input stream of the file for reading data


                byte[] buffer = new byte[BUFFER_SIZE];
                int bytesRead = -1;


                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                outputStream.close();
                inputStream.close();
            } catch (SocketTimeoutException e) {
                Log.e("Debug", "error: " + e.getMessage(), e);
            } catch (MalformedURLException ex) {
                Log.e("Debug", "error: " + ex.getMessage(), ex);
            } catch (IOException ioe) {
                Log.e("Debug", "error: " + ioe.getMessage(), ioe);
            }

            try {

                // always check HTTP response code from server
                int responseCode = httpConn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // reads server's response
                    BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                    String response = reader.readLine();

                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    Log.e("Server's response: ", response);
                } else {
                    Log.e("Server non-OK code: ", "" + responseCode);
                }
            } catch (IOException ioex) {
                Log.e("Debug", "error: " + ioex.getMessage(), ioex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
        }

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(activity);
            dialog.setMessage("Uploading...");
            dialog.show();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
