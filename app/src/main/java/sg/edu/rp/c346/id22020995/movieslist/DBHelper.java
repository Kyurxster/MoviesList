package sg.edu.rp.c346.id22020995.movieslist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    // Start version with 1
    // increment by 1 whenever db schema changes.
    private static final int DATABASE_VER = 1;
    // Filename of the database
    private static final String DATABASE_NAME = "moviesList.db";

    // Variables for onCreate table
    private static final String TABLE_MOVIES = "moviesList";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_GENRE = "genre";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_RATING = "rating";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE " + TABLE_MOVIES +  "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_GENRE + " TEXT, "
                + COLUMN_YEAR + " INTEGER, "
                + COLUMN_RATING + " TEXT )";
        db.execSQL(createTableSql);
        Log.i("info" ,"created tables");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);
        // Create table(s) again
        //onCreate(db);
        db.execSQL("ALTER TABLE " + TABLE_MOVIES + " ADD COLUMN  module_name TEXT ");
    }

    public void insertSong(String title, String genre, int years, String rating){

        // Get an instance of the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // We use ContentValues object to store the values for
        //  the db operation
        ContentValues values = new ContentValues();
        // Store the column name as key and the title as value
        values.put(COLUMN_TITLE, title);
        // Store the column name as key and the singers as value
        values.put(COLUMN_GENRE, genre);
        // Store the column name as key and the year as value
        values.put(COLUMN_YEAR, years);
        // Store the column name as key and the stars as value
        values.put(COLUMN_RATING, rating);
        // Insert the row into the TABLE_TASK
        db.insert(TABLE_MOVIES, null, values);
        // Close the database connection
        db.close();
    }

    public ArrayList<Movies> getMovies() {
        ArrayList<Movies> songs = new ArrayList<Movies>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_TITLE, COLUMN_GENRE, COLUMN_YEAR, COLUMN_RATING};
        Cursor cursor = db.query(TABLE_MOVIES, columns, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String genre = cursor.getString(2);
                int year = cursor.getInt(3);
                String rating = cursor.getString(4);
                Movies obj = new Movies(id, title, genre, year, rating);
                songs.add(obj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return songs;
    }

    public int updateSong(Movies data) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", data.getTitle());
        values.put("Genre",data.getGenre());
        values.put("year", data.getYear());
        values.put("rating", data.getRating());
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(data.getId())};
        int result = db.update(TABLE_MOVIES, values, condition, args);
        db.close();

        return result;
    }

    public int deleteSong(int id) {

        // Deletes a row given its rowId, but I want to be able to pass
        // in the name of the KEY_NAME and have it delete that row.
        SQLiteDatabase db = this.getReadableDatabase();
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(id)};
        int result = db.delete(TABLE_MOVIES, condition, args);

        return result;
    }
}
