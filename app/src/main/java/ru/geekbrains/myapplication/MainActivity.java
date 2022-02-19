package ru.geekbrains.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_THEME_NAME = "key_pref";
    private static final String PREF_THEME_KEY = "key_pref_theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getAppTheme());
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            NotesFragment notesFragment = NotesFragment.newInstance();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_fragment_notes, notesFragment)
                    .commit();
        }

        Toolbar toolbar = findViewById(R.id.toolbar_activity);
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.teal_700, getTheme()));
        toolbar.setSubtitle(R.string.toolbar_subtitle);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        Для правильного отображения в ландшафтной ориентации или использовать
//        метод onCreate() во фрагменте DescriptionNoteFragment.
//         ищем фрагмент, который сидит в контейнере R.id.cities_container
        Fragment backStackFragment = getSupportFragmentManager()
                .findFragmentById(R.id.container_fragment_notes);
//        если такой есть, и он является CoatOfArmsFragment
        if (backStackFragment instanceof DescriptionNoteFragment) {
//            то сэмулируем нажатие кнопки Назад
            getSupportFragmentManager().popBackStack();
        }
    }

    protected int getAppTheme() {
        SharedPreferences sharedPref = getSharedPreferences(PREF_THEME_NAME, Context.MODE_PRIVATE);
        return sharedPref.getInt(PREF_THEME_KEY, R.style.Theme_MyApplication);
    }
}