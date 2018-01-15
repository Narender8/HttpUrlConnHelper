package sample.narenderhttpurl.com.httpurlconnhelper.async;

import android.content.ContentValues;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import sample.narenderhttpurl.com.httpurlconnhelper.async.RequestType;

/**
 * HttpConnectionHelper
 *
 * @author dotkebi on 2016. 2. 19..
 */
public class HttpConnectionHelper {

    private static final String CHARSET = "UTF-8";
    private static final int RESPONSE_TIME = 15 * 1000; //15 SECONDS

    /**
     *
     * @param requestType
     * @param url
     * @return
     */
    public JSONObject enqueue(RequestType requestType, String url) {
        return enqueue(requestType, url, null, null);
    }

    /**
     *
     * @param requestType
     * @param url
     * @param header
     * @return
     */
    public JSONObject enqueue(RequestType requestType, String url, ContentValues header) {
        return enqueue(requestType, url, header, null);
    }

    /**
     *
     * @param requestType
     * @param url
     * @param header
     * @param body
     * @return
     */
    public JSONObject enqueue(RequestType requestType, String url, ContentValues header, ContentValues body) {
        JSONObject response = null;
        try {
            HttpURLConnection conn = getConnection(url, requestType, header);
            conn.connect();

            //Write
            OutputStream os = conn.getOutputStream();
            if (body != null)
                os.write(getQuery(body).getBytes());
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
            processed += (char) Integer.parseInt(token, 16);
            position = escaped.indexOf("\\u");
        }
        processed += escaped;
        return processed;
    }

    private HttpURLConnection getConnection(String url, RequestType requestType, ContentValues headerList) throws IOException {
        URL connectURL = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setDefaultUseCaches(false);
        conn.setReadTimeout(RESPONSE_TIME);

        if (requestType == RequestType.GET)
            conn.setRequestMethod("GET");
        else if (requestType == RequestType.POST)
            conn.setRequestMethod("POST");
        else if (requestType == RequestType.PUT)
            conn.setRequestMethod("PUT");
        else if (requestType == RequestType.PATCH)
            conn.setRequestMethod("PATCH");
        else if (requestType == RequestType.DELETE)
            conn.setRequestMethod("DELETE");

        if (headerList != null) {
            for (Map.Entry<String, Object> pair : headerList.valueSet()) {
                conn.setRequestProperty(pair.getKey(), pair.getValue().toString());
            }
        }

        return conn;
    }

    private String getQuery(ContentValues params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, Object> pair : params.valueSet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getKey(), CHARSET));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue().toString(), CHARSET));
        }

        return result.toString();
    }

}
