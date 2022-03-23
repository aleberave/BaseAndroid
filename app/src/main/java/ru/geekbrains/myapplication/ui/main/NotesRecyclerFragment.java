package ru.geekbrains.myapplication.ui.main;

import static ru.geekbrains.myapplication.repository.LocalSharedPreferencesRepositoryImpl.KEY_SP_2;
import static ru.geekbrains.myapplication.ui.main.MyDialogFragment.MY_DIALOG_FRAGMENT;
import static ru.geekbrains.myapplication.ui.editor.NewNoteFragment.NEW_NOTE_FRAGMENT;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import ru.geekbrains.myapplication.MainActivity;
import ru.geekbrains.myapplication.R;
import ru.geekbrains.myapplication.publisher.Observer;
import ru.geekbrains.myapplication.repository.LocalRepositoryImpl;
import ru.geekbrains.myapplication.repository.LocalSharedPreferencesRepositoryImpl;
import ru.geekbrains.myapplication.repository.NoteData;
import ru.geekbrains.myapplication.repository.NotesSource;
import ru.geekbrains.myapplication.repository.RemoteFirestoreRepositoryImpl;
import ru.geekbrains.myapplication.repository.RemoteFirestoreResponse;
import ru.geekbrains.myapplication.ui.editor.NewNoteFragment;
import ru.geekbrains.myapplication.ui.editor.NoteFragment;
import ru.geekbrains.myapplication.ui.settings.ThemeFragment;

public class NotesRecyclerFragment extends Fragment implements OnItemClickListener {

    private NotesAdapter notesAdapter;
    private NotesSource data;
    private RecyclerView recyclerView;
    private int currentPosition;
    private LocalSharedPreferencesRepositoryImpl localSharedPreferencesRepository;
    private RemoteFirestoreRepositoryImpl remoteFirestoreRepository;

    private static final String NOTES_DATA = "notesData";
    private static final String NOTES_DATA_ARRAYS = "notesDataArrays";

    public NotesRecyclerFragment() {
        // Required empty public constructor
    }

