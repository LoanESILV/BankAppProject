package com.loan.bankapplication;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JsonPlaceHolderApi {

    @GET("labbbank/accounts")
    Call<List<Account>> getAccounts();

    @GET("labbbank/config")
    Call<List<Account>> getConfig();
}
