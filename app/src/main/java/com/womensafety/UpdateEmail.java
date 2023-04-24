package com.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateEmail extends AppCompatActivity {
    private FirebaseAuth authProfile;
    private FirebaseUser firebaseUser;
    private ProgressBar progressBar;
    private TextView textViewAuthenticated;
    private String userOldEmail, userNewEmail,userPwd;
    private Button buttonUpdateEmail;
    private EditText editTextNewEmail, editTextPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);
        getSupportActionBar().setTitle("Update Email");
        progressBar=findViewById(R.id.progressBar);
        editTextPwd=findViewById(R.id.editText_update_email_verify_password);
        editTextNewEmail=findViewById(R.id.editText_update_email_new);
        textViewAuthenticated=findViewById(R.id.textView_update_email_authenticated);
        buttonUpdateEmail=findViewById(R.id.button_update_email);
        buttonUpdateEmail.setEnabled(false);
        editTextNewEmail.setEnabled(false);
        authProfile=FirebaseAuth.getInstance();
        firebaseUser=authProfile.getCurrentUser();


        //Set email Id on TextView
        userOldEmail=firebaseUser.getEmail();
        TextView textViewOldEmail=findViewById(R.id.textView_update_email_old);
        textViewOldEmail.setText(userOldEmail);

        if(firebaseUser.equals(""))
        {
            Toast.makeText(UpdateEmail.this,"Something went wrong. User's details not available.",Toast.LENGTH_SHORT).show();
        }else{
            reauthenticate(firebaseUser);

        }

    }
   //Verify Email before updating
    private void reauthenticate(FirebaseUser firebaseUser) {
    Button buttonVerifyUser=findViewById(R.id.button_authenticate_user);
    buttonVerifyUser.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            userPwd=editTextPwd.getText().toString();
            if(TextUtils.isEmpty(userPwd))
            {
                Toast.makeText(UpdateEmail.this,"Password is needed to continue",Toast.LENGTH_SHORT).show();
                editTextPwd.setError("Please enter your password for authentication");
                editTextPwd.requestFocus();
            }else{
                progressBar.setVisibility(View.VISIBLE);
                AuthCredential credential= EmailAuthProvider.getCredential(userOldEmail,userPwd);
                firebaseUser.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(UpdateEmail.this,"Password is verified. Now update Email",Toast.LENGTH_SHORT).show();
                        textViewAuthenticated.setText("You are authenticated. You can update your email");
                        editTextNewEmail.setEnabled(true);
                        editTextPwd.setEnabled(false);
                        buttonVerifyUser.setEnabled(false);
                        buttonUpdateEmail.setEnabled(true);


                    }
                });
            }
        }
    });
    }
}