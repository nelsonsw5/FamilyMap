package Handlers;

import DataAccess.DataAccessException;
import Encoder.ObjectEncoder;
import Requests.LoadRequest;
import Results.LoadResult;
import Services.ClearService;
import Services.LoadService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.SQLException;

public class LoadHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                LoadService loadService = new LoadService();
                // first, clear all the tables in the database
                new ClearService().clear();
                InputStream reqBody = exchange.getRequestBody();
                String reqBodyString = readString(reqBody);

                LoadRequest loadRequest = ObjectEncoder.deserialize(reqBodyString, LoadRequest.class);
                LoadResult loadResult = loadService.load(loadRequest);
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                String loadResultString = ObjectEncoder.Serialize(loadResult);
                OutputStream output = exchange.getResponseBody();
                writeString(loadResultString, output);
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
    public String readString(InputStream is) throws IOException {
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
