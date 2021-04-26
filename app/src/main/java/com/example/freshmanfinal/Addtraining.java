package com.example.freshmanfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Addtraining extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    EditText training_name , room_details;
    String tr_name,room_tr,selected_trainer;
    Button Addtraining;
    Spinner department;
    DatabaseReference getUserDataRefrense;
    ValueEventListener listener;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDatalist;
    DatabaseReference StoreUserValues,StoreUserValues1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtraining);

        training_name = (EditText) findViewById(R.id.add_training_name);
        room_details = (EditText) findViewById(R.id.add_training_room);
        Addtraining = (Button) findViewById(R.id.add_tr_btn);

        department = (Spinner) findViewById(R.id.Instructor_spn);
        department.setOnItemSelectedListener(this);

        spinnerDatalist = new ArrayList<String>();

        adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,spinnerDatalist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department.setAdapter(adapter);

        getdata();
        Addtraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String T_name = training_name.getText().toString();
                String T_room = room_details.getText().toString();


                Addtrainer( T_name,T_room,selected_trainer);
            }
        });


    }
    public void Addtrainer(String t_name, String Room , String dept_trainer)
    {
        StoreUserValues = FirebaseDatabase.getInstance().getReference().child("Trainings");
        String key = StoreUserValues.push().getKey();
        StoreUserValues1 = FirebaseDatabase.getInstance().getReference().child("Trainings").child(key);
        StoreUserValues1.child("ID").setValue(key);
        StoreUserValues1.child("Title").setValue(t_name);
        StoreUserValues1.child("Room").setValue(Room);
        StoreUserValues1.child("Trainer").setValue(selected_trainer)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Addtraining.this,"SUCCESSFULLY Added",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(Addtraining.this,"Error",Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected_trainer = department.getSelectedItem().toString();

        Toast.makeText(getApplicationContext(),selected_trainer, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getdata()
    {
        getUserDataRefrense = FirebaseDatabase.getInstance().getReference("Trainers");
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
}
