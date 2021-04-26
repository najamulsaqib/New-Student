package com.example.freshmanfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Addstudent extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{

    FirebaseAuth mAuth1 , mAuth2;
    EditText email , password ;
    Button CreateUser;
    String selected_dept;
    DatabaseReference getUserDataRefrense;
    String Department_name;
    DatabaseReference StoreUserValues,StoreUserValues1;
    RadioGroup rg;

    Spinner department;
    ValueEventListener listener;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDatalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addstudent);
        mAuth1 = FirebaseAuth.getInstance();

        department = (Spinner) findViewById(R.id.new_user_dept);
        department.setOnItemSelectedListener(this);

        spinnerDatalist = new ArrayList<String>();

        adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,spinnerDatalist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department.setAdapter(adapter);
        getdata();

        rg = (RadioGroup) findViewById(R.id.radioSex);


        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("[https://fypfirebaseproject-a0a79.firebaseio.com/]")
                .setApiKey("AIzaSyDndiljFo3VIz3nw5-ebe5Umz9cnIwuM48")
                .setApplicationId("fypfirebaseproject-a0a79").build();

        try { FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions, "FreshManApp");
            mAuth2 = FirebaseAuth.getInstance(myApp);
        } catch (IllegalStateException e){
            mAuth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance("FreshManApp"));
        }
        email = findViewById(R.id.new_user_email);
        password = findViewById(R.id.new_user_passs);
        CreateUser =  findViewById(R.id.create_user);
        CreateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioButtonID = rg.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
                final String Sexselected = selectedRadioButton.getText().toString();
                final String EMAIL = email.getText().toString();
                final String PASS = password.getText().toString();

                CreateUserNew(EMAIL,PASS,Sexselected);

            }
        });
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected_dept = department.getSelectedItem().toString();

        Toast.makeText(getApplicationContext(),selected_dept, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getdata()
    {
        getUserDataRefrense = FirebaseDatabase.getInstance().getReference("Departments");
        listener = getUserDataRefrense.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot item:dataSnapshot.getChildren())
                {
                    spinnerDatalist.add(item.getValue().toString());
                }
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void CreateUserNew(final String email, final String password,final String Sex)
    {
        mAuth2.createUserWithEmailAndPassword(email, password)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (!task.isSuccessful()) {
                            String ex = task.getException().toString();
                            Toast.makeText(Addstudent.this, "Registration Failed"+ex,
                                    Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            String userID =  mAuth2.getCurrentUser().getUid();
                            Toast.makeText(Addstudent.this,userID,Toast.LENGTH_SHORT).show();
                            StoreUserValues = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                            StoreUserValues1 = FirebaseDatabase.getInstance().getReference().child("Students").child(userID);
                            StoreUserValues.child("Email").setValue(email);
                            StoreUserValues1.child("Email").setValue(email);
                            StoreUserValues.child("Password").setValue(password);
                            StoreUserValues1.child("Password").setValue(password);
                            StoreUserValues.child("Department").setValue(selected_dept);
                            StoreUserValues1.child("Department").setValue(selected_dept);
                            StoreUserValues.child("Gender").setValue(Sex);
                            StoreUserValues1.child("Gender").setValue(Sex);
                            StoreUserValues1.child("Role").setValue("student");
                            StoreUserValues.child("Role").setValue("student")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful())
                                            {
                                                Toast.makeText(Addstudent.this,"SUCCESSFULLY LOADED",Toast.LENGTH_LONG).show();

                                            }
                                            else
                                            {
                                                Toast.makeText(Addstudent.this,"Error",Toast.LENGTH_LONG).show();
                                            }

                                        }
                                    });
                            mAuth2.signOut();
                        }




                    }
                });


    }
}
