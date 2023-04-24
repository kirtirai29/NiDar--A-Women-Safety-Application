package com.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    ImageView back,editDp,editNumber,editEmail;
    TextView username, email, phone;
    String u, e,p;
    CircleImageView img;
    private FirebaseAuth  authProfile;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        swipeToRefresh();
        back=findViewById(R.id.back);
        username= findViewById(R.id.username);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        img=findViewById(R.id.imgFormal);
        editDp=findViewById(R.id.imgEdit);

        authProfile=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=authProfile.getCurrentUser();

        editDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this,UploadActivity.class));
                finish();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        showUserProfile(firebaseUser);


    }

    private void swipeToRefresh() {
        swipeContainer=findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                startActivity(getIntent());
                finish();
                overridePendingTransition(0,0);
                swipeContainer.setRefreshing(false);

            }
        });
        swipeContainer.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright),getResources().getColor(android.R.color.holo_green_light));

    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userId=firebaseUser.getUid();
        DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Users");
        referenceProfile.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails userModel=snapshot.getValue(ReadWriteUserDetails.class);
                if(userModel!=null)
                {
                     u=userModel.username;
                     e=userModel.email;
                     p=userModel.phone;
                    username.setText(u);
                    email.setText(e);
                    phone.setText(p);

                    //set user DP

                    Uri uri=firebaseUser.getPhotoUrl();
                    Picasso.get().load(uri).into(img);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this,"Something went wrong..Try later on",Toast.LENGTH_SHORT).show();



            }
        });
    }
}