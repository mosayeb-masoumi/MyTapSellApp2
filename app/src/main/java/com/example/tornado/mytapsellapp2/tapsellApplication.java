package com.example.tornado.mytapsellapp2;

import android.app.Application;

import ir.tapsell.sdk.Tapsell;
import ir.tapsell.sdk.TapsellConfiguration;


public class tapsellApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        TapsellConfiguration config = new TapsellConfiguration(this);
        config.setPermissionHandlerMode(TapsellConfiguration.PERMISSION_HANDLER_DISABLED);
//        Tapsell.initialize(this, config, BuildConfig.tapsellSampleAppKey);
        Tapsell.initialize(this,config,"mpicgtbcoilqgprkeqhnpladaieadgfqmlaabtmbknseqlhftfpngrccnhkjpfblgkbofl");
        }
        }