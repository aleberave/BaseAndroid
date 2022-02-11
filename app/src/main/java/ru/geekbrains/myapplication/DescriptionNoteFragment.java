package ru.geekbrains.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

public class DescriptionNoteFragment extends Fragment {

    private static final String NOTE_KEY = "current_city_key";
    private DatePickerDialog picker;
    private MyNote myNote;
    private final StringBuilder stringBuilder = new StringBuilder();
    private String noteDate;

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
//        В момент создания нового фрагмента мы проверяем, создается ли этот фрагмент впервые,
//        и если да, то просто удаляем его из бэкстека.
//        Это равносильно нажатию кнопки Назад на смартфоне.
        if (savedInstanceState != null)
            requireActivity().getSupportFragmentManager().popBackStack();
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
        String[] noteDescription = getResources().getStringArray(R.array.note_description);
        EditText editText = view.findViewById(R.id.fragment_description_editText);
        editText.setTextSize(30f);
        Button buttonGet = view.findViewById(R.id.fragment_description_button);
        buttonGet.setOnClickListener(v -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            picker = new DatePickerDialog(getContext(), (datePicker, day1, month1, year1) -> {
                noteDate = String.valueOf(datePicker.getDayOfMonth())
                        .concat(getString(R.string.divided)).concat(String.valueOf(datePicker.getMonth() + 1))
                        .concat(getString(R.string.divided)).concat(String.valueOf(datePicker.getYear()));
                stringBuilder.append(requireActivity().getString(R.string.selected_date)).append(noteDate).append(getResources().getString(R.string.new_line))
                        .append(noteDescription[myNote.getIndex()]);

                if (!picker.isShowing()) {
                    editText.setText(getResources().getString(R.string.empty));
                } else {
                    editText.setText(stringBuilder);
                    stringBuilder.delete(0, stringBuilder.length());
                }
            }, year, month, day);
            picker.show();
        });

        editText.setText(noteDescription[myNote.getIndex()]);
    }

}