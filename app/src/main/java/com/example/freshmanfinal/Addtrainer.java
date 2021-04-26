package com.example.freshmanfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Addtrainer extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    String[] dept_list = {  "Cs & IT", "DPT", "Mass.Com","Aviation","MBBS","others"};
    private EditText trainer_name;
    private RadioGroup rg;

    String Department_name;
    Button add_trainer;
    FirebaseAuth mAuth;
    DatabaseReference StoreUserValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtrainer);
        mAuth = FirebaseAuth.getInstance();
        final Spinner department = (Spinner) findViewById(R.id.new_trainer_dept);
        department.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,dept_list);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department.setAdapter(aa);

        trainer_name = (EditText) findViewById(R.id.new_trainer_name);
        rg = (RadioGroup) findViewById(R.id.radioSex);
        add_trainer = (Button) findViewById(R.id.add_tr);
        add_trainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedRadioButtonID = rg.getCheckedRadioButtonId();
                RadioButton selectedRadioButton = (RadioButton) findViewById(selectedRadioButtonID);
                final String Sexselected = selectedRadioButton.getText().toString();
                String T_name = trainer_name.getText().toString();
                Addtrainer( T_name,Sexselected,Department_name);
            }
        });
    }

    public void Addtrainer(String t_name, String sex , String dept_seclected)
    {
        StoreUserValues = FirebaseDatabase.getInstance().getReference().child("Trainers");
        StoreUserValues.push().setValue(t_name)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Addtrainer.this,"SUCCESSFULLY Added",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(Addtrainer.this,"Error",Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Department_name = dept_list[position];
        Toast.makeText(getApplicationContext(),dept_list[position] , Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
