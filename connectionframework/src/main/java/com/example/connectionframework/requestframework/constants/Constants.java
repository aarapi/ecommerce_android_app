package com.example.connectionframework.requestframework.constants;


public class Constants {

    public static boolean DEBUG = false;
    public static boolean FILL_USER_INFO = false;


    public static boolean DEBUG_SKIP_BEFORE_LOGIN = false;
    public static boolean ON_PROD = false;
    public static boolean ON_PRODPILOT = false;
    public static boolean ON_LOCAL = false;
    public static boolean ON_PREPROD_DEV = false;
    public static boolean IS_CORPORATE = false;
    public static String ANNOYING_PROJECTS_STORY_PREF = "annoying_shared_prefs";

    public static final int Success = 200;
    public static final int Confirmation = 600;
    public static final int ConfirmationWithInput = 601;
    public static final int Warning = 400;
    public static final int WarningAndRefreshPage = 440;
    public static final int ConnectionFailed = 408;
    public static final int ConnectionTimedOut = 700; //409;
    public static final int InvalidAction = 300;
    public static final int Error = 503;
    public static final int WarningWithoutAlert = 220;
    public static final int Inform = 210;
    public static final int SwitchToHttp = 101;
    public static final int MobileSignatureDelay=221;
    public static final int SecureLogOff=701;

    public static class Application {


       // public static final String APP_VERSION = BuildConfig.VERSION_NAME;
        public static final int CONNECTION_TIMEOUT = 30000;
        public static final String CONNECTION_TIMED_OUT_ERROR_MESSAGE = "CONNECTION_TIMED_OUT_ERROR_MESSAGE";
        public static final String CONNECTION_OTHER_EXCEPTION = "CONNECTION_OTHER_EXCEPTION_MESSAGE";
        public static final String CERTIFICATE_ERROR = "CertificateException";
        public static String STATIC_CRYPTO_KEY = "";
        public static final String HOCKEYAPP_BETA_ID="6a0ed5d67f5b08f2ae4ee9901b5465e6";
        public static final String HOCKEYAPP_ALPHA_ID="679f48dff3cef2880fe9dee845ad6c4a";

    }



    public static class Values {


    }

}