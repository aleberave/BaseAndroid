package ru.geekbrains.myapplication.ui.main;

import static ru.geekbrains.myapplication.repository.LocalSharedPreferencesRepositoryImpl.KEY_SP_2;
import static ru.geekbrains.myapplication.ui.main.MyDialogFragment.MY_DIALOG_FRAGMENT;

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
import java.util.Calendar;
import java.util.Objects;

import ru.geekbrains.myapplication.MainActivity;
import ru.geekbrains.myapplication.R;
import ru.geekbrains.myapplication.publisher.Observer;
import ru.geekbrains.myapplication.repository.LocalRepositoryImpl;
import ru.geekbrains.myapplication.repository.LocalSharedPreferencesRepositoryImpl;
import ru.geekbrains.myapplication.repository.NoteData;
import ru.geekbrains.myapplication.repository.NotesSource;
import ru.geekbrains.myapplication.repository.PictureOrColorIndexConverter;
import ru.geekbrains.myapplication.repository.RemoteFirestoreRepositoryImpl;
import ru.geekbrains.myapplication.repository.RemoteFirestoreResponse;
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
            case (R.id.action_add): {
//                TODO доделать
//                Нажимая на плюсик открывает окно с редактирование заметки.
//                Только после заполнения полей добавлять заметку.
//                Возвращать ответ через Publisher-Observer.
                data.addNoteData(new NoteData("Title new note " + (data.size() + 1),
                        "Description new note " + (data.size() + 1),
                        PictureOrColorIndexConverter.randomPictureIndex(),
                        PictureOrColorIndexConverter.randomColorIndex(),
                        false,
                        Calendar.getInstance().getTime()));
//                перересовать адаптер по размерам которые занимают все карточки,
//                на весь экран, при добавлении новой карточки
                if (data.size() <= 2)
                    initAdapter();
                recyclerView.smoothScrollToPosition(data.size() - 1);
                notesAdapter.notifyItemInserted(data.size() - 1);
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
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            onItemClick(currentPosition);
        }

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
                if (localSharedPreferencesRepository != null && localSharedPreferencesRepository.size() != 0) {
                    // получаем заметки из SP-репозитория
                    data = localSharedPreferencesRepository;
                } else if (remoteFirestoreRepository != null && remoteFirestoreRepository.size() != 0) {
                    // получаем заметки из GF-репозитория
                    data = remoteFirestoreRepository;
                } else if (savedInstanceState != null) {
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

                data = getDataForRepository(localSharedPreferencesRepository);
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

                // когда асинхронный запрос, необходимо сохранять и возвращать состояние
                if (savedInstanceState != null) {
                    data = new RemoteFirestoreRepositoryImpl(savedInstanceState.getParcelableArrayList(NOTES_DATA_ARRAYS));
                    initAdapter();
                    // данные из удаленной БД
                } else if (remoteFirestoreRepository != null) {
                    data = getDataForRepository(remoteFirestoreRepository);
                    initAdapter();
                }
                break;
            }
        }

    }

    private NotesSource getDataForRepository(NotesSource repository) {
        if (repository != null) {
//                    заполняем данными репозиторий
            if (data != null && data.size() > 0 && repository.size() == 0) {
                for (int i = 0; i < data.size(); i++) {
                    repository.addNoteData(data.getNoteData(i));
                }
//                        проверяем на совпадение названия заметки
//                        и обновляем заметки из временного в GF - репозиторий
            } else if (data != null && repository.size() != 0) {
                if (repository.size() == data.size()) {
                    for (int i = 0; i < data.size(); i++) {
                        if (repository.getNoteData(i) != null && data.getNoteData(i) != null) {
                            if (!repository.getNoteData(i).getTitle().equals(data.getNoteData(i).getTitle())) {
                                repository.addNoteData(data.getNoteData(i));
                            } else {
                                repository.updateNoteData(i, data.getNoteData(i));
                            }
                        }
                    }
                } else if (repository.size() != data.size()) {
                    int counter = 0;
                    for (int i = 0; i < data.size(); i++) {
                        for (int j = 0; j < repository.size(); j++) {
                            if (repository.getNoteData(j) != null && data.getNoteData(i) != null) {
                                if (!(repository.getNoteData(j).getTitle().equals(data.getNoteData(i).getTitle()))) {
                                    counter++;
                                } else {
                                    repository.updateNoteData(i, data.getNoteData(i));
                                }
                            }
                        }
                        if (counter == repository.size()) {
                            repository.addNoteData(data.getNoteData(i));
                        }
                        counter = 0;
                    }
                }
            }
        }
        return repository;
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

        // анимация при обновлении и удалении карточки
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
        currentPosition = position;

        // Передает адаптеру позицию нажатого элемента
        //1.создаём маленькую копию
        Observer observer = new Observer() {
            @Override
            public void receiveCardData(NoteData cardData) {
                //3. отписываемся от получения сообщения
                ((MainActivity) requireActivity()).getPublisher().unsubscribe(this);
                data.updateNoteData(position, cardData);
                notesAdapter.notifyItemChanged(position);
            }
        };
        //2. подписываемся на получение сообщения
        ((MainActivity) requireActivity()).getPublisher().subscribe(observer);

        NoteFragment noteFragment;
        if (data.size() == 0) {
            noteFragment = new NoteFragment();
        } else {
            noteFragment = NoteFragment.newInstance(data.getNoteData(position));
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_fragment_notes, noteFragment)
                    .addToBackStack(getString(R.string.empty)).commit();
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_fragment_id_description_note, noteFragment)
                    .commit();
        }
    }
}