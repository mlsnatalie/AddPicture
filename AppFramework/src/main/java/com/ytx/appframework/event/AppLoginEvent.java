package com.ytx.appframework.event;

/**
 * Created by linxi on 2018/4/19.
 */

public class AppLoginEvent {
    public boolean isLogin; // 表示当前状态是否登录
    public Object data;

    public AppLoginEvent(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public AppLoginEvent(boolean isLogin, Object data) {
        this.isLogin = isLogin;
        this.data = data;
    }
}
