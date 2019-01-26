package projects.pratham.chatbot;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class Async extends AsyncTask<Void, Void, Void> {

    String LINE = "";
    String data = "";
    String RESOLVED_QUERY = "";
    String DATABACK = "";
    String bearer = "2623dadb11c848b39ff075de53f709e6";
    Context c;

    public Async(Context c){
        this.c = c;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        try {
            MainActivity.recognitionProgressView.animate ().alphaBy (0).setDuration (200);
            data=null;
            URL url = new URL ("https://api.dialogflow.com/v1/query?v=20150910&lang=en&query=" + MainActivity.QUERY + "&sessionId=" + MainActivity.SESSION_ID);
            URLConnection httpURLConnection = url.openConnection ();
            httpURLConnection.setRequestProperty ("Authorization", "Bearer "+bearer);
            InputStream inputStream = httpURLConnection.getInputStream ();
            BufferedReader bufferedReader = new BufferedReader (new InputStreamReader (inputStream));
            while (LINE != null){
                LINE = bufferedReader.readLine ();
                data += LINE;
            }
        } catch (MalformedURLException e) {
            Toast.makeText(c, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(c, "IOException: "+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        String a = data.substring (4, data.length () - 4);
        data = a;
        try {
            JSONObject datas = new JSONObject (data);
            JSONObject result = new JSONObject (datas.getString ("result"));
            DATABACK = result.getString ("resolvedQuery");
            JSONObject fulfillment = new JSONObject (result.getString ("fulfillment"));
            RESOLVED_QUERY = fulfillment.getString ("speech");
        }catch (JSONException e){
            Toast.makeText(c, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
        }
        String hello = "Hello";
        if(RESOLVED_QUERY.toLowerCase ().indexOf (hello.toLowerCase()) != -1){
            String name = RESOLVED_QUERY.substring (RESOLVED_QUERY.lastIndexOf (" ") + 1);
            MainActivity.NAME = name;
        }else {

        }
        String essa = DATABACK.substring (0, 1).toUpperCase () + DATABACK.substring (1);
        MainActivity.DATA = RESOLVED_QUERY;
        MainActivity.data.animateText (MainActivity.DATA);
        MainActivity.resolved.animateText(essa);
        super.onPostExecute (aVoid);
    }
}