package Handlers;

import DataAccess.DataAccessException;
import Encoder.ObjectEncoder;
import Results.AllEventsResult;
import Results.AllPersonsResult;
import Services.AllEventsService;
import Services.AllPersonsService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.Locale;

public class AllEventsHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        String eventID;
        String allEventsResultString = "";
        if (exchange.getRequestMethod().toLowerCase().equals("get")) {
            Headers reqHeaders = exchange.getRequestHeaders();
            if (reqHeaders.containsKey("Authorization")) {
                String authToken = reqHeaders.getFirst("Authorization");
                AllEventsService allEventsService = new AllEventsService();
                String urlPath = exchange.getRequestURI().toString();
                AllEventsResult allEventsResult = null;
                try {
                    allEventsResult = allEventsService.getAllEvents(authToken);
                } catch (SQLException | DataAccessException throwables) {
                    throwables.printStackTrace();
                }
                allEventsResultString = ObjectEncoder.Serialize(allEventsResult);
                assert allEventsResult != null;
                success = allEventsResult.isSuccess();
            }
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);

            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
            }

            OutputStream output = exchange.getResponseBody();
            writeString(allEventsResultString, output);
//            output.close();
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
