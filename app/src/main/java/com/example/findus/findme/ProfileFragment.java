package com.example.findus.findme;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.findus.findme.models.EmergencyContacts;
import com.example.findus.findme.models.InsuranceDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import butterknife.Bind;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment  implements View.OnClickListener {

    private static final String TAG = ProfileFragment.class.getSimpleName();
    public static final int RESULT_GALLERY = 0;
    public StorageReference mStorage;
    public  String mCurrentPhotoPath;

    @Bind(R.id.header_cover_image) ImageView mLogoImage;
    @Bind(R.id.user_profile_photo) ImageButton mUserProfilePhoto;
    @Bind(R.id.editProfile) ImageView mEditProfile;
    @Bind(R.id.user_profile_name) TextView mUserName;
    @Bind(R.id.bloodType) TextView mBloodType;
    @Bind(R.id.gender) TextView mGender;
    @Bind(R.id.emergencyContact1) TextView mEmergencyContactOne;
    @Bind(R.id.emergencyContact1phone) TextView mEmergencyContactOnePhone;
    @Bind(R.id.emergencyContact2) TextView mEmergencyContactTwo;
    @Bind(R.id.emergencyContact2phone) TextView mEmergencyContactTwoPhone;
    @Bind(R.id.emergencyContact3) TextView mEmergencyContactThree;
    @Bind(R.id.emergencyContact3phone) TextView mEmergencyContactThreePhone;
    @Bind(R.id.listConditions) TextView mConditionsList;
    @Bind(R.id.listMedicalAllergies) TextView mListMedicalAllergies;
    @Bind(R.id.listotherAllergies) TextView mlistOtherAllergies;
    @Bind(R.id.userAge) TextView mUserAge;
    @Bind(R.id.userNationalId) TextView mNationalId;
    @Bind(R.id.insurancePolicyNo) TextView mPolicyNo;
    @Bind(R.id.userMedCover) TextView mMedCover;
    @Bind(R.id.userPrefHospital) TextView mPrefHospital;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mStorage = FirebaseStorage.getInstance().getReference();
    }


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this, view);

        mUserProfilePhoto.setOnClickListener(this);
        mEditProfile.setOnClickListener(this);

        getProfileData();


        return view;

    }

    @Override
    public void onClick(View v) {

        if(v==mUserProfilePhoto){
            Intent galleryIntent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent , RESULT_GALLERY );


        }
        if(v==mEditProfile){
            Fragment EditProfFrag = new EditProfileFragment();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            transaction.replace(R.id.mainFrame,EditProfFrag);
            transaction.commit();
            
        }

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_GALLERY && resultCode == RESULT_OK){
            final Uri selectedImage = data.getData();

            Toast.makeText(getActivity(), "here!!", Toast.LENGTH_SHORT)
                    .show();

            StorageReference profileImagePath = mStorage.child("ProfileImages").child(selectedImage.getLastPathSegment());
            profileImagePath.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // if the upload is successful
                    @SuppressWarnings("VisibleForTests") Uri imageUri = taskSnapshot.getDownloadUrl();


                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                // handel uploads fails

                }
            });
        }

        if (data != null) {
            final Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContext().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            mUserProfilePhoto.setImageBitmap(BitmapFactory.decodeFile(picturePath));
            mUserProfilePhoto.setEnabled(true);
           cursor.close();

            StorageReference profileImagePath = mStorage.child("ProfileImages").child(selectedImage.getLastPathSegment());
            profileImagePath.putFile(selectedImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity(), "saved!!", Toast.LENGTH_SHORT)
                            .show();
                }
            });

        } else {
            Toast.makeText(getActivity(), "Try Again!!", Toast.LENGTH_SHORT)
                    .show();
        }

    }


    public void getProfileData() {
        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
        String uid = users.getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("user").child(uid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //emergency contact model
               String person1= dataSnapshot.child("emergencyContacts").child("emergencyContactNameOne").getValue().toString();
               String  person1Cont = dataSnapshot.child("emergencyContacts").child("emergencyContactNumberOne").getValue().toString();
                String person2= dataSnapshot.child("emergencyContacts").child("emergencyContactNameTwo").getValue().toString();
                String  person2Cont = dataSnapshot.child("emergencyContacts").child("emergencyContactNumberTwo").getValue().toString();
                String person3= dataSnapshot.child("emergencyContacts").child("emergencyContactNameThree").getValue().toString();
                String  person3Cont = dataSnapshot.child("emergencyContacts").child("emergencyContactNumberThree").getValue().toString();
                mEmergencyContactOne.setText(person1);
                mEmergencyContactOnePhone.setText(person1Cont);
                mEmergencyContactTwo.setText(person2);
                mEmergencyContactTwoPhone.setText(person2Cont);
                mEmergencyContactThree.setText(person3);
                mEmergencyContactThreePhone.setText(person3Cont);

                // medical detail model

                String age= dataSnapshot.child("medicalDetails").child("age").getValue().toString();
                String  condition = dataSnapshot.child("medicalDetails").child("condition").getValue().toString();
                String medAllergies= dataSnapshot.child("medicalDetails").child("medAllergies").getValue().toString();
                String  name = dataSnapshot.child("medicalDetails").child("name").getValue().toString();
                String phoneNumber= dataSnapshot.child("medicalDetails").child("phoneNumber").getValue().toString();
                String  otherAllegies = dataSnapshot.child("medicalDetails").child("userAllergies").getValue().toString();
                String  gender = dataSnapshot.child("medicalDetails").child("gender").getValue().toString();
                String  bloodType = dataSnapshot.child("medicalDetails").child("bloodGroup").getValue().toString();

                mUserAge.setText(age);
                mConditionsList.setText(condition);
                mListMedicalAllergies.setText(medAllergies);
                mUserName.setText(name);
                mlistOtherAllergies.setText(otherAllegies);
                mGender.setText(gender);
                mBloodType.setText(bloodType);

                 //insurance model

                String medCover= dataSnapshot.child("insuranceDetails").child("medCover").getValue().toString();
                String  natId = dataSnapshot.child("insuranceDetails").child("natId").getValue().toString();
                String policyNumber= dataSnapshot.child("insuranceDetails").child("policyNumber").getValue().toString();
                String  prefHospital = dataSnapshot.child("insuranceDetails").child("prefHospital").getValue().toString();

                mNationalId.setText(natId);
                mPolicyNo.setText(policyNumber);
                mMedCover.setText(medCover);
                mPrefHospital.setText(prefHospital);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//
    }



}
