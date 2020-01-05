package recoveryportal.adityagupta.recoveryportal.Utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import recoveryportal.adityagupta.recoveryportal.BuildConfig;
import recoveryportal.adityagupta.recoveryportal.Data.LoginDetails;
import recoveryportal.adityagupta.recoveryportal.Data.SearchHistoryData;
import recoveryportal.adityagupta.recoveryportal.Data.SearchResults;
import recoveryportal.adityagupta.recoveryportal.Placeholders.NOInternetPlaceHolder;
import recoveryportal.adityagupta.recoveryportal.R;
import recoveryportal.adityagupta.recoveryportal.Services.Downloader;

public class Common {

    public static final String APP_CHECK = "https://raw.githubusercontent.com/adityastic/ASDFGHJKL/master/FinconIndia.json";
    public static final String IP = "http://finconindia.com";
    public static final String LOGIN_LINK = IP + "/AndroidLogin?username=%s&password=%s";
    public static final String SEARCH_LINK = IP + "/AndroidSearch?regNo=%s&username=%s&password=%s";
    public static final String REPO_DONE = IP + "/AndroidRepoDone?action=%s&policestation=%s&location=%s&exeid=%s&excelid=%s&time=%s";
    public static final String COLLECTION_DONE = IP + "/AndroidCollectionDone?amtaken=%s&recno=%s&emi=%s&bcc=%s&lpp=%s&other=%s&exeid=%s&excelid=%s&time=%s";
    public static final String NO_ACTION_DONE = IP + "/AndroidNoActionDone?action=%s&exeid=%s&excelid=%s&time=%s";
    public static final String NEW_DETAIL_DONE = IP + "/AndroidNewDetailsDone";
    public static final String HISTORY_LINK = IP + "/AndroidSearchHistory?id=%s";

    public static SearchResults searchResults;
    public static LoginDetails loginDetails;
    public static List<SearchHistoryData> list;
    public static double version = BuildConfig.VERSION_CODE;
    public static boolean ff;

    @SuppressLint("HandlerLeak")
    public static void generateHistory(final Context context) {
        list = new ArrayList<>();
        if (isNetworkAvailable(context)) {
            startDownload(context, String.format(Common.HISTORY_LINK, Common.loginDetails.ID), "History.json", new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    if (msg.toString().contains("arg1=1")) {
                        try {
                            JSONArray mainObject = new JSONArray(ReadJsonFile.getJSONArray(context.getCacheDir() + "/alljsons/History.json").toString());

                            for (int i = 0; i < mainObject.length(); i++) {
                                JSONObject json = mainObject.getJSONObject(i);
                                list.add(new SearchHistoryData(json.getString("key"), json.getString("date")));
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            Intent i = new Intent(context, NOInternetPlaceHolder.class);
            context.startActivity(i);
        }
    }

    @SuppressLint("HandlerLeak")
    public static void checkApp(final Context context) {
        if (isNetworkAvailable(context)) {
            startDownload(context, APP_CHECK, "CheckApp.json", new Handler() {

                @Override
                public void handleMessage(Message msg) {
                    if (msg.toString().contains("arg1=1")) {
                        try {
                            JSONObject mainObject = new JSONObject(ReadJsonFile.getJSONObject(context.getCacheDir() + "/alljsons/CheckApp.json").toString());
                            Log.e("JSCHECK", ReadJsonFile.getJSONObject(context.getCacheDir() + "/alljsons/CheckApp.json").toString());
                            if (mainObject.has("work"))
                                ff = mainObject.getBoolean("work");
                            else
                                ff = false;

                            if (!ff) {
                                Intent i = new Intent(context, NOInternetPlaceHolder.class);
                                i.putExtra("mess", "Not Authorized");
                                context.startActivity(i);
                            }

                            if (mainObject.has("finconindia")) {
                                double newVersion = mainObject.getDouble("finconindia");
                                if (newVersion > version) {
                                    createNotification(newVersion, context);
                                    Intent i = new Intent(context, NOInternetPlaceHolder.class);
                                    i.putExtra("mess", "Please update App, Make sure you uninstall the current application and reinstall the new Application");
                                    context.startActivity(i);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            Intent i = new Intent(context, NOInternetPlaceHolder.class);
            context.startActivity(i);
        }
    }

    private static void createNotification(double newVersion, Context context) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse("http://finconindia.com/Documents/FinconAndroidApp.apk"));

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        i,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        NotificationManager mNotifyManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new
                NotificationCompat.Builder(context, "default");

        mBuilder.setContentTitle("Immediate Update");
        mBuilder.setContentText("You need to update your app to v" + newVersion + " Immediately !!");
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(resultPendingIntent);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);

        mBuilder.setPriority(Notification.PRIORITY_DEFAULT);
        mNotifyManager.notify(1, mBuilder.build());

    }


    public static String getHTMLString(String s) {
        return s.replace(" ", "%20");
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void startDownload(Context context, String link, String name, Handler handler) {

        File folders = new File(context.getCacheDir(), "/alljsons/");
        if (!folders.exists()) {
            folders.mkdirs();
        }

        Intent intent = new Intent(context.getApplicationContext(), Downloader.class);
        intent.putExtra("link", link);
        intent.putExtra("filename", name);
        intent.putExtra(Downloader.EXTRA_MESSENGER, new Messenger(handler));
        context.startService(intent);
    }

    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                maxImageSize / realImage.getWidth(),
                maxImageSize / realImage.getHeight());
        int width = Math.round(ratio * realImage.getWidth());
        int height = Math.round(ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }


    public static void trimCache(Context context) {
        try {
            File dir = context.getCacheDir();
            if (dir != null && dir.isDirectory()) {
                deleteDir(dir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }
}
