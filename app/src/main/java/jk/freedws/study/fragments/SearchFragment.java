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


public class SearchFragment extends Fragment {

    ListView listView;
    EditText searchElement;
    Context context;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor cursor;
    SimpleCursorAdapter adapter;

    Parcelable state;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);
        context = getActivity().getApplicationContext();
        listView = v.findViewById(R.id.search__list);

        TextView title = getActivity().findViewById(R.id.actionbar_title);
        searchElement = v.findViewById(R.id.searchElement);
        title.setText("Поиск");

        dbHelper = new DBHelper(context);
        dbHelper.create_db();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        state = listView.onSaveInstanceState();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(state != null) {
            listView.onRestoreInstanceState(state);
        }
        try {
            db = dbHelper.open();
            String[] headers = new String[]{DBHelper.COLUMN_NAME};
            adapter = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_1,
                    cursor, headers, new int[]{android.R.id.text1}, 0);

            if(!searchElement.getText().toString().isEmpty())
                adapter.getFilter().filter(searchElement.getText().toString());

            searchElement.addTextChangedListener(new TextWatcher() {

                public void afterTextChanged(Editable s) { }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    adapter.getFilter().filter(s.toString());
                }
            });

            adapter.setFilterQueryProvider(new FilterQueryProvider() {
                @Override
                public Cursor runQuery(CharSequence constraint) {

                    if (constraint == null || constraint.length() == 0) {
                        return db.rawQuery("select * from " + DBHelper.TABLE + " where " +DBHelper.COLUMN_ID +" =? ", new String[]{"0"});
                    }
                    else {
                        return db.rawQuery("select * from " + DBHelper.TABLE + " where " +
                                DBHelper.COLUMN_NAME + " like ? order by ?", new String[]{"%" + constraint.toString() + "%", "asc"});
                    }
                }
            });

            listView.setAdapter(adapter);
        }
        catch (SQLException ex){}
    }


    @Override
    public void onDestroy() {
        state = listView.onSaveInstanceState();
        super.onDestroy();
        if (db != null) {
            db.close();
        }
        if (cursor != null) {
            cursor.close();
        }
    }
}