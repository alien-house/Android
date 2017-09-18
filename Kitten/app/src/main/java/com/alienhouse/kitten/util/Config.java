package com.alienhouse.kitten.util;

/**
 * Created by shinji on 2017/09/01.
 */

public class Config {
    public static String GOOGLEAPIS_KEY = "AIzaSyCOvdiOD-i_OlyLIjrN8fJQp96P3gFYX6Q";
//    public static String GOOGLEAPIS_KEY = "AIzaSyATToa4mk7JoEWHepNvjeKKX9MZ8kc5IGw";

    public static String GITHUB_ID = "5a71911fdeceb4d69eee";
    public static String GITHUB_SECRET = "718114c4f8b9ff50593707853cf8c3f61ba05c25";
    public static String CALLBACK_URL = "https://programming-473ea.firebaseapp.com/__/auth/handler";

    public static final String EVENTBRITE_URL_BASE = "https://www.eventbriteapi.com";
    public static final String EVENTBRITE_URL_API = "/v3/events/search/?";
    public static final String EVENTBRITE_TOKEN = "CVKT5QQJUJYOJDWX2KNX";
    //https://www.eventbriteapi.com/v3/events/search/?q=ios developer&location.address=vancouver,bc&expand=organizer,venue,events&token=CVKT5QQJUJYOJDWX2KNX

    public static final String MEETUP_URL_BASE = "https://api.meetup.com";
    public static final String MEETUP_URL_API = "/find/events?";
    public static final String MEETUP_URL_SIGNID = "&sig_id=196968326";
//    public static final String MEETUP_URL_SIGN = "&sig=a964212704d5a561cf151c0d5fe2961a9908195b";
    public static final String MEETUP_URL_SIGN = "&sign=true&key=1b7e6b545246732d3b4d6c53f3a5563";
//    https://api.meetup.com/find/events?upcoming_events=true&photo-host=public&page=20&text=iOS+Developer&sig_id=196968326&sig=caaafeee55fdc0128c313cd4c0d41ef212377003
}
