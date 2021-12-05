package jk.freedws.study.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import jk.freedws.study.ItemActivity;
import jk.freedws.study.R;
import jk.freedws.study.db.DBHelper;

public class MainFragment extends Fragment {

    ListView userList;
    DBHelper dbHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        TextView title = getActivity().findViewById(R.id.actionbar_title);
        title.setText("Обществознание");


        View v = inflater.inflate(R.layout.fragment_main, container, false);
        context = getActivity().getApplicationContext();

        userList = v.findViewById(R.id.listView);
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, ItemActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        dbHelper = new DBHelper(context);
        // создаем базу данных
        dbHelper.create_db();

        return v;
    }


    @Override
    public void onResume() {
        super.onResume();
        db = dbHelper.open();
        userCursor = db.rawQuery("select * from " + DBHelper.TABLE, null);
        String[] headers = new String[]{DBHelper.COLUMN_NAME};
        userAdapter = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_1,
                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        userList.setAdapter(userAdapter);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        db.close();
        userCursor.close();
    }
}