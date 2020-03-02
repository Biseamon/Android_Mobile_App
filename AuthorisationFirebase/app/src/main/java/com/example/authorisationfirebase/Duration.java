package com.example.authorisationfirebase;


    public class Duration {

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
        public Duration(double x, double y) {
            this.x = x;
            this.y = y;
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
        public double durationFrom(Duration duration) {
            // Give difference in x,y
            double deltaXSq = Math.pow((duration.getX() - this.getX()), 2);
            double deltaYSq = Math.pow((duration.getY() - this.getY()), 2);

            // Calculate shortest path
            double durations = Math.sqrt(Math.abs(deltaXSq + deltaYSq));
            return durations;
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
