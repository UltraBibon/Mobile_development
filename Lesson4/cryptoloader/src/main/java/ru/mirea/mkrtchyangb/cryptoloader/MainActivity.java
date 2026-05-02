package ru.mirea.mkrtchyangb.cryptoloader;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import javax.crypto.SecretKey;
import ru.mirea.mkrtchyangb.cryptoloader.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String> {

    private ActivityMainBinding binding;
    private static final int LOADER_ID = 1234;  // Как в примере ТЗ
    private SecretKey currentKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ✅ Генерация ключа при старте
        currentKey = CryptoUtils.generateKey();

        // ✅ Обработчик кнопки (как в ТЗ)
        binding.buttonMirea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = binding.editTextMirea.getText().toString();

                if (input.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Введите текст!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // ✅ Шифрование текста (как в ТЗ)
                byte[] encrypted = CryptoUtils.encryptMsg(input, currentKey);

                // ✅ Подготовка Bundle с ключами, как в ТЗ:
                // - ARG_WORD = "word" для текста
                // - "key" для ключа шифрования
                Bundle bundle = new Bundle();
                bundle.putByteArray(MyLoader.ARG_WORD, encrypted);  // "word"
                bundle.putByteArray("key", currentKey.getEncoded()); // "key" — точно как в ТЗ

                // ✅ Инициализация Loader (как в ТЗ)
                LoaderManager.getInstance(MainActivity.this)
                        .initLoader(LOADER_ID, bundle, MainActivity.this);

                Toast.makeText(MainActivity.this, "Шифрование начато...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // ✅ onCreateLoader (как в ТЗ)
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        Log.d("CryptoLoader", "onCreateLoader: ID=" + id);

        if (id == LOADER_ID && args != null) {
            Toast.makeText(this, "onCreateLoader: " + id, Toast.LENGTH_SHORT).show();

            byte[] text = args.getByteArray(MyLoader.ARG_WORD);  // "word"
            byte[] key = args.getByteArray("key");                // "key"

            return new MyLoader(this, text, key);
        }
        throw new IllegalArgumentException("Неверный ID загрузчика: " + id);
    }

    // ✅ onLoadFinished (как в ТЗ)
    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String decryptedText) {
        Log.d("CryptoLoader", "onLoadFinished: " + decryptedText);

        if (loader.getId() == LOADER_ID) {
            // ✅ Отображение через Toast или SnackBar (как в ТЗ)
            Toast.makeText(this, "Дешифровано: " + decryptedText, Toast.LENGTH_LONG).show();
            binding.textViewMirea.setText("Оригинал: " + decryptedText);
        }
    }

    // ✅ onLoaderReset (как в ТЗ)
    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
        Log.d("CryptoLoader", "onLoaderReset");
        // Очистка ссылок при необходимости
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}