package ca.ualberta.cs.swapmyride.Model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.location.LocationManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by adrianomarini on 15-11-20.
 *
 * This class deals with all functions related to location
 * of items.
 *
 */
public class Geolocation {
    static final int MY_PERMISSIONS_REQUEST__COARSE_LOCATION = 1;
    static final int MY_PERMISSIONS_REQUEST__FINE_LOCATION = 1;

    private LocationManager lm;

    /**
     * @param context
     * @param activity
     *
     * This method deals with finding the device's current location
     *
     * This is considered the default location for items
     *
     * Method for providers and locationadapted from:
     * http://stackoverflow.com/questions/17591147/how-to-get-current-location-in-android
     * User: Boris Pawlowski (& Thomas Clemensen)           Accessed: 22-11-2015
     *
     * Method for finding address using Geocoder adapted from
     * http://stackoverflow.com/questions/9409195/how-to-get-complete-address-from-latitude-and-longitude
     * User: user370305                                    Accessed: 22-11-2015
     *
     */

    public Address getCurrentLocation(Context context, Activity activity) throws IllegalArgumentException {
        Address address = new Address(Locale.getDefault());
        lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // http://developer.android.com/training/permissions/requesting.html
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST__COARSE_LOCATION);

        }
        // http://developer.android.com/training/permissions/requesting.html
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        } else {

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST__FINE_LOCATION);

        }

        //Check which location providers are enabled
        //The two major ones are gps services and the mobile network
        Boolean gps = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Boolean network = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Location lastKnownLocation = new Location("GPS");

        if (!gps && !network){
            throw new IllegalArgumentException();
        }

        if (gps) {
            lastKnownLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        }

        //If GPS is not active, use the network to get a location
        if (!gps && network) {
            lastKnownLocation = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if(lastKnownLocation == null){

        }

        //If it is not null, try to get an address from the lat/long
        // that is returned -- Using Geocoder
        else{
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            double longitude = lastKnownLocation.getLongitude();
            double latitude = lastKnownLocation.getLatitude();

            address.setLongitude(longitude);
            address.setLatitude(latitude);

            //Get one possible address
            List<Address> addresses;
            try{
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
            } catch (IOException e){
                address.setLocality("ERROR");
                address.setCountryName("IO");
                return address;
            } catch (IllegalArgumentException e){
                address.setLocality("ERROR");
                address.setCountryName("Illegal");
                return address;
            }

            //set attributes from this one possible address
            if(addresses != null && addresses.size() == 1){
                address.setLocality(addresses.get(0).getLocality());
                address.setAddressLine(0, addresses.get(0).getAddressLine(0));
                address.setPostalCode(addresses.get(0).getPostalCode());
                address.setAdminArea(addresses.get(0).getAdminArea());
                address.setCountryName(addresses.get(0).getCountryName());}
        }
        return address;
    }

    public Address setSpecificLocation(Context context, String location){
        Address address = new Address(Locale.getDefault());
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try{
            addresses = geocoder.getFromLocationName(location, 1);
            address.setLocality(addresses.get(0).getLocality());
            address.setCountryName(addresses.get(0).getCountryName());
            address.setAdminArea(addresses.get(0).getAdminArea());
            address.setAddressLine(0, addresses.get(0).getAddressLine(0));
            address.setPostalCode(location);
            address.setLatitude(addresses.get(0).getLatitude());
            address.setLongitude(addresses.get(0).getLongitude());
        }
        catch(IOException e){
            return address;
        }
        catch (IllegalArgumentException e){
            return address;
        }
        return address;
    }
}