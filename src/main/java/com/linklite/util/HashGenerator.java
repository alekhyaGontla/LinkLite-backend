package com.linklite.util;

import java.util.UUID;

public class HashGenerator {
	 public static String generateShortCode() {

	        return UUID.randomUUID()
	                .toString()
	                .replace("-", "")
	                .substring(0, 6);

	    }
}
