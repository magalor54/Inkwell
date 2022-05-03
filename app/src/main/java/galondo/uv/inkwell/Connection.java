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

public class Connection {

    String url;

    String Type;

    ArrayList<Libro> libros = new ArrayList();
    ArrayList<String> covers = new ArrayList();

    int cont = 0;


    private void Connector(){
        HTTPConnector httpConnector = new HTTPConnector(url, true);
        httpConnector.execute();
    }

    private void getCovers(){
        for (int j = 0; j < libros.size(); j++) {

            url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + libros.get(j).getISBN();
            //Connector();

            HTTPConnector httpConnector2 = new HTTPConnector(url, false);
            httpConnector2.execute();
        }
    }

    /*
    private void RecycleView(String id){

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        adapter = new HorizontalAdapter(this, libros);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter.setOnItemClickListener(onItemClickListener);

    }

     */




    class HTTPConnector extends AsyncTask<String, Void, ArrayList> {

        public HTTPConnector(String url, Boolean type) {
            this.url = url;
            this.type = type;
        }

        String url;
        Boolean type;

        @Override
        protected ArrayList doInBackground(String... params) {

            Writer writer = new StringWriter();
            char[] buffer = new char[1024];
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

                if(type){
                    try {
                        JSONObject object = new JSONObject(writer.toString());
                        JSONArray result = object.getJSONArray("results");
                        //JSONArray records = result.getJSONArray("records");

                                for (int i = 0; i < result.length(); i++) {
                                    try {
                                        JSONObject rec = result.getJSONObject(i);
                                        JSONArray bookDetalis = rec.getJSONArray("book_details");

                                        if(bookDetalis.getJSONObject(0).getString("primary_isbn10").compareTo("None") != 0) {
                                            Log.d(null, "TITULO: " + bookDetalis.getJSONObject(0).getString("title"));
                                            Log.d(null, "AUTOR: " + bookDetalis.getJSONObject(0).getString("author"));
                                            Log.d(null, "ISBN: " + bookDetalis.getJSONObject(0).getString("primary_isbn10"));
                                            Log.d(null, "GENERO: " + rec.getString("display_name"));
                                            Log.d(null, "DESCRIPCION: " + bookDetalis.getJSONObject(0).getString("description"));
                                            Log.d(null, "-------------------------------------------");
                                            libros.add(new Libro(bookDetalis.getJSONObject(0).getString("title"), bookDetalis.getJSONObject(0).getString("author"), bookDetalis.getJSONObject(0).getString("primary_isbn10"), rec.getString("display_name"), bookDetalis.getJSONObject(0).getString("description")));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject object = new JSONObject(writer.toString());
                        JSONArray itemsArray = object.getJSONArray("items");
                        JSONObject itemsObj = itemsArray.getJSONObject(0);
                        JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                        JSONObject imageLinks = volumeObj.optJSONObject("imageLinks");
                        String thumbnail = imageLinks.optString("thumbnail");
                        covers.add(thumbnail);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } // end try - catch

                } // end for
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(type)
                return libros;
            else
                return covers;
        }

        @Override
        protected void onPostExecute(ArrayList result) {
            super.onPostExecute(result);
            cont++;

            type = false;
            if(cont == 1)
                getCovers();
            if(covers.size() == libros.size()){
                for (int i = 0; i < covers.size(); i++)
                    libros.get(i).setImage_drawable(covers.get(i));
            }

        }
    }
};


