package com.example.freshmanfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Adminn  extends AppCompatActivity implements View.OnClickListener {
    TextView name;
    private CardView addstudent,add_trainer,all_students,add_Session;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private DatabaseReference getUserDataRefrense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminn);
        mAuth = FirebaseAuth.getInstance();
        name = (TextView) findViewById(R.id.my_name);

        addstudent = (CardView) findViewById(R.id.add);
        add_trainer = (CardView) findViewById(R.id.add_trainers);
        add_Session = (CardView) findViewById(R.id.add_training);
        all_students = (CardView) findViewById(R.id.all_students);


        Toolbar toolbar = (Toolbar) findViewById(R.id.admin_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin-Dashboard");

        addstudent.setOnClickListener(this);
        add_trainer.setOnClickListener(this);
        add_Session.setOnClickListener(this);
        all_students.setOnClickListener(this);
        SetName();
        currentUser = mAuth.getCurrentUser();




    }

    public void SetName()
    {
        String online_user_id = mAuth.getCurrentUser().getUid();
        getUserDataRefrense = FirebaseDatabase.getInstance().getReference().child("Users").child(online_user_id);
        getUserDataRefrense.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String emaill = dataSnapshot.child("Email").getValue().toString();
                name.setText(emaill);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.add:
                Toast.makeText(getApplicationContext(),"Add student",Toast.LENGTH_SHORT).show();
                i = new Intent(Adminn.this,Addstudent.class);
                startActivity(i);
                break;
            case R.id.add_trainers:
                Toast.makeText(getApplicationContext(),"Add Trainer",Toast.LENGTH_SHORT).show();
                i = new Intent(Adminn.this,Addtrainer.class);
                startActivity(i);
                break;
            case R.id.all_students:
                Toast.makeText(getApplicationContext(),"view All Students",Toast.LENGTH_SHORT).show();
                i = new Intent(Adminn.this,ViewStudents.class);
                startActivity(i);
                break;
            case R.id.add_training:
                Toast.makeText(getApplicationContext(),"VIew Trainings",Toast.LENGTH_SHORT).show();
                i = new Intent(Adminn.this,View_Trainings.class);
                startActivity(i);
                break;
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);
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
        if (item.getItemId()== R.id.main_add_dpt_btn)
        {
            Intent i = new Intent(Adminn.this,Add_departments.class);
            startActivity(i);
        }
        if (item.getItemId()== R.id.main_traing_btn)
        {
            Intent i = new Intent(Adminn.this,Addtraining.class);
            startActivity(i);
        }

        return true;
    }
    private void LogOut()
    {
        Intent next= new Intent(Adminn.this,LoginActivity.class);
        startActivity(next);
        finish();
    }
}
