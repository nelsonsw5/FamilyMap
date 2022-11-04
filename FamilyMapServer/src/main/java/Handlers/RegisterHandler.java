package Handlers;

import DataAccess.DataAccessException;
import Encoder.ObjectEncoder;
import Requests.FillRequest;
import Requests.LoginRequest;
import Requests.RegisterRequest;
import Results.FillResult;
import Results.LoginResult;
import Results.RegisterResult;
import Services.FillService;
import Services.LoginService;
import Services.RegisterService;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.net.HttpURLConnection;
import java.sql.SQLException;
import java.util.Locale;

public class RegisterHandler implements HttpHandler {
    private static final int DEFAULT_NUM_GENERATIONS = 4;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean success = false;
        try {
            String jsonResult = "";
            if (exchange.getRequestMethod().toLowerCase(Locale.ROOT).equals("post")) {
                RegisterService registerService = new RegisterService();
                FillService fillService = new FillService();
                InputStream reqBody = exchange.getRequestBody();
                String reqBodyString = readString(reqBody);
                RegisterRequest rrequest = ObjectEncoder.deserialize(reqBodyString, RegisterRequest.class);
                RegisterResult rresult = registerService.register(rrequest);
                if (rresult.isSuccess()) {
                    FillRequest fillRequest = new FillRequest(rresult.getUsername(), DEFAULT_NUM_GENERATIONS, rresult.getPersonID());
                    FillResult fillResult = fillService.fill(fillRequest);
                }
                jsonResult = ObjectEncoder.Serialize(rresult);
                success = rresult.isSuccess();
            }
            if (!success) {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_BAD_REQUEST,0);
            }
            else {
                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            }
            OutputStream respBody = exchange.getResponseBody();
            writeString(jsonResult, respBody);
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
