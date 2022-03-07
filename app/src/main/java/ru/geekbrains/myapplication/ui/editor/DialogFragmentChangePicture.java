package ru.geekbrains.myapplication.ui.editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ru.geekbrains.myapplication.R;
import ru.geekbrains.myapplication.repository.NoteData;

public class DialogFragmentChangePicture extends DialogFragment {

    public static String DIALOG_FRAGMENT_CHANGE_PICTURE = "DialogFragmentChangePicture";
    private static final String CARD_DATA_DIALOG = "cardDataDialog";
    private NoteData noteData;


    public static DialogFragmentChangePicture newInstance(NoteData noteData) {
        Bundle args = new Bundle();
        DialogFragmentChangePicture fragment = new DialogFragmentChangePicture();
        args.putParcelable(CARD_DATA_DIALOG, noteData);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog_change_picture, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            noteData = getArguments().getParcelable(CARD_DATA_DIALOG);

            ImageView imageView = requireActivity().findViewById(R.id.fragment_dialog_change_picture_imageView);
            TextView textView = view.findViewById(R.id.textViewDialogFragmentChangePicture);
            textView.setTextSize(30F);
            textView.setText(R.string.change_sentiment);
            view.findViewById(R.id.radioButtonDialogFragment1).setOnClickListener(view1 -> {
                imageView.setImageResource(R.drawable.ic_sentiment_dissatisfied);
                noteData.setPicture(R.drawable.ic_sentiment_dissatisfied);
                dismiss();
            });
            view.findViewById(R.id.radioButtonDialogFragment2).setOnClickListener(view12 -> {
                imageView.setImageResource(R.drawable.ic_sentiment_satisfied);
                noteData.setPicture(R.drawable.ic_sentiment_satisfied);
                dismiss();
            });
            view.findViewById(R.id.radioButtonDialogFragment3).setOnClickListener(view1 -> {
                imageView.setImageResource(R.drawable.ic_sentiment_satisfied_alt);
                noteData.setPicture(R.drawable.ic_sentiment_satisfied_alt);
                dismiss();
            });
            view.findViewById(R.id.radioButtonDialogFragment4).setOnClickListener(view1 -> {
                imageView.setImageResource(R.drawable.ic_sentiment_very_dissatisfied);
                noteData.setPicture(R.drawable.ic_sentiment_very_dissatisfied);
                dismiss();
            });
            view.findViewById(R.id.radioButtonDialogFragment5).setOnClickListener(view1 -> {
                imageView.setImageResource(R.drawable.ic_sentiment_very_satisfied);
                noteData.setPicture(R.drawable.ic_sentiment_very_satisfied);
                dismiss();
            });
        }
    }
}

