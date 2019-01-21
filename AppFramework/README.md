## AppFragmentwork library wiki

本library主要是app 基本框架，包含如下基本功能

- TitleBar (app 基本的actionbar widget)
- ProgressContent （提供loading、error、empty、content 状态切换）
- BaseActivity (所有的activity 必需继承它)
- BaseFragment （同上）
- LazyFragment ()
- BaseActivity + BaseFragment + LazyFragment 都实现了presenter，且绑定activity或fragment生命周期
- 等等其他功能

### 添加依赖

1. 添加maven仓库地址

		maven { url 'http://maven.tiantong99.com:8081/nexus/content/repositories/releases/'}
		maven { url 'http://maven.tiantong99.com:8081/nexus/content/repositories/snapshots/'}

2. 添加依赖

        implementation 'com.ytx.spero:appframework:1.0.1'
		//其他依赖
		implementation 'com.ytx.spero:mvpframework:1.0.1'
		implementation 'android.arch.lifecycle:runtime:1.1.1'
        implementation 'android.arch.lifecycle:extensions:1.1.1'
        implementation 'org.greenrobot:eventbus:3.0.0'
		implementation 'io.reactivex:rxandroid:1.2.1'//建议使用最新版
        implementation 'io.reactivex:rxjava:1.3.4'//建议使用最新版
