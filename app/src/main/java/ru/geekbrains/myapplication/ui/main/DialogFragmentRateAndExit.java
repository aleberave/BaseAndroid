package ru.geekbrains.myapplication.ui.main;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.DialogFragment;

import ru.geekbrains.myapplication.R;

public class DialogFragmentRateAndExit extends DialogFragment {

    public final String CHANNEL_ID = "1";
    public static String DIALOG_FRAGMENT_EXIT = "DialogFragmentExit";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dailog, null);
        TextView textView = view.findViewById(R.id.textViewDialogFragment);
        textView.setText(getResources().getString(R.string.please_rate_the_app));
        view.findViewById(R.id.radioButtonDialogFragment1).setOnClickListener(view1 -> {
            showSnackBarAndPush(1);
        });
        view.findViewById(R.id.radioButtonDialogFragment2).setOnClickListener(view12 -> {
            showSnackBarAndPush(2);
        });
        view.findViewById(R.id.radioButtonDialogFragment3).setOnClickListener(view1 -> {
            showSnackBarAndPush(3);
        });
        view.findViewById(R.id.radioButtonDialogFragment4).setOnClickListener(view1 -> {
            showSnackBarAndPush(4);
        });
        view.findViewById(R.id.radioButtonDialogFragment5).setOnClickListener(view1 -> {
            showSnackBarAndPush(5);
        });
        return view;
    }

    private void showSnackBarAndPush(int i) {
        ((InterfaceToast) requireActivity()).getSnackBar(getString(R.string.thanks));
        showPushNotification(i);
        dismiss();
    }

    void showPushNotification(int i) {
        //NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
        NotificationManager notificationManager = (NotificationManager) requireActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "CHANNEL1", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(getString(R.string.description_channel));
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // Будет написано в самом пуше
        Notification notification = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setContentTitle(getString(R.string.get_the_rate))
                .setContentText(getString(R.string.rate_space) + i + getString(R.string.dot_thanks))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setPriority(Notification.PRIORITY_HIGH)
                .build();

        notificationManager.notify(1, notification);
    }

}

