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
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HorizontalAdapter adapter;
    ArrayList<Libro> libros = new ArrayList();
    ArrayList<String> covers = new ArrayList();
    String url;
    int cont = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        url = "https://api.nytimes.com/svc/books/v3/lists.json?list-name=hardcover-fiction&api-key=ZPckoDI4RV9SBHcj282KHIbQ8i8Cdjoq";
        Connector();

        /*
        url = "https://api.nytimes.com/svc/books/v3/lists.json?list-name=e-book-fiction&api-key=ZPckoDI4RV9SBHcj282KHIbQ8i8Cdjoq";
        Connector();

         */
    }

    private void Connector(){
        HTTPConnector httpConnector = new HTTPConnector(url, true);
        httpConnector.execute();
    }

    private void RecycleView(){

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        adapter = new HorizontalAdapter(this, libros);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        adapter.setOnItemClickListener(onItemClickListener);

    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ArrayList info = new ArrayList();

            // This viewHolder will have all required values.
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();

            //Log.d(null, "Este es mi print---->"+adapter.getInfoPosition(viewHolder.getAdapterPosition()).toString());

            //info = adapter.getInfoPosition(viewHolder.getAdapterPosition());
            // Implement the listener!

            Intent intent = new Intent(getApplicationContext(), Libro.class);

            /*
            intent.putExtra("titulo", String.valueOf(info.get(0)));
            intent.putExtra("imagen", String.valueOf(info.get(1)));
            intent.putExtra("autor", String.valueOf(info.get(2)));
            intent.putExtra("genero", String.valueOf(info.get(3)));
            intent.putExtra("paginas", String.valueOf(info.get(4)));
            intent.putExtra("fecha", String.valueOf(info.get(5)));
             */
            startActivity(intent);
        }
    };


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

                try {
                    JSONObject object = new JSONObject(writer.toString());
                    JSONArray result = object.getJSONArray("results");
                    //JSONArray records = result.getJSONArray("records");

                    runOnUiThread(new Runnable() {
                        public void run() {
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
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
               // end for
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

            for(int i = 0; i < libros.size(); i++){
                String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + libros.get(i).getISBN();
                galondo.uv.inkwell.HTTPConnector task = new galondo.uv.inkwell.HTTPConnector(url);
                try {
                    String urlPortada = task.execute().get();
                    Log.d(null, urlPortada);
                    libros.get(i).setImage_drawable(urlPortada);

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
            RecycleView();

        }
    }
    };



