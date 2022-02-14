package ru.geekbrains.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DescriptionNoteFragment extends Fragment {

    private static final String NOTE_KEY = "current_city_key";
    private MyNote myNote;
    private StringBuilder stringBuilder = new StringBuilder();

    public static DescriptionNoteFragment newInstance(MyNote myNote) {
        DescriptionNoteFragment fragment = new DescriptionNoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(NOTE_KEY, myNote);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            getParentFragmentManager().popBackStack();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_description_note_constraint, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            myNote = getArguments().getParcelable(NOTE_KEY);
        }
        initView(view);
    }

    private void initView(View view) {
        EditText editText = view.findViewById(R.id.fragment_description_editText);
        editText.setTextSize(30f);

        final String[] date;
        if (myNote.getNoteDateDay(requireContext()) > 0 &&
                myNote.getNoteDateMonth(requireContext()) > 0 &&
                myNote.getNoteDateYear(requireContext()) > 0) {
            date = new String[]{myNote.getNoteDateDay(requireContext()) + "/" +
                    (myNote.getNoteDateMonth(requireContext()) + 1) + "/" +
                    myNote.getNoteDateYear(requireContext())};
            stringBuilder = stringBuilder.append(date[0]).append("\n")
                    .append(myNote.getNoteBody(requireContext()));
        } else {
            stringBuilder = stringBuilder.append(myNote.getNoteBody(requireContext()));
        }

        editText.setText(stringBuilder);

        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(myNote);
        getChildFragmentManager().beginTransaction()
                .add(R.id.container_date_picker_fragment, datePickerFragment).commit();
    }
}