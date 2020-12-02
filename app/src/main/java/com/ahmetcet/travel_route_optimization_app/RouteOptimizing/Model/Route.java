package com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model;

import java.util.ArrayList;

public class Route {
    private String routeId;
    private String userId;
    private ArrayList<PointWithConstraints> pointList;
    private String routeDate;

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

    public void setRouteDate(String routeDate) {
        this.routeDate = routeDate;
    }

    public String getRouteDate() {
        return routeDate;
    }
}
