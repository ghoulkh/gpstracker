package com.bka.gpstracker.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.reflect.TypeToken;

public final class JsonMapper {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (Exception e) {
            return "";
        }
    }

    public static <T> T toObject(String json, TypeReference<T> tTypeReference) {
        try {
            return mapper.readValue(json, tTypeReference);
        } catch (Exception e) {
            return null;
        }
    }
}
