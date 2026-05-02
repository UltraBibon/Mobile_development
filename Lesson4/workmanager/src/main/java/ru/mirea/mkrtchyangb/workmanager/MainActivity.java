package ru.mirea.mkrtchyangb.workmanager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import ru.mirea.mkrtchyangb.workmanager.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBackgroundTask();
            }
        });
    }

    private void startBackgroundTask() {
        // ✅ Критерии запуска из ТЗ: интернет + зарядка
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)  // Только Wi-Fi (без тарификации)
                .setRequiresCharging(true)                       // Только при зарядке
                .build();

        // ✅ Создание WorkRequest с ограничениями
        WorkRequest uploadWorkRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
                .setConstraints(constraints)  // ✅ Применяем ограничения
                .build();

        // ✅ Запуск задачи
        WorkManager.getInstance(this).enqueue(uploadWorkRequest);

        Toast.makeText(this, "Фоновая задача запланирована", Toast.LENGTH_SHORT).show();
        Log.d("WorkManager", "Task enqueued with constraints");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}