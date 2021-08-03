package com.apartment_rental.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.apartment_rental.R;
import com.apartment_rental.SharedPref;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ViewApartmentFragment extends Fragment {

    SharedPref shrdd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View vw=inflater.inflate(R.layout.fragment_view_apartment, container, false);


        ViewPager imagePager=(ViewPager) vw.findViewById(R.id.imgPager);
        TextView vapRent=(TextView) vw.findViewById(R.id.vapRent);
        TextView vapSize=(TextView) vw.findViewById(R.id.vapSize);
        TextView vapRenterType=(TextView) vw.findViewById(R.id.renterType);
        TextView vapAddress=(TextView) vw.findViewById(R.id.vapAddress);
        TextView vapType=(TextView) vw.findViewById(R.id.vapType);
        TextView vapDesc=(TextView) vw.findViewById(R.id.vapdiscription);
        ImageView backBtnImg=(ImageView) vw.findViewById(R.id.backbtn);
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
                    //   sendSMS("+15149636316", "Hello world");
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
        return vw;
    }
//
//    private void sendSMS(String phoneNo, String msg) {
//        try {
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
//            Toast.makeText(getActivity(), "Message Sent",
//                    Toast.LENGTH_LONG).show();
//        } catch (Exception ex) {
//            Toast.makeText(getActivity(),ex.getMessage().toString(),
//                    Toast.LENGTH_LONG).show();
//            ex.printStackTrace();
//        }
//    }

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

            // referencing the image view from the item.xml file
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