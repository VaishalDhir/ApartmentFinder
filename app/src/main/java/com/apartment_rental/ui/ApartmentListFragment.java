package com.apartment_rental.ui;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.apartment_rental.ApartmentListAdapter;
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
    private ProgressDialog progress;
    RecyclerView listOfApartment;
    int count=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_apartment_list, container, false);
        Button filterButton=(Button) view.findViewById(R.id.filterbtn);
         listOfApartment=(RecyclerView) view.findViewById(R.id.aplist);
        RangeSlider rangeSlider = view.findViewById(R.id.sliderRange);
        RelativeLayout filterRel= view.findViewById(R.id.relsliderRange);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        listOfApartment.setLayoutManager(layoutManager);
        listOfApartment.addItemDecoration(new DividerItemDecoration(listOfApartment.getContext(), DividerItemDecoration.VERTICAL));

        userServices = ApiUtils.getUserService();
        progress=new ProgressDialog(getActivity());
        progress.setMessage("Loading");
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setIndeterminate(true);
        progress.setProgress(0);
        progress.show();
        getData(val1,val2);

        rangeSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                List<Float> vals=slider.getValues();
                 val1=vals.get(0);
                 val2=vals.get(1);

                progress.setMessage("Loading");
                progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                progress.setIndeterminate(true);
                progress.setProgress(0);
                progress.show();
                apart.clear();

                apData.clear();

                getData(val1,val2);
                count++;
                System.out.println("count+"+count);
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
                            for (int i = 0; i < 1; i++) {
                                int len=apart.get(i).getData().size();
                                apData.clear();
                                for (int j = i; j <=len-1 ; j++) {
                                    int rent=apart.get(i).getData().get(j).getRent();
                                    int filterval1=Math.round(value1);
                                    int filterval2=Math.round(value2);
                                    if(rent>filterval1 && rent<filterval2){
                                        progress.dismiss();
                                        apData.add(apart.get(i).getData().get(j));
                                        System.out.println("size----------"+apData.size());
                                        ApartmentListAdapter aptList=new ApartmentListAdapter(getActivity(),apData);
                                        listOfApartment.setAdapter(aptList);
                                    }
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