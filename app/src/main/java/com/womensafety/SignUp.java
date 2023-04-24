package com.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.womensafety.databinding.ActivitySignUpBinding;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    ImageView btnback;
    EditText username, email, phone, password;
    Button signup; TextView login;
    ProgressBar progressBar;
    FirebaseDatabase db;
    ActivitySignUpBinding binding;
    ReadWriteUserDetails readWriteUserDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressBar=findViewById(R.id.progressBar);
        // btnback=findViewById(R.id.btnBack);
        username=findViewById(R.id.edtUsername);
        email=findViewById(R.id.edtEmail);
        phone=findViewById(R.id.edtPhone);
        password=findViewById(R.id.edtPassword);
       // login=findViewById(R.id.txtLogin);
       // signup=findViewById(R.id.btnSignUp);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(SignUp.this,Login.class));

            }
        });

        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName=username.getText().toString();
                String Email=email.getText().toString();
                String Phone=phone.getText().toString();
                String Password=password.getText().toString();

                if(TextUtils.isEmpty(userName))
                {
                    Toast.makeText(SignUp.this,"Username can't be empty",Toast.LENGTH_SHORT).show();
                    username.setError("Field is required");
                    username.requestFocus();
                }else if(TextUtils.isEmpty(Email))
                {
                    Toast.makeText(SignUp.this,"Email can't be empty",Toast.LENGTH_SHORT).show();
                    email.setError("Field is required");
                    email.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
                {
                    Toast.makeText(SignUp.this,"Re-enter your Email",Toast.LENGTH_SHORT).show();
                    email.setError("Valid email is required");
                    email.requestFocus();

                }
                else if(TextUtils.isEmpty(Phone))
                {
                    Toast.makeText(SignUp.this,"Phone number can't be empty",Toast.LENGTH_SHORT).show();
                    phone.setError("Field is required");
                    phone.requestFocus();

                }
                else if(Phone.length()!=10)
                {
                    Toast.makeText(SignUp.this,"Re-enter Phone number ",Toast.LENGTH_SHORT).show();
                    phone.setError("Valid Number is required");
                    phone.requestFocus();
                }
                else if(TextUtils.isEmpty(Password))
                {
                    Toast.makeText(SignUp.this,"Password can't be empty",Toast.LENGTH_SHORT).show();
                    password.setError("Field is required");
                    password.requestFocus();

                }
                else if(Password.length()<6)
                {
                    Toast.makeText(SignUp.this,"Password can't be less than 6 character",Toast.LENGTH_SHORT).show();
                    password.setError("Field is required");
                    password.requestFocus();

                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(userName, Email, Password, Phone);
                }

            }
        });



    }

    private void registerUser(String userName, String Email, String Password, String Phone) {
        FirebaseAuth auth=FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful())
                {

                    FirebaseUser firebaseUser=auth.getCurrentUser();

                    //Save user data in Firebase Realtime Database
                   readWriteUserDetails= new ReadWriteUserDetails(userName, Email, Phone);
                    db=FirebaseDatabase.getInstance();
                    DatabaseReference referenceProfile= db.getReference("Users");
                    referenceProfile.child(firebaseUser.getUid()).setValue(readWriteUserDetails).addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()) {
                                        Toast.makeText(SignUp.this, "Registration is Successful. Please Verify your Email", Toast.LENGTH_SHORT).show();
                                        firebaseUser.sendEmailVerification();
                  /*  // Open new Activity
                    Intent intent=new Intent(SignUp.this,Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                    | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish(); */
                                    }
                                    else
                                    {
                                        Toast.makeText(SignUp.this, "Registration is failed. Try again Later", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            }
                    );




                }
                else
                {
                    try {
                        throw task.getException();

                    }
                    catch (FirebaseAuthWeakPasswordException e)
                    {
                        password.setError("Password is weak. Kindly enter mix of alphabets, digits etc");
                        password.requestFocus();
                    }catch (FirebaseAuthInvalidCredentialsException e)
                    {
                        password.setError("Your email is invalid or Already in use. Kindly re-enter.");
                        password.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e)
                    {
                        email.setError("User is already registered with this email.");
                        email.requestFocus();
                    } catch (Exception e)
                    {
                        Log.e("TAG",e.getMessage());
                        Toast.makeText(SignUp.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                    progressBar.setVisibility(View.GONE);

                }
            }
        });
    }
}