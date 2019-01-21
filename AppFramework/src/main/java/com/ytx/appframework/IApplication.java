package com.ytx.appframework;

import android.app.Activity;

/**
 * Created by fangcan on 2018/4/8.
 */

public interface IApplication {
    void bindCurrentActivity(Activity activity);
    void unBindCurrentActivity(Activity activity);
    Activity getCurrentActivity();
}
