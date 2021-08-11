package com.apartment_rental.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apartment_rental.R;
import com.apartment_rental.SharedPref;
import com.apartment_rental.controller.ApiUtils;
import com.apartment_rental.controller.UserService;
import com.apartment_rental.model.AddApartment;
import com.apartment_rental.model.LoginResponse;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class AddApartmentFragment extends Fragment implements LocationListener {
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 7;
    private Location location;
    ImageView apartment_img1, apartment_img2, apartment_img3;
    private int flag;
    private LocationManager locationManager;
    private String provider;
    UserService userServices;
    byte[] imgarr1, imgarr2, imgsarr3;
    double lat, lang;
    private ProgressDialog progress;
    TextView errorText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_apartment, container, false);
        checkAndroidVersion();

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the locatioin provider -> use
        // default
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission
                    (getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    &&
                    ActivityCompat.checkSelfPermission
                            (getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 1); // 1 is requestCode

            }
        }
        Location location = locationManager.getLastKnownLocation(provider);

        // Initialize the location fields
        if (location != null) {
            System.out.println("Provider " + provider + " has been selected.");
            onLocationChanged(location);
        } else {
            Toast.makeText(getContext(), "Location not available", Toast.LENGTH_SHORT).show();

        }
        userServices = ApiUtils.getUserService();

        EditText apartmentType_ed = (EditText) view.findViewById(R.id.apartType_ed);
        EditText apartmentadd_ed = (EditText) view.findViewById(R.id.apartaddress_ed);
        EditText apartmentDesc_ed = (EditText) view.findViewById(R.id.apartdesc_ed);
        EditText apartmentRent_ed = (EditText) view.findViewById(R.id.apartrent_ed);
        EditText apartmentSize_ed = (EditText) view.findViewById(R.id.apartsize_ed);
        EditText apartmentFacilty_ed = (EditText) view.findViewById(R.id.apartfacility_ed);

        Button btnImg1 = (Button) view.findViewById(R.id.apart_img1);
        Button btnImg2 = (Button) view.findViewById(R.id.apart_img2);
        Button btnImg3 = (Button) view.findViewById(R.id.apart_img3);
        apartment_img1 = (ImageView) view.findViewById(R.id.img1);
        apartment_img2 = (ImageView) view.findViewById(R.id.img2);
        apartment_img3 = (ImageView) view.findViewById(R.id.img3);
         errorText=(TextView) view.findViewById(R.id.setError);
        RadioGroup renter_type = (RadioGroup) view.findViewById(R.id.radioGroup);
        Button addApartment = (Button) view.findViewById(R.id.addProp);


        btnImg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 1;
                selectImage();
            }
        });
        btnImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 2;
                selectImage();
            }
        });
        btnImg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = 3;
                selectImage();
            }
        });

        addApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ApartType = apartmentType_ed.getText().toString();
                String ApartAddress = apartmentadd_ed.getText().toString();
                String ApartDesc_ed = apartmentDesc_ed.getText().toString();
                String Rent = apartmentRent_ed.getText().toString();
                if(Rent.equals("")){
                    Rent="0";
                }
                int RentVal=Integer.parseInt(Rent);
                String Size = apartmentSize_ed.getText().toString();
                String facility = apartmentFacilty_ed.getText().toString();
                int selectedId = renter_type.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                RadioButton radioButton = (RadioButton) view.findViewById(selectedId);
                if(RentVal>=1000 && RentVal<=5000){

                if (validateApartment(ApartType, ApartAddress, String.valueOf(RentVal), Size, ApartDesc_ed, facility)) {
                    progress=new ProgressDialog(getActivity());
                    progress.setMessage("Please Wait for a While");
                    progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progress.show();
                    AddProperty(ApartType, ApartAddress, String.valueOf(Rent), Size, ApartDesc_ed, facility, radioButton.getText().toString());
                }}else{
                        errorText.setText("Rent Should be in between 1000-5000");
                }
            }
        });
        return view;

    }
    /* Request updates at startup */
    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ActivityCompat.checkSelfPermission
                    (getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    &&
                    ActivityCompat.checkSelfPermission
                            (getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, 1); // 1 is requestCode

            }
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    private void AddProperty(String apartType, String apartAddress, String rent, String size, String apartDesc_ed, String facility, String rtype) {
        SharedPref shrd = new SharedPref(getActivity());
        int uid = shrd.getUserid();
        try {
            Call<AddApartment> call = userServices.InsertProperty(uid, apartAddress, apartType,
                    rent, size, facility, apartDesc_ed, rtype, imgarr1, imgarr2, imgsarr3, lat, lang);
            call.enqueue(new Callback<AddApartment>() {
                @Override
                public void onResponse(Call<AddApartment> call, Response<AddApartment> response) {
                    if (response.isSuccessful()) {

                        if (response.body().getStatus()) {
                            progress.dismiss();
                            new MaterialAlertDialogBuilder(getActivity()).setMessage(response.body().getMessage())
                                    .setPositiveButton("OK",(dialog, which) -> {
                                        Fragment fragment = new ProfileFragment();
                                        ((AppCompatActivity) getActivity()).getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.nav_host_fragment, fragment)
                                                .addToBackStack(fragment.getTag())
                                                .commit();
                                    }).show();

                        } else {
                            progress.dismiss();
                            new MaterialAlertDialogBuilder(getActivity()).setMessage(response.body().getError())
                                    .setPositiveButton("OK",(dialog, which) -> {
///
                                    }).show();
                        }
                    } else {
                        progress.dismiss();
                        new MaterialAlertDialogBuilder(getActivity()).setMessage("Error! Please try again!")
                                .setPositiveButton("OK",(dialog, which) -> {
                                   /////
                                }).show();
                    }
                }

                @Override
                public void onFailure(Call<AddApartment> call, Throwable t) {
                    progress.dismiss();
                    new MaterialAlertDialogBuilder(getActivity()).setMessage("Error! Please try again!")
                            .setPositiveButton("OK",(dialog, which) -> {
                                /////
                            }).show();
                }
            });
        } catch (Exception ex) {
            progress.dismiss();
            new MaterialAlertDialogBuilder(getActivity()).setMessage(ex.toString())
                    .setPositiveButton("OK",(dialog, which) -> {
                        /////
                    }).show();

        }
    }


    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");


                if (flag == 1) {
                    imgarr1 = getBytesFromBitmap(photo);
                    apartment_img1.setImageBitmap(photo);
                } else if (flag == 2) {
                    imgarr2 = getBytesFromBitmap(photo);
                    apartment_img2.setImageBitmap(photo);

                } else {
                    imgsarr3 = getBytesFromBitmap(photo);
                    apartment_img3.setImageBitmap(photo);

                }

            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getActivity().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                // c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));

                Bitmap b2 = null;
                final int destWidth = 500;//or the width you need
                int origWidth = thumbnail.getWidth();
                int origHeight = thumbnail.getHeight();
                if (origWidth > destWidth) {
                    // picture is wider than we want it, we calculate its target height
                    int destHeight = origHeight / (origWidth / destWidth);
                    // we create an scaled bitmap so it reduces the image, not just trim it
                    b2 = Bitmap.createScaledBitmap(thumbnail, destWidth, destHeight, false);
//
//                    // we save the file, at least until we have made use of it
                }


                if (flag == 1) {

                    imgarr1 = getBytesFromBitmap(b2);
                    apartment_img1.setImageBitmap(b2);
                } else if (flag == 2) {
                    imgarr2 = getBytesFromBitmap(b2);
                    apartment_img2.setImageBitmap(b2);
                } else {
                    imgsarr3 = getBytesFromBitmap(b2);
                    apartment_img3.setImageBitmap(b2);
                }
            }
        }
    }

    private void checkAndroidVersion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkAndRequestPermissions();
        }

    }

    private boolean checkAndRequestPermissions() {
        int camera = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA);
        int wtite = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (wtite != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (camera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (read != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("in fragment on request", "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("in fragment on request", "CAMERA & WRITE_EXTERNAL_STORAGE READ_EXTERNAL_STORAGE permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                    } else {
                        Log.d("in fragment on request", "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
//                        // shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA) ||
                                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showMessageOKCancel("Camera and Storage Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    break;
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getActivity(), "Go to settings and enable permissions", Toast.LENGTH_LONG)
                                    .show();
                            //                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            return stream.toByteArray();
        }
        return null;
    }


    private boolean validateApartment(String aType, String address,String rent,String size,String desc,String facility) {

        if (aType == null || aType.trim().length() == 0) {
            errorText.setText("Apartment Type is required");
            return false;
        }
        if (address == null || address.trim().length() == 0) {
            errorText.setText("Address is required");

            return false;
        }
        if (rent == null || rent.trim().length() == 0) {
            errorText.setText("Rent is required");

            return false;
        }
        if (size == null || size.trim().length() == 0) {
            errorText.setText("Size is required");

            return false;
        }
        if (desc == null || desc.trim().length() == 0) {
            errorText.setText("Description Type is required");

            return false;
        }
        if (facility == null || facility.trim().length() == 0) {
            errorText.setText("Facility Type is required");

            return false;
        }
        return true;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        lat=location.getLatitude();
        lang=location.getLongitude();
        System.out.println(lang);
    }
}

//Reference for current lat lang
//https://www.vogella.com/tutorials/AndroidLocationAPI/article.html