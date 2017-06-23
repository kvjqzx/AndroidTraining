package com.kvjqzx.androidtraining;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.kvjqzx.androidtraining.MESSAGE";
    private static final int PICK_CONTACT_REQUEST = 1;

    private EditText mMessage;
    private EditText mDir;
    private EditText mExternal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMessage = (EditText) findViewById(R.id.editText);
        mDir = (EditText) findViewById(R.id.editText2);
        mExternal = (EditText) findViewById(R.id.editText3);

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

    public void GetExternal(View view) {
        String str = "";
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            str = "External mounted.  ";

            // It will create a directory in below path, system deletes when the user uninstalls
            //  your app. /sdcard/Android/data/com.kvjqzx.androidtraining/files/Documents/Doc
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "Doc");
            file.mkdir();
            str += file.getAbsolutePath();
            str += "  " + file.getFreeSpace();
            str += "  " + file.getTotalSpace();
        }

        // If you use API 23 (Marshmallow) and above, you need to Request Permissions at Run Time
        // because it's a Dangerous Permission.
        File externalPub = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS), "Pics");
        str += externalPub.getAbsolutePath();
        try {
            if (!externalPub.createNewFile()) {
                Toast.makeText(this, "Directory not created", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        mExternal.setText(str);
    }

    public void OpenPhone(View view) {
        Uri number = Uri.parse("tel:5551234");
        Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
        startActivity(callIntent);
    }

    public void OpenMap(View view) {
        Uri location = Uri.parse("geo:32.0643402071,118.7905286520?z=4"); // z param is zoom level
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        startActivity(mapIntent);
    }

    public void OpenWeb(View view) {
        Uri webPage = Uri.parse("http://www.wlaneasy.com");
        Intent webIntent = new Intent(Intent.ACTION_VIEW, webPage);

        // If you invoke an intent and there is no app available on the device
        // that can handle the intent, your app will crash.
        PackageManager pm = getPackageManager();
        List activities = pm.queryIntentActivities(webIntent, PackageManager.MATCH_DEFAULT_ONLY);
        if (activities.size() > 0) {
            startActivity(webIntent);
        } else {
            Toast.makeText(this, "No App can handle this intent.", Toast.LENGTH_SHORT).show();
        }
    }

    public void startChooser(View view) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        Intent chooser = Intent.createChooser(intent, "Share this via");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    public void pickContact(View view) {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                Uri contactUri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};

                Cursor cursor = getContentResolver()
                        .query(contactUri, projection, null, null, null);
                cursor.moveToFirst();
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(column);
                Toast.makeText(this, "contact number: " + number, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
