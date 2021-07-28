package com.apartment_rental.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apartment_rental.R;
import com.apartment_rental.controller.ApiUtils;
import com.apartment_rental.controller.UserService;
import com.apartment_rental.model.Apartments;
import com.apartment_rental.model.Datuap;
import com.apartment_rental.model.Img1;
import com.apartment_rental.model.Img2;
import com.apartment_rental.model.Img3;
import com.apartment_rental.model.Register;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsFragment extends Fragment{
    UserService userServices;
    List<Apartments> apart=new ArrayList<>();
    Marker myMarker;
    Context ctx;
    List<Datuap> item=new ArrayList<>();


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
//            maps=googleMap;
            try {
                Call<Apartments> call=userServices.getAllApartments();
                call.enqueue(new Callback<Apartments>() {
                    @Override
                    public void onResponse(Call<Apartments> call, Response<Apartments> response) {
                        if (response.isSuccessful()) {

                            if(response.body().getStatus()){
                                apart.add(response.body());
                                for (int i = 0; i < apart.size(); i++) {
                                    int len=apart.get(i).getData().size();
                                      for (int j = i; j <=len-1 ; j+=1) {
                                          LatLng latLng = new LatLng(apart.get(i).getData().get(j).getLatitude(),
                                                  apart.get(i).getData().get(j).getLongitude());
                                          myMarker = googleMap.addMarker(new MarkerOptions().position(latLng).title(apart.get(i).getData().get(j).getApartmentType())
                                                  .snippet(apart.get(i).getData().get(j).getRent()+" C.A.D"));

                                          googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11.0f));
                                          googleMap.getUiSettings().setZoomControlsEnabled(true);
                                          item.add(apart.get(i).getData().get(j));

                                          googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                                              @Override
                                              public void onInfoWindowClick(@NonNull Marker marker) {
                                                  String title=marker.getTitle();
                                                  System.out.println(title);
                                                  showInfo(title);
//
                                              }
                                          });

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
    };

    private void showInfo(String title) {
        for(int val=0;val<=item.size();val++){
            if(item.get(val).getApartmentType().equals(title)){

                                                  Fragment fragment=new ViewApartmentFragment();
                                                  FragmentTransaction ft = getFragmentManager().beginTransaction();


                                                  Log.d("Map_Tag", "CLICK");
                                                  Img1 bmpis=item.get(val).getImg1();


                                                  List<Integer> imgs=bmpis.getData();
                                                  byte[] byteimgs = new byte[imgs.size()];
                                                  for (int i = 0; i < imgs.size(); i++) {
                                                      byteimgs[i] = (byte) imgs.get(i).intValue();
                                                  }

                                                  Img2 bmpi2=item.get(val).getImg2();
                                                  List<Integer> img2=bmpi2.getData();
                                                 byte[] byteimg2 = new byte[img2.size()];
                                                  for (int i = 0; i < img2.size(); i++) {
                                                      byteimg2[i] = (byte) img2.get(i).intValue();
                                                  }
                                                  Img3 bmpi3=item.get(val).getImg3();

                                                  List<Integer> img3=bmpi3.getData();
                                                 byte[] byteimg3 = new byte[img3.size()];
                                                  for (int i = 0; i < img3.size(); i++) {
                                                      byteimg3[i] = (byte) img3.get(i).intValue();
                                                  }
                                                  Bundle bundle = new Bundle();
                                                  String aatype= item.get(val).getApartmentType();
                                                  bundle.putString("atype",aatype);
                                                  bundle.putString("renter", item.get(val).getRenterType());
                                                  bundle.putString("address", item.get(val).getAddress());
                                                  bundle.putString("size", item.get(val).getSize());
                                                  bundle.putInt("rent", item.get(val).getRent());
                                                  bundle.putString("description", item.get(val).getDescription());
                                                  bundle.putByteArray("img1", byteimgs);
                                                  bundle.putByteArray("img2",byteimg2);
                                                  bundle.putByteArray("img3", byteimg3);
                                                  fragment.setArguments(bundle);
                                                   ft.replace(R.id.nav_host_fragment, fragment).addToBackStack("view").commit();
                                                   break;
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission
                    (getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    &&
                    ActivityCompat.checkSelfPermission
                            (getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 1); // 1 is requestCode

            }
        }
        userServices = ApiUtils.getUserService();
     //   getApartmentData();

        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case 1:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    System.out.println("PERMISSION DENIED");
                } else {
                    System.out.println("PERMISSION GRANTED");

                    // permission granted do something
                }
                break;
        }
    }
}