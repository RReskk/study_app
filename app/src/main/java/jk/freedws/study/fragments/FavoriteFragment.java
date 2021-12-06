package jk.freedws.study.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import jk.freedws.study.ItemActivity;
import jk.freedws.study.R;
import jk.freedws.study.db.DBHelper;


public class FavoriteFragment extends Fragment {

    ListView userList;
    EditText userFilter;
    Context context;
    DBHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    Parcelable state;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_favorite, container, false);
        TextView title = getActivity().findViewById(R.id.actionbar_title);
        title.setText("Избранное");

        context = getActivity().getApplicationContext();
        userList = v.findViewById(R.id.fav__list);
        userFilter = v.findViewById(R.id.searchElementFav);

        sqlHelper = new DBHelper(context);
        // создаем базу данных
        sqlHelper.create_db();

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ItemActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        return v;
    }

    @Override
    public void onPause() {
        state = userList.onSaveInstanceState();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(state != null) {
            userList.onRestoreInstanceState(state);
        }

        try {
            db = sqlHelper.open();
            userCursor = db.rawQuery("select * from " + DBHelper.TABLE + " where "
                    + DBHelper.COLUMN_FAVORITE + "= ?", new String[] {"true"});
            String[] headers = new String[]{DBHelper.COLUMN_NAME};
            userAdapter = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_1,
                    userCursor, headers, new int[]{android.R.id.text1}, 0);

            // если в текстовом поле есть текст, выполняем фильтрацию
            // данная проверка нужна при переходе от одной ориентации экрана к другой
            if(!userFilter.getText().toString().isEmpty())
                userAdapter.getFilter().filter(userFilter.getText().toString());

            // установка слушателя изменения текста
            userFilter.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) { }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                // при изменении текста выполняем фильтрацию
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    userAdapter.getFilter().filter(s.toString());
                }
            });

            // устанавливаем провайдер фильтрации
            userAdapter.setFilterQueryProvider(new FilterQueryProvider() {
                @Override
                public Cursor runQuery(CharSequence constraint) {

                    if (constraint == null || constraint.length() == 0) {

                        return db.rawQuery("select * from " + DBHelper.TABLE + " where "
                                + DBHelper.COLUMN_FAVORITE + "= ?", new String[] {"true"});
                    }
                    else {
                        return db.rawQuery("select * from " + DBHelper.TABLE + " where " +
                                DBHelper.COLUMN_NAME + " like ?", new String[]{"%" + constraint.toString() + "%"});
                    }
                }
            });

            userList.setAdapter(userAdapter);
        }
        catch (SQLException ex){}
    }

    @Override
    public void onDestroy() {
        state = userList.onSaveInstanceState();
        super.onDestroy();
        if (db != null) {
            db.close();
        }
        if (userCursor != null) {
            userCursor.close();
        }
    }
}