package com.project.realmprac;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by 정인섭 on 2017-11-17.
 */

public class RealmApp extends Application { //Activity가 실행되기 전 실행된다. 모든 Activity가 공통으로 사용하는것은 여기에 놔둔다.

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
    }
}
