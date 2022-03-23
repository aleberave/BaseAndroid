package ru.geekbrains.myapplication.ui.editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import ru.geekbrains.myapplication.R;
import ru.geekbrains.myapplication.repository.NoteData;
import ru.geekbrains.myapplication.repository.PictureOrColorIndexConverter;

public class DialogFragmentChangePicture extends DialogFragment {

    public static String DIALOG_FRAGMENT_CHANGE_PICTURE = "DialogFragmentChangePicture";
    private static final String CARD_DATA_DIALOG = "cardDataDialog";
    private NoteData noteData;
    private Fragment newFragment;

    public DialogFragmentChangePicture(Fragment newFragment) {
        this.newFragment = newFragment;
    }


    public static DialogFragmentChangePicture newInstance(NoteData noteData, Fragment newFragment) {
        Bundle args = new Bundle();
        DialogFragmentChangePicture fragment = new DialogFragmentChangePicture(newFragment);
        args.putParcelable(CARD_DATA_DIALOG, noteData);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_change_picture, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            noteData = getArguments().getParcelable(CARD_DATA_DIALOG);

            TextView textView = view.findViewById(R.id.textViewDialogFragmentChangePicture);
            textView.setTextSize(30F);
            textView.setText(R.string.change_sentiment);
            view.findViewById(R.id.radioButtonDialogFragment1).setOnClickListener(view1 -> {
                setImageNote(0);
            });
            view.findViewById(R.id.radioButtonDialogFragment2).setOnClickListener(view12 -> {
                setImageNote(1);
            });
            view.findViewById(R.id.radioButtonDialogFragment3).setOnClickListener(view1 -> {
                setImageNote(2);
            });
            view.findViewById(R.id.radioButtonDialogFragment4).setOnClickListener(view1 -> {
                setImageNote(3);
            });
            view.findViewById(R.id.radioButtonDialogFragment5).setOnClickListener(view1 -> {
                setImageNote(4);
            });
        }
    }

    private void setImageNote(int i) {
        noteData.setPicture(PictureOrColorIndexConverter.getPictureByIndex(i));
        dismiss();
        if (newFragment instanceof NewNoteFragment)
            ((NewNoteFragment) newFragment).getPublisher().sendCardDate(noteData);
        if (newFragment instanceof NoteFragment)
            ((NoteFragment) newFragment).getPublisher().sendCardDate(noteData);
    }
}

