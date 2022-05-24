package galondo.uv.inkwell;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.ExecutionException;

public class AddLibro extends AppCompatActivity {

    // creating variables for our edittext, button and dbhandler
    private EditText bookTitleEdt, bookAuthorEdt, bookISBNEdt, bookDescriptionEdt, bookGenreEdt;
    private Button addCourseBtn;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_libro);

        // initializing all our variables.
        bookTitleEdt = findViewById(R.id.idEdtBookTitle);
        bookAuthorEdt = findViewById(R.id.idEdtBookAuthor);
        bookDescriptionEdt = findViewById(R.id.idEdtBookDescription);
        bookISBNEdt = findViewById(R.id.idEdtBookISBN);
        bookGenreEdt = findViewById(R.id.idEdtBookGenre);
        addCourseBtn = (Button) findViewById(R.id.idBtnAddBook);

        // creating a new dbhandler class
        // and passing our context to it.
        dbHandler = new DBHandler(AddLibro.this);

        // below line is to add on click listener for our add course button.
        addCourseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // below line is to get data from all edit text fields.
                String bookTitle = bookTitleEdt.getText().toString();
                String bookAuthor = bookAuthorEdt.getText().toString();
                String bookDescription = bookDescriptionEdt.getText().toString();
                String bookISBN = bookISBNEdt.getText().toString();
                String bookImage;
                String bookGenre = bookGenreEdt.getText().toString();

                Log.d(null, "Titulo: " + bookTitle);

                Log.d(null, "Autor: " + bookAuthor);
                Log.d(null, "Descripcion: " + bookDescription);
                Log.d(null, "ISBN: " + bookISBN);
                Log.d(null, "Genero: " + bookGenre);

                // validating if the text fields are empty or not.
                if (bookTitle.isEmpty() && bookAuthor.isEmpty() && bookDescription.isEmpty() && bookISBN.isEmpty() && bookGenre.isEmpty()) {
                    Toast.makeText(AddLibro.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                    return;
                }

                bookImage = ConexionPortada(bookISBN);

                // on below line we are calling a method to add new
                // course to sqlite data and pass all our values to it.
                dbHandler.addNewCourse(bookTitle, bookAuthor, bookImage, bookISBN,bookGenre ,bookDescription );

                // after adding the data we are displaying a toast message.
                Toast.makeText(AddLibro.this, "Add has been added.", Toast.LENGTH_SHORT).show();
                bookTitleEdt.setText("");
                bookAuthorEdt.setText("");
                bookDescriptionEdt.setText("");
                bookISBNEdt.setText("");
                bookGenreEdt.setText("");
            }
        });
    }

    String ConexionPortada(String isbn){
        String url = "https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn;
        galondo.uv.inkwell.HTTPConnector task = new galondo.uv.inkwell.HTTPConnector(url);
        try {
            String urlPortada = task.execute().get();
            if(urlPortada.isEmpty() || urlPortada == null || urlPortada == "")
                urlPortada = "https://via.placeholder.com/300x400";
            return urlPortada;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return " ";
    }
}
