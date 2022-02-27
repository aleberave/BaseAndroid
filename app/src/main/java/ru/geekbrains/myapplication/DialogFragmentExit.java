package ru.geekbrains.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import ru.geekbrains.myapplication.ui.InterfaceToast;

public class DialogFragmentExit extends DialogFragment {

    public static String DIALOG_FRAGMENT_EXIT = "DialogFragmentExit";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dailog, null);
        TextView textView = view.findViewById(R.id.textViewDialogFragment);
        textView.setText(getResources().getString(R.string.please_rate_the_app));
        view.findViewById(R.id.radioButtonDialogFragment1).setOnClickListener(view1 -> {
            ((InterfaceToast) requireActivity()).getSnackBar(getString(R.string.thanks));
            dismiss();
        });
        view.findViewById(R.id.radioButtonDialogFragment2).setOnClickListener(view12 -> {
            ((InterfaceToast) requireActivity()).getSnackBar(getString(R.string.thanks));
            dismiss();
        });
        view.findViewById(R.id.radioButtonDialogFragment3).setOnClickListener(view1 -> {
            ((InterfaceToast) requireActivity()).getSnackBar(getString(R.string.thanks));
            dismiss();
        });
        view.findViewById(R.id.radioButtonDialogFragment4).setOnClickListener(view1 -> {
            ((InterfaceToast) requireActivity()).getSnackBar(getString(R.string.thanks));
            dismiss();
        });
        view.findViewById(R.id.radioButtonDialogFragment5).setOnClickListener(view1 -> {
            ((InterfaceToast) requireActivity()).getSnackBar(getString(R.string.thanks));
            dismiss();
        });
        return view;
    }

}

