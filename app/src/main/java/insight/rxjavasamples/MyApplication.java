package insight.rxjavasamples;

import android.app.Application;

/**
 * Created by wychi on 2016/7/27.
 */

public class MyApplication extends Application {
    Basic b = new Basic();
    @Override
    public void onCreate() {
        super.onCreate();

        b.rxLongRunTask(10);

    }
}
