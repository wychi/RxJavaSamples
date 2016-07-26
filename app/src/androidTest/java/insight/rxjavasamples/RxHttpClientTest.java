package insight.rxjavasamples;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

@RunWith(AndroidJUnit4.class)
public class RxHttpClientTest {

    private static final String TAG = "insight";

    @Test
    public void testGET() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        RxHttpClient httpClient = new RxHttpClient();
        httpClient.GET("https://www.google.com")
            .onErrorReturn(new Func1<Throwable, String>() {
                @Override
                public String call(Throwable throwable) {
                    Log.d(TAG, "== Error == " + throwable);
                    return null;
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.computation())
            .subscribe(new Action1<String>() {
                @Override
                public void call(String s) {
                    Log.d(TAG, "== Begin task1 subscribe == Thread#" + Thread.currentThread().getName());

                    Log.d(TAG, s);

                    Log.d(TAG, "== End task1 subscribe ==");

                    latch.countDown();
                }
            });

        latch.await();
    }
}