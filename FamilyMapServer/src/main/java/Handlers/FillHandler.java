package Handlers;

import DataAccess.DataAccessException;
import Encoder.ObjectEncoder;
import Requests.FillRequest;
import Requests.LoginRequest;
import Results.FillResult;
import Results.LoginResult;
import Services.ClearService;
import Services.FillService;
import Services.LoginService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.SQLException;

public class FillHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        int generations = 4;
        String username;

        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                FillService fillService = new FillService();
                InputStream inputStream = exchange.getRequestBody();
                String inputStreamString = readString(inputStream);
                String urlPath = exchange.getRequestURI().toString();
                String delim = "[/]+";
                String[] tokens = urlPath.split(delim);
                if (tokens.length == 4) {
                    username = tokens[2];
                    generations = Integer.valueOf(tokens[3]);
                }
                else {
                    username = tokens[2];
                }
                FillRequest fillRequest = new FillRequest(username,generations,null);
                FillResult fillResult = fillService.fill(fillRequest);
                if (fillResult == null) {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                }
                else {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
                }
                String fillResultString = ObjectEncoder.Serialize(fillResult);
                OutputStream output = exchange.getResponseBody();
                writeString(fillResultString, output);
                output.close();
                success = true;
            }
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
                exchange.getResponseBody().close();
            }
        }
        catch (IOException | SQLException | DataAccessException e) {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            exchange.getResponseBody().close();
            e.printStackTrace();
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
