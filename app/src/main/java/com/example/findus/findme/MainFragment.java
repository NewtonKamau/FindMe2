package com.example.findus.findme;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements View.OnClickListener{

    @Bind(R.id.get_started) CardView mGetStarted;
    @Bind(R.id.get_aid_tips) CardView mGetAidTips;
    @Bind(R.id.go_profile) CardView mGoProfile;

    public MainFragment() {
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
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ButterKnife.bind(this, view);

        mGetStarted.setOnClickListener(this);
        mGetAidTips.setOnClickListener(this);
        mGoProfile.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == mGetAidTips) {
            Intent intent = new Intent(getActivity(), FirstAidActivity.class);
            startActivity(intent);
        }if (v == mGetStarted){
            Intent intent = new Intent(getActivity(), GetStartedActivity.class);
            startActivity(intent);
        }if (v == mGoProfile){
            Intent intent = new Intent(getActivity(), ProfileFragment.class);
            startActivity(intent);
        }
    }
}
