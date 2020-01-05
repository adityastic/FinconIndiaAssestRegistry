package recoveryportal.adityagupta.recoveryportal.Utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

/**
 * Created by Aditya on 18/12/16.
 */
public class ReadJsonFile {
    static Context context;

    public ReadJsonFile(Context cont) {
        context = cont;
    }

    public static JSONObject getJSONObject(String arg) {

        try {
            File mainJson = new File(arg);
            FileInputStream stream = new FileInputStream(mainJson);
            String jsonStr = null;
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                jsonStr = Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }

            JSONObject response = new JSONObject(jsonStr);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static JSONArray getJSONArray(String arg) {

        try {
            File mainJson = new File(arg);
            FileInputStream stream = new FileInputStream(mainJson);
            String jsonStr = null;
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                jsonStr = Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }

            JSONArray response = new JSONArray(jsonStr);

            return response;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
