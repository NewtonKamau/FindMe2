package com.example.findus.findme;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.findus.findme.models.EmergencyContacts;
import com.example.findus.findme.models.InsuranceDetails;
import com.example.findus.findme.models.medicalDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends Fragment implements View.OnClickListener {
    @Bind(R.id.userFullName) EditText mUserFullName;
    @Bind(R.id.userPhoneNumber) EditText mUserPhoneNumber;
    @Bind(R.id.userAge) EditText mUserAge;
    @Bind(R.id.gender_spinner) Spinner mGenderSpinner;
    @Bind(R.id.bloodGroup_Spinner) Spinner mBloodGroupSpinner;
    @Bind(R.id.userMedicalConditions) EditText mUserMedicalConditions;
    @Bind(R.id.userMedAllergies) EditText mUserMedAllergies;
    @Bind(R.id.userAllergies) EditText mUserAllergies;
    @Bind(R.id.userMedicalBtn) Button mUserSaveMedicalButton;
    @Bind(R.id.emergencyContact) EditText mEmergencyContactName;
    @Bind(R.id.emergencyNumber) EditText mEmergencyNumber;
    @Bind(R.id.emergencyContact1) EditText mEmergencyContactName1;
    @Bind(R.id.emergencyNumber1) EditText mEmergencyNumber1;
    @Bind(R.id.emergencyContact2) EditText mEmergencyContactName2;
    @Bind(R.id.emergencyNumber2) EditText mEmergencyNumber2;
    @Bind(R.id.emergencyContactBtn) Button mEmergencyContactBtn;
    @Bind(R.id.userNationalId) EditText mNationalId;
    @Bind(R.id.insurancePolicyNo) EditText mPolicyNo;
    @Bind(R.id.userMedCover) EditText mMedCover;
    @Bind(R.id.userPrefHospital) EditText mPrefHospital;
    @Bind(R.id.userInsuranceButton) Button mUserInsuranceBtn;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        ButterKnife.bind(this,view);

        mUserSaveMedicalButton.setOnClickListener(this);
        mUserInsuranceBtn.setOnClickListener(this);
        mEmergencyContactBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v){
        if(v == mUserSaveMedicalButton){
            UpdateMedicalDataToFirebase();

            mUserAge.setText("");
            mUserFullName.setText("");
            mUserMedicalConditions.setText("");
            mUserMedAllergies.setText("");
            mUserAllergies.setText("");
            mUserPhoneNumber.setText("");
        }
        if(v==mUserInsuranceBtn){
            UpdateInsuranceToFirebase();

            mNationalId.setText("");
            mPolicyNo.setText("");
            mMedCover.setText("");
            mPrefHospital.setText("");

        }
        if(v==mEmergencyContactBtn){
            UpdateEmergencyContactsToFirebase();

            mEmergencyContactName.setText("");
            mEmergencyNumber.setText("");
            mEmergencyContactName1.setText("");
            mEmergencyNumber1.setText("");
            mEmergencyContactName2.setText("");
            mEmergencyNumber2.setText("");
        }

    }

    public void   UpdateMedicalDataToFirebase(){

        String username = mUserFullName.getText().toString();
        String userAge = mUserAge.getText().toString();
        String medicalConditions = mUserMedicalConditions.getText().toString();
        String medicalAllergies = mUserMedAllergies.getText().toString();
        String otherAllergies = mUserAllergies.getText().toString();
        String phoneNumber = mUserPhoneNumber.getText().toString();
        String gender= mGenderSpinner.getItemAtPosition(mGenderSpinner.getSelectedItemPosition()).toString();
        String bloodGroup = mBloodGroupSpinner.getItemAtPosition(mBloodGroupSpinner.getSelectedItemPosition()).toString();


        FirebaseUser users= FirebaseAuth.getInstance().getCurrentUser();
        String uid=users.getUid();


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user").child(uid);
        medicalDetails medDetails = new medicalDetails (username,userAge,phoneNumber,gender,bloodGroup, medicalConditions, medicalAllergies, otherAllergies);


        reference.child("medicalDetails").setValue(medDetails);

        Toast.makeText(getActivity(), " Details Updated", Toast.LENGTH_SHORT).show();
    }


    public void UpdateInsuranceToFirebase(){
        String nationalId = mNationalId.getText().toString();
        String policyNumber = mPolicyNo.getText().toString();
        String medicalCover = mMedCover.getText().toString();
        String preferedHospital = mPrefHospital.getText().toString();

        FirebaseUser users= FirebaseAuth.getInstance().getCurrentUser();
        String uid=users.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user").child(uid);

        InsuranceDetails insuranceDetails = new InsuranceDetails(nationalId, policyNumber,medicalCover, preferedHospital);

        reference.child("insuranceDetails").setValue(insuranceDetails);

        Toast.makeText(getActivity(), " Insurance Details Upaded", Toast.LENGTH_SHORT).show();
    }

    public void UpdateEmergencyContactsToFirebase(){

        String emergencyContactName = mEmergencyContactName.getText().toString();
        String emergencyContactNumber = mEmergencyNumber.getText().toString();
        String emergencyContactName1 = mEmergencyContactName1.getText().toString();
        String emergencyContactNumber1 = mEmergencyNumber1.getText().toString();
        String emergencyContactName2 = mEmergencyContactName2.getText().toString();
        String emergencyContactNumber2 = mEmergencyNumber2.getText().toString();

        FirebaseUser users= FirebaseAuth.getInstance().getCurrentUser();
        String uid=users.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user").child(uid);

        EmergencyContacts emergencyContact = new EmergencyContacts (emergencyContactName,emergencyContactNumber,emergencyContactName1,emergencyContactNumber1,emergencyContactName2,emergencyContactNumber2);

        reference.child("emergencyContacts").setValue(emergencyContact);

        Toast.makeText(getActivity(), " Details Saved", Toast.LENGTH_SHORT).show();
    }


}
