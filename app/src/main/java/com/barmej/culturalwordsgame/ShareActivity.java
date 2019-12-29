package com.barmej.culturalwordsgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ShareActivity extends AppCompatActivity {

    private EditText mSharedTitleEditText;
    private ImageView mSharedImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        mSharedTitleEditText = findViewById(R.id.shared_title_edit_text);
        mSharedImageView = findViewById(R.id.shared_image_view);

        File folder = getExternalFilesDir("Shared Image"); // Folder Name
        File myFile = new File(folder, "myImage.png"); // File Name

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(String.valueOf(myFile), options);
        getData(myFile, bitmap);


/**
        mQuizImage = getIntent().getIntExtra("question_answer");
        if (answer != null){
            mSharedImageView.setImageDrawable(answer);
        } */

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_PREF, MODE_PRIVATE);
        String sharedTitle = sharedPreferences.getString(Constants.SHARE_TITLE, "");
        mSharedTitleEditText.setText(sharedTitle);
    }

    public void onShareClicked(View view){
        String sharedTitle = mSharedTitleEditText.getText().toString();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, sharedTitle);
        intent.setType("text/plain");
        startActivity(intent);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHARE_TITLE, sharedTitle);
        editor.apply();
    }

    private void getData(File myFile, Bitmap bitmap) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(myFile);
            Log.i("bitmap", "set image view");
            mSharedImageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("bitmap", "catch set image view");
        } finally {
            if (fileInputStream != null) {
                try {
                    Log.i("bitmap", "finally set image view");
                    fileInputStream.close();
                } catch (IOException e) {
                    Log.i("bitmap", "catch finally set image view");
                    e.printStackTrace();
                }
            }
        }
    }

}
