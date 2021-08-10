package com.apartment_rental.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loc=locationSearchEd.getText().toString();
             String minVal=minRentEd.getText().toString();
                String maxVal=maxRentEd.getText().toString();
                if(minVal.equals("")){
                    minVal="0";
                }
                if(maxVal.equals("")){
                    maxVal="0";
                }
                int min= Integer.parseInt(minVal);
                int max=Integer.parseInt(maxVal);
                int selectedRId = renter_type.getCheckedRadioButtonId();
                int selectedAId = ap_type.getCheckedRadioButtonId();
                RadioButton radioButton_rType = (RadioButton) view.findViewById(selectedRId);
                RadioButton radioButton_aType = (RadioButton) view.findViewById(selectedAId);
                String aTypeVal=radioButton_aType.getText().toString();
                String rTypeVal=radioButton_rType.getText().toString();

                if(min>=1000 && max<=5000){
                    Fragment fragment = new ApartmentListFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("atype",aTypeVal);
                    bundle.putString("rtype",rTypeVal);
                    bundle.putString("address",loc);
                    bundle.putInt("minrent",min);
                    bundle.putInt("maxrent",max);
                    ((AppCompatActivity) getActivity()).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment, fragment)
                            .addToBackStack(fragment.getTag())
                            .commit();
                }else {
                    err_text.setText("Rent Should be in between 1000-5000 C.A.D");
                }


            }
        });

        return view;
    }
}