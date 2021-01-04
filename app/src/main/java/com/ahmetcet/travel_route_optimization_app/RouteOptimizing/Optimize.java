package com.ahmetcet.travel_route_optimization_app.RouteOptimizing;

import android.content.Context;
import android.location.Location;

import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.PointWithConstraints;
import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.Route;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class Optimize {
    private Context context;
    private Route _route;

    public Optimize(Context c, Route route){
        context=c;
        _route = route;
    }

    public Route GetOptimizedRoute(LatLng currentLocation){
        if(currentLocation == null){
            ArrayList<PointWithConstraints> orderedPointList = OrderPoints(_route.getPointList());
            _route.setPointList(orderedPointList);
        }else{
            ArrayList<PointWithConstraints> orderedPointList = OrderPointsByCurrentLocation(_route.getPointList(),currentLocation);
            _route.setPointList(orderedPointList);
        }

        return  _route;
    }

    private ArrayList<PointWithConstraints> OrderPoints(ArrayList<PointWithConstraints> points){
        ArrayList<PointWithConstraints> orderedPoints = new ArrayList<>();
        int i = 1;
        for (PointWithConstraints point:
             points) {
            point.setOrder(i);
            orderedPoints.add(point);
            i++;
        }

        return orderedPoints;
    }

    public ArrayList<PointWithConstraints> OrderPointsByCurrentLocation(ArrayList<PointWithConstraints> points, LatLng currentLocation){
        ArrayList<PointWithConstraints> orderedPoints = new ArrayList<>();

        for (PointWithConstraints point:
             points) {
            point.setOrder(0);
        }

        PointWithConstraints tempPoint =  getUnvisitedNearestPointToDest(currentLocation,points);
        tempPoint.setOrder(1);
        orderedPoints.add(tempPoint);
        int i = 2;

        for(int k = 0; k< points.size(); k++){
            tempPoint = getUnvisitedNearestPointToDest(tempPoint.getPointLocation(),points);
            if(tempPoint == null)
                break;
            tempPoint.setOrder(i);
            orderedPoints.add(tempPoint);
            i ++;
        }

        return orderedPoints;
    }

    private float getDistanceBetweenLocations(LatLng loc1, LatLng loc2){
        Location locationA = new Location("point A");

        locationA.setLatitude(loc1.latitude);
        locationA.setLongitude(loc1.longitude);

        Location locationB = new Location("point B");

        locationB.setLatitude(loc2.latitude);
        locationB.setLongitude(loc2.longitude);

        float distance = locationA.distanceTo(locationB);
        return distance;
    }

    private boolean isTimeIntervalSuitable(PointWithConstraints targetPoint){

        return true;
    }

    private PointWithConstraints getUnvisitedNearestPointToDest(LatLng dest, ArrayList<PointWithConstraints> pointList){
        float minDist = 9999999999f;
        PointWithConstraints temp_point = null;
        for (PointWithConstraints point:
                pointList) {
            float dist = getDistanceBetweenLocations(dest,point.getPointLocation());
            if(dist< minDist && point.getOrder() == 0){
                minDist = dist;
                temp_point = point;
            }
        }

        return temp_point;

    }
}
