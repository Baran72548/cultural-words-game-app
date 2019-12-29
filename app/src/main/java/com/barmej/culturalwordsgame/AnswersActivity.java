package com.barmej.culturalwordsgame;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AnswersActivity extends AppCompatActivity {

    private TextView mAnswerTextView;
    private TextView mAnswerDescriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        mAnswerTextView = findViewById(R.id.answer_text_view);
        mAnswerDescriptionTextView = findViewById(R.id.answer_description_text_view);

        String answer = getIntent().getStringExtra(Constants.QUESTION_ANSWER);
        String answerDescription = getIntent().getStringExtra(Constants.QUESTION_ANSWER_DESCRIPTION);
        if (answer != null && answerDescription != null){
            mAnswerTextView.setText(answer);
            mAnswerDescriptionTextView.setText(answerDescription);
        }
    }

    public void onReturnClicked(View view) {
        finish();
    }
}
