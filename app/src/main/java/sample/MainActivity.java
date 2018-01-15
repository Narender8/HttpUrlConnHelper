package sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import sample.httphelper.ConnectionHelper;
import sample.httphelper.RequestType;
import sample.httphelper.ResponseCallback;
import sample.narenderhttpurl.com.httpurlconnhelper.R;

public class MainActivity extends AppCompatActivity {

    String LoginUrl = "http://ip-api.com/json";
    TextView txtOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtOutput = (TextView) findViewById(R.id.txtOutput);
    }

    public void getFunction(View v) {
        ConnectionHelper.request(RequestType.GET, LoginUrl, new ResponseCallback() {
            @Override
            public void onSuccess(Object object) {
                try {
                    Log.e("onSuccess", "" + object.toString());
                    txtOutput.setText(object.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable failure) {
                try {
                    Log.e("onFailure", "" + failure.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
