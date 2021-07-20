package com.apartment_rental;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.apartment_rental.model.Datuap;
import com.apartment_rental.model.Img1;

import java.util.List;

public class ApartmentListAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context ctx;
    List<Datuap> apData;
    byte[] bytes;
    Bitmap bms;

    public ApartmentListAdapter(Context ctx, List<Datuap> apData) {
        this.ctx=ctx;
        this.apData=apData;
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

        holder.apartmentRent.setText(item.getRent().toString());
        holder.apartmentType.setText(item.getApartmentType());
        holder.apartmentFacility.setText(item.getFacility());
        Img1 bmp=item.getImg1();

        List<Integer> img=bmp.getData();
        bytes = new byte[img.size()];
        for (int i = 0; i < img.size(); i++) {
            bytes[i] = (byte) img.get(i).intValue();
        }
        bms=getImage(bytes);
        holder.apartmentImage.setImageBitmap(bms);
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
