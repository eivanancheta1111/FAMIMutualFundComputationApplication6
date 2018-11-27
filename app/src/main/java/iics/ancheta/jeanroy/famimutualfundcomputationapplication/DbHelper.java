package iics.ancheta.jeanroy.famimutualfundcomputationapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 5/5/2016.
 */
public class DbHelper extends SQLiteOpenHelper {
    public static final String TAG = DbHelper.class.getSimpleName();
    public static final String DB_NAME = "myapp.db";
    public static final int DB_VERSION = 1;

    public static final String USER_TABLE = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASS = "password";

    /*
    create table users(
        id integer primary key autoincrement,
        username text,
        email text,
        password text);
     */

    public static final String CREATE_TABLE_USERS = "CREATE TABLE " + USER_TABLE + "("
            + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_USERNAME + " TEXT,"
            + COLUMN_EMAIL + " TEXT,"
            + COLUMN_PASS + " TEXT);";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(User user) {

        //get writable database
        SQLiteDatabase db = this.getWritableDatabase();

        //create content values to insert
        ContentValues values = new ContentValues();

        //Put username in  @values
        values.put(COLUMN_USERNAME, user.userName);

        //Put email in  @values
        values.put(COLUMN_EMAIL, user.email);

        //Put password in  @values
        values.put(COLUMN_PASS, user.password);

        // insert row
        long todo_id = db.insert(USER_TABLE, null, values);

        Log.d(TAG, "user inserted" + todo_id);
    }

    /**
     * PROTOTYPE 5
     *
     * @param email
     * @param pass
     * @return
     */
    public boolean getUser(String email, String pass){
        //HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "select * from  " + USER_TABLE + " where " +
                COLUMN_EMAIL + " = " + "'"+email+"'" + " and " + COLUMN_PASS + " = " + "'"+pass+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {

            return true;
        }
        cursor.close();
        db.close();

        return false;
    }

    /**public Cursor getUsername(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.query(USER_TABLE,// Selecting Table
                new String[]{KEY_USER_NAME},//Selecting columns want to query
                KEY_EMAIL + "=?",
                new String[]{email},//Where clause
                null, null, null);
        return res;
    }

     public boolean isEmailExists(String email) {
         SQLiteDatabase db = this.getReadableDatabase();
         Cursor cursor = db.query(TABLE_USERS,// Selecting Table
         new String[]{KEY_ID, KEY_USER_NAME, KEY_EMAIL, KEY_PASSWORD},//Selecting columns want to query
         KEY_EMAIL + "=?",
         new String[]{email},//Where clause
         null, null, null);

         if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
         //if cursor has value then in user database there is user associated with this given email so return true
         return true;
         }

         //if email does not exist return false
         return false;
     }*/



    /**
     * PROTOTYPE 2
     */
    public User Authenticate(User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE,// Selecting Table
                new String[]{COLUMN_ID, COLUMN_USERNAME, COLUMN_EMAIL, COLUMN_PASS},//Selecting columns want to query
                COLUMN_EMAIL + "=?",
                new String[]{user.email},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            //if cursor has value then in user database there is user associated with this given email
            User user1 = new User(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));

            //Match both passwords check they are same or not
            if (user.password.equalsIgnoreCase(user1.password)) {
                return user1;
            }
        }

        //if user password does not matches or there is no record with that email then return @false
        return null;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + USER_TABLE, null);
        return res;
    }

    public Cursor getUsername(String email){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.query(USER_TABLE,// Selecting Table
                new String[]{COLUMN_USERNAME},//Selecting columns want to query
                COLUMN_EMAIL + "=?",
                new String[]{email},//Where clause
                null, null, null);
        return res;
    }

    public boolean isEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(USER_TABLE,// Selecting Table
                new String[]{COLUMN_ID, COLUMN_USERNAME, COLUMN_EMAIL, COLUMN_PASS},//Selecting columns want to query
                COLUMN_EMAIL+ "=?",
                new String[]{email},//Where clause
                null, null, null);

        if (cursor != null && cursor.moveToFirst()&& cursor.getCount()>0) {
            //if cursor has value then in user database there is user associated with this given email so return true
            return true;
        }

        //if email does not exist return false
        return false;
    }
}
