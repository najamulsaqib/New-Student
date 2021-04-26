package com.example.freshmanfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentDashboard extends AppCompatActivity implements View.OnClickListener {
    private CardView Mark_Attendace,View_Trainings,taken_tr,scan_qr1;
    TextView name;
    String st_name;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private DatabaseReference getUserDataRefrense1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);
        mAuth = FirebaseAuth.getInstance();
        name = (TextView) findViewById(R.id.st_name);
        Mark_Attendace = (CardView) findViewById(R.id.mark_attendace);
        View_Trainings = (CardView) findViewById(R.id.view_Training);
        taken_tr = (CardView) findViewById(R.id.Training_tak);
        scan_qr1 = (CardView) findViewById(R.id.Scan_Qr1);

        currentUser = mAuth.getCurrentUser();
        Toolbar toolbar = (Toolbar) findViewById(R.id.student_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Student-Dashboard");

        SetName();
        Mark_Attendace.setOnClickListener(this);
        View_Trainings.setOnClickListener(this);
        taken_tr.setOnClickListener(this);
        scan_qr1.setOnClickListener(this);


    }
    public void SetName()
    {
        String online_user_id = mAuth.getCurrentUser().getUid();
        //Toast.makeText(getApplicationContext(),online_user_id,Toast.LENGTH_SHORT).show();
        getUserDataRefrense1 = FirebaseDatabase.getInstance().getReference().child("Users").child(online_user_id);
        getUserDataRefrense1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String emaill = dataSnapshot.child("Email").getValue().toString();
               // Toast.makeText(getApplicationContext(),emaill,Toast.LENGTH_SHORT).show();
                name.setText(emaill);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.mark_attendace:
               // Toast.makeText(getApplicationContext(),"Add student",Toast.LENGTH_SHORT).show();
                i = new Intent(StudentDashboard.this,MarkAttendace.class);
                startActivity(i);
                break;
            case R.id.view_Training:
               // Toast.makeText(getApplicationContext(),"Add Trainer",Toast.LENGTH_SHORT).show();
                i = new Intent(StudentDashboard.this,Student_View_Trainings.class);
                startActivity(i);
                break;

            case R.id.Scan_Qr1:
                //Toast.makeText(getApplicationContext(),"SCAN QR 1",Toast.LENGTH_SHORT).show();
                i = new Intent(StudentDashboard.this,ScanQr.class);
                startActivity(i);
                break;
            case R.id.Training_tak:
                //Toast.makeText(getApplicationContext(),"SCAN QR 1",Toast.LENGTH_SHORT).show();
                i = new Intent(StudentDashboard.this,Taken_Trainings.class);
                startActivity(i);
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.student_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId()== R.id.main_logout_btn)
        {
            mAuth.signOut();
            LogOut();
        }


        return true;
    }
    private void LogOut()
    {
        Intent next= new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(next);
        finish();
    }
}
