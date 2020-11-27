package com.ahmetcet.travel_route_optimization_app.LocalData;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ahmetcet.travel_route_optimization_app.RouteOptimizing.Model.PointWithConstraints;

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
                    + column_P_Order + " TEXT,"
                    + column_P_PointLocation + " REAL,"
                    + column_P_EarliestTime + " REAL,"
                    + column_P_Priority + " TEXT)";

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
    }

    public void deleteData(){
        try{
            SQLiteDatabase db = this.getWritableDatabase();
            String query = "DELETE FROM " + table_Points;
            db.execSQL(query);
            db.close();

        }catch (Exception e){
        }
    }

    public void insertPoint(PointWithConstraints pointWithConstraints) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(column_P_RouteId, "");
            db.insert(table_Points, null, values);
            db.close();
        } catch (SQLException e) {
            e.toString();
        }
    }

    public ArrayList<PointWithConstraints> getData(Context context, double lat, double lng) {
        ArrayList<PointWithConstraints> resultList = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();

            String query = "SELECT *  FROM " + table_Points;

            Cursor cursor = db.rawQuery(query,null);
            int typeUIDOrdinal = cursor.getColumnIndex(column_P_RouteId);
            int typeCodeOrdinal = cursor.getColumnIndex(column_P_PointId);
            int typeNameOrdinal = cursor.getColumnIndex(column_P_PointName);
            int latOrdinal = cursor.getColumnIndex(column_P_PointLocation);
            int longOrdinal = cursor.getColumnIndex(column_P_EarliestTime);
            int updateDateOrdinal = cursor.getColumnIndex(column_P_Priority);
            while (cursor.moveToNext()){
                String typeUid = cursor.getString(typeUIDOrdinal);
                String typeCode = cursor.getString(typeCodeOrdinal);
                String typeName = cursor.getString(typeNameOrdinal);
                float latitude = cursor.getFloat(latOrdinal);
                float longitude = cursor.getFloat(longOrdinal);
                String updateDate = cursor.getString(updateDateOrdinal);


            }
            cursor.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
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

    public int getData(){
        int result= 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String query = "SELECT *  FROM " + table_Points ;

            Cursor cursor = db.rawQuery(query,null);
            result = cursor.getCount();
            cursor.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return result;
        }
        return result;
    }


}
