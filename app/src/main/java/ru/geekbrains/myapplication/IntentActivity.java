package ru.geekbrains.myapplication;

import static ru.geekbrains.myapplication.MainActivity.INTENT_ACTIVITY_KEY;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class IntentActivity extends AppCompatActivity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intent);

        setMyDrawable();

        getIntentMyResult();
    }

    public void getIntentMyResult() {
        textView = findViewById(R.id.textView_intent_activity);

        Bundle bundle = getIntent().getExtras();
        textView.setText(bundle.getString(INTENT_ACTIVITY_KEY));
    }

    public void setMyDrawable() {
        ConstraintLayout constraintLayout = findViewById(R.id.intent_constraint_layout);
        if (constraintLayout.getBackground() == null)
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                constraintLayout.setBackgroundResource(R.drawable.texture_wood_grain_weathered_washed_off_wooden_structure_grain_structure_background);
            } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                constraintLayout.setBackgroundResource(R.drawable.heart_wood_love_wooden_old_heart_background_love_heart_romantic);
            }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        setIntentToMain();
    }

    // Если брать ключи из ресурсов, то тоже работает,
    // но нам ни разу не говорили о таком подходе.
    // Интересно это не используется или есть какие-то подводные камни?

    private void setIntentToMain() {
        Intent intent = new Intent(getResources().getString(R.string.ru_geekbrains_myapplication_MainActivity));
        if (!textView.getText().toString().equals(getResources().getString(R.string.empty))) {
            intent.putExtra(getString(R.string.ru_geekbrains_myapplication_MainActivity), getResources().getString(R.string.my_main_from_intent_activity));
        }
//        В моем случае у главной активити, которая принимает интент, два интрент-фильтра,
//        один с категорией лаунчер, а другой с категорией дефолт.
//        Если у активити в манифесте только один интент-фильтр и этот фильтр с категорией лаунчер,
//        то отправляя из другого приложения интент нужно прописать, проверку на наличие
//        возможности обработки интента:

//        @SuppressLint({"WrongConstant", "QueryPermissionsNeeded"})
//        ActivityInfo activityInfo = intent.resolveActivityInfo(getPackageManager(), intent.getFlags());
//        if (activityInfo != null) {
        startActivity(intent);
        finish();
//        }
    }
}