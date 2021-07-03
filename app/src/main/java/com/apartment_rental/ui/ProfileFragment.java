package com.apartment_rental.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.apartment_rental.R;


public class ProfileFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vw=inflater.inflate(R.layout.fragment_profile, container, false);
        Button loginPage=(Button) vw.findViewById(R.id.loginPagebtn);
        Button registerPage=(Button) vw.findViewById(R.id.registeraccountbtn);
        Button addPropertyPage=(Button) vw.findViewById(R.id.addPropertybtn);
        Button updateProfilePage=(Button) vw.findViewById(R.id.updateprofilebtn);
        Button ViewPropertyPage=(Button) vw.findViewById(R.id.viewpropbtn);
        Button SignOut=(Button) vw.findViewById(R.id.signout);
        loginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag=new LoginFragment();
                loadFragment(getContext(),frag);
            }
        });
        registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag=new CreateAccountFragment();
                loadFragment(getContext(),frag);
            }
        });
        addPropertyPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
       updateProfilePage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       });
       ViewPropertyPage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       });
        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return vw;
    }



    private void loadFragment(Context context, Fragment fragment) {
        ((AppCompatActivity) context).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(fragment.getTag())
                .commit();
    }
    }