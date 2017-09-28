package ides.link.androidtask;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;

import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import butterknife.BindView;
import butterknife.ButterKnife;
import ides.link.androidtask.utilities.CommonUtilities;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;


public class MapFragment extends Fragment implements OnMapReadyCallback,
        LocationListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    @BindView(R.id.mapView) MapView mMapView;
    GoogleMap googleMap = null;
    boolean mLocationPermissionGranted;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 99;
    private GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    private static final String LOG_TAG = MapFragment.class.getSimpleName();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mMapView.onCreate(savedInstanceState);


        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        mMapView.getMapAsync(MapFragment.this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        if (mLocationPermissionGranted) {
            if (mLastLocation != null) {
                addMarker();
            }

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            mLocationPermissionGranted = false;
            if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                if (googleMap != null) {
                    addMarker();
                }
            } else {
                CommonUtilities.showPopupMessage(getActivity(),
                        getResources().getString(R.string.no_permission),
                        getResources().getString(R.string.no_permission_msg));            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        if (mLocationPermissionGranted) {
            googleMap.clear();
            mLastLocation = location;
            addMarker();
        }

    }


    private void addMarker() {

        LatLng currentLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
        googleMap.addMarker(new MarkerOptions()
                .position(currentLocation).title("Home Title")
                .snippet("Marker Description")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(currentLocation).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        LatLng arabContractors = new LatLng(30.055207, 31.297182);
        String disAr = CommonUtilities.getDistance(currentLocation, arabContractors);
        googleMap.addMarker(new MarkerOptions().position(arabContractors).title("Arab Contractors").snippet(disAr));

        LatLng baronPalace = new LatLng(30.086850, 31.330828);
        String disBa = CommonUtilities.getDistance(currentLocation, baronPalace);
        googleMap.addMarker(new MarkerOptions().position(baronPalace).title("Baron Palace").snippet(disBa));

        LatLng wonderLand = new LatLng(30.048075, 31.339068);
        String disWa = CommonUtilities.getDistance(currentLocation, baronPalace);
        googleMap.addMarker(new MarkerOptions().position(wonderLand).title("Wonder Land").snippet(disWa));

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                LatLng position = marker.getPosition();
                if (position.latitude != mLastLocation.getLatitude()) {
                    String uri = "http://maps.google.com/maps?saddr=" +
                            mLastLocation.getLatitude() + "," + mLastLocation.getLongitude() + "&daddr=" +
                            position.latitude + "," + position.latitude + "&mode=driving";

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);

                }
            }
        });

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            mLocationPermissionGranted = true;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (googleMap != null) {
            if (mLocationPermissionGranted) {
                addMarker();
            }
        }


    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(LOG_TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(LOG_TAG, "onConnectionFailed");
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

}