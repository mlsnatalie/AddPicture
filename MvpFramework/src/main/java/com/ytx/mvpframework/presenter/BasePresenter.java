package com.ytx.mvpframework.presenter;

import com.ytx.mvpframework.view.IView;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class BasePresenter<V extends IView> {
    private CompositeSubscription compositeSubscription = null;
    private V view;

    public BasePresenter(V view) {
        this.view = view;
    }

    public final V getView() {
        return view;
    }

    public final void addSubscription(Subscription subscription) {
        if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
            synchronized(BasePresenter.class) {
                if (compositeSubscription == null || compositeSubscription.isUnsubscribed()) {
                    compositeSubscription = new CompositeSubscription();
                }
            }
        }
        compositeSubscription.add(subscription);
    }

    public final void unSubscribe() {
        if (compositeSubscription != null) {
            compositeSubscription.unsubscribe();
        }
    }
}
