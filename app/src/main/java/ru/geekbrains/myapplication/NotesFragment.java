package ru.geekbrains.myapplication;

import static ru.geekbrains.myapplication.MyDialogFragment.MY_DIALOG_FRAGMENT;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NotesFragment extends Fragment {

    protected static final String CURRENT_NOTE_KEY = "current note key";
    private MyNote myNote;

    public NotesFragment() {
    }

    public static NotesFragment newInstance() {
        return new NotesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_notes_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.action_activity_rate_app): {
                MyDialogFragment myDialogFragment = new MyDialogFragment();
                myDialogFragment.show(requireActivity().getSupportFragmentManager(), MY_DIALOG_FRAGMENT);
                break;
            }
            case (R.id.action_activity_theme): {
                ThemeFragment themeFragment = ThemeFragment.newInstance();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.layout_activity_main, themeFragment)
                        .addToBackStack(getString(R.string.empty)).commit();
                break;
            }
            case (R.id.action_activity_exit): {
                requireActivity().finish();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CURRENT_NOTE_KEY, myNote);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            myNote = savedInstanceState.getParcelable(CURRENT_NOTE_KEY);
        } else {
            myNote = new MyNote(0);
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            DescriptionNoteFragment descriptionNoteFragment = DescriptionNoteFragment.newInstance(myNote);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_fragment_id_description_note, descriptionNoteFragment).commit();
        }
        getChooseTheme(view);
        initView(view);
    }

    private void getChooseTheme(View view) {
        view.findViewById(R.id.button_choose_a_theme).setOnClickListener(view1 -> {
            ThemeFragment themeFragment = ThemeFragment.newInstance();
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.layout_activity_main, themeFragment)
                        .addToBackStack(requireActivity().getString(R.string.add_to_back_stack_empty)).commit();
            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .add(R.id.layout_activity_main, themeFragment)
                        .addToBackStack(requireActivity().getString(R.string.add_to_back_stack_empty)).commit();
            }
        });
    }

    private void initView(View view) {
        LinearLayout linearLayout = (LinearLayout) view;
        String[] notesName = getResources().getStringArray(R.array.note_name);
        for (int i = 0; i < notesName.length; i++) {
            TextView textView = new TextView(getContext());
            textView.setTextSize(30f);
            textView.setText(notesName[i]);
            linearLayout.addView(textView);

            final int finalI = i;
            textView.setOnClickListener(view1 -> {
                myNote.setNoteIndex(finalI);
                DescriptionNoteFragment descriptionNoteFragment = DescriptionNoteFragment.newInstance(myNote);
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.container_fragment_notes, descriptionNoteFragment)
                            .addToBackStack(requireActivity().getString(R.string.add_to_back_stack_empty)).commit();
                } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    requireActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container_fragment_id_description_note, descriptionNoteFragment)
                            .commit();
                }
            });
        }
    }

}