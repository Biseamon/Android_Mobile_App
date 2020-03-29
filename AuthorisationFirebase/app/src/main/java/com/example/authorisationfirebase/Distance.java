package com.example.authorisationfirebase;

public class Distance {

    /**
     * A simple abstraction of a city. This class maintains Cartesian coordinates
     * and also knows the Pythagorean theorem.
     *
     * @author bkanber
     *
     */
        private double xAndY;

        /**
         * Initalize a city
         *
         *
         */
        public Distance(double xAndY) {
            this.xAndY = xAndY;

        }


        public double distances(Distance distance){

            this.xAndY = distance.getXandY();


//            double one = this.xAndY;
//            double two = distance.getXandY();
//            double minValue = 0;
//
//            List<Double> list = new ArrayList<>();
//            list.add(one);
//            list.add(two);


            //double minValue = getMin(list);


           // return minValue;


            //this.xAndY = distance.getXandY();

            double xAndY = distance.getXandY();

            return xAndY;
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


}
