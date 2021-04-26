package com.example.freshmanfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;

public class ScanQr extends AppCompatActivity {

    CodeScanner codeScanner;
    CodeScannerView scannerView;
    TextView resultData;
    Button mark_bt;
    DatabaseReference getUserDataRefrense1;
    DatabaseReference StoreUserValues1,StoreUserValues,StoreUserValues2;
    FirebaseAuth mAuth;
    String rs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        mAuth = FirebaseAuth.getInstance();
        mark_bt = (Button) findViewById(R.id.mark_At_button);
        int MY_PERMISSIONS_REQUEST_CAMERA=0;
        // Here, this is the current activity

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA))
            {

            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA );
                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }


        scannerView = findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(this,scannerView);
        resultData = findViewById(R.id.resultofQR);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        resultData.setText(result.getText());
                         rs = result.getText();


                    }
                });
            }

        });
        mark_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ScanQr.this,SaveAttendace.class);
                i.putExtra("Training_ID",rs);
                startActivity(i);
            }
        });


    }
    @Override
    protected void onResume() {
        super.onResume();
        //Search(rs);
        codeScanner.startPreview();
    }
    @Override
    protected void onPause() {
       // codeScanner.releaseResources();
       // Search(rs);

        super.onPause();
    }

    public void Search(final String result)
    {


    }

    public void Add_Attendance(String Training_id)
    {
        FirebaseUser currentUser;
        currentUser = mAuth.getCurrentUser();
        String Current_ID = mAuth.getCurrentUser().getUid();
        //Toast.makeText(ScanQr.this, Current_ID, Toast.LENGTH_LONG).show();
        Toast.makeText(ScanQr.this, Training_id, Toast.LENGTH_LONG).show();
        StoreUserValues1 = FirebaseDatabase.getInstance().getReference().child("Trainings").child(Training_id).child("Attendace").child("Present");
        StoreUserValues1.push().setValue(Current_ID);
        StoreUserValues = FirebaseDatabase.getInstance().getReference().child("Students").child(Current_ID).child("Trainings_taken");
        StoreUserValues.push().setValue(Training_id)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {

                            Toast.makeText(getApplicationContext(),"SUCCESSFULLY Present",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Error Occured",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}