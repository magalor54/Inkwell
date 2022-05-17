package galondo.uv.inkwell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class Biblioteca extends AppCompatActivity {

    // creating variables for our array list,
    // dbhandler, adapter and recycler view.
    private ArrayList<Libro> miBiblioteca;
    private DBHandler dbHandler;
    private HorizontalAdapter courseRVAdapter;
    private RecyclerView myBooksRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biblioteca);

        readCourses();

        FloatingActionButton fab = findViewById(R.id.fabBiblioteca);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), AddLibro.class);
                startActivity(myIntent);
            }
        });
    }
    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ArrayList info = new ArrayList();

            // This viewHolder will have all required values.
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();

            info = courseRVAdapter.getInfoPosition(viewHolder.getAdapterPosition());

            Intent intent = new Intent(getApplicationContext(), Libro.class);

            intent.putExtra("titulo", String.valueOf(info.get(0)));
            intent.putExtra("imagen", String.valueOf(info.get(1)));
            intent.putExtra("ISBN", String.valueOf(info.get(2)));
            intent.putExtra("autor", String.valueOf(info.get(3)));
            intent.putExtra("genero", String.valueOf(info.get(4)));
            intent.putExtra("info", String.valueOf(info.get(5)));
            intent.putExtra("local", true);

            startActivity(intent);
        }
    };

    @Override
    public void onResume()
    {  // After a pause OR at startup
        super.onResume();
        readCourses();
    }

    void readCourses(){
        // initializing our all variables.
        miBiblioteca = new ArrayList<>();
        dbHandler = new DBHandler(Biblioteca.this);

        // getting our course array
        // list from db handler class.
        miBiblioteca = dbHandler.readCourses();

        // on below line passing our array lost to our adapter class.
        courseRVAdapter = new HorizontalAdapter(this, miBiblioteca);
        myBooksRV = findViewById(R.id.bibliotecaRV);

        // setting layout manager for our recycler view.
        myBooksRV.setLayoutManager(new GridLayoutManager(this, 3));

        // setting our adapter to recycler view.
        myBooksRV.setAdapter(courseRVAdapter);
        courseRVAdapter.setOnItemClickListener(onItemClickListener);

    }
}