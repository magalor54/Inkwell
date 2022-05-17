package galondo.uv.inkwell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class Libro extends AppCompatActivity implements Serializable {

    private TextView _name;
    private ImageView _image_drawable;
    private TextView _autor;
    private TextView _ISBN;
    private TextView _genero;
    private TextView _bookInfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_drawable() {
        return image_drawable;
    }

    public void setImage_drawable(String image_drawable) {
        this.image_drawable = image_drawable;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getBookInfo() {
        return bookInfo;
    }

    public void setBookInfo(String bookInfo) {
        this.bookInfo = bookInfo;
    }

    private String name;
    private String image_drawable;

    private String autor;
    private String ISBN;
    private String genero;
    private String bookInfo;

    private Boolean isLocal;
    private DBHandler dbHandler;

    public Libro() {
    }

    public Libro(String name, String autor,String image_drawable, String ISBN, String genero, String bookInfo) {
        this.name = name;
        this.image_drawable = image_drawable;
        this.autor = autor;
        this.ISBN = ISBN;
        this.genero = genero;
        this.bookInfo = bookInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libro);

        _name = (TextView) findViewById(R.id.titulo);
        _image_drawable = (ImageView) findViewById(R.id.portada);
        _autor = (TextView) findViewById(R.id.autor);
        _genero = (TextView) findViewById(R.id.genero);
        _ISBN = (TextView) findViewById(R.id.isbn);
        _bookInfo = (TextView) findViewById(R.id.info);

        Bundle extras = getIntent().getExtras();

        _name.setText(extras.getString("titulo"));
        //_image_drawable.setImageDrawable(extras.getImageDrawable("imagen"));
        _autor.setText(extras.getString("autor"));
        Picasso.get().load(extras.getString("imagen")).resize(1000, 1000).into(_image_drawable);
        _ISBN.setText(extras.getString("ISBN"));
        _genero.setText(extras.getString("genero"));
        _bookInfo.setText(extras.getString("info"));
        isLocal = extras.getBoolean("local");
        dbHandler = new DBHandler(Libro.this);
        }

    /*************************************/
    /* Create the actionbar options menu */
    /*************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (isLocal) {
            menu.add(0, 0, 0, "Delete").setIcon(R.drawable.ic_baseline_delete_24)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        dbHandler.deleteCourse(_name.getText().toString());
        return true;


    }


    }


