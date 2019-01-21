## MVPFragmentwork library wiki

本library主要是MVP模式的一种实现，会动自动绑定activity和fragment生命周期方法

### 添加依赖

1. 添加maven仓库地址

		maven { url 'http://maven.tiantong99.com:8081/nexus/content/repositories/releases/'}
		maven { url 'http://maven.tiantong99.com:8081/nexus/content/repositories/snapshots/'}

2. 添加依赖

        implementation 'com.ytx.spero:mvpframework:1.0.1'
		//其他依赖
		implementation 'android.arch.lifecycle:runtime:1.1.1'
		implementation 'android.arch.lifecycle:extensions:1.1.1'
		implementation 'io.reactivex:rxandroid:1.2.1'//建议使用最新版
        implementation 'io.reactivex:rxjava:1.3.4'//建议使用最新版


3. 针对activity

		1. 定一个View实现IView接口
		2. 定义一个Presenter 继承ActivityPresenter

		example

		public interface TestView extends IView {
        	...
    	}

    	public class TestActivityPresenter extends ActivityPresenter<TestView> {
		    public TestActivityPresenter(TestView view) {
		        super(view);
		    }

		    @Override
		    public void resume(LifecycleOwner lifecycleOwner) {
		        // do something
		    }

		    //... other lifecycle method
		}


4. 针对Fragment，同上