    public static NotesRecyclerFragment newInstance() {
        return new NotesRecyclerFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notes_recycler, container, false);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_cards, menu);
        inflater.inflate(R.menu.menu_notes_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
                ((MainActivity) requireActivity()).getNavigation().replaceFragment(R.id.layout_activity_main, ThemeFragment.newInstance(), true);
                break;
            }
            case (R.id.action_activity_exit): {
                requireActivity().finish();
                break;
            }
            case (R.id.action_add): {
                NewNoteFragment newNoteFragment = new NewNoteFragment();
                newNoteFragment.show(requireActivity().getSupportFragmentManager(), NEW_NOTE_FRAGMENT);
                return true;
            }
            case (R.id.action_clear): {
                data.clearNotesData();
                notesAdapter.notifyDataSetChanged();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        requireActivity().getMenuInflater().inflate(R.menu.menu_card, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        currentPosition = notesAdapter.getMenuPosition();
        switch (item.getItemId()) {
            case (R.id.action_update): {
                onItemClick(currentPosition);
                return true;
            }
            case (R.id.action_delete): {
                data.deleteNoteData(currentPosition);
                notesAdapter.notifyItemRemoved(currentPosition);
                notesAdapter.notifyDataSetChanged();
                return true;
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        setupSource(savedInstanceState);
        initAdapter();
        initRecycler(view);
        initRadioGroup(view, savedInstanceState);
        getSharedPreferences(view);
        if (savedInstanceState != null) { // при повороте экрана получаем сохраненную позицию из переменной
            currentPosition = savedInstanceState.getInt(NOTES_DATA);
        }

        onItemClick(currentPosition);
    }

    private void getSharedPreferences(@NonNull View view) {
        switch (getCurrentSource()) {
            case SOURCE_LOCAL: {
                ((RadioButton) view.findViewById(R.id.radioButton_local)).setChecked(true);
                break;
            }
            case SOURCE_SP: {
                ((RadioButton) view.findViewById(R.id.radioButton_SP)).setChecked(true);
                break;
            }
            case SOURCE_GF: {
                ((RadioButton) view.findViewById(R.id.radioButton_GF)).setChecked(true);
                break;
            }
        }
    }

    private void initRadioGroup(View view, Bundle savedInstanceState) {
        view.findViewById(R.id.radioButton_local).setOnClickListener(getListener(savedInstanceState));
        view.findViewById(R.id.radioButton_SP).setOnClickListener(getListener(savedInstanceState));
        view.findViewById(R.id.radioButton_GF).setOnClickListener(getListener(savedInstanceState));
    }

    static final int SOURCE_LOCAL = 1;
    static final int SOURCE_SP = 2;
    static final int SOURCE_GF = 3;

    private View.OnClickListener getListener(Bundle savedInstanceState) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case (R.id.radioButton_local): {
                        setCurrentSource(SOURCE_LOCAL);
                        break;
                    }
                    case (R.id.radioButton_SP): {
                        setCurrentSource(SOURCE_SP);
                        break;
                    }
                    case (R.id.radioButton_GF): {
                        setCurrentSource(SOURCE_GF);
                        break;
                    }
                }
                setupSource(savedInstanceState);
            }
        };
    }

    static String KEY_SP_S1 = "keyPref1";
    static String KEY_SP_S1_CELL1 = "s1Cell1";

    public int getCurrentSource() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(KEY_SP_S1, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(KEY_SP_S1_CELL1, SOURCE_LOCAL);
    }

    public void setCurrentSource(int currentSource) {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences(KEY_SP_S1, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_SP_S1_CELL1, currentSource);
        editor.apply();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NOTES_DATA, currentPosition); // сохраняем в адаптере позицию в переменную в onSaveInstanceState
        outState.putParcelableArrayList(NOTES_DATA_ARRAYS, (ArrayList<NoteData>) data.getAllNoteData()); // сохраняем все карточки из репозитория
    }

    private void setupSource(Bundle savedInstanceState) {
        switch (getCurrentSource()) {
            case SOURCE_LOCAL: {
                if (savedInstanceState != null) {
                    // получаем заметки после сохранения состояния из массива ресурсов
                    data = new LocalRepositoryImpl(
                            savedInstanceState.getParcelableArrayList(NOTES_DATA_ARRAYS),
                            requireContext().getResources());
                } else {
                    // получаем автоматически созданные заметки из массива ресурсов
//                    data = new LocalRepositoryImpl(requireContext().getResources()).init();

//                    не создаем заметки по умолчанию
                    data = new LocalRepositoryImpl();
                }
                initAdapter();
                break;
            }
            case SOURCE_SP: {
                // инициализируем уже объявленный репозиторий
                if (localSharedPreferencesRepository == null) {
                    localSharedPreferencesRepository = new LocalSharedPreferencesRepositoryImpl(requireActivity()
                            .getSharedPreferences(KEY_SP_2, Context.MODE_PRIVATE)).init();
                }

                data = localSharedPreferencesRepository;
                initAdapter();
                break;
            }
            case SOURCE_GF: {
//                инициализируем уже объявленный репозиторий
                if (remoteFirestoreRepository == null) {
                    remoteFirestoreRepository = new RemoteFirestoreRepositoryImpl().init(new RemoteFirestoreResponse() {
                        @Override
                        public void initialized(NotesSource notesSource) {
                            initAdapter();
                        }
                    });
                }

                data = remoteFirestoreRepository;
                initAdapter();
            }
            break;
        }
    }

    void initAdapter() {
        if (notesAdapter == null) {
            notesAdapter = new NotesAdapter(this);
        }
        notesAdapter.setData(data);
        notesAdapter.setOnItemClickListener(this);
    }

    void initRecycler(View view) {
        recyclerView = view.findViewById(R.id.recyclerView);
        // либо в макете либо в коде определяет расположение карточек
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true); // если все элементы списка одинаковые, ускоряет работу inflater по нудуванию макета
        recyclerView.setAdapter(notesAdapter);  // Adapter напалняет данными RecyclerView

        // анимация для карточки
        DefaultItemAnimator animator = new DefaultItemAnimator();
        animator.setChangeDuration(3000); // при изменении
        animator.setMoveDuration(7000); // при движении
        animator.setAddDuration(3000); // при добавлении
        animator.setRemoveDuration(1000); // при удалении
        recyclerView.setItemAnimator(animator);

        // разделительная полоса между карточками
        DividerItemDecoration itemDecoration = new DividerItemDecoration(requireContext(), linearLayoutManager.getOrientation());
        itemDecoration.setDrawable(Objects.requireNonNull(ResourcesCompat.getDrawable(getResources(), R.drawable.separator, view.getContext().getTheme())));
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onItemClick(int position) {
        // Передает адаптеру позицию нажатого элемента
        currentPosition = position;

        //1.создаём маленькую копию
        Observer observer = new Observer() {
            @Override
            public void receiveCardData(NoteData noteData) {
                //3. отписываемся от получения сообщения
                ((MainActivity) requireActivity()).getPublisher().unsubscribe(this);

                if (data.getAllNoteData().contains(noteData)) {
                    data.updateNoteData(position, noteData);
                    notesAdapter.notifyItemChanged(position);
                } else {
                    data.addNoteData(noteData);
//                перерисовать адаптер по размерам которые занимают все карточки,
//                на весь экран, при добавлении новой карточки
                    if (data.size() <= 2) {
                        initAdapter();
                    }
                    recyclerView.smoothScrollToPosition(data.size() - 1);
                    notesAdapter.notifyItemInserted(data.size() - 1);
                }
            }
        };
        //2. подписываемся на получение сообщения
        ((MainActivity) requireActivity()).getPublisher().subscribe(observer);
        getOrientation(position);
    }

    private void getOrientation(int position) {
        NoteFragment noteFragment;
        if (data.size() == 0) {
            noteFragment = new NoteFragment();
        } else {
            noteFragment = NoteFragment.newInstance(data.getNoteData(position));
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            ((MainActivity) requireActivity()).getNavigation().addFragment(R.id.container_fragment_notes, noteFragment, true);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE && data.size() > 0) {
            ((MainActivity) requireActivity()).getNavigation().addFragment(R.id.container_fragment_id_description_note, noteFragment, false);
        }
    }
}