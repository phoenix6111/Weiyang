package com.greendao;

import java.io.UnsupportedEncodingException;

/**
 * Created by sheng on 2016/6/6.
 */
public class Test {
    public static final String split = "|";

    public static void main(String[] args) throws UnsupportedEncodingException {
        double distance = GetDistance(22.689195,114.37297,22.694985,114.39357);
        System.out.print(distance);
    }

    public static double EARTH_RADIUS = 6378.137;//地球半径
    private static double rad(double d)
    {
        return d * Math.PI / 180.0;
    }

    public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
    {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
                Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }
}
