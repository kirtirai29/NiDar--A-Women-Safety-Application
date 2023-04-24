package com.womensafety.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.womensafety.AboutUs;
import com.womensafety.BuildConfig;
import com.womensafety.ChoosenActivity;
import com.womensafety.Flashing;
import com.womensafety.Instructions;
import android.Manifest;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.womensafety.MainActivity;
import com.womensafety.NewsActivity;
import com.womensafety.R;
import com.womensafety.ReadWriteUserDetails;
import com.womensafety.SafeRoute;
import com.womensafety.ScreenOnOffBackgroundService;
import com.womensafety.ScreenOnOffReceiver;
import com.womensafety.SmsActivity;
import com.womensafety.databinding.FragmentHomeBinding;

import org.w3c.dom.Text;


public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    TextView user;
    private FirebaseAuth authProfile;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);



        Intent backgroundService = new Intent( getContext(), ScreenOnOffBackgroundService.class );
        getActivity().startService(backgroundService);
        Log.d( ScreenOnOffReceiver.SCREEN_TOGGLE_TAG, "Activity onCreate" );
        int permissionCheck = ContextCompat.checkSelfPermission (getContext(), Manifest.permission.SEND_SMS);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission (getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission (getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {


            final AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            View mView = getLayoutInflater().inflate(R.layout.custom_dialog_mainactivity,null);

            Button btn_okay = (Button)mView.findViewById(R.id.btn_okay);
            TextView heading=mView.findViewById (R.id.heading);
            heading.setText(R.string.access);
            TextView sms=mView.findViewById (R.id.sms);
            sms.setText(R.string.sms);
            TextView textView=mView.findViewById (R.id.textFormodal);
            textView.setText (R.string.permission);
            TextView location=mView.findViewById (R.id.location);
            location.setText(R.string.location);
            TextView locationText=mView.findViewById (R.id.textLocation);
            locationText.setText(R.string.liveloc);
            TextView call=mView.findViewById (R.id.call);
            call.setText(R.string.call);
            TextView callText=mView.findViewById (R.id.textCall);
            callText.setText(R.string.callpermission);
            TextView declaration=mView.findViewById (R.id.declaration);
            declaration.setText(R.string.declaration);
            TextView declaratioText=mView.findViewById (R.id.textDeclaration);
            declaratioText.setText(R.string.indiandeveloper);
            CheckBox checkbox = (CheckBox)mView.findViewById(R.id.checkBox);
            TextView checkBoxtext = (TextView)mView.findViewById(R.id.checkBoxText);
            checkbox.setVisibility (View.VISIBLE);
            checkBoxtext.setVisibility (View.VISIBLE);
            checkbox.setEnabled (true);
            checkBoxtext.setEnabled (true);

            checkbox.setText("");
            checkBoxtext.setText(Html.fromHtml("I accept the " +
                    "<a href='https://www.websitepolicies.com/policies/view/IaK4RjyB'>PRIVACY POLICY</a>"+" of the app"));
            checkBoxtext.setClickable(true);
            checkBoxtext.setMovementMethod(LinkMovementMethod.getInstance());
            alert.setView(mView);
            final AlertDialog alertDialog = alert.create();
            alertDialog.setCanceledOnTouchOutside(false);

            authProfile= FirebaseAuth.getInstance();
            FirebaseUser firebaseUser=authProfile.getCurrentUser();
           /* if(firebaseUser==null)
            {
                Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
            else
            {

                getUserData(firebaseUser);
            } */



            btn_okay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(checkbox.isChecked ()) {

                        alertDialog.dismiss ();
                        ActivityCompat.requestPermissions (getActivity(), new String[]{Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CALL_PHONE}, 0);
                    }else{
                        Toast.makeText (getContext(),"Please accept privacy policy",Toast.LENGTH_LONG).show ();

                    }
                }
            });
            alertDialog.show();

        }

        binding.Siren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), Flashing.class);
                startActivity(intent);
            }
        });

        binding.Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SmsActivity.class));
            }
        });
        binding.SafeRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SafeRoute.class));
            }
        });

        binding.News.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getActivity(), NewsActivity.class ) );
            }
        } );

       /* binding.aboutUs.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getActivity(), AboutUs.class ) );
            }

        } ); */

        binding.sendLocation.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getActivity(), Instructions.class ) );
            }
        } );


        binding.Currentlocation.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent( getActivity(), ChoosenActivity.class ) );
            }
        } );








        binding.constraintLayout2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {



                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "NiDar");
                    String shareMessage= "Hey Ladies,I am gifting a token of safety to all the females in my society as \n\n*NiDar* solves a very heart wrenching problem of our civilisation, *Women's Safety*. \n\nJust *download*,start using, and spread the app \n\nSo that any *female* related to you can feel safer and empowered in this world. \n\nDownload NiDar at:-\n";

                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID ;
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Share"));
                } catch(Exception e) {
                    //e.toString();
                }
            }
        });










        return binding.getRoot();
    }

   /* private void getUserData(FirebaseUser firebaseUser) {
        String userId= firebaseUser.getUid();
        DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Users");
        referenceProfile.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readWriteUserDetails=snapshot.getValue(ReadWriteUserDetails.class);
                if(readWriteUserDetails!=null)
                {
                    String use = readWriteUserDetails.getUsername();
                    binding.user.setText(use);
                    //set user DP

                    Uri uri=firebaseUser.getPhotoUrl();
                    Picasso.get().load(uri).into(binding.imgUser);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Something went wrong",Toast.LENGTH_SHORT).show();

            }
        });

    } */
}