package sample.httphelper;

/**
 * Created by Narender.Gusain on 12/1/17.
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
