package com.ahmetcet.travel_route_optimization_app.RouteOptimizing;

import android.content.Context;

import com.ahmetcet.travel_route_optimization_app.LocalData.PrefManager;
import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.PointWithConstraints;
import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.Route;

import java.util.ArrayList;
import java.util.UUID;

public class Optimize {
    Context context;

    public Optimize(Context c){
        context=c;
    }

    public Route GetOptimizedRoute(String routeId, ArrayList<PointWithConstraints> points){
        Route resultRoute = new Route();
        String userId = PrefManager.getPref_UserInfo(PrefManager.key_email,context);
        resultRoute.setRouteId(routeId);
        resultRoute.setUserId(userId);


        return  resultRoute;
    }

    private ArrayList<PointWithConstraints> OrderPoints(ArrayList<PointWithConstraints> points){
        ArrayList<PointWithConstraints> orderedPoints = new ArrayList<>();


        return orderedPoints;
    }
}
