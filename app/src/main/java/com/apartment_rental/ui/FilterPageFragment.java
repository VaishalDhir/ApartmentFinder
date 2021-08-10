package com.apartment_rental.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.apartment_rental.R;

import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;

import java.util.Locale;


public class FilterPageFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_filter_page, container, false);

        EditText locationSearchEd=(EditText) view.findViewById(R.id.searchLoc);
        EditText minRentEd=(EditText) view.findViewById(R.id.minrent);
        EditText maxRentEd=(EditText) view.findViewById(R.id.maxrent);
        RadioGroup renter_type = (RadioGroup) view.findViewById(R.id.radioGroup_renterType);
        RadioGroup ap_type = (RadioGroup) view.findViewById(R.id.radioGroup_aptype);
        TextView err_text=(TextView) view.findViewById(R.id.errtext_f);
        Button searchBtn=(Button) view.findViewById(R.id.searchResult);


        return view;
    }
}