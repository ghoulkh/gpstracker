package com.bka.gpstracker.common;

public final class TripStatus {
    public static final String NEW = "NEW";
    public static final String IN_PROGRESS = "IN_PROGRESS";
    public static final String COMPLECTED = "COMPLECTED";
    public static final String CANCELED = "CANCELED";

    public static boolean isMatch(String status) {
        if (NEW.equals(status) || IN_PROGRESS.equals(status) || COMPLECTED.equals(status) || CANCELED.equals(status))
            return true;
        else
            return false;
    }
}
