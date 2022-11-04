package com.example.familymapclient;

import com.example.familymapclient.net.ServerProxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Requests.LoginRequest;
import Requests.RegisterRequest;

import static org.junit.jupiter.api.Assertions.*;

class ServerProxyTests {
    String serverHost = "localhost";
    String serverPort = "8000";

    LoginRequest lr1;
    LoginRequest lr2;

    RegisterRequest rr1;
    RegisterRequest rr2;

    @BeforeEach
    void setUp() {
        ServerProxy.clear(serverHost,serverPort);

        rr1 = new RegisterRequest("sheila","parker","sparker@gmail.com",
                "sheila","parker","f");
        rr2 = new RegisterRequest("john","doe","jdoe@gmail.com",
                "john","doe","m");

        lr1 = new LoginRequest("sheila","parker");
        lr2 = new LoginRequest("john","doe");
    }

    @Test
    @DisplayName("Login Pass")
    void loginPass() {
        assertFalse(ServerProxy.login(serverHost,serverPort,lr1).isSuccess());
        ServerProxy.register(serverHost,serverPort,rr1);
        assertTrue(ServerProxy.login(serverHost,serverPort,lr1).isSuccess());
    }

    @Test
    @DisplayName("Login Fail")
    void loginFail() {
        assertFalse(ServerProxy.login(serverHost,serverPort,lr2).isSuccess());
        ServerProxy.register(serverHost,serverPort,rr2);
        assertFalse(ServerProxy.login(serverHost,serverPort,lr1).isSuccess());
    }

    @Test
    @DisplayName("Register Pass")
    void registerPass() {
        assertTrue(ServerProxy.register(serverHost,serverPort,rr1).isSuccess());
        assertTrue(ServerProxy.register(serverHost,serverPort,rr2).isSuccess());
    }

    @Test
    @DisplayName("Register Pass")
    void registerFail() {
        assertTrue(ServerProxy.register(serverHost,serverPort,rr1).isSuccess());
        assertFalse(ServerProxy.register(serverHost,serverPort,rr1).isSuccess());
    }

    @Test
    @DisplayName("Get All Persons Pass")
    void getAllPersonsPass() {
        String authToken = ServerProxy.register(serverHost,serverPort,rr1).getAuthtoken();
        assertTrue(ServerProxy.getAllPersons(serverHost,serverPort,authToken).isSuccess());
        assertEquals(ServerProxy.getAllPersons(serverHost,serverPort,authToken).getData()[0]
                .getAssociatedUsername(),rr1.getUsername());
    }

    @Test
    @DisplayName("Get All Persons Fail")
    void getAllPersonsFail() {
        String authToken = ServerProxy.register(serverHost,serverPort,rr1).getAuthtoken();
        String fakeAuthToken = "post_malone";
        assertFalse(ServerProxy.getAllPersons(serverHost,serverPort,fakeAuthToken).isSuccess());
    }

    @Test
    @DisplayName("Get All Events Pass")
    void getAllEventsPass() {
        String authToken = ServerProxy.register(serverHost,serverPort,rr1).getAuthtoken();
        assertTrue(ServerProxy.getAllEvents(serverHost,serverPort,authToken).isSuccess());
        assertEquals(ServerProxy.getAllEvents(serverHost,serverPort,authToken).getData()[0]
                .getAssociatedUsername(),rr1.getUsername());
    }

    @Test
    @DisplayName("Get All Events Fail")
    void getAllEventsFail() {
        String authToken = ServerProxy.register(serverHost,serverPort,rr2).getAuthtoken();
        String fakeAuthToken = "post_malone";
        assertFalse(ServerProxy.getAllEvents(serverHost,serverPort,fakeAuthToken).isSuccess());
    }

}