package com.example.pocketstore_customerapp.signup.setupProfile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.addresspickermap_module.AddressPickerMapsActivity;
import com.example.phonenumberverification_module.VerificationPageActivity;
import com.example.pocketstore_customerapp.R;
import com.example.pocketstore_customerapp.home.NearbyShopsActivity;

import com.example.uicomponents_module.CustomProgressDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class SetupProfileActivity extends AppCompatActivity implements SetupProfileView {

    EditText userName, userEmail;
    double latitude=0.0,longitude=0.0;
    CustomProgressDialog progressDialog;
    Button submitProfile;
    SetupProfilePresenter profilePresenter;
    EditText address;
    ImageButton pickLocation;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);
        userName = findViewById(R.id.full_name);
        userEmail = findViewById(R.id.et_email);
        address = findViewById(R.id.et_address);
        pickLocation = findViewById(R.id.pick_location_button);
        submitProfile = findViewById(R.id.btn_submit_profile);
        progressDialog = new CustomProgressDialog();

        pickLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // address pick image button pressed

                startActivityForResult(
                        new Intent(SetupProfileActivity.this, AddressPickerMapsActivity.class)
                        , ADDRESS_PICK_RESULT_CODE);
            }
        });

        profilePresenter = new SetupProfilePresenter(this,new Customer(),FirebaseDatabase.getInstance().getReference(),FirebaseAuth.getInstance());

        submitProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profilePresenter.validateFormInputs(userName.getText().toString(),userEmail.getText().toString(),
                        address.getText().toString(),latitude,longitude, VerificationPageActivity.getPhoneNumber()))
                {
                    profilePresenter.createProfile(SetupProfileActivity.this);
                }
            }
        });

    }


    @Override
    public void onProfileCreationLoading() {

        progressDialog.show(getSupportFragmentManager(),""); //showing progress dialog
    }

    @Override
    public void onProfileCreationSuccess() {
        //Start Home Activity
        progressDialog.dismiss();
        finish();
        Toasty.success(getApplicationContext(),"Profile has been saved!").show();
        startActivity(new Intent(getApplicationContext(), NearbyShopsActivity.class));
    }

    @Override
    public void onProfileCreationFailed(String errorMessage) {
        progressDialog.dismiss();
        Toasty.error(getApplicationContext(),"Failed to create profile!").show();
        Toast.makeText(getApplicationContext(),errorMessage,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEmailIsWrong(String errorMessage) {
        userEmail.setError(errorMessage);
    }

    @Override
    public void onFullNameIsWrong(String errorMessage) {
        userName.setError(errorMessage);
    }

    @Override
    public void onAddressIsWrong(String errorMessage) {
        address.setError(errorMessage);
    }


    private static final int ADDRESS_PICK_RESULT_CODE = 382;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {

            case ADDRESS_PICK_RESULT_CODE:

                if (resultCode != RESULT_OK) {
                    Toasty.error(this, "Address pick failed! Please try again");
                    return;
                }

                String fetchedAddress = data.getStringExtra(AddressPickerMapsActivity.ADDRESS_KEY);
                if(fetchedAddress==null || fetchedAddress.isEmpty())
                    address.setHint("please type in your address");
                else
                    address.setText(fetchedAddress);

                String latLng = data.getStringExtra(AddressPickerMapsActivity.LATLNG_KEY);
                // latLng format = "lat/lng: (23.79887484608253,90.358943939209)"
                this.latitude = Double.valueOf(latLng.substring(latLng.indexOf('(') + 1, latLng.indexOf(',')));
                this.longitude = Double.valueOf(latLng.substring(latLng.indexOf(',') + 1, latLng.indexOf(')')));


        }
    }

}