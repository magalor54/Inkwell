package galondo.uv.inkwell;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

import jp.wasabeef.picasso.transformations.BlurTransformation;

public class Libro extends AppCompatActivity implements Serializable {

    private TextView _name;
    private ImageView _image_drawable;
    private ImageView _background_image;
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
        _background_image = (ImageView) findViewById(R.id.backgorundImage);
        _autor = (TextView) findViewById(R.id.autor);
        _genero = (TextView) findViewById(R.id.genero);
        _ISBN = (TextView) findViewById(R.id.isbn);
        _bookInfo = (TextView) findViewById(R.id.info);

        Bundle extras = getIntent().getExtras();

        _name.setText(extras.getString("titulo"));
        //_image_drawable.setImageDrawable(extras.getImageDrawable("imagen"));
        _autor.setText(extras.getString("autor"));
        Picasso.get().load(extras.getString("imagen")).resize(1000, 1000).into(_image_drawable);
        Picasso.get()
                .load(extras.getString("imagen"))
                .resize(1000, 1000)
                .transform(new BlurTransformation(this, 25, 1))
                .into(_background_image);

        _ISBN.setText(extras.getString("ISBN"));
        _genero.setText(extras.getString("genero"));
        _bookInfo.setText(extras.getString("info"));
        isLocal = extras.getBoolean("local");
        dbHandler = new DBHandler(Libro.this);

        if (isLocal) {

            //dbHandler.deleteCourse(_name.getText().toString());

            FloatingActionButton floatingActionButton = new FloatingActionButton(this);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(32, 32, 32, 32);
            floatingActionButton.setLayoutParams(layoutParams);
            floatingActionButton.setImageResource(android.R.drawable.ic_menu_delete);
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // We are showing only toast message. However, you can do anything you need.
                    Toast.makeText(getApplicationContext(), "You clicked Floating Action Button", Toast.LENGTH_SHORT).show();
                }
            });

            LinearLayout linearLayout = findViewById(R.id.rootContainer);
            if (linearLayout != null) {
                linearLayout.addView(floatingActionButton);
            }
        }


    }

    }
