package sample.narenderhttpurl.com.httpurlconnhelper.async;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import com.aquevix.placesautocomplete.Constants;

import java.util.concurrent.*;

public enum BackgroundExecutorService {
    INSTANCE;

    /*
     * Max single thread ExecutorService that will spin down thread after use
     */
    private final Executor executor;
    {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1,
                5L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(),
                new ThreadFactory() {
                    @Override
                    public Thread newThread(@NonNull final Runnable r) {
                        return new Thread(r, Constants.LOG_TAG + "Thread");
                    }
                });
        executor.allowCoreThreadTimeOut(true);
        this.executor = executor;
    }

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public <R> void enqueue(final BackgroundJob<R> job) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final R result = job.executeInBackground();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            job.onSuccess(result);
                        }
                    });
                } catch (final Exception e) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            job.onFailure(e);
                        }
                    });
                }
            }
        });
    }
}
