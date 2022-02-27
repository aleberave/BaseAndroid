package ru.geekbrains.myapplication;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DescriptionNoteFragment extends Fragment {

    private static final String NOTE_KEY = "current_city_key";
    private MyNote myNote;
    private EditText editText;
    private ImageView imageView;
    private StringBuilder stringBuilder = new StringBuilder();

    public static DescriptionNoteFragment newInstance(MyNote myNote) {
        DescriptionNoteFragment fragment = new DescriptionNoteFragment();
        Bundle args = new Bundle();
        args.putParcelable(NOTE_KEY, myNote);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        if (this.isVisible()) {
            inflater.inflate(R.menu.menu_description_fragment, menu);
            menu.findItem(R.id.action_activity_exit).setVisible(false);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_description_delete_date): {
                myNote.setNoteDate(requireContext(), -1, -1, -1);
                editText.setText(myNote.getNoteBody(requireContext()));
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_description_note_constraint, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            myNote = getArguments().getParcelable(NOTE_KEY);
        }
        initView(view);
        getPopupMenu();
    }

    private void initView(View view) {
        imageView = view.findViewById(R.id.fragment_description_imageView);
        TypedArray images = getResources().obtainTypedArray(R.array.note_image);
        imageView.setImageResource(images.getResourceId(myNote.getNoteIndex(), R.drawable.ic_sentiment_dissatisfied));
        images.recycle();

        DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(myNote);
        getChildFragmentManager().beginTransaction()
                .add(R.id.container_date_picker_fragment, datePickerFragment).commit();

        editText = view.findViewById(R.id.fragment_description_editText);
        editText.setTextSize(30f);

        final String[] date;
        if (myNote.getNoteDateDay(requireContext()) > 0 &&
                myNote.getNoteDateMonth(requireContext()) > 0 &&
                myNote.getNoteDateYear(requireContext()) > 0) {
            date = new String[]{myNote.getNoteDateDay(requireContext()) + getString(R.string.divided) +
                    (myNote.getNoteDateMonth(requireContext()) + 1) + getString(R.string.divided) +
                    myNote.getNoteDateYear(requireContext())};
            stringBuilder = stringBuilder.append(date[0]).append(getString(R.string.new_line))
                    .append(myNote.getNoteBody(requireContext()));
        } else {
            stringBuilder = stringBuilder.append(myNote.getNoteBody(requireContext()));
        }
        editText.setText(stringBuilder);
    }

    private void getPopupMenu() {
        imageView.setOnLongClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(requireContext(), view, Gravity.CENTER_VERTICAL);
            requireActivity().getMenuInflater().inflate(R.menu.menu_description_popup, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                if (menuItem.getItemId() == R.id.action_popup_delete) {
                    myNote.setNoteDate(requireContext(), -1, -1, -1);
                    editText.setText(myNote.getNoteBody(requireContext()));
                    return true;
                }
                return false;
            });
            popupMenu.show();
            return false;
        });
    }
}