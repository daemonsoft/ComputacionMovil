package co.edu.udea.compumovil.gr04_20171.lab3.services;

/**
 * Created by daemonsoft on 2/04/17.
 */


public class Config {
    //server host URL
    public static final String HOST = "https://api-mobileevents.rhcloud.com/rest/";

    //api paths
    public static final String USERS_PATH = "users";
    public static final String EVENTS_PATH = "events";
    public static final String PHOTO_PATH = "photos";


    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "mobileeventsapp";

    //This would be used to store the email of current logged in user
    public static final String EMAIL_SHARED_PREF = "email";

    //We will use this to store the boolean in sharedpreference to track user keepsession option
    public static final String KEEP_SESSION_SHARED_PREF = "keepsession";
    public static final String NAME_SHARED_PREF = "name";
    public static final String AGE_SHARED_PREF = "age";
    public static final String PASSWORD_SHARED_PREF = "password";
    public static final String LOGIN_REQUEST = "login";
    public static final String SIGN_UP_REQUEST = "register";


    public static String getUserUrl(String email) {
        return HOST + USERS_PATH + "/" + email;
    }
    public static String getEventsUrl() {
        return HOST + EVENTS_PATH;
    }

    public static String getUserPostUrl() {
        return HOST + USERS_PATH;
    }

    public static String getPhotoUrl(String name) {
        return HOST + PHOTO_PATH + "/" + name;
    }
    public static String getPhotoPostUrl(String name) {
        return HOST + PHOTO_PATH + "/" + name;
    }

    public static String getEventPostUrl() {
        return HOST + EVENTS_PATH + "/";
    }
}