package sample.narenderhttpurl.com.httpurlconnhelper.async;

public interface BackgroundJob<R> {
    R executeInBackground() throws Exception;

    void onSuccess(R result);

    void onFailure(Throwable error);
}
