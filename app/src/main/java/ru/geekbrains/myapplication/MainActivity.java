package ru.geekbrains.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    private final HashSet<Button> buttons = new HashSet<>();
    private TextView textViewResult;
    private Calculator calculator;
    public static int REQUEST_CODE = 999;
    public static String MAIN_KEY = "main_key";
    public static String INTENT_ACTIVITY_KEY = "intent_activity";

    private static final String SP_NAME = "name_sp";
    private static final String SP_KEY = "name_sp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getAppTheme());
        setContentView(R.layout.activity_main);

        initView();
        initListeners();

        getIntentAcrossManifest();
    }

    private void getIntentAcrossManifest() {
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            textViewResult.setText(getString(R.string.empty));
            Log.e("BUNDLE", "NULL");
        } else if (bundle.getString(getString(R.string.ru_geekbrains_myapplication_MainActivity)) != null) {
            textViewResult.setText(bundle.getString(getString(R.string.ru_geekbrains_myapplication_MainActivity)));
        }
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

    private void initView() {
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
        buttons.add(findViewById(R.id.button_choose_a_theme));
        buttons.add(findViewById(R.id.button_to_intent_activity));

        textViewResult = findViewById(R.id.textView_result);
        calculator = new Calculator(false, false, 0, 0, this);
    }

    public void initListeners() {
        View.OnClickListener listener = view -> {
            if (calculator.isResult()) {
                calculator.getStringBuilder().delete(0, calculator.getStringBuilder().capacity());
                calculator.setResult(false);
                calculator.setOperation(false);
                calculator.setCounterDotFirst(0);
                calculator.setCounterDotSecond(0);
            }
            switch (view.getId()) {
                case (R.id.button0): {
                    calculator.doNumberZero(getString(R.string.zero));
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
                    calculator.getBeOrNotToBeDot();
                    break;
                }
                case (R.id.button_plus): {
                    calculator.doOperation(getString(R.string.plus));
                    break;
                }
                case (R.id.button_minus): {
                    calculator.doOperation(getString(R.string.minus));
                    break;
                }
                case (R.id.button_multiply): {
                    calculator.doOperation(getString(R.string.multiply));
                    break;
                }
                case (R.id.button_divide): {
                    calculator.doOperation(getString(R.string.divide));
                    break;
                }
                case (R.id.button_equal): {
                    calculator.resultStringBuilder();
                    break;
                }
                case (R.id.button_choose_a_theme): {
                    Intent intent = new Intent(MainActivity.this, ThemeActivity.class);
                    startActivityIfNeeded(intent, REQUEST_CODE);
                    break;
                }
                case (R.id.button_to_intent_activity): {
                    Intent intent = new Intent(getString(R.string.ru_geekbrains_myapplication_IntentActivity));
                    if (calculator.getStringBuilder() != null) {
                        intent.putExtra(INTENT_ACTIVITY_KEY, calculator.getStringBuilder().toString());
                        calculator.setStringBuilder(getResources().getString(R.string.empty));
                    }
                    startActivity(intent);
                    break;
                }
            }
            textViewResult.setText(calculator.getStringBuilder());
        };

        for (Button button : buttons) {
            button.setOnClickListener(listener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                setAppTheme(data.getIntExtra(MAIN_KEY, R.style.myThemePurple));
                recreate();
            }
        }
    }

    protected void setAppTheme(int codeStyle) {
        SharedPreferences sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(SP_KEY, codeStyle);
        editor.apply();
    }

    protected int getAppTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        return sharedPreferences.getInt(SP_KEY, R.style.myThemeDefault);
    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayout linearLayout = findViewById(R.id.main_linear_layout);
        if (linearLayout.getBackground() == null)
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                linearLayout.setBackgroundResource(R.drawable.sunset_purple_sky_beach_sand_footprints_shore_water);
            }
    }
}