package galondo.uv.inkwell;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "booksdb";

    // below int is our database version
    private static final int DB_VERSION = 2;

    // below variable is for our table name.
    private static final String TABLE_NAME = "mybooks";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our course name column
    private static final String TITLE_COL = "title";

    // below variable id for our course duration column.
    private static final String AUTHOR_COL = "author";

    // below variable for our course description column.
    private static final String DESCRIPTION_COL = "description";

    // below variable is for our course tracks column.
    private static final String ISBN_COL = "ISBN";

    // below variable is for our course tracks column.
    private static final String IMAGE_COL = "image";

    // below variable is for our course tracks column.
    private static final String GENRE_COL = "genre";

    // creating a constructor for our database handler.
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TITLE_COL + " TEXT,"
                + AUTHOR_COL + " TEXT,"
                + IMAGE_COL + " TEXT,"
                + ISBN_COL + " TEXT,"
                + GENRE_COL + " TEXT,"
                + DESCRIPTION_COL + " TEXT)";

        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
    }

    // this method is use to add new course to our sqlite database.
    public void addNewCourse(String bookName, String bookAuthor, String bookDescription, String bookISBN, String bookImage, String bookGenre) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();

        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(TITLE_COL, bookName);
        values.put(AUTHOR_COL, bookAuthor);
        values.put(DESCRIPTION_COL, bookDescription);
        values.put(ISBN_COL, bookISBN);
        values.put(IMAGE_COL, bookImage);
        values.put(GENRE_COL, bookGenre);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);

        // at last we are closing our
        // database after adding database.
        db.close();
    }

    public ArrayList<Libro> readCourses() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorBooks = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        // on below line we are creating a new array list.
        ArrayList<Libro> courseModalArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorBooks.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                courseModalArrayList.add(new Libro(cursorBooks.getString(1),
                        cursorBooks.getString(2),
                        cursorBooks.getString(6),
                        cursorBooks.getString(4),
                        cursorBooks.getString(3),cursorBooks.getString(5)));
            } while (cursorBooks.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorBooks.close();
        return courseModalArrayList;
    }

    // below is the method for deleting our course.
    public void deleteCourse(String courseName) {
        Log.d(null, "DBHandler delete Course-------------> " + courseName);
        // on below line we are creating
        // a variable to write our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are calling a method to delete our
        // course and we are comparing it with our course name.
        db.delete(TABLE_NAME, "title=?", new String[]{courseName});
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


}
