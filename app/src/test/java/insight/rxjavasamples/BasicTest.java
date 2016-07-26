package insight.rxjavasamples;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Use Schedulers.computation() to represent AndroidSchedulers.mainThread()
 */
public class BasicTest {

    @Test
    public void test1() throws Exception {
        Basic basic = new Basic();

        Observable<String> task1 = basic.task1("test1");
        task1.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

                System.out.println("== Begin task1 subscribe == Thread#" + Thread.currentThread().getName());

                System.out.println(s);

                System.out.println("== End task1 subscribe ==");
            }
        });
    }

    @Test
    public void test11() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        Basic basic = new Basic();
        basic.task1("test1")
            .observeOn(Schedulers.computation())
            .subscribeOn(Schedulers.io())
            .subscribe(new Action1<String>() {
                @Override
                public void call(String s) {

                    System.out.println("== Begin task1 subscribe == Thread#" + Thread.currentThread().getName());

                    System.out.println(s);

                    System.out.println("== End task1 subscribe ==");

                    latch.countDown();
                }
            });


        latch.await();
    }

    @Test
    public void test12() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        Basic basic = new Basic();

        Observable<String> task1 = basic.task1("test1");
        task1.subscribeOn(Schedulers.io())
            .doOnCompleted(new Action0() {
                @Override
                public void call() {
                    System.out.println("doOnCompleted");
                }
            }).doOnError(new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    System.out.println("doOnError " + throwable);
                }
            }).doOnTerminate(new Action0() {
                @Override
                public void call() {
                    System.out.println("doOnTerminate");
                }
            }).doOnUnsubscribe(new Action0() {
                @Override
                public void call() {
                    System.out.println("doOnUnsubscribe");
                }
            }).doAfterTerminate(new Action0() {
                @Override
                public void call() {
                    System.out.println("doAfterTerminate");

                    latch.countDown();
                }
            }).subscribe(new Action1<String>() {
                @Override
                public void call(String s) {

                    System.out.println("== Begin task1 subscribe == Thread#" + Thread.currentThread().getName());

                    System.out.println(s);

                    System.out.println("== End task1 subscribe ==");
                }
            });

        latch.await();
    }

    @Test
    public void test13() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        final Basic basic = new Basic();

        Observable<String> task1 = basic.taskError("test1");
        task1.subscribeOn(Schedulers.io())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        System.out.println("doOnCompleted");
                    }
                }).doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                System.out.println("doOnError " + throwable);
            }
        }).doOnTerminate(new Action0() {
            @Override
            public void call() {
                System.out.println("doOnTerminate");
            }
        }).doOnUnsubscribe(new Action0() {
            @Override
            public void call() {
                System.out.println("doOnUnsubscribe");
            }
        }).doAfterTerminate(new Action0() {
            @Override
            public void call() {
                System.out.println("doAfterTerminate");

                latch.countDown();
            }
        }).onErrorResumeNext(new Func1<Throwable, Observable<? extends String>>() {
            @Override
            public Observable<? extends String> call(Throwable throwable) {
                return basic.task1("ABAB");
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

                System.out.println("== Begin task1 subscribe == Thread#" + Thread.currentThread().getName());

                System.out.println(s);

                System.out.println("== End task1 subscribe ==");
            }
        });

        latch.await();
    }

    @Test
    public void test14() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        final Basic basic = new Basic();

        Observable<String> task1 = basic.taskError("test1");
        task1.subscribeOn(Schedulers.io())
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        System.out.println("doOnCompleted");
                    }
                }).doOnError(new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                System.out.println("doOnError " + throwable);
            }
        }).doOnTerminate(new Action0() {
            @Override
            public void call() {
                System.out.println("doOnTerminate");
            }
        }).doOnUnsubscribe(new Action0() {
            @Override
            public void call() {
                System.out.println("doOnUnsubscribe");
            }
        }).doAfterTerminate(new Action0() {
            @Override
            public void call() {
                System.out.println("doAfterTerminate");

                latch.countDown();
            }
        }).onErrorReturn(new Func1<Throwable, String>() {
            @Override
            public String call(Throwable throwable) {
                return "onErrorReturn";
            }
        }).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {

                System.out.println("== Begin task1 subscribe == Thread#" + Thread.currentThread().getName());

                System.out.println(s);

                System.out.println("== End task1 subscribe ==");
            }
        });

        latch.await();
    }


    @Test
    public void test2() throws Exception {
        Basic basic = new Basic();

        Observable<String> task1 = basic.task1("test1");
        Observable<String> task2 = basic.task2("test2");

        final CountDownLatch latch = new CountDownLatch(2);

        task1.subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("== Begin task1 subscribe == Thread#" + Thread.currentThread().getName());

                        System.out.println(s);

                        System.out.println("== End task1 subscribe ==");

                        latch.countDown();
                    }
                });

        task2.subscribeOn(Schedulers.newThread()).subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("== Begin task2 subscribe == Thread#" + Thread.currentThread().getName());

                System.out.println(s);

                System.out.println("== End task2 subscribe ==");
                latch.countDown();
            }
        });

        latch.await();
    }

    @Test
    public void test3() throws Exception {
        Basic basic = new Basic();

        Observable<String> task1 = basic.task1("test1");
        Observable<String> task2 = basic.task2("test2");

        final CountDownLatch latch = new CountDownLatch(2);

        Observable.concat(task1, task2)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        System.out.println("== Begin concat subscribe == Thread#" + Thread.currentThread().getName());

                        System.out.println(s);

                        System.out.println("== End concat ==");
                        latch.countDown();
                    }
                });

        latch.await();
    }

    @Test
    public void test31() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        Basic basic = new Basic();
        final Observable<String> task1 = basic.task1("test1").subscribeOn(Schedulers.newThread());
        final Observable<String> task2 = basic.task2("test2").subscribeOn(Schedulers.newThread());

        Observable<Boolean> pendingTasks = Observable.combineLatest(task1, task2, new Func2<String, String, Boolean>() {
            @Override
            public Boolean call(String s, String s2) {
                System.out.println("== Begin combineLatest subscribe == Thread#" + Thread.currentThread().getName());

                System.out.println(s);

                System.out.println(s2);

                System.out.println("== End combineLatest ==");

                return null;
            }
        });

        pendingTasks.observeOn(Schedulers.computation())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean o) {
                        System.out.println("== done == Thread#" + Thread.currentThread().getName());
                        latch.countDown();
                    }
                });

        latch.await();
    }

    @Test
    public void test4() throws Exception {
        Basic basic = new Basic();

        Observable<String> task1 = basic.task1("test1");
        Observable<String> task2 = basic.task2("test2");

        final CountDownLatch latch = new CountDownLatch(1);

        Observable.zip(task1, task2, new Func2<String, String, Object>() {
            @Override
            public Object call(String s, String s2) {
                System.out.println("== Begin zip subscribe == Thread#" + Thread.currentThread().getName());

                System.out.println(s);

                System.out.println(s2);

                System.out.println("== End zip ==");
                latch.countDown();

                return null;
            }
        }).subscribeOn(Schedulers.io()).subscribe();

        latch.await();
    }

    @Test
    public void test5() throws Exception {

        final CountDownLatch latch = new CountDownLatch(1);

        Basic basic = new Basic();
        Observable<String> task1 = basic.rxLongRunTask(6000).subscribeOn(Schedulers.io());
        Observable<String> timeout = Observable.just("TIMEOUT").delay(2000, TimeUnit.MILLISECONDS);

        Observable.amb(task1, timeout).subscribe(new Action1<Serializable>() {
            @Override
            public void call(Serializable serializable) {
                System.out.println("== Begin amb subscribe == Thread#" + Thread.currentThread().getName());

                System.out.println(serializable);

                System.out.println("== End amb ==");

                latch.countDown();
            }
        });

        latch.await();
    }

    @Test
    public void test6() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        Basic basic = new Basic();
        Observable<String> task1 = basic.rxHttpGet("http://www.google.com").subscribeOn(Schedulers.io());

        task1.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("== Begin test6 subscribe == Thread#" + Thread.currentThread().getName());
                System.out.println(s);
                System.out.println("== End test6 ==");

                latch.countDown();
            }
        });

        latch.await();
    }

    @Test
    public void test7() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        Basic basic = new Basic();
        Observable<String> task1 = basic.rxLoadFile("proguard-rules.pro").subscribeOn(Schedulers.io());

        task1.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("== Begin test6 subscribe == Thread#" + Thread.currentThread().getName());
                System.out.println(s);
                System.out.println("== End test6 ==");

                latch.countDown();
            }
        });

        latch.await();
    }

    @Test
    public void test8() throws Exception {
        final CountDownLatch latch = new CountDownLatch(1);

        final Basic basic = new Basic();
        Observable<String> task1 = basic.rxLoadFile("proguard-rules.pro").subscribeOn(Schedulers.io());

        task1.subscribe(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("== Begin test6 subscribe == Thread#" + Thread.currentThread().getName());
                System.out.println(s);
                System.out.println("== End test6 ==");

                ByteArrayInputStream is = new ByteArrayInputStream(s.getBytes());
                try {
                    basic.saveFile(is, "abc.txt");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                latch.countDown();
            }
        });

        latch.await();
    }

    /**
     * Manually create Observable & Subscriber
     */
    @Test
    public void test9() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        Observable<Integer> o = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
                subscriber.onCompleted();
            }
        });

        o.doOnCompleted(new Action0() {
            @Override
            public void call() {
                System.out.println("onCompleted");

                latch.countDown();
            }
        })
        .subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("event: " + integer);
            }
        });

        System.out.println("await");
        latch.await(2000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void test91() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        Observable<Integer> o = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(1);
                subscriber.onNext(2);
                subscriber.onNext(3);
                subscriber.onCompleted();
            }
        });

        o.map(new Func1<Integer, Integer>() {
            @Override
            public Integer call(Integer integer) {
                return integer * 10;
            }
        })
        .doOnCompleted(new Action0() {
            @Override
            public void call() {
                System.out.println("onCompleted");

                latch.countDown();
            }
        })
        .subscribe(new Action1<Integer>() {
            @Override
            public void call(Integer integer) {
                System.out.println("event: " + integer);
            }
        });

        System.out.println("await");
        latch.await(2000, TimeUnit.MILLISECONDS);
    }
}