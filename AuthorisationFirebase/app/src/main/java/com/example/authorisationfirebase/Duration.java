package com.example.authorisationfirebase;


    public class Duration {

        /**
         * A simple abstraction of a city. This class maintains Cartesian coordinates
         * and also knows the Pythagorean theorem.
         *
         * @author bkanber
         *
         */
        private double xAndY;
       // private double y;

        /**
         * Initalize a city
         *
         */
        public Duration(double xAndY) {
            this.xAndY = xAndY;
        }

        public double durations(Duration duration){

            double xAndY = duration.getX();

            return xAndY;
        }

        /**
         * Calculate distance from another city
         *
         * Pythagorean theorem: a^2 + b^2 = c^2
         *
         * @param duration
         *            The city to calculate the distance from
         * @return distance The distance from the given city
         */
//        public double durationFrom(Duration duration) {
//            // Give difference in x,y
//            double deltaXSq = Math.pow((duration.getX() - this.getX()), 2);
//            double deltaYSq = Math.pow((duration.getY() - this.getY()), 2);
//
//            // Calculate shortest path
//            double durations = Math.sqrt(Math.abs(deltaXSq + deltaYSq));
//            return durations;
//        }

        /**
         * Get x position of city
         *
         * @return x X position of city
         */
        public double getX() {
            return this.xAndY;
        }

        /**
         * Get y position of city
         *
         * @return y Y position of city
         */
//        public double getY() {
//            return this.y;
//        }
    }
