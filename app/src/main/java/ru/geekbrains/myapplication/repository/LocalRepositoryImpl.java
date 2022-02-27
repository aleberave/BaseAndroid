package ru.geekbrains.myapplication.repository;

import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.geekbrains.myapplication.R;

public class LocalRepositoryImpl implements CardSource {

    private final List<CardData> dateSource;
    private final Resources resources;

    public LocalRepositoryImpl(Resources resources) {
        dateSource = new ArrayList<>();
        this.resources = resources;
    }

    public LocalRepositoryImpl init() {
        String[] titles = resources.getStringArray(R.array.note_name);
        String[] descriptions = resources.getStringArray(R.array.note_description);
        TypedArray pictures = resources.obtainTypedArray(R.array.note_image);

        for (int i = 0; i < titles.length; i++) {
            dateSource.add(new CardData(titles[i], descriptions[i],
                    pictures.getResourceId(i, 0), false,
                    Calendar.getInstance().getTime()));
        }
        pictures.recycle();
        return this;
    }

    @Override
    public int size() {
        return dateSource.size();
    }

    @Override
    public List<CardData> getAllCardDAta() {
        return dateSource;
    }

    @Override
    public CardData getCardDAta(int position) {
        return dateSource.get(position);
    }


//    private int noteIndex;
//
//    /**
//     * ключи для хранения даты в SharedPreferences
//     */
//    private static final String KEY_NOTE_YEAR = "key_note_year_";
//    private static final String KEY_NOTE_MONTH = "key_note_monthOfYear_";
//    private static final String KEY_NOTE_DAY = "";
//    private static final String KEY_PREF = "note_date";
//
//    public int getNoteIndex() {
//        return noteIndex;
//    }
//
//    public void setNoteIndex(int noteIndex) {
//        this.noteIndex = noteIndex;
//    }
//
//    public String getNoteName(Context mContext) {
//        return mContext.getResources().getStringArray(R.array.note_name)[noteIndex];
//    }
//
//    public String getNoteBody(Context mContext) {
//        return mContext.getResources().getStringArray(R.array.note_description)[noteIndex];
//    }
//
//    public int getNoteDateYear(Context mContext) {
//        SharedPreferences sp = mContext.getApplicationContext().getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE);
//        return sp.getInt(KEY_NOTE_YEAR + noteIndex, -1);
//    }
//
//    public int getNoteDateMonth(Context mContext) {
//        SharedPreferences sp = mContext.getApplicationContext().getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE);
//        return sp.getInt(KEY_NOTE_MONTH + noteIndex, -1);
//    }
//
//    public int getNoteDateDay(Context mContext) {
//        SharedPreferences sp = mContext.getApplicationContext().getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE);
//        return sp.getInt(KEY_NOTE_DAY + noteIndex, -1);
//    }
//
//    /**
//     * так как мы получаем год, месяц, день в одну операцию, то и сохраним их сразу
//     * для хранения даты заметки, будем использовать SharedPreferences
//     */
//    public void setNoteDate(Context mContext, int year, int monthOfYear, int dayOfMonth) {
//        SharedPreferences sp = mContext.getApplicationContext().getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putInt(KEY_NOTE_YEAR + noteIndex, year);
//        editor.putInt(KEY_NOTE_MONTH + noteIndex, monthOfYear);
//        editor.putInt(KEY_NOTE_DAY + noteIndex, dayOfMonth);
//        editor.apply();
//    }
}

