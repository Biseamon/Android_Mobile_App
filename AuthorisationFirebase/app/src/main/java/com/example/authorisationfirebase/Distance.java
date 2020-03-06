package com.example.authorisationfirebase;

public class Distance {

    /**
     * A simple abstraction of a city. This class maintains Cartesian coordinates
     * and also knows the Pythagorean theorem.
     *
     * @author bkanber
     *
     */
        private double x;
        private double y;

        /**
         * Initalize a city
         *
         * @param x
         *            X position of city
         * @param y
         *            Y position of city
         */
        public Distance(double x, double y) {
            this.x = x;
            this.y = y;
        }

        /**
         * Calculate distance from another city
         *
         * Pythagorean theorem: a^2 + b^2 = c^2
         *
         * @param distance
         *            The city to calculate the distance from
         * @return distance The distance from the given city
         */
//    public double distanceFrom(Distance distance) {
//        // Give difference in x,y
//        double deltaXSq = Math.pow((distance.getX() - this.getX()),2);
//        double deltaYSq = Math.pow((distance.getY() - this.getY()),2);
//
//        double distances = Math.sqrt(deltaXSq + deltaYSq);
//
//        return distances;
//
//    }


    /**
     * This method is needed in cases where the
     * distance between two latitudes and longitudes
     * must be calculated.
     * It uses a math formula called Haversine formula.
     *
     * In our case all we need to is we have to pass the latitudes and
     * longitudes of the places so the method can perform the calculus.
     *
     * Thereafter, the genetic algorithm will peroform in oreder to solve the
     * TSP problem.
     *
     * At the moment the data that is processed by the method does not correspond to latitude
     * and longitude coordinates. Hence, there will be an explanation  in the Result section of
     * the dissertation of how that could be achieved.
     *
      * @param distance
     * @return
     */
    public double distanceBetweenLatLng(Distance distance){
        double lat1 = Math.toRadians(this.getX());
        double lng1 = Math.toRadians(this.getY());
        double lat2 = Math.toRadians(distance.getX());
        double lng2 = Math.toRadians(distance.getY());

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
        public double getX() {
            return this.x;
        }

        /**
         * Get y position of city
         *
         * @return y Y position of city
         */
        public double getY() {
            return this.y;
        }

}
