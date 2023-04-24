package com.womensafety;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {
    FirebaseAuth authProfile;

    ImageView btnback;
    RelativeLayout logout,changePassword,lang,sendMess,aboutus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        authProfile= FirebaseAuth.getInstance();

        btnback=findViewById(R.id.backbtn);
        logout=findViewById(R.id.logout);
        changePassword=findViewById(R.id.changePassword);
        lang=findViewById(R.id.lang);
        sendMess=findViewById(R.id.sendMess);
        aboutus=findViewById(R.id.aboutus);

        aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this,AboutUs.class));
            }
        });

        sendMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                composeEmail();
            }
        });
        lang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Settings.this,Language2.class));
            }
        });

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authProfile.signOut();
                Intent intent=new Intent(Settings.this,Login.class);

                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                        | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText resetMail=new EditText(view.getContext());
                final AlertDialog.Builder passwordDialog= new AlertDialog.Builder(view.getContext());
                passwordDialog.setTitle("Change Password");
                passwordDialog.setMessage("Enter your New Password ");
                passwordDialog.setView(resetMail);

                passwordDialog.setPositiveButton("Update",(DialogInterface.OnClickListener) (dialog, which) -> {
                    String newPassword= resetMail.getText().toString();
                    FirebaseAuth.getInstance().getCurrentUser().updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(Settings.this,"Password Changed Successfully",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Settings.this,"Error:"+e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                });
                passwordDialog.setNegativeButton("Cancel",(DialogInterface.OnClickListener) (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    dialog.cancel();
                });

                passwordDialog.create().show();
            }
        });

    }
    public void composeEmail() {
        String subject="Contacting for NiDar";
        String mailto = "mailto:kirtirai2906@gmail.com" +
                "?cc="+"janvigupta411@gmail.com"+
                "&subject=" + Uri.encode(subject);


        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse(mailto));

        try {
            startActivity(emailIntent);
        } catch (ActivityNotFoundException e) {
            //TODO: Handle case where no email app is available
        }


    }
}