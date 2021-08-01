package com.apartment_rental.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.apartment_rental.R;
import com.apartment_rental.SharedPref;


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
        TextView helloText=(TextView) vw.findViewById(R.id.hellotext);
        RelativeLayout relativeLay1=(RelativeLayout) vw.findViewById(R.id.rel1);
        RelativeLayout relativeLay2=(RelativeLayout) vw.findViewById(R.id.rel2);

        SharedPref shrd=new SharedPref(getActivity());
        if(shrd.getType().equals("renter")){
            relativeLay1.setVisibility(View.GONE);
            relativeLay2.setVisibility(View.VISIBLE);
            helloText.setText("Hello "+shrd.getFirstname()+"...");
        }else if(shrd.getType().equals("user")){
            relativeLay1.setVisibility(View.GONE);
            relativeLay2.setVisibility(View.VISIBLE);
            addPropertyPage.setVisibility(View.GONE);
            ViewPropertyPage.setVisibility(View.GONE);
            updateProfilePage.setVisibility(View.GONE);
            helloText.setText("Hello "+shrd.getFirstname()+"...");
        }else  {
            relativeLay1.setVisibility(View.VISIBLE);
            relativeLay2.setVisibility(View.GONE);
        }
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
                Fragment frag=new AddApartmentFragment();
                loadFragment(getContext(),frag);
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
            shrd.clearPreferences();
            Fragment frag=new ProfileFragment();
            loadFragment(getContext(),frag);
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