package ru.mirea.mkrtchyangb.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import ru.mirea.mkrtchyangb.looper.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private MyLooper myLooper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ✅ Handler для главного потока (как в ТЗ)
        Handler mainThreadHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                String result = msg.getData().getString("result");
                Log.d("MainActivity", "Task execute. This is result: " + result);
                binding.textViewMirea.setText(result);
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
            }
        };

        // ✅ Создание и запуск потока с Looper (как в ТЗ)
        myLooper = new MyLooper(mainThreadHandler);
        myLooper.start();

        // ✅ Обработчик кнопки
        binding.buttonMirea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    // ✅ Чтение данных (согласно ТЗ: возраст и профессия)
                    int age = Integer.parseInt(binding.editTextAge.getText().toString());
                    String profession = binding.editTextProfession.getText().toString();

                    if (profession.isEmpty()) {
                        profession = "Студент";
                    }

                    // ✅ Формирование сообщения
                    Message msg = Message.obtain();
                    Bundle bundle = new Bundle();
                    bundle.putInt("AGE", age);
                    bundle.putString("PROFESSION", profession);
                    msg.setData(bundle);

                    // ✅ Отправка в поток с Looper (проверка на null, как в ТЗ)
                    if (myLooper.mHandler != null) {
                        myLooper.mHandler.sendMessage(msg);
                        Toast.makeText(MainActivity.this, "Отправлено в обработку...", Toast.LENGTH_SHORT).show();
                    }

                } catch (NumberFormatException e) {
                    Toast.makeText(MainActivity.this, "Введите корректный возраст!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ✅ Остановка Looper при уничтожении
        if (myLooper != null && myLooper.mHandler != null) {
            myLooper.mHandler.getLooper().quit();
        }
        binding = null;
    }
}