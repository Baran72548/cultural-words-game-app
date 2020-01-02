package com.barmej.culturalwordsgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_WRITE_EXTERNAL_STORAGE = 123;

    private ImageView mCultureIconImageView;

    private CulturalWord[] mCulturalWords = {
            new CulturalWord(R.drawable.icon_1, R.string.eqal),
            new CulturalWord(R.drawable.icon_2, R.string.farwa),
            new CulturalWord(R.drawable.icon_3, R.string.sadoo),
            new CulturalWord(R.drawable.icon_4, R.string.hinaa),
            new CulturalWord(R.drawable.icon_5, R.string.dala),
            new CulturalWord(R.drawable.icon_6, R.string.khif),
            new CulturalWord(R.drawable.icon_7, R.string.kingar),
            new CulturalWord(R.drawable.icon_8, R.string.namraqa),
            new CulturalWord(R.drawable.icon_9, R.string.qendeel),
            new CulturalWord(R.drawable.icon_10, R.string.fanoos),
            new CulturalWord(R.drawable.icon_11, R.string.tarboosh),
            new CulturalWord(R.drawable.icon_12, R.string.emama),
            new CulturalWord(R.drawable.icon_13, R.string.dishdasha)
    };
    private String[] ANSWERS_DESCRIPTION;

    private Random mRandom = new Random();
    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_PREF, MODE_PRIVATE);
        String appLang = sharedPreferences.getString(Constants.APP_LANG, "");
        //Use device's default language if there is no saved language.
        if (!appLang.equals(""))
            LocaleHelper.setLocale(this, appLang);

        setContentView(R.layout.activity_main);

        ANSWERS_DESCRIPTION = getResources().getStringArray(R.array.answer_description);

        mCultureIconImageView = findViewById(R.id.icon_image_view);
        Button changeQuestion = findViewById(R.id.change_question_button);
        changeQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewQuestion();
            }
        });

        Button viewAnswer = findViewById(R.id.view_answer_button);
        viewAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CulturalWord answer = mCulturalWords[mCurrentIndex];
                String name = getResources().getString(answer.getName());
                Intent intent = new Intent(MainActivity.this, AnswersActivity.class);
                intent.putExtra(Constants.QUESTION_ANSWER, name + ":");
                intent.putExtra(Constants.QUESTION_ANSWER_DESCRIPTION, ANSWERS_DESCRIPTION[mCurrentIndex]);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.change_language_menu){
            showLanguageDialog();
            return true;
        } else if (item.getItemId() == R.id.share_menu){
            checkPermissionAndShare();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showLanguageDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle(R.string.change_language)
                .setItems(R.array.languages, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String language = "ar";
                        switch (i) {
                            case 0:
                                language = "ar";
                                break;
                            case 1:
                                language = "en";
                                break;
                        }
                        //Call saveLanguage() method in case the user exit the application.
                        saveLanguage(language);
                        LocaleHelper.setLocale(MainActivity.this, language);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).create();
        alertDialog.show();
    }

    private void saveLanguage(String lang){
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.APP_LANG, lang);
        editor.apply();
    }

    /**
     * Creating an Intent to start a new activity and share the image there.
     */
    public void shareImage(){
        CulturalWord image = mCulturalWords[mCurrentIndex];

        Intent intent = new Intent(MainActivity.this, ShareActivity.class);
        intent.putExtra(Constants.QUESTION_IMAGE, image.getPicture());
        startActivity(intent);
    }

    /**
     * Asking for a permission to save data in external storage.
     */
    private void checkPermissionAndShare(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            // Here we don't have the permission so, we need to ask the user for it.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                // App doesn't have the permission this is why we need to explain the reason to the user.
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setMessage(R.string.permission_explanation)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PERMISSIONS_WRITE_EXTERNAL_STORAGE);
                            }
                        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();

                alertDialog.show();
            } else {
                // Here we don't need to explain why we need the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            }
        } else {
            // Here we already have a permission so, directly call shareImage method.
            shareImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_WRITE_EXTERNAL_STORAGE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                shareImage();
            } else {
                Toast.makeText(MainActivity.this, R.string.permission_explanation, Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Creating a new random number.
     */
    private int getRandomNumber(){
        int newRandom = mCurrentIndex;
        while (newRandom == mCurrentIndex){
            newRandom = mRandom.nextInt(mCulturalWords.length);
        }
        return newRandom;
    }

    /**
     * This method displays a new image of a new question.
     */
    private void showNewQuestion(){
        mCurrentIndex = getRandomNumber();
        CulturalWord image = mCulturalWords[mCurrentIndex];
        Drawable culturalIcon = ContextCompat.getDrawable(this, image.getPicture());
        mCultureIconImageView.setImageDrawable(culturalIcon);
    }
}
