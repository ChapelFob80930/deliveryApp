package com.example.loginsignup;

public class userSession {
    private static String userEmail;

    private static int userId;

    // Method to set the UserId
    public static void setUserId(int id) {
        userId = id;
    }
    public static int getUserId() {
        return userId;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static void setUserEmail(String email) {
        userEmail = email;
    }


}
