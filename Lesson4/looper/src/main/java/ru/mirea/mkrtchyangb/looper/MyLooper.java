package ru.mirea.mkrtchyangb.looper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


public class MyLooper extends Thread {

    public Handler mHandler;  // Публичный, как в ТЗ
    private Handler mainHandler;  // Для отправки ответов в главный поток

    public MyLooper(Handler mainThreadHandler) {
        this.mainHandler = mainThreadHandler;
    }

    @Override
    public void run() {
        Log.d("MyLooper", "run: подготовка Looper");

        // ✅ 1. Подготовка Looper для текущего потока (как в ТЗ)
        Looper.prepare();

        // ✅ 2. Создание Handler для обработки входящих сообщений
        mHandler = new Handler(Looper.myLooper()) {
            @Override
            public void handleMessage(Message msg) {
                // ✅ Получение данных из сообщения (согласно ТЗ)
                Bundle data = msg.getData();
                int age = data.getInt("AGE", 0);
                String profession = data.getString("PROFESSION", "");

                Log.d("MyLooper", "Получено: возраст=" + age + ", профессия=" + profession);

                // ✅ ЗАДЕРЖКА = возраст в секундах (требование ТЗ)
                try {
                    Log.d("MyLooper", "Задержка на " + age + " секунд...");
                    Thread.sleep(age * 1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    Log.e("MyLooper", "Задержка прервана", e);
                }

                // ✅ Формирование ответа
                Message response = Message.obtain();
                Bundle result = new Bundle();
                result.putString("result", "Обработка завершена: " + profession + " (" + age + " лет)");
                response.setData(result);

                // ✅ Отправка ответа в главный поток
                mainHandler.sendMessage(response);

                Log.d("MyLooper", "Ответ отправлен в главный поток");
            }
        };

        // ✅ 3. Запуск цикла обработки сообщений (как в ТЗ)
        Log.d("MyLooper", "Запуск Looper.loop()");
        Looper.loop();
    }
}