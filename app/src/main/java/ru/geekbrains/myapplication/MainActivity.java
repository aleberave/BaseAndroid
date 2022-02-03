package ru.geekbrains.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final HashSet<Button> buttons = new HashSet<>();
    private TextView textViewResult;
    private Calculator calculator;
    private final HashSet<RadioButton> radioButtons = new HashSet<>();

    private static final String PREF_NAME = "key_pref";
    private static final String PREF_THEME_KEY = "key_pref_theme";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getAppTheme());
        setContentView(R.layout.activity_main);

        init();
        for (Button button : buttons) {
            button.setOnClickListener(listener);
        }
        for (RadioButton radioButton : radioButtons) {
            radioButton.setOnClickListener(this);
        }

    }

    protected void setAppTheme(int codeStyle) {
        SharedPreferences sharedPref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(PREF_THEME_KEY, codeStyle);
        editor.apply();
    }

    protected int getAppTheme() {
        SharedPreferences sharedPref = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        return sharedPref.getInt(PREF_THEME_KEY, R.style.myThemeDefault);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("calculator", calculator);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        calculator = savedInstanceState.getParcelable("calculator");
        textViewResult.setText(calculator.getStringBuilder());
    }

    private void init() {
        buttons.add(findViewById(R.id.button0));
        buttons.add(findViewById(R.id.button1));
        buttons.add(findViewById(R.id.button2));
        buttons.add(findViewById(R.id.button3));
        buttons.add(findViewById(R.id.button4));
        buttons.add(findViewById(R.id.button5));
        buttons.add(findViewById(R.id.button6));
        buttons.add(findViewById(R.id.button7));
        buttons.add(findViewById(R.id.button8));
        buttons.add(findViewById(R.id.button9));
        buttons.add(findViewById(R.id.button_dot));
        buttons.add(findViewById(R.id.button_plus));
        buttons.add(findViewById(R.id.button_minus));
        buttons.add(findViewById(R.id.button_multiply));
        buttons.add(findViewById(R.id.button_divide));
        buttons.add(findViewById(R.id.button_equal));
        textViewResult = findViewById(R.id.textView_result);
        calculator = new Calculator(false, false, 0, 0, this);
        radioButtons.add(findViewById(R.id.radioButton_defaultTheme));
        radioButtons.add(findViewById(R.id.radioButton_orangeTheme));
        radioButtons.add(findViewById(R.id.radioButton_purpleTheme));
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (calculator.isResult()) {
                calculator.getStringBuilder().delete(0, calculator.getStringBuilder().capacity());
                calculator.setResult(false);
                calculator.setOperation(false);
                calculator.setCounterDotFirst(0);
                calculator.setCounterDotSecond(0);
            }
            switch (view.getId()) {
                case (R.id.button0): {
                    if (!calculator.getStringBuilder().toString().equals(getString(R.string.zero))) {
                        calculator.setStringBuilder(getString(R.string.zero));
                    }
                    break;
                }
                case (R.id.button1): {
                    calculator.setStringBuilder(getString(R.string.one));
                    break;
                }
                case (R.id.button2): {
                    calculator.setStringBuilder(getString(R.string.two));
                    break;
                }
                case (R.id.button3): {
                    calculator.setStringBuilder(getString(R.string.three));
                    break;
                }
                case (R.id.button4): {
                    calculator.setStringBuilder(getString(R.string.four));
                    break;
                }
                case (R.id.button5): {
                    calculator.setStringBuilder(getString(R.string.five));
                    break;
                }
                case (R.id.button6): {
                    calculator.setStringBuilder(getString(R.string.six));
                    break;
                }
                case (R.id.button7): {
                    calculator.setStringBuilder(getString(R.string.seven));
                    break;
                }
                case (R.id.button8): {
                    calculator.setStringBuilder(getString(R.string.eight));
                    break;
                }
                case (R.id.button9): {
                    calculator.setStringBuilder(getString(R.string.nine));
                    break;
                }
                case (R.id.button_dot): {
                    String a = calculator.getStringBuilder().toString();
                    if (a.length() == 0) {
                        break;
                    } else {
                        String s = calculator.getStringBuilder().substring(calculator.getStringBuilder().length() - 1, calculator.getStringBuilder().length());
                        if (s.equals(getString(R.string.zero)) || s.equals(getString(R.string.one)) ||
                                s.equals(getString(R.string.two)) || s.equals(getString(R.string.three)) ||
                                s.equals(getString(R.string.four)) || s.equals(getString(R.string.five)) ||
                                s.equals(getString(R.string.six)) || s.equals(getString(R.string.seven)) ||
                                s.equals(getString(R.string.eight)) || s.equals(getString(R.string.nine))) {
                            if (calculator.getCounterDotFirst() < 1) {
                                calculator.increaseCounterDotFirst();
                                calculator.setStringBuilder(getString(R.string.dot));
                            } else if (calculator.getCounterDotSecond() < 1)
                                if (a.contains(getString(R.string.plus))
                                        || a.contains(getString(R.string.minus))
                                        || a.contains(getString(R.string.multiply))
                                        || a.contains(getString(R.string.divide))) {
                                    calculator.increaseCounterDotSecond();
                                    calculator.setStringBuilder(getString(R.string.dot));
                                }
                        }
                    }
                    break;
                }
                case (R.id.button_plus): {
                    if (calculator.isOperation()) {
                        calculator.setOperation(true);
                        calculator.setStringBuilder(getString(R.string.plus));
                    }
                    break;
                }
                case (R.id.button_minus): {
                    if (calculator.isOperation()) {
                        calculator.setOperation(true);
                        calculator.setStringBuilder(getString(R.string.minus));
                    }
                    break;
                }
                case (R.id.button_multiply): {
                    if (calculator.isOperation()) {
                        calculator.setOperation(true);
                        calculator.setStringBuilder(getString(R.string.multiply));
                    }
                    break;
                }
                case (R.id.button_divide): {
                    if (calculator.isOperation()) {
                        calculator.setOperation(true);
                        calculator.setStringBuilder(getString(R.string.divide));
                    }
                    break;
                }
                case (R.id.button_equal): {
                    calculator.setResult(true);
                    double doubleResult = calculator.resultStringBuilder();
                    calculator.getStringBuilder().delete(0, calculator.getStringBuilder().capacity());
                    int intResult = (int) Math.floor(doubleResult);
                    double temp = doubleResult - intResult;
                    if (temp > 0) {
                        calculator.setStringBuilder(String.format(Locale.ROOT, "%.4f", doubleResult));
                    } else {
                        calculator.setStringBuilder(String.format(Locale.ROOT, "%d", intResult));
                    }
                    break;
                }
            }
            textViewResult.setText(calculator.getStringBuilder());
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.radioButton_defaultTheme): {
                setAppTheme(R.style.myThemeDefault);
                break;
            }
            case (R.id.radioButton_orangeTheme): {
                setAppTheme(R.style.myThemeOrange);
                break;
            }
            case (R.id.radioButton_purpleTheme): {
                setAppTheme(R.style.myThemePurple);
                break;
            }
        }
        recreate();
    }
}