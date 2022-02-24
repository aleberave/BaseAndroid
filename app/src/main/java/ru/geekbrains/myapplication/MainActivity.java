package ru.geekbrains.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements InterfaceToast {

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

    public void getSnackBar(String message) {
        View view = findViewById(R.id.layout_activity_main);
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

    public void getToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected int getAppTheme() {
        SharedPreferences sharedPref = getSharedPreferences(PREF_THEME_NAME, Context.MODE_PRIVATE);
        return sharedPref.getInt(PREF_THEME_KEY, R.style.Theme_MyApplication);
    }

    @Override
    public void finish() {
        super.finish();
        Toast.makeText(getApplicationContext(), getString(R.string.finished), Toast.LENGTH_SHORT).show();
    }

    boolean firstClick = true;

    @Override
    public void onBackPressed() {
        Fragment backStackFragment = getSupportFragmentManager().findFragmentById(R.id.container_fragment_notes);
        if (firstClick && (backStackFragment instanceof NotesFragment)) {
            firstClick = false;
            new DialogFragmentExit().show(getSupportFragmentManager(), DialogFragmentExit.DIALOG_FRAGMENT_EXIT);
        } else {
            super.onBackPressed();
        }
    }
}