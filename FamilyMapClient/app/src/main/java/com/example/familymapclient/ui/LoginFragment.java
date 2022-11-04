package com.example.familymapclient.ui;

import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.familymapclient.R;
import com.example.familymapclient.net.LoginTask;
import com.example.familymapclient.net.RegisterTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Requests.LoginRequest;
import Requests.RegisterRequest;

public class LoginFragment extends MapFragment {

    Button loginButton;
    Button registerButton;

    EditText serverHost;
    EditText serverPort;
    EditText username;
    EditText password;
    EditText firstName;
    EditText lastName;
    EditText email;

    RadioButton genderFemale;
    RadioButton genderMale;

    String serverHostString = "";
    String serverPortString = "";
    String usernameString = "";
    String passwordString = "";
    String firstNameString = "";
    String lastNameString = "";
    String emailString = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        serverHost = view.findViewById(R.id.serverHost);
        serverPort = view.findViewById(R.id.serverPort);
        username = view.findViewById(R.id.username);
        password = view.findViewById(R.id.password);
        firstName = view.findViewById(R.id.firstName);
        lastName = view.findViewById(R.id.lastName);
        email = view.findViewById(R.id.email);
        genderMale = view.findViewById(R.id.genderMale);
        genderFemale = view.findViewById(R.id.genderFemale);
        genderFemale.setChecked(true);

        serverHost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                serverHostString = s.toString();
                EnableLogin();
                EnableRegister();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        serverPort.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                serverPortString = s.toString();
                EnableLogin();
                EnableRegister();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                usernameString = s.toString();
                EnableLogin();
                EnableRegister();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwordString = s.toString();
                EnableLogin();
                EnableRegister();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                firstNameString = s.toString();
                EnableLogin();
                EnableRegister();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                lastNameString = s.toString();
                EnableLogin();
                EnableRegister();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emailString = s.toString();
                EnableLogin();
                EnableRegister();
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        Handler uiThreadMessageHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage (Message m) {
               Bundle bundle = m.getData();
               String message = bundle.getString("Message");
               boolean isSuccess = bundle.getBoolean("Success",true);
               Toast.makeText(getContext(),message,Toast.LENGTH_LONG).show();
               if (isSuccess) {
                   ((MainActivity) requireActivity()).switchFragment();
               }
            }
        };

        loginButton = view.findViewById(R.id.login);
        loginButton.setOnClickListener(v -> {
            serverHostString = serverHost.getText().toString();
            serverPortString = serverPort.getText().toString();
            usernameString = username.getText().toString();
            passwordString = password.getText().toString();
            LoginRequest loginRequest = new LoginRequest(usernameString, passwordString);
            LoginTask loginTask = new LoginTask(serverHostString, serverPortString,
                    loginRequest, uiThreadMessageHandler);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(loginTask);
        });

        registerButton = view.findViewById(R.id.register);
        registerButton.setOnClickListener(v -> {
            serverHostString = serverHost.getText().toString();
            serverPortString = serverPort.getText().toString();
            usernameString = username.getText().toString();
            passwordString = password.getText().toString();
            emailString = email.getText().toString();
            firstNameString = firstName.getText().toString();
            lastNameString = lastName.getText().toString();
            String genderString;
            if (genderFemale.isChecked()) { genderString = "f"; }
            else { genderString = "m"; }
            RegisterRequest registerRequest = new RegisterRequest(usernameString,
                    passwordString, emailString, firstNameString, lastNameString, genderString);
            RegisterTask registerTask = new RegisterTask(serverHostString, serverPortString,
                    registerRequest, uiThreadMessageHandler);
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(registerTask);
        });
        return view;
    }

    private void EnableLogin() {
        loginButton.setEnabled(usernameString.length() > 0 && serverPortString.length() > 0 &&
                serverHostString.length() > 0 && passwordString.length() > 0);
    }
    private void EnableRegister() {
        registerButton.setEnabled(serverHostString.length() > 0 && serverPortString.length() > 0 &&
                usernameString.length() > 0 && passwordString.length() > 0 &&
                firstNameString.length() > 0 && lastNameString.length() > 0 &&
                emailString.length() > 0);
    }
}