package com.example.freshmanfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Selection extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference getUserDataRefrense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser;
        currentUser = mAuth.getCurrentUser();
        if(currentUser == null)
        {
            LogOut();
        }
        else
            {
                String online_user_id = mAuth.getCurrentUser().getUid();
                getUserDataRefrense = FirebaseDatabase.getInstance().getReference().child("Users").child(online_user_id);
                getUserDataRefrense.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String role = dataSnapshot.child("Role").getValue().toString();
                        if(role.equals("Admin"))
                        {
                            Toast.makeText(Selection.this,"Admin",Toast.LENGTH_SHORT).show();
                            Intent Admin = new Intent(Selection.this , Adminn.class);
                            Admin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Admin);
                            finish();

                        }
                        else if(role.equals("student"))
                        {
                            Toast.makeText(Selection.this,"Student",Toast.LENGTH_SHORT).show();
                            Intent Student = new Intent(Selection.this , StudentDashboard.class);
                            Student.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Student);
                            finish();
                        }
                        else if(role.equals("depts_heads"))
                        {
                            Intent Dept = new Intent(Selection.this , DepartmentHead.class);
                            Dept.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(Dept);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(Selection.this,"Error Occured",Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        }
    }
    private void LogOut()
    {
        Intent next= new Intent(Selection.this,LoginActivity.class);
        startActivity(next);
        finish();
    }

}
