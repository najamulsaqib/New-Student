package com.example.freshmanfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import androidmads.library.qrgenearator.QRGSaver;

public class Start_Training extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    String resiver_tr_id,selected_trainer;
    EditText datee;
    EditText training_name , room_details,training_time;
    Bitmap qrBits;
    private String savePath = Environment.getExternalStorageDirectory().getPath() + "/QRCode/";
    Button Addtraining;
    Spinner department;
    DatabaseReference getUserDataRefrense;
    ValueEventListener listener;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDatalist;
    DatabaseReference StoreUserValues1,StoreUserValues;
     Calendar myCalendar;
     String Title , Room , Date , Timee ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start__training);

        resiver_tr_id = getIntent().getExtras().get("visit_user_id").toString();
         datee = (EditText) findViewById(R.id.add_training_date);
        training_name = (EditText) findViewById(R.id.add_training_name);
        room_details = (EditText) findViewById(R.id.add_training_room);
        training_time = (EditText) findViewById(R.id.add_training_time);
        Addtraining = (Button) findViewById(R.id.add_tr_btn);

        department = (Spinner) findViewById(R.id.Instructor_spn);
        department.setOnItemSelectedListener(this);

        spinnerDatalist = new ArrayList<String>();
        adapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,spinnerDatalist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department.setAdapter(adapter);
        set_data();
        getdata();

        Addtraining.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Title = training_name.getText().toString();
                Room = room_details.getText().toString();
                Timee = training_time.getText().toString();
                Update_Training(Title,Room,Timee);
                Add_Active_Training(Title,Room,Timee);
            }
        });


         //////////////date picker/////////////
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setDate();
            }
        };

         datee.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 new DatePickerDialog(Start_Training.this, date, myCalendar
                         .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                         myCalendar.get(Calendar.DAY_OF_MONTH)).show();

             }
         });
/////////////////////

    }


    private void setDate() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        datee.setText(sdf.format(myCalendar.getTime()));
        Date= datee.getText().toString();

        Toast.makeText(getApplicationContext(),Date,Toast.LENGTH_SHORT).show();


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
    public void set_data()
    {
        getUserDataRefrense = FirebaseDatabase.getInstance().getReference().child("Trainings").child(resiver_tr_id);
        getUserDataRefrense.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                String tile = dataSnapshot.child("Title").getValue().toString();
                String room = dataSnapshot.child("Room").getValue().toString();
                String instructor = dataSnapshot.child("Trainer").getValue().toString();



                training_name.setText(tile);
                room_details.setText(room);





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

    }
    public void Update_Training(String title, final String room , String time)
    {
        StoreUserValues1 = FirebaseDatabase.getInstance().getReference().child("Trainings").child(resiver_tr_id);
        StoreUserValues = FirebaseDatabase.getInstance().getReference().child("Active_Trainings").child(resiver_tr_id);
        StoreUserValues1.child("Title").setValue(title);
        StoreUserValues.child("Title").setValue(title);
        StoreUserValues1.child("Room").setValue(room);
        StoreUserValues.child("Room").setValue(room);
        StoreUserValues1.child("Timing").setValue(time);
        StoreUserValues.child("Timing").setValue(time);
        StoreUserValues1.child("Date").setValue(Date);
        StoreUserValues.child("Date").setValue(Date);
        StoreUserValues.child("Trainer").setValue(selected_trainer);
        StoreUserValues1.child("Trainer").setValue(selected_trainer)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                          //  QRGEncoder qrgEncoder = new QRGEncoder(resiver_tr_id, null, QRGContents.Type.TEXT, 200);
                         //   try {
                                // Getting QR-Code as Bitmap
                          //      qrBits = qrgEncoder.encodeAsBitmap();
                                // Setting Bitmap to ImageView
                                //qrr.setImageBitmap(qrBits);
                           //     Intent i = new Intent(Start_Training.this,QR_View.class);
                           //     i.putExtra("Qrbits",qrBits);
                           //     i.putExtra("Title",Title);
                           //     i.putExtra("id",resiver_tr_id);
                           //     startActivity(i);
                                // QRGSaver qrgSaver = new QRGSaver();
                                // qrgSaver.save(savePath, name.getText().toString().trim(), qrBits, QRGContents.ImageType.IMAGE_JPEG);
                         //   } catch (Exception e) {
                           //     e.printStackTrace();
                           // }
                            Toast.makeText(getApplicationContext(),"SUCCESSFULLY Added",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }



    public void Add_Active_Training(String title, final String room , String time)
    {

        StoreUserValues = FirebaseDatabase.getInstance().getReference().child("Active_Trainings").child("Trainings").child(resiver_tr_id);
        StoreUserValues.child("ID").setValue(resiver_tr_id);
        StoreUserValues.child("Title").setValue(title);
        StoreUserValues.child("Room").setValue(room);
        StoreUserValues.child("Timing").setValue(time);
        StoreUserValues.child("Date").setValue(Date);
        StoreUserValues.child("Trainer").setValue(selected_trainer)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if(task.isSuccessful())
                        {
                            QRGEncoder qrgEncoder = new QRGEncoder(resiver_tr_id, null, QRGContents.Type.TEXT, 200);
                            try {
                                // Getting QR-Code as Bitmap
                                qrBits = qrgEncoder.encodeAsBitmap();
                                // Setting Bitmap to ImageView
                                //qrr.setImageBitmap(qrBits);
                                Intent i = new Intent(Start_Training.this,QR_View.class);
                                i.putExtra("Qrbits",qrBits);
                                i.putExtra("Title",Title);
                                i.putExtra("id",resiver_tr_id);
                                startActivity(i);
                                // QRGSaver qrgSaver = new QRGSaver();
                                // qrgSaver.save(savePath, name.getText().toString().trim(), qrBits, QRGContents.ImageType.IMAGE_JPEG);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(),"SUCCESSFULLY Added",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
