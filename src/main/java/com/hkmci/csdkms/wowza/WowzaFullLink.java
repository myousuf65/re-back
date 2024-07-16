package com.hkmci.csdkms.wowza;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class WowzaFullLink {

    private String url;
    private String urlPath;
    private Map<String, String> params = new HashMap<>();


    public void setURL(String url) throws URISyntaxException {
        this.url = url;
        URI uri = new URI(url);
        this.urlPath = uri.getPath();
    }

    public void setExtraParams(Map<String, String> params) {
        this.params = params;
    }


    public String getFullURL() {
        params.putAll(params);


        StringBuilder urlBuilder = new StringBuilder(url);
        if (!params.isEmpty()) {
            urlBuilder.append(url.contains("?") ? "&" : "?");
            params.forEach((key, value) -> urlBuilder.append(key).append("=").append(value).append("&"));
            urlBuilder.deleteCharAt(urlBuilder.length() - 1);
        }

        return urlBuilder.toString();
    }


}