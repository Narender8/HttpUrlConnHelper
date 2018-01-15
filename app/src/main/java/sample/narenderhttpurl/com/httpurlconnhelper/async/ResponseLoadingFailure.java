package sample.narenderhttpurl.com.httpurlconnhelper.async;

/**
 * Created by ravishankeryadav on 12/1/16.
 */
public class ResponseLoadingFailure extends Throwable {
    private final String source;

    public ResponseLoadingFailure(final String source) {
        this.source = source;
    }

    public ResponseLoadingFailure(final String source, final Throwable error) {
        super(error);
        this.source = source;
    }

    public String getSourcePlace() {
        return source;
    }
}
