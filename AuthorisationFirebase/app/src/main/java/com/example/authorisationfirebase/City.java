package com.example.authorisationfirebase;

import com.google.android.gms.maps.model.LatLng;

public class City {

    private LatLng posX;
    private LatLng posY;

    /**
     * Initalize a city
     *
     */
    public City(LatLng posX, LatLng posY) {
        this.posX = posX;
        this.posY = posY;
    }

    /**
     * This method is needed in cases where the
     * distance between two latitudes and longitudes
     * must be calculated.
     * It uses a math formula called Haversine formula.
     *
     * In our case all we need to is we have to pass the latitudes and
     * longitudes of the places so the method can perform the calculus.
     *
     * Thereafter, the genetic algorithm will perform in order to solve the
     * TSP problem.
     *
     * At the moment the data that is processed by the method does not correspond to latitude
     * and longitude coordinates. Hence, there will be an explanation  in the Result section of
     * the dissertation of how that could be achieved.
     *
     * @param cities
     *
     * @return
     */

    public double distanceBetweenLatLng(City cities){
        double lat1 = Math.toRadians(this.posX.latitude);
        double lng1 = Math.toRadians(this.posY.longitude);
        double lat2 = Math.toRadians(cities.getPosX().latitude);
        double lng2 = Math.toRadians(cities.getPosY().longitude);

        // Haversine Formula (Distance of points on sphere)
        double Radius = 6371; // radius of earth in km

        double dLat = lat2 - lat1;
        double dLng = lng2 - lng1;
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLng / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return Radius * c;
    }


    /**
     * Get x position of city
     *
     * @return x X position of city
     */
    public LatLng getPosX() {
        return this.posX;
    }

    public LatLng getPosY() {
        return this.posY;
    }

}
