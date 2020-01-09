package com.barmej.culturalwordsgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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

    private static final int PERMISSIONS_WRITE_EXTERNAL_STORAGE = 123;

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
                checkPermissionAndShare();
            }
        });
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    /**
     * Share text and image (after saving it in external storage and creating a uri to it) with other apps.
     */
    private void share() {
        SharedPreferences sharedPreferences = getSharedPreferences(Constants.APP_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.SHARE_TITLE, sharedTitle);
        editor.apply();

        String sharedTitle = mSharedTitleEditText.getText().toString();

        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
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

            imageUri = FileProvider.getUriForFile(ShareActivity.this, getPackageName(), file);
            Log.i("URI", "URI is: " + imageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageUri;
    }

    /**
     * Asking for a permission to save data in external storage.
     */
    private void checkPermissionAndShare() {
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
                                ActivityCompat.requestPermissions(ShareActivity.this,
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
            // Here we already have a permission so, directly call share method.
            share();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_WRITE_EXTERNAL_STORAGE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                share();
            } else {
                Toast.makeText(ShareActivity.this, R.string.permission_explanation, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
