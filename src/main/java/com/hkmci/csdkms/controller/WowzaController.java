package com.hkmci.csdkms.controller;

import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.hkmci.csdkms.wowza.WowzaFullLink;
import com.hkmci.csdkms.wowza.WowzaTokenGenerator;

@CrossOrigin
@RestController
@RequestMapping("/wowza")
public class WowzaController {
	
	@Value("${wowza.host}")
    private String host;
	
	@Value("${wowza.sharedSecret}")
    private String secret;
	
	@Value("${wowza.prefixtoken}")
    private String tokenName;
	
	@Value("${wowza.clientIP}")
	private String clientIP;
    
    @RequestMapping("/getFullLinkToken/{streamName}")
    public ResponseEntity<JsonResult> getStreamTokenUrl(@RequestBody JsonNode jsonNode,@PathVariable String streamName, HttpServletRequest request, HttpSession session) {
        String videoPath = jsonNode.get("media_url").asText();
        clientIP = null;

        String url = WowzaTokenGenerator.gen(videoPath, host, secret, tokenName, clientIP);
        return JsonResult.fileList("Post Found Successfully", url,null,session);
    }
    
    @RequestMapping("/getFullLinkToken_given/{streamName}")
    public ResponseEntity<JsonResult> getStreamTokenUrlGiven(@RequestBody JsonNode jsonNode,@PathVariable String streamName, HttpServletRequest request, HttpSession session) {
        String videoPath = "wowza.mp4";
        clientIP = null;
        String url = WowzaTokenGenerator.gen(videoPath, host, secret, tokenName, clientIP);
        return JsonResult.fileList("Post Found Successfully", url,null,session);
    }
    
	@RequestMapping("/getFullLink/{userId}")
	public ResponseEntity<JsonResult> getFullLink(@RequestBody JsonNode jsonNode,@PathVariable Long userId, HttpServletRequest request, HttpSession session) {
		WowzaFullLink wowzaToken = new WowzaFullLink();
        
        try {
        	wowzaToken.setURL(jsonNode.get("media_url").asText());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        
        long startTime = System.currentTimeMillis() / 1000;
        long endTime = startTime + (3 * 60 * 60); // 3 hours

        Map<String, String> params = new HashMap<>();
        params.put("endtime", String.valueOf(endTime));
        params.put("starttime", String.valueOf(startTime));
        params.put("CustomParam1", "CustomValue");

        wowzaToken.setExtraParams(params);

        String url = wowzaToken.getFullURL();
        
		return JsonResult.fileList("Post Found Successfully", url,null,session);
	}
	
	private String getClientIP(HttpServletRequest request) {
	    String ipAddress = request.getHeader("X-Forwarded-For");
	    if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
	        ipAddress = request.getHeader("Proxy-Client-IP");
	    }
	    if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
	        ipAddress = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
	        ipAddress = request.getHeader("HTTP_CLIENT_IP");
	    }
	    if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
	        ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
	    }
	    if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
	        ipAddress = request.getRemoteAddr();
	    }
	    return ipAddress;
	}

}
