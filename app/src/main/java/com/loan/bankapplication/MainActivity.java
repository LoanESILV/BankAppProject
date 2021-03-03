package com.loan.bankapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import androidx.biometric.BiometricPrompt;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    EditText inputPIN;
    Button buttonConnect;
    Button buttonBiometric;
    String pinValue = "0000";
    Boolean isFirstLogin = true;

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputPIN = findViewById(R.id.editTextPIN);
        buttonConnect = findViewById(R.id.buttonConnect);
        buttonBiometric = findViewById(R.id.buttonBiometric);

        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                connect();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric Authentication")
                .setSubtitle("Please use your finger to login")
                .setNegativeButtonText("Use secret PIN")
                .build();

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pinVerification(inputPIN.getText().toString())){
                    if(isFirstLogin){
                        changePIN();
                    }
                    else{
                        connect();
                    }
                }
                else{
                    Toast.makeText(getApplicationContext(), "Wrong PIN", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonBiometric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFirstLogin){
                    Toast.makeText(getApplicationContext(), "Can't use biometric authentication at your first login", Toast.LENGTH_SHORT).show();
                }
                else{
                    biometricPrompt.authenticate(promptInfo);
                }
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        inputPIN.setText("");
    }

    public void connect(){
        Intent intent = new Intent(this, AccountsListActivity.class);
        startActivity(intent);
    }

    public void changePIN(){
        Intent intent = new Intent(this, ChangePINActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        if(requestCode == 1){
            pinValue = data.getStringExtra("newPIN");
            isFirstLogin = false;
            connect();
        }
    }

    public Boolean pinVerification(String input) {
        if (isFirstLogin) {
            if (input.equals(pinValue)) {
                return true;
            } else {
                return false;
            }
        } else {
            byte[] md5Input = input.getBytes();
            BigInteger md5Data = null;

            try {
                md5Data = new BigInteger(1, md5.encryptMD5(md5Input));
            } catch (Exception e) {
                e.printStackTrace();
            }

            String md5Str = md5Data.toString(16);

            if (md5Str.length() < 32) {
                md5Str = 0 + md5Str;
            }

            if (md5Str.equals(pinValue)) {
                return true;
            }
            else {
                return false;
            }
        }
    }
}