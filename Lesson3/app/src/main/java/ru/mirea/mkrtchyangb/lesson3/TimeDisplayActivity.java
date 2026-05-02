package ru.mirea.mkrtchyangb.lesson3;


import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class TimeDisplayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_display);

        TextView tvResult = findViewById(R.id.tvResult);

        // Извлекаем данные из Intent
        String time = getIntent().getStringExtra(MainActivity.EXTRA_TIME);
        int square = getIntent().getIntExtra(MainActivity.EXTRA_SQUARE, 0);

        // Формируем строку по ТЗ
        String result = String.format(
                "КВАДРАТ ЗНАЧЕНИЯ МОЕГО НОМЕРА ПО СПИСКУ В ГРУППЕ СОСТАВЛЯЕТ ЧИСЛО %d, а текущее время %s",
                square, time
        );

        tvResult.setText(result);
    }
}