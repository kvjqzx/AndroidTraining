package com.kvjqzx.androidtraining;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.kvjqzx.androidtraining.MESSAGE";
    private EditText mMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMessage = (EditText) findViewById(R.id.editText);
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
}
