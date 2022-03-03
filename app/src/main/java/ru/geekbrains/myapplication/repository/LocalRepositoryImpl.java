package ru.geekbrains.myapplication.repository;

import android.content.res.Resources;
import android.content.res.TypedArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.geekbrains.myapplication.R;

public class LocalRepositoryImpl implements CardsSource {

    private final List<CardData> dateSource;
    private final Resources resources;

    public LocalRepositoryImpl(Resources resources) {
        dateSource = new ArrayList<>();
        this.resources = resources;
    }

    public LocalRepositoryImpl(ArrayList<CardData> cardDataArrayList, Resources resources) {
        dateSource = cardDataArrayList;
        this.resources = resources;
    }

    public LocalRepositoryImpl init() {
        String[] titles = resources.getStringArray(R.array.note_name);
        String[] descriptions = resources.getStringArray(R.array.note_description);
        TypedArray pictures = resources.obtainTypedArray(R.array.note_image);
        TypedArray colors = resources.obtainTypedArray(R.array.note_colors);

        for (int i = 0; i < titles.length; i++) {
            dateSource.add(new CardData(titles[i], descriptions[i],
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
    public List<CardData> getAllCardDAta() {
        return dateSource;
    }

    @Override
    public CardData getCardDAta(int position) {
        return dateSource.get(position);
    }

    @Override
    public void clearCardsData() {
        dateSource.clear();
    }

    @Override
    public void addCardData(CardData cardData) {
        dateSource.add(cardData);
    }

    @Override
    public void deleteCardDAta(int position) {
        dateSource.remove(position);
    }

    @Override
    public void updateCardDAta(int position, CardData newCardData) {
        dateSource.set(position, newCardData);
    }
}

