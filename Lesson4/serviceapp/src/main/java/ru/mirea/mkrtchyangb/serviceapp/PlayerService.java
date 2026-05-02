package ru.mirea.mkrtchyangb.serviceapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class PlayerService extends Service {

    private MediaPlayer mediaPlayer;
    // ✅ ID канала уведомлений
    public static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // Для непривязанного сервиса — не реализуем
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("PlayerService", "onCreate");

        // ✅ Создание канала уведомлений (Android 8.0+)
        createNotificationChannel();

        // ✅ Инициализация MediaPlayer с файлом из res/raw
        mediaPlayer = MediaPlayer.create(this, R.raw.ost);
        mediaPlayer.setLooping(false);  // Не повторять

        // ✅ Создание уведомления для foreground-сервиса
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("🎵 Музыкальный плеер")
                // ✅ ВАЖНО: Укажите СВОЁ название композиции (требование ТЗ!)
                .setContentText("Воспроизведение: ost")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setOngoing(true);  // Нельзя смахнуть

        // ✅ Запуск сервиса в переднем плане
        startForeground(1, builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("PlayerService", "onStartCommand");

        // ✅ Начало воспроизведения
        if (mediaPlayer != null) {
            mediaPlayer.start();

            // ✅ Обработчик окончания трека
            mediaPlayer.setOnCompletionListener(mp -> {
                stopForeground(true);
                stopSelf();
            });
        }

        // ✅ Возвращаем режим перезапуска (как в ТЗ)
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("PlayerService", "onDestroy");
        super.onDestroy();

        // ✅ Очистка ресурсов
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        stopForeground(true);
    }

    /**
     * Создание канала уведомлений для Android 8.0+
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    // ✅ Укажите своё имя канала
                    "Student Мкртчян Notification",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("MIREA Music Channel");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}