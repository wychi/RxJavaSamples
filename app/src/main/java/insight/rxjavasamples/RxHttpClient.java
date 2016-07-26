package insight.rxjavasamples;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;

import rx.Observable;

/**
 * Created by wychi on 2016/7/21.
 */
public class RxHttpClient {

    private static final String TAG = "insight";
    
    public Observable<String> GET(final String urlString) {
        Log.d(TAG, "== RxHttpClient GET == Thread#" + Thread.currentThread().getName());

        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {

                long ts = System.currentTimeMillis();
                Log.d(TAG, "== Begin GET == Thread#" + Thread.currentThread().getName());

                URL url = null;
                HttpURLConnection urlConnection = null;
                try {
                    url = new URL(urlString);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());

                    ByteArrayOutputStream result = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = in.read(buffer)) != -1) {
                        result.write(buffer, 0, length);
                    }

                    long elapsed = System.currentTimeMillis() - ts;
                    Log.d(TAG, "== End   GET == " + elapsed + "ms");

                    return result.toString("UTF-8");

                } finally {
                    if(urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        });
    }
}
