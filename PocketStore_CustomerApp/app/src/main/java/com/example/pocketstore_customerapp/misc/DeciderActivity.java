package com.example.pocketstore_customerapp.misc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.pocketstore_customerapp.R;
import com.example.pocketstore_customerapp.home.NearbyShopsActivity;
import com.example.pocketstore_customerapp.misc.sharedPreferences.UserInfoSharedPreferences;
import com.example.pocketstore_customerapp.signup.setupProfile.Customer;
import com.example.pocketstore_customerapp.signup.setupProfile.SetupProfileActivity;
import com.example.uicomponents_module.CustomProgressDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class DeciderActivity extends AppCompatActivity {
    // decide here where to go after phone verification is done
    // also fetch user info here if user already exists

    private CustomProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decider);

        progressDialog = new CustomProgressDialog();

        decide();

    }

    private void decide() {

        progressDialog.show(getSupportFragmentManager(),""); //showing progress dialog

        String userID= FirebaseAuth.getInstance().getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(Utils.customersNode);
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Intent intent;

                if (snapshot.exists()) {
                    // user has an account no need to signup

                    String userId = (String) snapshot.child(Utils.userIdNode).getValue();
                    String userName = (String) snapshot.child(Utils.userNameNode).getValue();
                    String email = (String) snapshot.child(Utils.emailNode).getValue();
                    String phoneNumber = (String) snapshot.child(Utils.customerPhoneNumberNode).getValue();
                    String homeAddress = (String) snapshot.child(Utils.homeAddressNode).getValue();
                    double latitude = (double) snapshot.child(Utils.customerLatitudeNode).getValue();
                    double longitude = (double) snapshot.child(Utils.customerLongitudeNode).getValue();

                    Customer customer = new Customer(userId, userName,
                            phoneNumber, homeAddress, email, latitude, longitude);

                    storeExistingUserInfoLocally(customer);

                    intent = new Intent(DeciderActivity.this, NearbyShopsActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    Toasty.success(getApplicationContext(), "Welcome back!").show();

                }

                else {
                    // first time user, start setUpProfile

                    intent = new Intent(DeciderActivity.this, SetupProfileActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    //Toast.makeText(VerificationPageActivity.this, "Hello there!", Toast.LENGTH_LONG).show();
                    Toasty.info(getApplicationContext(), "Provide necessary user information!").show();

                }

                progressDialog.dismiss();
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.error(getApplicationContext(), "Something went wrong!").show();
                progressDialog.dismiss();
            }

        });

    }

    private void storeExistingUserInfoLocally(Customer customer) {
        // locally store user information in shared preferences

        UserInfoSharedPreferences.setUserID(customer.getUserID(), this);
        UserInfoSharedPreferences.setUsername(customer.getUsername(), this);
        UserInfoSharedPreferences.setCustomerPhoneNumber(customer.getCustomerPhoneNumber(), this);
        UserInfoSharedPreferences.setEmail(customer.getEmail(), this);
        UserInfoSharedPreferences.setHomeAddress(customer.getHomeAddress(), this);
        UserInfoSharedPreferences.setCustomerLatitude(customer.getCustomerLatitude(), this);
        UserInfoSharedPreferences.setCustomerLongitude(customer.getCustomerLongitude(), this);

    }

}



