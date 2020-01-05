package com.barmej.culturalwordsgame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ShareActivity extends AppCompatActivity {

    private EditText mSharedTitleEditText;
    private ImageView mSharedImageView;
    int sharedImage;
    Drawable drawable;
    String sharedTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        mSharedTitleEditText = findViewById(R.id.shared_title_edit_text);
        mSharedImageView = findViewById(R.id.shared_image_view);

        sharedImage = getIntent().getIntExtra(Constants.QUESTION_IMAGE, 0);
        drawable = ContextCompat.getDrawable(this, sharedImage);
        mSharedImageView.setImageDrawable(drawable);

        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_PREF, MODE_PRIVATE);
        sharedTitle = sharedPreferences.getString(Constants.SHARE_TITLE, "");
        mSharedTitleEditText.setText(sharedTitle);

        Button shareButton = findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();

                SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(Constants.SHARE_TITLE, sharedTitle);
                editor.apply();

                String sharedTitle = mSharedTitleEditText.getText().toString();
                Uri bitmapUri = getBitmapFromDrawable(bitmap);

                if (bitmapUri != null) {
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, sharedTitle);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
                    shareIntent.setType("image/*");
                    startActivity(Intent.createChooser(shareIntent, "Share Image"));
                } else {
                    Toast.makeText(ShareActivity.this, R.string.uri_failed, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Saving shared image in external storage and get its URI to pass it through the intent.
     */
    public Uri getBitmapFromDrawable(Bitmap bitmap){
        // Store image to default external storage directory
        Uri imageUri = null;
        try {
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();

            imageUri = FileProvider.getUriForFile(ShareActivity.this, "com.barmej.culturalwordsgame", file);
            Log.i("URI", "URI is: " + imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageUri;
    }

}
