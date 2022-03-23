package ru.geekbrains.myapplication.ui.editor;

import static ru.geekbrains.myapplication.ui.editor.DialogFragmentChangePicture.DIALOG_FRAGMENT_CHANGE_PICTURE;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

import ru.geekbrains.myapplication.MainActivity;
import ru.geekbrains.myapplication.Navigation;
import ru.geekbrains.myapplication.R;
import ru.geekbrains.myapplication.publisher.Observer;
import ru.geekbrains.myapplication.publisher.Publisher;
import ru.geekbrains.myapplication.repository.NoteData;
import ru.geekbrains.myapplication.repository.PictureOrColorIndexConverter;

public class NewNoteFragment extends DialogFragment {

    public static String NEW_NOTE_FRAGMENT = "newNoteFragment";
    private NoteData noteData;
    private Publisher publisher;
    private Observer observer;

    public Publisher getPublisher() {
        return publisher;
    }

    public NewNoteFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        publisher = new Publisher();
        initView(view);
    }

    private void initView(View view) {

        EditText title = (EditText) view.findViewById(R.id.fragment_card_input_title);
        EditText description = (EditText) view.findViewById(R.id.fragment_card_input_description);
        ImageView imageView = view.findViewById(R.id.fragment_dialog_change_picture_imageView);

        noteData = new NoteData(title.getText().toString(),
                description.getText().toString(),
                PictureOrColorIndexConverter.randomPictureIndex(),
                PictureOrColorIndexConverter.randomColorIndex(),
                false,
                Calendar.getInstance().getTime());

        observer = new Observer() {
            @Override
            public void receiveCardData(NoteData noteData) {
                imageView.setImageResource(noteData.getPicture());
            }
        };
        getPublisher().subscribe(observer);

        title.setTextSize(30f);

        // рандомная картинка
        int idPicture = PictureOrColorIndexConverter.randomPictureIndex();
        imageView.setImageResource(idPicture);
        noteData.setPicture(idPicture);

        imageView.setOnLongClickListener(view1 -> {
            DialogFragmentChangePicture dialogFragmentChangePicture = DialogFragmentChangePicture.newInstance(noteData, this);
            dialogFragmentChangePicture.show(requireActivity().getSupportFragmentManager(), DIALOG_FRAGMENT_CHANGE_PICTURE);
            return false;
        });

        description.setTextSize(30f);

        new Navigation(getChildFragmentManager())
                .addFragment(R.id.container_date_picker_fragment,
                        DatePickerRecyclerFragment.newInstance(noteData), false);

        view.findViewById(R.id.buttonSave).setOnClickListener(it -> {
            if (title.getText().toString().length() > 0 && description.getText().toString().length() > 0) {
                noteData = new NoteData(title.getText().toString(),
                        description.getText().toString(),
                        noteData.getPicture(),
                        PictureOrColorIndexConverter.randomColorIndex(),
                        false,
                        noteData.getDate());

                ((MainActivity) requireActivity()).getPublisher().sendCardDate(noteData);
                ((MainActivity) requireActivity()).getSupportFragmentManager().popBackStack();

                // Скрывает клавиатуру для EditText, если она
                // активированна и если пользователь сам не
                // закрыл клавиатуру перед закрытием фрагмента.
                title.setFocusable(false);
                description.setFocusable(false);
                requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                dismiss();
            } else if (title.getText().toString().length() == 0) {
                Toast.makeText(requireContext(), getString(R.string.input_title_note), Toast.LENGTH_SHORT).show();
            } else if (description.getText().toString().length() == 0) {
                Toast.makeText(requireContext(), getString(R.string.input_description_note), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDestroyView() {
        getPublisher().unsubscribe(observer);
        super.onDestroyView();
    }
}

