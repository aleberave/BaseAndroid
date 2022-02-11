package ru.geekbrains.myapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    //    protected static final String FRAGMENT_TAG = "Tag Fragment Notes";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
//            При создании через тэг нужна проверка, что фрагмент существует;
//            NotesFragment notesFragment = (NotesFragment) getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
//            assert notesFragment != null;
//            android:tag="@string/tag_fragment_notes" (добавить в Linear в макет фрагмента)
//            <string name="tag_fragment_notes">Tag Fragment Notes</string> (добавить в ресурсы)

            NotesFragment notesFragment = NotesFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment_notes, notesFragment).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        Для правильного отображения в ландшафтной ориентации или использовать
//        метод onCreate() во фрагменте DescriptionNoteFragment.
//        // ищем фрагмент, который сидит в контейнере R.id.cities_container
//        Fragment backStackFragment = (Fragment) getSupportFragmentManager()
//                .findFragmentById(R.id.container_fragment_notes);
//        // если такой есть, и он является CoatOfArmsFragment
//        if (backStackFragment != null && backStackFragment instanceof DescriptionNoteFragment) {
//            //то сэмулируем нажатие кнопки Назад
//            onBackPressed();
//        }
    }
}