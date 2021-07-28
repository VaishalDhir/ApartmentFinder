package com.apartment_rental.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.apartment_rental.model.Img1;
import com.apartment_rental.model.Img2;
import com.apartment_rental.model.Img3;
import com.apartment_rental.ui.ApartmentListFragment;
import com.apartment_rental.ui.ViewApartmentFragment;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavouriteAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context ctx;
    List<Datuap> apData;
    byte[] byteimg1,byteimg2,byteimg3;
    Bitmap bms;
    UserService userServices;

    public FavouriteAdapter(Context ctx, List<Datuap> apData) {
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
        holder.disLikeBtn.setVisibility(View.GONE);

//        if(item.getStatus()==0) {
//            holder.disLikeBtn.setImageResource(R.drawable.dlike);
//        }else{
//            holder.disLikeBtn.setImageResource(R.drawable.like);
//        }
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
                bundle.putString("description", item.getDescription());
                bundle.putByteArray("img1", byteimgs);
                bundle.putByteArray("img2",byteimg2);
                bundle.putByteArray("img3", byteimg3);
                fragment.setArguments(bundle);
                ((FragmentActivity)ctx).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment).addToBackStack("view").commit();
            }
        });
//
//        holder.disLikeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                try {
//                    Call<AddApartment> call = userServices.LikeDisLike(item.getApartmentId(),uid);
//                    call.enqueue(new Callback<AddApartment>() {
//                        @Override
//                        public void onResponse(Call<AddApartment> call, Response<AddApartment> response) {
//                            if (response.isSuccessful()) {
//
//                                if (response.body().getStatus()) {
//                                    if(item.getStatus()==0) {
//                                        holder.disLikeBtn.setImageResource(R.drawable.like);
//                                        Fragment fragment = new ApartmentListFragment();
//                                        ((AppCompatActivity) ctx).getSupportFragmentManager()
//                                                .beginTransaction()
//                                                .replace(R.id.nav_host_fragment, fragment)
//                                                .addToBackStack(fragment.getTag())
//                                                .commit();
//                                    }else{
//                                        holder.disLikeBtn.setImageResource(R.drawable.dlike);
//                                        Fragment fragment = new ApartmentListFragment();
//                                        ((AppCompatActivity) ctx).getSupportFragmentManager()
//                                                .beginTransaction()
//                                                .replace(R.id.nav_host_fragment, fragment)
//                                                .addToBackStack(fragment.getTag())
//                                                .commit();
//
//                                    }
//                                } else {
//                                    System.out.println( response.body().getError());
//                                }
//                            } else {
//                                System.out.println( "Error Error");
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<AddApartment> call, Throwable t) {
//                            System.out.println("error");
//                        }
//                    });
//                } catch (Exception ex) {
//                    System.out.println(ex.toString());
//
//                }
//            }
//
//        });


    }

    @Override
    public int getItemCount() {
        return apData.size();
    }
    public Bitmap getImage(byte[] image) {
        // ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
        Bitmap bmsp = BitmapFactory.decodeByteArray(image, 0 ,image.length);
        return bmsp;

    }
}
