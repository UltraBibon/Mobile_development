package ru.mirea.mkrtchyangb.lesson3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.mirea.mkrtchyangb.lesson3.R;
import ru.mirea.mkrtchyangb.lesson3.TimeDisplayActivity;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_TIME = "current_time";
    public static final String EXTRA_SQUARE = "square_number";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSendTimeClick(View view) {
        // Получаем системное время
        long dateInMillis = System.currentTimeMillis();
        String format = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String dateString = sdf.format(new Date(dateInMillis));

        int myNumber = 13;
        int square = myNumber * myNumber;

        // Создание Intent
        Intent intent = new Intent(this, TimeDisplayActivity.class);
        intent.putExtra(EXTRA_TIME, dateString);
        intent.putExtra(EXTRA_SQUARE, square);

        startActivity(intent);
    }
}