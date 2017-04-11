package sample.narenderhttpurl.com.httpurlconnhelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * HttpConnectionHelper
 * @author dotkebi on 2016. 2. 19..
 */
public class HttpConnectionHelper {
    private static final String CHARSET = "UTF-8";

    public JSONObject callApi(String url, String param) {
        JSONObject response = null;
        try {
            HttpURLConnection conn = getConnection(url);
            conn.connect();

            //Write
            OutputStream os = conn.getOutputStream();
            os.write(param.getBytes(CHARSET));
            os.flush();
            os.close();

            int responseCode = conn.getResponseCode();
            String responsedString = getResponse(conn);

            if (responseCode == 200 || responseCode == 201) {
                response = new JSONObject(responsedString);
            }
            conn.disconnect();
        } catch (JSONException | IOException joe) {
            joe.printStackTrace();
        }
        return response;
    }

    private String getResponse(HttpURLConnection connection) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), CHARSET));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        return unescapeJava(sb.toString());
    }

    private String unescapeJava(String escaped) {
        if (!escaped.contains("\\u")) {
            return escaped;
        }
        String processed = "";

        int position = escaped.indexOf("\\u");
        while (position != -1) {
            if (position != 0) {
                processed += escaped.substring(0, position);
            }
            String token = escaped.substring(position + 2, position + 6);
            escaped = escaped.substring(position + 6);
            processed += (char)Integer.parseInt(token, 16);
            position = escaped.indexOf("\\u");
        }
        processed += escaped;
        return processed;
    }

    private HttpURLConnection getConnection(String url) throws IOException {
        URL connectURL = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setDefaultUseCaches(false);
        conn.setRequestMethod("POST");
        return conn;
    }
}
