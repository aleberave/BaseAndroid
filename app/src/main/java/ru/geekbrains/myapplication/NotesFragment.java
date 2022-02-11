package ru.geekbrains.myapplication;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NotesFragment extends Fragment {

    protected static final String CURRENT_NOTE_KEY = "current note key";
    private MyNote myNote;

    public NotesFragment() {
    }

    public static NotesFragment newInstance() {
        return new NotesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CURRENT_NOTE_KEY, myNote);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            myNote = savedInstanceState.getParcelable(CURRENT_NOTE_KEY);
        } else {
            myNote = new MyNote(0);
        }
        if (getActivity() != null)
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                DescriptionNoteFragment descriptionNoteFragment = DescriptionNoteFragment.newInstance(myNote);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_fragment_id_description_note, descriptionNoteFragment).commit();
            }
        initView(view);
    }

    private void initView(View view) {
        LinearLayout linearLayout = (LinearLayout) view;
        String[] notesName = getResources().getStringArray(R.array.note_name);
        for (int i = 0; i < notesName.length; i++) {
            TextView textView = new TextView(getContext());
            textView.setTextSize(30f);
            textView.setText(notesName[i]);
            linearLayout.addView(textView);

            final int finalI = i;
            textView.setOnClickListener(view1 -> {
                if (getActivity() != null) {
                    myNote = new MyNote(finalI);
                    DescriptionNoteFragment descriptionNoteFragment = DescriptionNoteFragment.newInstance(myNote);
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container_fragment_notes, descriptionNoteFragment)
                                .addToBackStack("").commit();
                    } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container_fragment_id_description_note, descriptionNoteFragment).commit();
                    }
                }
            });
        }
    }

}