package com.hkmci.csdkms.wowza;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class WowzaTokenGenerator {
	
    @SuppressWarnings("unused")
	public static String gen(String streamName, String host, String secret, String tokenName, String clientIP) {
    	String url = host;
        String stream = "vod-key2/_definst/"+streamName;
        long start = System.currentTimeMillis() / 1000;
        long end = start + 30; // 30 minutes
        
        String hashString = "";
        if (clientIP == null || clientIP == "") {
            hashString = stream + "?" + secret + "&" + tokenName + "endtime=" + end + "&" + tokenName + "starttime=" + start;
        } else {
            hashString = stream + "?" + secret + "&" + clientIP + "&" + tokenName + "endtime=" + end + "&" + tokenName + "starttime=" + start;
        }
        System.out.println("hashString:"+hashString);
        byte[] hashBytes = calculateSHA256Hash(hashString);
        String base64Hash = base64UrlEncode(hashBytes);
        
        List<String> params = new ArrayList<>();
        
        params.add(tokenName + "starttime=" + start);
        params.add(tokenName + "endtime=" + end);
        params.add(tokenName + "hash=" + base64Hash);
        if (clientIP != null) {
            //params.add(clientIP);
        }
        
        //Collections.sort(params);
        
        StringBuilder playbackUrlBuilder = new StringBuilder();
        playbackUrlBuilder.append(url).append(stream).append("/playlist.m3u8?");
        
        if (url.matches(".*(rtmp).*")) {
            playbackUrlBuilder = new StringBuilder(url).append(stream).append("?");
        }
        
        for (String entry : params) {
            playbackUrlBuilder.append(entry).append("&");
        }
        
        String playbackURL = playbackUrlBuilder.toString().replaceAll("(\\&)$", "");
        return playbackURL+"=";
    	
        
    }
    
    private static byte[] calculateSHA256Hash(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(data.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private static String base64UrlEncode(byte[] input) {
        String base64 = Base64.getUrlEncoder().withoutPadding().encodeToString(input);
        return base64.replace("+", "-").replace("/", "_");
    }
}