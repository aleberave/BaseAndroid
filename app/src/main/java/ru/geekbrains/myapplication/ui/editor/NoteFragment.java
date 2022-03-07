package ru.geekbrains.myapplication.ui.editor;

import static ru.geekbrains.myapplication.ui.editor.DialogFragmentChangePicture.DIALOG_FRAGMENT_CHANGE_PICTURE;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Calendar;

import ru.geekbrains.myapplication.MainActivity;
import ru.geekbrains.myapplication.R;
import ru.geekbrains.myapplication.repository.NoteData;

public class NoteFragment extends Fragment {

    private static final String CARD_DATA = "cardData";
    private NoteData noteData;
    private DatePickerRecyclerFragment datePickerFragment;

    public static NoteFragment newInstance(NoteData noteData) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(CARD_DATA, noteData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            if (this.isVisible()) {
                inflater.inflate(R.menu.menu_description_fragment, menu);
                menu.findItem(R.id.action_activity_exit).setVisible(false);
            }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_description_delete_date): {
                noteData.setDate(Calendar.getInstance().getTime());
                datePickerFragment = DatePickerRecyclerFragment.newInstance(noteData);
                getChildFragmentManager().beginTransaction()
                        .add(R.id.container_date_picker_fragment, datePickerFragment).commit();
                break;
            }
            case (R.id.action_description_exit): {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    requireActivity().getSupportFragmentManager().popBackStack();
                    break;
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    requireActivity().finish();
                    break;
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        initView(view);
    }

    private void initView(View view) {
        if (getArguments() != null) {
            noteData = getArguments().getParcelable(CARD_DATA);
            EditText title = (EditText) view.findViewById(R.id.fragment_card_input_title);
            EditText description = (EditText) view.findViewById(R.id.fragment_card_input_description);
            ImageView imageView = view.findViewById(R.id.fragment_dialog_change_picture_imageView);

            title.setTextSize(30f);
            title.setText(noteData.getTitle());

            //    TODO Нужно доделать
            //    НЕ РАБОТАЕТ с временной cardData?!
            // 1. Выбор картинки и даты сделать через
            //    Publisher-Observer(?!), м.б. предварительно сохранив
            //    данные из cardData во временной cardData1,
            //    т.к. картинку и дату получаем из
            //    диалога-фрагмента и дочернего-фрагмента.
            //    TODO Разобрать ещё раз Publisher-Observer
            // 2. Копируем все данные из cardData в cardData1,
            //    а значит все изменения будут в cardData1,
            //    и после выхода из фрагмента эти данные не сохраняться,
            //    если не нажать кнопку Save.
            // 3. Если нажата кнопка Save, то копируем и сохраняем данные
            //    из временной cardData1 в cardData.

            imageView.setImageResource(noteData.getPicture());
            imageView.setOnLongClickListener(view1 -> {
                DialogFragmentChangePicture dialogFragmentChangePicture = DialogFragmentChangePicture.newInstance(noteData);
                dialogFragmentChangePicture.show(requireActivity().getSupportFragmentManager(), DIALOG_FRAGMENT_CHANGE_PICTURE);

                return false;
            });

            description.setTextSize(30f);
            description.setText(noteData.getDescription());

            datePickerFragment = DatePickerRecyclerFragment.newInstance(noteData);
            getChildFragmentManager().beginTransaction()
                    .add(R.id.container_date_picker_fragment, datePickerFragment).commit();

            view.findViewById(R.id.buttonSave).setOnClickListener(it -> {
                noteData.setLike(true);
                noteData.setTitle(title.getText().toString());
                noteData.setDescription(description.getText().toString());

                ((MainActivity) requireActivity()).getPublisher().sendCardDate(noteData);
                ((MainActivity) requireActivity()).getSupportFragmentManager().popBackStack();

                // Скрывает клавиатуру для EditText, если она
                // активированна и если пользователь сам не
                // закрыл клавиатуру перед закрытием фрагмента.
                title.setFocusable(false);
                description.setFocusable(false);
                requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            });
        }

    }

}