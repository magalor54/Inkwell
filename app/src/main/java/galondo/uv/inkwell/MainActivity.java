package galondo.uv.inkwell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;

    private HorizontalAdapter adapterHardcover;
    private HorizontalAdapter adapterFiction;
    private HorizontalAdapter adapterPaper;
    ArrayList<Libro> hardCoverBooks = new ArrayList();
    ArrayList<Libro> fictionBooks = new ArrayList();
    ArrayList<Libro> paperBlack = new ArrayList();
    ArrayList<String> covers = new ArrayList();
    ArrayList<Libro> todos_libros = new ArrayList();

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.featherChiquito);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_animation);
        imageView.startAnimation(animation);

        this.todos_libros = (ArrayList<Libro>) getIntent().getSerializableExtra("todos_libros");
        this.fictionBooks = (ArrayList<Libro>) getIntent().getSerializableExtra("fictionBooks");
        this.paperBlack = (ArrayList<Libro>) getIntent().getSerializableExtra("paperBlack");
        this.hardCoverBooks = (ArrayList<Libro>) getIntent().getSerializableExtra("hardCoverBooks");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), Biblioteca.class);
                startActivity(myIntent);
            }
        });

        RecycleView(hardCoverBooks);
        RecycleView(fictionBooks);
        RecycleView(paperBlack);

        //startAlert();
    }


    private void RecycleView( ArrayList<Libro> libro){

        if(libro.get(0).getGenero().equals("Hardcover Fiction")) {
            recyclerView = (RecyclerView) findViewById(R.id.recycler);
            adapterHardcover = new HorizontalAdapter(this, libro);
            recyclerView.setAdapter(adapterHardcover);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            adapterHardcover.setOnItemClickListener(onItemClickListener);
        }
        if(libro.get(0).getGenero().equals("E-Book Fiction")) {
            recyclerView1 = (RecyclerView) findViewById(R.id.recycler1);
            adapterFiction = new HorizontalAdapter(this, libro);
            recyclerView1.setAdapter(adapterFiction);
            recyclerView1.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            adapterFiction.setOnItemClickListener(onItemClickListener1);
        }
        if(libro.get(0).getGenero().equals("Paperback Nonfiction")){
            recyclerView2 = (RecyclerView) findViewById(R.id.recycler2);
            adapterPaper = new HorizontalAdapter(this, libro);
            recyclerView2.setAdapter(adapterPaper);
            recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
            adapterPaper.setOnItemClickListener(onItemClickListener2);
        }
    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ArrayList info = new ArrayList();

            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();

            info = adapterHardcover.getInfoPosition(viewHolder.getAdapterPosition());

            Intent intent = new Intent(getApplicationContext(), Libro.class);

            intent.putExtra("titulo", String.valueOf(info.get(0)));
            intent.putExtra("imagen", String.valueOf(info.get(1)));
            intent.putExtra("ISBN", String.valueOf(info.get(2)));
            intent.putExtra("autor", String.valueOf(info.get(3)));
            intent.putExtra("genero", String.valueOf(info.get(4)));
            intent.putExtra("info", String.valueOf(info.get(5)));

            startActivity(intent);
        }
    };

    private View.OnClickListener onItemClickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ArrayList info = new ArrayList();

            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();

            info = adapterFiction.getInfoPosition(viewHolder.getAdapterPosition());

            Intent intent = new Intent(getApplicationContext(), Libro.class);

            intent.putExtra("titulo", String.valueOf(info.get(0)));
            intent.putExtra("imagen", String.valueOf(info.get(1)));
            intent.putExtra("ISBN", String.valueOf(info.get(2)));
            intent.putExtra("autor", String.valueOf(info.get(3)));
            intent.putExtra("genero", String.valueOf(info.get(4)));
            intent.putExtra("info", String.valueOf(info.get(5)));

            startActivity(intent);
        }
    };

    private View.OnClickListener onItemClickListener2 = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ArrayList info = new ArrayList();

            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();

            info = adapterPaper.getInfoPosition(viewHolder.getAdapterPosition());

            Intent intent = new Intent(getApplicationContext(), Libro.class);

            intent.putExtra("titulo", String.valueOf(info.get(0)));
            intent.putExtra("imagen", String.valueOf(info.get(1)));
            intent.putExtra("ISBN", String.valueOf(info.get(2)));
            intent.putExtra("autor", String.valueOf(info.get(3)));
            intent.putExtra("genero", String.valueOf(info.get(4)));
            intent.putExtra("info", String.valueOf(info.get(5)));

            startActivity(intent);
        }
    };

    public void startAlert() {
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (5000), pendingIntent);
        Toast.makeText(this, "Alarm set in " + 5000 + " milliseconds",
                Toast.LENGTH_LONG).show();

    }

    /*************************************/
    /* Create the actionbar options menu */
    /*************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(0, 0, 0, "Buscar").setIcon(R.drawable.ic_baseline_search_24)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

       return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        Intent myIntent = new Intent(this, Busqueda.class);
        myIntent.putExtra("todos_libros", todos_libros);
        startActivity(myIntent);
        return true;


    }

    };





