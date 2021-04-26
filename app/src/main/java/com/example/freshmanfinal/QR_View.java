package com.example.freshmanfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGSaver;

public class QR_View extends AppCompatActivity {

    ImageView QR_image;
    Button save_btn;
    String resiver_tr_id;
    String currentPhotoPath;
    DatabaseReference StoreUserValues,StoreUserValues1;
    Uri URI;
    Bitmap qrBits;
    String Title;
    private AppCompatActivity activity;
    public String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr__view);
        save_btn = (Button) findViewById(R.id.save_qr);
        QR_image = (ImageView) findViewById(R.id.qr_image);
        resiver_tr_id = getIntent().getExtras().get("id").toString();
        Title = getIntent().getExtras().get("Title").toString();
        activity = this;

        Intent intent = getIntent();
        qrBits = (Bitmap) intent.getParcelableExtra("Qrbits");
        QR_image.setImageBitmap(qrBits);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                 //   try {
                   //    createImageFile();
               String ImagePath = MediaStore.Images.Media.insertImage(
                        getContentResolver(),
                        qrBits,
                        Title,
                        "demo_image"
                );

                 URI = Uri.parse(ImagePath);

                Toast.makeText(activity, "Stored Image "+Title, Toast.LENGTH_LONG).show();



               // Addtrainer();
                     //   boolean save = new QRGSaver().save(currentPhotoPath, resiver_tr_id, qrBits, QRGContents.ImageType.IMAGE_JPEG);
                       // String result = save ? "Image Saved" : "Image Not Saved";
                       // galleryAddPic();
                       // Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
                    ///}
                    //catch (Exception e) {
                      //  e.printStackTrace();
                    //}
                //} else {
                  //  ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                //}
            }
        });

    }
    public void Addtrainer(String t_name, String Room , String dept_trainer)
    {
        StoreUserValues = FirebaseDatabase.getInstance().getReference().child("Start_Trainings");
        String key = StoreUserValues.push().getKey();
        StoreUserValues1 = FirebaseDatabase.getInstance().getReference().child("Start_Trainings").child(key);
        StoreUserValues1.child("ID").setValue(key);
        StoreUserValues1.child("Tr_id").setValue(t_name);
        //StoreUserValues1.child("Room").setValue(Room);
        StoreUserValues1.child("Trainer").setValue(Room)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(),"SUCCESSFULLY Added",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

}
