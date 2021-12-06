package jk.freedws.study;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import jk.freedws.study.db.DBHelper;

public class ItemActivity extends AppCompatActivity {

    TextView textView;
    ImageView star;
    DBHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    long userId=0;
    Boolean isFavorite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        changeActionBar();

        textView = findViewById(R.id.item__definition_text);
        star = findViewById(R.id.imageStar);
        sqlHelper = new DBHelper(this);
        db = sqlHelper.open();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userId = extras.getLong("id");
        }
        // если 0, то добавление
        if (userId > 0) {
            // получаем элемент по id из бд
            userCursor = db.rawQuery("select * from " + DBHelper.TABLE + " where " +
                    DBHelper.COLUMN_ID + "=?", new String[]{String.valueOf(userId)});
            userCursor.moveToFirst();
            textView.setText(userCursor.getString(2));
            isFavorite = Boolean.parseBoolean(userCursor.getString(3));
            starChange(isFavorite);

            userCursor.close();
        }

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addFav();
            }
        });
    }

    private void starChange(Boolean isTrue) {
        if (isTrue) {
            star.setImageResource(R.drawable.ic_baseline_star_24);
        } else {
            star.setImageResource(R.drawable.ic_baseline_star_border_24);
        }
    }

    public void addFav(){
        ContentValues cv = new ContentValues();
        if (isFavorite == false) {
            starChange(true);
            isFavorite = true;
            cv.put(DBHelper.COLUMN_FAVORITE, "true");
        } else {
            starChange(false);
            isFavorite = false;
            cv.put(DBHelper.COLUMN_FAVORITE, "false");
        }

        if (userId > 0) {
            db.update(DBHelper.TABLE, cv, DBHelper.COLUMN_ID + "=" + String.valueOf(userId), null);
        }
    }

    private void changeActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);
    }
}