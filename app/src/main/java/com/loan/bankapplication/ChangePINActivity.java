package com.loan.bankapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigInteger;

public class ChangePINActivity extends AppCompatActivity {

    Button buttonSave;
    EditText editTextChangePIN;
    EditText editTextConfirmPIN;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        buttonSave = findViewById(R.id.buttonSave);
        editTextChangePIN = findViewById(R.id.editTextChangePIN);
        editTextConfirmPIN = findViewById(R.id.editTextConfirmPIN);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if(editTextChangePIN.getText().toString().equals(editTextConfirmPIN.getText().toString()) && editTextChangePIN.getText().toString().length() == 4){
                     byte[] md5Input = editTextChangePIN.getText().toString().getBytes();
                     BigInteger md5Data = null;

                     try {
                         md5Data = new BigInteger(1,md5.encryptMD5(md5Input));
                     } catch (Exception e) {
                         e.printStackTrace();
                     }

                     String md5Str = md5Data.toString(16);

                     if(md5Str.length()<32){
                         md5Str = 0 + md5Str;
                     }

                     Intent intent = new Intent();
                     intent.putExtra("newPIN", md5Str);
                     setResult(1,intent);
                     finish();
                 }
                 else{
                     Toast.makeText(getApplicationContext(), "PIN doesn't contain 4 numbers or it and its confirmation are different", Toast.LENGTH_SHORT).show();
                 }
            }
        });
    }
}
