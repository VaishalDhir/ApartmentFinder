package com.apartment_rental.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.apartment_rental.R;
import com.apartment_rental.SharedPref;
import com.apartment_rental.controller.ApiUtils;
import com.apartment_rental.controller.UserService;
import com.apartment_rental.model.AddApartment;
import com.apartment_rental.model.Datuap;
import com.apartment_rental.model.Favourite;
import com.apartment_rental.model.Img1;
import com.apartment_rental.model.Img2;
import com.apartment_rental.model.Img3;
import com.apartment_rental.ui.ApartmentListFragment;
import com.apartment_rental.ui.LoginFragment;
import com.apartment_rental.ui.ProfileFragment;
import com.apartment_rental.ui.ViewApartmentFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApartmentListAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context ctx;
    List<Datuap> apData;
    byte[] byteimg1,byteimg2,byteimg3;
    Bitmap bms;
    UserService userServices;
    public ApartmentListAdapter(Context ctx, List<Datuap> apData) {
        this.ctx=ctx;
        this.apData=apData;
        userServices = ApiUtils.getUserService();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.aplistlayout,parent,false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Datuap item = apData.get(position);
        SharedPref shrd = new SharedPref(ctx);
        int uid = shrd.getUserid();
        try {
            List<Favourite> fav=new ArrayList<>();

            Call<Favourite> call=userServices.GetFavourite(uid);
            call.enqueue(new Callback<Favourite>() {
                @Override
                public void onResponse(Call<Favourite> call, Response<Favourite> response) {
                    if (response.isSuccessful()) {

                        if(response.body().getStatus()){

                            fav.add(response.body());
                            int lengt=fav.size();
                            if(lengt!=0) {
                                for (int i = 0; i <= fav.size() - 1; i++) {
                                    int len=fav.get(i).getData().size();
                                    for(int j=i;j<len;j++) {
                                        for (int k = 0; k <= apData.size() - 1; k++) {
                                            if (fav.get(i).getData().get(j).getApartmentId() == item.getApartmentId()) {
                                                holder.disLikeBtn.setImageResource(R.drawable.like);
                                            }
                                        }
                                    }
                                }
                            }else{
                                System.out.println("No Data");
                            }
                        }
                    } else {
                        Toast.makeText(ctx, "Error! Please try again!", Toast.LENGTH_SHORT).show();
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
        holder.apartmentRent.setText(item.getRent().toString());
        holder.apartmentType.setText(item.getApartmentType());
        holder.apartmentFacility.setText(item.getFacility());
        Img1 bmpi1=item.getImg1();
        List<Integer> img1=bmpi1.getData();
        byteimg1 = new byte[img1.size()];
        for (int i = 0; i < img1.size(); i++) {
            byteimg1[i] = (byte) img1.get(i).intValue();
        }
        bms=getImage(byteimg1);
        holder.apartmentImage.setImageBitmap(bms);
        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Img1 bmpis=item.getImg1();
                List<Integer> imgs=bmpis.getData();
                byte[] byteimgs = new byte[imgs.size()];
                for (int i = 0; i < imgs.size(); i++) {
                    byteimgs[i] = (byte) imgs.get(i).intValue();
                }
                Img2 bmpi2=item.getImg2();
                List<Integer> img2=bmpi2.getData();
                byteimg2 = new byte[img2.size()];
                for (int i = 0; i < img2.size(); i++) {
                    byteimg2[i] = (byte) img2.get(i).intValue();
                }
                Img3 bmpi3=item.getImg3();
                List<Integer> img3=bmpi3.getData();
                byteimg3 = new byte[img3.size()];
                for (int i = 0; i < img3.size(); i++) {
                    byteimg3[i] = (byte) img3.get(i).intValue();
                }
                Fragment fragment = new ViewApartmentFragment();
                Bundle bundle = new Bundle();
                bundle.putString("atype", item.getApartmentType());
                bundle.putString("renter", item.getRenterType());
                bundle.putString("address", item.getAddress());
                bundle.putString("size", item.getSize());
                bundle.putInt("rent", item.getRent());
                bundle.putString("displaystatus", "list");
                bundle.putString("description", item.getDescription());
                bundle.putByteArray("img1", byteimgs);
                bundle.putByteArray("img2",byteimg2);
                bundle.putByteArray("img3", byteimg3);
                fragment.setArguments(bundle);
                ((FragmentActivity)ctx).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment).addToBackStack("view").commit();
            }
        });

        holder.disLikeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    if (shrd.getUserid() != 0) {
                        Call<AddApartment> call = userServices.LikeDisLike(item.getApartmentId(), uid);
                        call.enqueue(new Callback<AddApartment>() {
                            @Override
                            public void onResponse(Call<AddApartment> call, Response<AddApartment> response) {
                                if (response.isSuccessful()) {

                                    if (response.body().getStatus()) {
                                        if (response.body().getMessage().equals("Apartment Added to Your Favourite")) {
                                            holder.disLikeBtn.setImageResource(R.drawable.like);
                                            Fragment fragment = new ApartmentListFragment();
                                            ((AppCompatActivity) ctx).getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.nav_host_fragment, fragment)
                                                    .addToBackStack(fragment.getTag())
                                                    .commit();
                                        } else {
                                            holder.disLikeBtn.setImageResource(R.drawable.dlike);
                                            Fragment fragment = new ApartmentListFragment();
                                            ((AppCompatActivity) ctx).getSupportFragmentManager()
                                                    .beginTransaction()
                                                    .replace(R.id.nav_host_fragment, fragment)
                                                    .addToBackStack(fragment.getTag())
                                                    .commit();
                                        }
                                    } else {
                                        System.out.println(response.body().getError());
                                    }
                                } else {
                                    new MaterialAlertDialogBuilder(ctx).setMessage("There is some error")
                                            .setPositiveButton("OK",(dialog, which) -> {

                                            }).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<AddApartment> call, Throwable t) {
                                System.out.println("error");
                            }
                        });
                    }else{
                        new MaterialAlertDialogBuilder(ctx).setMessage("You need to Sign in First")
                                .setPositiveButton("OK",(dialog, which) -> {
                                    Fragment fragment = new LoginFragment();
                                    ((AppCompatActivity) ctx).getSupportFragmentManager()
                                            .beginTransaction()
                                            .replace(R.id.nav_host_fragment, fragment)
                                            .addToBackStack(fragment.getTag())
                                            .commit();
                                }).show();
                    }
                }catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return apData.size();
    }
    public Bitmap getImage(byte[] image) {
        Bitmap bmsp = BitmapFactory.decodeByteArray(image, 0 ,image.length);
        return bmsp;
    }
}
