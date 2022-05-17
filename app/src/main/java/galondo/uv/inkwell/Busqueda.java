package galondo.uv.inkwell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.SearchView;

import java.util.ArrayList;

public class Busqueda extends AppCompatActivity {

    ArrayList<Libro> todos_libros = new ArrayList();
    private HorizontalAdapter adapter;
    private RecyclerView recyclerView;
    private SearchView searchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView)menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth((Integer.MAX_VALUE));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                adapter.getFilter().filter(s);

                recyclerView = (RecyclerView)findViewById(R.id.recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(Busqueda.this));
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(onItemClickListener);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);

                recyclerView = (RecyclerView)findViewById(R.id.recycler);
                recyclerView.setLayoutManager(new LinearLayoutManager(Busqueda.this));
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(onItemClickListener);
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda);
        Bundle extras = getIntent().getExtras();
        todos_libros = (ArrayList<Libro>) getIntent().getSerializableExtra("todos_libros");
        Log.d(null, todos_libros.toString());
        RecycleView(todos_libros);
    }

    private void RecycleView( ArrayList<Libro> libro){

        recyclerView = (RecyclerView) findViewById(R.id.recycler);

        adapter = new HorizontalAdapter(this, libro);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        adapter.setOnItemClickListener(onItemClickListener);

    }

    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ArrayList info = new ArrayList();

            // This viewHolder will have all required values.
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();

            info = adapter.getInfoPosition(viewHolder.getAdapterPosition());

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
}