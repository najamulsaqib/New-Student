package com.example.freshmanfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewStudents extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    String[] dept_list = {  "Cs & IT", "DPT", "Mass.Com","Aviation","MBBS","others"};
    String Department_name;
    String Text;
   public String selected_dept ;
    Spinner department;
    private RecyclerView Alluser;
    private DatabaseReference alluser_databaseReef,getUserDataRefrense;
    ValueEventListener listener;
    ArrayAdapter<String> adapter;
    ArrayList<String> spinnerDatalist;
    Query query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_students);


        department = (Spinner) findViewById(R.id._dept);
       department.setOnItemSelectedListener(this);

        spinnerDatalist = new ArrayList<String>();

        adapter = new ArrayAdapter(ViewStudents.this,
                android.R.layout.simple_spinner_item,spinnerDatalist);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       department.setAdapter(adapter);

        getdata();
        Alluser = (RecyclerView) findViewById(R.id.all_user_list);
        Alluser.setHasFixedSize(true);



        Alluser.setLayoutManager(new LinearLayoutManager(this));
        alluser_databaseReef = FirebaseDatabase.getInstance().getReference().child("Students");
        alluser_databaseReef.keepSynced(true);




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
//////////////////////////////////////

    @Override
    protected void onStart()
    {

        query = FirebaseDatabase.getInstance().getReference().child("Students")
                .orderByChild("Department")
                .equalTo(selected_dept);

        super.onStart();
        FirebaseRecyclerOptions<Allusers> option =
                new FirebaseRecyclerOptions.Builder<Allusers>()
                        .setQuery(alluser_databaseReef,Allusers.class)
                        .build();

        FirebaseRecyclerAdapter<Allusers,AllusersViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Allusers, AllusersViewHolder>(option)
        {
            @Override
            protected void onBindViewHolder(@NonNull final AllusersViewHolder holder, final int position, @NonNull final Allusers model)
            {
                holder.user_id.setText(model.getUser_id());
                holder.user_dpt.setText(model.getUser_dept());


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        String visit_User_id = getRef(position).getKey();
                        Intent profileIntent = new Intent(ViewStudents.this,ViewStudents.class);
                        profileIntent.putExtra("visit_user_id",visit_User_id);
                        startActivity(profileIntent);
                    }
                });


            }

            @NonNull
            @Override
            public AllusersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_user_display_layout,viewGroup,false);
                AllusersViewHolder viewHolder = new AllusersViewHolder(view);
                return viewHolder;
            }
        };

        Alluser.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }
    public static class AllusersViewHolder extends RecyclerView.ViewHolder
    {
        TextView user_id,user_dpt;
        CircleImageView profileimage;
        View mview;

        public AllusersViewHolder(@NonNull View itemView)
        {
            super(itemView);
            user_id = itemView.findViewById(R.id.all_user_username);
            user_dpt = itemView.findViewById(R.id.all_user_userstatus);
            profileimage = itemView.findViewById(R.id.all_user_profile_image);
        }
    }


///////////////////////////////


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         selected_dept = department.getSelectedItem().toString();

        Toast.makeText(getApplicationContext(),selected_dept , Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
