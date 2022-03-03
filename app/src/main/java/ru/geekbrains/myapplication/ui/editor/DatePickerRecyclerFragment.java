package ru.geekbrains.myapplication.ui.editor;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import ru.geekbrains.myapplication.R;
import ru.geekbrains.myapplication.repository.CardData;

public class DatePickerRecyclerFragment extends Fragment {

    private CardData cardData;
    private final static String CARD_DATA = "cardData";

    public DatePickerRecyclerFragment() {
    }

    public static DatePickerRecyclerFragment newInstance(CardData cardData) {
        DatePickerRecyclerFragment fragment = new DatePickerRecyclerFragment();
        Bundle args = new Bundle();
        args.putParcelable(CARD_DATA, cardData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cardData = getArguments().getParcelable(CARD_DATA);
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

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(cardData.getDate());

        datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker datePicker1, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        cardData.setDate(calendar.getTime());
                    }
                });

        datePicker.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(DatePickerRecyclerFragment.this.requireContext(), view, Gravity.CENTER_VERTICAL);
                DatePickerRecyclerFragment.this.requireActivity().getMenuInflater().inflate(R.menu.menu_description_popup, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    if (menuItem.getItemId() == R.id.action_popup_delete) {
                        Calendar calendar1 = Calendar.getInstance();
                        datePicker.init(calendar1.get(Calendar.YEAR),
                                calendar1.get(Calendar.MONTH),
                                calendar1.get(Calendar.DAY_OF_MONTH), null);
                        cardData.setDate(calendar.getTime());
                        return true;
                    }
                    return false;
                });
                popupMenu.show();
                return false;
            }
        });
    }

}