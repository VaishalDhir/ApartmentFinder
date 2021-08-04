package com.apartment_rental.ui;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apartment_rental.Adapter.FavouriteAdapter;
import com.apartment_rental.Adapter.RenterPropertyAdapter;
import com.apartment_rental.R;
import com.apartment_rental.SharedPref;
import com.apartment_rental.controller.ApiUtils;
import com.apartment_rental.controller.SwipeHelper;
import com.apartment_rental.controller.UserService;
import com.apartment_rental.model.AddApartment;
import com.apartment_rental.model.Apartments;
import com.apartment_rental.model.Datuap;
import com.apartment_rental.model.Img1;
import com.apartment_rental.model.Img2;
import com.apartment_rental.model.Img3;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewRenterListFragment extends Fragment {
    UserService userServices;
    List<Apartments> apart=new ArrayList<>();
    List<Datuap> apData=new ArrayList<>();
    RenterPropertyAdapter aptList;
    private ProgressDialog progress;
    RecyclerView lisRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_view_renter_list, container, false);

        lisRecyclerView=(RecyclerView) view.findViewById(R.id.renterlist);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        lisRecyclerView.setLayoutManager(layoutManager);
        lisRecyclerView.addItemDecoration(new DividerItemDecoration(lisRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        userServices = ApiUtils.getUserService();
        progress=new ProgressDialog(getActivity());
        progress.setMessage("Loading");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        progress.show();
        getData();
        SwipeHelper swipeHelper = new SwipeHelper(getActivity()) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Delete",
                        0,
                        Color.parseColor("#FF3C30"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(final int pos) {
                                try {

                                    Call<AddApartment> call=userServices.DeleteApt(apData.get(pos).getApartmentId());
                                    call.enqueue(new Callback<AddApartment>() {
                                        @Override
                                        public void onResponse(Call<AddApartment> call, Response<AddApartment> response) {
                                            if (response.isSuccessful()) {

                                                if(response.body().getStatus()){

                                                    aptList.removeItem(pos);
                                                }
                                            } else {
                                                Toast.makeText(getContext(), "Error! Please try again!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        @Override
                                        public void onFailure(Call<AddApartment> call, Throwable t) {
                                            System.out.println("error");
                                        }
                                    });
                                }catch (Exception ex){
                                    System.out.println(ex.toString());
                                }

                                apData.get(pos).getApartmentId();

                                Snackbar snackbar = Snackbar.make(lisRecyclerView, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                                snackbar.setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                     //   itemAdapter.restoreItem(item, pos);
                                        lisRecyclerView.scrollToPosition(pos);
                                    }
                                });

                                snackbar.setActionTextColor(Color.YELLOW);
                                snackbar.show();
                            }
                        }
                ));
                underlayButtons.add(new SwipeHelper.UnderlayButton(
                        "Update",
                        0,
                        Color.parseColor("#FF9502"),
                        new SwipeHelper.UnderlayButtonClickListener() {
                            @Override
                            public void onClick(int pos) {
                                Img1 bmpis=  apData.get(pos).getImg1();
                                List<Integer> imgs=bmpis.getData();
                                byte[] byteimgs = new byte[imgs.size()];
                                for (int i = 0; i < imgs.size(); i++) {
                                    byteimgs[i] = (byte) imgs.get(i).intValue();
                                }
                                Img2 bmpi2=  apData.get(pos).getImg2();
                                List<Integer> img2=bmpi2.getData();
                                byte[]  byteimg2 = new byte[img2.size()];
                                for (int i = 0; i < img2.size(); i++) {
                                    byteimg2[i] = (byte) img2.get(i).intValue();
                                }
                                Img3 bmpi3=  apData.get(pos).getImg3();
                                List<Integer> img3=bmpi3.getData();
                                byte[] byteimg3 = new byte[img3.size()];
                                for (int i = 0; i < img3.size(); i++) {
                                    byteimg3[i] = (byte) img3.get(i).intValue();
                                }
                                Fragment fragment = new UpdateFragment();
                                Bundle bundle = new Bundle();

                                bundle.putInt("aid",   apData.get(pos).getApartmentId());

                                bundle.putString("atype",   apData.get(pos).getApartmentType());
                                bundle.putString("renter",   apData.get(pos).getRenterType());
                                bundle.putString("address",   apData.get(pos).getAddress());
                                bundle.putString("size",   apData.get(pos).getSize());
                                bundle.putInt("rent",   apData.get(pos).getRent());
                                bundle.putString("facility",   apData.get(pos).getFacility());
                                bundle.putString("description",   apData.get(pos).getDescription());
                                bundle.putByteArray("img1", byteimgs);
                                bundle.putByteArray("img2",byteimg2);
                                bundle.putByteArray("img3", byteimg3);

                                fragment.setArguments(bundle);
                                ((FragmentActivity)getActivity()).getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.nav_host_fragment, fragment).addToBackStack("view").commit();
                            }
                        }
                ));

            }
        };
        swipeHelper.attachToRecyclerView(lisRecyclerView);

        return view;
    }

    private void getData(){
        try {
            SharedPref shrd = new SharedPref(getActivity());
            int uid = shrd.getUserid();
            Call<Apartments> call=userServices.getRenterProp(uid);
            call.enqueue(new Callback<Apartments>() {
                @Override
                public void onResponse(Call<Apartments> call, Response<Apartments> response) {
                    if (response.isSuccessful()) {

                        if(response.body().getStatus()){
                            progress.dismiss();

                            apart.add(response.body());

                            for (int i = 0; i < 1; i++) {
                                int len=apart.get(i).getData().size();
                                for (int j = i; j <=len-1 ; j++) {
                                    apData.add(apart.get(i).getData().get(j));
                                     aptList=new RenterPropertyAdapter(getActivity(),apData);
                                    lisRecyclerView.setAdapter(aptList);
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