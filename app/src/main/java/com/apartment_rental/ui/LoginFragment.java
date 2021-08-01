package com.apartment_rental.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apartment_rental.R;
import com.apartment_rental.SharedPref;
import com.apartment_rental.controller.ApiUtils;
import com.apartment_rental.controller.UserService;
import com.apartment_rental.model.LoginPojo;
import com.apartment_rental.model.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {
    UserService userServices;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        EditText emailIdEditText = (EditText) view.findViewById(R.id.email_ed);
        EditText passwordEditText = (EditText) view.findViewById(R.id.password_ed);
        RelativeLayout loginButton = (RelativeLayout) view.findViewById(R.id.rel1_btn);
        TextView ResetPasswordText=(TextView) view.findViewById(R.id.Resetpassword);
        userServices = ApiUtils.getUserService();

        ResetPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new ResetPasswordFragment();
                ((AppCompatActivity) getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment)
                        .addToBackStack(fragment.getTag())
                        .commit();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emaidId = emailIdEditText.getText().toString();
                String passwordText = passwordEditText.getText().toString();

                if (validateLogin(emaidId, passwordText)) {
                    Authenticate(emaidId, passwordText);
                }
//
            }
        });
        return view;
    }

    private void Authenticate(String emaidId, String passwordText) {
        try {
            Call<LoginResponse> call = userServices.loginUser(emaidId,passwordText);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {

                            if(response.body().getSuccess()){
                                SharedPref shrd=new SharedPref(getActivity());
                           //     shrd.setIslogin(true);
                                shrd.setFirstname(response.body().getData().get(0).getFirstname());
                                shrd.setUserId(response.body().getData().get(0).getUserid());
                                shrd.setType(response.body().getData().get(0).getType());
                            Fragment fragment = new ProfileFragment();
                            ((AppCompatActivity) getActivity()).getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.nav_host_fragment, fragment)
                                    .addToBackStack(fragment.getTag())
                                    .commit();
                        }else {
                            Toast.makeText(getContext(), "Wrong email id or password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "Error! Please try again!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {
                    System.out.println("error");
                }
            });
        }catch (Exception ex){
            System.out.println(ex.toString());

        }
    }

    private boolean validateLogin(String email, String password) {

        if (email == null || email.trim().length() == 0) {
            Toast.makeText(getContext(), "EM is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password == null || password.trim().length() == 0) {
            Toast.makeText(getContext(), "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}