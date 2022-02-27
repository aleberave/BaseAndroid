package ru.geekbrains.myapplication.ui;

import static ru.geekbrains.myapplication.MyDialogFragment.MY_DIALOG_FRAGMENT;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import ru.geekbrains.myapplication.DescriptionNoteFragment;
import ru.geekbrains.myapplication.MyDialogFragment;
import ru.geekbrains.myapplication.MyNote;
import ru.geekbrains.myapplication.R;
import ru.geekbrains.myapplication.ThemeFragment;
import ru.geekbrains.myapplication.repository.LocalRepositoryImpl;

public class NotesRecyclerFragment extends Fragment implements OnItemClickListener {

    NotesAdapter notesAdapter;
    protected static final String CURRENT_NOTE_KEY = "current note key";
    private MyNote myNote;

    public NotesRecyclerFragment() {
        // Required empty public constructor
    }

    public static NotesRecyclerFragment newInstance() {
        NotesRecyclerFragment fragment = new NotesRecyclerFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler, container, false);
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

        initAdapter();
        initRecycler(view);
    }

    void initAdapter() {
        notesAdapter = new NotesAdapter();
        LocalRepositoryImpl localRepositoryImpl = new LocalRepositoryImpl(requireContext().getResources());
        notesAdapter.setData(localRepositoryImpl.init());
        notesAdapter.setOnItemClickListener(this);
    }

    void initRecycler(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        //        либо в макете либо в коде
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true); // если все элементы списка одинаковые, ускоряет работу inflater по нудуванию макета
        recyclerView.setAdapter(notesAdapter);  // Adapter напалняет данными RecyclerView

        // азделительная полоса между карточками
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), linearLayoutManager.getOrientation());
        itemDecoration.setDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.separator, view.getContext().getTheme())));
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(CURRENT_NOTE_KEY, myNote);
    }

    @Override
    public void onItemClick(int position) {
        myNote.setNoteIndex(position);
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
    }
}