package com.ahmetcet.travel_route_optimization_app.LocalData;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.PointWithConstraints;
import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.Route;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.ArrayList;

public class SQLiteDataProvider extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TRO_LOCAL_DATA";

    //region PointsTable
    protected static final String table_Points = "tb_Points";
    protected static final String column_P_RouteId = "ROUTE_ID";
    protected static final String column_P_PointId = "POINT_ID";
    protected static final String column_P_PointName = "POINT_NAME";
    protected static final String column_P_Order = "POINT_ORDER";
    protected static final String column_P_PointLocation = "POINT_LOCATION";
    protected static final String column_P_EarliestTime = "EARLIEST_TIME";
    protected static final String column_P_LatestTime = "LATEST_TIME";
    protected static final String column_P_Priority = "PRORITY";
    protected static final String column_P_PreviousPointId = "PREVIOUS_POINT_ID";
    //endregion

    //region Routes

    protected static final String table_Routes = "tb_Routes";
    protected static final String column_R_RouteId = "ROUTE_ID";
    protected static final String column_R_RouteName = "ROUTE_NAME";
    protected static final String column_R_UserId = "USER_ID";
    protected static final String column_R_RouteDate = "ROUTE_DATE";
    protected static final String column_R_TravelType = "ROUTE_TRAVEL_TYPE";

    //endregion



    public SQLiteDataProvider(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try{
            String query_create_TABLE_tbPOINTS = "CREATE TABLE " + table_Points
                    + "(" + column_P_RouteId + " TEXT,"
                    + column_P_PointId + " TEXT,"
                    + column_P_PointName + " TEXT,"
                    + column_P_Order + " INT,"
                    + column_P_PointLocation + " TEXT,"
                    + column_P_EarliestTime + " TEXT,"
                    + column_P_LatestTime + " TEXT,"
                    + column_P_Priority + " INT,"
                    + column_P_PreviousPointId + " TEXT)";

            db.execSQL(query_create_TABLE_tbPOINTS);

        }catch (Exception ex){
        }

        try{
            String query_create_TABLE_tbPOINTS = "CREATE TABLE " + table_Routes
                    + "(" + column_R_RouteId + " TEXT,"
                    + column_R_UserId + " TEXT,"
                    + column_R_RouteName + " TEXT,"
                    + column_R_RouteDate + " TEXT,"
                    + column_R_TravelType + " INT)";

            db.execSQL(query_create_TABLE_tbPOINTS);

        }catch (Exception ex){
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + table_Points);
            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            db.execSQL("DROP TABLE IF EXISTS " + table_Routes);
            onCreate(db);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteRouteData(String routeID){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "DELETE FROM " + table_Points + " WHERE " + column_P_RouteId + " IS " + routeID;
            db.execSQL(query);
            db.close();
            return true;

        }catch (Exception e){
            return false;
        }
    }

    public boolean insertRoute(Route route) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            Gson gson = new Gson();
            if(route != null){
                ContentValues values = new ContentValues();
                values.put(column_R_RouteId, route.getRouteId());
                values.put(column_R_UserId, route.getUserId());
                values.put(column_R_RouteName, route.getRouteName());
                values.put(column_R_RouteDate, route.getRouteDate());
                values.put(column_R_TravelType, route.getTravelType());
                db.insert(table_Routes, null, values);
            }
            for (PointWithConstraints point:
                 route.getPointList()) {
                ContentValues values = new ContentValues();
                values.put(column_P_RouteId, point.getRouteId());
                values.put(column_P_PointId, point.getPointId());
                values.put(column_P_PointName, point.getPointName());
                values.put(column_P_Order, point.getOrder());
                values.put(column_P_PointLocation, gson.toJson(point.getPointLocation()));
                values.put(column_P_EarliestTime, point.getEarliestTime());
                values.put(column_P_LatestTime, point.getLatestTime());
                values.put(column_P_Priority, point.getPriority());
                values.put(column_P_PreviousPointId, point.getPreviousPointId());
                db.insert(table_Points, null, values);
            }
            db.close();
            return true;
        } catch (SQLException e) {
            e.toString();
            return false;
        }
    }

    public ArrayList<PointWithConstraints> getPointListByRouteId(Context context, String routeID) throws SQLException {
        ArrayList<PointWithConstraints> resultList = new ArrayList<>();
        Gson gson = new Gson();
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            String query = "SELECT * FROM " + table_Points + " WHERE " + column_P_RouteId + " IS " + routeID;

            Cursor cursor = db.rawQuery(query,null);
            int routeIDOrdinal = cursor.getColumnIndex(column_P_RouteId);
            int pointIDOrdinal = cursor.getColumnIndex(column_P_PointId);
            int pointNameOrdinal = cursor.getColumnIndex(column_P_PointName);
            int pointLocationOrdinal = cursor.getColumnIndex(column_P_PointLocation);
            int earliestTimeOrdinal = cursor.getColumnIndex(column_P_EarliestTime);
            int latestTimeOrdinal = cursor.getColumnIndex(column_P_LatestTime);
            int priorityOrdinal = cursor.getColumnIndex(column_P_Priority);
            int orderOrdinal = cursor.getColumnIndex(column_P_Order);
            int previousPointIdOrdinal = cursor.getColumnIndex(column_P_PreviousPointId);

            while (cursor.moveToNext()){
                String routeId = cursor.getString(routeIDOrdinal);
                String pointId = cursor.getString(pointIDOrdinal);
                String pointName = cursor.getString(pointNameOrdinal);
                String pointLocation = cursor.getString(pointLocationOrdinal);
                String earliestTime = cursor.getString(earliestTimeOrdinal);
                String latestTime = cursor.getString(latestTimeOrdinal);
                int priority = cursor.getInt(priorityOrdinal);
                int order = cursor.getInt(orderOrdinal);
                String previousPointId = cursor.getString(previousPointIdOrdinal);
                PointWithConstraints point = new PointWithConstraints();
                point.setRouteId(routeId);
                point.setPointId(pointId);
                point.setPointName(pointName);
                point.setPointLocation(gson.fromJson(pointLocation, LatLng.class));
                point.setPriority(priority);
                point.setOrder(order);
                point.setEarliestTime(earliestTime);
                point.setLatestTime(latestTime);
                point.setPreviousPointId(previousPointId);

                resultList.add(point);
            }
            cursor.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return resultList;
    }

    public ArrayList<Route> getAllRoutes() throws SQLException {
        ArrayList<Route> resultList = new ArrayList<>();
        Gson gson = new Gson();
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            String query = "SELECT *  FROM " + table_Routes ;

            Cursor cursor = db.rawQuery(query,null);
            int routeIDOrdinal = cursor.getColumnIndex(column_R_RouteId);
            int userIDOrdinal = cursor.getColumnIndex(column_R_UserId);
            int routeNameOrdinal = cursor.getColumnIndex(column_R_RouteName);
            int routeDateOrdinal = cursor.getColumnIndex(column_R_RouteDate);
            int travelTypeOrdinal = cursor.getColumnIndex(column_R_TravelType);


            while (cursor.moveToNext()){
                String routeId = cursor.getString(routeIDOrdinal);
                String userId = cursor.getString(userIDOrdinal);
                String routeName = cursor.getString(routeNameOrdinal);
                String routeDate = cursor.getString(routeDateOrdinal);
                int travelType = cursor.getInt(travelTypeOrdinal);
                Route route = new Route();
                route.setRouteId(routeId);
                route.setUserId(userId);
                route.setRouteName(routeName);
                route.setRouteDate(routeDate);
                route.setTravelType(travelType);

                resultList.add(route);
            }
            cursor.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return resultList;
    }


    public boolean UpdatePhotoStatus (String data) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();


            ContentValues cv = new ContentValues();
            cv.put(column_P_Order,data);

            db.update(table_Points,cv,column_P_EarliestTime + "=\"" + data + "\"", null);

            db.close();
        }
        catch (SQLException e) {
            return  false;
        }
        return true;
    }


}
