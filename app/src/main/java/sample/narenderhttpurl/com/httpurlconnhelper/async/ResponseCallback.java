package sample.narenderhttpurl.com.httpurlconnhelper.async;

/**
 * Created by ravishankeryadav on 12/1/16.
 */
public interface ResponseCallback {
    /**
     * Success callback when a PlaceDetails is fetched for the given Place
     */
    void onSuccess(Object object);

    /**
     * Failure callback when a PlaceDetails was failed to be fetched or parsed from the Places API
     */
    void onFailure(Throwable failure);
}
