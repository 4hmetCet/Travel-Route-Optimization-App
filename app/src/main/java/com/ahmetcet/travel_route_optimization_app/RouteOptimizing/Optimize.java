package com.ahmetcet.travel_route_optimization_app.RouteOptimizing;

import android.content.Context;

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
        int i = 1;
        for (PointWithConstraints point:
                points) {
            point.setOrder(i);
            orderedPoints.add(point);
            i++;
        }

        return orderedPoints;
    }
}
