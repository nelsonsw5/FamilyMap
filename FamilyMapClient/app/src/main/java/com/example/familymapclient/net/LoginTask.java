package com.example.familymapclient.net;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import Requests.LoginRequest;
import Results.AllEventsResult;
import Results.AllPersonsResult;
import Results.LoginResult;

public class LoginTask implements Runnable {
    String serverHost;
    String serverPort;
    LoginRequest lr;
    Handler messageHandler;


    public LoginTask (String serverHost, String serverPort, LoginRequest lr, Handler messageHandler) {
        this.serverHost = serverHost;
        this.serverPort = serverPort;
        this.lr = lr;
        this.messageHandler = messageHandler;
    }

    @Override
    public void run() {
        LoginResult loginResult = ServerProxy.login(serverHost, serverPort, lr);
        assert loginResult != null;
        if (loginResult.isSuccess()) {
            AllEventsResult allEventsResult = ServerProxy.getAllEvents(serverHost,serverPort,
                    loginResult.getAuthtoken());
            AllPersonsResult allPersonsResult = ServerProxy.getAllPersons(serverHost,serverPort,
                    loginResult.getAuthtoken());
            DataCache.getDataCache().setCurrentUserPersonID(loginResult.getPersonID());
            assert allEventsResult != null;
            DataCache.getDataCache().fillEventMap(allEventsResult);
            DataCache.getDataCache().fillEventColors(allEventsResult);
            assert allPersonsResult != null;
            DataCache.getDataCache().fillPeopleMap(allPersonsResult);
            DataCache.getDataCache().fillPeopleEvents(allEventsResult);
            DataCache.getDataCache().initialMapCopyFill();
        }
        sendMessage(loginResult);
    }

    public void sendMessage(LoginResult loginResult) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        if (loginResult.isSuccess()) {
            messageBundle.putString("Message","Login Successful " + loginResult.getUsername());
            messageBundle.putBoolean("Success",true);
        }
        else {
            messageBundle.putString("Message","Invalid Login");
            messageBundle.putBoolean("Success",false);
        }
        message.setData(messageBundle);
        messageHandler.sendMessage(message);
    }
}
