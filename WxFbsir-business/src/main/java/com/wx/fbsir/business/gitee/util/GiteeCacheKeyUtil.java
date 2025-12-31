package com.wx.fbsir.business.gitee.util;

public final class GiteeCacheKeyUtil {
    public static final String ACCESS_TOKEN_KEY_PREFIX = "gitee:access:token:";
    public static final String AUTH_STATE_KEY_PREFIX = "gitee:auth:state:";

    private GiteeCacheKeyUtil() {
    }

    public static String getAccessTokenKey(Long userId) {
        return ACCESS_TOKEN_KEY_PREFIX + userId;
    }

    public static String getAuthStateKey(String state) {
        return AUTH_STATE_KEY_PREFIX + state;
    }
}
