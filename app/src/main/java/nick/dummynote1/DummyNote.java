package nick.dummynote1;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class DummyNote extends AppCompatActivity implements ListView.OnItemClickListener{
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummynote);
        listView = (ListView)findViewById(R.id.listView);
        listView.setEmptyView(findViewById(R.id.empty));
        listView.setOnItemClickListener(this);
        mDbHelper = new DB(this).open();//開啟資料庫動作 只需一開始開啟就好了
        setAdapter();
    }

    private DB mDbHelper;
    private Cursor mNotesCursor;

    private void setAdapter() {
        mNotesCursor = mDbHelper.getAll();      //開始讀取資料  呼叫getAll()方法
        if(mNotesCursor!=null)
            mNotesCursor.moveToFirst();
        startManagingCursor(mNotesCursor);      //不須要讓使用者等待 可使用loaderManaging(可研究)
        String[] from = new String[]{"note","created"};   //顯示特定欄位 note(一開始設定)
        int[] to = new int[]{R.id.text1,R.id.text2};   //目的地顯示在 text1身上
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.simple_list_item_1, mNotesCursor,
                from, to,SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);   //
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(0,1,0,"新增").setIcon(android.R.drawable.ic_menu_add).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0,2,0,"刪除").setIcon(android.R.drawable.ic_menu_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        menu.add(0,3,0,"修改").setIcon(android.R.drawable.ic_menu_edit).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }
    int count;
    long rowId;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                count++;
                mDbHelper.create(count+".Note");
                setAdapter();
                break;
            case 2:
                mDbHelper.delete(rowId);
                rowId=0;
                setAdapter();
                break;
//            case 3:
//                mDbHelper.update(rowId,null);
//                setAdapter();
//                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long id) {
        rowId=id;
        Toast.makeText(this,"第"+i+"項",Toast.LENGTH_SHORT).show();
    }
}
