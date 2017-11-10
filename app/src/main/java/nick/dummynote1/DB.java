package nick.dummynote1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.wifi.aware.PublishConfig;
import android.widget.Toast;

import java.util.Date;

/**
 * Created by 58_009 on 2017/11/7.
 */

public class DB {
    private static final String DATABASE_NAME ="notes.db";
    private static final int DATABASE_VERSION = 1;          //版本異動 記得改數字
    private static final String DATABASE_TABLE = "mytable";    //原先版本
//    private static final String NEW_DATABASE_TABLE = "mytable2";    //新的版本
    private static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS "+DATABASE_TABLE+"(_id INTEGER PRIMARY KEY,note TEXT NOT NULL,created INTEGER);";   //CMD 建立資料庫的語法

        private  static class  DatabaseHelper extends SQLiteOpenHelper{
//            Context mCtx;
            public DatabaseHelper(Context context) {
                super(context, DATABASE_NAME, null, DATABASE_VERSION );
//               mCtx =context;
            }
            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(DATABASE_CREATE);//資料表建立工作   //若有資料表時 就不會重新建立
//                Toast.makeText(mCtx,"資料表更新完畢",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS "+ DATABASE_TABLE);//讓他去執行刪除資料表的動作 刪除DATABASE_TABLE
                onCreate(db);
            }
        }

    private Context mCtx = null;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DB(Context ctx) {
        this.mCtx = ctx;
    }

    public DB open() throws SQLException {
        dbHelper = new DatabaseHelper(mCtx);
        db = dbHelper.getWritableDatabase();    //資料庫不存在 就會新增一個  資料庫存在 就會做個連線
        return this;
    }

    public void close() {
        dbHelper.close();
    }
    public static final String KEY_ROWID ="_id";
    public static final String KEY_NOTE = "note";
    public static final String KEY_CREATED = "created";

    String[] strCols = new String[]{KEY_ROWID,KEY_NOTE,KEY_CREATED};

    public Cursor getAll(){
//        return db.rawQuery("SELECT * FROM "+DATABASE_TABLE,null);
        return db.query(DATABASE_TABLE,
                strCols,
                null,
                null,
                null,
                null,
                null);
    }
    public long create(String noteName){
        Date now = new Date();
        ContentValues args = new ContentValues();
        args.put(KEY_NOTE,noteName);
        args.put(KEY_CREATED,now.getTime());
        return db.insert(DATABASE_TABLE,null,args);
    }
    public boolean delete(long rowId){  //要刪除 特地某筆資料 就帶參數
        if(rowId>0){
            return db.delete(DATABASE_TABLE,KEY_ROWID+"="+rowId,null)>0;    //參數(指定的表格,where語法,)
        }else{
            return db.delete(DATABASE_TABLE,null,null)>0;
        }
    }
    public boolean delete() {           //要刪除全部 不帶參數
        return delete(-1) ;             //刪除到最後
    }
    public boolean update(long rowId, String note){
        ContentValues args = new ContentValues();
        args.put(KEY_NOTE,note);
        return db.update(DATABASE_TABLE,args,KEY_ROWID+"="+rowId,null)>0;
    }
}
