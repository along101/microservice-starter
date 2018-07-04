package com.along101.microservice.starter.raptor;

import com.dianping.cat.Cat;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yinzuolong
 */
public class PropertyContext implements Cat.Context {

    private Map<String, String> properties = new HashMap<>();

    @Override
    public void addProperty(String key, String value) {
        properties.put(key, value);
    }

    @Override
    public String getProperty(String key) {
        return properties.get(key);
    }
}