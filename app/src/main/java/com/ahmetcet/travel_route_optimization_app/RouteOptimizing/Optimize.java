package com.ahmetcet.travel_route_optimization_app.RouteOptimizing;

import android.content.Context;
import android.location.Location;

import com.ahmetcet.travel_route_optimization_app.LocalData.PrefManager;
import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.PointWithConstraints;
import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.Route;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.UUID;

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

    private ArrayList<PointWithConstraints> OrderPointsByCurrentLocation(ArrayList<PointWithConstraints> points, LatLng currentLocation){
        ArrayList<PointWithConstraints> orderedPoints = new ArrayList<>();

        for (PointWithConstraints point:
             points) {
            point.setOrder(0);
        }


        getNearestPointToDest(currentLocation,points).setOrder(1);
        int i = 2;
        for (PointWithConstraints point:
                points) {
            point.setOrder(i);
            orderedPoints.add(point);
            i++;
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

    private PointWithConstraints getNearestPointToDest(LatLng dest, ArrayList<PointWithConstraints> pointList){
        float minDist = Float.MAX_VALUE;
        PointWithConstraints temp_point = new PointWithConstraints();
        for (PointWithConstraints point:
                pointList) {
            float dist = getDistanceBetweenLocations(dest,point.getPointLocation());
            if(dist< minDist){
                minDist = dist;
                temp_point = point;
            }
        }

        return temp_point;

    }
}
