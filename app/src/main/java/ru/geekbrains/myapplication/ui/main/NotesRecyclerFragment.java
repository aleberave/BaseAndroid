package ru.geekbrains.myapplication.ui.main;

import static ru.geekbrains.myapplication.ui.main.MyDialogFragment.MY_DIALOG_FRAGMENT;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ContextMenu;
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
import ru.geekbrains.myapplication.repository.CardData;
import ru.geekbrains.myapplication.repository.CardsSource;
import ru.geekbrains.myapplication.repository.LocalRepositoryImpl;
import ru.geekbrains.myapplication.ui.editor.CardFragment;
import ru.geekbrains.myapplication.ui.settings.ThemeFragment;

public class NotesRecyclerFragment extends Fragment implements OnItemClickListener {

    private NotesAdapter notesAdapter;
    private CardsSource data;
    private RecyclerView recyclerView;
    private int currentPosition;

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
        return inflater.inflate(R.layout.fragment_recycler, container, false);
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
                // при повороте экрана в репозитории LocalRepositoryImpl не сохраняет
                // поэтому в горизонтальной ориентации приложение падает
                // т.к. должна вывестись несохраненная карточка
                data.addCardData(new CardData("Title new note " + (data.size() + 1),
                        "Description new note " + (data.size() + 1),
                        R.drawable.ic_sentiment_satisfied, R.color.design_default_color_background, false,
                        Calendar.getInstance().getTime()));
                recyclerView.smoothScrollToPosition(data.size() - 1);
                notesAdapter.notifyItemInserted(data.size() - 1);
                return true;
            }
            case (R.id.action_clear): {
                data.clearCardsData();
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
                data.deleteCardDAta(currentPosition);
                notesAdapter.notifyItemRemoved(currentPosition);
                return true;
            }
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        initAdapter(savedInstanceState);
        initRecycler(view);
        if (savedInstanceState != null) { // при повороте экрана получаем сохраненную позицию из переменной
            currentPosition = savedInstanceState.getInt(NOTES_DATA);
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            onItemClick(currentPosition);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(NOTES_DATA, currentPosition); // сохраняем в адаптере позицию в переменную в onSaveInstanceState
        outState.putParcelableArrayList(NOTES_DATA_ARRAYS, (ArrayList<CardData>) data.getAllCardDAta()); // сохраняем все карточки из репозитория
    }

    void initAdapter(Bundle savedInstanceState) {
        notesAdapter = new NotesAdapter(this);
        if (savedInstanceState != null) {
            data = new LocalRepositoryImpl(savedInstanceState.getParcelableArrayList(NOTES_DATA_ARRAYS), requireContext().getResources());
        } else {
            data = new LocalRepositoryImpl(requireContext().getResources()).init();
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
        animator.setChangeDuration(3000);
        animator.setMoveDuration(7000);
        animator.setAddDuration(3000);
        animator.setRemoveDuration(1000);
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
        //3. отписываемся от получения сообщения
        Observer observer = new Observer() {
            @Override
            public void receiveCardData(CardData cardData) {
                //3. отписываемся от получения сообщения
                ((MainActivity) requireActivity()).getPublisher().unsubscribe(this);
                data.updateCardDAta(position, cardData);
                notesAdapter.notifyItemChanged(position);
            }
        };
        //2. подписываемся на получение сообщения
        ((MainActivity) requireActivity()).getPublisher().subscribe(observer);

        CardFragment cardFragment = CardFragment.newInstance(data.getCardDAta(position));
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_fragment_notes, cardFragment)
                    .addToBackStack(getString(R.string.empty)).commit();
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_fragment_id_description_note, cardFragment)
                    .commit();
        }
    }
}