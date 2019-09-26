package com.example.mainpolygon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {



    TextView gps;
    AlertDialog.Builder builder,builder_save,builder_delete;
    AlertDialog alertDialog,alertDialog_save, alertDialog_delete ;
    Button recordGps,cancel,saveGarden,cancel_save,delete, cancel_delete;


    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (googleServicesAvailable()){
            Toast.makeText(this,"Google Play Services Are Available",Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_main);


            //before inflating the custom alert dialog layout, we will get the current activity viewgroup
            android.view.ViewGroup viewGroup = (android.view.ViewGroup) findViewById(android.R.id.content);

            //then we will inflate the custom alert dialog xml that we created
            View dialogView = LayoutInflater.from(this).inflate(R.layout.add_new_point_dialog, viewGroup, false);

            //then we will inflate the custom alert dialog xml that we created
            View dialogView_save = LayoutInflater.from(this).inflate(R.layout.save_garden_dialog, viewGroup, false);

            //then we will inflate the custom alert dialog xml that we created
            View dialogView_delete = LayoutInflater.from(this).inflate(R.layout.delete_point_dialog, viewGroup, false);


            gps= (TextView) dialogView.findViewById(R.id.gps);

            recordGps= (Button) dialogView.findViewById(R.id.buttonOk);
            saveGarden= (Button) dialogView_save.findViewById(R.id.buttonSave);

            cancel_save= (Button) dialogView_save.findViewById(R.id.btnCancel);
            cancel = (Button) dialogView.findViewById(R.id.buttonCancel);

            cancel_delete= (Button) dialogView_delete.findViewById(R.id.btnCancel);
            delete= (Button) dialogView_delete.findViewById(R.id.btnDelete);

            //Edit text to pick values for long and lat

            //Editextlat = findViewById(R.id.edt_lat);



            //Now we need an AlertDialog.Builder object
            builder = new AlertDialog.Builder(this);
            builder_save = new AlertDialog.Builder(this);
            builder_delete = new AlertDialog.Builder(this);


            //setting the view of the builder to our custom view that we already inflated
            builder.setView(dialogView);
            builder_save.setView(dialogView_save);
            builder_delete.setView(dialogView_delete);

            //finally creating the alert dialog and displaying it
            alertDialog= builder.create();
            alertDialog_save= builder_save.create();
            alertDialog_delete= builder_delete.create();

//start google maps

            initMap();
        }else {
            Toast.makeText(this,"No Google Maps",Toast.LENGTH_SHORT).show();
        }

    }

    private void initMap(){
        MapFragment mMapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.maps);
        mMapFragment.getMapAsync(this);
    }

    public boolean googleServicesAvailable(){

        GoogleApiAvailability availability = GoogleApiAvailability.getInstance();
        int isAvailable = availability.isGooglePlayServicesAvailable(this);

        if (isAvailable == ConnectionResult.SUCCESS){
            return  true;

        }else if(availability.isUserResolvableError(isAvailable)){
            Dialog dialog = availability.getErrorDialog(this,isAvailable,0);
            dialog.show();
        }else{
            Toast.makeText(this, "Can't Connect to Play Services", Toast.LENGTH_SHORT).show();
        }
        return false;

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        gotoLocationzoom(0.3246214,32.5740853,15);


        if (mGoogleMap != null){

            mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    MainActivity.this.setMarker("Local",latLng.latitude,latLng.longitude);
                }
            });


            /*
                      mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                @Override
                public void onMarkerDragStart(Marker marker) {

                }

                @Override
                public void onMarkerDrag(Marker marker) {

                }

                @Override
                public void onMarkerDragEnd(Marker marker) {

                    Geocoder mGeocorder =  new Geocoder(MainActivity.this);

                    LatLng ll = marker.getPosition();

                    double lat = ll.latitude;
                    double lng = ll.longitude;
                    List<Address> list  = null;

                    try {
                        list  = mGeocorder.getFromLocation(lat,lng,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Address address = list.get(0);

                    marker.setTitle(address.getLocality());
                    //marker.


                }
            });
*/

        }
        //gotoLocationzoom(0.3246214,32.5740853,15);

        //mGoogleMap.setMyLocationEnabled(true);

/*
mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();
* */


    }

    private void  gotoLocation(double lat,double lng){

        LatLng ll = new LatLng(lat,lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLng(ll);
        mGoogleMap.moveCamera( cameraUpdate);

    }

    private void  gotoLocationzoom(double lat,double lng,float zoom){

        LatLng ll = new LatLng(lat,lng);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(ll,zoom);
        mGoogleMap.moveCamera( cameraUpdate);

    }


    Marker mMarker ;

    public void geoLocate(View view) throws IOException {

/*
   EditText locate = findViewById(R.id.locate);
        String location = locate.getText().toString();


        Geocoder geocoder = new Geocoder(this);
        List<Address> list = geocoder.getFromLocationName(location,1);

        Address address = list.get(0);

        String locality = address.getLocality();

        Toast.makeText(this, "Location"+locality, Toast.LENGTH_SHORT).show();


        double lat = address.getLatitude();
        double lng = address.getLongitude();

        gotoLocationzoom(lat,lng,15);

        setMarker(locality, lat, lng);
* */




    }

    Circle circle;


    ArrayList<Marker> markers_list= new ArrayList<Marker>();
    static  final int Polygon_Points = 10;
    Polygon shape;

    private void setMarker(String locality, double lat, double lng) {
        /*
          if (mMarker != null){

            removeEverything();
            mMarker.remove();

        }
        * */

        if (markers_list.size()== Polygon_Points){

        }


        MarkerOptions mMarkerOptions = new MarkerOptions()
                .title(locality)
                .draggable(true)
                .position(new LatLng(lat,lng))
                .snippet("I am Here");


        mMarker = mGoogleMap.addMarker(mMarkerOptions);

        markers_list.add(mMarker);

        if (markers_list.size() == Polygon_Points){

            drawPolygon();

        }

        circle = drawCircle(new LatLng(lat,lng));
    }

    private void drawPolygon() {

        PolygonOptions options = new PolygonOptions()
                .fillColor( 0x33000FF)
                .strokeWidth(3)
                .strokeColor(Color.RED);

        for (int i = 0 ; i < Polygon_Points; i++){
             options.add(markers_list.get(i).getPosition());

        }

        shape = mGoogleMap.addPolygon(options);


    }

    private Circle drawCircle(LatLng latlng) {


        CircleOptions options = new CircleOptions()
                .center(latlng)
                .radius(500)
                .fillColor(0x33FF0000)
                .strokeColor(Color.BLUE)
                .strokeWidth(3);

        return mGoogleMap.addCircle(options);

    }


    private void removerEverything(){

        mMarker.remove();
        mMarker = null;
        circle.remove();
        circle = null;

    }

    /*
     public void geoLocate() {
    }
    * */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.mapTypeNone:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            case R.id.add_point:
                showDialog();
                break;
            case R.id.save:
                alertDialog_save.show();
                break;
                default:
                    break;


        }
        return super.onOptionsItemSelected(item);
    }

    private void showDialog() {
        alertDialog.show();

    }

    LocationRequest mLocationRequest;
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(2000);

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);




    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        if (location == null){
            Toast.makeText(this, "Cant get Current Location", Toast.LENGTH_SHORT).show();
        } else {

            LatLng userLocation =  new LatLng(location.getLatitude(),location.getLongitude());

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLocation,15);
            mGoogleMap.animateCamera(cameraUpdate);

        }

    }
}
