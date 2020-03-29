package com.example.authorisationfirebase;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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


            double one = this.xAndY;
            double two = duration.getXandY();

            List<Double> list = new ArrayList<>();
            list.add(one);
            list.add(two);

            double minValue = getMin(list);

            return minValue;

//            double firstDuration = this.xAndY;
//            double secondDuration = duration.getXandY();
//            double smallestDistance = Double.MAX_VALUE;
//
//            List<Double> list = new ArrayList<>();
//            list.add(firstDuration);
//            list.add(secondDuration);
//
//            for (int i = 0; i < list.size(); i++) {
//                if(list.get(i) < smallestDistance){
//                    smallestDistance = list.get(i);
//                }
//            }
//
//            return smallestDistance;

//            double xAndY = duration.getX();
//
//            return xAndY;
        }

        /**
         * Calculate distance from another city
         *
         * Pythagorean theorem: a^2 + b^2 = c^2
         *
        // * @param duration
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


       public Double getMin(List<Double> list){
            return Collections.min(list);
        }
        /**
         * Get x position of city
         *
         * @return x X position of city
         */
        public double getXandY() {
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
