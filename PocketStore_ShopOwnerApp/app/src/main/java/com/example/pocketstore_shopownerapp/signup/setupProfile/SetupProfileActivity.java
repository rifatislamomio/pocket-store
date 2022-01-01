package com.example.pocketstore_shopownerapp.signup.setupProfile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.addresspickermap_module.AddressPickerMapsActivity;
import com.example.phonenumberverification_module.VerificationPageActivity;
import com.example.pocketstore_shopownerapp.R;
import com.example.pocketstore_shopownerapp.home.InsideShopActivity;
import com.example.uicomponents_module.CustomProgressDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import es.dmoral.toasty.Toasty;

public class SetupProfileActivity extends AppCompatActivity implements SetupProfileView  {

    private static final String TAG = "SPA-debug";

    EditText shopOwnerName, shopName;
    double latitude=0.0,longitude=0.0;
    CustomProgressDialog progressDialog;
    Button submitProfile;
    SetupProfilePresenter profilePresenter;
    EditText address;
    ImageButton pickLocation;
    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);
        shopOwnerName = findViewById(R.id.shopowner_name);
        shopName = findViewById(R.id.shop_name);
        address = findViewById(R.id.et_address);
        pickLocation = findViewById(R.id.pick_location_button);
        submitProfile = findViewById(R.id.btn_submit_profile);
        progressDialog = new CustomProgressDialog();
        spinner = findViewById(R.id.shoptype_spinner);

        //Setting up the spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.shoptype_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_drop_down_item);
        spinner.setAdapter(adapter);


        profilePresenter = new SetupProfilePresenter(this, new Shop(),
                FirebaseDatabase.getInstance().getReference(), FirebaseAuth.getInstance());

        submitProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(profilePresenter.validateFormInputs(shopOwnerName.getText().toString().trim(),shopName.getText().toString().trim(),address.getText().toString().trim(),latitude,longitude,
                                                                    // 100 meters
                        VerificationPageActivity.getPhoneNumber(),100.0, spinner.getSelectedItem().toString()))
                {
                    profilePresenter.createProfile(getApplicationContext());
                }
            }
        });
    }

    @Override
    public void onProfileCreationLoading() {
        progressDialog.show(getSupportFragmentManager(),"");
    }

    @Override
    public void onProfileCreationSuccess() {
        //Start Home Activity
        progressDialog.dismiss();
        Toasty.success(getApplicationContext(),"Profile has been saved!").show();
        finish();
        startActivity(new Intent(getApplicationContext(), InsideShopActivity.class));
    }

    @Override
    public void onProfileCreationFailed(String errorMessage) {
        progressDialog.dismiss();
        Toasty.error(getApplicationContext(),"Failed to create profile!").show();
        Toast.makeText(getApplicationContext(),errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShopNameIsWrong(String errorMessage) {
        shopName.setError(errorMessage);
    }

    @Override
    public void onFullNameIsWrong(String errorMessage) {
        shopOwnerName.setError(errorMessage);
    }

    @Override
    public void onAddressIsWrong(String errorMessage) {
        address.setError(errorMessage);
    }


    private static final int ADDRESS_PICK_RESULT_CODE = 690;
    public void addressPickClicked(View view) {
        // address pick image button pressed

        startActivityForResult(
                new Intent(this, AddressPickerMapsActivity.class)
                , ADDRESS_PICK_RESULT_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode){

            case ADDRESS_PICK_RESULT_CODE:

                if(resultCode!=RESULT_OK){
                    Toasty.error(this, "Address pick failed! Please try again");
                    return;
                }

                String fetchedAddress = data.getStringExtra(AddressPickerMapsActivity.ADDRESS_KEY);
                if(fetchedAddress==null || fetchedAddress.isEmpty())
                    address.setHint("please type in your address");
                else
                    address.setText(fetchedAddress);

                String latLng =  data.getStringExtra(AddressPickerMapsActivity.LATLNG_KEY);

                // latLng format = "lat/lng: (23.79887484608253,90.358943939209)"
                this.latitude = Double.valueOf( latLng.substring( latLng.indexOf('(')+1, latLng.indexOf(',') ) );
                this.longitude = Double.valueOf( latLng.substring( latLng.indexOf(',')+1, latLng.indexOf(')') ) );

        }
    }
}