package ru.mirea.mkrtchyangb.favoritebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ShareActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        // Получение данных из MainActivity
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            TextView tvDeveloper = findViewById(R.id.tvDeveloperBook);
            String developerBook = extras.getString(MainActivity.KEY);
            tvDeveloper.setText("Моя любимая книга: " + developerBook);
        }
    }

    public void onSendClick(View view) {
        EditText etUserBook = findViewById(R.id.etUserBook);
        String userBook = etUserBook.getText().toString().trim();

        if (!userBook.isEmpty()) {
            // Отправка данных обратно
            Intent data = new Intent();
            data.putExtra(MainActivity.USER_MESSAGE, userBook);
            setResult(RESULT_OK, data);
            finish(); // Завершаем активность
        }
    }
}