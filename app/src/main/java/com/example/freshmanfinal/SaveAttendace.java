package com.example.freshmanfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SaveAttendace extends AppCompatActivity {

    String Training_ID;
    FirebaseAuth mAuth;
    DatabaseReference StoreUserValues,StoreUserValues1;
    String tr_Date,tr_Room,tr_Timing,tr_Title,tr_traner;

    DatabaseReference getUserDataRefrense1,getUserDataRefrense12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_attendace);
        Training_ID = getIntent().getExtras().get("Training_ID").toString();
        mAuth = FirebaseAuth.getInstance();
        //Toast.makeText(getApplicationContext(),Training_ID,Toast.LENGTH_LONG).show();
        MarkAtt(Training_ID);
    }

    private void MarkAtt(final String training_id)
    {
        final String online_user_id = mAuth.getCurrentUser().getUid();
        //Toast.makeText(getApplicationContext(),online_user_id,Toast.LENGTH_SHORT).show();
        getUserDataRefrense1 = FirebaseDatabase.getInstance().getReference().child("Active_Trainings").child("Trainings").child(training_id);
        String ID = getUserDataRefrense1.getKey().toString();
        if(ID.equals(training_id))
        {
            getUserDataRefrense12 = FirebaseDatabase.getInstance().getReference().child("Trainings").child(training_id);
            getUserDataRefrense12.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    tr_Date = dataSnapshot.child("Date").getValue().toString();
                    tr_Room = dataSnapshot.child("Room").getValue().toString();
                    tr_Timing = dataSnapshot.child("Timing").getValue().toString();
                    tr_Title = dataSnapshot.child("Title").getValue().toString();
                    tr_traner = dataSnapshot.child("Trainer").getValue().toString();
                    StoreUserValues1= FirebaseDatabase.getInstance().getReference().child("Students").child(online_user_id).child("Training").child("Present").child(training_id);

                    StoreUserValues1.child("Title").setValue(tr_Title);

                    StoreUserValues1.child("Room").setValue(tr_Room);

                    StoreUserValues1.child("Timing").setValue(tr_Timing);

                    StoreUserValues1.child("Date").setValue(tr_Date);

                    StoreUserValues1.child("Trainer").setValue(tr_traner)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(getApplicationContext(),"Attendace Marked",Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(SaveAttendace.this,StudentDashboard.class);
                                        startActivity(i);
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(SaveAttendace.this,ScanQr.class);
                                        startActivity(i);


                                    }
                                }
                            });


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



           // StoreUserValues = FirebaseDatabase.getInstance().getReference().child("Trainings").child(training_id).child("Attendace").child("Present");
            //StoreUserValues.child(online_user_id).child("ID").setValue(online_user_id);


        }
        else
        {
            Toast.makeText(getApplicationContext(),"Attendace Not Marked",Toast.LENGTH_LONG).show();

        }
    }
}
