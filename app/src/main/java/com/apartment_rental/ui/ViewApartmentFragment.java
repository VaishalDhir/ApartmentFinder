package com.apartment_rental.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apartment_rental.Adapter.ApartmentListAdapter;
import com.apartment_rental.R;
import com.apartment_rental.SharedPref;
import com.apartment_rental.controller.ApiUtils;
import com.apartment_rental.controller.UserService;
import com.apartment_rental.model.Apartments;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewApartmentFragment extends Fragment {
    UserService userServices;

    SharedPref shrdd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vw=inflater.inflate(R.layout.fragment_view_apartment, container, false);

        userServices= ApiUtils.getUserService();
        ViewPager imagePager=(ViewPager) vw.findViewById(R.id.imgPager);
        TextView vapRent=(TextView) vw.findViewById(R.id.vapRent);
        TextView vapSize=(TextView) vw.findViewById(R.id.vapSize);
        TextView vapRenterType=(TextView) vw.findViewById(R.id.renterType);
        TextView vapAddress=(TextView) vw.findViewById(R.id.vapAddress);
        TextView vapType=(TextView) vw.findViewById(R.id.vapType);
        TextView vapDesc=(TextView) vw.findViewById(R.id.vapdiscription);
        ImageView backBtnImg=(ImageView) vw.findViewById(R.id.backbtn);
        ImageView shareBtnImg=(ImageView) vw.findViewById(R.id.imagedShareIcon);
        Button contact_person=(Button) vw.findViewById(R.id.contact_person);
        Bundle bundle = this.getArguments();
        shrdd=new SharedPref(getActivity());
        String Type = bundle.getString("atype");
        String RenterType = bundle.getString("renter");
        String Description = bundle.getString("description");
        String Address = bundle.getString("address");
        String screenType = bundle.getString("displaystatus");
        String Size=bundle.getString("size");
        int Rent=bundle.getInt("rent");
        int apId=bundle.getInt("aid");
        byte[] img1=bundle.getByteArray("img1");
        byte[] img2=bundle.getByteArray("img2");
        byte[] img3=bundle.getByteArray("img3");

        ArrayList<byte[]> imgList=new ArrayList<>();
        imgList.add(img1);
        imgList.add(img2);
        imgList.add(img3);

        // Initializing the ViewPagerAdapter
      ViewPagerAdapter  mViewPagerAdapter = new ViewPagerAdapter(getActivity(), imgList);

        // Adding the Adapter to the ViewPager
        imagePager.setAdapter(mViewPagerAdapter);

        vapRent.setText(String.valueOf(Rent)+" C.A.D");
        vapSize.setText(Size);
        vapRenterType.setText(RenterType);
        vapAddress.setText(Address);
        vapType.setText(Type);
        vapDesc.setText(Description);

        contact_person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shrdd.getUserid()!=0) {


                    getEmailId(apId);


                }else{
                    new MaterialAlertDialogBuilder(getActivity()).setMessage("You need to Sign in First")
                            .setPositiveButton("OK",(dialog, which) -> {
                                Fragment fragment = new LoginFragment();
                                ((AppCompatActivity) getActivity()).getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.nav_host_fragment, fragment)
                                        .addToBackStack(fragment.getTag())
                                        .commit();
                            }).show();
                }
            }
        });


        backBtnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(screenType.equals("map")){
                    Fragment fragment = new MapsFragment();
                    ((AppCompatActivity) getActivity()).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment, fragment)
                            .addToBackStack(fragment.getTag())
                            .commit();
                }else  if(screenType.equals("list")){
                    Fragment fragment = new ApartmentListFragment();
                    ((AppCompatActivity) getActivity()).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment, fragment)
                            .addToBackStack(fragment.getTag())
                            .commit();
                }else{
                    Fragment fragment = new ViewFavouriteFragment();
                    ((AppCompatActivity) getActivity()).getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.nav_host_fragment, fragment)
                            .addToBackStack(fragment.getTag())
                            .commit();
                }
            }
        });
        shareBtnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT,Address+Type );
                try {
                    requireActivity().startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.whatsapp")));
                }
            }
        });
        return vw;
     }

    private void getEmailId(int apId) {
        try {
            Call<Apartments> call=userServices.getEmailId(apId);
            call.enqueue(new Callback<Apartments>() {
                @Override
                public void onResponse(Call<Apartments> call, Response<Apartments> response) {
                    if (response.isSuccessful()) {

                        if(response.body().getStatus()){

                            Intent intent = new Intent(Intent.ACTION_SENDTO);
                            intent.setData(Uri.parse("mailto:?subject=" + "Regarding Apartment Visit" + "&to=" +response.body().getData().get(0).getEmail())); // only email apps should handle this
                            intent.putExtra(Intent.EXTRA_EMAIL, "ashish4321ynr@gmail.com");


                            try {
                                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                                    startActivity(intent);
                                }
                            } catch (android.content.ActivityNotFoundException ex) {
                                Toast.makeText(getActivity(), "No Email client found!!",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            new MaterialAlertDialogBuilder(getActivity()).setMessage(response.body().getErr())
                                    .setPositiveButton("OK",(dialog, which) -> {

                                    }).show();
                        }
                    } else {
                        new MaterialAlertDialogBuilder(getActivity()).setMessage("Error! Please try again!")
                                .setPositiveButton("OK",(dialog, which) -> {

                                }).show();
                    }
                }
                @Override
                public void onFailure(Call<Apartments> call, Throwable t) {
                    new MaterialAlertDialogBuilder(getActivity()).setMessage("Error! Please try again!")
                            .setPositiveButton("OK",(dialog, which) -> {

                            }).show();
                }
            });
        }catch (Exception ex){
            new MaterialAlertDialogBuilder(getActivity()).setMessage(ex.toString())
                    .setPositiveButton("OK",(dialog, which) -> {

                    }).show();
        }
    }


    public  class ViewPagerAdapter extends PagerAdapter{
        Context ctx;
        ArrayList<byte[]> imgList;
        LayoutInflater mLayoutInflater;
        Bitmap bitmapImage;
        public ViewPagerAdapter(Context ctx, ArrayList<byte[]> imgList) {
            this.ctx=ctx;
            this.imgList=imgList;
            mLayoutInflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return imgList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((LinearLayout) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, final int position) {
            // inflating the item.xml
            View itemView = mLayoutInflater.inflate(R.layout.imagelayout, container, false);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageViewMain);

            bitmapImage=getImage(imgList.get(position));
            imageView.setImageBitmap(bitmapImage);

            Objects.requireNonNull(container).addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((LinearLayout) object);

        }
        public Bitmap getImage(byte[] image) {
            // ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(bytes);
            Bitmap bmsp = BitmapFactory.decodeByteArray(image, 0 ,image.length);
            return bmsp;
        }
    }
}