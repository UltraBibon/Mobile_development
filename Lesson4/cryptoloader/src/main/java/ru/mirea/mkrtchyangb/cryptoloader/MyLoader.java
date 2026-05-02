package ru.mirea.mkrtchyangb.cryptoloader;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.loader.content.AsyncTaskLoader;

public class MyLoader extends AsyncTaskLoader<String> {

    private final byte[] encryptedText;
    private final byte[] keyBytes;

    // ✅ Ключи Bundle должны точно совпадать с ТЗ
    public static final String ARG_WORD = "word";  // Для зашифрованного текста
    // "key" — используется напрямую, как в ТЗ

    public MyLoader(@NonNull Context context, byte[] text, byte[] key) {
        super(context);
        this.encryptedText = text;
        this.keyBytes = key;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        // ✅ Немедленно начать загрузку (как в ТЗ)
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        Log.d("MyLoader", "loadInBackground: начало дешифровки");

        // ✅ Имитация долгой операции (5 секунд, как в примере ТЗ)
        SystemClock.sleep(5000);

        try {
            // ✅ Восстановление ключа из байтов (как в ТЗ)
            SecretKey originalKey = new SecretKeySpec(keyBytes, 0, keyBytes.length, "AES");

            // ✅ Дешифровка
            String decrypted = CryptoUtils.decryptMsg(encryptedText, originalKey);

            Log.d("MyLoader", "loadInBackground: успешно дешифровано");
            return decrypted;

        } catch (Exception e) {
            Log.e("MyLoader", "Ошибка дешифровки", e);
            return "Ошибка: " + e.getMessage();
        }
    }
}