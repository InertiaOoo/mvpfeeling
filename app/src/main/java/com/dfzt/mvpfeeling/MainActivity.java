package com.dfzt.mvpfeeling;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import me.yokeyword.fragmentation.ExtraTransaction;
import me.yokeyword.fragmentation.ISupportActivity;
import me.yokeyword.fragmentation.SupportActivityDelegate;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.bt_1)
    Button bt_1;

    @BindView(R.id.tv_1)
    TextView tv_1;

    @BindView(R.id.tv_2)
    TextView tv_2;

    Disposable disposable1;
    Disposable disposable2;


    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        observable.subscribe(observer);


        startService(new Intent(this,MyService.class));

//        testinterval();

        testCutdown(20);

    }

    @OnClick(R.id.bt_1)
    void button1() {
        Log.e(TAG, "click1");

        if (disposable2 != null && !disposable2.isDisposed()) {
            disposable2.dispose();
            disposable2 = null;
        }

    }

    public static void setObeservable(Observable obeservable) {

        obeservable.subscribe(observer);
    }



    private void testCutdown(int time){

        Observable.interval(1, TimeUnit.SECONDS)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return time - aLong;
                    }
                }).take( time + 1 ).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.e("zhao", "accept: 倒计时： " + aLong);
            }
        });
    }

    private void testinterval() {
        //方法1
        disposable1 = Flowable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) throws Exception {

                        tv_1.setText(String.valueOf(aLong));

                        if (disposable1 != null && !disposable1.isDisposed() && aLong > 14) {
                            disposable1.dispose();
                            disposable1 = null;
                        }
                    }
                });

//方法2
        disposable2 = Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                        tv_2.setText(String.valueOf(aLong));

                    }
                });
    }

    private void testZip() {


        Observable.zip(getStringObservable(), getIntegerObservable(), new BiFunction<String, Integer, String>() {
            @Override
            public String apply(@NonNull String s, @NonNull Integer integer) throws Exception {
                return s + integer;
            }
        })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e("zhao", "accept: " + s);
                    }
                });

    }

    //创建 String 发射器
    private Observable<String> getStringObservable() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext("A");
                e.onNext("B");
                e.onNext("C");
            }
        });
    }

    //创建 Integer 发射器
    private Observable<Integer> getIntegerObservable() {
        return Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
                e.onNext(4);
                e.onNext(5);
            }
        });
    }


    private void testconcatMap() {

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(1);
                e.onNext(2);
                e.onNext(3);
            }
        })
                .concatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < 5; i++) {
                            list.add("I am value " + integer);
                        }
                        //随机生成一个时间
                        int delayTime = (int) (1 + Math.random() * 10);
                        return Observable.fromIterable(list).delay(2000, TimeUnit.MILLISECONDS);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.e("zhao", "accept: " + s);
                    }
                });

    }


    static Observer observer = new Observer<Integer>() {

        private Disposable mDisposable;
        private int i;


        @Override
        public void onSubscribe(Disposable d) {
            Log.d(TAG, "======================onSubscribe");
        }

        @Override
        public void onNext(Integer integer) {
            Log.d(TAG, "onNext收到：" + integer);
            i++;
            if (i == 2) {
                Log.d(TAG, "onNext：dispose");
                mDisposable.dispose();//取消订阅，不再接收事件
                Log.d(TAG, "onNext isDisposed : " + mDisposable.isDisposed());
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.d(TAG, "======================onError");
        }

        @Override
        public void onComplete() {
            Log.d(TAG, "======================onComplete");
        }
    };


}
