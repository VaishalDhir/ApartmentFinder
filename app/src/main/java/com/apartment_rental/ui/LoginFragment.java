package com.apartment_rental.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.apartment_rental.R;
import com.apartment_rental.controller.ApiUtils;
import com.apartment_rental.controller.UserService;
import com.apartment_rental.model.LoginPojo;
import com.apartment_rental.model.LoginResponse;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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
        userServices = ApiUtils.getUserService();
//        try {
//
//            Retrofit retrofit = new Retrofit.Builder()
//                    .baseUrl("http://10.0.2.2:8012/api/")  // for emulator
//                    // .baseUrl("http://192.168.2.28:8012/api/")       //for mobile device use ipconfig in cmd and then wireless and then take ip of ipv4
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//            userServices = retrofit.create(UserService.class);
//
//        } catch (Exception e) {
//            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
//        }
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
            LoginPojo login = new LoginPojo(emaidId, passwordText);
            Call<LoginResponse> call = userServices.loginUser(emaidId,passwordText);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        if(response.message().equals("OK")){
                            String body=response.body().getMessage();
                            System.out.println("hello world"+body);
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