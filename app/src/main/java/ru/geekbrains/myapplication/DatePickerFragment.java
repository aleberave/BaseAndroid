package ru.geekbrains.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DatePickerFragment extends Fragment {

    private MyNote myNote;
    private final static String MY_NOTE = "my note";

    public DatePickerFragment() {
    }

    public static DatePickerFragment newInstance(MyNote myNote) {
        DatePickerFragment fragment = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putParcelable(MY_NOTE, myNote);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            myNote = getArguments().getParcelable(MY_NOTE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_date_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        DatePicker datePicker = view.findViewById(R.id.date_picker_on);
        int dayOf = datePicker.getDayOfMonth();
        int monthOf = datePicker.getMonth();
        int yearOf = datePicker.getYear();

        if (myNote.getNoteDateDay(requireContext()) > 0 &&
                myNote.getNoteDateMonth(requireContext()) > 0 &&
                myNote.getNoteDateYear(requireContext()) > 0) {
            dayOf = myNote.getNoteDateDay(requireContext());
            monthOf = myNote.getNoteDateMonth(requireContext());
            yearOf = myNote.getNoteDateYear(requireContext());
        }

        datePicker.init(yearOf, monthOf, dayOf, (datePicker1, year, monthOfYear, dayOfMonth) -> {
            String dateStr = dayOfMonth + getString(R.string.divided) + (monthOfYear + 1) + getString(R.string.divided) + year;
            String da = dateStr + getString(R.string.new_line) + myNote.getNoteBody(requireContext());
            myNote.setNoteDate(requireContext(), year, monthOfYear, dayOfMonth);
            assert (getParentFragment() != null ? getParentFragment().getView() : null) != null;
            EditText editText = getParentFragment().getView().findViewById(R.id.fragment_description_editText);
            editText.setText(da);
        });
    }
}