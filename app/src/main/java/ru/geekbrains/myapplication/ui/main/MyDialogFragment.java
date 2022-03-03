package ru.geekbrains.myapplication.ui.main;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import ru.geekbrains.myapplication.R;
import ru.geekbrains.myapplication.ui.main.InterfaceToast;

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

}

