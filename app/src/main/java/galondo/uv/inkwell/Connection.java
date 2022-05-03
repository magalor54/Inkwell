package galondo.uv.inkwell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class HTTPConnector extends AsyncTask<String, Void, String> {

    public HTTPConnector(String url) {
        this.url = url;
    }
    String url;

    @Override
    protected String doInBackground(String... params) {

        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        String finalURL = new String();
        try {
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");
            //add request header
            con.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
            con.setRequestProperty("accept", "application/json;");
            con.setRequestProperty("accept-language", "es");
            con.connect();
            int responseCode = con.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP error code: " + responseCode);
            }
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF8"));

            int n;
            while ((n = in.read(buffer)) != -1) {
                writer.write(buffer, 0, n);

            }
            in.close();
            try {
                JSONObject object = new JSONObject(writer.toString());
                JSONArray itemsArray = object.getJSONArray("items");
                JSONObject itemsObj = itemsArray.getJSONObject(0);
                JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                String thumbnail = imageLinks.optString("thumbnail");
                finalURL = thumbnail;

            } catch (JSONException e) {
                e.printStackTrace();
            } // end try - catch


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return finalURL;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}


