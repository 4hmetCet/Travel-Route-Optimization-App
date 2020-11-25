package com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model;

import com.google.android.gms.maps.model.LatLng;

public class PointWithConstraints {
    private String pointId;
    private String pointName;
    private LatLng pointLocation;
    private String earliestTime;
    private String latestTime;
    private int priority;
    private String previousPointId;


    public void setEarliestTime(String earliestTime) {
        this.earliestTime = earliestTime;
    }

    public void setLatestTime(String latestTime) {
        this.latestTime = latestTime;
    }

    public void setPointId(String pointId) {
        this.pointId = pointId;
    }

    public void setPointLocation(LatLng pointLocation) {
        this.pointLocation = pointLocation;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public void setPreviousPointId(String previousPointId) {
        this.previousPointId = previousPointId;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getEarliestTime() {
        return earliestTime;
    }

    public int getPriority() {
        return priority;
    }

    public LatLng getPointLocation() {
        return pointLocation;
    }

    public String getLatestTime() {
        return latestTime;
    }

    public String getPointId() {
        return pointId;
    }

    public String getPointName() {
        return pointName;
    }

    public String getPreviousPointId() {
        return previousPointId;
    }
}
