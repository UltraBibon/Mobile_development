package ru.mirea.mkrtchyangb.thread;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Arrays;
import ru.mirea.mkrtchyangb.thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private int counter = 0;  // Счётчик запущенных потоков

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ✅ Инициализация ViewBinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ✅ Отображение информации о главном потоке (как в ТЗ)
        displayThreadInfo();

        // ✅ Обработчик кнопки "Рассчитать"
        binding.buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateInBackground();
            }
        });
    }

    /**
     * Отображение информации о главном потоке
     * Реализует требования ТЗ: получение имени, изменение имени, логирование стека
     */
    private void displayThreadInfo() {
        // ✅ Получение текущего потока (как в ТЗ)
        Thread mainThread = Thread.currentThread();

        StringBuilder info = new StringBuilder();
        info.append("Имя текущего потока: ").append(mainThread.getName()).append("\n");

        // ✅ Изменение имени потока (как в ТЗ)
        mainThread.setName("ГРУППА: БСБО-ХХ-ХХ, НОМЕР: ХХ, ФИЛЬМ: ХХ");
        info.append("Новое имя потока: ").append(mainThread.getName()).append("\n");

        // ✅ Дополнительные данные из ТЗ
        info.append("Приоритет: ").append(mainThread.getPriority()).append("\n");
        info.append("Группа: ").append(mainThread.getThreadGroup());

        binding.textViewThreadInfo.setText(info.toString());

        // ✅ Логирование стека вызовов (как в ТЗ)
        Log.d("ThreadInfo", "Stack: " + Arrays.toString(mainThread.getStackTrace()));

        // ✅ Получение группы потоков (как в ТЗ)
        Log.d("ThreadInfo", "Group: " + mainThread.getThreadGroup());
    }

    /**
     * Расчёт среднего количества пар в фоновом потоке
     * Реализует требование ТЗ: "Посчитать в фоновом потоке среднее количество пар в день"
     */
    private void calculateInBackground() {
        try {
            // ✅ Чтение входных данных
            int totalPairs = Integer.parseInt(binding.editTextTotalPairs.getText().toString());
            int studyDays = Integer.parseInt(binding.editTextStudyDays.getText().toString());

            // ✅ Создание и запуск фонового потока (как в ТЗ)
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int threadNumber = counter++;
                    Log.d("ThreadProject", "Запущен поток № " + threadNumber);

                    // ✅ РАСЧЁТ (выполняется в фоне, но быстро)
                    double average = (double) totalPairs / studyDays;
                    String resultText = String.format("Среднее: %.2f пар/день", average);

                    // ✅ ИМИТАЦИЯ долгой операции (20 секунд, как в ТЗ)
                    long endTime = System.currentTimeMillis() + 20 * 1000;
                    while (System.currentTimeMillis() < endTime) {
                        try {
                            synchronized (this) {
                                wait(1000);  // Ждём 1 секунду
                                Log.d("ThreadProject", "Имитация работы... поток №" + threadNumber);
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            Log.e("ThreadProject", "Поток прерван", e);
                            break;
                        }
                    }

                    // ✅ Обновление UI в главном потоке (обязательно!)
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            binding.textViewResult.setText(resultText);
                            Toast.makeText(MainActivity.this, "Расчёт завершён!", Toast.LENGTH_SHORT).show();
                        }
                    });

                    Log.d("ThreadProject", "Выполнен поток № " + threadNumber);
                }
            }).start();  // ✅ Запуск потока (как в ТЗ)

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Введите корректные числа!", Toast.LENGTH_SHORT).show();
        }
    }

    // ✅ Освобождение binding
    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}