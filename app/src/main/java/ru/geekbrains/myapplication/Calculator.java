package ru.geekbrains.myapplication;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Locale;

public class Calculator implements Parcelable {

    private StringBuilder stringBuilder;
    private boolean result;
    private boolean operation;
    private Context mainActivity;
    private int counterDotSecond;
    private int counterDotFirst;
    private int indexOperation;

    public Calculator(boolean operation, boolean result, int counterDotFirst, int counterDotSecond, Context mainActivity) {
        if (!result) {
            stringBuilder = new StringBuilder();
            this.operation = operation;
            this.result = false;
            this.counterDotFirst = counterDotFirst;
            this.counterDotSecond = counterDotSecond;
            this.mainActivity = mainActivity;
        }
    }

    protected Calculator(Parcel in) {
        stringBuilder.append(in.readString());
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

    public void resultStringBuilder() {
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
        setResult(true);
        getStringBuilder().delete(0, getStringBuilder().capacity());
        int intResult = (int) Math.floor(res);
        double temp = res - intResult;
        if (temp > 0) {
            setStringBuilder(String.format(Locale.ROOT, "%.4f", res));
        } else {
            setStringBuilder(String.format(Locale.ROOT, "%d", intResult));
        }
    }

    public void getBeOrNotToBeDot() {
        String a = getStringBuilder().toString();
        if (!(a.length() == 0)) {
            String s = getStringBuilder().substring(getStringBuilder().length() - 1, getStringBuilder().length());
            if (s.equals(mainActivity.getString(R.string.zero)) || s.equals(mainActivity.getString(R.string.one)) ||
                    s.equals(mainActivity.getString(R.string.two)) || s.equals(mainActivity.getString(R.string.three)) ||
                    s.equals(mainActivity.getString(R.string.four)) || s.equals(mainActivity.getString(R.string.five)) ||
                    s.equals(mainActivity.getString(R.string.six)) || s.equals(mainActivity.getString(R.string.seven)) ||
                    s.equals(mainActivity.getString(R.string.eight)) || s.equals(mainActivity.getString(R.string.nine))) {
                if (getCounterDotFirst() < 1) {
                    increaseCounterDotFirst();
                    setStringBuilder(mainActivity.getString(R.string.dot));
                } else if (getCounterDotSecond() < 1) {
                    if (a.contains(mainActivity.getString(R.string.plus))
                            || a.contains(mainActivity.getString(R.string.minus))
                            || a.contains(mainActivity.getString(R.string.multiply))
                            || a.contains(mainActivity.getString(R.string.divide))) {
                        increaseCounterDotSecond();
                        setStringBuilder(mainActivity.getString(R.string.dot));
                    }
                }
            }
        }
    }

    public void doOperation(String operationSign) {
        if (getStringBuilder().length() != 0) {
            if (isOperation()) {
                setOperation(true);
                setStringBuilder(operationSign);
                indexOperation = getStringBuilder().length();
            }
        }
    }

    public void doNumberZero(String thisNumber) {
        if (isOperation()) {
            if (!getStringBuilder().toString().equals(thisNumber)) {
                setStringBuilder(thisNumber);
            }
        } else if (!isOperation()) {
            String strZero = getStringBuilder().substring(indexOperation);
            if (!strZero.equals(thisNumber)) {
                if (!getStringBuilder().toString().equals(thisNumber)) {
                    setStringBuilder(thisNumber);
                }
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(String.valueOf(stringBuilder));
    }
}

