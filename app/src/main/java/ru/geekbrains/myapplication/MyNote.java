package ru.geekbrains.myapplication;

// Создайте класс данных со структурой заметок: название заметки,
// описание заметки, дата создания и т. п.

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

public class MyNote implements Parcelable {

    private int noteIndex;

    /**
     * ключи для хранения даты в SharedPreferences
     */
    private static final String KEY_NOTE_YEAR = "key_note_year_";
    private static final String KEY_NOTE_MONTH = "key_note_monthOfYear_";
    private static final String KEY_NOTE_DAY = "";
    private static final String KEY_PREF = "note_date";

    public MyNote(int index) {
        this.noteIndex = index;
    }

    public int getNoteIndex() {
        return noteIndex;
    }

    public void setNoteIndex(int noteIndex) {
        this.noteIndex = noteIndex;
    }

    public String getNoteName(Context mContext) {
        return mContext.getResources().getStringArray(R.array.note_name)[noteIndex];
    }

    public String getNoteBody(Context mContext) {
        return mContext.getResources().getStringArray(R.array.note_description)[noteIndex];
    }

    public int getNoteDateYear(Context mContext) {
        SharedPreferences sp = mContext.getApplicationContext().getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE);
        return sp.getInt(KEY_NOTE_YEAR + noteIndex, -1);
    }

    public int getNoteDateMonth(Context mContext) {
        SharedPreferences sp = mContext.getApplicationContext().getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE);
        return sp.getInt(KEY_NOTE_MONTH + noteIndex, -1);
    }

    public int getNoteDateDay(Context mContext) {
        SharedPreferences sp = mContext.getApplicationContext().getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE);
        return sp.getInt(KEY_NOTE_DAY + noteIndex, -1);
    }

    /**
     * так как мы получаем год, месяц, день в одну операцию, то и сохраним их сразу
     * для хранения даты заметки, будем использовать SharedPreferences
     */
    public void setNoteDate(Context mContext, int year, int monthOfYear, int dayOfMonth) {
        SharedPreferences sp = mContext.getApplicationContext().getSharedPreferences(KEY_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(KEY_NOTE_YEAR + noteIndex, year);
        editor.putInt(KEY_NOTE_MONTH + noteIndex, monthOfYear);
        editor.putInt(KEY_NOTE_DAY + noteIndex, dayOfMonth);
        editor.apply();
    }

    protected MyNote(Parcel in) {
        noteIndex = in.readInt();
    }

    public static final Creator<MyNote> CREATOR = new Creator<MyNote>() {
        @Override
        public MyNote createFromParcel(Parcel in) {
            return new MyNote(in);
        }

        @Override
        public MyNote[] newArray(int size) {
            return new MyNote[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(noteIndex);
    }

}

