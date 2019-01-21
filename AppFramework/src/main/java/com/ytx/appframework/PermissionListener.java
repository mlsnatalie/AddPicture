package com.ytx.appframework;

import java.util.List;

/**
 * Created by asi on 2016/12/27.
 */

public  interface PermissionListener {
    //全部授权成功
    void onGranted();
    //授权部分
    void  onGranted(List<String> grantedPermission);
    //拒绝授权
    void  onDenied(List<String> deniedPermission);
}
