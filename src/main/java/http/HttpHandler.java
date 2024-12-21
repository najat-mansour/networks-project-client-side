package http;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class HttpHandler {
    /**
     * method to add a new parameter to the query string
     *
     * @param txt the query string
     * @param key the key of the added parameter
     * @param value the value of the added parameter
     * @return the new (updated) query string after adding the parameter
     */
    public String addParameter(String txt, String key, String value) {
        if (!txt.isEmpty()) {
            txt += "&";
        }

        return txt + key + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

    public String requestByGet(String urlString) throws IOException {
        // generate a URL
        URL url = new URL(urlString);

        // Open a connection
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set request method
        connection.setRequestMethod("GET");

        return readResponse(connection);
    }

    public String requestByPost(String urlString, String body) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        BufferedOutputStream out = new BufferedOutputStream(connection.getOutputStream());
        out.write(body.getBytes());
        out.close();

        return readResponse(connection);
    }

    public String readResponse(HttpURLConnection connection) throws IOException {
        // Read the response
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Close the connection
        connection.disconnect();

        // return the response
        return response.toString();
    }
}
