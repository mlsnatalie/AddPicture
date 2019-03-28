package com.example.menglingshuai.addpicture;

/**
 * @author rjhy
 * @created on 16-9-28
 * @desc desc
 */
public class CodeConfig {
    /** common code */
    public static final int OK = 200;
    public static final int ERROR = 500;

    //用户没有绑定
    public static final int NEED_BIND_PHONE = 34006;
    //需要登陆
    public static final int NEED_LOGIN = 34007;
    //账号在其他设备登录
    public static final int LOGIN_KICK = 34022;
    //你的登录已过期, 请重新登录
    public static final int TOKEN_EXPIRED = 34023;
    //你的付费已过期, 购买后可以继续使用
    public static final int PAY_EXPIRED = 34024;


    public static final int NO_SUGGEST_STOCK = 13001;

    /** TOKEN 过期 */
    public static final int EWD_TOKEN_EXPIRED = 401;
    /** REFRESH TOKEN 过期 */
    public static final int EWD_REFRESH_EXPIRATION_CODE = 99999;
}
