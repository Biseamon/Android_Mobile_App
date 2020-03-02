package com.example.authorisationfirebase;

import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class ClassTesting {

    @Test
    public void distanceClassTesting(){
        Result result = JUnitCore.runClasses(Distance.class);
        for(Failure failure : result.getFailures()){
            System.out.println(failure.toString());
        }
    }

    @Test
    public void downloadUrlClassTesting(){
        Result result = JUnitCore.runClasses(DownloadUrl.class);
        for(Failure failure : result.getFailures()){
            System.out.println(failure.toString());
        }
    }

    @Test
    public void durationClassTesting(){
        Result result = JUnitCore.runClasses(Duration.class);
        for(Failure failure : result.getFailures()){
            System.out.println(failure.toString());
        }
    }

    @Test
    public void geneticAlgorithmClassTesting(){
        Result result = JUnitCore.runClasses(GeneticAlgorithm.class);
        for(Failure failure : result.getFailures()){
            System.out.println(failure.toString());
        }
    }

    @Test
    public void getDirectionsDataClassTesting(){
        Result result = JUnitCore.runClasses(GetDirectionsData.class);
        for(Failure failure : result.getFailures()){
            System.out.println(failure.toString());
        }
    }

    @Test
    public void getDistanceMatrixClassTesting(){
        Result result = JUnitCore.runClasses(GetDistanceMatrixData.class);
        for(Failure failure : result.getFailures()){
            System.out.println(failure.toString());
        }
    }

    @Test
    public void getNearbyPlacesClassTesting(){
        Result result = JUnitCore.runClasses(GetNearbyPlaces.class);
        for(Failure failure : result.getFailures()){
            System.out.println(failure.toString());
        }
    }

    @Test
    public void individualClassTesting(){
        Result result = JUnitCore.runClasses(Individual.class);
        for(Failure failure : result.getFailures()){
            System.out.println(failure.toString());
        }
    }

    @Test
    public void loginClassTesting(){
        Result result = JUnitCore.runClasses(Login.class);
        for(Failure failure : result.getFailures()){
            System.out.println(failure.toString());
        }
    }

    @Test
    public void mapsActivityClassTesting(){
        Result result = JUnitCore.runClasses(MapsActivity.class);
        for(Failure failure : result.getFailures()){
            System.out.println(failure.toString());
        }
    }

    @Test
    public void parserClassTesting(){
        Result result = JUnitCore.runClasses(Parser.class);
        for(Failure failure : result.getFailures()){
            System.out.println(failure.toString());
        }
    }

    @Test
    public void populationClassTesting(){
        Result result = JUnitCore.runClasses(Population.class);
        for(Failure failure : result.getFailures()){
            System.out.println(failure.toString());
        }
    }

    @Test
    public void preferencesClassTesting(){
        Result result = JUnitCore.runClasses(Preferences.class);
        for(Failure failure : result.getFailures()){
            System.out.println(failure.toString());
        }
    }

    @Test
    public void registerClassTesting(){
        Result result = JUnitCore.runClasses(Register.class);
        for(Failure failure : result.getFailures()){
            System.out.println(failure.toString());
        }
    }

    @Test
    public void routeClassTesting(){
        Result result = JUnitCore.runClasses(Route.class);
        for(Failure failure : result.getFailures()){
            System.out.println(failure.toString());
        }
    }

}
