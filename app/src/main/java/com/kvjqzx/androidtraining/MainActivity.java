package com.kvjqzx.androidtraining;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.kvjqzx.androidtraining.MESSAGE";
    private EditText mMessage;
    private EditText mDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMessage = (EditText) findViewById(R.id.editText);
        mDir = (EditText) findViewById(R.id.editText2);

        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        mMessage.setText(sp.getString(EXTRA_MESSAGE, "Hi"));
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);

        String message = mMessage.getText().toString();

        SharedPreferences sp = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(EXTRA_MESSAGE, message);
        editor.commit();

        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void GetPath(View view) {
        File fileDir = getFilesDir();
        File fileCacheDir = getCacheDir();
        mDir.setText(fileDir.getAbsolutePath() + "  " + fileCacheDir.getAbsolutePath());

        try {
            // Create a file test.txt in /data/data/com.kvjqzx.androidtraning/files/
            File file = new File(getFilesDir(), "test.txt");
            file.createNewFile();

            // It will create a test2.txt in data directory
            FileOutputStream out = openFileOutput("test2.txt", MODE_PRIVATE);
            out.write("Hello world".getBytes());
            out.close();

            // It will create a file like cache5625271425208450524.tmp in cache directory
            File cache = File.createTempFile("cache", null, getCacheDir());
            cache.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
