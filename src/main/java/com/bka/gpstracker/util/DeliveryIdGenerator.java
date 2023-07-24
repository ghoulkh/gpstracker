package com.bka.gpstracker.util;


public class DeliveryIdGenerator {

    private static String STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String geneId() {
        Long currentTime = System.currentTimeMillis();
        String currentTimeString = String.valueOf(currentTime);
        char[] chars = currentTimeString.toCharArray();
        StringBuilder builder = new StringBuilder();
        int stringLength = chars.length;
        int between = stringLength/2;
        builder.append(genePrefix()).append("-");
        for (int i = 0; i < stringLength; i ++) {
            builder.append(STRING
                    .charAt(Integer.parseInt(String.valueOf(currentTimeString.charAt(i)))));
            if (i == between)
                builder.append("-");
        }

        return builder.toString();
    }

    private static String genePrefix() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 5; i ++) {
            int index
                    = (int)(STRING.length()
                    * Math.random());
            builder.append(STRING.charAt(index));
        }
        return builder.toString();
    }
}
