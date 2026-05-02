package ru.mirea.mkrtchyangb.lesson4;  // ✅ Проверьте пакет!

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
// ✅ Автоматически сгенерированный класс:
import ru.mirea.mkrtchyangb.lesson4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // ✅ Объявление binding-объекта (private, как в примере ТЗ)
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ Инициализация binding (как в ТЗ)
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ✅ Работа с компонентами через binding (пример из ТЗ)
        binding.editTextMirea.setText("Мой номер по списку №___");

        // ✅ Обработчик кнопки (как в ТЗ)
        binding.buttonMirea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(MainActivity.class.getSimpleName(), "onClickListener");
                // Дополнительно: покажем реакцию интерфейса
                binding.textViewMirea.setText("Кнопка нажата!");
            }
        });
    }

    // ✅ Освобождение памяти (предотвращение утечек)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}