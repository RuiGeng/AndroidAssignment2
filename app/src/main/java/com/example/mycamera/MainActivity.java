package com.example.mycamera;

import android.app.AlertDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static Bitmap bmp;
    private static FingerView v;
    public static String inputText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        v = (FingerView)findViewById(R.id.imageView);

        inputText = "";

        Button buttonText = (Button)findViewById(R.id.buttonText);
        Button buttonSketch = (Button)findViewById(R.id.buttonSketch);
    }

    //Event handler for Capture button
    public void takeSelfie(View view){
        v.setImageResource(android.R.color.transparent);
        v.clear();
        inputText = "";

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    //Event handler for Text button
    public void addText(View view){
        // create Intent to take a picture and return control to the calling application
        FingerView.action = "text";

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Textual Comments");

        // Set up the input
        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputText = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                inputText = "";
                dialog.cancel();
            }
        });

        builder.show();
    }

    //Event handler for Sketch button
    public void addSketch(View view){
        FingerView.action  = "sketch";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Bundle extras = data.getExtras();
                bmp = (Bitmap) extras.get("data");

                //Set the image that we took as the image on the imageview
                v.setImageBitmap(bmp);
            }
        }
    }

    public void saveSelfie(View view){
        v.buildDrawingCache();
        bmp = v.getDrawingCache();
        savePhoto(bmp);
        v.setImageResource(android.R.color.transparent);
        v.clear();
    }

    private String savePhoto(Bitmap bmp)
    {
        File imageFileFolder = new File(Environment.getExternalStorageDirectory(),"MyCamera");
        imageFileFolder.mkdir();
        FileOutputStream out = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File imageFileName = new File(imageFileFolder, timeStamp + ".jpg");
        try
        {
            out = new FileOutputStream(imageFileName);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            scanPhoto(imageFileName.toString());
            out = null;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return imageFileName.toString();
    }

    private void scanPhoto(String imageFileName)
    {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imageFileName);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
}
