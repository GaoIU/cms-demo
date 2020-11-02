package com.gaoc.core.util;

public class SessionUtil {

	private static ThreadLocal<String> USER_ID = new ThreadLocal<>();

	public static void setUserId(String userId) {
		USER_ID.set(userId);
	}

	public static String getUserId() {
		return USER_ID.get();
	}

	private SessionUtil() {
	}

}
