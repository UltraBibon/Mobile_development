package ru.mirea.mkrtchyangb.data_thread;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.concurrent.TimeUnit;
import ru.mirea.mkrtchyangb.data_thread.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnStart.setOnClickListener(v -> startThreadTest());
    }

    private void startThreadTest() {
        binding.tvInfo.setText("Начало теста...\n");

        // ✅ Создаём три Runnable (как в примере ТЗ)
        final Runnable runn1 = new Runnable() {
            public void run() {
                binding.tvInfo.append("\n[1] runOnUiThread: немедленное выполнение");
            }
        };

        final Runnable runn2 = new Runnable() {
            public void run() {
                binding.tvInfo.append("\n[2] post: добавление в очередь View");
            }
        };

        final Runnable runn3 = new Runnable() {
            public void run() {
                binding.tvInfo.append("\n[3] postDelayed: выполнение через 2 сек");
            }
        };

        // ✅ Фоновый поток
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Задержка 2 секунды перед первым действием
                    TimeUnit.SECONDS.sleep(2);

                    // ✅ Метод 1: runOnUiThread (немедленная обработка)
                    runOnUiThread(runn1);

                    TimeUnit.SECONDS.sleep(1);

                    // ✅ Метод 3: postDelayed (с задержкой 2000 мс)
                    binding.tvInfo.postDelayed(runn3, 2000);

                    // ✅ Метод 2: post (немедленная обработка через очередь сообщений View)
                    binding.tvInfo.post(runn2);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        // ✅ Описание различий в TextView (требование ТЗ)
        binding.tvInfo.append("\n\n=== РАЗЛИЧИЯ МЕТОДОВ ===");
        binding.tvInfo.append("\n• runOnUiThread(): добавляет Runnable в очередь главного потока. Выполняется, когда главный поток освободится.");
        binding.tvInfo.append("\n• View.post(): добавляет Runnable в очередь сообщений конкретного View. Порядок выполнения зависит от очереди сообщений.");
        binding.tvInfo.append("\n• View.postDelayed(): как post(), но с задержкой в миллисекундах.");
        binding.tvInfo.append("\n\n=== ОЖИДАЕМЫЙ ПОРЯДОК ===");
        binding.tvInfo.append("\n1. runn1 (runOnUiThread) — после 2 сек задержки");
        binding.tvInfo.append("\n2. runn2 (post) — сразу после отправки");
        binding.tvInfo.append("\n3. runn3 (postDelayed) — через 2 сек после отправки");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}