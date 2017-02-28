package tonydarko.mykhai.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "my_khai_db";

    public static final String EXTRA_BALL_TABLE = "extra_ball_table";

    public static final String GROUP_COLUMN = "group";
    public static final String FIO_COLUMN = "fio_student";
    public static final String FULL_INFO_COLUMN = "full_ball";
    public static final String BALL_COLUMN = "ball";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
