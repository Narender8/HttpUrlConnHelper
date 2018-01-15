package sample.httphelper;

public interface ResponseCallback {
    void onSuccess(Object object);

    void onFailure(Throwable failure);
}
