package com.example.familymapclient.net;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import Requests.RegisterRequest;
import Results.AllEventsResult;
import Results.AllPersonsResult;
import Results.RegisterResult;

public class RegisterTask implements Runnable {
    String serverHost;
    String serverPort;
    RegisterRequest rr;
    Handler messageHandler;

    public RegisterTask (String serverHost, String serverPort, RegisterRequest rr, Handler messageHandler) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.rr = rr;
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        RegisterResult registerResult = ServerProxy.register(serverHost, serverPort, rr);
        assert registerResult != null;
        if (registerResult.isSuccess()) {
            AllEventsResult allEventsResult = ServerProxy.getAllEvents(serverHost,serverPort,registerResult.getAuthtoken());
            AllPersonsResult allPersonsResult = ServerProxy.getAllPersons(serverHost,serverPort,registerResult.getAuthtoken());
            DataCache.getDataCache().setCurrentUserPersonID(registerResult.getPersonID());
            DataCache.getDataCache().fillEventMap(allEventsResult);
            DataCache.getDataCache().fillEventColors(allEventsResult);
            DataCache.getDataCache().fillPeopleMap(allPersonsResult);
            DataCache.getDataCache().fillPeopleEvents(allEventsResult);
            DataCache.getDataCache().initialMapCopyFill();
        }
        sendMessage(registerResult);
    }

    public void sendMessage(RegisterResult registerResult) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        if (registerResult.isSuccess()) {
            messageBundle.putString("Message","Register Successful: " + registerResult.getUsername());
            messageBundle.putBoolean("Success",true);
        }
        else {
            messageBundle.putString("Message","Invalid Registration");
            messageBundle.putBoolean("Success",false);
        }
        message.setData(messageBundle);
        messageHandler.sendMessage(message);
    }

}
