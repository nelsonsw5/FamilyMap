package Handlers;

import DataAccess.DataAccessException;
import Encoder.ObjectEncoder;
import Results.SinglePersonResult;
import Services.SinglePersonService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.SQLException;

public class SinglePersonHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        String personID;
        if (exchange.getRequestMethod().toLowerCase().equals("get")) {
            Headers reqHeaders = exchange.getRequestHeaders();
            String spResultString = "";
            if (reqHeaders.containsKey("Authorization")) {
                String authToken = reqHeaders.getFirst("Authorization");
                SinglePersonService singlePersonService = new SinglePersonService();
                String urlPath = exchange.getRequestURI().toString();
                String[] tokens = urlPath.split("/");
                personID = tokens[2];
                SinglePersonResult spResult = null;
                try {
                    spResult = singlePersonService.getSinglePerson(personID, authToken);
                } catch (DataAccessException | SQLException e) {
                    e.printStackTrace();
                }
                spResultString = ObjectEncoder.Serialize(spResult);
                assert spResult != null;
                success = spResult.isSuccess();
            }
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
            }
            OutputStream output = exchange.getResponseBody();
            writeString(spResultString, output);
            exchange.getResponseBody().close();
        }
    }
    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}
