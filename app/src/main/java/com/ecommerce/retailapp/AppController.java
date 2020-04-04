/*
        ~ Copyright (c) 2017. http://annoyingprojects.com- All Rights Reserved
        ~ Unauthorized copying of this file, via any medium is strictly prohibited
        ~ If you use or distribute this project then you MUST ADD A COPY OF LICENCE
        ~ along with the project.
        ~  Written by Albi Arapi <albiarapi1@gmail.com>, 2017.
        */

package com.ecommerce.retailapp;

import android.app.Application;

import com.ecommerce.retailapp.utils.PreferenceHelper;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

@ReportsCrashes(mailTo = "albiarapi1@gmail.com", customReportContent = {
        ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME,
        ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL,
        ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT}, mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast_text)
public class AppController extends Application {

    public static final String TAG = AppController.class.getSimpleName();

    private static AppController mInstance;

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        // The following line triggers the initialization of ACRA for crash Log Reposrting
        if (PreferenceHelper.getPrefernceHelperInstace().getBoolean(
                this, PreferenceHelper.SUBMIT_LOGS, true)) {
            ACRA.init(this);
        }
    }

}