package ru.geekbrains.myapplication;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class MyDialogFragment extends DialogFragment {

    public static String MY_DIALOG_FRAGMENT = "MyDialogFragment";

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        return new AlertDialog.Builder(requireContext())
                .setTitle(getResources().getString(R.string.please_rate_the_app))
                .setMessage(getResources().getString(R.string.want_to_rate))
                .setPositiveButton(getResources().getString(R.string.yes), (dialogInterface, i) -> {
                    ((InterfaceToast) requireActivity()).getToastMessage(getResources().getString(R.string.yes));
                    Uri address = Uri.parse("https://play.google.com/");
                    Intent intent = new Intent(Intent.ACTION_VIEW, address);
//                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH, address);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                    dismiss();
                })
                .setNegativeButton(getResources().getString(R.string.no), (dialogInterface, i) -> {
                    ((InterfaceToast) requireActivity()).getToastMessage(getResources().getString(R.string.no));
                    Uri mail = Uri.parse("mailto:");
                    Intent intent = new Intent(Intent.ACTION_VIEW, mail);
//                    Intent intent = new Intent(Intent.ACTION_SENDTO, mail);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                    dismiss();
                })
                .show();
    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//
//        View view = inflater.inflate(R.layout.fragment_dailog, null);
//        TextView textView = view.findViewById(R.id.textViewDialogFragment);
//        textView.setText(getResources().getString(R.string.please_rate_the_app));
//        view.findViewById(R.id.radioButtonDialogFragment1).setOnClickListener(view1 -> {
//            getToastMessage(getString(R.string.one));
//            dismiss();
//        });
//        view.findViewById(R.id.radioButtonDialogFragment2).setOnClickListener(view12 -> {
//            getToastMessage(getString(R.string.two));
//            dismiss();
//        });
//        view.findViewById(R.id.radioButtonDialogFragment3).setOnClickListener(view1 -> {
//            getToastMessage(getString(R.string.three));
//            dismiss();
//        });
//        view.findViewById(R.id.radioButtonDialogFragment4).setOnClickListener(view1 -> {
//            getToastMessage(getString(R.string.four));
//            dismiss();
//        });
//        view.findViewById(R.id.radioButtonDialogFragment5).setOnClickListener(view1 -> {
//            getToastMessage(getString(R.string.five));
//            dismiss();
//        });
//        return view;
//    }
//
//    void getToastMessage(String message) {
//        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
//    }
}

