package bean;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lyxsh on 2016/8/13.
 */
public class Sqlite extends SQLiteOpenHelper
{
    private static final String DB_NAME = "Icard_sqlite.db";//数据库名
    private static final int version = 1; //数据库版本

    public Sqlite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    public Sqlite(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler)
    {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sqlstr;
        sqlstr = "CREATE TABLE xinxi (_id INTEGER PRIMARY KEY AUTOINCREMENT,EQID TEXT,EQIDMD5 TEXT);";
        db.execSQL(sqlstr);
        sqlstr ="insert into xinxi (_id) values('1')";
        db.execSQL(sqlstr);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
