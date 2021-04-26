package com.example.freshmanfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Add_departments extends AppCompatActivity {
    private EditText dept_name;

    Button add_dept;
    FirebaseAuth mAuth;
    DatabaseReference StoreUserValues;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_departments);
        dept_name = (EditText) findViewById(R.id.new_department_name);

        add_dept = (Button) findViewById(R.id.add_dpt);
        add_dept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String T_name = dept_name.getText().toString();
                Adddept(T_name);
            }
        });
    }
    public void Adddept(String t_name)
    {
        StoreUserValues = FirebaseDatabase.getInstance().getReference().child("Departments");
        StoreUserValues.push().setValue(t_name)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(Add_departments.this,"SUCCESSFULLY Added",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(Add_departments.this,"Error",Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

}
