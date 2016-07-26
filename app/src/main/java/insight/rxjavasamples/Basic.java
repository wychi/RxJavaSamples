package insight.rxjavasamples;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Action0;

/**
 * Created by wychi on 2016/7/21.
 */
public class Basic {

    public static class ThreadUtil {
        public static void sleep(long ms) {
            try {
                Thread.sleep(ms);
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void IOUtilClose(Closeable closeable) {
        if(closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    Action0 mOnCompleteHandler = new Action0() {
        @Override
        public void call() {
            System.out.println("mOnCompleteHandler");
        }
    };

    public Observable<String> taskError(final String name) {
        System.out.println("task1 Thread#" + Thread.currentThread().getName());

        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                long ts = System.currentTimeMillis();
                System.out.println("== Begin taskError == Thread#" + Thread.currentThread().getName());
                ThreadUtil.sleep((long) (Math.random() * 2000));

                long elapsed = System.currentTimeMillis() - ts;
                System.out.println("== End   taskError == " + elapsed + "ms");

                throw new Exception();
            }
        });
    }

    public Observable<String> task1(final String name) {
        System.out.println("task1 Thread#" + Thread.currentThread().getName());

        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                long ts = System.currentTimeMillis();
                System.out.println("== Begin task1 == Thread#" + Thread.currentThread().getName());
                ThreadUtil.sleep((long) (Math.random() * 2000));

                long elapsed = System.currentTimeMillis() - ts;
                System.out.println("== End   task1 == " + elapsed + "ms");
                return "Hello " + name;
            }
        });
    }

    public Observable<String> task2(final String name) {
        System.out.println("task2 Thread#" + Thread.currentThread().getName());

        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {

                System.out.println("== Begin task2 == Thread#" + Thread.currentThread().getName());
                long ts = System.currentTimeMillis();

                ThreadUtil.sleep((long) (Math.random() * 2000));

                long elapsed = System.currentTimeMillis() - ts;
                System.out.println("== End   task2 == " + elapsed + "ms");
                return "Hello " + name;
            }
        });
    }

    public String longRunTask(long duration) {
        System.out.println("== Begin longRunTask == Thread#" + Thread.currentThread().getName());
        long ts = System.currentTimeMillis();

        ThreadUtil.sleep( duration);

        long elapsed = System.currentTimeMillis() - ts;
        System.out.println("== End   longRunTask == " + elapsed + "ms");

        return "LONG_RUN_TASK_DONE";
    }

    public Observable<String> rxLongRunTask(final long ts) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return longRunTask(ts);
            }
        });
    }

    public String httpGet(String url) throws IOException {
        System.out.println("== Begin httpGet == Thread#" + Thread.currentThread().getName());
        long ts = System.currentTimeMillis();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();

        long elapsed = System.currentTimeMillis() - ts;
        System.out.println("== End   httpGet == " + elapsed + "ms");

        return response.body().string();
    }

    public Observable<String> rxHttpGet(final String url) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return httpGet(url);
            }
        });
    }

    public String loadFile(String filepath) throws IOException {
        FileInputStream fs = null;
        ByteArrayOutputStream result = null;

        try {
            fs = new FileInputStream(filepath);
            result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fs.read(buffer)) != -1) {
                result.write(buffer, 0, length);
            }

            return result.toString("UTF-8");
        } finally {
            IOUtilClose(fs);
            IOUtilClose(result);
        }
    }

    public Observable<String> rxLoadFile(final String filepath) {
        return Observable.fromCallable(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return loadFile(filepath);
            }
        }).doOnCompleted(mOnCompleteHandler);
    }

    public void saveFile(ByteArrayInputStream in, String filepath) throws IOException {
        FileOutputStream fs = null;
        try {
            fs = new FileOutputStream(filepath);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) != -1) {
                fs.write(buffer, 0, length);
            }
        } finally {
            IOUtilClose(fs);
        }
    }

    public Observable<Void> rxSaveFile(final ByteArrayInputStream in, final String filepath) {
        return Observable.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                saveFile(in, filepath);

                return null;
            }
        });
    }
}
