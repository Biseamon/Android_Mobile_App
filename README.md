# Android_Mobile_App
## Mobile App
The idea is to build an app that takes through a list of desired places using genetic algorithm.
The GA is intended to show the shortest path and time from one place to another.
The application is using following technologies and APIs:
1. Firebase - for storing data remotely and dynamically.
2. Android SDK - this allows to find the currecnt location of the device on the map.
2. Google Maps - to perform different visual effects on a map.
3. Places API - to search for nearby places based on the chosen preferences.
4. Directions API - to display routes between nearby places starting from the current location. Also, to solve the TSP.
5. Distance Matrix API - to extract all the distances and durations between origin place and destination.
6. Geocoding API - to convert origin and destinations street name extracted from Distance Matrix API to latitudes and longitudes needed for the Genetic Algorithm, which takes as input coordinates.

## User guide

### 1. Try to enter login and password.
![Image of Yaktocat](https://i.imgur.com/qAXmhVs.jpg)

### 2. If user don't have a login and password than the user must register first. Click on Create Account.
![Image of Yaktocat](https://i.imgur.com/q8ChxUK.jpg)

### 3. After succesfully entering the account details the user will be transferred to the Preferences page where is possible to choose what type of place to visit. Firstly, choose the place: Restaurants, Bars or ATMs. After that set the price range from 1 to 4. If user chooses ATMs then the price range must be set to null. After that click SAVE button.
![Image of Yaktocat](https://i.imgur.com/RjPnp83.jpg)

### 4. After saving the place to Firebase click on continue to open the map showing the device current location and the nearby places.
![Image of Yaktocat](https://i.imgur.com/bY6cz4G.jpg)

### 5. On clicking the Generate Routes button the Direction API will generate an optimised route between the current location and all the nearby places. It will solve the TSP and in the top left corner will be shown the distance, duration and the time taken processing the task in milliseconds.
![Image of Yaktocat](https://i.imgur.com/mePEHQx.jpg)

### 6. The GA button at the moment does not display anything on the UI, because it is not fixed yet. However, soon this functionality will be available. It will allow to find the shortest routes between nearby places using Distance Matrix API, Geocoding API, GA, and Haversine Formula that calculates the distance between two latitudes and longitudes.
