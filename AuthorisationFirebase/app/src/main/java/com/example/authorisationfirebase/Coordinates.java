package com.example.authorisationfirebase;

import com.google.android.gms.maps.model.LatLng;

public class Coordinates {

    private LatLng latLngs;

    public Coordinates(LatLng latLngs){
        this.latLngs = latLngs;
    }

    public LatLng listOfLatLngs(Coordinates latLng){

        LatLng latLng1 = latLng.getLatLngs();

        return latLng1;
    }

    public LatLng getLatLngs(){
        return this.latLngs;
    }


}
