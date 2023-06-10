package com.bka.gpstracker.util;

import com.bka.gpstracker.model.request.NewTripRequest;
import com.bka.gpstracker.solr.entity.CurrentPosition;
import com.bka.gpstracker.solr.entity.Trip;

public class Utils {

    public static boolean isMatch(CurrentPosition currentPosition, Trip trip) {
        double lat1 = Double.valueOf(trip.getFromLat());
        double lon1 = Double.valueOf(trip.getFromLon());
        double lat2 = Double.valueOf(currentPosition.getLat());
        double lon2 = Double.valueOf(currentPosition.getLon());

        return distance(lat1, lat2, lon1, lon2) < 2000;
    }
    public static double distance(double lat1, double lat2, double lon1,
                                  double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }
}
