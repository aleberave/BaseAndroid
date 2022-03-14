package ru.geekbrains.myapplication.repository;

import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.geekbrains.myapplication.R;

public class LocalRepositoryImpl implements NotesSource {

    private final List<NoteData> dateSource;
    private final Resources resources;

    public LocalRepositoryImpl(Resources resources) {
        dateSource = new ArrayList<>();
        this.resources = resources;
    }

    public LocalRepositoryImpl(ArrayList<NoteData> noteDataArrayList, Resources resources) {
        dateSource = noteDataArrayList;
        this.resources = resources;
    }

    public LocalRepositoryImpl() {
        dateSource = new ArrayList<>();
        resources = null;
    }

    public LocalRepositoryImpl init() {
        String[] titles = resources.getStringArray(R.array.note_name);
        String[] descriptions = resources.getStringArray(R.array.note_description);
        TypedArray pictures = resources.obtainTypedArray(R.array.note_image);
        TypedArray colors = resources.obtainTypedArray(R.array.note_colors);

        for (int i = 0; i < titles.length; i++) {
            dateSource.add(new NoteData(titles[i], descriptions[i],
                    pictures.getResourceId(i, 0),
                    colors.getResourceId(i, 0), false,
                    Calendar.getInstance().getTime()));
        }
        pictures.recycle();
        colors.recycle();
        return this;
    }

    @Override
    public int size() {
        return dateSource.size();
    }

    @Override
    public List<NoteData> getAllNoteData() {
        return dateSource;
    }

    @Override
    public NoteData getNoteData(int position) {
        return dateSource.get(position);
    }

    @Override
    public void clearNotesData() {
        dateSource.clear();
    }

    @Override
    public void addNoteData(NoteData noteData) {
        dateSource.add(noteData);
    }

    @Override
    public void deleteNoteData(int position) {
        dateSource.remove(position);
    }

    @Override
    public void updateNoteData(int position, NoteData newNoteData) {
        dateSource.set(position, newNoteData);
    }
}

