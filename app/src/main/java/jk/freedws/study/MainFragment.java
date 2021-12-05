package jk.freedws.study;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MainFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        TextView title = getActivity().findViewById(R.id.actionbar_title);
        title.setText("Обществознание");
        return inflater.inflate(R.layout.fragment_main, container, false);
    }
}