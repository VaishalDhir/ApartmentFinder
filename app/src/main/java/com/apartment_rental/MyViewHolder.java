package com.apartment_rental;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder{

    ImageView apartmentImage;
    TextView apartmentType,apartmentRent,apartmentFacility;

    CardView clickLayout;
    public MyViewHolder(View itemView) {
        super(itemView);

        apartmentType=(TextView) itemView.findViewById(R.id.apType);
        apartmentRent=(TextView) itemView.findViewById(R.id.apRent);
        apartmentFacility=(TextView) itemView.findViewById(R.id.apFacility);
        apartmentImage=(ImageView) itemView.findViewById(R.id.apImage);
        clickLayout=(CardView) itemView.findViewById(R.id.card_view_inner);

    }
}
