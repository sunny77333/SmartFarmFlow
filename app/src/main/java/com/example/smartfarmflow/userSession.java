package com.example.smartfarmflow;

public class userSession {
    private static userSession instance;
    private String userId;

    // Private constructor to prevent instantiation
    private userSession() {}

    // Get the instance of UserSession
    public static synchronized userSession getInstance() {
        if (instance == null) {
            instance = new userSession();
        }
        return instance;
    }

    // Set the userId
    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Get the userId
    public String getUserId() {
        return userId;
    }

    // Clear user session
    public void clearSession() {
        userId = null;
    }
}
