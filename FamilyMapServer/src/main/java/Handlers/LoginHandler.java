package Handlers;

import DataAccess.DataAccessException;
import Encoder.ObjectEncoder;
import Requests.LoginRequest;
import Results.LoginResult;
import Services.LoginService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpURLConnection;
import java.sql.SQLException;

public class LoginHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String loginResultString = "";
        boolean success = false;
        try {
            if (exchange.getRequestMethod().toLowerCase().equals("post")) {
                LoginService loginService = new LoginService();
                InputStream inputStream = exchange.getRequestBody();
                String inputStreamString = readString(inputStream);
                LoginRequest loginRequest = ObjectEncoder.deserialize(inputStreamString,LoginRequest.class);
                LoginResult loginResult = loginService.login(loginRequest);
                loginResultString = ObjectEncoder.Serialize(loginResult);
                success = loginResult.isSuccess();
            }
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST, 0);
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);
            }
            OutputStream output = exchange.getResponseBody();
            writeString(loginResultString, output);
            exchange.getResponseBody().close();
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
