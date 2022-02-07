package ru.geekbrains.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.HashSet;

public class ThemeActivity extends AppCompatActivity {

    private final HashSet<RadioButton> radioButtons = new HashSet<>();
    Button buttonGoBackCalculator;

    private static final String PREF_NAME = "key_pref";
    private static final String PREF_THEME_KEY = "key_pref_theme";

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(getAppTheme());
        setContentView(R.layout.activity_theme);

        setBackgroundDrawable();

        init();
        for (RadioButton radioButton : radioButtons) {
            radioButton.setOnClickListener(listenerTheme);
        }

        buttonGoBackCalculator.setOnClickListener(view -> changeThemeIntent());
    }

    private void setBackgroundDrawable() {
        ConstraintLayout constraintLayout;
        constraintLayout = findViewById(R.id.theme_constraint_layout);
        if (getAppTheme() == R.style.myThemeDefault) {
            constraintLayout.setBackgroundResource(R.drawable.art_deco_frame1);
        } else if (getAppTheme() == R.style.myThemeOrange) {
            constraintLayout.setBackgroundResource(R.drawable.art_deco_frame2);
        } else if (getAppTheme() == R.style.myThemePurple) {
            constraintLayout.setBackgroundResource(R.drawable.art_nouveau_frame);
        }
    }

    private void init() {
        buttonGoBackCalculator = findViewById(R.id.button_goBackCalculator);
        radioButtons.add(findViewById(R.id.radioButton_defaultTheme));
        radioButtons.add(findViewById(R.id.radioButton_orangeTheme));
        radioButtons.add(findViewById(R.id.radioButton_purpleTheme));
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

    View.OnClickListener listenerTheme = view -> {
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
    };

    @Override
    public void onBackPressed() {
        changeThemeIntent();
    }

    private void changeThemeIntent() {
        Intent intent = new Intent();
        intent.putExtra(MainActivity.MAIN_KEY, getAppTheme());
        ThemeActivity.this.setResult(RESULT_OK, intent);
        finish();
    }
}

