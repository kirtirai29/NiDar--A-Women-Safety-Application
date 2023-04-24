package com.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.womensafety.databinding.ActivityForgetBinding;

public class ForgetActivity extends AppCompatActivity {
    private ActivityForgetBinding binding;
    FirebaseAuth authProfile;
    ProgressBar progressBar;
    Button btnforget;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        progressBar=findViewById(R.id.circle);
        email = findViewById(R.id.forEmail);
        btnforget = findViewById(R.id.btnForgetPassword);
        btnforget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail= email.getText().toString();
                if(TextUtils.isEmpty(mail)) {
                    email.setError("Required");
                    email.requestFocus();
                } else if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches())
                {
                    Toast.makeText(ForgetActivity.this,"Re-enter your Email",Toast.LENGTH_SHORT).show();
                    email.setError("Valid email is required");
                    email.requestFocus();

                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    forgetPassword(mail);
                }
            }
        });
    }



    private void forgetPassword(String mail) {

        authProfile=FirebaseAuth.getInstance();
        authProfile.sendPasswordResetEmail(mail)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful())
                        {
                            Toast.makeText(ForgetActivity.this,"Check Your Mail Inbox", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(ForgetActivity.this, Login.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            try {
                                throw task.getException();
                            }catch (FirebaseAuthInvalidUserException e)
                            {
                                email.setError("User does'nt exist or no longer valid");
                            }catch (Exception e)
                            {
                                Log.e("TAG",e.getMessage());
                                Toast.makeText(ForgetActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

}