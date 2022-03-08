package com.abim.lks_hotel_4_mobile;

public class MyRequest {
    private static final String baseURL = "http://192.168.60.22:666/";
    private static final String loginURL = "api/employee";
    private static final String fdURL = "api/fd";
    private static final String roomURL = "api/room";
    private static final String checkOutURL = "api/fdCheckout";

    public static String getBaseURL() {
        return baseURL;
    }

    public static String getLoginURL() {
        return getBaseURL() + loginURL;
    }

    public static String getFdURL() {
        return getBaseURL() + fdURL;
    }

    public static String getRoomURL() {
        return getBaseURL() + roomURL;
    }

    public static String getCheckOutURL() {
        return getBaseURL() + checkOutURL;
    }
}
