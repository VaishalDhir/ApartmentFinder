package com.apartment_rental.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.apartment_rental.R;
import com.apartment_rental.controller.ApiUtils;
import com.apartment_rental.controller.UserService;
import com.apartment_rental.model.Datuap;
import com.apartment_rental.model.Img1;
import com.apartment_rental.model.Img2;
import com.apartment_rental.model.Img3;
import com.apartment_rental.ui.ViewApartmentFragment;

import java.util.List;

public class RenterPropertyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context ctx;
    List<Datuap> apData;
    byte[] byteimg1,byteimg2,byteimg3;
    Bitmap bms;
    UserService userServices;

    public RenterPropertyAdapter(Context ctx, List<Datuap> apData) {
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

    }


    public void removeItem(int position) {
        apData.remove(position);
        notifyItemRemoved(position);
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
