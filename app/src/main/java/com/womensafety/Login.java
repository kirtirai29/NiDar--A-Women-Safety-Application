package com.womensafety;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText email, password;
    TextView forget;
    ProgressBar progressBar;
  Button signup,log;
  FirebaseAuth authProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        authProfile=FirebaseAuth.getInstance();

        email = findViewById(R.id.edtEmail);
        password= findViewById(R.id.edtPassword);

        progressBar= findViewById(R.id.circlewala);
        forget= findViewById(R.id.txtForgetPassword);

        log=findViewById(R.id.btnLogin);


        signup=findViewById(R.id.btnSignUp);

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,ForgetActivity.class));

            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtemail=email.getText().toString();
                String pass= password.getText().toString();
                if(TextUtils.isEmpty(txtemail))
                {
                    email.setError("Email is required");
                    email.requestFocus();
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(txtemail).matches())
                {
                    Toast.makeText(Login.this,"Re-enter your Email",Toast.LENGTH_SHORT).show();
                    email.setError("Valid email is required");
                    email.requestFocus();

                }
                else if(TextUtils.isEmpty(pass))
                {
                    password.setError("Email is required");
                    password.requestFocus();
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(txtemail, pass);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,SignUp.class));
            }
        });



    }

    private void loginUser(String txtemail, String pass) {
        authProfile.signInWithEmailAndPassword(txtemail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser firebaseUser=authProfile.getCurrentUser();
                   if(firebaseUser.isEmailVerified())
                   {Toast.makeText(Login.this,"Logged in Successfully",Toast.LENGTH_SHORT).show();

                       Intent intent=new Intent(Login.this, MainActivity.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
                               | Intent.FLAG_ACTIVITY_NEW_TASK);
                       startActivity(intent);
                       finish();
                   }
                   else{
                       firebaseUser.sendEmailVerification();
                       authProfile.signOut();
                       showAlertDialog();
                   }
                }
                else
                {
                    try{
                        throw task.getException();
                    }
                    catch (FirebaseAuthInvalidCredentialsException e){
                        email.setError("Invalid Credentials");
                        email.requestFocus();
                    }catch (FirebaseAuthInvalidUserException e)
                    {
                        email.setError("User does not exist");
                        email.requestFocus();

                    } catch (Exception e){
                        Log.e("TAG",e.getMessage());
                        Toast.makeText(Login.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(Login.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Please verify your email now. You can't login without verifying.");
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    //to check if user is already log in or not
    @Override
    protected void onStart() {
        super.onStart();
        if(authProfile.getCurrentUser()!=null)
        {
            Toast.makeText(Login.this,"Already Logged in",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Login.this, MainActivity.class));
        }else
        {
            Toast.makeText(Login.this,"Login Now",Toast.LENGTH_SHORT).show();

        }

    }
}