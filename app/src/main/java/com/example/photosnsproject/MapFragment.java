package com.example.photosnsproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment {
    private View view;
    private Context context;

    private GpsTracker gpsTracker;
    private double latitude=0.0, longitude=0.0;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    public static MapFragment newInstance(){
        return new MapFragment();
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        context = getActivity();
        view = inflater.inflate(R.layout.activity_map_fragment, container, false);

        checkPermissions();
        init();
        setFloatingButton();

        return view;
    }

    private void init() {
        TextView tvMyGps = view.findViewById(R.id.tvMyGPS);
        ImageView btnRenew = view.findViewById(R.id.btnRenew);
        btnRenew.setOnClickListener(v -> {
            Animation anim = AnimationUtils.loadAnimation(context, R.anim.rotate_anim);
            btnRenew.startAnimation(anim);
            gpsTracker = new GpsTracker(context);
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();

            String address = getCurrentAddress(latitude, longitude);
            tvMyGps.setText(address+" 근처의 인기 게시물이에요");
        });
        btnRenew.callOnClick();
    }

    private void checkPermissions() {
        if(!checkLocationServicesStatus()){
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSIONS_REQUEST_CODE && grantResults.length==REQUIRED_PERMISSIONS.length){
            boolean checkResult = true;

            for(int result : grantResults){
                if(result!=PackageManager.PERMISSION_GRANTED){
                    checkResult = false;
                    break;
                }
            }
            if(checkResult){

            }
            else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])||ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])){
                    Toast.makeText(context, "위치 권한이 거부되었습니다", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }else{
                    Toast.makeText(context, "위치 권한이 거부되었습니다. 설정(앱 정보)에서 위치 권한을 허용해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String getCurrentAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7
            );
        }catch (IOException ioException){
            return "지오코더 서비스 사용불가";
        }catch (IllegalArgumentException illegalArgumentException){
            return "잘못된 GPS 좌표";
        }
        if(addresses==null || addresses.size()==0){
            return "주소 미발견";
        }
        Address address = addresses.get(0);
        return address.getAddressLine(0).toString();
    }

    private void checkRunTimePermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(hasFineLocationPermission== PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission==PackageManager.PERMISSION_GRANTED){

        }else{
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])){
                Toast.makeText(context, "근처의 게시물을 보기 위해서는 위치 접근 권한이 필요합니다", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }else{
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("근처의 게시물을 보기 위해서는 위치 서비스가 필요합니다.\n위치 권한을 수정하시겠습니까?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", (dialog, which) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, GPS_ENABLE_REQUEST_CODE);
        });
        builder.setNegativeButton("취소", (dialog, which) -> {
            dialog.cancel();
        });
        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case GPS_ENABLE_REQUEST_CODE:
                if(checkLocationServicesStatus()){
                    checkRunTimePermission();
                    return;
                }
                break;
        }
    }

    private boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)||locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void setFloatingButton() {
        FloatingActionButton fab = view.findViewById(R.id.btnMap);
        fab.setOnClickListener(v -> {
            if(latitude!=0.0 && longitude!=0.0){
                Bundle bundle = new Bundle();
                bundle.putDouble("latitude", latitude);
                bundle.putDouble("longitude", longitude);
                SubMapFragment subMapFragment = SubMapFragment.newInstance();
                subMapFragment.setArguments(bundle);
                ((MainActivity)getActivity()).follow(subMapFragment);
            }
        });
    }
}