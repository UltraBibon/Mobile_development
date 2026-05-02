package ru.mirea.mkrtchyangb.serviceapp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import ru.mirea.mkrtchyangb.serviceapp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final int PERMISSION_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ✅ Проверка разрешений
        checkPermissions();

        // ✅ Кнопка "Play" — запуск сервиса
        binding.buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(MainActivity.this, PlayerService.class);
                // ✅ Используем startForegroundService (как в ТЗ)
                ContextCompat.startForegroundService(MainActivity.this, serviceIntent);
                Toast.makeText(MainActivity.this, "Воспроизведение начато", Toast.LENGTH_SHORT).show();
            }
        });

        // ✅ Кнопка "Stop" — остановка сервиса
        binding.buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService(new Intent(MainActivity.this, PlayerService.class));
                Toast.makeText(MainActivity.this, "Воспроизведение остановлено", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Проверка и запрос разрешений
     */
    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            Log.d("MainActivity", "Разрешения получены");
        } else {
            Log.d("MainActivity", "Запрос разрешений");
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            android.Manifest.permission.POST_NOTIFICATIONS,
                            android.Manifest.permission.FOREGROUND_SERVICE
                    },
                    PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Разрешения предоставлены", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Разрешения отклонены!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}