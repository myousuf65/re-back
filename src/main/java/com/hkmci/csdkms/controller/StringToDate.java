package com.hkmci.csdkms.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StringToDate {
	
    public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    
    private static final Object LOCK = new Object();

    // OK
    public Date parseString(String datetime) throws Exception {
        synchronized (LOCK) {
            return format.parse(datetime);
        }
    }

    // OK
    public Date parseStringV2(String datetime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse(datetime);
    }
}