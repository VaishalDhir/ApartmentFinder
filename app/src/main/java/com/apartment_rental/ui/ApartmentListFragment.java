package com.apartment_rental.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.apartment_rental.R;
import com.apartment_rental.controller.ApiUtils;
import com.apartment_rental.controller.UserService;
import com.apartment_rental.model.Apartments;
import com.apartment_rental.model.Datuap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApartmentListFragment extends Fragment {
    UserService userServices;
    List<Apartments> apart=new ArrayList<>();
    Float val1=1000f,val2=5000f;
    List<Datuap> apData=new ArrayList<>();




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_apartment_list, container, false);
        Button filterButton=(Button) view.findViewById(R.id.filterbtn);
        RangeSlider rangeSlider = view.findViewById(R.id.sliderRange);
        RelativeLayout filterRel= view.findViewById(R.id.relsliderRange);

        userServices = ApiUtils.getUserService();
        getData(val1,val2);

        rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                List<Float> vals=slider.getValues();
                 val1=vals.get(0);
                 val2=vals.get(1);
                System.out.println(val1+""+val2);
                apData.clear();
                getData(val1,val2);
            }
        });


        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterRel.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    private void getData(Float value1, Float value2){
        try {
            Call<Apartments> call=userServices.getAllApartments();
            call.enqueue(new Callback<Apartments>() {
                @Override
                public void onResponse(Call<Apartments> call, Response<Apartments> response) {
                    if (response.isSuccessful()) {

                        if(response.body().getStatus()){
                            apart.add(response.body());
                            for (int i = 0; i < apart.size(); i++) {
                                int len=apart.get(i).getData().size();
                                for (int j = i; j <=len-1 ; j++) {
                                    int rent=apart.get(i).getData().get(j).getRent();
                                    int filterval1=Math.round(value1);
                                    int filterval2=Math.round(value2);
                                    if(rent>filterval1 && rent<filterval2){
                                        apData.add(apart.get(i).getData().get(j));
                                        System.out.println(apData);
                                    }
                             //       LatLng latLng = new LatLng(apart.get(i).getData().get(j).getLatitude(), apart.get(i).getData().get(j).getLongitude());

                                }
                            }


                        }
                    } else {
                        Toast.makeText(getContext(), "Error! Please try again!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Apartments> call, Throwable t) {
                    System.out.println("error");
                }
            });
        }catch (Exception ex){
            System.out.println(ex.toString());

        }



    }
}