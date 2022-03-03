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
import ru.geekbrains.myapplication.repository.CardData;

public class CardFragment extends Fragment {

    private static final String CARD_DATA = "cardData";
    private CardData cardData;
    private DatePickerRecyclerFragment datePickerFragment;

    public static CardFragment newInstance(CardData cardData) {
        CardFragment fragment = new CardFragment();
        Bundle args = new Bundle();
        args.putParcelable(CARD_DATA, cardData);
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
                cardData.setDate(Calendar.getInstance().getTime());
                datePickerFragment = DatePickerRecyclerFragment.newInstance(cardData);
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
            cardData = getArguments().getParcelable(CARD_DATA);
            EditText title = (EditText) view.findViewById(R.id.fragment_card_input_title);
            EditText description = (EditText) view.findViewById(R.id.fragment_card_input_description);
            ImageView imageView = view.findViewById(R.id.fragment_dialog_change_picture_imageView);

            title.setTextSize(30f);
            title.setText(cardData.getTitle());

            imageView.setImageResource(cardData.getPicture());
            imageView.setOnLongClickListener(view1 -> {
                DialogFragmentChangePicture dialogFragmentChangePicture = DialogFragmentChangePicture.newInstance(cardData);
                dialogFragmentChangePicture.show(requireActivity().getSupportFragmentManager(), DIALOG_FRAGMENT_CHANGE_PICTURE);
                return false;
            });

            description.setTextSize(30f);
            description.setText(cardData.getDescription());

            datePickerFragment = DatePickerRecyclerFragment.newInstance(cardData);
            getChildFragmentManager().beginTransaction()
                    .add(R.id.container_date_picker_fragment, datePickerFragment).commit();

            view.findViewById(R.id.buttonSave).setOnClickListener(it -> {
                cardData.setLike(true);
                cardData.setTitle(title.getText().toString());
                cardData.setDescription(description.getText().toString());

                ((MainActivity) requireActivity()).getPublisher().sendCardDate(cardData);
                ((MainActivity) requireActivity()).getSupportFragmentManager().popBackStack();

                title.setFocusable(false);
                description.setFocusable(false);
                requireActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            });
        }

    }

}