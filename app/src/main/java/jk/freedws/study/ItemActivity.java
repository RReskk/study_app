package jk.freedws.study;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import jk.freedws.study.db.DBHelper;

public class ItemActivity extends AppCompatActivity {

    ImageView star;
    TextView textView;
    public boolean favorite = false;

    DBHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long Id=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        changeActionBar();
        setContentView(R.layout.activity_item);

        sqlHelper = new DBHelper(this);
        sqlHelper.create_db();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Id = extras.getLong("id");
        }

        if (Id > 0) {
            // получаем элемент по id из бд
            userCursor = db.rawQuery("select * from definitions where " +
                    DBHelper.COLUMN_ID + "=?", new String[]{String.valueOf(Id)});
            userCursor.moveToFirst();
            textView.setText(userCursor.getString(3));
            userCursor.close();
        } else {
            // скрываем кнопку удаления
        }

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                star.setImageResource(R.drawable.ic_baseline_star_24);
            }
        });

    }

//    public void saveClicke(View view){
//        ContentValues cv = new ContentValues();
//        cv.put(DBHelper.COLUMN_FAVORITE, true);
//
//        if (userId > 0) {
//            db.update(DatabaseHelper.TABLE, cv, DatabaseHelper.COLUMN_ID + "=" + userId, null);
//        } else {
//            db.insert(DatabaseHelper.TABLE, null, cv);
//        }
//        goHome();
//    }

    private void changeActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
    }
}