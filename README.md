# AddPicture_baseProject
从图库选择图片或者用摄像头拍摄图片，显示到Activity上，基础的Android项目组件
# AddPicture
Android选择相册图片

# 请求权限的两种方式


# 1.使用依赖动态请求权限
1、在Manifest里设置权限
2、引入github上的依赖库
github项目地址：https://github.com/mlsnatalie/Asi-library
implementation 'com.github.dfqin:grantor:2.5'

3、在需要请求权限的地方，写入方法
requestReadPhoneState()
4、请求权限的具体方法
```
private fun requestReadPhoneState() {

        PermissionsUtil.requestPermission(this, object : PermissionListener {
            override fun permissionDenied(permission: Array<out String>) {
                Toast.makeText(this@MainActivity, "访问内部存储", Toast.LENGTH_LONG).show()
            }

            override fun permissionGranted(permission: Array<out String>) {
                Toast.makeText(this@MainActivity, "用户拒绝了访问内部存储", Toast.LENGTH_LONG).show()
            }

        }, Manifest.permission.READ_EXTERNAL_STORAGE)
    }
```

# 2.第二种动态申请权限的方法
1.在MainActivity继承的BaseActivity里，写入动态申请权限的方法
```
 /**
     * 权限授权回调
     */
    PermissionListener mPermissionListener;
    
    
    
    /**
     * 权限申请
     * @param permissions
     * @param listener
     */
    protected void requestRunTimePermission(String[] permissions, PermissionListener listener) {

        //todo 获取栈顶activity，如果null。return；

        this.mPermissionListener = listener;

        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if(ContextCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
                permissionList.add(permission);
            }
        }
        if(!permissionList.isEmpty()){
            ActivityCompat.requestPermissions(this,permissionList.toArray(new String[permissionList.size()]),1);
        }else{
            listener.onGranted();
        }
    }

    /**
     * 申请结果
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case 1:
                if (grantResults.length>0){
                    List<String> deniedPermissions = new ArrayList<>();
                    List<String> grantedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED){
                            String permission = permissions[i];
                            deniedPermissions.add(permission);
                        }else{
                            String permission = permissions[i];
                            grantedPermissions.add(permission);
                        }
                    }

                    if (deniedPermissions.isEmpty()){
                        mPermissionListener.onGranted();
                    }else{
                        mPermissionListener.onDenied(deniedPermissions);
                        mPermissionListener.onGranted(grantedPermissions);
                    }
                }
                break;
        }
    }
```

2、写PermissionListener的回调接口
```
public  interface PermissionListener {
    //全部授权成功
    void onGranted();
    //授权部分
    void  onGranted(List<String> grantedPermission);
    //拒绝授权
    void  onDenied(List<String> deniedPermission);
}
```

3、MainActivity继承接口，写回调方法
```
class MainActivity : BaseActivity<PreparePicturePresenter>(), PreparePictureView, PermissionListener {
    override fun onGranted() {
        Toast.makeText(this@MainActivity, "访问内部存储", Toast.LENGTH_LONG).show()
    }

    override fun onGranted(grantedPermission: MutableList<String>?) {
        Toast.makeText(this@MainActivity, "访问内部存储", Toast.LENGTH_LONG).show()
    }

    override fun onDenied(deniedPermission: MutableList<String>?) {
    }
}
```

4、在需要申请权限的地方
```
val array = arrayOfNulls<String>(1)
        array[0] = Manifest.permission.READ_EXTERNAL_STORAGE
        requestRunTimePermission(array, this)
```