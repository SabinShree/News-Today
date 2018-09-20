package DatabaseManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

public class DatabaseHandler {
    static SQLiteDatabase mDatabase;
    public static final String databaseName = "newsInfo.db";
    private static final String imageUrl = "imageUrl";
    public static final String tableName = "NewsLoadsCategory";
    static final String newsName = "newsName";
    static final String description = "description";
    static final String newsTime = "newsTime";
    static final String author = "author";
    static final String url = "url";
    static Context context;
    public static final String createNewsInfoTable = "CREATE TABLE IF NOT EXISTS " + tableName + " (" + url + " TEXT PRIMARY KEY, " + imageUrl + " TEXT, " + newsName + " TEXT," +
            description + " TEXT," + newsTime + " TEXT, " + author + " TEXT " + ")";

    public DatabaseHandler(Context context) {
        DatabaseHandler.context = context;
        DatabaseHelperUser databaseHelperUser = new DatabaseHelperUser(context);
        mDatabase = databaseHelperUser.getWritableDatabase();
    }

    public static class DatabaseHelperUser extends SQLiteOpenHelper {

        public DatabaseHelperUser(Context context) {
            super(context, databaseName, null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(createNewsInfoTable);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("Drop TABLE IF EXISTS " + tableName);
            onCreate(db);
        }
    }
    public long insertData(ContentValues values) {
        return mDatabase.insert(tableName, "", values);
    }
    public Cursor query(String[] projection, String selection, String[] arguments, String sortOrder) {
        SQLiteQueryBuilder sqLiteQueryBuilder = new SQLiteQueryBuilder();
        sqLiteQueryBuilder.setTables(tableName);
        return sqLiteQueryBuilder.query(mDatabase, projection, selection, arguments, null, null, sortOrder);
    }
}
