package ru.geekbrains.myapplication;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

public class Calculator implements Parcelable {

    private StringBuilder stringBuilder;
    private boolean result;
    private boolean operation;
    private Context mainActivity;
    private int counterDotSecond;
    private int counterDotFirst;

    public Calculator(boolean operation, boolean result, int counterDotFirst, int counterDotSecond, Context mainActivity) {
        if (!result) {
            stringBuilder = new StringBuilder();
            this.result = false;
            this.operation = operation;
            this.mainActivity = mainActivity;
            this.counterDotFirst = counterDotFirst;
            this.counterDotSecond = counterDotSecond;
        }
    }

    protected Calculator(Parcel in) {
        stringBuilder.append(in.readString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            result = in.readBoolean();
            operation = in.readBoolean();
        }
        counterDotFirst = in.readInt();
        counterDotSecond = in.readInt();
    }

    public static final Creator<Calculator> CREATOR = new Creator<Calculator>() {
        @Override
        public Calculator createFromParcel(Parcel in) {
            return new Calculator(in);
        }

        @Override
        public Calculator[] newArray(int size) {
            return new Calculator[size];
        }
    };

    public int getCounterDotFirst() {
        return counterDotFirst;
    }

    public void setCounterDotFirst(int counterDotFirst) {
        this.counterDotFirst = counterDotFirst;
    }

    public void increaseCounterDotFirst() {
        counterDotFirst++;
    }

    public int getCounterDotSecond() {
        return counterDotSecond;
    }

    public void setCounterDotSecond(int counterDotSecond) {
        this.counterDotSecond = counterDotSecond;
    }

    public void increaseCounterDotSecond() {
        counterDotSecond++;
    }

    public boolean isOperation() {
        return !operation;
    }

    public void setOperation(boolean operation) {
        this.operation = operation;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    public void setStringBuilder(String string) {
        stringBuilder.append(string);
    }

    public Double resultStringBuilder() {
        String s = this.getStringBuilder().toString();
        String[][] strings = new String[4][];
        strings[0] = s.split(mainActivity.getString(R.string.split_plus));
        strings[1] = s.split(mainActivity.getString(R.string.split_minus));
        strings[2] = s.split(mainActivity.getString(R.string.split_multiply));
        strings[3] = s.split(mainActivity.getString(R.string.split_divide));
        int count = 0;
        for (int i = 0; i < strings.length; i++) {
            if (strings[i].length > 1) {
                count = i;
            }
        }
        double res = 0;
        switch (count) {
            case 0: {
                res = Double.parseDouble(String.valueOf(strings[count][0])) +
                        Double.parseDouble(String.valueOf(strings[count][1]));
                break;
            }
            case 1: {
                res = Double.parseDouble(String.valueOf(strings[count][0])) -
                        Double.parseDouble(String.valueOf(strings[count][1]));
                break;
            }
            case 2: {
                res = Double.parseDouble(String.valueOf(strings[count][0])) *
                        Double.parseDouble(String.valueOf(strings[count][1]));
                break;
            }
            case 3: {
                res = Double.parseDouble(String.valueOf(strings[count][0])) /
                        Double.parseDouble(String.valueOf(strings[count][1]));
                break;
            }
        }
        return res;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(String.valueOf(stringBuilder));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            parcel.writeBoolean(result);
            parcel.writeBoolean(operation);
        }
        parcel.writeInt(counterDotFirst);
        parcel.writeInt(counterDotSecond);
    }
}

