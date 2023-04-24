package com.womensafety;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.womensafety.databinding.ActivityMainBinding;
import com.womensafety.databinding.NavDrawerLayoutBinding;
import com.womensafety.databinding.ToolbarLayoutBinding;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    TextView usernam;
    private NavDrawerLayoutBinding navDrawerLayoutBinding;
    private ActivityMainBinding activityMainBinding;
    private ToolbarLayoutBinding toolbarLayoutBinding;
    private FirebaseAuth authProfile;
    CircleImageView imageView;

    String use;
    MenuItem item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navDrawerLayoutBinding = NavDrawerLayoutBinding.inflate(getLayoutInflater());
        setContentView(navDrawerLayoutBinding.getRoot());
        activityMainBinding = navDrawerLayoutBinding.mainActivity;
        toolbarLayoutBinding = activityMainBinding.toolbar;
        setSupportActionBar(toolbarLayoutBinding.toolbar);
        authProfile= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=authProfile.getCurrentUser();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, navDrawerLayoutBinding.navDrawer,
                toolbarLayoutBinding.toolbar,
                R.string.navigation_open,
                R.string.navigation_close
        );
        navDrawerLayoutBinding.navDrawer.addDrawerListener(toggle);
        toggle.syncState();
        NavController navController = Navigation.findNavController(this, R.id.fragmentContainer);
        NavigationUI.setupWithNavController(
                navDrawerLayoutBinding.navigationView,
                navController
        );



        View headerLayout= navDrawerLayoutBinding.navigationView.getHeaderView(0);
        imageView=headerLayout.findViewById(R.id.img);
        usernam= headerLayout.findViewById(R.id.Uname);

        if(firebaseUser==null)
        {
            Toast.makeText(MainActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();
        }
        else
        {

            getUserData(firebaseUser);
        }






    }



    @Override
    public void onBackPressed() {
        if(navDrawerLayoutBinding.navDrawer.isDrawerOpen(GravityCompat.START))
            navDrawerLayoutBinding.navDrawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    public void getUserData(FirebaseUser firebaseUser)
    {
        String userId= firebaseUser.getUid();
        DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Users");
        referenceProfile.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails=snapshot.getValue(ReadWriteUserDetails.class);
                if(readWriteUserDetails!=null)
                {
                    use=readWriteUserDetails.username;
                    usernam.setText(use);
                    //set user DP

                    Uri uri=firebaseUser.getPhotoUrl();
                    Picasso.get().load(uri).into(imageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this,"Something went wrong",Toast.LENGTH_SHORT).show();

            }
        });

    }



}
