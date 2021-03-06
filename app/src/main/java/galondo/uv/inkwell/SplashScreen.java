package galondo.uv.inkwell;

import static java.util.Calendar.WEDNESDAY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class SplashScreen extends AppCompatActivity {

    ArrayList<Libro> hardCoverBooks = new ArrayList();
    ArrayList<Libro> fictionBooks = new ArrayList();
    ArrayList<Libro> paperBlack = new ArrayList();
    ArrayList<Libro> todos_libros = new ArrayList();
    String url, url1, url2;
    ImageView imageView, imageView2;
    int cont = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        imageView = findViewById(R.id.feather);
        imageView2 = findViewById(R.id.ink);

        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in_animation);
        imageView.startAnimation(animation);
        imageView2.startAnimation(animation);


        createNotificationChannel();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 22);
        calendar.set(Calendar.SECOND, 00);

        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DAY_OF_WEEK, WEDNESDAY);
        }

        Intent intent = new Intent(SplashScreen.this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }


        url = "https://api.nytimes.com/svc/books/v3/lists.json?list-name=hardcover-fiction&api-key=ZPckoDI4RV9SBHcj282KHIbQ8i8Cdjoq";
        SplashScreen.HTTPConnector httpConnector = new SplashScreen.HTTPConnector(url);

        url1 = "https://api.nytimes.com/svc/books/v3/lists.json?list-name=e-book-fiction&api-key=ZPckoDI4RV9SBHcj282KHIbQ8i8Cdjoq";
        SplashScreen.HTTPConnector httpConnector1 = new SplashScreen.HTTPConnector(url1);

        url2 = "https://api.nytimes.com/svc/books/v3/lists.json?list-name=paperback-nonfiction&api-key=ZPckoDI4RV9SBHcj282KHIbQ8i8Cdjoq";
        SplashScreen.HTTPConnector httpConnector2 = new SplashScreen.HTTPConnector(url2);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.HONEYCOMB) {
            httpConnector.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.valueOf((Void[]) null));
            httpConnector1.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.valueOf((Void[]) null));
            httpConnector2.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, String.valueOf((Void[]) null));

        }
        else {
            httpConnector.execute(String.valueOf((Void) null));
        }

    }

    class HTTPConnector extends AsyncTask<String, Void, ArrayList> {

        public HTTPConnector(String url) {
            this.url = url;
        }

        String url;
        ArrayList<Libro> libros = new ArrayList<Libro>();

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

                    runOnUiThread(new Runnable() {
                        public void run() {
                            for (int i = 0; i < result.length(); i++) {
                                try {
                                    JSONObject rec = result.getJSONObject(i);
                                    JSONArray bookDetalis = rec.getJSONArray("book_details");

                                    if(bookDetalis.getJSONObject(0).getString("primary_isbn10").compareTo("None") != 0) {
                                        libros.add(new Libro(bookDetalis.getJSONObject(0).getString("title"), bookDetalis.getJSONObject(0).getString("author"), "",bookDetalis.getJSONObject(0).getString("primary_isbn10"), rec.getString("display_name"), bookDetalis.getJSONObject(0).getString("description")));
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
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return libros;
        }

        @Override
        protected void onPostExecute(ArrayList result) {
            super.onPostExecute(result);

            ArrayList<Libro> libro = result;


            for(int i = 0; i < libro.size(); i++){
                String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + libro.get(i).getISBN();
                galondo.uv.inkwell.HTTPConnector task = new galondo.uv.inkwell.HTTPConnector(url);
                try {
                    String urlPortada = task.execute().get();
                    libro.get(i).setImage_drawable(urlPortada);

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            todos_libros.addAll(libro);
            cont++;

            if(cont == 3){
                ordenarLibros();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                intent.putExtra("todos_libros", todos_libros);
                intent.putExtra("hardCoverBooks", hardCoverBooks);
                intent.putExtra("fictionBooks", fictionBooks);
                intent.putExtra("paperBlack", paperBlack);

                startActivity(intent);
            }


        }
    }

    void ordenarLibros(){

        for(int i = 0; i < todos_libros.size(); i++){
            if(todos_libros.get(i).getGenero().equals("Hardcover Fiction")) {
                hardCoverBooks.add(todos_libros.get(i));
            }
            if(todos_libros.get(i).getGenero().equals("E-Book Fiction")){
                fictionBooks.add(todos_libros.get(i));
            }
            if(todos_libros.get(i).getGenero().equals("Paperback Nonfiction")) {
                paperBlack.add(todos_libros.get(i));
            }
        }


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("54", name, importance);
            channel.setDescription(description);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }
}