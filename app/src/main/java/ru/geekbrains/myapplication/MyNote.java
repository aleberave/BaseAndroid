package ru.geekbrains.myapplication;

// Создайте класс данных со структурой заметок: название заметки,
// описание заметки, дата создания и т. п.

import android.os.Parcel;
import android.os.Parcelable;

public class MyNote implements Parcelable {

    private int index;

    protected MyNote(Parcel in) {
        index = in.readInt();
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public MyNote(int index) {
        this.index = index;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(index);
    }

}

