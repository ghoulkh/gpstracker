package com.bka.gpstracker.util;

import com.bka.gpstracker.common.DriverStatus;
import com.bka.gpstracker.model.request.NewTripRequest;
import com.bka.gpstracker.solr.entity.CurrentPosition;
import com.bka.gpstracker.solr.entity.DeliveryInfo;
import com.bka.gpstracker.solr.entity.Trip;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.beanutils.BeanUtilsBean;

import java.text.Normalizer;
import java.util.Date;

@Log4j2
public class Utils {

    public static boolean isMatch(CurrentPosition currentPosition, Trip trip) {
        double lat1 = Double.valueOf(trip.getFromLat());
        double lon1 = Double.valueOf(trip.getFromLon());
        double lat2 = Double.valueOf(currentPosition.getLat());
        double lon2 = Double.valueOf(currentPosition.getLon());

        return distance(lat1, lat2, lon1, lon2) < 2000;
    }

    public static String geneDataSearchDelivery(DeliveryInfo deliveryInfo) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(deliveryInfo.getId().toLowerCase())
                .append(" ");
        if (deliveryInfo.getFromAddress() != null) {
            stringBuilder.append(deliveryInfo.getFromAddress().toLowerCase())
                    .append(" ");
        }
        if (deliveryInfo.getToAddress() != null)
            stringBuilder.append(deliveryInfo.getToAddress().toLowerCase())
                            .append(" ");
        if (deliveryInfo.getFullNameReceiver() != null)
            stringBuilder.append(deliveryInfo.getFullNameReceiver().toLowerCase())
                            .append(" ");
        if (deliveryInfo.getEmailReceiver() != null)
            stringBuilder.append(deliveryInfo.getEmailReceiver().toLowerCase())
                            .append(" ");
        if (deliveryInfo.getPhoneNumberReceiver() != null)
            stringBuilder.append(deliveryInfo.getPhoneNumberReceiver().toLowerCase());
        return stringBuilder.toString();
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

    public static void copyPropertiesNotNull(Object source, Object target) {
        try {
            BeanUtilsBean notNull=new NullAwareBeanUtilsBean();
            notNull.copyProperties(target, source);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public static String toEn(String input) {
        if (input == null) return null;
        return Normalizer
                .normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replace("Đ", "D")
                .replace("đ", "d");
    }


    public static String getStatusFromLastCheckIn(Date lastCheckInAt) {
        if (lastCheckInAt == null) {
            return DriverStatus.INACTIVE;
        }
        Date sixHoursAgo = new Date(System.currentTimeMillis() - 3600 * 1000 * 4);
        if (lastCheckInAt.before(sixHoursAgo)) {
            return DriverStatus.INACTIVE;
        }
        return DriverStatus.ACTIVE;
    }
}
