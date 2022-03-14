package ru.geekbrains.myapplication.repository;

import java.util.Random;

import ru.geekbrains.myapplication.R;

// Класс конвертирует индексы картинок в id, и обратно id в индексы картинок
// (заменяем все идентификаторы на карту(массив) интов)
public class PictureOrColorIndexConverter {

    private static final Random random = new Random();
    private static final Object syncObj = new Object();

    private static final int[] picIndex = {
            R.drawable.ic_sentiment_dissatisfied,
            R.drawable.ic_sentiment_satisfied,
            R.drawable.ic_sentiment_satisfied_alt,
            R.drawable.ic_sentiment_very_dissatisfied,
            R.drawable.ic_sentiment_very_satisfied
    };

    private static final int[] clrIndex = {
            R.color.colorTextRed1,
            R.color.colorTextRed2,
            R.color.colorTextRed3,
            R.color.colorTextRed4,
            R.color.colorTextRed5
    };

    public static int randomPictureIndex() {
        synchronized (syncObj) {
            return picIndex[random.nextInt(picIndex.length)];
        }
    }

    public static int getPictureByIndex(int indexPicture) {
        if (indexPicture < 0 || indexPicture >= picIndex.length) {
            indexPicture = 0;
        }
        return picIndex[indexPicture];
    }

    public static int getIndexByPicture(int pictureIndex) {
        for (int i = 0; i < picIndex.length; i++) {
            if (picIndex[i] == pictureIndex) {
                return i;
            }
        }
        return 0;
    }

    public static int randomColorIndex() {
        synchronized (syncObj) {
            return clrIndex[random.nextInt(clrIndex.length)];
        }
    }

    public static int getColorByIndex(int indexColor) {
        if (indexColor < 0 || indexColor >= clrIndex.length) {
            indexColor = 0;
        }
        return clrIndex[indexColor];
    }

    public static int getIndexByColor(int colorIndex) {
        for (int i = 0; i < clrIndex.length; i++) {
            if (clrIndex[i] == colorIndex) {
                return i;
            }
        }
        return 0;
    }

}

