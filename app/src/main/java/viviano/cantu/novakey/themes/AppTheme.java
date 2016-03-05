package viviano.cantu.novakey.themes;


import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import viviano.cantu.novakey.R;
import viviano.cantu.novakey.utils.Util;

/**
 * Created by Viviano on 8/18/2015.
 */
public class AppTheme {

    public final String name, pk;
    public final int color1, color2, color3;

    public AppTheme(String ... params) {
        name = params[0];
        pk = params[1];
        color1 = Util.webToColor(params[2]);
        color2 = Util.webToColor(params[3]);
        color3 = Util.webToColor(params[4]);
    }

    private static ArrayList<AppTheme> themes;
    private static final String filename = "app_colors.json";

    /**
     * Function that creates a theme from the given package name
     * @param pk package name
     * @return A BaseTheme colored according to the app
     */
    public static AppTheme fromPk(String pk) {
        for (AppTheme t : themes) {
            if (t.pk.equals(pk))
                return t;
        }
        return null;
    }

    /**
     * Will load from csv file in raw
     *
     * @param context context
     * @param res resources to load from
     */
    public static void load(Context context, Resources res) {
        themes = new ArrayList<>();

        try {
            Log.d("App Colors", "Loading saved colors...");
            FileInputStream fis = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            loadFromJSON(sb.toString());
        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundExceptio ", "app_colors.json does not exist");
        } catch (IOException e) {
            Log.e("IOException", "app_colors.json is empty");
        }

        //if file not found download from web
        AppColorTask at = new AppColorTask(context);
        Log.d("App Colors", "Fetching colors from Network...");
        at.execute(res.getString(R.string.app_color_url));
    }

    private static class AppColorTask extends AsyncTask<String, Integer, String> {

        private String data = null;
        private final Context context;

        AppColorTask(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                data = downloadUrl(params[0]);
                Log.d("App Colors", "Data received from network: " + data);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String str) {
            try {
                loadFromJSON(str);
                SaveColorsTask st = new SaveColorsTask(
                        context.openFileOutput(filename, Context.MODE_PRIVATE));
                st.execute(str);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private static class SaveColorsTask extends AsyncTask<String, Integer, String> {

        private String data = null;
        private final FileOutputStream outputStream;


        SaveColorsTask(FileOutputStream outputStream) {
            this.outputStream = outputStream;
        }

        @Override
        protected String doInBackground(String... params) {
            data = params[0];
            try {
                OutputStreamWriter osw = new OutputStreamWriter(outputStream);
                osw.write(data);
                osw.flush();
                osw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }
    }

    private static String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }
            data = sb.toString();
            br.close();

        }catch(Exception e){
            Log.e("Exception ", e.toString());
        }finally{
            if (iStream == null)
                Log.d("App Colors", "iStream is null");
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private static void loadFromJSON(String str) {
        try {
           JSONArray arr = new JSONArray(str);
           ArrayList<AppTheme> temp = new ArrayList<>();
           for (int i = 0; i < arr.length(); i++) {
               JSONObject curr = arr.getJSONObject(i);
               temp.add(
               new AppTheme(
                       curr.get("App Name").toString(),
                       curr.get("App Package").toString(),
                       curr.get("Primary").toString(),
                       curr.get("Accent").toString(),
                       curr.get("Contrast").toString()
               ));
           }
           themes = temp;
       } catch (JSONException e) {
           Log.e("JSONException", e.toString());
       } catch (NullPointerException e) {
            Log.e("NullPointerException", e.toString());
        }
    }

}
