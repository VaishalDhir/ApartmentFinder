package com.apartment_rental.ui;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apartment_rental.Adapter.ApartmentListAdapter;
import com.apartment_rental.Adapter.FavouriteAdapter;
import com.apartment_rental.R;
import com.apartment_rental.SharedPref;
import com.apartment_rental.controller.ApiUtils;
import com.apartment_rental.controller.UserService;
import com.apartment_rental.model.Apartments;
import com.apartment_rental.model.Datuap;
import com.apartment_rental.model.Favourite;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewFavouriteFragment extends Fragment {
    UserService userServices;
    List<Apartments> apart=new ArrayList<>();
    List<Datuap> apData=new ArrayList<>();
    List<Datuap> favApData=new ArrayList<>();
    List<Favourite> fav=new ArrayList<>();

    private ProgressDialog progress;
        RecyclerView lisRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_view_favourite, container, false);

        lisRecyclerView=(RecyclerView) view.findViewById(R.id.favaplist);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        lisRecyclerView.setLayoutManager(layoutManager);
        lisRecyclerView.addItemDecoration(new DividerItemDecoration(lisRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        userServices = ApiUtils.getUserService();
        progress=new ProgressDialog(getActivity());
        progress.setMessage("Loading");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progress.show();
        getData();


        return view;
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
                                for (int j = i; j <=len-1 ; j++) {
                                    apData.add(apart.get(i).getData().get(j));

                                }
                            }
                            FavouriteApartment();
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

    public void FavouriteApartment(){
        try {
            SharedPref shrd = new SharedPref(getActivity());
            int uid = shrd.getUserid();
            Call<Favourite> call=userServices.GetFavourite(uid);
            call.enqueue(new Callback<Favourite>() {
                @Override
                public void onResponse(Call<Favourite> call, Response<Favourite> response) {
                    if (response.isSuccessful()) {

                        if(response.body().getStatus()){
                            progress.dismiss();
//                            int len=response.body().getData().size();
//                            System.out.println(len);
                            fav.add(response.body());
                            int lengt=fav.size();
                            if(lengt!=0) {
                                for (int i = 0; i <= fav.size() - 1; i++) {
                                    int len=fav.get(i).getData().size();
                                    for(int j=i;j<len;j++) {
                                        for (int k = 0; k <= apData.size() - 1; k++) {
                                            if (fav.get(i).getData().get(j).getApartmentId() == apData.get(k).getApartmentId()) {
                                                favApData.add(apData.get(k));
                                                FavouriteAdapter aptList = new FavouriteAdapter(getActivity(), favApData);
                                                lisRecyclerView.setAdapter(aptList);
                                            }
                                        }
                                    }

                                }
                            }else{
                                System.out.println("No Data");
                            }
                        }
                    } else {
                        Toast.makeText(getContext(), "Error! Please try again!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Favourite> call, Throwable t) {
                    System.out.println("error");
                }
            });
        }catch (Exception ex){
            System.out.println(ex.toString());
        }
    }
}