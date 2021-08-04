package com.apartment_rental.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apartment_rental.R;
import com.apartment_rental.SharedPref;
import com.apartment_rental.controller.ApiUtils;
import com.apartment_rental.controller.UserService;
import com.apartment_rental.model.LoginResponse;
import com.apartment_rental.model.Register;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateAccountFragment extends Fragment {
    TextView errortxt;
    UserService userServices;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout fo9r this fragment

        View view=inflater.inflate(R.layout.fragment_create_account, container, false);
        EditText firstNameEd=(EditText) view.findViewById(R.id.fname_ed);
        EditText lastNameEd=(EditText) view.findViewById(R.id.lname_ed);
        EditText ContactEd=(EditText) view.findViewById(R.id.contact_ed);
        EditText emailIdEd=(EditText) view.findViewById(R.id.emailid_ed);
        EditText passwordEd=(EditText) view.findViewById(R.id.createpassword_ed);
        RadioGroup type = (RadioGroup) view.findViewById(R.id.radioGroup_cat);

        EditText confirmPasswordEd=(EditText) view.findViewById(R.id.confirm_ed);
        RelativeLayout createAccount=(RelativeLayout) view.findViewById(R.id.rel_create);
        userServices = ApiUtils.getUserService();
        errortxt=(TextView) view.findViewById(R.id.errortext);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String first=firstNameEd.getText().toString();
                String last=lastNameEd.getText().toString();
                String contactnum=ContactEd.getText().toString();
                String emaidIdtext=emailIdEd.getText().toString();
                String passwordText=passwordEd.getText().toString();
                String confirmPass=confirmPasswordEd.getText().toString();
                int selectedId = type.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) view.findViewById(selectedId);

                if (validateLogin(first,last,emaidIdtext,contactnum,passwordText,confirmPass)) {
                    registerUser(first,last,emaidIdtext,contactnum,passwordText,radioButton.getText().toString());
                }
            }
        });
        return view;
    }

    private void registerUser(String first, String last, String emaidIdtext, String contactnum, String passwordText,String type)  {
        try {
            if(type.equals("tenant")){
                type="renter";
            }else{
                type="user";

            }

            Call<Register> call = userServices.userRegister(first,last,emaidIdtext,contactnum,passwordText,type);
            call.enqueue(new Callback<Register>() {
                @Override
                public void onResponse(Call<Register> call, Response<Register> response) {
                    if (response.isSuccessful()) {

                        if(response.body().getStatus()){
                            new MaterialAlertDialogBuilder(getContext()).setMessage("Account Created  Successfull")
                                    .setPositiveButton("ok",(dialog, which) -> {

                                        Fragment fragment = new ProfileFragment();
                                        ((AppCompatActivity) getActivity()).getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.nav_host_fragment, fragment)
                                                .addToBackStack(fragment.getTag())
                                                .commit();
                                    }).show();
                        }else{
                            new MaterialAlertDialogBuilder(getActivity()).setMessage(response.body().getMessage())
                                    .setPositiveButton("OK",(dialog, which) -> {
                                        /////
                                    }).show();
                        }
                    } else {
                 //       Toast.makeText(getContext(), "Error! Please try again!", Toast.LENGTH_SHORT).show();
                        new MaterialAlertDialogBuilder(getActivity()).setMessage("Error! Please try again!")
                                .setPositiveButton("OK",(dialog, which) -> {
                                    /////
                                }).show();
                    }
                }

                @Override
                public void onFailure(Call<Register> call, Throwable t) {
                    System.out.println("error");
                    new MaterialAlertDialogBuilder(getActivity()).setMessage("Error! Please try again!")
                            .setPositiveButton("OK",(dialog, which) -> {
                                /////
                            }).show();
                }
            });
        }catch (Exception ex){
            new MaterialAlertDialogBuilder(getActivity()).setMessage(ex.toString())
                    .setPositiveButton("OK",(dialog, which) -> {
                        /////
                    }).show();

        }
    }

    private boolean validateLogin(String first,String last,String email,String contact, String password,String confirmPassword) {

        if (email == null || email.trim().length() == 0) {
           errortxt.setText("Email Id is required");
            return false;
        }
        if (password == null || password.trim().length() == 0) {
            errortxt.setText("Password is required");
            return false;
        }
        if (first == null || first.trim().length() == 0) {
            errortxt.setText("First Name is required");
            return false;
        }
        if (last == null || last.trim().length() == 0) {
            errortxt.setText("Last Name is required");
            return false;
        }
        if (contact == null || contact.trim().length() == 0) {
            errortxt.setText("Phone Number is required");
            return false;
        }
        if (confirmPassword == null || confirmPassword.trim().length() == 0) {
            errortxt.setText(" Confirm Password is required");
            return false;
        }
        if(!(password.equals(confirmPassword))){
            errortxt.setText("Both password should match");
            return false;
        }
        return true;
    }
}