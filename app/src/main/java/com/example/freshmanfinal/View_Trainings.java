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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class View_Trainings extends AppCompatActivity {
    private RecyclerView Alltrainer;
    private DatabaseReference alluser_databaseReef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__trainings);

        Alltrainer = (RecyclerView) findViewById(R.id.all_user_list);
        Alltrainer.setHasFixedSize(true);


        Alltrainer.setLayoutManager(new LinearLayoutManager(this));
        alluser_databaseReef = FirebaseDatabase.getInstance().getReference().child("Trainings");
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


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        String visit_User_id = getRef(position).getKey();
                        Intent profileIntent = new Intent(View_Trainings.this,Start_Training.class);
                        profileIntent.putExtra("visit_user_id",visit_User_id);
                        Toast.makeText(getApplicationContext(),visit_User_id , Toast.LENGTH_LONG).show();
                        startActivity(profileIntent);
                    }
                });


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
