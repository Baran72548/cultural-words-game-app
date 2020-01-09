package com.barmej.culturalwordsgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

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
    private String[] answersDescription;

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

        answersDescription = getResources().getStringArray(R.array.answer_description);

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
                intent.putExtra(Constants.QUESTION_ANSWER_DESCRIPTION, answersDescription[mCurrentIndex]);
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
            shareImage();
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
                        recreate();
//                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
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
     * Saving current state of activity for rotation case.
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.BUNDLE_CURRENT_INDEX, mCurrentIndex);
    }

    /**
     * Restore the saved state of activity after rotation.
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentIndex = savedInstanceState.getInt(Constants.BUNDLE_CURRENT_INDEX);
        if (mCurrentIndex != 0) {
            CulturalWord image = mCulturalWords[mCurrentIndex];
            Drawable culturalIcon = ContextCompat.getDrawable(this, image.getPicture());
            mCultureIconImageView.setImageDrawable(culturalIcon);
        }
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
