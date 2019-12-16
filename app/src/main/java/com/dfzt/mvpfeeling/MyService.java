package com.dfzt.mvpfeeling;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;

public class MyService extends Service {


    @Override
    public void onCreate() {
        super.onCreate();

        MainActivity.setObeservable(observable);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }


    Observable observable =  Observable.create(new ObservableOnSubscribe<Integer>() {
        @Override
        public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {

            emitter.onNext(1);
            emitter.onNext(10);
            emitter.onNext(1000);

        }
    });



    @Override
    public IBinder onBind(Intent intent) {
return null;
    }



}
