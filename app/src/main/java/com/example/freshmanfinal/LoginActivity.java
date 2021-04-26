package com.example.freshmanfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText email, password;
    private Button LoginBtn;
    private ProgressDialog loodingbar;
    private DatabaseReference getUserDataRefrense;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loodingbar = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.user_id);
        password = (EditText) findViewById(R.id.user_pass);
        LoginBtn =(Button) findViewById(R.id.loginbtn);
        LoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString();
                String Pass = password.getText().toString();
                UserLogin (Email,Pass);

            }
        });
    }

    private void UserLogin(String email, String pass)
    {
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(getApplicationContext(),"Please Enter Email",Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(pass))
        {
            Toast.makeText(getApplicationContext(),"Please Enter Password",Toast.LENGTH_SHORT).show();
        }
        else
        {
            loodingbar.setTitle("Login into Account");
            loodingbar.setMessage("Please Wait ,While we are varifying Your Information");
            loodingbar.show();

            mAuth.signInWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful())
                            {
                                String online_user_id = mAuth.getCurrentUser().getUid();
                                getUserDataRefrense = FirebaseDatabase.getInstance().getReference().child("Users").child(online_user_id);
                                getUserDataRefrense.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String role = dataSnapshot.child("Role").getValue().toString();
                                        if(role.equals("Admin"))
                                        {
                                            Toast.makeText(LoginActivity.this,"Admin",Toast.LENGTH_SHORT).show();
                                            Intent Admin = new Intent(LoginActivity.this , Adminn.class);
                                            Admin.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(Admin);
                                            finish();

                                        }
                                         if(role.equals("student"))
                                        {
                                            Intent Student = new Intent(LoginActivity.this , StudentDashboard.class);
                                           Student.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(Student);
                                            finish();
                                        }
                                         if(role.equals("depts_heads"))
                                        {
                                            Intent Dept = new Intent(LoginActivity.this , DepartmentHead.class);
                                            Dept.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |    Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(Dept);
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(LoginActivity.this,"Error Occured",Toast.LENGTH_SHORT).show();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this,"Wrong Password And Email , Please Enter Correct Password And Email",Toast.LENGTH_SHORT).show();
                            }

                            loodingbar.dismiss();

                        }
                    });
        }
    }

}
