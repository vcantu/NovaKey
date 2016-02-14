package viviano.cantu.novakey.themes;


import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
     * @param res resources to load from
     */
    public static void load(Resources res) {
        themes = new ArrayList<>();

        InputStream is = res.openRawResource(R.raw.app_colors);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] params = line.split(",");
                themes.add(new AppTheme(params));
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e) {
                throw new RuntimeException("Error while closing input stream: " + e);
            }
        }

        AppColorTask at = new AppColorTask();
        at.execute(res.getString(R.string.app_color_url));
    }

    private static class AppColorTask extends AsyncTask<String, Integer, String> {

        String data = null;

        /**
         * Override this method to perform a computation on a background thread. The
         * specified parameters are the parameters passed to {@link #execute}
         * by the caller of this task.
         * <p>
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected String doInBackground(String... params) {
            try {
                data = downloadUrl(params[0]);
                System.out.println(data);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        /**
         * <p>Runs on the UI thread after {@link #doInBackground}. The
         * specified result is the value returned by {@link #doInBackground}.</p>
         * <p>
         * <p>This method won't be invoked if the task was cancelled.</p>
         *
         * @param s The result of the operation computed by {@link #doInBackground}.
         * @see #onPreExecute
         * @see #doInBackground
         * @see #onCancelled(Object)
         */
        @Override
        protected void onPostExecute(String s) {
            try {
                loadFromJSON(new JSONArray(s));
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            Log.d("Exception: ", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private static void loadFromJSON(JSONArray arr) {
       try {
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
           Log.e("Exception", e.toString());
       }
    }

}
