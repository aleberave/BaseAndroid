package ru.geekbrains.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ThemeFragment extends Fragment {

    private static final String PREF_NAME = "key_pref";
    private static final String PREF_THEME_KEY = "key_pref_theme";

    public ThemeFragment() {
        // Required empty public constructor
    }

    public static ThemeFragment newInstance() {
        ThemeFragment fragment = new ThemeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setTheme(getAppTheme());
        return inflater.inflate(R.layout.fragment_theme, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        RadioButton defaultRadioButton = view.findViewById(R.id.radioButton_defaultTheme);
        RadioButton orangeRadioButton = view.findViewById(R.id.radioButton_orangeTheme);
        RadioButton purpleRadioButton = view.findViewById(R.id.radioButton_purpleTheme);

        defaultRadioButton.setOnClickListener(listener);
        orangeRadioButton.setOnClickListener(listener);
        purpleRadioButton.setOnClickListener(listener);
    }

    View.OnClickListener listener = view -> {
        switch (view.getId()) {
            case (R.id.radioButton_defaultTheme): {
                setAppTheme(R.style.Theme_MyApplication);
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
        requireActivity().recreate();
    };

    protected void setAppTheme(int codeStyle) {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(PREF_THEME_KEY, codeStyle);
        editor.apply();
    }

    protected int getAppTheme() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPref.getInt(PREF_THEME_KEY, R.style.Theme_MyApplication);
    }

}