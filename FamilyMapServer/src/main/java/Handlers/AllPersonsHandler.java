package Handlers;

import DataAccess.DataAccessException;
import Encoder.ObjectEncoder;
import Results.AllPersonsResult;
import Results.SinglePersonResult;
import Services.AllPersonsService;
import Services.SinglePersonService;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.SQLException;

public class AllPersonsHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        String personID;
        String allPersonsResultString = "";
        if (exchange.getRequestMethod().toLowerCase().equals("get")) {
            Headers reqHeaders = exchange.getRequestHeaders();
            if (reqHeaders.containsKey("Authorization")) {
                String authToken = reqHeaders.getFirst("Authorization");
                AllPersonsService allPersonsService = new AllPersonsService();
                String urlPath = exchange.getRequestURI().toString();
                AllPersonsResult allPersonsResult = null;
                try {
                    allPersonsResult = allPersonsService.getAllPersons(authToken);
                } catch (SQLException | DataAccessException throwables) {
                    throwables.printStackTrace();
                }

                allPersonsResultString = ObjectEncoder.Serialize(allPersonsResult);
                success = allPersonsResult.isSuccess();
            }
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
            }
            OutputStream output = exchange.getResponseBody();
            writeString(allPersonsResultString, output);
//                output.close();
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
