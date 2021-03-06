package com.apartment_rental.ui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.apartment_rental.Adapter.ApartmentListAdapter;
import com.apartment_rental.R;
import com.apartment_rental.controller.ApiUtils;
import com.apartment_rental.controller.SwipeHelper;
import com.apartment_rental.controller.UserService;
import com.apartment_rental.model.Apartments;
import com.apartment_rental.model.Datuap;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApartmentListFragment extends Fragment {
    UserService userServices;
    List<Apartments> apart=new ArrayList<>();
    Float val1=1000f,val2=5000f;
    List<Datuap> apData=new ArrayList<>();
    private ProgressDialog progress;
    RecyclerView listOfApartment;
    int count=0;

    ApartmentListAdapter aptList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_apartment_list, container, false);
        Button filterButton=(Button) view.findViewById(R.id.filterbtn);
         listOfApartment=(RecyclerView) view.findViewById(R.id.aplist);
        progress=new ProgressDialog(getActivity());

        Bundle bundle = this.getArguments();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listOfApartment.setLayoutManager(layoutManager);
        listOfApartment.addItemDecoration(new DividerItemDecoration(listOfApartment.getContext(), DividerItemDecoration.VERTICAL));

        userServices = ApiUtils.getUserService();
        if(bundle!=null) {
            String filteradress = bundle.getString("address");
            String filteratype = bundle.getString("atype");
            String filterrtype = bundle.getString("rtype");
            int filterminRent = bundle.getInt("minrent");
            int filtermaxRent = bundle.getInt("maxrent");
            progress.setMessage("Loading");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            progress.show();
            getFilterProperties(filteradress, filteratype, filterrtype, filterminRent, filtermaxRent);

        }else {
            getData();

            progress.setMessage("Loading");
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            progress.show();
        }
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new FilterPageFragment();
                ((AppCompatActivity) getActivity()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment)
                        .addToBackStack(fragment.getTag())
                        .commit();
//                filterRel.setVisibility(View.VISIBLE);
            }
        });


//
//




        return view;
    }

    private void getFilterProperties(String filteradress, String filteratype, String filterrtype, int filterminRent, int filtermaxRent) {

        try {
            Call<Apartments> call=userServices.getFilteredProperty(filteradress,filteratype,filterrtype,String.valueOf(filterminRent),String.valueOf(filtermaxRent));
            call.enqueue(new Callback<Apartments>() {
                @Override
                public void onResponse(Call<Apartments> call, Response<Apartments> response) {
                    if (response.isSuccessful()) {

                        if(response.body().getStatus()){

                            apart.add(response.body());
                            for (int i = 0; i < 1; i++) {
                                int len=apart.get(i).getData().size();
                                apData.clear();
                                for (int j = i; j <=len-1 ; j++) {
                                    progress.dismiss();

                                    apData.add(apart.get(i).getData().get(j));
                                    System.out.println("size----------"+apData.size());
                                    aptList=new ApartmentListAdapter(getActivity(),apData);
                                    listOfApartment.setAdapter(aptList);

                                    //  }
                                }
                            }
                        }else{
                            progress.dismiss();
                            new MaterialAlertDialogBuilder(getActivity()).setMessage("No Apartment Found")
                                    .setPositiveButton("OK",(dialog, which) -> {
                                        Fragment fragment = new ApartmentListFragment();
                                        ((AppCompatActivity) getActivity()).getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.nav_host_fragment, fragment)
                                                .addToBackStack(fragment.getTag())
                                                .commit();
                                    }).show();
                        }
                    } else {
                        progress.dismiss();

                        Toast.makeText(getContext(), "Error! Please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Apartments> call, Throwable t) {
                    progress.dismiss();

                    System.out.println("error");
                }
            });
        }catch (Exception ex){
            progress.dismiss();

            System.out.println(ex.toString());
        }



    }

    private void getData(){
        try {
            Call<Apartments> call=userServices.getAllApartments();
            call.enqueue(new Callback<Apartments>() {
                @Override
                public void onResponse(Call<Apartments> call, Response<Apartments> response) {
                    if (response.isSuccessful()) {

                        if(response.body().getStatus()){

                            apart.add(response.body());
                            for (int i = 0; i < 1; i++) {
                                int len=apart.get(i).getData().size();
                                apData.clear();
                                for (int j = i; j <=len-1 ; j++) {
                                    progress.dismiss();
//
//                                    int rent=apart.get(i).getData().get(j).getRent();
//                                    int filterval1=Math.round(value1);
//                                    int filterval2=Math.round(value2);
//                                    if(rent>filterval1 && rent<filterval2){
                                        apData.add(apart.get(i).getData().get(j));
                                        System.out.println("size----------"+apData.size());
                                         aptList=new ApartmentListAdapter(getActivity(),apData);
                                        listOfApartment.setAdapter(aptList);

                                  //  }
                                }
                            }
                        }
                    } else {
                        progress.dismiss();

                        Toast.makeText(getContext(), "Error! Please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Apartments> call, Throwable t) {
                    progress.dismiss();

                    System.out.println("error");
                }
            });
        }catch (Exception ex){
            progress.dismiss();

            System.out.println(ex.toString());
        }
    }
}