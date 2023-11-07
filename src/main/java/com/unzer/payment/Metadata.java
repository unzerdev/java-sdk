package com.unzer.payment;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class Metadata extends BaseResource {
    private String id;
    private Map<String, String> metadataMap;
    private Unzer unzer;

    public Metadata() {
        this(false);
    }

    public Metadata(boolean sorted) {
        if (sorted) {
            metadataMap = new TreeMap<String, String>();
        } else {
            metadataMap = new LinkedHashMap<String, String>();
        }
    }

    public Metadata addMetadata(String key, String value) {
        getMetadataMap().put(key, value);
        return this;
    }

    public Map<String, String> getMetadataMap() {
        return metadataMap;
    }

    public void setMetadataMap(Map<String, String> metadataMap) {
        this.metadataMap = metadataMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getResourceUrl() {
        return "/v1/metadata/<resourceId>";
    }

    @Deprecated
    public Unzer getUnzer() {
        return unzer;
    }

    @Deprecated
    public void setUnzer(Unzer unzer) {
        this.unzer = unzer;
    }
}
