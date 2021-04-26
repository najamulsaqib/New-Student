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

import de.hdodenhof.circleimageview.CircleImageView;

public class Taken_Trainings extends AppCompatActivity {
    private RecyclerView Alltrainer;
    private DatabaseReference alluser_databaseReef;
    Query query;
    FirebaseAuth mauth;
    String Current_User_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taken__trainings);
        mauth = FirebaseAuth.getInstance();
        Current_User_ID = mauth.getCurrentUser().getUid();
        Alltrainer = (RecyclerView) findViewById(R.id.all_user_list);
        Alltrainer.setHasFixedSize(true);


        Alltrainer.setLayoutManager(new LinearLayoutManager(this));
        alluser_databaseReef = FirebaseDatabase.getInstance().getReference().child("Students").child(Current_User_ID).child("Training").child("Present");
        alluser_databaseReef.keepSynced(true);
    }


//////////////////////////////////////

    @Override
    protected void onStart()
    {



        super.onStart();
        FirebaseRecyclerOptions<AllTrainers> option =
                new FirebaseRecyclerOptions.Builder<AllTrainers>()
                        .setQuery(alluser_databaseReef,AllTrainers.class)
                        .build();

        FirebaseRecyclerAdapter<AllTrainers, View_Trainings.AllTrainersViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<AllTrainers, View_Trainings.AllTrainersViewHolder>(option)
        {
            @Override
            protected void onBindViewHolder(@NonNull final View_Trainings.AllTrainersViewHolder holder, final int position, @NonNull final AllTrainers model)
            {
                holder.Title.setText(model.getTR_Title());
                holder.Room.setText(model.get_Room());
                holder.Trainer.setText(model.get_Trainer());




            }

            @NonNull
            @Override
            public View_Trainings.AllTrainersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_trainer_display_layout,viewGroup,false);
                View_Trainings.AllTrainersViewHolder viewHolder = new View_Trainings.AllTrainersViewHolder(view);
                return viewHolder;
            }
        };

        Alltrainer.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }
    public static class AllTrainersViewHolder extends RecyclerView.ViewHolder
    {
        TextView Title,Room,Trainer,Date;
        CircleImageView profileimage;
        View mview;

        public AllTrainersViewHolder(@NonNull View itemView)
        {
            super(itemView);
            Title = itemView.findViewById(R.id.all_trainer_title);
            Room = itemView.findViewById(R.id.all_trainig_room);
            Trainer = itemView.findViewById(R.id.all_trainig_Instructor);
            Date = itemView.findViewById(R.id.all_trainig_date);
            // Room = itemView.findViewById(R.id.all_user_profile_image);
        }
    }


///////////////////////////////


}
