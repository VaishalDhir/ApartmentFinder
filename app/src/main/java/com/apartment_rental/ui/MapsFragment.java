package com.apartment_rental.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.apartment_rental.R;
import com.apartment_rental.controller.ApiUtils;
import com.apartment_rental.controller.UserService;
import com.apartment_rental.model.Apartments;
import com.apartment_rental.model.Register;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsFragment extends Fragment {
    UserService userServices;
    List<Apartments> apart=new ArrayList<>();

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
                                      for (int j = i; j <=len-1 ; j++) {
                                          LatLng latLng = new LatLng(apart.get(i).getData().get(j).getLatitude(), apart.get(i).getData().get(j).getLongitude());

                                         // LatLng sydney = new LatLng(-34, 151);
                                          googleMap.addMarker(new MarkerOptions().position(latLng));
                                          googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11.0f));
                                          googleMap.getUiSettings().setZoomControlsEnabled(true);
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






            // inside on map ready method
            // we will be displaying all our markers.
            // for adding markers we are running for loop and
            // inside that we are drawing marker on our map.




        }
    };

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