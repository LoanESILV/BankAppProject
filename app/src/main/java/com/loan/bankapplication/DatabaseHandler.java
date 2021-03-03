package com.loan.bankapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME="AccountsDB";

    private static final String TABLE_NAME = "AccountsTable";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_ACCOUNT_NAME = "accountName";
    private static final String COLUMN_AMOUNT = "amount";
    private static final String COLUMN_IBAN = "iban";
    private static final String COLUMN_CURRENCY = "currency";

    public DatabaseHandler(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +  COLUMN_ACCOUNT_NAME + " VARCHAR(256), " + COLUMN_AMOUNT + " VARCHAR(256), " + COLUMN_CURRENCY + " VARCHAR(256), " + COLUMN_IBAN + " VARCHAR(256))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE " + TABLE_NAME);
        this.onCreate(db);
    }

    public void insertData(Account account){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ACCOUNT_NAME, account.getAccountName());
        cv.put(COLUMN_AMOUNT, account.getAmount());
        cv.put(COLUMN_CURRENCY, account.getCurrency());
        cv.put(COLUMN_IBAN, account.getIban());

        db.insert(TABLE_NAME, null, cv);
        db.close();
    }

    public void cleanData(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("DROP TABLE " + TABLE_NAME);

        db.execSQL("CREATE TABLE " + TABLE_NAME + "( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +  COLUMN_ACCOUNT_NAME + " VARCHAR(256), " + COLUMN_AMOUNT + " VARCHAR(256), " + COLUMN_CURRENCY + " VARCHAR(256), " + COLUMN_IBAN + " VARCHAR(256))");
    }

    public List<Account> readData(){
        List<Account> accountsList = new ArrayList<Account>();

        String selectQuery = "SELECT " + COLUMN_ACCOUNT_NAME + ", " + COLUMN_AMOUNT + ", " + COLUMN_CURRENCY + ", " + COLUMN_IBAN + " FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Account account = new Account();
                account.setAccountName(cursor.getString(0));
                account.setAmount(cursor.getString(1));
                account.setCurrency(cursor.getString(2));
                account.setIban(cursor.getString(3));
                accountsList.add(account);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return accountsList;
    }
}
