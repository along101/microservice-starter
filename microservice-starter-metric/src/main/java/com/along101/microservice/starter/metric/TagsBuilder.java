package com.along101.microservice.starter.metric;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by yinzuolong on 2017/4/26.
 */
public class TagsBuilder {

    private Map<String, String> tags = new HashMap<>();

    private TagsBuilder() {

    }

    public static TagsBuilder create() {
        return new TagsBuilder();
    }

    public Map<String, String> build() {
        return this.tags;
    }

    public TagsBuilder put(String name, String value) {
        this.tags.put(name, value);
        return this;
    }

    public TagsBuilder putAll(Map<String, String> tags) {
        this.tags.putAll(tags);
        return this;
    }
}
