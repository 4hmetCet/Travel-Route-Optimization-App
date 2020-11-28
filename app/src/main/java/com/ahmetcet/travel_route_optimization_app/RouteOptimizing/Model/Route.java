package com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model;

import java.util.ArrayList;

public class Route {
    private String routeId;
    private String userId;
    private ArrayList<PointWithConstraints> pointList;

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setPointList(ArrayList<PointWithConstraints> pointList) {
        this.pointList = pointList;
    }

    public ArrayList<PointWithConstraints> getPointList() {
        return pointList;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
