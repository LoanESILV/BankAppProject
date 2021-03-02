package com.loan.bankapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AccountsListActivity extends AppCompatActivity {

    private ListView listView;
    private static final ArrayList<String> accountsName = new ArrayList<String>();
    private static final ArrayList<String> accountsIBAN = new ArrayList<String>();
    private static final ArrayList<String> accountsAmount = new ArrayList<String>();
    private static final ArrayList<String> accountsCurrency = new ArrayList<String>();

    Button buttonRefresh;
    Button buttonDisconnect;

    DatabaseHandler databaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_accounts);

        buttonRefresh = findViewById(R.id.buttonRefresh);
        buttonDisconnect = findViewById(R.id.buttonDisconnect);

        listView = findViewById(R.id.listView);

        databaseHandler = new DatabaseHandler(this);

        refreshAccounts();

        buttonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.removeAllViewsInLayout();
                finish();
            }
        });

        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshAccounts();
                Toast.makeText(AccountsListActivity.this, "Accounts list refreshed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        ArrayList<String> aName;
        ArrayList<String> aIBAN;
        ArrayList<String> aAmount;
        ArrayList<String> aCurrency;

        MyAdapter (Context c, ArrayList<String> name, ArrayList<String> iban, ArrayList<String> amount, ArrayList<String> currency) {
            super(c, R.layout.list_row, R.id.textViewAccountName, name);
            this.context = c;
            this.aName = name;
            this.aIBAN = iban;
            this.aAmount = amount;
            this.aCurrency = currency;

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.list_row, parent, false);
            TextView myAccountName = row.findViewById(R.id.textViewAccountName);
            TextView myIBAN = row.findViewById(R.id.textViewIBAN);
            TextView myAmount = row.findViewById(R.id.textViewAmount);
            TextView myCurrency = row.findViewById(R.id.textViewCurrency);

            myAccountName.setText(aName.get(position));
            myIBAN.setText(aIBAN.get(position));
            myAmount.setText(aAmount.get(position));
            myCurrency.setText(aCurrency.get(position));

            return row;
        }
    }

    @Override
    public void onBackPressed(){
        listView.removeAllViewsInLayout();
        finish();
    }

    public void refreshAccounts(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://60102f166c21e10017050128.mockapi.io/").addConverterFactory(GsonConverterFactory.create()).build();

        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<Account>> call = jsonPlaceHolderApi.getAccounts();

        call.enqueue(new Callback<List<Account>>() {
            @Override
            public void onResponse(Call<List<Account>> call, Response<List<Account>> response) {
                accountsName.clear();
                accountsIBAN.clear();
                accountsAmount.clear();
                accountsCurrency.clear();

                if(!response.isSuccessful()){
                    Toast.makeText(AccountsListActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Account> accounts = response.body();

                for(Account account : accounts){

                    accountsName.add(account.getAccountName());
                    accountsIBAN.add(account.getIban());
                    accountsAmount.add(account.getAmount());
                    accountsCurrency.add(account.getCurrency());

                    databaseHandler.insertData(account);

                    MyAdapter myAdapter = new MyAdapter(AccountsListActivity.this, accountsName, accountsIBAN, accountsAmount, accountsCurrency);
                    listView.setAdapter(myAdapter);

                }

            }

            @Override
            public void onFailure(Call<List<Account>> call, Throwable t) {
                Toast.makeText(AccountsListActivity.this, "Can't establish connection, last update of your accounts are loaded. Please check your internet connection...", Toast.LENGTH_SHORT).show();

                ArrayList<String> accountsNameOffline = new ArrayList<String>();
                ArrayList<String> accountsIBANOffline = new ArrayList<String>();
                ArrayList<String> accountsAmountOffline = new ArrayList<String>();
                ArrayList<String> accountsCurrencyOffline = new ArrayList<String>();

                List<Account> accounts = databaseHandler.readData();

                for(Account account : accounts){
                    accountsNameOffline.add(account.getAccountName());
                    accountsIBANOffline.add(account.getIban());
                    accountsAmountOffline.add(account.getAmount());
                    accountsCurrencyOffline.add(account.getCurrency());

                    MyAdapter myAdapter = new MyAdapter(AccountsListActivity.this, accountsNameOffline, accountsIBANOffline, accountsAmountOffline, accountsCurrencyOffline);
                    listView.setAdapter(myAdapter);
                }

            }
        });
    }
}